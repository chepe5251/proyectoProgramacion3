package com.padron.dto;

/**
 * DTO que encapsula los parámetros de una consulta al padrón.
 *
 * RAMA:  feature/modelo
 * OWNER: Cristian Meléndez
 */
public class SolicitudPadron {

    private String cedula;
    private FormatoSalida formato;
   
    /** Crea una solicitud vacía con formato JSON por defecto. */
    public SolicitudPadron() {
        this.formato = FormatoSalida.JSON;
    }

    /** Crea una solicitud con los datos mínimos requeridos para consultar el padrón. */
    public SolicitudPadron(String cedula, FormatoSalida formato) {
        this.cedula = cedula;
        this.formato = formato;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public FormatoSalida getFormato() {
        return formato;
    }

    public void setFormato(FormatoSalida formato) {
        this.formato = formato;
    }

    /** Indica si la solicitud tiene los campos mínimos esperados. */
    public boolean esValida() {
        return cedula != null && !cedula.isBlank() && formato != null;
    }

    @Override
    public String toString() {
        return "SolicitudPadron{" +
                "cedula='" + cedula + '\'' +
                ", formato=" + formato +
                '}';
    }
}
