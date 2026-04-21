package com.padron.logica;

import com.padron.datos.RepositorioDistelec;
import com.padron.datos.RepositorioPadron;
import com.padron.dto.FormatoSalida;
import com.padron.dto.RespuestaPadron;
import com.padron.dto.SolicitudPadron;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServicioPadronTest {

    @TempDir
    Path tempDir;

    @Test
    void retornaRespuestaExitosaConNombreApellidosYDireccion() throws IOException {
        ServicioPadron servicio = crearServicio(
            "115050133,303005, ,20260914,00000,CRISTIAN JOAN                 ,MELENDEZ                  ,GARCIA                    ",
            "303005,CARTAGO,LA UNION,SAN RAFAEL"
        );

        RespuestaPadron respuesta = servicio.consultarPadron(
            new SolicitudPadron("115050133", FormatoSalida.JSON)
        );

        assertFalse(respuesta.esError());
        assertEquals("115050133", respuesta.getCedula());
        assertEquals("CRISTIAN JOAN", respuesta.getNombre());
        assertEquals("MELENDEZ", respuesta.getPrimerApellido());
        assertEquals("GARCIA", respuesta.getSegundoApellido());
        assertEquals("CRISTIAN JOAN MELENDEZ GARCIA", respuesta.getNombreCompleto());
        assertEquals("CARTAGO", respuesta.getProvincia());
        assertEquals("LA UNION", respuesta.getCanton());
        assertEquals("SAN RAFAEL", respuesta.getDistrito());
    }

    @Test
    void retorna404CuandoLaCedulaNoExiste() throws IOException {
        ServicioPadron servicio = crearServicio(
            "115050133,303005, ,20260914,00000,CRISTIAN JOAN                 ,MELENDEZ                  ,GARCIA                    ",
            "303005,CARTAGO,LA UNION,SAN RAFAEL"
        );

        RespuestaPadron respuesta = servicio.consultarPadron(
            new SolicitudPadron("000000001", FormatoSalida.JSON)
        );

        assertTrue(respuesta.esError());
        assertEquals("404", respuesta.getCodigoError());
        assertEquals("Cedula no encontrada en el padron.", respuesta.getMensajeError());
    }

    @Test
    void retorna400CuandoLaCedulaEsInvalida() throws IOException {
        ServicioPadron servicio = crearServicio(
            "115050133,303005, ,20260914,00000,CRISTIAN JOAN                 ,MELENDEZ                  ,GARCIA                    ",
            "303005,CARTAGO,LA UNION,SAN RAFAEL"
        );

        RespuestaPadron respuesta = servicio.consultarPadron(
            new SolicitudPadron("12345", FormatoSalida.JSON)
        );

        assertTrue(respuesta.esError());
        assertEquals("400", respuesta.getCodigoError());
        assertEquals("Cedula invalida: debe contener exactamente 9 digitos.", respuesta.getMensajeError());
    }

    @Test
    void consultaNormalizaCedulaAntesDeBuscar() throws IOException {
        ServicioPadron servicio = crearServicio(
            "109870456,001,,20260101,,MARIA FERNANDA,JIMENEZ,ROJAS",
            "001,SAN JOSE,SAN JOSE,CARMEN"
        );

        RespuestaPadron respuesta = servicio.consultarPadron(
            new SolicitudPadron("1-0987-0456", FormatoSalida.JSON)
        );

        assertFalse(respuesta.esError());
        assertEquals("109870456", respuesta.getCedula());
        assertEquals("JIMENEZ", respuesta.getPrimerApellido());
        assertEquals("ROJAS", respuesta.getSegundoApellido());
        assertEquals("MARIA FERNANDA JIMENEZ ROJAS", respuesta.getNombreCompleto());
    }

    @Test
    void consultaRetorna400CuandoCedulaNormalizadaNoTieneNueveDigitos() throws IOException {
        ServicioPadron servicio = crearServicio(
            "109870456,001,,20260101,,MARIA FERNANDA,JIMENEZ,ROJAS",
            "001,SAN JOSE,SAN JOSE,CARMEN"
        );

        RespuestaPadron respuesta = servicio.consultarPadron(
            new SolicitudPadron("1-23", FormatoSalida.JSON)
        );

        assertTrue(respuesta.esError());
        assertEquals("400", respuesta.getCodigoError());
    }

    private ServicioPadron crearServicio(String lineaPadron, String lineaDistelec) throws IOException {
        Path archivoPadron = tempDir.resolve("PADRON.txt");
        Path archivoDistelec = tempDir.resolve("distelec.txt");

        Files.writeString(archivoPadron, lineaPadron);
        Files.writeString(archivoDistelec, lineaDistelec);

        RepositorioPadron repositorioPadron = new RepositorioPadron(archivoPadron.toString());
        RepositorioDistelec repositorioDistelec = new RepositorioDistelec(archivoDistelec.toString());
        repositorioPadron.cargarDesdeArchivo();
        repositorioDistelec.cargarDesdeArchivo();
        return new ServicioPadron(repositorioPadron, repositorioDistelec);
    }
}
