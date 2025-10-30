package modelo;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import util.UtilidadesJSON;

public class RepositorioDatos {
    private static final String FILE_PATH = "src/main/resources/datos.json";

    /**
     * Carga todos los datos (Estudiantes y Asignaturas) desde el JSON.
     * @return JsonObject con "estudiantes" y "asignaturas".
     */
    public JsonObject cargarDatos() {
        JsonObject datos = UtilidadesJSON.leerJSON(FILE_PATH);
        if (datos == null) {
            // Retorna un objeto vacío para inicializar listas vacías en SistemaInscripcion
            datos = new JsonObject();
            datos.add("estudiantes", new JsonArray());
            datos.add("asignaturas", new JsonArray());
        }
        return datos;
    }

    /**
     * Guarda todos los datos (Estudiantes y Asignaturas) en el JSON.
     * @param estudiantes Lista de estudiantes.
     * @param asignaturas Lista de asignaturas.
     */
    public void guardarDatos(java.util.List<Estudiante> estudiantes, java.util.List<Asignatura> asignaturas) {
        JsonObject datos = new JsonObject();
        datos.add("estudiantes", UtilidadesJSON.listToJsonArray(estudiantes));
        datos.add("asignaturas", UtilidadesJSON.listToJsonArray(asignaturas));
        UtilidadesJSON.escribirJSON(FILE_PATH, datos);
    }

    /**
     * Limpia el JSON para un estado inicial vacío.
     */
    public void limpiarDatos() {
        JsonObject datosVacios = new JsonObject();
        datosVacios.add("estudiantes", new JsonArray());
        datosVacios.add("asignaturas", new JsonArray());
        UtilidadesJSON.escribirJSON(FILE_PATH, datosVacios);
        System.out.println("🚨 ATENCIÓN: Todos los datos (estudiantes y asignaturas) en el JSON han sido eliminados.");
    }
}