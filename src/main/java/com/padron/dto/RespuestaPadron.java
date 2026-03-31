package com.padron.dto;

import com.padron.entidades.Direccion;
import com.padron.entidades.Persona;

/**
 * DTO que representa la respuesta a una consulta del padrón.
 * Contiene tanto los datos exitosos como la información de error.
 *
 * RAMA:  feature/modelo
 * OWNER: Cristian Meléndez
 */
public class RespuestaPadron {

    private String cedula;
    private String nombreCompleto;
    private String provincia;
    private String canton;
    private String distrito;
    private String codigoError;
    private String mensajeError;

    /** Constructor vacío para serialización o construcción manual. */
    public RespuestaPadron() {
    }

    /** Constructor para una respuesta exitosa. */
    public RespuestaPadron(String cedula, String nombreCompleto, String provincia, String canton, String distrito) {
        this.cedula = cedula;
        this.nombreCompleto = nombreCompleto;
        this.provincia = provincia;
        this.canton = canton;
        this.distrito = distrito;
    }

    /** Constructor para una respuesta de error. */
    public RespuestaPadron(String codigoError, String mensajeError) {
        this.codigoError = codigoError;
        this.mensajeError = mensajeError;
    }

    /** Crea una respuesta exitosa a partir de las entidades principales. */
    public static RespuestaPadron exitosa(Persona persona, Direccion direccion) {
        return new RespuestaPadron(
                persona != null ? persona.getCedula() : null,
                persona != null ? persona.getNombreCompleto() : null,
                direccion != null ? direccion.getProvincia() : null,
                direccion != null ? direccion.getCanton() : null,
                direccion != null ? direccion.getDistrito() : null
        );
    }

    /** Crea una respuesta de error con código y mensaje. */
    public static RespuestaPadron error(String codigoError, String mensajeError) {
        return new RespuestaPadron(codigoError, mensajeError);
    }

    /** Indica si la respuesta representa un error. */
    public boolean esError() {
        return codigoError != null && !codigoError.isBlank();
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getCodigoError() {
        return codigoError;
    }

    public void setCodigoError(String codigoError) {
        this.codigoError = codigoError;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    @Override
    public String toString() {
        return "RespuestaPadron{" +
                "cedula='" + cedula + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", provincia='" + provincia + '\'' +
                ", canton='" + canton + '\'' +
                ", distrito='" + distrito + '\'' +
                ", codigoError='" + codigoError + '\'' +
                ", mensajeError='" + mensajeError + '\'' +
                '}';
    }
}
