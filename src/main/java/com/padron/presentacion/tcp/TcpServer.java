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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Servidor TCP que escucha solicitudes de consulta al padrón.
 *
 * Protocolo (texto plano por socket):
 *   Solicitud del cliente:  "GET|109870456|json\n"
 *   Respuesta del servidor: JSON o XML según formato pedido
 */
public class TcpServer {
    private static final int TAMANO_POOL = 20;

    private final int            puerto;
    private final ServicioPadron servicio;
    private final Serializador   serializador;
    private final ExecutorService poolClientes;

    private ServerSocket serverSocket;
    private boolean      corriendo = false;

    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------

    public TcpServer(int puerto, ServicioPadron servicio, Serializador serializador) {
        this.puerto       = puerto;
        this.servicio     = servicio;
        this.serializador = serializador;
        this.poolClientes = Executors.newFixedThreadPool(TAMANO_POOL);
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
                poolClientes.submit(() -> manejarCliente(cliente));
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
        poolClientes.shutdown();
        try {
            if (!poolClientes.awaitTermination(5, TimeUnit.SECONDS)) {
                poolClientes.shutdownNow();
            }
        } catch (InterruptedException e) {
            poolClientes.shutdownNow();
            Thread.currentThread().interrupt();
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
     * Formato esperado: "GET|XXXXXXXXX|json"
     *
     * @param lineaTexto línea recibida del cliente
     * @return           SolicitudPadron con los campos extraídos
     */
    private SolicitudPadron parsearSolicitud(String lineaTexto) {
        SolicitudPadron solicitud = new SolicitudPadron();
        if (lineaTexto == null || lineaTexto.isBlank()) return solicitud;

        String lineaNormalizada = lineaTexto.trim();
        String[] partes = lineaNormalizada.split("\\|", 3);
        if (partes.length == 3 && "GET".equalsIgnoreCase(partes[0].trim())) {
            solicitud.setCedula(partes[1].trim());

            FormatoSalida formato = FormatoSalida.fromString(partes[2].trim());
            if (formato != null) {
                solicitud.setFormato(formato);
            }
            return solicitud;
        }

        return parsearSolicitudLegacy(lineaNormalizada, solicitud);
    }

    private SolicitudPadron parsearSolicitudLegacy(String lineaTexto, SolicitudPadron solicitud) {
        for (String par : lineaTexto.split("&")) {
            String[] kv = par.split("=", 2);
            if (kv.length < 2) continue;
            switch (kv[0].trim()) {
                case "cedula":
                    solicitud.setCedula(kv[1].trim());
                    break;
                case "formato":
                    FormatoSalida formato = FormatoSalida.fromString(kv[1].trim());
                    if (formato != null) {
                        solicitud.setFormato(formato);
                    }
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
