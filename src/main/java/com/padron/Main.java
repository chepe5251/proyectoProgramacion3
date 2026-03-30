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

    private static final int PUERTO_TCP  = 5000;
    private static final int PUERTO_HTTP = 8080;

    public static void main(String[] args) throws Exception {

        System.out.println("=== Sistema de Consulta de Padron Electoral ===");

        // Rutas a los archivos de datos — se pasan como argumentos al ejecutar:
        //   java -jar padron-electoral.jar <ruta_PADRON.txt> <ruta_distelec.txt>
        // Si no se pasan argumentos, usa las rutas por defecto del proyecto Maven.
        String rutaPadron   = args.length > 0 ? args[0] : "src/main/resources/data/PADRON.txt";
        String rutaDistelec = args.length > 1 ? args[1] : "src/main/resources/data/distelec.txt";

        System.out.println("Ruta PADRON.txt  : " + rutaPadron);
        System.out.println("Ruta distelec.txt: " + rutaDistelec);

        // 1. Cargar repositorios de datos
        RepositorioPadron   repoPadron   = new RepositorioPadron(rutaPadron);
        RepositorioDistelec repoDistelec = new RepositorioDistelec(rutaDistelec);

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
