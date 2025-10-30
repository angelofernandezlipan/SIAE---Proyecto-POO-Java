package launcher;

import controlador.ControladorAdmin;
import controlador.ControladorEstudiante;
import modelo.RepositorioDatos;
import modelo.Sesion;
import modelo.SistemaInscripcion;
import vista.MenuConsola;

public class Main {
    public static void main(String[] args) {
        RepositorioDatos repositorio = new RepositorioDatos();
        SistemaInscripcion sistema = new SistemaInscripcion(repositorio);
        Sesion sesion = new Sesion();

        ControladorEstudiante contEstudiante = new ControladorEstudiante(sistema, sesion);
        ControladorAdmin contAdmin = new ControladorAdmin(sistema);

        MenuConsola menu = new MenuConsola(contEstudiante, contAdmin);

        menu.mostrarMenuPrincipal();
    }
}