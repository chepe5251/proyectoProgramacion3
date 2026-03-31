package com.padron.dto;

/**
 * Enum que representa los formatos de salida soportados por el sistema.
 *
 * RAMA:  feature/modelo
 * OWNER: Cristian Meléndez
 *
 * Uso:
 *   FormatoSalida formato = FormatoSalida.fromString("json");
 *   // → FormatoSalida.JSON
 */
public enum FormatoSalida {
    JSON,
    XML;

    /**
     * Convierte un texto a su enum correspondiente.
     *
     * @param valor texto recibido en la solicitud
     * @return JSON, XML o null si no coincide con un formato soportado
     */
    public static FormatoSalida fromString(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        switch (valor.trim().toUpperCase()) {
            case "JSON":
                return JSON;
            case "XML":
                return XML;
            default:
                return null;
        }
    }
}
