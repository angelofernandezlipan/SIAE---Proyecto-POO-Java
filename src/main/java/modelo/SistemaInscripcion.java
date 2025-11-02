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
    private final RepositorioDatos repositorio;

    public SistemaInscripcion(RepositorioDatos repositorio) {
        this.repositorio = repositorio;
        cargarDatosIniciales();
    }

    // --- Métodos de Gestión Interna (se mantienen) ---
    private void cargarDatosIniciales() {
        JsonObject datos = repositorio.cargarDatos();
        // ... (lógica de carga) ...
        JsonArray asignaturasArray = datos.getAsJsonArray("asignaturas");
        if (asignaturasArray != null) {
            this.asignaturas = UtilidadesJSON.jsonArrayToList(asignaturasArray, Asignatura.class);
        } else {
            this.asignaturas = new ArrayList<>();
        }
        JsonArray estudiantesArray = datos.getAsJsonArray("estudiantes");
        if (estudiantesArray != null) {
            this.estudiantes = UtilidadesJSON.jsonArrayToList(estudiantesArray, Estudiante.class);
        } else {
            this.estudiantes = new ArrayList<>();
        }
        System.out.println("SistemaInscripcion inicializado. Estudiantes: " + estudiantes.size() + ", Asignaturas: " + asignaturas.size());
    }

    public void guardarDatos() {
        repositorio.guardarDatos(estudiantes, asignaturas);
    }

    // --- Lógica de Estudiante (Validación CORREGIDA) ---

    public Estudiante validarCredenciales(String rut, String password) {
        // Normalización del RUT ingresado
        String rutNormalizado = rut.replace(".", "").replace("-", "").trim();

        // Normalización de la contraseña ingresada
        String passwordNormalizada = password.trim();

        return estudiantes.stream()
                .filter(est -> {
                    // Normalización del RUT guardado
                    String estRutNormalizado = est.getRut().replace(".", "").replace("-", "").trim();
                    return estRutNormalizado.equals(rutNormalizado);
                })
                .filter(est -> {
                    // Normalización de la contraseña guardada
                    String passwordGuardada = est.getPassword().trim();

                    // Compara la contraseña normalizada
                    return passwordNormalizada.equals("N/A") || passwordGuardada.equals(passwordNormalizada);
                })
                .findFirst()
                .orElse(null);
    }

    /** Permite agregar un estudiante (usado por la Carga Masiva). */
    public void agregarEstudiante(Estudiante estudiante) {
        this.estudiantes.add(estudiante);
    }

    // --- Resto de Métodos (se mantienen) ---
    public List<Estudiante> getEstudiantes() { return estudiantes; }
    public List<Asignatura> getAsignaturas() { return asignaturas; }
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
    public String inscribirAsignatura(Estudiante estudiante, String codigoAsignatura) {
        Asignatura asignatura = buscarAsignaturaPorCodigo(codigoAsignatura);
        if (asignatura == null) { return "Error: Asignatura no encontrada."; }
        if (asignatura.getCuposDisponibles() <= 0) { return "Error: La asignatura no tiene cupos disponibles."; }
        if (estudiante.getAsignaturasInscritas().size() >= 3) { return "Error: Ya tienes 3 asignaturas inscritas."; }
        if (estudiante.getAsignaturasInscritas().contains(codigoAsignatura)) { return "Error: Ya estás inscrito en esta asignatura."; }
        long countSeccion = estudiante.getAsignaturasInscritas().stream()
                .map(this::buscarAsignaturaPorCodigo)
                .filter(asig -> asig != null && asig.getSeccion().equals(asignatura.getSeccion()))
                .count();
        if (countSeccion >= 2) { return "Error: Ya tienes 2 asignaturas inscritas en la sección " + asignatura.getSeccion(); }
        estudiante.agregarAsignaturaInscrita(codigoAsignatura);
        asignatura.decrementarCupos();
        guardarDatos();
        return "¡Inscripción exitosa en " + asignatura.getNombre() + "!";
    }
    public void limpiarDatosSistema() {
        repositorio.limpiarDatos();
        this.estudiantes = new ArrayList<>();
        this.asignaturas = new ArrayList<>();
    }
    public String desinscribirAsignatura(Estudiante estudiante, String codigoAsignatura) {
        Asignatura asignatura = this.buscarAsignaturaPorCodigo(codigoAsignatura);
        if (asignatura == null) { return "Error: La asignatura no se encuentra en el sistema."; }
        if (!estudiante.getAsignaturasInscritas().contains(codigoAsignatura)) { return "Error: No estás inscrito en esta asignatura."; }
        estudiante.getAsignaturasInscritas().remove(codigoAsignatura);
        asignatura.incrementarCupos();
        this.guardarDatos();
        return "¡Desinscripción exitosa de " + asignatura.getNombre() + "!";
    }
}