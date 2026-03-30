package com.padron.presentacion.tcp;

import com.padron.dto.FormatoSalida;
import com.padron.dto.RespuestaPadron;
import com.padron.dto.SolicitudPadron;
import com.padron.logica.ServicioPadron;
import com.padron.util.Serializador;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Servidor TCP que escucha solicitudes de consulta al padrón.
 *
 * RAMA:  feature/tcp
 * OWNER: Desarrollador 4
 *
 * Protocolo esperado (texto plano por socket):
 *   Solicitud del cliente:  "cedula=109870456&formato=json\n"
 *   Respuesta del servidor: JSON o XML según formato pedido
 *
 * TODO (feature/tcp):
 *  - Implementar iniciar()
 *  - Implementar manejarCliente() en un Thread separado
 *  - Implementar parsearSolicitud() para leer el texto del socket
 *  - Implementar detener() para cerrar el ServerSocket limpiamente
 *  - Agregar timeout de lectura por socket (evitar clientes colgados)
 */
public class TcpServer {

    private final int            puerto;
    private final ServicioPadron servicio;
    private final Serializador   serializador;

    private ServerSocket serverSocket;
    private boolean      corriendo = false;

    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------

    public TcpServer(int puerto, ServicioPadron servicio, Serializador serializador) {
        this.puerto       = puerto;
        this.servicio     = servicio;
        this.serializador = serializador;
    }

    // ---------------------------------------------------------------
    // Ciclo de vida
    // ---------------------------------------------------------------

    /**
     * Inicia el servidor TCP y entra al loop de aceptar conexiones.
     *
     * TODO (feature/tcp): implementar este método.
     *  1. Crear ServerSocket en el puerto configurado
     *  2. Loop: aceptar Socket → lanzar Thread → manejarCliente()
     *  3. Capturar IOException cuando corriendo=false (cierre limpio)
     *
     * @throws IOException si no se puede abrir el puerto
     */
    public void iniciar() throws IOException {
        // TODO: implementar
        throw new UnsupportedOperationException("Pendiente de implementación.");
    }

    /**
     * Detiene el servidor cerrando el ServerSocket.
     *
     * TODO (feature/tcp): implementar este método.
     */
    public void detener() {
        corriendo = false;
        // TODO: cerrar serverSocket si no es null
    }

    // ---------------------------------------------------------------
    // Manejo de clientes
    // ---------------------------------------------------------------

    /**
     * Maneja una conexión individual de un cliente.
     * Debe ejecutarse en su propio Thread.
     *
     * TODO (feature/tcp): implementar este método.
     *  1. Leer línea del socket (BufferedReader)
     *  2. Parsear con parsearSolicitud()
     *  3. Llamar servicio.consultarPadron()
     *  4. Serializar y escribir respuesta al socket (PrintWriter)
     *  5. Cerrar socket al terminar
     *
     * @param cliente socket del cliente conectado
     */
    private void manejarCliente(Socket cliente) {
        // TODO: implementar en try-with-resources
    }

    /**
     * Parsea la línea de texto recibida por TCP en una SolicitudPadron.
     *
     * TODO (feature/tcp): implementar este método.
     *  Formato: "cedula=XXXXXXXXX&formato=json"
     *
     * @param lineaTexto línea recibida del cliente
     * @return           SolicitudPadron con los campos extraídos
     */
    private SolicitudPadron parsearSolicitud(String lineaTexto) {
        // TODO: split por '&', luego por '=', poblar SolicitudPadron
        return null;
    }

    // ---------------------------------------------------------------
    // Getters de estado
    // ---------------------------------------------------------------

    public boolean isCorriendo() { return corriendo; }
    public int     getPuerto()   { return puerto; }
}
