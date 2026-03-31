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

    private String codElectoral;
    private String provincia;
    private String canton;
    private String distrito;

    public Direccion() {
    }

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

    @Override
    public String toString() {
        return "Direccion{" +
                "codElectoral='" + codElectoral + '\'' +
                ", provincia='" + provincia + '\'' +
                ", canton='" + canton + '\'' +
                ", distrito='" + distrito + '\'' +
                '}';
    }
}