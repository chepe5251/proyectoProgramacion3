package com.padron.datos;

import com.padron.entidades.Direccion;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Repositorio que carga y consulta el archivo distelec.txt.
 * Resuelve los códigos numéricos (provincia, cantón, distrito) a nombres.
 *
 * RAMA:  feature/repositorios
 * OWNER: Desarrollador 2
 *
 * Formato esperado de distelec.txt (una entrada por línea):
 *   codigoProvincia,codigoCanton,codigoDistrito,nombreProvincia,nombreCanton,nombreDistrito
 *
 * TODO (feature/repositorios):
 *  - Implementar cargarDesdeArchivo()
 *  - Implementar buscarDireccion()
 *  - Aclarar con el equipo el delimitador exacto del archivo distelec.txt
 */
public class RepositorioDistelec {

    /**
     * Clave del índice: "provincia-canton-distrito"  (ej: "1-1-1").
     * Valor: Direccion con los nombres resueltos.
     */
    private final Map<String, Direccion> indice = new HashMap<>();

    private final String rutaArchivo;

    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------

    public RepositorioDistelec(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    // ---------------------------------------------------------------
    // Métodos públicos
    // ---------------------------------------------------------------

    /**
     * Carga el archivo distelec.txt en memoria.
     * Debe llamarse una sola vez al iniciar la aplicación.
     *
     * TODO (feature/repositorios): implementar este método.
     *
     * @throws IOException si el archivo no existe o no se puede leer
     */
    public void cargarDesdeArchivo() throws IOException {
        // TODO: abrir BufferedReader sobre rutaArchivo
        // TODO: parsear cada línea con parsearLinea()
        // TODO: insertar en indice con clave generada por buildClave()
        throw new UnsupportedOperationException("Pendiente de implementación.");
    }

    /**
     * Retorna la Direccion que corresponde a los códigos dados.
     *
     * TODO (feature/repositorios): implementar este método.
     *
     * @return Direccion con nombres resueltos, o null si no existe
     */
    public Direccion buscarDireccion(int codigoProvincia, int codigoCanton, int codigoDistrito) {
        // TODO: return indice.get(buildClave(codigoProvincia, codigoCanton, codigoDistrito));
        throw new UnsupportedOperationException("Pendiente de implementación.");
    }

    /** Retorna cuántas entradas de distelec están en memoria. */
    public int totalEntradas() {
        return indice.size();
    }

    // ---------------------------------------------------------------
    // Métodos privados de parsing
    // ---------------------------------------------------------------

    /**
     * Genera la clave del índice a partir de los tres códigos.
     * Ejemplo: buildClave(1, 1, 1) → "1-1-1"
     */
    private String buildClave(int provincia, int canton, int distrito) {
        return provincia + "-" + canton + "-" + distrito;
    }

    /**
     * Parsea una línea de distelec.txt y retorna una Direccion.
     *
     * TODO (feature/repositorios): implementar este método.
     *  - Retornar null si la línea está malformada
     */
    private Direccion parsearLinea(String linea) {
        // TODO: implementar parsing
        return null;
    }
}
