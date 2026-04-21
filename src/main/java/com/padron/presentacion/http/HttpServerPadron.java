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
 * Servidor HTTP mÃ­nimo (raw sockets) para consultas al padrÃ³n.
 *
 * Endpoint: GET /padron?cedula=109870456&formato=json HTTP/1.1
 * Respuesta: HTTP/1.1 200 OK con Content-Type application/json o application/xml
 */
public class HttpServerPadron {
    private static final int TAMANO_POOL = 20;
    private static final String ENDPOINT_PADRON = "/padron";

    private final int puerto;
    private final ServicioPadron servicio;
    private final Serializador serializador;
    private final ExecutorService poolClientes;

    private ServerSocket serverSocket;
    private boolean corriendo = false;

    public HttpServerPadron(int puerto, ServicioPadron servicio, Serializador serializador) {
        this.puerto = puerto;
        this.servicio = servicio;
        this.serializador = serializador;
        this.poolClientes = Executors.newFixedThreadPool(TAMANO_POOL);
    }

    public void iniciar() throws IOException {
        serverSocket = new ServerSocket(puerto);
        corriendo = true;
        System.out.println("Servidor HTTP escuchando en puerto " + puerto);
        while (corriendo) {
            try {
                Socket cliente = serverSocket.accept();
                poolClientes.submit(() -> manejarCliente(cliente));
            } catch (IOException e) {
                if (!corriendo) break;
                System.err.println("Error aceptando conexion HTTP: " + e.getMessage());
            }
        }
    }

    public void detener() {
        corriendo = false;
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

    private void manejarCliente(Socket cliente) {
        try (
            BufferedReader entrada = new BufferedReader(
                new InputStreamReader(cliente.getInputStream()));
            PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true)
        ) {
            String requestLine = entrada.readLine();
            if (requestLine == null || requestLine.isBlank()) return;

            String headerLine;
            while ((headerLine = entrada.readLine()) != null && !headerLine.isBlank()) {}

            HttpRequestData requestData = parsearRequestLine(requestLine);
            if (requestData.statusCode != 200) {
                RespuestaPadron error = RespuestaPadron.error(
                    String.valueOf(requestData.statusCode),
                    requestData.errorMessage
                );
                String body = serializador.serializar(error, requestData.formato);
                salida.print(construirRespuestaHttp(requestData.statusCode, contentType(requestData.formato), body));
                salida.flush();
                return;
            }

            SolicitudPadron solicitud = requestData.solicitud;
            RespuestaPadron respuesta = servicio.consultarPadron(solicitud);

            String contentType = contentType(solicitud.getFormato());
            String body = serializador.serializar(respuesta, solicitud.getFormato());
            int status = obtenerStatusHttp(respuesta);

            salida.print(construirRespuestaHttp(status, contentType, body));
            salida.flush();
        } catch (IOException e) {
            System.err.println("Error atendiendo cliente HTTP: " + e.getMessage());
        } finally {
            try { cliente.close(); } catch (IOException ignored) {}
        }
    }

    HttpRequestData parsearRequestLine(String requestLine) {
        String[] partes = requestLine.trim().split("\\s+");
        if (partes.length < 3) {
            return HttpRequestData.error(400, "Request HTTP invalido.");
        }

        String metodo = partes[0].trim();
        String target = partes[1].trim();
        FormatoSalida formato = extraerFormatoDesdeTarget(target);

        if (!"GET".equalsIgnoreCase(metodo)) {
            return HttpRequestData.error(405, "Metodo no permitido. Use GET.", formato);
        }

        String ruta = extraerRuta(target);
        if (!ruta.equals(ENDPOINT_PADRON) && !ruta.startsWith(ENDPOINT_PADRON + "/")) {
            return HttpRequestData.error(404, "Endpoint no encontrado.", formato);
        }

        SolicitudPadron solicitud = parsearParametros(extraerQueryString(target));
        String cedulaEnRuta = extraerCedulaDesdeRuta(ruta);
        if (cedulaEnRuta != null && !cedulaEnRuta.isBlank()) {
            solicitud.setCedula(cedulaEnRuta);
        }

        if (solicitud.getFormato() == null) {
            solicitud.setFormato(FormatoSalida.JSON);
        }

        return HttpRequestData.ok(solicitud);
    }

    SolicitudPadron parsearParametros(String queryString) {
        SolicitudPadron solicitud = new SolicitudPadron();
        if (queryString == null || queryString.isBlank()) return solicitud;
        for (String par : queryString.split("&")) {
            String[] kv = par.split("=", 2);
            if (kv.length < 2) continue;
            switch (kv[0].trim().toLowerCase()) {
                case "cedula":
                    solicitud.setCedula(kv[1].trim());
                    break;
                case "formato":
                case "format":
                    solicitud.setFormato(FormatoSalida.fromString(kv[1].trim()));
                    break;
            }
        }
        return solicitud;
    }

    private String extraerRuta(String target) {
        int indiceQuery = target.indexOf('?');
        return indiceQuery >= 0 ? target.substring(0, indiceQuery) : target;
    }

    private String extraerQueryString(String target) {
        int indiceQuery = target.indexOf('?');
        return indiceQuery >= 0 ? target.substring(indiceQuery + 1) : "";
    }

    private String extraerCedulaDesdeRuta(String ruta) {
        if (ruta == null || !ruta.startsWith(ENDPOINT_PADRON + "/")) {
            return null;
        }

        String cedula = ruta.substring((ENDPOINT_PADRON + "/").length()).trim();
        return cedula.isEmpty() || cedula.contains("/") ? null : cedula;
    }

    private FormatoSalida extraerFormatoDesdeTarget(String target) {
        SolicitudPadron solicitud = parsearParametros(extraerQueryString(target));
        return solicitud.getFormato() != null ? solicitud.getFormato() : FormatoSalida.JSON;
    }

    private String contentType(FormatoSalida formato) {
        return formato == FormatoSalida.XML ? "application/xml" : "application/json";
    }

    private String construirRespuestaHttp(int statusCode, String contentType, String body) {
        String statusText = statusCode == 200 ? "OK"
                          : statusCode == 400 ? "Bad Request"
                          : statusCode == 404 ? "Not Found"
                          : statusCode == 405 ? "Method Not Allowed"
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

    public boolean isCorriendo() { return corriendo; }
    public int getPuerto() { return puerto; }

    static class HttpRequestData {
        final int statusCode;
        final String errorMessage;
        final SolicitudPadron solicitud;
        final FormatoSalida formato;

        private HttpRequestData(int statusCode, String errorMessage, SolicitudPadron solicitud, FormatoSalida formato) {
            this.statusCode = statusCode;
            this.errorMessage = errorMessage;
            this.solicitud = solicitud;
            this.formato = formato != null ? formato : FormatoSalida.JSON;
        }

        static HttpRequestData ok(SolicitudPadron solicitud) {
            return new HttpRequestData(200, null, solicitud, solicitud != null ? solicitud.getFormato() : FormatoSalida.JSON);
        }

        static HttpRequestData error(int statusCode, String errorMessage) {
            return new HttpRequestData(statusCode, errorMessage, null, FormatoSalida.JSON);
        }

        static HttpRequestData error(int statusCode, String errorMessage, FormatoSalida formato) {
            return new HttpRequestData(statusCode, errorMessage, null, formato);
        }
    }
}
