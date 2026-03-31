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
    private String codElectoral;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;

    public Persona() {
    }

    public Persona(String cedula, String codElectoral, String nombre, String primerApellido, String segundoApellido) {
        this.cedula = cedula;
        this.codElectoral = codElectoral;
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCodElectoral() {
        return codElectoral;
    }

    public void setCodElectoral(String codElectoral) {
        this.codElectoral = codElectoral;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getNombreCompleto() {
        return nombre + " " + primerApellido + " " + segundoApellido;
    }

    @Override
    public String toString() {
        return "Persona{" +
                "cedula='" + cedula + '\'' +
                ", codElectoral='" + codElectoral + '\'' +
                ", nombre='" + nombre + '\'' +
                ", primerApellido='" + primerApellido + '\'' +
                ", segundoApellido='" + segundoApellido + '\'' +
                '}';
    }
}