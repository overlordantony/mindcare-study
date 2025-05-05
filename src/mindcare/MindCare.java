package mindcare;

import mindcare.ui.LoginFrame;
import java.io.File;

public class MindCare {

    public static void main(String[] args) {
        verificarYCrearSeeds();
        new LoginFrame();
    }

    private static void verificarYCrearSeeds() {
        boolean cargarSeeds = false;

        // Verificar psicologos.txt
        File archivoPsicologos = new File("psicologos.txt");
        if (!archivoPsicologos.exists() || archivoPsicologos.length() == 0) {
            cargarSeeds = true;
        }

        // Verificar pacientes.txt
        File archivoPacientes = new File("pacientes.txt");
        if (!archivoPacientes.exists() || archivoPacientes.length() == 0) {
            cargarSeeds = true;
        }

        if (cargarSeeds) {
            System.out.println("Cargando datos iniciales...");
            Seeds.main(null);
        } else {
            System.out.println("Datos ya existen, no se cargan Seeds.");
        }
    }
}
