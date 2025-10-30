package modelo;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import util.UtilidadesJSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SistemaInscripcion {
    private List<Estudiante> estudiantes;
    private List<Asignatura> asignaturas;
    private final RepositorioDatos repositorio; // Inyección de dependencia

    public SistemaInscripcion(RepositorioDatos repositorio) {
        this.repositorio = repositorio;
        // Carga los datos existentes al inicio, NO los sobrescribe.
        cargarDatosIniciales();
    }

    // --- Métodos de Gestión Interna ---

    private void cargarDatosIniciales() {
        JsonObject datos = repositorio.cargarDatos();

        // Carga de Asignaturas
        JsonArray asignaturasArray = datos.getAsJsonArray("asignaturas");
        if (asignaturasArray != null) {
            this.asignaturas = UtilidadesJSON.jsonArrayToList(asignaturasArray, Asignatura.class);
        } else {
            this.asignaturas = new ArrayList<>();
        }

        // Carga de Estudiantes
        JsonArray estudiantesArray = datos.getAsJsonArray("estudiantes");
        if (estudiantesArray != null) {
            this.estudiantes = UtilidadesJSON.jsonArrayToList(estudiantesArray, Estudiante.class);
        } else {
            this.estudiantes = new ArrayList<>();
        }

        System.out.println("SistemaInscripcion inicializado. Estudiantes: " + estudiantes.size() + ", Asignaturas: " + asignaturas.size());
    }

    /** Guarda los datos. Debe ser llamado después de cada modificación al modelo. */
    public void guardarDatos() {
        repositorio.guardarDatos(estudiantes, asignaturas);
    }

    // --- Getters Públicos ---

    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public List<Asignatura> getAsignaturas() {
        return asignaturas;
    }

    // --- Lógica de Estudiante ---

    public Estudiante validarCredenciales(String rut, String password) {
        String rutNormalizado = rut.replace(".", "").replace("-", "");
        return estudiantes.stream()
                .filter(est -> est.getRut().replace(".", "").replace("-", "").equals(rutNormalizado))
                .filter(est -> password.equals("N/A") || est.getPassword().equals(password)) // "N/A" para chequeo interno
                .findFirst()
                .orElse(null);
    }

    /** Permite agregar un estudiante (usado por la Carga Masiva). */
    public void agregarEstudiante(Estudiante estudiante) {
        this.estudiantes.add(estudiante);
    }

    // --- Lógica de Asignatura ---

    public Asignatura buscarAsignaturaPorCodigo(String codigo) {
        return asignaturas.stream()
                .filter(asig -> asig.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null);
    }

    public List<Asignatura> obtenerAsignaturasDisponibles() {
        return asignaturas.stream()
                .filter(asig -> asig.getCuposDisponibles() > 0)
                .collect(Collectors.toList());
    }

    // --- Lógica de Inscripción ---

    public String inscribirAsignatura(Estudiante estudiante, String codigoAsignatura) {
        Asignatura asignatura = buscarAsignaturaPorCodigo(codigoAsignatura);
        if (asignatura == null) {
            return "Error: Asignatura no encontrada.";
        }

        // 1. Validación de cupos
        if (asignatura.getCuposDisponibles() <= 0) {
            return "Error: La asignatura no tiene cupos disponibles.";
        }

        // 2. Validación de inscritos (máximo 3)
        if (estudiante.getAsignaturasInscritas().size() >= 3) {
            return "Error: Ya tienes 3 asignaturas inscritas.";
        }

        // 3. Validación de duplicidad
        if (estudiante.getAsignaturasInscritas().contains(codigoAsignatura)) {
            return "Error: Ya estás inscrito en esta asignatura.";
        }

        // 4. Validación por sección (máximo 2 por sección)
        long countSeccion = estudiante.getAsignaturasInscritas().stream()
                .map(this::buscarAsignaturaPorCodigo)
                .filter(asig -> asig != null && asig.getSeccion().equals(asignatura.getSeccion()))
                .count();

        if (countSeccion >= 2) {
            return "Error: Ya tienes 2 asignaturas inscritas en la sección " + asignatura.getSeccion();
        }

        // Si todas las validaciones son exitosas, se realiza la inscripción
        estudiante.agregarAsignaturaInscrita(codigoAsignatura);
        asignatura.decrementarCupos(); // Actualización atómica del modelo
        guardarDatos(); // Persistir cambios
        return "¡Inscripción exitosa en " + asignatura.getNombre() + "!";
    }

    /** Llama a la función de limpieza del repositorio. */
    public void limpiarDatosSistema() {
        repositorio.limpiarDatos();
        // Recarga el sistema con listas vacías después de la limpieza
        this.estudiantes = new ArrayList<>();
        this.asignaturas = new ArrayList<>();
    }
}