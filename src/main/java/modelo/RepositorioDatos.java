package modelo;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import util.UtilidadesJSON;

import java.util.List;

public class RepositorioDatos {

    // 1. YA NO SON 'static final'. Ahora son variables que pueden cambiar.
    private final String rutaEstudiantes;
    private final String rutaAsignaturas;

    /**
     * Constructor por defecto (PRODUCCIÓN).
     * Este es el que usa tu App normal (Main.java).
     * Apunta a los archivos reales.
     */
    public RepositorioDatos() {
        this.rutaEstudiantes = "src/main/resources/datos_volatiles.json";
        this.rutaAsignaturas = "src/main/resources/asignaturas.json";
    }

    /**
     * Constructor para PRUEBAS (TEST).
     * @param modoTest Si es true, usa un archivo temporal para no borrar los datos.
     */
    public RepositorioDatos(boolean modoTest) {
        if (modoTest) {
            this.rutaEstudiantes = "src/main/resources/datos_volatiles_test.json";
            this.rutaAsignaturas = "src/main/resources/asignaturas.json";
        } else {
            this.rutaEstudiantes = "src/main/resources/datos_volatiles.json";
            this.rutaAsignaturas = "src/main/resources/asignaturas.json";
        }
    }

    // --- Métodos de Archivo de Asignaturas (Estático) ---

    public JsonObject cargarAsignaturas() {
        JsonObject datos = UtilidadesJSON.leerJSON(this.rutaAsignaturas);
        if (datos == null) {
            datos = new JsonObject();
            datos.add("asignaturas", new JsonArray());
        }
        return datos;
    }

    public void guardarAsignaturas(List<Asignatura> asignaturas) {
        JsonObject datos = new JsonObject();
        datos.add("asignaturas", UtilidadesJSON.listToJsonArray(asignaturas));
        UtilidadesJSON.escribirJSON(this.rutaAsignaturas, datos);
    }

    // --- Métodos de Archivo de Datos Volátiles (Estudiantes) ---

    public JsonObject cargarEstudiantes() {
        JsonObject datos = UtilidadesJSON.leerJSON(this.rutaEstudiantes);
        if (datos == null) {
            datos = new JsonObject();
            datos.add("estudiantes", new JsonArray());
        }
        return datos;
    }

    public void guardarEstudiantes(List<Estudiante> estudiantes) {
        JsonObject datos = new JsonObject();
        datos.add("estudiantes", UtilidadesJSON.listToJsonArray(estudiantes));
        UtilidadesJSON.escribirJSON(this.rutaEstudiantes, datos);
    }

    public void limpiarEstudiantes() {
        JsonObject datosVacios = new JsonObject();
        datosVacios.add("estudiantes", new JsonArray());
        UtilidadesJSON.escribirJSON(this.rutaEstudiantes, datosVacios);
        System.out.println("🚨 ATENCIÓN: Datos de estudiantes eliminados en: " + this.rutaEstudiantes);
    }
}