package com.padron.entidades;

/**
 * Entidad que representa la descripción geográfica de un distrito electoral.
 * Los códigos de provincia, cantón y distrito se resuelven contra distelec.txt.
 *
 * RAMA:  feature/modelo
 * OWNER: Desarrollador 1
 *
 * TODO (feature/modelo):
 *  - Agregar equals() y hashCode() basados en los tres códigos
 *  - Considerar si necesitamos código de junta receptora
 */
public class Direccion {

    private String codElec;
    private String nombreProvincia;
    private String nombreCanton;
    private String nombreDistrito;

    // ---------------------------------------------------------------
    // Constructores
    // ---------------------------------------------------------------

    public Direccion() {
        // Vacío: requerido para serialización
    }

    public Direccion(String codElec, String nombreProvincia,
                     String nombreCanton, String nombreDistrito) {
        this.codElec = codElec;
        this.nombreProvincia = nombreProvincia;
        this.nombreCanton = nombreCanton;
        this.nombreDistrito = nombreDistrito;
    }

    // ---------------------------------------------------------------
    // Getters y Setters
    // ---------------------------------------------------------------

    public String getCodElec()                   { return codElec; }
    public void   setCodElec(String v)           { this.codElec = v; }

    public String getNombreProvincia()           { return nombreProvincia; }
    public void   setNombreProvincia(String v)   { this.nombreProvincia = v; }

    public String getNombreCanton()              { return nombreCanton; }
    public void   setNombreCanton(String v)      { this.nombreCanton = v; }

    public String getNombreDistrito()            { return nombreDistrito; }
    public void   setNombreDistrito(String v)    { this.nombreDistrito = v; }

    // ---------------------------------------------------------------
    // Utilidades
    // ---------------------------------------------------------------

    /** Retorna la dirección completa: "Provincia, Cantón, Distrito". */
    public String getDireccionCompleta() {
        return nombreProvincia + ", " + nombreCanton + ", " + nombreDistrito;
    }

    @Override
    public String toString() {
        return "Direccion{" + getDireccionCompleta() + "}";
    }
}
