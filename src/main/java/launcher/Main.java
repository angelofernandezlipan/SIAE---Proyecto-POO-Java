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
        ControladorLogin contLogin = new ControladorLogin(sistema);

        new VentanaLogin(contLogin);
    }
}