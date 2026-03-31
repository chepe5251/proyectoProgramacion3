package com.padron.dto;

/**
 * Enum que representa los formatos de salida soportados por el sistema.
 *
 * RAMA:  feature/modelo
 * OWNER: Desarrollador 1
 *
 * Uso:
 *   FormatoSalida formato = FormatoSalida.fromString("json");
 *   // → FormatoSalida.JSON
 */
public enum FormatoSalida {
    JSON,
    XML;

    public static FormatoSalida fromString(String valor) {
        if (valor == null) {
            return null;
        }

        switch (valor.toUpperCase()) {
            case "JSON":
                return JSON;
            case "XML":
                return XML;
            default:
                return null;
        }
    }
}
