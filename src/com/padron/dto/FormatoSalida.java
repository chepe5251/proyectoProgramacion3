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

    JSON("json"),
    XML("xml");

    private final String codigo;

    FormatoSalida(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    /**
     * Convierte un string ("json" o "xml") en el enum correspondiente.
     *
     * @param valor  string recibido del cliente (case-insensitive)
     * @return       el FormatoSalida correspondiente
     * @throws IllegalArgumentException si el valor no es reconocido
     */
    public static FormatoSalida fromString(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("El formato de salida no puede ser nulo.");
        }
        for (FormatoSalida f : values()) {
            if (f.codigo.equalsIgnoreCase(valor.trim())) {
                return f;
            }
        }
        throw new IllegalArgumentException(
                "Formato desconocido: '" + valor + "'. Use 'json' o 'xml'.");
    }
}
