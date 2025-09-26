import java.util.Scanner;

public class MenuConsola {
    private final SistemaInscripcion sistema;
    private final Sesion sesion;
    private final Scanner scanner;

    public MenuConsola(SistemaInscripcion sistema, Sesion sesion) {
        this.sistema = sistema;
        this.sesion = sesion;
        this.scanner = new Scanner(System.in);
    }

    // Método para mostrar el menú principal
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

    // Lógica para el inicio de sesión del estudiante
    private void loginEstudiante() {
        System.out.print("Ingrese su RUT (sin puntos ni guión): ");
        String rut = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String password = scanner.nextLine();

        Estudiante estudiante = sistema.validarCredenciales(rut, password);
        if (estudiante != null) {
            sesion.iniciarSesion(estudiante);
            System.out.println("\n¡Bienvenido, " + sesion.getEstudianteActual().getNombre() + "!");
            menuEstudiante();
        } else {
            System.out.println("Credenciales incorrectas. Intente de nuevo.");
        }
    }

    // Menú para estudiantes
    private void menuEstudiante() {
        while (true) {
            System.out.println("\n--- Menú de Estudiante ---");
            System.out.println("1. Ver asignaturas disponibles");
            System.out.println("2. Inscribir una asignatura");
            System.out.println("3. Ver mis asignaturas inscritas");
            System.out.println("4. Cerrar sesión");
            System.out.print("Seleccione una opción: ");

            String opcion = scanner.nextLine();
            switch (opcion) {
                case "1":
                    sistema.mostrarAsignaturasDisponibles();
                    break;
                case "2":
                    inscribirAsignatura(sesion.getEstudianteActual());
                    break;
                case "3":
                    System.out.println("\n--- Tus Asignaturas Inscritas ---");
                    if (sesion.getEstudianteActual().getAsignaturasInscritas().isEmpty()) {
                        System.out.println("No tienes asignaturas inscritas.");
                    } else {
                        sesion.getEstudianteActual().getAsignaturasInscritas().forEach(codigo -> {
                            Asignatura asig = sistema.buscarAsignaturaPorCodigo(codigo);
                            if (asig != null) {
                                System.out.println("- " + asig.getNombre() + " (" + asig.getSeccion() + ")");
                            }
                        });
                    }
                    break;
                case "4":
                    sesion.cerrarSesion();
                    System.out.println("Sesión cerrada.");
                    return;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    // Lógica para inscribir una asignatura
    private void inscribirAsignatura(Estudiante estudiante) {
        if (estudiante.getAsignaturasInscritas().size() >= 3) {
            System.out.println("No puedes inscribir más de 3 asignaturas.");
            return;
        }

        sistema.mostrarAsignaturasDisponibles();
        System.out.print("Ingrese el código de la asignatura a inscribir: ");
        String codigo = scanner.nextLine().toUpperCase();

        String resultado = sistema.inscribirAsignatura(estudiante, codigo);
        System.out.println(resultado);
    }

    // Lógica para el inicio de sesión del administrador
    private void loginAdministrador() {
        System.out.print("Ingrese el nombre de usuario de administrador: ");
        String user = scanner.nextLine();
        System.out.print("Ingrese la contraseña: ");
        String pass = scanner.nextLine();

        if (user.equals("admin") && pass.equals("admin")) { // Credenciales simples para la demo
            System.out.println("\n¡Bienvenido, Administrador!");
            menuAdministrador();
        } else {
            System.out.println("Credenciales incorrectas.");
        }
    }

    // Menú para administradores
    private void menuAdministrador() {
        while (true) {
            System.out.println("\n--- Menú de Administrador ---");
            System.out.println("1. Generar reporte de inscripciones");
            System.out.println("2. Cerrar sesión");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();
            switch (opcion) {
                case "1":
                    sistema.generarReporteInscripciones();
                    break;
                case "2":
                    System.out.println("Sesión de administrador cerrada.");
                    return;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }
}