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
 * Formato esperado (una persona por línea, separado por |):
 *   cedula|codElectoral|nombre|primerApellido|segundoApellido
 */
public class RepositorioPadron {

    private static final String DELIMITADOR = ",";

    /** Índice principal: cédula → Persona. Cargado una sola vez al inicio. */
    private final Map<String, Persona> indice = new HashMap<>();

    private final String rutaArchivo;

    public RepositorioPadron(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    /**
     * Carga el padrón desde el archivo de texto en memoria.
     * Debe llamarse una sola vez al iniciar la aplicación.
     */
    public void cargarDesdeArchivo() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                Persona p = parsearLinea(linea);
                if (p != null) {
                    indice.put(p.getCedula().trim(), p);
                }
            }
        }
        System.out.println("Padron cargado: " + indice.size() + " personas.");
    }

    /**
     * Busca una persona por su número de cédula.
     *
     * @param cedula número de cédula
     * @return       la Persona encontrada, o null si no existe
     */
    public Persona buscarPorCedula(String cedula) {
        if (cedula == null) return null;
        return indice.get(cedula.trim());
    }

    /** Retorna cuántas personas están cargadas en memoria. */
    public int totalPersonas() {
        return indice.size();
    }

    /**
     * Parsea una línea de PADRON.txt y retorna una Persona.
     *
     * Formato TSE (separado por comas):
     *   [0] cedula      [1] codelec   [2] relleno  [3] fechacaduc
     *   [4] junta       [5] nombre    [6] primerApellido  [7] segundoApellido
     */
    private Persona parsearLinea(String linea) {
        if (linea == null || linea.isBlank()) return null;
        String[] partes = linea.split(DELIMITADOR);
        if (partes.length < 8) return null;
        try {
            return new Persona(
                partes[0].trim(),  // cedula
                partes[1].trim(),  // codElectoral
                partes[5].trim(),  // nombre
                partes[6].trim(),  // primerApellido
                partes[7].trim()   // segundoApellido
            );
        } catch (Exception e) {
            System.err.println("Linea malformada (ignorada): " + linea);
            return null;
        }
    }
}
