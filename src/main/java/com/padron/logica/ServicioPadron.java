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
 * RAMA:  feature/logica
 * OWNER: Desarrollador 3
 *
 * Esta clase NO sabe nada de TCP, HTTP, JSON ni XML.
 * Esa responsabilidad es de las capas de presentación y util.
 *
 * TODO (feature/logica):
 *  - Implementar consultarPadron()
 *  - Implementar validarCedula()
 *  - Agregar logging básico (System.out o java.util.logging)
 */
public class ServicioPadron {

    private final RepositorioPadron    repositorioPadron;
    private final RepositorioDistelec  repositorioDistelec;

    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------

    public ServicioPadron(RepositorioPadron repositorioPadron,
                          RepositorioDistelec repositorioDistelec) {
        this.repositorioPadron   = repositorioPadron;
        this.repositorioDistelec = repositorioDistelec;
    }

    // ---------------------------------------------------------------
    // Métodos públicos
    // ---------------------------------------------------------------

    /**
     * Procesa una solicitud de consulta al padrón.
     *
     * TODO (feature/logica): implementar este método.
     *  Pasos sugeridos:
     *  1. Validar la solicitud (cedula no vacía, formato reconocido)
     *  2. Buscar la persona en RepositorioPadron
     *  3. Si no existe → retornar RespuestaPadron.error("Cédula no encontrada.")
     *  4. Resolver la dirección con RepositorioDistelec
     *  5. Retornar RespuestaPadron.exitosa(persona, direccion)
     *
     * @param solicitud datos de la consulta
     * @return          respuesta con los datos o mensaje de error
     */
    public RespuestaPadron consultarPadron(SolicitudPadron solicitud) {
        if (solicitud == null || !solicitud.esValida()) {
            return RespuestaPadron.error("400", "Solicitud invalida: cedula y formato son requeridos.");
        }
        if (!validarCedula(solicitud.getCedula())) {
            return RespuestaPadron.error("400", "Cedula invalida: debe contener exactamente 9 digitos.");
        }
        Persona persona = repositorioPadron.buscarPorCedula(solicitud.getCedula());
        if (persona == null) {
            return RespuestaPadron.error("404", "Cedula no encontrada en el padron.");
        }
        Direccion direccion = repositorioDistelec.buscarPorCodElectoral(persona.getCodElectoral());
        return RespuestaPadron.exitosa(persona, direccion);
    }

    // ---------------------------------------------------------------
    // Métodos privados
    // ---------------------------------------------------------------

    /**
     * Valida que la cédula tenga el formato correcto.
     *
     * TODO (feature/logica): implementar validación.
     *  - No nula, no vacía
     *  - Solo dígitos
     *  - Exactamente 9 caracteres
     *
     * @return true si la cédula es válida
     */
    private boolean validarCedula(String cedula) {
       if (cedula == null || cedula.trim().isEmpty()) {
            return false;
        }
        // Debe tener exactamente 9 dígitos y ser solo números
        return cedula.matches("\\d{9}");
    }
}
