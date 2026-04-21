package com.padron.util;

import com.padron.dto.FormatoSalida;
import com.padron.dto.RespuestaPadron;

/**
 * Convierte una RespuestaPadron a String en formato JSON o XML.
 *
 * IMPORTANTE: esta clase construye el JSON/XML manualmente con StringBuilder
 * para no depender de librerías externas.
 */
public class Serializador {

    // ---------------------------------------------------------------
    // Métodos públicos
    // ---------------------------------------------------------------

    /**
     * Serializa una RespuestaPadron según el formato solicitado.
     *
     * @param respuesta objeto a serializar
     * @param formato   JSON o XML
     * @return          String con la representación serializada
     */
    public String serializar(RespuestaPadron respuesta, FormatoSalida formato) {
        switch (formato) {
            case JSON: return aJson(respuesta);
            case XML:  return aXml(respuesta);
            default:
                throw new IllegalArgumentException("Formato no soportado: " + formato);
        }
    }

    // ---------------------------------------------------------------
    // Métodos privados
    // ---------------------------------------------------------------

    /**
     * Construye el JSON de la respuesta manualmente.
     */
    private String aJson(RespuestaPadron respuesta) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        if (respuesta.esError()) {
            sb.append("  \"exito\": false,\n");
            sb.append("  \"codigoError\": \"").append(escaparJson(respuesta.getCodigoError())).append("\",\n");
            sb.append("  \"mensaje\": \"").append(escaparJson(respuesta.getMensajeError())).append("\"\n");
        } else {
            sb.append("  \"exito\": true,\n");
            sb.append("  \"cedula\": \"").append(escaparJson(respuesta.getCedula())).append("\",\n");
            sb.append("  \"nombre\": \"").append(escaparJson(respuesta.getNombre())).append("\",\n");
            sb.append("  \"primerApellido\": \"").append(escaparJson(respuesta.getPrimerApellido())).append("\",\n");
            sb.append("  \"segundoApellido\": \"").append(escaparJson(respuesta.getSegundoApellido())).append("\",\n");
            sb.append("  \"nombreCompleto\": \"").append(escaparJson(respuesta.getNombreCompleto())).append("\",\n");
            sb.append("  \"provincia\": \"").append(escaparJson(respuesta.getProvincia())).append("\",\n");
            sb.append("  \"canton\": \"").append(escaparJson(respuesta.getCanton())).append("\",\n");
            sb.append("  \"distrito\": \"").append(escaparJson(respuesta.getDistrito())).append("\"\n");
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Construye el XML de la respuesta manualmente.
     */
    private String aXml(RespuestaPadron respuesta) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<respuesta>\n");
        if (respuesta.esError()) {
            sb.append("  <exito>false</exito>\n");
            sb.append("  <codigoError>").append(escaparXml(respuesta.getCodigoError())).append("</codigoError>\n");
            sb.append("  <mensaje>").append(escaparXml(respuesta.getMensajeError())).append("</mensaje>\n");
        } else {
            sb.append("  <exito>true</exito>\n");
           sb.append("  <cedula>").append(escaparXml(respuesta.getCedula())).append("</cedula>\n");
            sb.append("  <nombre>").append(escaparXml(respuesta.getNombreCompleto())).append("</nombre>\n");
            sb.append("  <primerApellido>").append(escaparXml(respuesta.getPrimerApellido())).append("</primerApellido>\n");
            sb.append("  <segundoApellido>").append(escaparXml(respuesta.getSegundoApellido())).append("</segundoApellido>\n");
            sb.append("  <provincia>").append(escaparXml(respuesta.getProvincia())).append("</provincia>\n");
            sb.append("  <canton>").append(escaparXml(respuesta.getCanton())).append("</canton>\n");
            sb.append("  <distrito>").append(escaparXml(respuesta.getDistrito())).append("</distrito>\n");
        }
        sb.append("</respuesta>");
        return sb.toString();
    }

    // ---------------------------------------------------------------
    // Utilidad interna
    // ---------------------------------------------------------------

    /**
     * Escapa caracteres especiales para JSON.
     */
    private String escaparJson(String valor) {
        if (valor == null) return "";
        StringBuilder sb = new StringBuilder();
        for (char c : valor.toCharArray()) {
            switch (c) {
                case '\\': sb.append("\\\\"); break;
                case '"':  sb.append("\\\""); break;
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }

    /**
     * Escapa caracteres reservados y de control para XML.
     */
    private String escaparXml(String valor) {
        if (valor == null) return "";

        return valor.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&apos;");
    }

    private boolean esControlXmlInvalido(char c) {
        return c < 0x20 && c != '\n' && c != '\r' && c != '\t';
    }
}
