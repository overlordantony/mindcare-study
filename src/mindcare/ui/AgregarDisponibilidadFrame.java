package mindcare.ui;

import javax.swing.*;
import mindcare.GestorArchivos;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AgregarDisponibilidadFrame extends JFrame {

    private int idPsicologo;
    private JSpinner fechaSpinner;
    private JTextField horaField;

    public AgregarDisponibilidadFrame(int idPsicologo) {
        this.idPsicologo = idPsicologo;

        setTitle("MindCare - Agregar Disponibilidad");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        Color buttonColor = Color.decode("#1E90FF");
        Color cancelColor = Color.decode("#E51A4C");
        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 16);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel tituloLabel = new JLabel("Agregar Nueva Disponibilidad", SwingConstants.CENTER);
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(tituloLabel);
        mainPanel.add(Box.createVerticalStrut(20));

        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1;

        JLabel fechaLabel = new JLabel("Fecha:");
        fechaSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(fechaSpinner, "dd-MM-yyyy");
        fechaSpinner.setEditor(dateEditor);

        JLabel horaLabel = new JLabel("Hora (hh:mm):");
        horaField = new JTextField();

        gbc.gridy = 0;
        formPanel.add(fechaLabel, gbc);
        gbc.gridy++;
        formPanel.add(fechaSpinner, gbc);
        gbc.gridy++;
        formPanel.add(horaLabel, gbc);
        gbc.gridy++;
        formPanel.add(horaField, gbc);

        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(30));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonsPanel.setBackground(Color.WHITE);

        JButton guardarButton = createStyledButton("Guardar", buttonColor, buttonFont);
        JButton cancelarButton = createStyledButton("Cancelar", cancelColor, buttonFont);

        guardarButton.addActionListener(e -> guardarDisponibilidad());
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
        button.setPreferredSize(new Dimension(150, 35));
        return button;
    }

    private void guardarDisponibilidad() {
        Date fechaSeleccionada = (Date) fechaSpinner.getValue();
        String fechaFormateada = new SimpleDateFormat("dd-MM-yyyy").format(fechaSeleccionada);
        String hora = horaField.getText().trim();

        if (hora.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar la hora.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!hora.matches("^\\d{2}:\\d{2}$")) {
            JOptionPane.showMessageDialog(this, "Formato de hora inválido. Use hh:mm.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date hoy = new Date();
        SimpleDateFormat soloFecha = new SimpleDateFormat("yyyyMMdd");
        int fechaHoy = Integer.parseInt(soloFecha.format(hoy));
        int fechaElegida = Integer.parseInt(soloFecha.format(fechaSeleccionada));

        if (fechaElegida < fechaHoy) {
            JOptionPane.showMessageDialog(this, "No puede agregar disponibilidad en fechas pasadas.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String disponibilidad = idPsicologo + "," + fechaFormateada + "H" + hora;
        GestorArchivos.guardarEnArchivo("disponibilidades.txt", disponibilidad);

        JOptionPane.showMessageDialog(this, "Disponibilidad agregada correctamente.");
        dispose();
    }
}
