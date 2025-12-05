package controlador;

import modelo.Estudiante;
import modelo.SistemaInscripcion;
import vista.VentanaInscripcion;
import vista.VentanaLogin;
import vista.VentanaPrincipalEstudiante;
import javax.swing.JFrame;
import java.util.List;

/**
 * Controlador para la navegación y acciones del menú principal del estudiante.
 * Gestiona la lógica de cierre de sesión con validación de carga académica.
 */
public class ControladorPrincipalEstudiante {

    private final SistemaInscripcion sistema;
    private final Estudiante estudiante;
    private VentanaPrincipalEstudiante vista;

    // Referencias globales para permitir el regreso al Login
    private final ControladorEstudiante contEstudianteGlobal;
    private final ControladorAdmin contAdminGlobal;

    public ControladorPrincipalEstudiante(SistemaInscripcion sistema, Estudiante estudiante,
                                          ControladorEstudiante contEstudianteGlobal, ControladorAdmin contAdminGlobal) {
        this.sistema = sistema;
        this.estudiante = estudiante;
        this.contEstudianteGlobal = contEstudianteGlobal;
        this.contAdminGlobal = contAdminGlobal;
    }

    public void setVista(VentanaPrincipalEstudiante vista) {
        this.vista = vista;
    }

    // --- MANEJADORES DE EVENTOS ---

    public String getNombreEstudiante() {
        return estudiante.getNombre();
    }

    public void manejarAperturaInscripcion() {
        ControladorInscripcion contInscripcion = new ControladorInscripcion(this.sistema, this.estudiante);
        new VentanaInscripcion(contInscripcion);
    }

    /**
     * Gestiona el proceso de cierre de sesión.
     * Valida si el estudiante cumple con el requisito de 3 asignaturas inscritas.
     */
    public void manejarLogout() {
        // 1. Verificar cantidad de inscripciones
        List<String> inscritas = estudiante.getAsignaturasInscritas();
        int cantidad = inscritas.size();

        // 2. Si tiene menos de 3, mostrar advertencia
        if (cantidad < 3) {
            String mensaje = "⚠️ ALERTA ACADÉMICA:\n" +
                    "Actualmente tienes " + cantidad + " asignatura(s) inscrita(s).\n" +
                    "El requisito académico es inscribir al menos 3 asignaturas.";

            // Preguntar a la vista si el usuario confirma la salida a pesar de la advertencia
            boolean deseaSalir = vista.mostrarAdvertenciaLogout(mensaje);

            if (!deseaSalir) {
                return; // Si el usuario dice "No", cancelamos el logout y se queda en la ventana.
            }
        }

        // 3. Proceso normal de Logout (Si tiene 3+ materias o si decidió salir igual)
        if (this.vista != null) {
            this.vista.cerrar();
        }

        // Volver a instanciar el Login
        ControladorLogin contLogin = new ControladorLogin(this.contEstudianteGlobal, this.contAdminGlobal);
        new VentanaLogin(contLogin);
    }
}