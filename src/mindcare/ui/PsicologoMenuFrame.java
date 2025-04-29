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

        setTitle("MindCare - Menú Psicólogo");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton verAgendaButton = new JButton("Ver Agenda");
        JButton atenderCitasButton = new JButton("Atender Citas");
        JButton agregarDisponibilidadButton = new JButton("Agregar Disponibilidad");
        JButton cerrarSesionButton = new JButton("Cerrar Sesión");

        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 16);
        verAgendaButton.setFont(buttonFont);
        atenderCitasButton.setFont(buttonFont);
        agregarDisponibilidadButton.setFont(buttonFont);
        cerrarSesionButton.setFont(buttonFont);

        buttonsPanel.add(verAgendaButton);
        buttonsPanel.add(atenderCitasButton);
        buttonsPanel.add(agregarDisponibilidadButton);
        buttonsPanel.add(cerrarSesionButton);

        textArea = new JTextArea("Aquí se mostrará la información de agenda o citas...");
        textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(650, 300));

        JLabel bienvenidaLabel = new JLabel("Bienvenido, Dr./Dra. " + this.nombrePsicologo);
        bienvenidaLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bienvenidaLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

        mainPanel.add(bienvenidaLabel, BorderLayout.NORTH);
        mainPanel.add(buttonsPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);

        // Acciones de botones
        verAgendaButton.addActionListener(e -> mostrarAgenda());
        atenderCitasButton.addActionListener(e -> {
            new AtenderCitaFrame(idPsicologo);
        });        
        agregarDisponibilidadButton.addActionListener(e -> {
            new AgregarDisponibilidadFrame(idPsicologo);
        });
        
        cerrarSesionButton.addActionListener(e -> {
            String[] opciones = {"Sí", "No"};
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
        });
    }

    private int obtenerIdPsicologoDesdeNombre(String nombre) {
        for (String linea : GestorArchivos.leerArchivo("psicologos.txt")) {
            String[] partes = linea.split(",");
            if (partes.length >= 2) {
                if (partes[1].equalsIgnoreCase(nombre)) {
                    return Integer.parseInt(partes[0]);
                }
            }
        }
        return -1; // No encontrado
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

    private void mostrarAgenda() {
        textArea.setText("=== MI AGENDA ===\n\n");

        if (idPsicologo == -1) {
            textArea.append("❌ No se encontró el psicólogo.\n");
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
