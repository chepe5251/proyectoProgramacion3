package com.padron.entidades;

/**
 * Entidad que representa un ciudadano registrado en el padrón electoral.
 *
 * RAMA:  feature/modelo
 * OWNER: Desarrollador 1
 *
 * TODO (feature/modelo):
 *  - Validar formato de cédula (9 dígitos, sin guiones)
 *  - Agregar equals() y hashCode() basados en cedula
 *  - Decidir si sexo se modela como enum Sex {MASCULINO, FEMENINO}
 */
public class Persona {

    private String cedula;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private int    codigoProvincia;
    private int    codigoCanton;
    private int    codigoDistrito;

    // ---------------------------------------------------------------
    // Constructores
    // ---------------------------------------------------------------

    public Persona() {
        // Vacío: requerido para serialización
    }

    public Persona(String cedula, String nombre,
                   String primerApellido, String segundoApellido,
                   int codigoProvincia, int codigoCanton, int codigoDistrito) {
        this.cedula          = cedula;
        this.nombre          = nombre;
        this.primerApellido  = primerApellido;
        this.segundoApellido = segundoApellido;
        this.codigoProvincia = codigoProvincia;
        this.codigoCanton    = codigoCanton;
        this.codigoDistrito  = codigoDistrito;
    }

    // ---------------------------------------------------------------
    // Getters y Setters
    // ---------------------------------------------------------------

    public String getCedula()            { return cedula; }
    public void   setCedula(String v)    { this.cedula = v; }

    public String getNombre()            { return nombre; }
    public void   setNombre(String v)    { this.nombre = v; }

    public String getPrimerApellido()           { return primerApellido; }
    public void   setPrimerApellido(String v)   { this.primerApellido = v; }

    public String getSegundoApellido()          { return segundoApellido; }
    public void   setSegundoApellido(String v)  { this.segundoApellido = v; }

    public int  getCodigoProvincia()       { return codigoProvincia; }
    public void setCodigoProvincia(int v)  { this.codigoProvincia = v; }

    public int  getCodigoCanton()          { return codigoCanton; }
    public void setCodigoCanton(int v)     { this.codigoCanton = v; }

    public int  getCodigoDistrito()        { return codigoDistrito; }
    public void setCodigoDistrito(int v)   { this.codigoDistrito = v; }

    // ---------------------------------------------------------------
    // Utilidades
    // ---------------------------------------------------------------

    /** Retorna el nombre completo: "Nombre PrimerApellido SegundoApellido". */
    public String getNombreCompleto() {
        return nombre + " " + primerApellido + " " + segundoApellido;
    }

    @Override
    public String toString() {
        return "Persona{cedula='" + cedula + "', nombre='" + getNombreCompleto() + "'}";
    }
}
