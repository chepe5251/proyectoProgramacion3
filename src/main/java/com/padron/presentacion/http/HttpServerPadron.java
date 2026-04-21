package com.padron.presentacion.http;

import com.padron.dto.FormatoSalida;
import com.padron.dto.RespuestaPadron;
import com.padron.dto.SolicitudPadron;
import com.padron.logica.ServicioPadron;
import com.padron.util.Serializador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Servidor HTTP mínimo (raw sockets) para consultas al padrón.
 *
 * Endpoint: GET /padron?cedula=109870456&formato=json HTTP/1.1
 * Respuesta: HTTP/1.1 200 OK con Content-Type application/json o application/xml
 */
public class HttpServerPadron {
    private static final int TAMANO_POOL = 20;

    private final int            puerto;
    private final ServicioPadron servicio;
    private final Serializador   serializador;
    private final ExecutorService poolClientes;

    private ServerSocket serverSocket;
    private boolean      corriendo = false;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(20);

    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------

    public HttpServerPadron(int puerto, ServicioPadron servicio, Serializador serializador) {
        this.puerto       = puerto;
        this.servicio     = servicio;
        this.serializador = serializador;
        this.poolClientes = Executors.newFixedThreadPool(TAMANO_POOL);
    }

    // ---------------------------------------------------------------
    // Ciclo de vida
    // ---------------------------------------------------------------

    /**
     * Inicia el servidor HTTP y entra al loop de aceptar conexiones.
     *
     * @throws IOException si no se puede abrir el puerto
     */
    public void iniciar() throws IOException {
        serverSocket = new ServerSocket(puerto);
        corriendo = true;
        System.out.println("Servidor HTTP escuchando en puerto " + puerto);
        while (corriendo) {
            try {
                Socket cliente = serverSocket.accept();
                threadPool.execute(() -> manejarCliente(cliente));
            } catch (IOException e) {
                if (!corriendo) break;
                System.err.println("Error aceptando conexion HTTP: " + e.getMessage());
            }
        }
    }

    /**
     * Detiene el servidor cerrando el ServerSocket.
     */
    public void detener() {
        corriendo = false;
        threadPool.shutdown();
        if (serverSocket != null && !serverSocket.isClosed()) {
            try { serverSocket.close(); } catch (IOException ignored) {}
        }
        poolClientes.shutdown();
        try {
            if (!poolClientes.awaitTermination(5, TimeUnit.SECONDS)) {
                poolClientes.shutdownNow();
            }
        } catch (InterruptedException e) {
            poolClientes.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    // ---------------------------------------------------------------
    // Manejo de requests
    // ---------------------------------------------------------------

    /**
     * Lee y responde una request HTTP desde el socket en su propio Thread.
     *
     * @param cliente socket del cliente conectado
     */
    private void manejarCliente(Socket cliente) {
        try (
            BufferedReader entrada = new BufferedReader(
                new InputStreamReader(cliente.getInputStream()));
            PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true)
        ) {
            // Leer primera línea: "GET /padron?cedula=X&formato=Y HTTP/1.1"
            String requestLine = entrada.readLine();
            if (requestLine == null || requestLine.isBlank()) return;

            if (!requestLine.startsWith("GET")) {
                salida.print(construirRespuestaHttp(405, "text/plain", "Metodo No Permitido"));
                salida.flush();
                return;
            }
            
            // Consumir headers hasta línea vacía (requerido por protocolo HTTP)
            String headerLine;
            while ((headerLine = entrada.readLine()) != null && !headerLine.isBlank()) {}

            // Extraer query string
            String queryString = "";
            SolicitudPadron solicitud;
            
            if (requestLine.contains("?")) {
                queryString = requestLine.split("\\?")[1].split(" ")[0];
                solicitud = parsearParametros(queryString);
            } else if (requestLine.contains("/padron/")) {
                // Soporte para /padron/{cedula}
                String path = requestLine.split(" ")[1].split("\\?")[0];
                String cedula = path.substring(path.lastIndexOf("/") + 1);
                solicitud = new SolicitudPadron(cedula, FormatoSalida.JSON);
            } else {
                solicitud = parsearParametros("");
            }

            RespuestaPadron respuesta = servicio.consultarPadron(solicitud);

            String contentType = solicitud.getFormato() == FormatoSalida.XML
                               ? "application/xml" : "application/json";
            String body        = serializador.serializar(respuesta, solicitud.getFormato());
            int    status      = obtenerStatusHttp(respuesta);

            salida.print(construirRespuestaHttp(status, contentType, body));
            salida.flush();
        } catch (IOException e) {
            System.err.println("Error atendiendo cliente HTTP: " + e.getMessage());
        } finally {
            try { cliente.close(); } catch (IOException ignored) {}
        }
    }

    /**
     * Extrae los parámetros de la query string de una URL.
     *
     * @param queryString la parte "cedula=...&formato=..." de la URL
     * @return            SolicitudPadron con los parámetros extraídos
     */
    private SolicitudPadron parsearParametros(String queryString) {
        SolicitudPadron solicitud = new SolicitudPadron();
        if (queryString == null || queryString.isBlank()) return solicitud;
        for (String par : queryString.split("&")) {
            String[] kv = par.split("=", 2);
            if (kv.length < 2) continue;
            switch (kv[0].trim()) {
                case "cedula":  solicitud.setCedula(kv[1].trim()); break;
                case "formato":
                    try { solicitud.setFormato(FormatoSalida.fromString(kv[1].trim())); }
                    catch (IllegalArgumentException e) { /* mantener default JSON */ }
                    break;
            }
        }
        return solicitud;
    }

    /**
     * Construye una respuesta HTTP completa como String.
     */
    private String construirRespuestaHttp(int statusCode, String contentType, String body) {
        String statusText = statusCode == 200 ? "OK"
                          : statusCode == 400 ? "Bad Request"
                          : statusCode == 404 ? "Not Found"
                          : "Internal Server Error";
        return "HTTP/1.1 " + statusCode + " " + statusText + "\r\n"
             + "Content-Type: " + contentType + "; charset=UTF-8\r\n"
             + "Content-Length: " + body.getBytes(StandardCharsets.UTF_8).length + "\r\n"
             + "Connection: close\r\n"
             + "\r\n"
             + body;
    }

    private int obtenerStatusHttp(RespuestaPadron respuesta) {
        if (!respuesta.esError()) {
            return 200;
        }

        try {
            return Integer.parseInt(respuesta.getCodigoError());
        } catch (NumberFormatException e) {
            return 400;
        }
    }

    // ---------------------------------------------------------------
    // Getters de estado
    // ---------------------------------------------------------------

    public boolean isCorriendo() { return corriendo; }
    public int     getPuerto()   { return puerto; }
}
