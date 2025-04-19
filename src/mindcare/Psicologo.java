/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mindcare;

/**
 *
 * @author MAOZ-
 */
import java.util.ArrayList;
import java.util.List;

public class Psicologo extends Usuario {
    private String especialidad;
    private List<String> disponibilidad;
    private List<Integer> citasAsignadas;

    // Constructor
    public Psicologo(int idUsuario, String nombre, String correo, String clave, String especialidad) {
        super(idUsuario, nombre, correo, clave, "psicologo");
        this.especialidad = especialidad;
        this.disponibilidad = new ArrayList<>();
        this.citasAsignadas = new ArrayList<>();
    }

    // Getters y Setters
    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public List<String> getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(List<String> disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public List<Integer> getCitasAsignadas() {
        return citasAsignadas;
    }

    public void setCitasAsignadas(List<Integer> citasAsignadas) {
        this.citasAsignadas = citasAsignadas;
    }

    // Métodos funcionales
    public void agregarDisponibilidad(String nuevaFechaHora) {
        disponibilidad.add(nuevaFechaHora);
    }

    public void verAgenda() {
        System.out.println("Agenda de " + getNombre() + ": " + citasAsignadas);
    }

    public void atenderCita(int idCita) {
        System.out.println("Atendiendo cita con ID: " + idCita);
        // Aquí iría la lógica real de atención
    }

    public void verPacientesAtendidos() {
        System.out.println("Pacientes atendidos por " + getNombre() + ":");
        for (int idCita : citasAsignadas) {
            System.out.println("- ID Cita: " + idCita);
        }
    }

    // toString() para guardar en .txt
    @Override
    public String toString() {
        String disp = String.join(";", disponibilidad);
        String citas = String.join(";", citasAsignadas.stream()
                                .map(String::valueOf)
                                .toArray(String[]::new));
        return super.toString() + "," + especialidad + "," + disp + "," + citas;
    }
}

