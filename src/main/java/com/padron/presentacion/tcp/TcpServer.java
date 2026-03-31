package com.padron.presentacion.tcp;

import com.padron.dto.FormatoSalida;
import com.padron.dto.RespuestaPadron;
import com.padron.dto.SolicitudPadron;
import com.padron.logica.ServicioPadron;
import com.padron.util.Serializador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Servidor TCP que escucha solicitudes de consulta al padrón.
 *
 * Protocolo (texto plano por socket):
 *   Solicitud del cliente:  "cedula=109870456&formato=json\n"
 *   Respuesta del servidor: JSON o XML según formato pedido
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
     * @throws IOException si no se puede abrir el puerto
     */
    public void iniciar() throws IOException {
        serverSocket = new ServerSocket(puerto);
        corriendo = true;
        System.out.println("Servidor TCP escuchando en puerto " + puerto);
        while (corriendo) {
            try {
                Socket cliente = serverSocket.accept();
                new Thread(() -> manejarCliente(cliente)).start();
            } catch (IOException e) {
                if (!corriendo) break;
                System.err.println("Error aceptando conexion TCP: " + e.getMessage());
            }
        }
    }

    /**
     * Detiene el servidor cerrando el ServerSocket.
     */
    public void detener() {
        corriendo = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try { serverSocket.close(); } catch (IOException ignored) {}
        }
    }

    // ---------------------------------------------------------------
    // Manejo de clientes
    // ---------------------------------------------------------------

    /**
     * Maneja una conexión individual de un cliente en su propio Thread.
     *
     * @param cliente socket del cliente conectado
     */
    private void manejarCliente(Socket cliente) {
        try (
            BufferedReader entrada = new BufferedReader(
                new InputStreamReader(cliente.getInputStream()));
            PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true)
        ) {
            String lineaTexto = entrada.readLine();
            SolicitudPadron solicitud = parsearSolicitud(lineaTexto);
            RespuestaPadron respuesta = servicio.consultarPadron(solicitud);
            salida.println(serializador.serializar(respuesta, solicitud.getFormato()));
        } catch (IOException e) {
            System.err.println("Error atendiendo cliente TCP: " + e.getMessage());
        } finally {
            try { cliente.close(); } catch (IOException ignored) {}
        }
    }

    /**
     * Parsea la línea de texto recibida por TCP en una SolicitudPadron.
     * Formato: "cedula=XXXXXXXXX&formato=json"
     *
     * @param lineaTexto línea recibida del cliente
     * @return           SolicitudPadron con los campos extraídos
     */
    private SolicitudPadron parsearSolicitud(String lineaTexto) {
        SolicitudPadron solicitud = new SolicitudPadron();
        if (lineaTexto == null || lineaTexto.isBlank()) return solicitud;
        for (String par : lineaTexto.trim().split("&")) {
            String[] kv = par.split("=", 2);
            if (kv.length < 2) continue;
            switch (kv[0].trim()) {
                case "cedula":  solicitud.setCedula(kv[1].trim()); break;
                case "formato":
                    try { solicitud.setFormato(FormatoSalida.fromString(kv[1].trim())); }
                    catch (IllegalArgumentException e) { /* mantener default JSON */ }
                    break;
            }
        }
        return solicitud;
    }

    // ---------------------------------------------------------------
    // Getters de estado
    // ---------------------------------------------------------------

    public boolean isCorriendo() { return corriendo; }
    public int     getPuerto()   { return puerto; }
}
