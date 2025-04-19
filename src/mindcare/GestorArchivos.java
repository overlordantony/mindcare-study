/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mindcare;

/**
 *
 * @author MAOZ-
 */
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class GestorArchivos {

    // Método para guardar en un archivo
    public static void guardarEnArchivo(String nombreArchivo, String contenido) {
        try (FileWriter writer = new FileWriter(nombreArchivo, true)) {
            writer.write(contenido + "\n");
            System.out.println("✅ Guardado en " + nombreArchivo);
        } catch (IOException e) {
            System.out.println("❌ Error al guardar en " + nombreArchivo + ": " + e.getMessage());
        }
    }

    // Método para leer un archivo y devolver las líneas como una lista
    public static List<String> leerArchivo(String nombreArchivo) {
        List<String> lineas = new ArrayList<>();
        File archivo = new File(nombreArchivo);

        if (!archivo.exists()) {
            return lineas;  // Si el archivo no existe, devuelve lista vacía
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (IOException e) {
            System.out.println("❌ Error al leer " + nombreArchivo + ": " + e.getMessage());
        }

        return lineas;
    }

    // Método para actualizar un registro en el archivo
    public static void actualizarRegistro(String nombreArchivo, String viejoRegistro, String nuevoRegistro) {
        List<String> lineas = leerArchivo(nombreArchivo);
        boolean encontrado = false;

        try (FileWriter writer = new FileWriter(nombreArchivo, false)) {  // Sobrescribe el archivo
            for (String linea : lineas) {
                if (linea.equals(viejoRegistro)) {
                    writer.write(nuevoRegistro + "\n");
                    encontrado = true;
                    System.out.println("✅ Registro actualizado.");
                } else {
                    writer.write(linea + "\n");
                }
            }
            if (!encontrado) {
                System.out.println("❌ Registro no encontrado para actualizar.");
            }
        } catch (IOException e) {
            System.out.println("❌ Error al actualizar registro: " + e.getMessage());
        }
    }

    // Método para eliminar un registro en el archivo
    public static void eliminarRegistro(String nombreArchivo, String registroAEliminar) {
        List<String> lineas = leerArchivo(nombreArchivo);
        boolean eliminado = false;

        try (FileWriter writer = new FileWriter(nombreArchivo, false)) {  // Sobrescribe el archivo
            for (String linea : lineas) {
                if (!linea.equals(registroAEliminar)) {
                    writer.write(linea + "\n");
                } else {
                    eliminado = true;
                }
            }
            if (eliminado) {
                System.out.println("✅ Registro eliminado.");
            } else {
                System.out.println("❌ Registro no encontrado para eliminar.");
            }
        } catch (IOException e) {
            System.out.println("❌ Error al eliminar registro: " + e.getMessage());
        }
    }

    // Método para eliminar todo el contenido de un archivo
    public static void limpiarArchivo(String nombreArchivo) {
        try (FileWriter writer = new FileWriter(nombreArchivo, false)) {
            writer.write("");  // Sobrescribe el archivo con nada, lo limpia
            System.out.println("✅ Archivo " + nombreArchivo + " limpiado.");
        } catch (IOException e) {
            System.out.println("❌ Error al limpiar el archivo: " + e.getMessage());
        }
    }
}

