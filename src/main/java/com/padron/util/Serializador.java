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
            sb.append("  \"nombre\": \"").append(escaparJson(respuesta.getNombreCompleto())).append("\",\n");
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
            sb.append("  <codigoError>").append(respuesta.getCodigoError()).append("</codigoError>\n");
            sb.append("  <mensaje>").append(respuesta.getMensajeError()).append("</mensaje>\n");
        } else {
            sb.append("  <exito>true</exito>\n");
            sb.append("  <cedula>").append(respuesta.getCedula()).append("</cedula>\n");
            sb.append("  <nombre>").append(respuesta.getNombreCompleto()).append("</nombre>\n");
            sb.append("  <provincia>").append(respuesta.getProvincia()).append("</provincia>\n");
            sb.append("  <canton>").append(respuesta.getCanton()).append("</canton>\n");
            sb.append("  <distrito>").append(respuesta.getDistrito()).append("</distrito>\n");
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
        return valor.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
