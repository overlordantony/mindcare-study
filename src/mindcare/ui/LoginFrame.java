package mindcare.ui;

import javax.swing.*;

import mindcare.GestorArchivos;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField correoField;
    private JPasswordField claveField;
    private JButton loginButton;

    public LoginFrame() {
        setTitle("MindCare - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar pantalla

        // Crear componentes
        JLabel correoLabel = new JLabel("Correo:");
        JLabel claveLabel = new JLabel("Clave:");
        correoField = new JTextField(20);
        claveField = new JPasswordField(20);
        loginButton = new JButton("Ingresar");

        // Layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(correoLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(correoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(claveLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(claveField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(loginButton, gbc);

        // Acción del botón
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autenticarUsuario();
            }
        });

        setVisible(true);
    }

    private void autenticarUsuario() {
        String correo = correoField.getText();
        String clave = new String(claveField.getPassword());
    
        boolean autenticado = false;
    
        // Buscar en pacientes
        for (String linea : GestorArchivos.leerArchivo("pacientes.txt")) {
            String[] partes = linea.split(",");
            if (partes.length >= 5) {
                String correoArchivo = partes[2];
                String claveArchivo = partes[3];
    
                if (correoArchivo.equals(correo) && claveArchivo.equals(clave)) {
                    autenticado = true;
                    JOptionPane.showMessageDialog(this, "✅ Bienvenido paciente " + partes[1]);
                    
                    // Abrir menú de paciente
                    new PacienteMenuFrame(partes[1]);
                    dispose(); // Cierra LoginFrame
                    return;
                }
            }
        }
    
        // Buscar en psicologos
        for (String linea : GestorArchivos.leerArchivo("psicologos.txt")) {
            String[] partes = linea.split(",");
            if (partes.length >= 5) {
                String correoArchivo = partes[2];
                String claveArchivo = partes[3];
    
                if (correoArchivo.equals(correo) && claveArchivo.equals(clave)) {
                    autenticado = true;
                    JOptionPane.showMessageDialog(this, "✅ Bienvenido psicólogo " + partes[1]);
                    
                    // Abrir menú de psicólogo
                    new PsicologoMenuFrame(partes[1]);
                    dispose(); // Cierra LoginFrame
                    return;
                }
            }
        }
    
        if (!autenticado) {
            JOptionPane.showMessageDialog(this, "❌ Usuario o clave incorrectos.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
        }
    }
}
