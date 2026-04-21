package com.padron.datos;

import com.padron.entidades.Persona;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RepositorioPadronTest {

    @TempDir
    Path tempDir;

    @Test
    void cargaFormatoRealDeOchoColumnasConNombreYApellidosCorrectos() throws IOException {
        Path archivoPadron = tempDir.resolve("PADRON.txt");
        Files.writeString(
                archivoPadron,
                "115050133,303005, ,20260914,00000,CRISTIAN JOAN                 ,MELENDEZ                  ,GARCIA                    "
        );

        RepositorioPadron repositorio = new RepositorioPadron(archivoPadron.toString());
        repositorio.cargarDesdeArchivo();

        Persona persona = repositorio.buscarPorCedula("115050133");
        assertNotNull(persona);
        assertEquals("CRISTIAN JOAN", persona.getNombre());
        assertEquals("MELENDEZ", persona.getPrimerApellido());
        assertEquals("GARCIA", persona.getSegundoApellido());
        assertEquals("CRISTIAN JOAN MELENDEZ GARCIA", persona.getNombreCompleto());
    }
}
