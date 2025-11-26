package modelo;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import util.UtilidadesJSON;

// Se definen dos rutas de archivos.
public class RepositorioDatos {
    private static final String FILE_DATOS_VOLATILES = "src/main/resources/datos_volatiles.json";
    private static final String FILE_ASIGNATURAS_ESTATICAS = "src/main/resources/asignaturas.json";

    // --- Métodos de Archivo de Asignaturas (Estático) ---

    /** Carga solo la información de Asignaturas desde su archivo dedicado. */
    public JsonObject cargarAsignaturas() {
        JsonObject datos = UtilidadesJSON.leerJSON(FILE_ASIGNATURAS_ESTATICAS);
        if (datos == null) {
            datos = new JsonObject();
            datos.add("asignaturas", new JsonArray());
        }
        return datos;
    }

    /** Guarda el estado (cupos y listas de inscritos) de las asignaturas. */
    public void guardarAsignaturas(java.util.List<Asignatura> asignaturas) {
        JsonObject datos = new JsonObject();
        datos.add("asignaturas", UtilidadesJSON.listToJsonArray(asignaturas));
        UtilidadesJSON.escribirJSON(FILE_ASIGNATURAS_ESTATICAS, datos);
    }

    // --- Métodos de Archivo de Datos Volátiles (Estudiantes) ---

    /** Carga solo la información volátil (Estudiantes) desde su archivo. */
    public JsonObject cargarEstudiantes() {
        JsonObject datos = UtilidadesJSON.leerJSON(FILE_DATOS_VOLATILES);
        if (datos == null) {
            datos = new JsonObject();
            datos.add("estudiantes", new JsonArray());
        }
        return datos;
    }

    /** Guarda solo la lista de Estudiantes (incluyendo credenciales). */
    public void guardarEstudiantes(java.util.List<Estudiante> estudiantes) {
        JsonObject datos = new JsonObject();
        datos.add("estudiantes", UtilidadesJSON.listToJsonArray(estudiantes));
        UtilidadesJSON.escribirJSON(FILE_DATOS_VOLATILES, datos);
    }

    /** Limpia los datos de estudiantes (el archivo volátil). */
    public void limpiarEstudiantes() {
        JsonObject datosVacios = new JsonObject();
        datosVacios.add("estudiantes", new JsonArray());
        UtilidadesJSON.escribirJSON(FILE_DATOS_VOLATILES, datosVacios);
        System.out.println("🚨 ATENCIÓN: Datos de estudiantes (volátiles) eliminados.");
    }
}