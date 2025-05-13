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
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Colores y fuentes
        Color labelColor = Color.decode("#4B4B4B");
        Color buttonColor = Color.decode("#1E90FF");
        Color secondaryButtonColor = Color.decode("#E51A4C");
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 16);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Selector de citas
        JLabel citaLabel = new JLabel("Seleccione una cita:");
        citaLabel.setForeground(labelColor);
        citaLabel.setFont(labelFont);
        citaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        citasComboBox = new JComboBox<>();
        citasComboBox.setMaximumSize(new Dimension(500, 30));
        cargarCitasAgendadas();

        mainPanel.add(citaLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(citasComboBox);
        mainPanel.add(Box.createVerticalStrut(20));

        // Notas
        JLabel notaLabel = new JLabel("Notas:");
        notaLabel.setForeground(labelColor);
        notaLabel.setFont(labelFont);
        notaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        notaArea = new JTextArea(5, 40);
        notaArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        notaArea.setLineWrap(true);
        notaArea.setWrapStyleWord(true);
        JScrollPane notaScroll = new JScrollPane(notaArea);

        mainPanel.add(notaLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(notaScroll);
        mainPanel.add(Box.createVerticalStrut(20));

        // Diagnóstico
        JLabel diagnosticoLabel = new JLabel("Diagnóstico:");
        diagnosticoLabel.setForeground(labelColor);
        diagnosticoLabel.setFont(labelFont);
        diagnosticoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        diagnosticoArea = new JTextArea(5, 40);
        diagnosticoArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        diagnosticoArea.setLineWrap(true);
        diagnosticoArea.setWrapStyleWord(true);
        JScrollPane diagnosticoScroll = new JScrollPane(diagnosticoArea);

        mainPanel.add(diagnosticoLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(diagnosticoScroll);
        mainPanel.add(Box.createVerticalStrut(30));

        // Botones en horizontal
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonsPanel.setBackground(Color.WHITE);

        JButton guardarButton = createStyledButton("Guardar", buttonColor, buttonFont);
        JButton cancelarButton = createStyledButton("Cancelar", secondaryButtonColor, buttonFont);

        guardarButton.addActionListener(e -> guardarAtencion());
        cancelarButton.addActionListener(e -> dispose());

        buttonsPanel.add(guardarButton);
        buttonsPanel.add(cancelarButton);

        mainPanel.add(buttonsPanel);

        add(mainPanel);
        setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor, Font font) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g2);
                g2.dispose();
            }

            @Override
            public void setContentAreaFilled(boolean b) {}
        };

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(font);
        button.setPreferredSize(new Dimension(200, 45));
        return button;
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
            JOptionPane.showMessageDialog(this, "Debe seleccionar una cita.");
            return;
        }

        String nota = notaArea.getText().trim();
        String diagnostico = diagnosticoArea.getText().trim();
        if (nota.isEmpty() && diagnostico.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe escribir al menos una nota o diagnóstico.");
            return;
        }

        int idCita = idsCitasDisponibles.get(selectedIndex);

        actualizarEstadoCita(idCita);
        actualizarHistorial(idCita, nota, diagnostico);

        JOptionPane.showMessageDialog(this, "Cita atendida y historial actualizado.");
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
            String nuevoHistorial = idPaciente + "," + 
                (nota.isEmpty() ? "" : nota) + "," + 
                (diagnostico.isEmpty() ? "" : diagnostico) + "," + 
                idCita;
            nuevosHistoriales.add(nuevoHistorial);
        }

        GestorArchivos.limpiarArchivo("historiales.txt");
        for (String nuevaLinea : nuevosHistoriales) {
            GestorArchivos.guardarEnArchivo("historiales.txt", nuevaLinea);
        }
    }
}
