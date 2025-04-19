/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mindcare;

/**
 *
 * @author MAOZ-
 */
public class Cita {
    private int idCita;
    private String fecha;
    private String hora;
    private int idPaciente;
    private int idPsicologo;
    private String estado; // "agendada", "cancelada", "atendida"

    // Constructor
    public Cita(int idCita, String fecha, String hora, int idPaciente, int idPsicologo) {
        this.idCita = idCita;
        this.fecha = fecha;
        this.hora = hora;
        this.idPaciente = idPaciente;
        this.idPsicologo = idPsicologo;
        this.estado = "agendada";
    }

    // Getters y Setters
    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public int getIdPsicologo() {
        return idPsicologo;
    }

    public void setIdPsicologo(int idPsicologo) {
        this.idPsicologo = idPsicologo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Métodos funcionales
    public void mostrarDetalle() {
        System.out.println("Cita #" + idCita);
        System.out.println("Fecha: " + fecha + " | Hora: " + hora);
        System.out.println("Paciente ID: " + idPaciente);
        System.out.println("Psicólogo ID: " + idPsicologo);
        System.out.println("Estado: " + estado);
    }

    public void reprogramar(String nuevaFecha, String nuevaHora) {
        this.fecha = nuevaFecha;
        this.hora = nuevaHora;
        System.out.println("Cita reprogramada.");
    }

    public void cancelar() {
        this.estado = "cancelada";
        System.out.println("Cita cancelada.");
    }

    // toString() para guardar en .txt
    @Override
    public String toString() {
        return idCita + "," + fecha + "," + hora + "," + idPaciente + "," + idPsicologo + "," + estado;
    }
}

