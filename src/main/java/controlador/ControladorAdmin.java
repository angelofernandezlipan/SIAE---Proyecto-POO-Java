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

    // CONSTRUCTOR PRINCIPAL: Recibe las dependencias del Modelo y los Controladores Globales
    public ControladorAdmin(SistemaInscripcion sistema,
                            ControladorEstudiante contEstudianteGlobal,
                            ControladorAdmin contAdminGlobal) {
        this.sistema = sistema;
        this.gestionAdmin = new GestionAdministrativa(sistema);
        this.contEstudianteGlobal = contEstudianteGlobal;
        this.contAdminGlobal = contAdminGlobal;
    }

    /**
     * Permite inyectar las referencias globales después de la inicialización
     * (útil si el constructor no puede manejarlas todas).
     */
    public void setControladoresGlobales(ControladorEstudiante contEstudianteGlobal, ControladorAdmin contAdminGlobal) {
        this.contEstudianteGlobal = contEstudianteGlobal;
        this.contAdminGlobal = contAdminGlobal;
    }

    /**
     * Inyecta la referencia a la ventana principal para poder cerrarla al hacer logout.
     */
    public void setVistaPrincipal(VentanaPrincipal vistaPrincipal) {
        this.vistaPrincipal = vistaPrincipal;
    }

    // --- Lógica de Sesión ---

    /**
     * Valida las credenciales de administrador.
     */
    public boolean login(String user, String pass) {
        // Credenciales simples para la demostración
        return user.equals("admin") && pass.equals("admin");
    }

    /**
     * Cierra la ventana actual y regresa al Login.
     */
    public void manejarLogout() {
        if (this.vistaPrincipal != null) {
            this.vistaPrincipal.dispose(); // Cierra la ventana principal
        }

        // Crea un nuevo ControladorLogin inyectando las dependencias globales (contEstudianteGlobal, contAdminGlobal)
        ControladorLogin contLogin = new ControladorLogin(this.contEstudianteGlobal, this.contAdminGlobal);
        new VentanaLogin(contLogin); // Abre la ventana de login
    }

    // --- Lógica de Negocio Administrativa ---

    /**
     * Delega la carga masiva de estudiantes al modelo administrativo.
     */
    public List<String> cargarEstudiantes(String filePath) {
        return gestionAdmin.cargarListaCursos(filePath);
    }

    /**
     * Delega la exportación de reportes al modelo administrativo.
     */
    public void exportarReportes(int tipoReporte) {
        gestionAdmin.exportarReportes(tipoReporte);
    }

    /**
     * Delega la función de limpiar todos los datos del sistema.
     */
    public void limpiarDatosSistema() {
        sistema.limpiarDatosSistema();
    }

    /**
     * Intenta abrir el archivo de reporte de credenciales con el programa predeterminado del SO.
     * @return Mensaje de éxito o error.
     */
    public String abrirReporteCredenciales() {
        File file = new File(REPORTE_CREDENCIALES_PATH);

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
}