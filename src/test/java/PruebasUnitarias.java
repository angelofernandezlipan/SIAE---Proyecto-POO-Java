import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PruebasUnitarias {
    private SistemaInscripcion sistema;

    @BeforeEach
    public void setUp() {
        sistema = new SistemaInscripcion();
        sistema.cargarDatosDesdeJSON(); // Cargar datos antes de cada prueba
    }

    @Test
    public void testLoginCorrecto() {
        // Asumiendo que "nn1" y "pass" son credenciales válidas en tu datos.json
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
        // Suponiendo que el código de asignatura es válido y tiene cupos
        String resultado = sistema.inscribirAsignatura(estudiante, "ART101");
        Assertions.assertTrue(resultado.startsWith("¡Inscripción exitosa"), "La inscripción debería ser exitosa");
        Assertions.assertTrue(estudiante.getAsignaturasInscritas().contains("ART101"), "El estudiante debería tener la asignatura inscrita");
    }

    @Test
    public void testInscripcionSinCupos() {
        Estudiante estudiante = sistema.validarCredenciales("123456789", "pass");
        Assertions.assertNotNull(estudiante);

        // Llenar cupos de la asignatura para la prueba
        Asignatura asig = sistema.buscarAsignaturaPorCodigo("CIE303");
        asig.setCuposDisponibles(0);

        String resultado = sistema.inscribirAsignatura(estudiante, "CIE303");
        Assertions.assertTrue(resultado.startsWith("Error: La asignatura no tiene cupos"), "La inscripción debería fallar por falta de cupos");
    }
}
