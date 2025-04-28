package mindcare.ui;

import javax.swing.*;
import mindcare.GestorArchivos;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AtenderCitaFrame extends JFrame {

    private int idPsicologo;
    private JComboBox<String> citasComboBox;
    private JTextArea notaArea;
    private JTextArea diagnosticoArea;
    private List<Integer> idsCitasDisponibles;

    public AtenderCitaFrame(int idPsicologo) {
        this.idPsicologo = idPsicologo;

        setTitle("Atender Cita");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Selector de citas
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        JLabel citaLabel = new JLabel("Seleccione una cita:");
        citasComboBox = new JComboBox<>();
        cargarCitasAgendadas();
        topPanel.add(citaLabel, BorderLayout.NORTH);
        topPanel.add(citasComboBox, BorderLayout.CENTER);

        // Centro: áreas de texto
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        // Panel para notas
        JPanel notaPanel = new JPanel(new BorderLayout(5, 5));
        JLabel notaLabel = new JLabel("Notas:");
        notaArea = new JTextArea();
        notaArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        notaArea.setLineWrap(true);
        notaArea.setWrapStyleWord(true);
        JScrollPane notaScroll = new JScrollPane(notaArea);

        notaPanel.add(notaLabel, BorderLayout.NORTH);
        notaPanel.add(notaScroll, BorderLayout.CENTER);

        // Panel para diagnóstico
        JPanel diagnosticoPanel = new JPanel(new BorderLayout(5, 5));
        JLabel diagnosticoLabel = new JLabel("Diagnóstico:");
        diagnosticoArea = new JTextArea();
        diagnosticoArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        diagnosticoArea.setLineWrap(true);
        diagnosticoArea.setWrapStyleWord(true);
        JScrollPane diagnosticoScroll = new JScrollPane(diagnosticoArea);

        diagnosticoPanel.add(diagnosticoLabel, BorderLayout.NORTH);
        diagnosticoPanel.add(diagnosticoScroll, BorderLayout.CENTER);

        centerPanel.add(notaPanel);
        centerPanel.add(diagnosticoPanel);

        // Botones
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton guardarButton = new JButton("Guardar");
        JButton cancelarButton = new JButton("Cancelar");

        guardarButton.addActionListener(e -> guardarAtencion());
        cancelarButton.addActionListener(e -> dispose());

        bottomPanel.add(guardarButton);
        bottomPanel.add(cancelarButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void cargarCitasAgendadas() {
        idsCitasDisponibles = new ArrayList<>();
        for (String linea : GestorArchivos.leerArchivo("citas.txt")) {
            String[] partes = linea.split(",");
            if (partes.length >= 6) {
                int idCita = Integer.parseInt(partes[0]);
                String fecha = partes[1];
                String hora = partes[2];
                int idPaciente = Integer.parseInt(partes[3]);
                int idPsicologoArchivo = Integer.parseInt(partes[4]);
                String estado = partes[5];

                if (idPsicologoArchivo == idPsicologo && estado.equals("agendada")) {
                    String nombrePaciente = obtenerNombrePaciente(idPaciente);
                    citasComboBox.addItem(idCita + " - " + fecha + " a las " + hora + " - " + nombrePaciente);
                    idsCitasDisponibles.add(idCita);
                }
            }
        }
    }

    private String obtenerNombrePaciente(int idPaciente) {
        for (String linea : GestorArchivos.leerArchivo("pacientes.txt")) {
            String[] partes = linea.split(",");
            if (partes.length >= 2) {
                int idArchivo = Integer.parseInt(partes[0]);
                if (idArchivo == idPaciente) {
                    return partes[1];
                }
            }
        }
        return "Desconocido";
    }

    private void guardarAtencion() {
        int selectedIndex = citasComboBox.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "❌ Debe seleccionar una cita.");
            return;
        }

        String nota = notaArea.getText().trim();
        String diagnostico = diagnosticoArea.getText().trim();
        if (nota.isEmpty() && diagnostico.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Debe escribir al menos una nota o diagnóstico.");
            return;
        }

        int idCita = idsCitasDisponibles.get(selectedIndex);

        actualizarEstadoCita(idCita);
        actualizarHistorial(idCita, nota, diagnostico);

        JOptionPane.showMessageDialog(this, "✅ Cita atendida y historial actualizado.");
        dispose();
    }

    private void actualizarEstadoCita(int idCita) {
        List<String> nuevasCitas = new ArrayList<>();
        for (String linea : GestorArchivos.leerArchivo("citas.txt")) {
            String[] partes = linea.split(",");
            if (partes.length >= 6) {
                int idArchivo = Integer.parseInt(partes[0]);
                if (idArchivo == idCita) {
                    partes[5] = "atendida";
                    linea = String.join(",", partes);
                }
            }
            nuevasCitas.add(linea);
        }

        GestorArchivos.limpiarArchivo("citas.txt");
        for (String nuevaLinea : nuevasCitas) {
            GestorArchivos.guardarEnArchivo("citas.txt", nuevaLinea);
        }
    }

    private void actualizarHistorial(int idCita, String nota, String diagnostico) {
        int idPaciente = -1;
        for (String linea : GestorArchivos.leerArchivo("citas.txt")) {
            String[] partes = linea.split(",");
            if (partes.length >= 6) {
                int idArchivo = Integer.parseInt(partes[0]);
                if (idArchivo == idCita) {
                    idPaciente = Integer.parseInt(partes[3]);
                    break;
                }
            }
        }

        if (idPaciente == -1) return;

        List<String> nuevosHistoriales = new ArrayList<>();
        boolean historialActualizado = false;

        for (String linea : GestorArchivos.leerArchivo("historiales.txt")) {
            String[] partes = linea.split(",");
            if (partes.length >= 4) {
                int idHistorial = Integer.parseInt(partes[0]);
                if (idHistorial == idPaciente) {
                    if (!nota.isEmpty()) {
                        partes[1] += ";" + nota;
                    }
                    if (!diagnostico.isEmpty()) {
                        partes[2] += ";" + diagnostico;
                    }
                    partes[3] += ";" + idCita;
                    linea = String.join(",", partes);
                    historialActualizado = true;
                }
            }
            nuevosHistoriales.add(linea);
        }

        if (!historialActualizado) {
            String nuevoHistorial = idPaciente + "," + (nota.isEmpty() ? "" : nota) + "," + (diagnostico.isEmpty() ? "" : diagnostico) + "," + idCita;
            nuevosHistoriales.add(nuevoHistorial);
        }

        GestorArchivos.limpiarArchivo("historiales.txt");
        for (String nuevaLinea : nuevosHistoriales) {
            GestorArchivos.guardarEnArchivo("historiales.txt", nuevaLinea);
        }
    }
}
