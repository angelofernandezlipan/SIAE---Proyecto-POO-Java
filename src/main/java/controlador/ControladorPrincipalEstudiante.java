package controlador;

import modelo.Estudiante;
import modelo.SistemaInscripcion;
import vista.VentanaInscripcion;
import vista.VentanaLogin;
import vista.VentanaPrincipalEstudiante;


public class ControladorPrincipalEstudiante {

    // --- Atributos ---
    private final SistemaInscripcion sistema; // El Modelo
    private final Estudiante estudiante;     // El usuario logueado
    private VentanaPrincipalEstudiante vista; // La Vista que controla


    public ControladorPrincipalEstudiante(SistemaInscripcion sistema, Estudiante estudiante) {
        this.sistema = sistema;
        this.estudiante = estudiante;
    }


    public void setVista(VentanaPrincipalEstudiante vista) {
        this.vista = vista;
    }

    // --- MANEJADORES DE EVENTOS ---
    // (Métodos que serán llamados por la Vista)

    public void manejarAperturaInscripcion() {
        ControladorInscripcion contInscripcion = new ControladorInscripcion(this.sistema, this.estudiante);
        new VentanaInscripcion(contInscripcion);
    }


    public void manejarLogout() {

        if (this.vista != null) {
            this.vista.cerrar();
        }


        ControladorLogin contLogin = new ControladorLogin(this.sistema);

        new VentanaLogin(contLogin);
    }
}