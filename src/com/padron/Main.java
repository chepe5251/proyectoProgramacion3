package com.padron;

import com.padron.datos.RepositorioDistelec;
import com.padron.datos.RepositorioPadron;
import com.padron.logica.ServicioPadron;
import com.padron.presentacion.http.HttpServerPadron;
import com.padron.presentacion.tcp.TcpServer;
import com.padron.util.Serializador;

/**
 * Punto de entrada principal de la aplicación.
 *
 * RAMA:  main / feature/integracion (último en mergearse)
 * OWNER: Líder del equipo o Desarrollador 1
 *
 * TODO (integración final):
 *  - Leer puertos y rutas de archivos desde build.properties o args[]
 *  - Iniciar TCP y HTTP en threads separados
 *  - Agregar shutdown hook para detener servidores limpiamente
 */
public class Main {

    // Configuración por defecto (puede venir de build.properties)
    private static final int    PUERTO_TCP   = 5000;
    private static final int    PUERTO_HTTP  = 8080;
    private static final String RUTA_PADRON    = "resources/data/PADRON.txt";
    private static final String RUTA_DISTELEC  = "resources/data/distelec.txt";

    public static void main(String[] args) throws Exception {

        System.out.println("=== Sistema de Consulta de Padron Electoral ===");

        // 1. Cargar repositorios de datos
        RepositorioPadron   repoPadron   = new RepositorioPadron(RUTA_PADRON);
        RepositorioDistelec repoDistelec = new RepositorioDistelec(RUTA_DISTELEC);

        // TODO: descomentar cuando feature/repositorios esté mergeado
        // repoPadron.cargarDesdeArchivo();
        // repoDistelec.cargarDesdeArchivo();
        // System.out.println("Padron cargado: " + repoPadron.totalPersonas() + " personas.");

        // 2. Construir capa de lógica
        ServicioPadron servicio     = new ServicioPadron(repoPadron, repoDistelec);
        Serializador   serializador = new Serializador();

        // 3. Iniciar servidores
        // TODO: descomentar cuando feature/tcp y feature/http estén mergeados
        // TcpServer tcpServer = new TcpServer(PUERTO_TCP, servicio, serializador);
        // HttpServerPadron httpServer = new HttpServerPadron(PUERTO_HTTP, servicio, serializador);

        // Thread hiloTcp  = new Thread(tcpServer::iniciar);
        // Thread hiloHttp = new Thread(httpServer::iniciar);
        // hiloTcp.start();
        // hiloHttp.start();

        System.out.println("Servidor TCP  escuchando en puerto: " + PUERTO_TCP  + "  [PENDIENTE]");
        System.out.println("Servidor HTTP escuchando en puerto: " + PUERTO_HTTP + "  [PENDIENTE]");
        System.out.println("El proyecto compila correctamente. Implementaciones pendientes en cada feature.");
    }
}
