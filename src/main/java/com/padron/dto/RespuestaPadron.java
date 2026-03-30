package com.padron.dto;

import com.padron.entidades.Direccion;
import com.padron.entidades.Persona;

/**
 * DTO que representa la respuesta a una consulta del padrón.
 * Contiene el resultado de la búsqueda y metadatos de la operación.
 *
 * RAMA:  feature/modelo
 * OWNER: Desarrollador 1
 *
 * TODO (feature/modelo):
 *  - Decidir si incluir timestamp en la respuesta
 *  - Considerar un campo "version" para la API
 */
public class RespuestaPadron {

    private boolean  exito;
    private String   mensaje;
    private Persona  persona;
    private Direccion direccion;

    // ---------------------------------------------------------------
    // Constructores
    // ---------------------------------------------------------------

    public RespuestaPadron() {
        // Vacío: requerido para serialización
    }

    /** Respuesta exitosa con datos completos. */
    public static RespuestaPadron exitosa(Persona persona, Direccion direccion) {
        RespuestaPadron r = new RespuestaPadron();
        r.exito     = true;
        r.mensaje   = "Consulta exitosa.";
        r.persona   = persona;
        r.direccion = direccion;
        return r;
    }

    /** Respuesta de error (cédula no encontrada, formato inválido, etc.). */
    public static RespuestaPadron error(String motivo) {
        RespuestaPadron r = new RespuestaPadron();
        r.exito   = false;
        r.mensaje = motivo;
        return r;
    }

    // ---------------------------------------------------------------
    // Getters y Setters
    // ---------------------------------------------------------------

    public boolean   isExito()              { return exito; }
    public void      setExito(boolean v)    { this.exito = v; }

    public String    getMensaje()           { return mensaje; }
    public void      setMensaje(String v)   { this.mensaje = v; }

    public Persona   getPersona()           { return persona; }
    public void      setPersona(Persona v)  { this.persona = v; }

    public Direccion getDireccion()               { return direccion; }
    public void      setDireccion(Direccion v)    { this.direccion = v; }

    @Override
    public String toString() {
        return "RespuestaPadron{exito=" + exito + ", persona=" + persona + "}";
    }
}
