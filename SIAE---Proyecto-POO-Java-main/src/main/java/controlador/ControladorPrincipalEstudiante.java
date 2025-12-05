package controlador;

import modelo.Estudiante;
import modelo.SistemaInscripcion;
import vista.VentanaInscripcion;
import vista.VentanaLogin;
import vista.VentanaPrincipalEstudiante;
import javax.swing.JFrame;

public class ControladorPrincipalEstudiante {
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
    public void manejarAperturaInscripcion() {
        ControladorInscripcion contInscripcion = new ControladorInscripcion(this.sistema, this.estudiante);
        new VentanaInscripcion(contInscripcion);
    }


    public void manejarLogout() {

        if (this.vista != null) {
            this.vista.cerrar();
        }
        ControladorLogin contLogin = new ControladorLogin(this.contEstudianteGlobal, this.contAdminGlobal);
        new VentanaLogin(contLogin);
    }
}