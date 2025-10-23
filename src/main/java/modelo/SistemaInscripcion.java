package modelo;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class SistemaInscripcion {
    private List<Estudiante> estudiantes;
    private List<Asignatura> asignaturas;
    private static final String FILE_PATH = "src/main/resources/datos.json";

    public SistemaInscripcion() {
        // CORRECCIÓN CLAVE: Iniciar con listas vacías y guardar para limpiar el JSON al inicio.
        this.estudiantes = new ArrayList<>();
        this.asignaturas = new ArrayList<>();

        // 1. Cargar la lista inicial de asignaturas (si existe el JSON)
        cargarAsignaturasDesdeJSON();

        // 2. Sobrescribir el JSON al inicio para resetear la lista de estudiantes
        // Esto asume que las asignaturas están codificadas en el JSON y los estudiantes se cargarán después.
        guardarDatosEnJSON();
    }

    // Método auxiliar para la carga masiva
    public void agregarEstudiante(Estudiante estudiante) {
        this.estudiantes.add(estudiante);
    }

    // Getters Públicos para modelo.GestionAdministrativa
    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public List<Asignatura> getAsignaturas() {
        return asignaturas;
    }

    // --- Lógica de Carga y Persistencia ---

    // Carga solo la información de Asignaturas al inicio (si existe)
    private void cargarAsignaturasDesdeJSON() {
        JsonObject datos = UtilidadesJSON.leerJSON(FILE_PATH);
        if (datos != null && datos.has("asignaturas")) {
            JsonArray asignaturasArray = datos.getAsJsonArray("asignaturas");
            this.asignaturas = new ArrayList<>();
            if (asignaturasArray != null) {
                asignaturasArray.forEach(jsonElement -> {
                    JsonObject asigObj = jsonElement.getAsJsonObject();
                    String codigo = asigObj.get("codigo").getAsString();
                    String nombreAsig = asigObj.get("nombre").getAsString();
                    String seccion = asigObj.get("seccion").getAsString();
                    int cuposMaximos = asigObj.get("cuposMaximos").getAsInt();
                    int cuposDisponibles = asigObj.get("cuposDisponibles").getAsInt();
                    this.asignaturas.add(new Asignatura(codigo, nombreAsig, seccion, cuposMaximos, cuposDisponibles));
                });
            }
            System.out.println("Datos de asignaturas cargados/inicializados correctamente.");
        } else {
            // Si el JSON no existe, inicializa la lista de asignaturas vacía
            this.asignaturas = new ArrayList<>();
        }
        // Inicializa estudiantes vacía
        this.estudiantes = new ArrayList<>();
    }

    // Guarda datos en el archivo JSON
    public void guardarDatosEnJSON() {
        JsonObject datos = new JsonObject();
        datos.add("estudiantes", UtilidadesJSON.listToJsonArray(estudiantes));
        datos.add("asignaturas", UtilidadesJSON.listToJsonArray(asignaturas));
        UtilidadesJSON.escribirJSON(FILE_PATH, datos);
    }

    // Valida credenciales de un estudiante
    public Estudiante validarCredenciales(String rut, String password) {
        String rutNormalizado = rut.replace(".", "").replace("-", "");
        for (Estudiante est : estudiantes) {
            String estRutNormalizado = est.getRut().replace(".", "").replace("-", "");
            // La validación verifica el RUT y si la contraseña es "N/A" (para control interno)
            if (estRutNormalizado.equals(rutNormalizado)) {
                if (password.equals("N/A") || est.getPassword().equals(password)) {
                    return est;
                }
            }
        }
        return null;
    }

    // Mostrar asignaturas disponibles
    public void mostrarAsignaturasDisponibles() {
        System.out.println("\n--- Asignaturas Disponibles ---");
        asignaturas.stream()
                .filter(asig -> asig.getCuposDisponibles() > 0)
                .forEach(asig -> System.out.println(asig.toString()));
    }

    // Inscripción de una asignatura
    public String inscribirAsignatura(Estudiante estudiante, String codigoAsignatura) {
        Asignatura asignatura = buscarAsignaturaPorCodigo(codigoAsignatura);
        if (asignatura == null) {
            return "Error: modelo.Asignatura no encontrada.";
        }

        // Validación de reglas
        if (estudiante.getAsignaturasInscritas().size() >= 3) {
            return "Error: Ya tienes 3 asignaturas inscritas.";
        }
        if (asignatura.getCuposDisponibles() <= 0) {
            return "Error: La asignatura no tiene cupos disponibles.";
        }
        if (estudiante.getAsignaturasInscritas().contains(codigoAsignatura)) {
            return "Error: Ya estás inscrito en esta asignatura.";
        }

        long countSeccion = estudiante.getAsignaturasInscritas().stream()
                .map(this::buscarAsignaturaPorCodigo)
                .filter(asig -> asig != null && asig.getSeccion().equals(asignatura.getSeccion()))
                .count();

        if (countSeccion >= 2) {
            return "Error: Ya tienes 2 asignaturas inscritas en la sección " + asignatura.getSeccion();
        }

        // Si todas las validaciones son exitosas, se realiza la inscripción
        estudiante.agregarAsignaturaInscrita(codigoAsignatura);
        asignatura.setCuposDisponibles(asignatura.getCuposDisponibles() - 1);
        guardarDatosEnJSON(); // Guardar cambios inmediatamente
        return "¡Inscripción exitosa en " + asignatura.getNombre() + "!";
    }

    // Buscar una asignatura por su código
    public Asignatura buscarAsignaturaPorCodigo(String codigo) {
        return asignaturas.stream()
                .filter(asig -> asig.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null);
    }

    // Generar un reporte de inscripciones (Función ahora delegada a modelo.GestionAdministrativa)
    public void generarReporteInscripciones() {
        System.out.println("\n--- Reporte de Inscripciones ---");
        System.out.println("Esta función ahora utiliza modelo.GestionAdministrativa para exportar un archivo TXT.");
    }
}