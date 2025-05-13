package mindcare.ui;

import javax.swing.*;
import mindcare.GestorArchivos;
import java.awt.*;

public class PacienteMenuFrame extends JFrame {

    private String nombrePaciente;
    private int idPaciente;
    private JTextArea textArea;

    public PacienteMenuFrame(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
        this.idPaciente = obtenerIdPacienteDesdeNombre(nombrePaciente);

        setIconImage(Toolkit.getDefaultToolkit().getImage("src/mindcare/ui/assets/logo.png"));

        setTitle("MindCare - Menú Paciente");
        setSize(1100, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        Color labelColor = Color.decode("#4B4B4B");
        Color buttonColor = Color.decode("#1E90FF");
        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 16);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Logo
        ImageIcon logoIcon = new ImageIcon("src/mindcare/ui/assets/logo.png");
        Image image = logoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(image));
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoPanel.setBackground(Color.WHITE);
        logoPanel.add(logoLabel);

        // Bienvenida
        JLabel bienvenidaLabel = new JLabel("Bienvenido, " + this.nombrePaciente);
        bienvenidaLabel.setForeground(labelColor);
        bienvenidaLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bienvenidaLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(bienvenidaLabel, BorderLayout.CENTER);
        topPanel.add(logoPanel, BorderLayout.EAST);

        // Botones
        JPanel buttonsWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonsWrapper.setBackground(Color.WHITE);

        JButton agendarCitaButton = createStyledButton("Agendar Cita", buttonColor, buttonFont);
        JButton verCitasButton = createStyledButton("Ver Citas Agendadas", buttonColor, buttonFont);
        JButton verHistorialButton = createStyledButton("Ver Historial Médico", buttonColor, buttonFont);
        JButton cerrarSesionButton = createStyledButton("Cerrar Sesión", buttonColor, buttonFont);

        buttonsWrapper.add(agendarCitaButton);
        buttonsWrapper.add(verCitasButton);
        buttonsWrapper.add(verHistorialButton);
        buttonsWrapper.add(cerrarSesionButton);

        // Área de Texto
        textArea = new JTextArea("Aquí se mostrará la información de citas o historial...");
        textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(1000, 300));

        // Centro
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(buttonsWrapper, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);

        // Acciones
        agendarCitaButton.addActionListener(e -> new AgendarCitaFrame(idPaciente));
        verCitasButton.addActionListener(e -> mostrarCitas());
        verHistorialButton.addActionListener(e -> mostrarHistorial());
        cerrarSesionButton.addActionListener(e -> cerrarSesion());
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
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(font);
        button.setPreferredSize(new Dimension(220, 40));
        return button;
    }

    private void cerrarSesion() {
        String[] opciones = {"Sí", "No"};
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);

        int opcion = JOptionPane.showOptionDialog(
                this,
                "¿Estás seguro que quieres cerrar sesión?",
                "Cerrar Sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (opcion == JOptionPane.YES_OPTION) {
            new LoginFrame();
            dispose();
        }
    }

    private int obtenerIdPacienteDesdeNombre(String nombre) {
        for (String linea : GestorArchivos.leerArchivo("pacientes.txt")) {
            String[] partes = linea.split(",");
            if (partes.length >= 5) {
                if (partes[1].equalsIgnoreCase(nombre)) {
                    return Integer.parseInt(partes[0]);
                }
            }
        }
        return -1;
    }

    private String obtenerNombrePsicologo(int idPsicologo) {
        for (String linea : GestorArchivos.leerArchivo("psicologos.txt")) {
            String[] partes = linea.split(",");
            if (partes.length >= 5) {
                if (Integer.parseInt(partes[0]) == idPsicologo) {
                    return partes[1];
                }
            }
        }
        return "Desconocido";
    }

    private void mostrarCitas() {
        textArea.setText("=== MIS CITAS ===\n\n");

        if (idPaciente == -1) {
            textArea.append("No se encontró el paciente.\n");
            return;
        }

        boolean hayCitas = false;
        for (String linea : GestorArchivos.leerArchivo("citas.txt")) {
            String[] partes = linea.split(",");
            if (partes.length >= 6) {
                int idPacienteCita = Integer.parseInt(partes[3]);
                String fecha = partes[1];
                String hora = partes[2];
                int idPsicologo = Integer.parseInt(partes[4]);
                String estado = partes[5];

                if (idPacienteCita == idPaciente && estado.equals("agendada")) {
                    String nombrePsicologo = obtenerNombrePsicologo(idPsicologo);
                    textArea.append("Fecha: " + fecha + "\n");
                    textArea.append("Hora: " + hora + "\n");
                    textArea.append("Psicólogo: " + nombrePsicologo + "\n\n");
                    hayCitas = true;
                }
            }
        }

        if (!hayCitas) {
            textArea.append("No tienes citas agendadas.\n");
        }
    }

    private void mostrarHistorial() {
        textArea.setText("=== MI HISTORIAL MÉDICO ===\n\n");

        if (idPaciente == -1) {
            textArea.append("No se encontró el paciente.\n");
            return;
        }

        boolean historialEncontrado = false;
        for (String linea : GestorArchivos.leerArchivo("historiales.txt")) {
            String[] partes = linea.split(",");
            if (partes.length >= 4) {
                int idHistorial = Integer.parseInt(partes[0]);
                if (idHistorial == idPaciente) {
                    String[] notas = partes[1].split(";");
                    String[] diagnosticos = partes[2].split(";");
                    String[] citasRelacionadas = partes[3].split(";");

                    int total = Math.max(notas.length, Math.max(diagnosticos.length, citasRelacionadas.length));

                    for (int i = 0; i < total; i++) {
                        textArea.append("------------------------------------------------\n");

                        if (i < citasRelacionadas.length && !citasRelacionadas[i].trim().isEmpty()) {
                            int idCita = Integer.parseInt(citasRelacionadas[i].trim());
                            String infoCita = obtenerInfoCita(idCita);
                            textArea.append("Cita: " + infoCita + "\n");
                        } else {
                            textArea.append("Cita: Sin información.\n");
                        }

                        if (i < notas.length && !notas[i].trim().isEmpty()) {
                            textArea.append("Nota: " + notas[i].trim() + "\n");
                        } else {
                            textArea.append("Nota: No registrada.\n");
                        }

                        if (i < diagnosticos.length && !diagnosticos[i].trim().isEmpty()) {
                            textArea.append("Diagnóstico: " + diagnosticos[i].trim() + "\n");
                        } else {
                            textArea.append("Diagnóstico: No registrado.\n");
                        }

                        textArea.append("------------------------------------------------\n\n");
                    }

                    historialEncontrado = true;
                    break;
                }
            }
        }

        if (!historialEncontrado) {
            textArea.append("No tienes historial registrado aún.\n");
        }
    }

    private String obtenerInfoCita(int idCita) {
        for (String linea : GestorArchivos.leerArchivo("citas.txt")) {
            String[] partes = linea.split(",");
            if (partes.length >= 6) {
                if (Integer.parseInt(partes[0]) == idCita) {
                    String fecha = partes[1];
                    String hora = partes[2];
                    int idPsicologo = Integer.parseInt(partes[4]);
                    String nombrePsicologo = obtenerNombrePsicologo(idPsicologo);
                    return fecha + " a las " + hora + " con " + nombrePsicologo;
                }
            }
        }
        return "Información no encontrada.";
    }
}
