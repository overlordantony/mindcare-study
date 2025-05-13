package mindcare;

public class Seeds {
    public static void main(String[] args) {
        GestorArchivos.limpiarArchivo("psicologos.txt");
        GestorArchivos.limpiarArchivo("pacientes.txt");
        GestorArchivos.limpiarArchivo("citas.txt");
        GestorArchivos.limpiarArchivo("historiales.txt");
        GestorArchivos.limpiarArchivo("disponibilidades.txt");

        // Crear psicólogos
        Psicologo p1 = new Psicologo(1, "Ana Torres", "ana@correo.com", "1234", "Clínica");
        Psicologo p2 = new Psicologo(2, "Juan Pérez", "juan@correo.com", "abcd", "Infantil");
        Psicologo p3 = new Psicologo(3, "Laura Gómez", "laura@correo.com", "5678", "Adolescente");

        // Guardar psicólogos
        GestorArchivos.guardarEnArchivo("psicologos.txt", p1.toString());
        GestorArchivos.guardarEnArchivo("psicologos.txt", p2.toString());
        GestorArchivos.guardarEnArchivo("psicologos.txt", p3.toString());

        // Crear pacientes
        Paciente pac1 = new Paciente(100, "Luis Pérez", "luis@correo.com", "abcd", "1995-06-21");
        Paciente pac2 = new Paciente(101, "Maria Gómez", "maria@correo.com", "efgh", "1992-09-10");

        // Guardar pacientes
        GestorArchivos.guardarEnArchivo("pacientes.txt", pac1.toString());
        GestorArchivos.guardarEnArchivo("pacientes.txt", pac2.toString());

        // Crear disponibilidades (idPsicologo, fechaHoraDisponible)
        GestorArchivos.guardarEnArchivo("disponibilidades.txt", "1,20-05-2025H09:00");
        GestorArchivos.guardarEnArchivo("disponibilidades.txt", "1,21-05-2025H10:00");
        GestorArchivos.guardarEnArchivo("disponibilidades.txt", "2,22-05-2025H14:00");
        GestorArchivos.guardarEnArchivo("disponibilidades.txt", "3,23-05-2025H16:00");
        GestorArchivos.guardarEnArchivo("disponibilidades.txt", "3,24-05-2025H11:00");

        System.out.println("✅ Seeds ejecutado. Psicólogos, pacientes y disponibilidades cargados.");
    }
}
