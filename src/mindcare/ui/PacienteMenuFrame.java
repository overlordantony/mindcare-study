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

        setTitle("MindCare - Menú Paciente");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton agendarCitaButton = new JButton("Agendar Cita");
        JButton verCitasButton = new JButton("Ver Citas Agendadas");
        JButton verHistorialButton = new JButton("Ver Historial Médico");
        JButton cerrarSesionButton = new JButton("Cerrar Sesión");

        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 16);
        agendarCitaButton.setFont(buttonFont);
        verCitasButton.setFont(buttonFont);
        verHistorialButton.setFont(buttonFont);
        cerrarSesionButton.setFont(buttonFont);

        buttonsPanel.add(agendarCitaButton);
        buttonsPanel.add(verCitasButton);
        buttonsPanel.add(verHistorialButton);
        buttonsPanel.add(cerrarSesionButton);

        textArea = new JTextArea("Aquí se mostrará la información de citas o historial...");
        textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(650, 300));

        JLabel bienvenidaLabel = new JLabel("Bienvenido, " + this.nombrePaciente);
        bienvenidaLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bienvenidaLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

        mainPanel.add(bienvenidaLabel, BorderLayout.NORTH);
        mainPanel.add(buttonsPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);

        agendarCitaButton.addActionListener(e -> {
            new AgendarCitaFrame(idPaciente);
        });

        verCitasButton.addActionListener(e -> {
            mostrarCitas();
        });

        verHistorialButton.addActionListener(e -> {
            mostrarHistorial();
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

    private int obtenerIdPacienteDesdeNombre(String nombre) {
        for (String linea : GestorArchivos.leerArchivo("pacientes.txt")) {
            String[] partes = linea.split(",");
            if (partes.length >= 5) {
                String nombreArchivo = partes[1];
                if (nombreArchivo.equalsIgnoreCase(nombre)) {
                    return Integer.parseInt(partes[0]); // idUsuario
                }
            }
        }
        return -1;
    }
    
    private String obtenerNombrePsicologo(int idPsicologo) {
        for (String linea : GestorArchivos.leerArchivo("psicologos.txt")) {
            String[] partes = linea.split(",");
            if (partes.length >= 5) {
                int idArchivo = Integer.parseInt(partes[0]);
                if (idArchivo == idPsicologo) {
                    return partes[1];
                }
            }
        }
        return "Desconocido";
    }

    private void mostrarCitas() {
        textArea.setText("=== MIS CITAS ===\n\n");

        if (idPaciente == -1) {
            textArea.append("❌ No se encontró el paciente.\n");
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

    private String obtenerInfoCita(int idCita) {
        for (String linea : GestorArchivos.leerArchivo("citas.txt")) {
            String[] partes = linea.split(",");
            if (partes.length >= 6) {
                int idArchivo = Integer.parseInt(partes[0]);
                if (idArchivo == idCita) {
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
    

    private void mostrarHistorial() {
        textArea.setText("=== MI HISTORIAL MÉDICO ===\n\n");
    
        if (idPaciente == -1) {
            textArea.append("❌ No se encontró el paciente.\n");
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
    
                        // Mostrar cita relacionada
                        if (i < citasRelacionadas.length && !citasRelacionadas[i].trim().isEmpty()) {
                            int idCita = Integer.parseInt(citasRelacionadas[i].trim());
                            String infoCita = obtenerInfoCita(idCita);
                            textArea.append("Cita: " + infoCita + "\n");
                        } else {
                            textArea.append("Cita: Sin información.\n");
                        }
    
                        // Mostrar nota
                        if (i < notas.length && !notas[i].trim().isEmpty()) {
                            textArea.append("Nota: " + notas[i].trim() + "\n");
                        } else {
                            textArea.append("Nota: No registrada.\n");
                        }
    
                        // Mostrar diagnóstico
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
    

}
