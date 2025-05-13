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
        setSize(400, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        getContentPane().setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        ImageIcon logoIcon = new ImageIcon("src/mindcare/ui/assets/logo.png");
        Image image = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(image);
        JLabel logoLabel = new JLabel(resizedIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(logoLabel);
        mainPanel.add(Box.createVerticalStrut(15));

        // Colores
        Color labelColor = Color.decode("#4B4B4B");
        Color buttonColor = Color.decode("#1E90FF");

        // Correo
        JLabel correoLabel = new JLabel("Correo:");
        correoLabel.setForeground(labelColor);
        correoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        correoField = new JTextField(20);
        correoField.setMaximumSize(new Dimension(300, 30));
        correoField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
        correoField.addActionListener(e -> autenticarUsuario());

        mainPanel.add(correoLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(correoField);
        mainPanel.add(Box.createVerticalStrut(20)); 

        JLabel claveLabel = new JLabel("Contraseña:");
        claveLabel.setForeground(labelColor);
        claveLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        claveField = new JPasswordField(20);
        claveField.setMaximumSize(new Dimension(300, 30));
        claveField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
        claveField.addActionListener(e -> autenticarUsuario());

        mainPanel.add(claveLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(claveField);
        mainPanel.add(Box.createVerticalStrut(20));

        loginButton = new JButton("Ingresar") {
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
            public void setContentAreaFilled(boolean b) {
            }
        };

        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setBackground(buttonColor);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setPreferredSize(new Dimension(120, 40));

        mainPanel.add(loginButton);
        mainPanel.add(Box.createVerticalStrut(10));

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autenticarUsuario();
            }
        });

        add(mainPanel);
        setVisible(true);
    }

   private void autenticarUsuario() {
    String correo = correoField.getText();
    String clave = new String(claveField.getPassword());
    boolean autenticado = false;

    for (String linea : GestorArchivos.leerArchivo("pacientes.txt")) {
        String[] partes = linea.split(",");
        if (partes.length >= 5) {
            String correoArchivo = partes[2];
            String claveArchivo = partes[3];
            if (correoArchivo.equals(correo) && claveArchivo.equals(clave)) {
                autenticado = true;
                UIManager.put("OptionPane.background", Color.WHITE);
                UIManager.put("Panel.background", Color.WHITE);
                JOptionPane.showMessageDialog(this, "Bienvenido paciente " + partes[1]);
                new PacienteMenuFrame(partes[1]);
                dispose();
                return;
            }
        }
    }

    for (String linea : GestorArchivos.leerArchivo("psicologos.txt")) {
        String[] partes = linea.split(",");
        if (partes.length >= 5) {
            String correoArchivo = partes[2];
            String claveArchivo = partes[3];
            if (correoArchivo.equals(correo) && claveArchivo.equals(clave)) {
                autenticado = true;
                UIManager.put("OptionPane.background", Color.WHITE);
                UIManager.put("Panel.background", Color.WHITE);
                JOptionPane.showMessageDialog(this, "Bienvenido psicólogo " + partes[1]);
                new PsicologoMenuFrame(partes[1]);
                dispose();
                return;
            }
        }
    }

    if (!autenticado) {
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);
        JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
    }
}

}
