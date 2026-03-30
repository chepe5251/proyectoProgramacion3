package com.padron.dto;

/**
 * DTO que encapsula los parámetros de una consulta al padrón.
 *
 * RAMA:  feature/modelo
 * OWNER: Desarrollador 1
 *
 * Ejemplo de solicitud desde TCP:
 *   cedula=109870456&formato=json
 *
 * TODO (feature/modelo):
 *  - Agregar validación de formato de cédula aquí o en ServicioPadron
 *  - Considerar si el campo "formato" debe tener valor por defecto JSON
 */
public class SolicitudPadron {

    /** Número de cédula a consultar (sin guiones, 9 dígitos). */
    private String cedula;

    /** Formato de respuesta deseado: JSON o XML. */
    private FormatoSalida formato;

    // ---------------------------------------------------------------
    // Constructores
    // ---------------------------------------------------------------

    public SolicitudPadron() {
        // Por defecto: formato JSON
        this.formato = FormatoSalida.JSON;
    }

    public SolicitudPadron(String cedula, FormatoSalida formato) {
        this.cedula  = cedula;
        this.formato = formato;
    }

    // ---------------------------------------------------------------
    // Getters y Setters
    // ---------------------------------------------------------------

    public String getCedula()              { return cedula; }
    public void   setCedula(String v)      { this.cedula = v; }

    public FormatoSalida getFormato()           { return formato; }
    public void          setFormato(FormatoSalida v) { this.formato = v; }

    // ---------------------------------------------------------------
    // Utilidades
    // ---------------------------------------------------------------

    /**
     * Indica si la solicitud tiene los campos mínimos requeridos.
     * La validación profunda (formato de cédula) la hace ServicioPadron.
     */
    public boolean esValida() {
        return cedula != null && !cedula.isBlank() && formato != null;
    }

    @Override
    public String toString() {
        return "SolicitudPadron{cedula='" + cedula + "', formato=" + formato + "}";
    }
}
