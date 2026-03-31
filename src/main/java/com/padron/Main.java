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

        repoPadron.cargarDesdeArchivo();
        repoDistelec.cargarDesdeArchivo();

        // 2. Construir capa de lógica
        ServicioPadron servicio     = new ServicioPadron(repoPadron, repoDistelec);
        Serializador   serializador = new Serializador();

        // 3. Iniciar servidores en threads separados
        TcpServer        tcpServer  = new TcpServer(PUERTO_TCP, servicio, serializador);
        HttpServerPadron httpServer = new HttpServerPadron(PUERTO_HTTP, servicio, serializador);

        Thread hiloTcp  = new Thread(() -> {
            try { tcpServer.iniciar(); }
            catch (Exception e) { System.err.println("Error TCP: " + e.getMessage()); }
        });
        Thread hiloHttp = new Thread(() -> {
            try { httpServer.iniciar(); }
            catch (Exception e) { System.err.println("Error HTTP: " + e.getMessage()); }
        });

        // 4. Apagado limpio al detener el proceso (Ctrl+C)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nApagando servidores...");
            tcpServer.detener();
            httpServer.detener();
        }));

        hiloTcp.start();
        hiloHttp.start();

        System.out.println("Servidor TCP  listo en puerto: " + PUERTO_TCP);
        System.out.println("Servidor HTTP listo en puerto: " + PUERTO_HTTP);
        System.out.println("Presiona Ctrl+C para detener.");
    }
}
