package com.padron.entidades;

/**
 * Entidad que representa la descripción geográfica de un distrito electoral.
 * Los códigos de provincia, cantón y distrito se resuelven contra distelec.txt.
 *
 * RAMA:  feature/modelo
 * OWNER: Cristian Meléndez
 */
public class Direccion {

    private String codElectoral;
    private String provincia;
    private String canton;
    private String distrito;

    /** Constructor vacío para uso flexible del modelo. */
    public Direccion() {
    }

    /** Crea una dirección con la información geográfica asociada al código electoral. */
    public Direccion(String codElectoral, String provincia, String canton, String distrito) {
        this.codElectoral = codElectoral;
        this.provincia = provincia;
        this.canton = canton;
        this.distrito = distrito;
    }

    public String getCodElectoral() {
        return codElectoral;
    }

    public void setCodElectoral(String codElectoral) {
        this.codElectoral = codElectoral;
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

    /** Retorna la dirección lista para mostrar en texto. */
    public String getDireccionCompleta() {
        StringBuilder direccionCompleta = new StringBuilder();

        if (provincia != null && !provincia.isBlank()) {
            direccionCompleta.append(provincia.trim());
        }
        if (canton != null && !canton.isBlank()) {
            if (direccionCompleta.length() > 0) direccionCompleta.append(", ");
            direccionCompleta.append(canton.trim());
        }
        if (distrito != null && !distrito.isBlank()) {
            if (direccionCompleta.length() > 0) direccionCompleta.append(", ");
            direccionCompleta.append(distrito.trim());
        }

        return direccionCompleta.toString();
    }

    @Override
    public String toString() {
        return "Direccion{" +
                "codElectoral='" + codElectoral + '\'' +
                ", direccionCompleta='" + getDireccionCompleta() + '\'' +
                '}';
    }
}
