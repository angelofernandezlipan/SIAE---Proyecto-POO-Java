package controlador;

import modelo.Estudiante;
import modelo.SistemaInscripcion;
import vista.VentanaLogin;
import vista.VentanaPrincipalEstudiante;


public class ControladorLogin {

    // --- Atributos ---
    private final SistemaInscripcion sistema;
    private VentanaLogin vistaLogin;


    public ControladorLogin(SistemaInscripcion sistema) {
        this.sistema = sistema;
    }


    public void setVista(VentanaLogin vistaLogin) {
        this.vistaLogin = vistaLogin;
    }

    public void intentarLogin(String rut, String password) {
        Estudiante estudiante = sistema.validarCredenciales(rut, password);

        if (estudiante != null) {

            if (this.vistaLogin != null) {
                this.vistaLogin.cerrar();
            }

            ControladorPrincipalEstudiante contPrincipal = new ControladorPrincipalEstudiante(this.sistema, estudiante);

            new VentanaPrincipalEstudiante(contPrincipal, estudiante.getNombre());

        } else {

            if (this.vistaLogin != null) {
                this.vistaLogin.mostrarError("Usuario (RUT) o contraseña incorrectos");
            }
        }
    }
}
