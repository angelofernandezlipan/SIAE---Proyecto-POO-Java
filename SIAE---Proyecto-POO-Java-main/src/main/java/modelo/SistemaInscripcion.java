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

    private void cargarDatosIniciales() {
        JsonObject datosEstudiantes = repositorio.cargarEstudiantes();
        JsonArray estudiantesArray = datosEstudiantes.getAsJsonArray("estudiantes");
        if (estudiantesArray != null) {
            this.estudiantes = UtilidadesJSON.jsonArrayToList(estudiantesArray, Estudiante.class);
        } else {
            this.estudiantes = new ArrayList<>();
        }

        JsonObject datosAsignaturas = repositorio.cargarAsignaturas();
        JsonArray asignaturasArray = datosAsignaturas.getAsJsonArray("asignaturas");

        if (asignaturasArray != null && asignaturasArray.size() > 0) {
            this.asignaturas = UtilidadesJSON.jsonArrayToList(asignaturasArray, Asignatura.class);
            System.out.println("SistemaInscripcion: Asignaturas cargadas desde archivo estático.");
        } else {
            System.out.println("SistemaInscripcion: Creando y guardando asignaturas por defecto en el archivo estático.");
            this.asignaturas = crearAsignaturasIniciales();
            repositorio.guardarAsignaturas(this.asignaturas);
        }

        System.out.println("SistemaInscripcion inicializado. Estudiantes: " + estudiantes.size() + ", Asignaturas: " + asignaturas.size());
    }

    private List<Asignatura> crearAsignaturasIniciales() {
        List<Asignatura> lista = new ArrayList<>();
        lista.add(new Asignatura("ART101", "Historia del Arte", "ARTISTICO", 45, 45));
        lista.add(new Asignatura("HUM202", "Filosofia y Etica", "HUMANISTA", 45, 45));
        lista.add(new Asignatura("CIE303", "Biologia Avanzada", "CIENTIFICO", 45, 45));
        lista.add(new Asignatura("HUM505", "Literatura Universal", "HUMANISTA", 45, 45));
        lista.add(new Asignatura("CIE606", "Fisica Moderna", "CIENTIFICO", 45, 45));
        lista.add(new Asignatura("ART404", "Dibujo y Pintura", "ARTISTICO", 45, 45));
        lista.add(new Asignatura("HUM707", "Historia de Chile", "HUMANISTA", 45, 45));
        return lista;
    }

    public void guardarDatos() {
        repositorio.guardarEstudiantes(estudiantes);
        repositorio.guardarAsignaturas(asignaturas);
    }


    public Estudiante validarCredenciales(String rut, String password) {
        String rutNormalizado = rut.replace(".", "").replace("-", "").trim();
        String passwordNormalizada = password.trim();

        return estudiantes.stream()
                .filter(est -> {
                    String estRutNormalizado = est.getRut().replace(".", "").replace("-", "").trim();
                    return estRutNormalizado.equals(rutNormalizado);
                })
                .filter(est -> {
                    String passwordGuardada = est.getPassword().trim();
                    return passwordNormalizada.equals("N/A") || passwordGuardada.equals(passwordNormalizada);
                })
                .findFirst()
                .orElse(null);
    }

    public void agregarEstudiante(Estudiante estudiante) {
        this.estudiantes.add(estudiante);
    }
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

    public String desinscribirAsignatura(Estudiante estudiante, String codigoAsignatura) {
        Asignatura asignatura = this.buscarAsignaturaPorCodigo(codigoAsignatura);
        if (asignatura == null) { return "Error: La asignatura no se encuentra en el sistema."; }
        if (!estudiante.getAsignaturasInscritas().contains(codigoAsignatura)) { return "Error: No estás inscrito en esta asignatura."; }
        estudiante.getAsignaturasInscritas().remove(codigoAsignatura);
        asignatura.incrementarCupos();
        this.guardarDatos();
        return "¡Desinscripción exitosa de " + asignatura.getNombre() + "!";
    }

    public void limpiarDatosSistema() {
        repositorio.limpiarEstudiantes();
        this.estudiantes = new ArrayList<>();
        this.asignaturas = repositorio.cargarAsignaturas().getAsJsonArray("asignaturas") != null ?
                UtilidadesJSON.jsonArrayToList(repositorio.cargarAsignaturas().getAsJsonArray("asignaturas"), Asignatura.class) :
                new ArrayList<>();
        System.out.println("Datos volátiles de estudiantes y de sesión limpiados.");
    }

    public List<Estudiante> getEstudiantes() { return estudiantes; }
    public List<Asignatura> getAsignaturas() { return asignaturas; }
}