package com.padron.datos;

import com.padron.entidades.Direccion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Repositorio que carga y consulta el archivo distelec.txt.
 * Resuelve el codElectoral a nombres de provincia, cantón y distrito.
 *
 * Formato esperado (una entrada por línea, separado por |):
 *   codElectoral|provincia|canton|distrito
 */
public class RepositorioDistelec {

    private static final String DELIMITADOR = "\\|";

    /** Índice: codElectoral → Direccion. */
    private final Map<String, Direccion> indice = new HashMap<>();

    private final String rutaArchivo;

    public RepositorioDistelec(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    /**
     * Carga el archivo distelec.txt en memoria.
     * Debe llamarse una sola vez al iniciar la aplicación.
     */
    public void cargarDesdeArchivo() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                Direccion d = parsearLinea(linea);
                if (d != null) {
                    indice.put(d.getCodElectoral().trim(), d);
                }
            }
        }
        System.out.println("Distelec cargado: " + indice.size() + " distritos.");
    }

    /**
     * Retorna la Direccion que corresponde al código electoral dado.
     *
     * @param codElectoral código electoral de la persona
     * @return             Direccion con nombres resueltos, o null si no existe
     */
    public Direccion buscarPorCodElectoral(String codElectoral) {
        if (codElectoral == null) return null;
        return indice.get(codElectoral.trim());
    }

    /** Retorna cuántas entradas de distelec están en memoria. */
    public int totalEntradas() {
        return indice.size();
    }

    /**
     * Parsea una línea de distelec.txt y retorna una Direccion.
     * Formato: codElectoral|provincia|canton|distrito
     */
    private Direccion parsearLinea(String linea) {
        if (linea == null || linea.isBlank()) return null;
        String[] partes = linea.split(DELIMITADOR);
        if (partes.length < 4) return null;
        try {
            return new Direccion(
                partes[0].trim(),
                partes[1].trim(),
                partes[2].trim(),
                partes[3].trim()
            );
        } catch (Exception e) {
            System.err.println("Linea malformada (ignorada): " + linea);
            return null;
        }
    }
}
