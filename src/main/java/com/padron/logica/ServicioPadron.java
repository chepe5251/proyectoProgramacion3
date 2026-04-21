package com.padron.logica;

import com.padron.datos.RepositorioDistelec;
import com.padron.datos.RepositorioPadron;
import com.padron.dto.RespuestaPadron;
import com.padron.dto.SolicitudPadron;
import com.padron.entidades.Direccion;
import com.padron.entidades.Persona;

/**
 * Servicio central de negocio del sistema.
 * Coordina los repositorios y produce una RespuestaPadron.
 *
 * Esta clase NO sabe nada de TCP, HTTP, JSON ni XML.
 * Esa responsabilidad es de las capas de presentaciÃ³n y util.
 */
public class ServicioPadron {

    private final RepositorioPadron repositorioPadron;
    private final RepositorioDistelec repositorioDistelec;

    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------

    public ServicioPadron(RepositorioPadron repositorioPadron,
                          RepositorioDistelec repositorioDistelec) {
        this.repositorioPadron = repositorioPadron;
        this.repositorioDistelec = repositorioDistelec;
    }

    // ---------------------------------------------------------------
    // MÃ©todos pÃºblicos
    // ---------------------------------------------------------------

    /**
     * Procesa una solicitud de consulta al padrÃ³n.
     *
     * @param solicitud datos de la consulta
     * @return          respuesta con los datos o mensaje de error
     */
    public RespuestaPadron consultarPadron(SolicitudPadron solicitud) {
        if (solicitud == null || !solicitud.esValida()) {
            return RespuestaPadron.error("400", "Solicitud invalida: cedula y formato son requeridos.");
        }

        String cedulaNormalizada = normalizarCedula(solicitud.getCedula());
        solicitud.setCedula(cedulaNormalizada);

        if (!validarCedula(cedulaNormalizada)) {
            return RespuestaPadron.error("400", "Cedula invalida: debe contener exactamente 9 digitos.");
        }

        Persona persona = repositorioPadron.buscarPorCedula(cedulaNormalizada);
        if (persona == null) {
            return RespuestaPadron.error("404", "Cedula no encontrada en el padron.");
        }

        Direccion direccion = repositorioDistelec.buscarPorCodElectoral(persona.getCodElectoral());
        return RespuestaPadron.exitosa(persona, direccion);
    }

    // ---------------------------------------------------------------
    // MÃ©todos privados
    // ---------------------------------------------------------------

    /**
     * Valida que la cÃ©dula tenga exactamente 9 dÃ­gitos.
     *
     * @return true si la cÃ©dula es vÃ¡lida
     */
    private boolean validarCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            return false;
        }

        return cedula.matches("\\d{9}");
    }

    /**
     * Normaliza la cÃ©dula removiendo espacios y separadores comunes.
     */
    private String normalizarCedula(String cedula) {
        if (cedula == null) {
            return null;
        }

        return cedula.replaceAll("[^\\d]", "");
    }
}
