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
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        Color buttonColor = Color.decode("#1E90FF");
        Color cancelColor = Color.decode("#E51A4C");
        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 16);

        UIManager.put("ComboBox.background", Color.WHITE);
        UIManager.put("ComboBox.selectionBackground", buttonColor);
        UIManager.put("ComboBox.foreground", Color.BLACK);
        UIManager.put("ComboBox.selectionForeground", Color.WHITE);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel tituloLabel = new JLabel("Agendar Nueva Cita", SwingConstants.CENTER);
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(tituloLabel);
        mainPanel.add(Box.createVerticalStrut(20));

        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new GridLayout(4, 1, 0, 8));

        JLabel psicologoLabel = new JLabel("Seleccione Psicólogo:");
        psicologoComboBox = new JComboBox<>(obtenerNombresPsicologos());
        psicologoComboBox.setBackground(Color.WHITE);

        JLabel disponibilidadLabel = new JLabel("Seleccione Disponibilidad:");
        disponibilidadComboBox = new JComboBox<>();
        disponibilidadComboBox.setBackground(Color.WHITE);

        formPanel.add(psicologoLabel);
        formPanel.add(psicologoComboBox);
        formPanel.add(disponibilidadLabel);
        formPanel.add(disponibilidadComboBox);

        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(30));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.setBackground(Color.WHITE);

        JButton agendarButton = createStyledButton("Agendar", buttonColor, buttonFont);
        JButton volverButton = createStyledButton("Volver", cancelColor, buttonFont);

        agendarButton.addActionListener(e -> agendarCita());
        volverButton.addActionListener(e -> dispose());

        Dimension buttonSize = new Dimension(150, 35);
        agendarButton.setPreferredSize(buttonSize);
        agendarButton.setMaximumSize(buttonSize);
        volverButton.setPreferredSize(buttonSize);
        volverButton.setMaximumSize(buttonSize);

        buttonsPanel.add(Box.createHorizontalGlue());
        buttonsPanel.add(agendarButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonsPanel.add(volverButton);
        buttonsPanel.add(Box.createHorizontalGlue());

        mainPanel.add(buttonsPanel);

        add(mainPanel);
        setVisible(true);

        actualizarDisponibilidad();
        psicologoComboBox.addActionListener(e -> actualizarDisponibilidad());
    }

    private JButton createStyledButton(String text, Color bgColor, Font font) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g2);
                g2.dispose();
            }

            @Override
            public void setContentAreaFilled(boolean b) {}
        };

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(font);
        return button;
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
            if (partes.length >= 2 && partes[1].equals(nombre)) {
                return Integer.parseInt(partes[0]);
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
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idPsicologo = obtenerIdPsicologoPorNombre(nombrePsicologo);
        if (idPsicologo == -1) {
            JOptionPane.showMessageDialog(this, "Error al obtener el psicólogo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String fechaHoraOriginal = disponibilidadSeleccionada.replace(" a las ", "H");
        String[] partesFechaHora = fechaHoraOriginal.split("H");
        if (partesFechaHora.length != 2) {
            JOptionPane.showMessageDialog(this, "Formato de fecha y hora incorrecto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String fecha = partesFechaHora[0];
        String hora = partesFechaHora[1];

        int nuevoIdCita = obtenerNuevoIdCita();
        Cita nuevaCita = new Cita(nuevoIdCita, fecha, hora, idPaciente, idPsicologo);
        GestorArchivos.guardarEnArchivo("citas.txt", nuevaCita.toString());
        eliminarDisponibilidad(idPsicologo, fechaHoraOriginal);

        JOptionPane.showMessageDialog(this, "Cita agendada exitosamente.");
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
