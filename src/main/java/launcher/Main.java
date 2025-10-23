package launcher;

public class Main {
    public static void main(String[] args) {
        // Clases principales.
        SistemaInscripcion sistema = new SistemaInscripcion();
        Sesion sesion = new Sesion();

        // Interfaz de consola.
        MenuConsola menu = new MenuConsola(sistema, sesion);

        // Bucle principal.
        menu.mostrarMenuPrincipal();
    }
}