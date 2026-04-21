package com.padron.presentacion.http;

import com.padron.dto.FormatoSalida;
import com.padron.dto.SolicitudPadron;
import com.padron.util.Serializador;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpServerPadronTest {

    private final HttpServerPadron server = new HttpServerPadron(8080, null, new Serializador());

    @Test
    void aceptaCedulaEnRutaYAliasFormat() {
        HttpServerPadron.HttpRequestData requestData =
            server.parsearRequestLine("GET /padron/109870456?format=xml HTTP/1.1");

        SolicitudPadron solicitud = requestData.solicitud;
        assertEquals(200, requestData.statusCode);
        assertEquals("109870456", solicitud.getCedula());
        assertEquals(FormatoSalida.XML, solicitud.getFormato());
    }

    @Test
    void rechazaMetodosDistintosDeGetCon405() {
        HttpServerPadron.HttpRequestData requestData =
            server.parsearRequestLine("POST /padron?cedula=109870456 HTTP/1.1");

        assertEquals(405, requestData.statusCode);
        assertEquals("Metodo no permitido. Use GET.", requestData.errorMessage);
    }
}
