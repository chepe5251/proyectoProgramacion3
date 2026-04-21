package com.padron.util;

import com.padron.dto.FormatoSalida;
import com.padron.dto.RespuestaPadron;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SerializadorTest {

    private final Serializador serializador = new Serializador();

    @Test
    void escapaCaracteresEspecialesEnJson() {
        RespuestaPadron respuesta = new RespuestaPadron(
                "115050133",
                "ANA \"MARIA\"",
                "LOPEZ\nUNO",
                "GARCIA\tDOS",
                "ANA \"MARIA\" LOPEZ\nUNO GARCIA\tDOS",
                "SAN \\ JOSE",
                "A",
                "B"
        );

        String json = serializador.serializar(respuesta, FormatoSalida.JSON);

        assertTrue(json.contains("\\\"MARIA\\\""));
        assertTrue(json.contains("LOPEZ\\nUNO"));
        assertTrue(json.contains("GARCIA\\tDOS"));
        assertTrue(json.contains("SAN \\\\ JOSE"));
    }

    @Test
    void escapaCaracteresReservadosEnXml() {
        RespuestaPadron respuesta = new RespuestaPadron(
                "115050133",
                "ANA & BOB",
                "LOPEZ <UNO>",
                "GARCIA \"DOS\"",
                "ANA & BOB LOPEZ <UNO> GARCIA \"DOS\"",
                "SAN > JOSE",
                "A",
                "B"
        );

        String xml = serializador.serializar(respuesta, FormatoSalida.XML);

        assertTrue(xml.contains("ANA &amp; BOB"));
        assertTrue(xml.contains("LOPEZ &lt;UNO&gt;"));
        assertTrue(xml.contains("GARCIA &quot;DOS&quot;"));
        assertTrue(xml.contains("SAN &gt; JOSE"));
    }
}
