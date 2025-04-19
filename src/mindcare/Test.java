/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mindcare;

/**
 *
 * @author MAOZ-
 */
public class Test {

    public static void main(String[] args) {
        // Crear un psicólogo
        Psicologo p1 = new Psicologo(1, "Ana Torres", "ana@correo.com", "1234", "Clínica");
        p1.agregarDisponibilidad("17-04-2025H09:00");
        p1.agregarDisponibilidad("17-04-2025H11:00");
        
        // Guardar el psicólogo en el archivo
        GestorArchivos.guardarEnArchivo("psicologos.txt", p1.toString());
        
        // Crear un paciente
        Paciente pac1 = new Paciente(2, "Luis Pérez", "luis@correo.com", "abcd", "1995-06-21");
        
        // Guardar el paciente en el archivo
        GestorArchivos.guardarEnArchivo("pacientes.txt", pac1.toString());
        
        // Crear una cita
        Cita cita1 = new Cita(100, "17-04-2025", "09:00", 2, 1);
        
        // Guardar la cita en el archivo
        GestorArchivos.guardarEnArchivo("citas.txt", cita1.toString());
        
        // Ver si los datos se guardan correctamente en el archivo
        System.out.println("\n==== Leer psicólogos ====");
        for (String linea : GestorArchivos.leerArchivo("psicologos.txt")) {
            System.out.println(linea);
        }
        
        System.out.println("\n==== Leer pacientes ====");
        for (String linea : GestorArchivos.leerArchivo("pacientes.txt")) {
            System.out.println(linea);
        }
        
        System.out.println("\n==== Leer citas ====");
        for (String linea : GestorArchivos.leerArchivo("citas.txt")) {
            System.out.println(linea);
        }
        
        // Actualizar el psicólogo
        System.out.println("\n==== Actualizando Psicólogo ====");
        GestorArchivos.actualizarRegistro("psicologos.txt", p1.toString(), "1,Ana Torres,ana@correo.com,1234,Clínica,17-04-2025H09:00,17-04-2025H11:00");
        
        // Eliminar una cita
        System.out.println("\n==== Eliminando Cita ====");
        GestorArchivos.eliminarRegistro("citas.txt", cita1.toString());
    }
}

