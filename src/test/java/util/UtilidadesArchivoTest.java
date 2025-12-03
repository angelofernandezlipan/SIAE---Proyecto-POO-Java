package util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.Arrays;
import java.util.List;

class UtilidadesArchivoTest {

    private final String RUTA_TXT_TEST = "src/test/resources/test_output.txt";
    private final String RUTA_CSV_TEST = "src/test/resources/test_carga_masiva.csv";

    @BeforeEach
    void setUp() {
        // Asegurar que el directorio de recursos de prueba exista
        File directorio = new File("src/test/resources");
        if (!directorio.exists()) {
            directorio.mkdirs(); // Crea la carpeta si no existe
        }
    }

    @Test
    void testLeerArchivoInexistente() {
        String rutaFalsa = "fantasma.txt";
        List<String> resultado = UtilidadesArchivo.leerArchivo(rutaFalsa);
        Assertions.assertNull(resultado, "Debe retornar null si el archivo no existe");
    }

    @Test
    void testExportarArchivoTexto() {
        // Prueba básica para TXT
        List<String> lineas = Arrays.asList("Hola", "Mundo");
        UtilidadesArchivo.exportarArchivo(RUTA_TXT_TEST, lineas);

        File archivo = new File(RUTA_TXT_TEST);
        Assertions.assertTrue(archivo.exists(), "El archivo TXT debe crearse");

        if (archivo.exists())
            archivo.delete();
    }

    // --- TEST PARA CSV ---
    @Test
    void testCicloCompletoCSV() {
        // OBJETIVO: Simular el formato real de Carga Masiva (RUT,Nombre,Curso)

        // 1. Datos simulando un CSV
        List<String> datosCsv = Arrays.asList(
                "11.111.111-1,Juan Perez,4A",
                "22.222.222-2,Maria Lopez,4B"
        );

        // 2. Exportar como .csv
        UtilidadesArchivo.exportarArchivo(RUTA_CSV_TEST, datosCsv);

        // 3. Verificar que existe
        File archivo = new File(RUTA_CSV_TEST);
        Assertions.assertTrue(archivo.exists(), "El archivo CSV debería existir");

        // 4. Leerlo de vuelta para asegurar integridad
        List<String> leido = UtilidadesArchivo.leerArchivo(RUTA_CSV_TEST);

        Assertions.assertNotNull(leido);
        Assertions.assertEquals(2, leido.size(), "Debe leer exactamente 2 líneas");
        Assertions.assertEquals("11.111.111-1,Juan Perez,4A", leido.get(0), "La primera línea debe mantenerse intacta");

        // 5. Limpieza
        if (archivo.exists()) {
            archivo.delete();
        }
    }
}
