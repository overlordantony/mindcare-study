package mindcare.ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import mindcare.GestorArchivos;
import mindcare.Cita;

public class AgendarCitaFrame extends JFrame {

    private JComboBox<String> psicologoComboBox;
    private JComboBox<String> disponibilidadComboBox;
    private int idPaciente;

    public AgendarCitaFrame(int idPaciente) {
        this.idPaciente = idPaciente;

        setTitle("MindCare - Agendar Cita");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel tituloLabel = new JLabel("Agendar Nueva Cita", SwingConstants.CENTER);
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        mainPanel.add(tituloLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel psicologoLabel = new JLabel("Seleccione Psicólogo:");
        psicologoComboBox = new JComboBox<>(obtenerNombresPsicologos());

        JLabel disponibilidadLabel = new JLabel("Seleccione Disponibilidad:");
        disponibilidadComboBox = new JComboBox<>();

        formPanel.add(psicologoLabel);
        formPanel.add(psicologoComboBox);
        formPanel.add(disponibilidadLabel);
        formPanel.add(disponibilidadComboBox);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 20, 0));

        JButton agendarButton = new JButton("Agendar");
        JButton volverButton = new JButton("Volver");

        agendarButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        volverButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        agendarButton.addActionListener(e -> agendarCita());
        volverButton.addActionListener(e -> dispose());

        buttonsPanel.add(agendarButton);
        buttonsPanel.add(volverButton);

        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);

        actualizarDisponibilidad();
        psicologoComboBox.addActionListener(e -> actualizarDisponibilidad());
    }

    private String[] obtenerNombresPsicologos() {
        List<String> nombres = new ArrayList<>();
        for (String linea : GestorArchivos.leerArchivo("psicologos.txt")) {
            String[] partes = linea.split(",");
            if (partes.length >= 2) {
                nombres.add(partes[1]);
            }
        }
        return nombres.toArray(new String[0]);
    }

    private int obtenerIdPsicologoPorNombre(String nombre) {
        for (String linea : GestorArchivos.leerArchivo("psicologos.txt")) {
            String[] partes = linea.split(",");
            if (partes.length >= 2) {
                if (partes[1].equals(nombre)) {
                    return Integer.parseInt(partes[0]);
                }
            }
        }
        return -1;
    }

    private void actualizarDisponibilidad() {
        disponibilidadComboBox.removeAllItems();
        String nombreSeleccionado = (String) psicologoComboBox.getSelectedItem();
        if (nombreSeleccionado == null) return;

        int idPsicologo = obtenerIdPsicologoPorNombre(nombreSeleccionado);
        if (idPsicologo == -1) return;

        for (String linea : GestorArchivos.leerArchivo("disponibilidades.txt")) {
            String[] partes = linea.split(",");
            if (partes.length >= 2) {
                int idArchivo = Integer.parseInt(partes[0]);
                String fechaHora = partes[1];
                if (idArchivo == idPsicologo) {
                    disponibilidadComboBox.addItem(fechaHora.replace("H", " a las "));
                }
            }
        }
    }

    private void agendarCita() {
        String nombrePsicologo = (String) psicologoComboBox.getSelectedItem();
        String disponibilidadSeleccionada = (String) disponibilidadComboBox.getSelectedItem();

        if (nombrePsicologo == null || disponibilidadSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "❌ Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idPsicologo = obtenerIdPsicologoPorNombre(nombrePsicologo);
        if (idPsicologo == -1) {
            JOptionPane.showMessageDialog(this, "❌ Error al obtener el psicólogo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Separar fecha y hora
        String fechaHoraOriginal = disponibilidadSeleccionada.replace(" a las ", "H");
        String[] partesFechaHora = fechaHoraOriginal.split("H");
        if (partesFechaHora.length != 2) {
            JOptionPane.showMessageDialog(this, "❌ Formato de fecha y hora incorrecto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String fecha = partesFechaHora[0];
        String hora = partesFechaHora[1];

        // Generar ID de cita
        int nuevoIdCita = obtenerNuevoIdCita();

        // Crear cita
        Cita nuevaCita = new Cita(nuevoIdCita, fecha, hora, idPaciente, idPsicologo);
        GestorArchivos.guardarEnArchivo("citas.txt", nuevaCita.toString());

        // Eliminar disponibilidad usada
        eliminarDisponibilidad(idPsicologo, fechaHoraOriginal);

        JOptionPane.showMessageDialog(this, "✅ Cita agendada exitosamente.");
        dispose();
    }

    private int obtenerNuevoIdCita() {
        int maxId = 0;
        for (String linea : GestorArchivos.leerArchivo("citas.txt")) {
            String[] partes = linea.split(",");
            if (partes.length >= 1) {
                int id = Integer.parseInt(partes[0]);
                if (id > maxId) {
                    maxId = id;
                }
            }
        }
        return maxId + 1;
    }

    private void eliminarDisponibilidad(int idPsicologo, String fechaHora) {
        List<String> nuevasDisponibilidades = new ArrayList<>();
        for (String linea : GestorArchivos.leerArchivo("disponibilidades.txt")) {
            String[] partes = linea.split(",");
            if (partes.length >= 2) {
                int idArchivo = Integer.parseInt(partes[0]);
                String fechaHoraArchivo = partes[1];
                if (!(idArchivo == idPsicologo && fechaHoraArchivo.equals(fechaHora))) {
                    nuevasDisponibilidades.add(linea);
                }
            }
        }

        GestorArchivos.limpiarArchivo("disponibilidades.txt");
        for (String nuevaLinea : nuevasDisponibilidades) {
            GestorArchivos.guardarEnArchivo("disponibilidades.txt", nuevaLinea);
        }
    }
}
