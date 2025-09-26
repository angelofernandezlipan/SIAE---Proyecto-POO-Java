import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Se inicializan las clases principales del sistema.
        SistemaInscripcion sistema = new SistemaInscripcion();
        Sesion sesion = new Sesion();

        // Se inicializa la interfaz de consola, pasándole las instancias necesarias.
        MenuConsola menu = new MenuConsola(sistema, sesion);

        // Se ejecuta el bucle principal de la aplicación.
        menu.mostrarMenuPrincipal();
    }
}