package test; // Asume que tus pruebas están en el paquete 'test'

import modelo.Asignatura;
import modelo.Estudiante;
import modelo.RepositorioDatos; // CLAVE: Necesitamos el Repositorio
import modelo.SistemaInscripcion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class SistemaInscripcionTest {
    private SistemaInscripcion sistema;
    private RepositorioDatos repositorio; // Nueva referencia
    private Estudiante estudiantePrueba1;
    private Estudiante estudiantePrueba2;

    // La ruta debe coincidir con la que usa modelo.RepositorioDatos
    // No la definimos aquí, ya que está encapsulada en RepositorioDatos.

    @BeforeEach
    public void setUp() {
        // 1. Inicializar el Repositorio y el Sistema
        repositorio = new RepositorioDatos();
        sistema = new SistemaInscripcion(repositorio);

        // 2. Limpiar el JSON usando el método del sistema, garantizando un estado inicial limpio
        // Nota: Esto simula la limpieza manual que haría el Admin si el sistema no persistiera.
        sistema.limpiarDatosSistema();

        // 3. Crear datos iniciales en memoria (y persistirlos)
        estudiantePrueba1 = new Estudiante("12.345.678-9", "nn1", "pass", "4A");
        estudiantePrueba2 = new Estudiante("98.765.432-1", "nn2", "pass", "4B");

        Asignatura art101 = new Asignatura("ART101", "Historia del Arte", "ARTISTICO", 45, 45);
        Asignatura hum202 = new Asignatura("HUM202", "Filosofia y Etica", "HUMANISTA", 45, 45);
        Asignatura cie303 = new Asignatura("CIE303", "Biologia Avanzada", "MATEMATICO_CIENTIFICO", 45, 45);
        Asignatura art404 = new Asignatura("ART404", "Dibujo y Pintura", "ARTISTICO", 45, 45);
        Asignatura hum505 = new Asignatura("HUM505", "Literatura Universal", "HUMANISTA", 45, 45);
        Asignatura hum707 = new Asignatura("HUM707", "Historia de Chile", "HUMANISTA", 45, 45);

        sistema.agregarEstudiante(estudiantePrueba1);
        sistema.agregarEstudiante(estudiantePrueba2);

        // El sistema carga las asignaturas desde el JSON, si la limpieza fue total, hay que re-agregarlas:
        // Pero para simplificar, asumiremos que las asignaturas están precargadas en el JSON
        // O agregamos asignaturas de prueba para asegurar que existen
        // (Dependiendo de cómo esté tu archivo 'datos.json' después de la limpieza)

        // Asumiendo que el JSON base de la prueba está vacío para Asignaturas tras el clear, las agregamos:
        List<Asignatura> asignaturasIniciales = List.of(art101, hum202, cie303, art404, hum505, hum707);

        // Ya que SistemaInscripcion.getAsignaturas() retorna la lista actual,
        // la llenamos manualmente para la prueba y luego guardamos.
        sistema.getAsignaturas().clear(); // Limpia asignaturas si el clearAdmin no lo hizo
        sistema.getAsignaturas().addAll(asignaturasIniciales);

        // Persistir el estado inicial
        sistema.guardarDatos();

        // 4. Recargar el sistema (o usar el existente) para asegurar que los datos vienen del JSON.
        // Usaremos el existente para simplicidad.

        // Re-obtener estudiantes del sistema para asegurar que son los objetos manejados por el sistema
        estudiantePrueba1 = sistema.validarCredenciales("123456789", "pass");
    }

    // ----------------------------------------------------------------------------------
    // Tests de Login
    // ----------------------------------------------------------------------------------

    @Test
    public void testLoginCorrecto() {
        Estudiante estudiante = sistema.validarCredenciales("123456789", "pass");
        Assertions.assertNotNull(estudiante, "El login debería ser exitoso para credenciales válidas.");
        Assertions.assertEquals("nn1", estudiante.getNombre(), "El nombre del estudiante debería coincidir.");
    }

    @Test
    public void testLoginIncorrecto() {
        Estudiante estudiante = sistema.validarCredenciales("999999999", "passIncorrecta");
        Assertions.assertNull(estudiante, "El login debería fallar para credenciales incorrectas.");
    }

    // ----------------------------------------------------------------------------------
    // Tests de Inscripción Exitosa
    // ----------------------------------------------------------------------------------

    @Test
    public void testInscripcionExitosa() {
        // Aseguramos que el estudiantePrueba1 está cargado
        Assertions.assertNotNull(estudiantePrueba1);
        int cuposIniciales = sistema.buscarAsignaturaPorCodigo("ART101").getCuposDisponibles();

        String resultado = sistema.inscribirAsignatura(estudiantePrueba1, "ART101");

        // Verificaciones
        Assertions.assertTrue(resultado.startsWith("¡Inscripción exitosa"), "La inscripción debería ser exitosa.");
        Assertions.assertTrue(estudiantePrueba1.getAsignaturasInscritas().contains("ART101"), "El estudiante debería tener la asignatura inscrita.");
        Assertions.assertEquals(cuposIniciales - 1, sistema.buscarAsignaturaPorCodigo("ART101").getCuposDisponibles(), "Los cupos disponibles deben decrementar en 1.");
    }

    // ----------------------------------------------------------------------------------
    // Tests de Restricciones
    // ----------------------------------------------------------------------------------

    @Test
    public void testInscripcionSinCupos() {
        Assertions.assertNotNull(estudiantePrueba1);

        // Usamos la asignatura CIE303 de prueba
        Asignatura asig = sistema.buscarAsignaturaPorCodigo("CIE303");
        asig.decrementarCupos(); // Decrementamos cupo
        asig.decrementarCupos(); // Y así sucesivamente hasta que queden 0 cupos. (Simulación rápida para prueba)
        asig.decrementarCupos();
        asig.decrementarCupos();
        // ... (Simulamos un decremento hasta 0. En un caso real, esto se haría antes)
        // Por la inicialización, sabemos que tiene 45. Lo dejamos con 0 para la prueba:
        for (int i = 0; i < 45; i++) {
            asig.decrementarCupos();
        }

        Assertions.assertEquals(0, asig.getCuposDisponibles());

        // Probamos la inscripción con 0 cupos
        String resultado = sistema.inscribirAsignatura(estudiantePrueba1, "CIE303");

        Assertions.assertTrue(resultado.startsWith("Error: La asignatura no tiene cupos"), "La inscripción debería fallar por falta de cupos.");
        Assertions.assertFalse(estudiantePrueba1.getAsignaturasInscritas().contains("CIE303"), "El estudiante NO debería tener la asignatura inscrita.");
    }

    @Test
    public void testRestriccionMaximoDosPorSeccion() {
        Assertions.assertNotNull(estudiantePrueba1);

        // 1. Inscribe la primera humanista (HUM202) - Éxito
        sistema.inscribirAsignatura(estudiantePrueba1, "HUM202");
        // 2. Inscribe la segunda humanista (HUM505) - Éxito
        sistema.inscribirAsignatura(estudiantePrueba1, "HUM505");

        // 3. Intenta inscribir la tercera humanista (HUM707) - Debe fallar
        String resultado = sistema.inscribirAsignatura(estudiantePrueba1, "HUM707");

        Assertions.assertTrue(resultado.startsWith("Error: Ya tienes 2 asignaturas inscritas en la sección HUMANISTA"), "La inscripción debe fallar por el límite de 2 por sección.");
        Assertions.assertEquals(2, estudiantePrueba1.getAsignaturasInscritas().size(), "El estudiante solo debe tener 2 asignaturas inscritas.");
    }

    @Test
    public void testRestriccionMaximoTresTotal() {
        Assertions.assertNotNull(estudiantePrueba1);

        // 1. Inscribe ARTISTICO (ART101)
        sistema.inscribirAsignatura(estudiantePrueba1, "ART101");
        // 2. Inscribe HUMANISTA (HUM202)
        sistema.inscribirAsignatura(estudiantePrueba1, "HUM202");
        // 3. Inscribe MATEMATICO_CIENTIFICO (CIE303)
        sistema.inscribirAsignatura(estudiantePrueba1, "CIE303");

        // Intenta inscribir la 4ta asignatura (ART404, de otra sección pero ya son 3)
        String resultado = sistema.inscribirAsignatura(estudiantePrueba1, "ART404");

        Assertions.assertTrue(resultado.startsWith("Error: Ya tienes 3 asignaturas inscritas"), "La inscripción debe fallar por el límite total de 3 asignaturas.");
        Assertions.assertEquals(3, estudiantePrueba1.getAsignaturasInscritas().size(), "El estudiante solo debe tener 3 asignaturas inscritas.");
    }
}