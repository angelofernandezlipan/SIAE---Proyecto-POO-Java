package controlador;

import modelo.GestionAdministrativa;
import modelo.SistemaInscripcion;
import vista.VentanaPrincipal;
import vista.VentanaLogin;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ControladorAdmin {
    private final GestionAdministrativa gestionAdmin;
    private final SistemaInscripcion sistema;
    private static final String REPORTE_CREDENCIALES_PATH = "reporte_credenciales_admin.txt";

    // Dependencias para el Logout (referencias globales)
    private ControladorEstudiante contEstudianteGlobal;
    private ControladorAdmin contAdminGlobal;
    private VentanaPrincipal vistaPrincipal;

    public ControladorAdmin(SistemaInscripcion sistema,
                            ControladorEstudiante contEstudianteGlobal,
                            ControladorAdmin contAdminGlobal) {
        this.sistema = sistema;
        this.gestionAdmin = new GestionAdministrativa(sistema);
        this.contEstudianteGlobal = contEstudianteGlobal;
        this.contAdminGlobal = contAdminGlobal;
    }

    // ... (Otros métodos de setup y login se mantienen) ...

    public void setControladoresGlobales(ControladorEstudiante contEstudianteGlobal, ControladorAdmin contAdminGlobal) {
        this.contEstudianteGlobal = contEstudianteGlobal;
        this.contAdminGlobal = contAdminGlobal;
    }

    public void setVistaPrincipal(VentanaPrincipal vistaPrincipal) {
        this.vistaPrincipal = vistaPrincipal;
    }

    // --- Lógica de Negocio Administrativa ---

    /**
     * Delega la exportación de reportes al modelo administrativo y retorna el contenido.
     */
    public List<String> exportarReportes(int tipoReporte) { // <--- CORREGIDO: Retorna List<String>
        // La Gestión Administrativa ya devuelve List<String> y se encarga de guardar el TXT
        return gestionAdmin.exportarReportes(tipoReporte);
    }

    // ... (El resto de métodos se mantienen) ...
    public List<String> cargarEstudiantes(String filePath) {
        return gestionAdmin.cargarListaCursos(filePath);
    }

    public void limpiarDatosSistema() {
        sistema.limpiarDatosSistema();
    }

    public String abrirReporteCredenciales() {
        File file = new File(REPORTE_CREDENCIALES_PATH);
        // ... (Lógica de apertura de archivo) ...
        if (!file.exists()) {
            return "Error: El archivo de credenciales no existe. Ejecute la Carga Masiva primero.";
        }
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            try {
                Desktop.getDesktop().open(file);
                return "✅ Abriendo el archivo: " + REPORTE_CREDENCIALES_PATH;
            } catch (IOException e) {
                return "Error al intentar abrir el archivo: " + e.getMessage();
            }
        } else {
            return "Error: La función para abrir archivos automáticamente no es soportada en este sistema operativo.";
        }
    }

    public boolean login(String user, String pass) {
        return user.equals("admin") && pass.equals("admin");
    }

    public void manejarLogout() {
        if (this.vistaPrincipal != null) {
            this.vistaPrincipal.dispose();
        }
        ControladorLogin contLogin = new ControladorLogin(this.contEstudianteGlobal, this.contAdminGlobal);
        new VentanaLogin(contLogin);
    }
}