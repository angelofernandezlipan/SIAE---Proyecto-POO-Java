import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SistemaInscripcion {
    private List<Estudiante> estudiantes;
    private List<Asignatura> asignaturas;
    private static final String FILE_PATH = "src/main/resources/datos.json";

    public SistemaInscripcion() {
        this.estudiantes = new ArrayList<>();
        this.asignaturas = new ArrayList<>();
        cargarDatosDesdeJSON();
    }

    // Cargar datos desde el archivo JSON
    public void cargarDatosDesdeJSON() {
        JsonObject datos = UtilidadesJSON.leerJSON(FILE_PATH);
        if (datos != null) {
            JsonArray estudiantesArray = datos.getAsJsonArray("estudiantes");
            JsonArray asignaturasArray = datos.getAsJsonArray("asignaturas");

            this.estudiantes = new ArrayList<>();
            if (estudiantesArray != null) {
                estudiantesArray.forEach(jsonElement -> {
                    JsonObject estObj = jsonElement.getAsJsonObject();
                    String rut = estObj.get("rut").getAsString();
                    String nombre = estObj.get("nombre").getAsString();
                    String password = estObj.get("password").getAsString();
                    List<String> inscritas = new ArrayList<>();
                    if (estObj.has("asignaturasInscritas") && estObj.get("asignaturasInscritas").isJsonArray()) {
                        estObj.getAsJsonArray("asignaturasInscritas").forEach(item -> inscritas.add(item.getAsString()));
                    }
                    this.estudiantes.add(new Estudiante(rut, nombre, password, inscritas));
                });
            }

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
            System.out.println("Datos cargados correctamente desde " + FILE_PATH);
        }
    }

    // Guardar datos en el archivo JSON
    public void guardarDatosEnJSON() {
        JsonObject datos = new JsonObject();
        datos.add("estudiantes", UtilidadesJSON.listToJsonArray(estudiantes));
        datos.add("asignaturas", UtilidadesJSON.listToJsonArray(asignaturas));
        UtilidadesJSON.escribirJSON(FILE_PATH, datos);
    }

    // Validar credenciales de un estudiante
    public Estudiante validarCredenciales(String rut, String password) {
        String rutNormalizado = rut.replace(".", "").replace("-", "");
        for (Estudiante est : estudiantes) {
            String estRutNormalizado = est.getRut().replace(".", "").replace("-", "");
            if (estRutNormalizado.equals(rutNormalizado) && est.getPassword().equals(password)) {
                return est;
            }
        }
        return null; // Retorna null si no encuentra el estudiante
    }

    // Mostrar asignaturas disponibles
    public void mostrarAsignaturasDisponibles() {
        System.out.println("\n--- Asignaturas Disponibles ---");
        asignaturas.stream()
                .filter(asig -> asig.getCuposDisponibles() > 0)
                .forEach(asig -> System.out.println(asig.toString()));
    }

    // Realizar inscripción de una asignatura
    public String inscribirAsignatura(Estudiante estudiante, String codigoAsignatura) {
        Asignatura asignatura = buscarAsignaturaPorCodigo(codigoAsignatura);
        if (asignatura == null) {
            return "Error: Asignatura no encontrada.";
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

    // Generar un reporte de inscripciones
    public void generarReporteInscripciones() {
        System.out.println("\n--- Reporte de Inscripciones ---");
        System.out.println("Total de estudiantes: " + estudiantes.size());
        System.out.println("\nEstado de cupos por asignatura:");
        for (Asignatura asig : asignaturas) {
            System.out.printf("- %s (%s): %d/%d cupos\n", asig.getNombre(), asig.getCodigo(), asig.getCuposDisponibles(), asig.getCuposMaximos());
        }
        System.out.println("\nAsignaturas inscritas por estudiante:");
        for (Estudiante est : estudiantes) {
            System.out.printf("- %s (%s): %d asignaturas inscritas\n", est.getNombre(), est.getRut(), est.getAsignaturasInscritas().size());
            est.getAsignaturasInscritas().forEach(asigCodigo -> {
                Asignatura asig = buscarAsignaturaPorCodigo(asigCodigo);
                if (asig != null) {
                    System.out.printf("  > %s (%s)\n", asig.getNombre(), asig.getSeccion());
                }
            });
        }
    }
}
