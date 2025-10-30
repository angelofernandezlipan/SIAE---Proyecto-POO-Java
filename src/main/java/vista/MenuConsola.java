package vista;

import controlador.ControladorAdmin;
import controlador.ControladorEstudiante;
import modelo.Estudiante;

import java.util.List;
import java.util.Scanner;

public class MenuConsola {
    private final ControladorEstudiante contEstudiante;
    private final ControladorAdmin contAdmin;
    private final Scanner scanner;

    // Recibe los controladores por inyección
    public MenuConsola(ControladorEstudiante contEstudiante, ControladorAdmin contAdmin) {
        this.contEstudiante = contEstudiante;
        this.contAdmin = contAdmin;
        this.scanner = new Scanner(System.in);
    }

    // Método principal
    public void mostrarMenuPrincipal() {
        while (true) {
            System.out.println("\n--- Sistema de Inscripción de Asignaturas Electivas (SIAE) ---");
            System.out.println("1. Iniciar sesión como Estudiante");
            System.out.println("2. Iniciar sesión como Administrador");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");

            String opcion = scanner.nextLine();
            switch (opcion) {
                case "1":
                    loginEstudiante();
                    break;
                case "2":
                    loginAdministrador();
                    break;
                case "3":
                    System.out.println("Saliendo del sistema. ¡Adiós!");
                    return;
                default:
                    System.out.println("Opción inválida. Intente de nuevo.");
            }
        }
    }

    // --- Menús y Lógica de Estudiante ---

    private void loginEstudiante() {
        System.out.print("Ingrese su RUT (sin puntos ni guión): ");
        String rut = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String password = scanner.nextLine();

        Estudiante estudiante = contEstudiante.login(rut, password);
        if (estudiante != null) {
            System.out.println("\n¡Bienvenido, " + estudiante.getNombre() + "!");
            menuEstudiante();
        } else {
            System.out.println("Credenciales incorrectas. Intente de nuevo.");
        }
    }

    private void menuEstudiante() {
        while (contEstudiante.haySesionActiva()) {
            System.out.println("\n--- Menú de Estudiante ---");
            System.out.println("1. Ver asignaturas disponibles");
            System.out.println("2. Inscribir una asignatura");
            System.out.println("3. Ver mis asignaturas inscritas");
            System.out.println("4. Cerrar sesión");
            System.out.print("Seleccione una opción: ");

            String opcion = scanner.nextLine();
            switch (opcion) {
                case "1":
                    mostrarAsignaturasDisponibles();
                    break;
                case "2":
                    inscribirAsignatura();
                    break;
                case "3":
                    mostrarInscripcionesEstudiante();
                    break;
                case "4":
                    contEstudiante.logout();
                    System.out.println("Sesión cerrada.");
                    return;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private void mostrarAsignaturasDisponibles() {
        System.out.println("\n--- Asignaturas Disponibles ---");
        List<String> disponibles = contEstudiante.obtenerAsignaturasDisponiblesInfo();
        if (disponibles.isEmpty()) {
            System.out.println("No hay asignaturas disponibles con cupos.");
        } else {
            disponibles.forEach(System.out::println);
        }
    }

    private void inscribirAsignatura() {
        if (contEstudiante.getEstudianteActual().getAsignaturasInscritas().size() >= 3) {
            System.out.println("No puedes inscribir más de 3 asignaturas.");
            return;
        }

        mostrarAsignaturasDisponibles();
        System.out.print("Ingrese el código de la asignatura a inscribir: ");
        String codigo = scanner.nextLine().toUpperCase();

        String resultado = contEstudiante.inscribirAsignatura(codigo);
        System.out.println(resultado);
    }

    private void mostrarInscripcionesEstudiante() {
        System.out.println("\n--- Tus Asignaturas Inscritas ---");
        contEstudiante.obtenerInscripcionesEstudiante().forEach(System.out::println);
    }

    // --- Menús y Lógica de Administrador ---

    private void loginAdministrador() {
        System.out.print("Ingrese el nombre de usuario de administrador: ");
        String user = scanner.nextLine();
        System.out.print("Ingrese la contraseña: ");
        String pass = scanner.nextLine();

        if (contAdmin.login(user, pass)) {
            System.out.println("\n¡Bienvenido, Administrador!");
            menuAdministrador();
        } else {
            System.out.println("Credenciales incorrectas.");
        }
    }

    private void menuAdministrador() {
        while (true) {
            System.out.println("\n--- Menú de Administrador ---");
            System.out.println("1. Carga Masiva de Estudiantes (TXT)");
            System.out.println("2. Exportar Reportes");
            System.out.println("3. Limpiar todos los datos (JSON)"); // NUEVA FUNCIÓN
            System.out.println("4. Cerrar sesión");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    cargarMasivaEstudiantes();
                    break;
                case "2":
                    menuExportarReportes();
                    break;
                case "3":
                    limpiarDatosAdmin(); // Llamada a la nueva función
                    break;
                case "4":
                    System.out.println("Sesión de administrador cerrada.");
                    return;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private void cargarMasivaEstudiantes() {
        System.out.println("\n--- Carga Masiva de Estudiantes ---");
        System.out.println("Asegure que el archivo TXT/CSV contenga líneas con: RUT, Nombre, Curso (ej: 3A, 4B)");
        System.out.print("Ingrese la ruta/nombre del archivo a cargar (ej: alumnos.txt): ");
        String filePath = scanner.nextLine();

        List<String> mensajes = contAdmin.cargarEstudiantes(filePath); // DELEGADO A CONTROLADOR

        System.out.println("\n--- Resultado de la Carga ---");
        mensajes.forEach(System.out::println);
        if (mensajes.get(0).startsWith("Carga masiva exitosa")) {
            System.out.println("Recuerde revisar el archivo 'reporte_credenciales_admin.txt' creado en la raíz.");
        }
    }

    private void menuExportarReportes() {
        while (true) {
            System.out.println("\n--- Exportar Reportes ---");
            System.out.println("1. Reporte de Cupos por Asignatura (TXT)");
            System.out.println("2. Reporte de Inscripciones por Estudiante y Curso (TXT)");
            System.out.println("3. Volver al Menú Admin");
            System.out.print("Seleccione un reporte para exportar: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    contAdmin.exportarReportes(1); // DELEGADO A CONTROLADOR
                    break;
                case "2":
                    contAdmin.exportarReportes(2); // DELEGADO A CONTROLADOR
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    // Lógica para la nueva función: Limpiar JSON
    private void limpiarDatosAdmin() {
        System.out.println("\n--- LIMPIAR DATOS DE PERSISTENCIA ---");
        System.out.println("⚠️ ADVERTENCIA: Esta acción eliminará PERMANENTEMENTE a TODOS los Estudiantes y Asignaturas del archivo de datos.");
        System.out.print("Escriba 'CONFIRMAR' para continuar: ");
        String confirmacion = scanner.nextLine();

        if ("CONFIRMAR".equals(confirmacion)) {
            contAdmin.limpiarDatosSistema(); // DELEGADO A CONTROLADOR
            System.out.println("✅ Datos del sistema eliminados exitosamente.");
        } else {
            System.out.println("❌ Operación cancelada. Los datos permanecen intactos.");
        }
    }
}