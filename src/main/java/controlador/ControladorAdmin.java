package controlador;

import modelo.GestionAdministrativa;
import modelo.SistemaInscripcion;

import java.util.List;

public class ControladorAdmin {
    private final GestionAdministrativa gestionAdmin;
    private final SistemaInscripcion sistema; // Para limpieza de datos

    public ControladorAdmin(SistemaInscripcion sistema) {
        this.sistema = sistema;
        this.gestionAdmin = new GestionAdministrativa(sistema);
    }

    // Lógica simple de login, mantenida aquí para encapsulamiento
    public boolean login(String user, String pass) {
        // Credenciales simples para la demo
        return user.equals("admin") && pass.equals("admin");
    }

    public List<String> cargarEstudiantes(String filePath) {
        return gestionAdmin.cargarListaCursos(filePath);
    }

    public void exportarReportes(int tipoReporte) {
        gestionAdmin.exportarReportes(tipoReporte);
    }

    /** Nueva función para limpiar todos los datos del sistema. */
    public void limpiarDatosSistema() {
        sistema.limpiarDatosSistema();
    }
}