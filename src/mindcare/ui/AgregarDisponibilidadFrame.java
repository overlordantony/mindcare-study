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

        setTitle("Agregar Disponibilidad");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Formulario
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        JLabel fechaLabel = new JLabel("Fecha:");
        fechaSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(fechaSpinner, "dd-MM-yyyy");
        fechaSpinner.setEditor(dateEditor);

        JLabel horaLabel = new JLabel("Hora (hh:mm):");
        horaField = new JTextField();

        formPanel.add(fechaLabel);
        formPanel.add(fechaSpinner);
        formPanel.add(horaLabel);
        formPanel.add(horaField);

        // Botones
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton guardarButton = new JButton("Guardar");
        JButton cancelarButton = new JButton("Cancelar");

        guardarButton.addActionListener(e -> guardarDisponibilidad());
        cancelarButton.addActionListener(e -> dispose());

        buttonsPanel.add(guardarButton);
        buttonsPanel.add(cancelarButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void guardarDisponibilidad() {
        Date fechaSeleccionada = (Date) fechaSpinner.getValue();
        String fechaFormateada = new SimpleDateFormat("dd-MM-yyyy").format(fechaSeleccionada);
        String hora = horaField.getText().trim();
    
        if (hora.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Debe ingresar la hora.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        if (!hora.matches("^\\d{2}:\\d{2}$")) {
            JOptionPane.showMessageDialog(this, "❌ Formato de hora inválido. Use hh:mm.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // 🔥 Validar que la fecha no sea anterior a hoy
        Date hoy = new Date();
        SimpleDateFormat soloFecha = new SimpleDateFormat("yyyyMMdd");
        int fechaHoy = Integer.parseInt(soloFecha.format(hoy));
        int fechaElegida = Integer.parseInt(soloFecha.format(fechaSeleccionada));
    
        if (fechaElegida < fechaHoy) {
            JOptionPane.showMessageDialog(this, "❌ No puede agregar disponibilidad en fechas pasadas.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        String disponibilidad = idPsicologo + "," + fechaFormateada + "H" + hora;
        GestorArchivos.guardarEnArchivo("disponibilidades.txt", disponibilidad);
    
        JOptionPane.showMessageDialog(this, "✅ Disponibilidad agregada correctamente.");
        dispose();
    }
    
}
