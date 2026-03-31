package com.padron.util;

import com.padron.dto.FormatoSalida;
import com.padron.dto.RespuestaPadron;

/**
 * Convierte una RespuestaPadron a String en formato JSON o XML.
 *
 * RAMA:  feature/serializacion
 * OWNER: Desarrollador 3
 *
 * IMPORTANTE: esta clase construye el JSON/XML manualmente con StringBuilder
 * para no depender de librerías externas (compatible con Ant sin gestión de deps).
 * Si el equipo decide agregar Jackson u otra librería, se puede refactorizar.
 *
 * TODO (feature/serializacion):
 *  - Implementar aJson()
 *  - Implementar aXml()
 *  - Manejar el caso donde persona o direccion son null (respuesta de error)
 *  - Escapar caracteres especiales en Strings (tildes, ñ)
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
     *
     * TODO (feature/serializacion): implementar.
     *
     * Ejemplo de salida esperada (respuesta exitosa):
     * {
     *   "exito": true,
     *   "mensaje": "Consulta exitosa.",
     *   "persona": {
     *     "cedula": "109870456",
     *     "nombre": "Juan Perez Perez",
     *     "provincia": "San José",
     *     "canton": "San José",
     *     "distrito": "Carmen"
     *   }
     * }
     */
    private String aJson(RespuestaPadron respuesta) {
        // TODO: implementar con StringBuilder
        return "{ \"status\": \"success\" }";
    }

    /**
     * Construye el XML de la respuesta manualmente.
     *
     * TODO (feature/serializacion): implementar.
     *
     * Ejemplo de salida esperada (respuesta exitosa):
     * <?xml version="1.0" encoding="UTF-8"?>
     * <respuesta>
     *   <exito>true</exito>
     *   <mensaje>Consulta exitosa.</mensaje>
     *   <persona>
     *     <cedula>109870456</cedula>
     *     <nombre>Juan Perez Perez</nombre>
     *     <provincia>San José</provincia>
     *     <canton>San José</canton>
     *     <distrito>Carmen</distrito>
     *   </persona>
     * </respuesta>
     */
    private String aXml(RespuestaPadron respuesta) {
        // TODO: implementar con StringBuilder
        return "<status>success</status>";
    }

    // ---------------------------------------------------------------
    // Utilidad interna
    // ---------------------------------------------------------------

    /**
     * Escapa caracteres especiales para JSON.
     * TODO (feature/serializacion): manejar tildes, ñ, comillas, backslash.
     */
    private String escaparJson(String valor) {
        if (valor == null) return "";
        return valor.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
