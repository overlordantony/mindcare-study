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

public class Paciente extends Usuario {
    private String fechaNacimiento;
    private List<Integer> citasAgendadas;
    private Historial historial;

    // Constructor
    public Paciente(int idUsuario, String nombre, String correo, String clave, String fechaNacimiento) {
        super(idUsuario, nombre, correo, clave, "paciente");
        this.fechaNacimiento = fechaNacimiento;
        this.citasAgendadas = new ArrayList<>();
        this.historial = new Historial(idUsuario); // idUsuario actúa como idPaciente
    }

    // Getters y Setters
    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public List<Integer> getCitasAgendadas() {
        return citasAgendadas;
    }

    public void setCitasAgendadas(List<Integer> citasAgendadas) {
        this.citasAgendadas = citasAgendadas;
    }

    public Historial getHistorial() {
        return historial;
    }

    public void setHistorial(Historial historial) {
        this.historial = historial;
    }

    // Métodos funcionales
    public void verCitas() {
        System.out.println("Citas agendadas del paciente " + getNombre() + ": " + citasAgendadas);
    }

    public void verHistorial() {
        historial.mostrarHistorial();
    }

    public void actualizarDatos(String nuevoNombre, String nuevoCorreo, String nuevaClave, String nuevaFechaNacimiento) {
        setNombre(nuevoNombre);
        setCorreo(nuevoCorreo);
        setClave(nuevaClave);
        setFechaNacimiento(nuevaFechaNacimiento);
    }

    // toString() para guardar en .txt
    @Override
    public String toString() {
        // citasAgendadas como una sola línea separada por ";"
        String citas = String.join(";", citasAgendadas.stream()
                                .map(String::valueOf)
                                .toArray(String[]::new));
        return super.toString() + "," + fechaNacimiento + "," + citas;
    }
}
