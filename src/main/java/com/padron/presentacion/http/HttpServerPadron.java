package com.padron.presentacion.http;

import com.padron.dto.FormatoSalida;
import com.padron.dto.RespuestaPadron;
import com.padron.dto.SolicitudPadron;
import com.padron.logica.ServicioPadron;
import com.padron.util.Serializador;

import java.io.IOException;
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
        // TODO: implementar
        throw new UnsupportedOperationException("Pendiente de implementación.");
    }

    /**
     * Detiene el servidor.
     *
     * TODO (feature/http): implementar este método.
     */
    public void detener() {
        corriendo = false;
        // TODO: cerrar serverSocket si no es null
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
        // TODO: implementar en try-with-resources
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
        // TODO: split por '&', luego por '=', poblar SolicitudPadron
        return null;
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
        // TODO: implementar
        return "";
    }

    // ---------------------------------------------------------------
    // Getters de estado
    // ---------------------------------------------------------------

    public boolean isCorriendo() { return corriendo; }
    public int     getPuerto()   { return puerto; }
}
