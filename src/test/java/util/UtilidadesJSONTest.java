package util;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;

class UtilidadesJSONTest {

    private final String RUTA_JSON_TEST = "src/test/resources/test_output.json";

    @BeforeEach
    void setUp() {
        // Asegurar que el directorio de recursos de prueba exista
        File directorio = new File("src/test/resources");
        if (!directorio.exists()) {
            directorio.mkdirs(); // Crea la carpeta si no existe
        }
    }

    @Test
    void testLeerJsonInexistente() {
        String rutaFalsa = "ruta/falsa/no_existe.json";
        JsonObject resultado = UtilidadesJSON.leerJSON(rutaFalsa);
        Assertions.assertNull(resultado, "Debe retornar null si el archivo JSON no existe");
    }

    @Test
    void testEscrituraYLecturaJson() {
        // OBJETIVO: Verificar ciclo completo (Escritura -> Lectura -> Integridad)
        // 1. Preparar Datos Originales
        JsonObject jsonOriginal = new JsonObject();
        jsonOriginal.addProperty("usuario", "pepito");
        jsonOriginal.addProperty("nivel", 99);

        // 2. Acción: Escribir en disco
        UtilidadesJSON.escribirJSON(RUTA_JSON_TEST, jsonOriginal);

        // 3. Verificación 1: Existencia física
        File archivo = new File(RUTA_JSON_TEST);
        Assertions.assertTrue(archivo.exists(), "El archivo debe existir físicamente");

        // 4. Verificación 2: Integridad de contenido (Leer lo que acabamos de escribir)
        JsonObject jsonLeido = UtilidadesJSON.leerJSON(RUTA_JSON_TEST);

        Assertions.assertNotNull(jsonLeido, "El JSON leído no debe ser null");

        // Comparamos propiedad por propiedad para estar seguros
        Assertions.assertEquals("pepito", jsonLeido.get("usuario").getAsString(), "El usuario debe coincidir");
        Assertions.assertEquals(99, jsonLeido.get("nivel").getAsInt(), "El nivel debe coincidir");

        // 5. Limpieza
        if (archivo.exists()) {
            archivo.delete();
        }
    }
}