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

    private int    codigoProvincia;
    private String nombreProvincia;
    private int    codigoCanton;
    private String nombreCanton;
    private int    codigoDistrito;
    private String nombreDistrito;

    // ---------------------------------------------------------------
    // Constructores
    // ---------------------------------------------------------------

    public Direccion() {
        // Vacío: requerido para serialización
    }

    public Direccion(int codigoProvincia, String nombreProvincia,
                     int codigoCanton,    String nombreCanton,
                     int codigoDistrito,  String nombreDistrito) {
        this.codigoProvincia  = codigoProvincia;
        this.nombreProvincia  = nombreProvincia;
        this.codigoCanton     = codigoCanton;
        this.nombreCanton     = nombreCanton;
        this.codigoDistrito   = codigoDistrito;
        this.nombreDistrito   = nombreDistrito;
    }

    // ---------------------------------------------------------------
    // Getters y Setters
    // ---------------------------------------------------------------

    public int    getCodigoProvincia()           { return codigoProvincia; }
    public void   setCodigoProvincia(int v)      { this.codigoProvincia = v; }

    public String getNombreProvincia()           { return nombreProvincia; }
    public void   setNombreProvincia(String v)   { this.nombreProvincia = v; }

    public int    getCodigoCanton()              { return codigoCanton; }
    public void   setCodigoCanton(int v)         { this.codigoCanton = v; }

    public String getNombreCanton()              { return nombreCanton; }
    public void   setNombreCanton(String v)      { this.nombreCanton = v; }

    public int    getCodigoDistrito()            { return codigoDistrito; }
    public void   setCodigoDistrito(int v)       { this.codigoDistrito = v; }

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
