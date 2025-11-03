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
        // 1. Inicialización de la capa de Modelo y Persistencia
        RepositorioDatos repositorio = new RepositorioDatos();
        SistemaInscripcion sistema = new SistemaInscripcion(repositorio);
        Sesion sesion = new Sesion();

        // 2. Declaración de variables para los Controladores
        // Se declaran primero como variables para poder pasarlas como argumentos a sí mismas.
        ControladorEstudiante contEstudiante;
        ControladorAdmin contAdmin;

        // --- 3. Inicialización Mutua de Controladores Específicos ---
        // Inicializamos los controladores pasándonos la referencia de sí mismos (el 'this' global)
        // La referencia a 'contAdmin' se usa en 'contEstudiante' y viceversa para el Logout.

        contEstudiante = new ControladorEstudiante(sistema, sesion);

        // Se inicializa contAdmin con los 3 argumentos: sistema, contEstudiante y contAdmin (a sí mismo).
        // Se pasa la variable contAdmin (aunque aún no está completamente inicializada, su referencia es válida
        // para el constructor, ya que solo se usará después del Main).
        contAdmin = new ControladorAdmin(sistema, contEstudiante, null);
        // Inicializamos contAdmin con 'null' temporalmente para evitar un error de variable local.
        // Después, inyectamos la referencia correcta a contAdmin en contEstudiante, ya que contAdmin ya existe.

        // Corregimos la referencia que faltaba y la inyectamos en contEstudiante (usando el setter si existiera)
        // O más limpio: Inicializamos contAdmin con su propia referencia:

        // Vamos a reescribir la inicialización para asegurar que las dependencias mutuas se resuelvan correctamente:

        // 2. Controladores específicos: Se crean sin las referencias mutuas primero.
        ControladorEstudiante contEstudianteGlobal = new ControladorEstudiante(sistema, sesion);
        ControladorAdmin contAdminGlobal = new ControladorAdmin(sistema, null, null); // Inicialización placeholder

        // 3. Ahora que existen, se inyectan las referencias globales usando el méthodo setControladoresGlobales
        // (Este méthodo debe existir en tu ControladorAdmin y ControladorEstudiante para esta solución).
        contAdminGlobal.setControladoresGlobales(contEstudianteGlobal, contAdminGlobal);
        contEstudianteGlobal.setControladoresGlobales(contEstudianteGlobal, contAdminGlobal);

        // 4. Se crea el Controlador Login
        ControladorLogin contLogin = new ControladorLogin(contEstudianteGlobal, contAdminGlobal);

        // --- 5. Inicialización de la capa de Vista ---
        new VentanaLogin(contLogin);
    }
}