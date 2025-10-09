import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PruebasUnitarias {
    private SistemaInscripcion sistema;

    // La ruta debe coincidir con la que usa SistemaInscripcion
    private static final String FILE_PATH = "src/main/resources/datos.json";

    // Datos JSON limpios para resetear el archivo antes de cada prueba
    private static final String JSON_INICIAL_LIMPIO = "{\n" +
            "  \"estudiantes\": [\n" +
            "    {\n" +
            "      \"rut\": \"12.345.678-9\",\n" +
            "      \"nombre\": \"nn1\",\n" +
            "      \"password\": \"pass\",\n" +
            "      \"asignaturasInscritas\": []\n" +
            "    },\n" +
            "    {\n" +
            "      \"rut\": \"98.765.432-1\",\n" +
            "      \"nombre\": \"nn2\",\n" +
            "      \"password\": \"pass\",\n" +
            "      \"asignaturasInscritas\": []\n" +
            "    }\n" +
            "  ],\n" +
            "  \"asignaturas\": [\n" +
            "    {\n" +
            "      \"codigo\": \"ART101\",\n" +
            "      \"nombre\": \"Historia del Arte\",\n" +
            "      \"seccion\": \"ARTISTICO\",\n" +
            "      \"cuposMaximos\": 45,\n" +
            "      \"cuposDisponibles\": 45\n" +
            "    },\n" +
            "    {\n" +
            "      \"codigo\": \"HUM202\",\n" +
            "      \"nombre\": \"Filosofia y Etica\",\n" +
            "      \"seccion\": \"HUMANISTA\",\n" +
            "      \"cuposMaximos\": 45,\n" +
            "      \"cuposDisponibles\": 45\n" +
            "    },\n" +
            "    {\n" +
            "      \"codigo\": \"CIE303\",\n" +
            "      \"nombre\": \"Biologia Avanzada\",\n" +
            "      \"seccion\": \"MATEMATICO_CIENTIFICO\",\n" +
            "      \"cuposMaximos\": 45,\n" +
            "      \"cuposDisponibles\": 45\n" +
            "    },\n" +
            "    {\n" +
            "      \"codigo\": \"ART404\",\n" +
            "      \"nombre\": \"Dibujo y Pintura\",\n" +
            "      \"seccion\": \"ARTISTICO\",\n" +
            "      \"cuposMaximos\": 45,\n" +
            "      \"cuposDisponibles\": 45\n" +
            "    },\n" +
            "    {\n" +
            "      \"codigo\": \"HUM505\",\n" +
            "      \"nombre\": \"Literatura Universal\",\n" +
            "      \"seccion\": \"HUMANISTA\",\n" +
            "      \"cuposMaximos\": 45,\n" +
            "      \"cuposDisponibles\": 45\n" +
            "    },\n" +
            "    {\n" +
            "      \"codigo\": \"CIE606\",\n" +
            "      \"nombre\": \"Fisica Moderna\",\n" +
            "      \"seccion\": \"MATEMATICO_CIENTIFICO\",\n" +
            "      \"cuposMaximos\": 45,\n" +
            "      \"cuposDisponibles\": 45\n" +
            "    },\n" +
            "    {\n" +
            "      \"codigo\": \"HUM707\",\n" +
            "      \"nombre\": \"Historia de Chile\",\n" +
            "      \"seccion\": \"HUMANISTA\",\n" +
            "      \"cuposMaximos\": 45,\n" +
            "      \"cuposDisponibles\": 45\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    @BeforeEach
    public void setUp() {
        // 1. Resetear el archivo datos.json a su estado inicial ANTES de cargar
        UtilidadesJSON.escribirJSONPlano(FILE_PATH, JSON_INICIAL_LIMPIO);

        // 2. Inicializar el sistema (llama al constructor que carga los datos limpios)
        sistema = new SistemaInscripcion();
    }

    @Test
    public void testLoginCorrecto() {
        Estudiante estudiante = sistema.validarCredenciales("123456789", "pass");
        Assertions.assertNotNull(estudiante, "El login debería ser exitoso para credenciales válidas");
        Assertions.assertEquals("nn1", estudiante.getNombre(), "El nombre del estudiante debería coincidir");
    }

    @Test
    public void testLoginIncorrecto() {
        Estudiante estudiante = sistema.validarCredenciales("999999999", "passIncorrecta");
        Assertions.assertNull(estudiante, "El login debería fallar para credenciales incorrectas");
    }

    @Test
    public void testInscripcionExitosa() {
        Estudiante estudiante = sistema.validarCredenciales("123456789", "pass");
        Assertions.assertNotNull(estudiante);

        String resultado = sistema.inscribirAsignatura(estudiante, "ART101");

        // La aserción verifica el inicio del mensaje de éxito
        Assertions.assertTrue(resultado.startsWith("¡Inscripción exitosa"), "La inscripción debería ser exitosa");
        Assertions.assertTrue(estudiante.getAsignaturasInscritas().contains("ART101"), "El estudiante debería tener la asignatura inscrita");
    }

    @Test
    public void testInscripcionSinCupos() {
        Estudiante estudiante = sistema.validarCredenciales("123456789", "pass");
        Assertions.assertNotNull(estudiante);

        // Modificamos el objeto en memoria para la prueba
        Asignatura asig = sistema.buscarAsignaturaPorCodigo("CIE303");
        // Forzamos 0 cupos
        asig.setCuposDisponibles(0);

        // Probamos la inscripción con 0 cupos en memoria
        String resultado = sistema.inscribirAsignatura(estudiante, "CIE303");

        // La aserción verifica el inicio del mensaje de error
        Assertions.assertTrue(resultado.startsWith("Error: La asignatura no tiene cupos"), "La inscripción debería fallar por falta de cupos");
        Assertions.assertFalse(estudiante.getAsignaturasInscritas().contains("CIE303"), "El estudiante NO debería tener la asignatura inscrita");
    }
}
