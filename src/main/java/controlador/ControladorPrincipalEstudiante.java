package controlador;

import modelo.Estudiante;
import modelo.SistemaInscripcion;
import vista.VentanaInscripcion;
import vista.VentanaLogin;
import vista.VentanaPrincipalEstudiante;
import javax.swing.JFrame;

public class ControladorPrincipalEstudiante {

    // --- Atributos ---
    private final SistemaInscripcion sistema;
    private final Estudiante estudiante;
    private VentanaPrincipalEstudiante vista;

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

    public void manejarAperturaInscripcion() {
        ControladorInscripcion contInscripcion = new ControladorInscripcion(this.sistema, this.estudiante);
        new VentanaInscripcion(contInscripcion);
    }


    public void manejarLogout() {

        if (this.vista != null) {
            this.vista.cerrar(); // Cierra el JFrame del estudiante
        }

        // CLAVE CORREGIDA: El método global acepta JFrame.
        // Como VentanaPrincipalEstudiante es un JFrame, el casting ahora es a JFrame y es legal.
        // Si VentanaPrincipalEstudiante USARA JFrame internamente, debemos obtener esa referencia.

        // Asumiendo que 'vista' tiene un método para obtener la referencia a la ventana,
        // o que la clase misma extiende JFrame. Dado que la clase NO extiende,
        // debemos obtener la referencia al JFrame interno (menuEstudiantes).

        // Si la vista no extiende JFrame:
        // this.contEstudianteGlobal.manejarLogout(this.vista.menuEstudiantes);

        // Pero usaremos la solución más simple que asume que 'cerrar()' ya hizo dispose().
        // Simplemente llamamos al manejador para que abra la ventana de login:

        ControladorLogin contLogin = new ControladorLogin(this.contEstudianteGlobal, this.contAdminGlobal);
        new VentanaLogin(contLogin);
    }
}