package mindcare.ui;

import javax.swing.*;
import mindcare.GestorArchivos;
import java.awt.*;

public class PsicologoMenuFrame extends JFrame {

    private String nombrePsicologo;
    private int idPsicologo;
    private JTextArea textArea;

    public PsicologoMenuFrame(String nombrePsicologo) {
        this.nombrePsicologo = nombrePsicologo;
        this.idPsicologo = obtenerIdPsicologoDesdeNombre(nombrePsicologo);

        setIconImage(Toolkit.getDefaultToolkit().getImage("src/mindcare/ui/assets/logo.png"));

        setTitle("MindCare - Menú Psicólogo");
        setSize(1100, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        Color labelColor = Color.decode("#4B4B4B");
        Color buttonColor = Color.decode("#1E90FF");
        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 16);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Espacio superior e inferior reducido

        // Logo
        ImageIcon logoIcon = new ImageIcon("src/mindcare/ui/assets/logo.png");
        Image image = logoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(image));
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoPanel.setBackground(Color.WHITE);
        logoPanel.add(logoLabel);

        // Bienvenida
        JLabel bienvenidaLabel = new JLabel("Bienvenido, Dr./Dra. " + this.nombrePsicologo);
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

        JButton verAgendaButton = createStyledButton("Ver Agenda", buttonColor, buttonFont);
        JButton atenderCitasButton = createStyledButton("Atender Citas", buttonColor, buttonFont);
        JButton agregarDisponibilidadButton = createStyledButton("Agregar Disponibilidad", buttonColor, buttonFont);
        JButton cerrarSesionButton = createStyledButton("Cerrar Sesión", buttonColor, buttonFont);

        buttonsWrapper.add(verAgendaButton);
        buttonsWrapper.add(atenderCitasButton);
        buttonsWrapper.add(agregarDisponibilidadButton);
        buttonsWrapper.add(cerrarSesionButton);

        // Área de texto
        textArea = new JTextArea("Aquí se mostrará la información de agenda o citas...");
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
        verAgendaButton.addActionListener(e -> mostrarAgenda());
        atenderCitasButton.addActionListener(e -> new AtenderCitaFrame(idPsicologo));
        agregarDisponibilidadButton.addActionListener(e -> new AgregarDisponibilidadFrame(idPsicologo));
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

    private int obtenerIdPsicologoDesdeNombre(String nombre) {
        for (String linea : GestorArchivos.leerArchivo("psicologos.txt")) {
            String[] partes = linea.split(",");
            if (partes.length >= 2 && partes[1].equalsIgnoreCase(nombre)) {
                return Integer.parseInt(partes[0]);
            }
        }
        return -1;
    }

    private String obtenerNombrePaciente(int idPaciente) {
        for (String linea : GestorArchivos.leerArchivo("pacientes.txt")) {
            String[] partes = linea.split(",");
            if (partes.length >= 2 && Integer.parseInt(partes[0]) == idPaciente) {
                return partes[1];
            }
        }
        return "Desconocido";
    }

    private void mostrarAgenda() {
        textArea.setText("=== MI AGENDA ===\n\n");

        if (idPsicologo == -1) {
            textArea.append("No se encontró el psicólogo.\n");
            return;
        }

        boolean hayCitas = false;
        for (String linea : GestorArchivos.leerArchivo("citas.txt")) {
            String[] partes = linea.split(",");
            if (partes.length >= 6) {
                int idPsicologoCita = Integer.parseInt(partes[4]);
                String fecha = partes[1];
                String hora = partes[2];
                int idPaciente = Integer.parseInt(partes[3]);
                String estado = partes[5];

                if (idPsicologoCita == idPsicologo && estado.equals("agendada")) {
                    String nombrePaciente = obtenerNombrePaciente(idPaciente);
                    textArea.append("Fecha: " + fecha + "\n");
                    textArea.append("Hora: " + hora + "\n");
                    textArea.append("Paciente: " + nombrePaciente + "\n\n");
                    hayCitas = true;
                }
            }
        }

        if (!hayCitas) {
            textArea.append("No tienes citas agendadas.\n");
        }
    }
}
