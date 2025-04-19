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

public class Historial {
    private int idHistorial;
    private List<String> notas;
    private List<String> diagnosticos;
    private List<Integer> citasRelacionadas;

    // Constructor
    public Historial(int idHistorial) {
        this.idHistorial = idHistorial;
        this.notas = new ArrayList<>();
        this.diagnosticos = new ArrayList<>();
        this.citasRelacionadas = new ArrayList<>();
    }

    // Getters y Setters
    public int getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(int idHistorial) {
        this.idHistorial = idHistorial;
    }

    public List<String> getNotas() {
        return notas;
    }

    public void setNotas(List<String> notas) {
        this.notas = notas;
    }

    public List<String> getDiagnosticos() {
        return diagnosticos;
    }

    public void setDiagnosticos(List<String> diagnosticos) {
        this.diagnosticos = diagnosticos;
    }

    public List<Integer> getCitasRelacionadas() {
        return citasRelacionadas;
    }

    public void setCitasRelacionadas(List<Integer> citasRelacionadas) {
        this.citasRelacionadas = citasRelacionadas;
    }

    // Métodos funcionales
    public void agregarNota(String nota) {
        notas.add(nota);
    }

    public void agregarDiagnostico(String diagnostico) {
        diagnosticos.add(diagnostico);
    }

    public void agregarCitaRelacionada(int idCita) {
        citasRelacionadas.add(idCita);
    }

    public void mostrarHistorial() {
        System.out.println("📑 Historial del paciente #" + idHistorial);
        System.out.println("Notas:");
        for (String nota : notas) {
            System.out.println("- " + nota);
        }
        System.out.println("Diagnósticos:");
        for (String diag : diagnosticos) {
            System.out.println("- " + diag);
        }
        System.out.println("Citas relacionadas: " + citasRelacionadas);
    }

    // toString() para guardar en .txt
    @Override
    public String toString() {
        String notasTxt = String.join(";", notas);
        String diagTxt = String.join(";", diagnosticos);
        String citasTxt = String.join(";", citasRelacionadas.stream()
                                .map(String::valueOf)
                                .toArray(String[]::new));
        return idHistorial + "," + notasTxt + "," + diagTxt + "," + citasTxt;
    }
}

