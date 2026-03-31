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

/**
 * Servidor HTTP mínimo (raw sockets) para consultas al padrón.
 *
 * RAMA:  feature/http
 * OWNER: Desarrollador 4
 *
 * Endpoint esperado:
 *   GET /padron?cedula=109870456&formato=json HTTP/1.1
 *
 * Respuesta:
 *   HTTP/1.1 200 OK
 *   Content-Type: application/json
 *   <CUERPO JSON o XML>
 *
 * TODO (feature/http):
 *  - Implementar iniciar()
 *  - Implementar manejarCliente() que parsee la request HTTP
 *  - Implementar parsearParametros() para extraer query params
 *  - Implementar construirRespuestaHttp() con headers correctos
 *  - Retornar 400 Bad Request si faltan parámetros
 *  - Retornar 404 Not Found si la cédula no existe
 *  - Retornar 500 Internal Server Error en caso de excepción
 */
public class HttpServerPadron {

    private final int            puerto;
    private final ServicioPadron servicio;
    private final Serializador   serializador;

    private ServerSocket serverSocket;
    private boolean      corriendo = false;

    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------

    public HttpServerPadron(int puerto, ServicioPadron servicio, Serializador serializador) {
        this.puerto       = puerto;
        this.servicio     = servicio;
        this.serializador = serializador;
    }

    // ---------------------------------------------------------------
    // Ciclo de vida
    // ---------------------------------------------------------------

    /**
     * Inicia el servidor HTTP y entra al loop de aceptar conexiones.
     *
     * TODO (feature/http): implementar este método.
     *  1. Crear ServerSocket en el puerto configurado
     *  2. Loop: aceptar Socket → lanzar Thread → manejarCliente()
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
                new Thread(() -> manejarCliente(cliente)).start();
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
        if (serverSocket != null && !serverSocket.isClosed()) {
            try { serverSocket.close(); } catch (IOException ignored) {}
        }
    }

    // ---------------------------------------------------------------
    // Manejo de requests
    // ---------------------------------------------------------------

    /**
     * Lee y responde una request HTTP desde el socket.
     * Debe ejecutarse en su propio Thread.
     *
     * TODO (feature/http): implementar este método.
     *  1. Leer la primera línea: "GET /padron?... HTTP/1.1"
     *  2. Leer headers hasta línea vacía (obligatorio en HTTP)
     *  3. Extraer query string y parsear parámetros
     *  4. Llamar servicio.consultarPadron()
     *  5. Escribir respuesta HTTP completa (status line + headers + body)
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

            // Consumir headers hasta línea vacía (requerido por protocolo HTTP)
            String headerLine;
            while ((headerLine = entrada.readLine()) != null && !headerLine.isBlank()) {}

            // Extraer query string
            String queryString = "";
            if (requestLine.contains("?")) {
                queryString = requestLine.split("\\?")[1].split(" ")[0];
            }

            SolicitudPadron solicitud = parsearParametros(queryString);
            RespuestaPadron respuesta = servicio.consultarPadron(solicitud);

            String contentType = solicitud.getFormato() == FormatoSalida.XML
                               ? "application/xml" : "application/json";
            String body        = serializador.serializar(respuesta, solicitud.getFormato());
            int    status      = respuesta.esError() ? 400 : 200;

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
     * TODO (feature/http): implementar este método.
     *  Ejemplo: "/padron?cedula=109870456&formato=xml"
     *           → cedula="109870456", formato="xml"
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
     *
     * TODO (feature/http): implementar este método.
     *  - statusCode: 200, 400, 404, 500
     *  - contentType: "application/json" o "application/xml"
     *  - body: el JSON o XML serializado
     */
    private String construirRespuestaHttp(int statusCode, String contentType, String body) {
        String statusText = statusCode == 200 ? "OK"
                          : statusCode == 400 ? "Bad Request"
                          : statusCode == 404 ? "Not Found"
                          : "Internal Server Error";
        return "HTTP/1.1 " + statusCode + " " + statusText + "\r\n"
             + "Content-Type: " + contentType + "; charset=UTF-8\r\n"
             + "Content-Length: " + body.getBytes().length + "\r\n"
             + "Connection: close\r\n"
             + "\r\n"
             + body;
    }

    // ---------------------------------------------------------------
    // Getters de estado
    // ---------------------------------------------------------------

    public boolean isCorriendo() { return corriendo; }
    public int     getPuerto()   { return puerto; }
}
