package com.padron.entidades;

/**
 * Entidad que representa un ciudadano registrado en el padrón electoral.
 *
 * RAMA:  feature/modelo
 * OWNER: Cristian Meléndez
 */
public class Persona {

    private String cedula;
    private String codElectoral;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;

    /** Constructor vacío para uso flexible del modelo. */
    public Persona() {
    }

    /** Crea una persona con los datos mínimos del padrón. */
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

    /** Retorna el nombre completo listo para mostrar en salidas. */
    public String getNombreCompleto() {
        StringBuilder nombreCompleto = new StringBuilder();

        if (nombre != null && !nombre.isBlank()) {
            nombreCompleto.append(nombre.trim());
        }
        if (primerApellido != null && !primerApellido.isBlank()) {
            if (nombreCompleto.length() > 0) nombreCompleto.append(" ");
            nombreCompleto.append(primerApellido.trim());
        }
        if (segundoApellido != null && !segundoApellido.isBlank()) {
            if (nombreCompleto.length() > 0) nombreCompleto.append(" ");
            nombreCompleto.append(segundoApellido.trim());
        }

        return nombreCompleto.toString();
    }

    @Override
    public String toString() {
        return "Persona{" +
                "cedula='" + cedula + '\'' +
                ", codElectoral='" + codElectoral + '\'' +
                ", nombreCompleto='" + getNombreCompleto() + '\'' +
                '}';
    }
}
