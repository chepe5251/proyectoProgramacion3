package com.padron.datos;

import com.padron.entidades.Persona;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Repositorio que carga y consulta el archivo PADRON.txt.
 *
 * RAMA:  feature/repositorios
 * OWNER: Desarrollador 2
 *
 * Formato esperado de PADRON.txt (una persona por línea):
 *   cedula,nombre,primerApellido,segundoApellido,codigoProvincia,codigoCanton,codigoDistrito
 *
 * TODO (feature/repositorios):
 *  - Implementar cargarDesdeArchivo()
 *  - Implementar buscarPorCedula()
 *  - Manejar líneas malformadas con log de advertencia (no lanzar excepción)
 *  - Considerar si el índice debe ser un HashMap o un TreeMap
 */
public class RepositorioPadron {

    /** Índice principal: cédula → Persona. Cargado una sola vez al inicio. */
    private final Map<String, Persona> indice = new HashMap<>();

    private final String rutaArchivo;

    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------

    public RepositorioPadron(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    // ---------------------------------------------------------------
    // Métodos públicos
    // ---------------------------------------------------------------

    /**
     * Carga el padrón desde el archivo de texto en memoria.
     * Debe llamarse una sola vez al iniciar la aplicación.
     *
     * TODO (feature/repositorios): implementar este método.
     *
     * @throws IOException si el archivo no existe o no se puede leer
     */
    public void cargarDesdeArchivo() throws IOException {
        // TODO: abrir BufferedReader sobre rutaArchivo
        // TODO: parsear cada línea con parsearLinea()
        // TODO: insertar en indice con cédula como clave
        throw new UnsupportedOperationException("Pendiente de implementación.");
    }

    /**
     * Busca una persona por su número de cédula.
     *
     * TODO (feature/repositorios): implementar este método.
     *
     * @param cedula número de cédula (9 dígitos, sin guiones)
     * @return       la Persona encontrada, o null si no existe
     */
    public Persona buscarPorCedula(String cedula) {
        // TODO: normalizar cédula (trim, quitar guiones)
        // TODO: retornar indice.get(cedula)
        throw new UnsupportedOperationException("Pendiente de implementación.");
    }

    /**
     * Retorna cuántas personas están cargadas en memoria.
     * Útil para verificar que la carga fue exitosa.
     */
    public int totalPersonas() {
        return indice.size();
    }

    // ---------------------------------------------------------------
    // Métodos privados de parsing
    // ---------------------------------------------------------------

    /**
     * Parsea una línea de PADRON.txt y retorna una Persona.
     *
     * TODO (feature/repositorios): implementar este método.
     *  - Dividir por coma (o el delimitador real del archivo)
     *  - Crear y retornar objeto Persona
     *  - Retornar null si la línea está malformada
     */
    private Persona parsearLinea(String linea) {
        // TODO: implementar parsing
        return null;
    }
}
