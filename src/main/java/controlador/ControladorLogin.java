package controlador;

import modelo.Estudiante;
import vista.VentanaLogin;
import vista.VentanaPrincipalEstudiante;
import vista.VentanaPrincipal; // <--- ¡IMPORT FALTANTE AÑADIDO!

public class ControladorLogin {

    private final ControladorEstudiante contEstudiante;
    private final ControladorAdmin contAdmin;
    private VentanaLogin vista;

    public ControladorLogin(ControladorEstudiante contEstudiante, ControladorAdmin contAdmin) {
        this.contEstudiante = contEstudiante;
        this.contAdmin = contAdmin;
    }

    public void setVista(VentanaLogin vista) {
        this.vista = vista;
    }

    public void intentarLogin(String rol, String usuario, String password) {
        vista.mostrarError("");

        if (rol.equals("Estudiante")) {
            Estudiante estudiante = contEstudiante.login(usuario, password);

            if (estudiante != null) {
                vista.cerrar();

                // 1. Crear el controlador de la ventana principal de estudiante.
                // Se usa contEstudiante.getSistema() en lugar de contEstudiante.sistema
                ControladorPrincipalEstudiante contPrincipalEst = new ControladorPrincipalEstudiante(
                        contEstudiante.getSistema(),
                        estudiante,
                        this.contEstudiante,
                        this.contAdmin
                );

                // 2. Abrir la ventana de estudiante (con su controlador)
                new VentanaPrincipalEstudiante(contPrincipalEst, estudiante.getNombre());

            } else {
                vista.mostrarError("Error: RUT o contraseña de estudiante incorrectos.");
            }

        } else if (rol.equals("Administrador")) {
            boolean loginExitoso = contAdmin.login(usuario, password);

            if (loginExitoso) {
                vista.cerrar();

                // Inyección de los tres argumentos para el constructor de Administrador
                new VentanaPrincipal(contAdmin, this.contEstudiante, this.contAdmin);
            } else {
                vista.mostrarError("Error: Usuario o contraseña de administrador incorrectos.");
            }
        }
    }
}