package modelo;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class RepositorioDatosTest {

    private RepositorioDatos repositorioTest;
    private final String RUTA_TEST = "src/main/resources/datos_volatiles_test.json";

    @BeforeEach
    void setUp() {
        // 1. Instanciamos en MODO TEST (true)
        repositorioTest = new RepositorioDatos(true);

        // 2. Aseguramos un inicio limpio borrando el archivo de prueba anterior si existe
        File archivoTest = new File(RUTA_TEST);
        if (archivoTest.exists()) {
            archivoTest.delete();
        }
    }

    @Test
    void testAislamientoDeArchivos() {
        // OBJETIVO: Verificar que guardar en modo test NO toca el archivo real.

        // 1. Crear un estudiante "falso" que solo debería existir en pruebas
        Estudiante estudianteFantasma = new Estudiante("99.999.999-K", "Fantasma Test", "pass", "4A");
        List<Estudiante> listaTest = new ArrayList<>();
        listaTest.add(estudianteFantasma);

        // 2. Guardar usando el repositorio de prueba
        repositorioTest.guardarEstudiantes(listaTest);

        // VERIFICACIÓN 1: El archivo de prueba debe existir
        File archivoTest = new File(RUTA_TEST);
        Assertions.assertTrue(archivoTest.exists(), "El archivo datos_volatiles_test.json debería haberse creado.");

        // VERIFICACIÓN 2: El archivo de PRODUCCIÓN no debe tener este dato
        // Instanciamos un repositorio NORMAL (false / constructor vacío) para leer el archivo real
        RepositorioDatos repositorioProduccion = new RepositorioDatos();
        JsonObject datosReales = repositorioProduccion.cargarEstudiantes();
        JsonArray arrayReales = datosReales.getAsJsonArray("estudiantes");

        // Buscamos si el "Fantasma Test" se coló en los datos reales
        boolean encontradoEnProd = false;
        if (arrayReales != null) {
            for (int i = 0; i < arrayReales.size(); i++) {
                JsonObject jsonEst = arrayReales.get(i).getAsJsonObject();
                if (jsonEst.get("rut").getAsString().equals("99.999.999-K")) {
                    encontradoEnProd = true;
                    break;
                }
            }
        }

        Assertions.assertFalse(encontradoEnProd, "¡PELIGRO! El dato de prueba se guardó en el archivo de producción.");
    }

    @Test
    void testLecturaDeArchivoVacioOInexistente() {
        // OBJETIVO: Verificar que si no hay archivo, el sistema no se cae (no devuelve null).

        // 1. Aseguramos que NO existe el archivo de prueba (lo borramos)
        File archivoTest = new File(RUTA_TEST);
        if (archivoTest.exists()) {
            archivoTest.delete();
        }

        // 2. Intentamos cargar estudiantes
        JsonObject resultado = repositorioTest.cargarEstudiantes();

        // 3. Validaciones
        Assertions.assertNotNull(resultado, "El método no debe devolver null aunque no exista el archivo.");
        Assertions.assertTrue(resultado.has("estudiantes"), "El JSON debe tener la clave 'estudiantes'.");
        Assertions.assertEquals(0, resultado.getAsJsonArray("estudiantes").size(), "La lista de estudiantes debe estar vacía.");
    }
}