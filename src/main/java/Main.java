import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Scanner;

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