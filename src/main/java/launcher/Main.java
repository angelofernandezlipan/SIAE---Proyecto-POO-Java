package launcher;

import controlador.ControladorAdmin;
import controlador.ControladorEstudiante;
import modelo.RepositorioDatos;
import modelo.Sesion;
import modelo.SistemaInscripcion;
import vista.VentanaLogin;
import controlador.ControladorLogin;

public class Main {
    public static void main(String[] args) {
        RepositorioDatos repositorio = new RepositorioDatos();
        SistemaInscripcion sistema = new SistemaInscripcion(repositorio);
        Sesion sesion = new Sesion();

        ControladorEstudiante contEstudiante;
        ControladorAdmin contAdmin;

        contEstudiante = new ControladorEstudiante(sistema, sesion);

        contAdmin = new ControladorAdmin(sistema, contEstudiante, null);

        ControladorEstudiante contEstudianteGlobal = new ControladorEstudiante(sistema, sesion);
        ControladorAdmin contAdminGlobal = new ControladorAdmin(sistema, null, null); // Inicialización placeholder

        contAdminGlobal.setControladoresGlobales(contEstudianteGlobal, contAdminGlobal);
        contEstudianteGlobal.setControladoresGlobales(contEstudianteGlobal, contAdminGlobal);

        ControladorLogin contLogin = new ControladorLogin(contEstudianteGlobal, contAdminGlobal);

        new VentanaLogin(contLogin);
    }
}