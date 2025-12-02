package modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AsignaturaTest {

    private Asignatura asignatura;
    private final int CUPOS_MAXIMOS = 30;
    private final int CUPOS_INICIALES = 10;

    @BeforeEach
    void setUp() {
        // Inicializamos una asignatura fresca antes de cada prueba
        // Codigo: MAT-101, Nombre: Matemáticas, Sección: 1, Max: 30, Disponibles: 10
        asignatura = new Asignatura("MAT-101", "Matemáticas", "1", CUPOS_MAXIMOS, CUPOS_INICIALES);
    }

    @Test
    @DisplayName("El constructor debe inicializar correctamente todos los atributos")
    void testConstructor() {
        assertAll("Verificando propiedades iniciales",
                () -> assertEquals("MAT-101", asignatura.getCodigo()),
                () -> assertEquals("Matemáticas", asignatura.getNombre()),
                () -> assertEquals("1", asignatura.getSeccion()),
                () -> assertEquals(CUPOS_INICIALES, asignatura.getCuposDisponibles())
        );
    }

    @Test
    @DisplayName("decrementarCupos debe restar 1 si hay cupos disponibles")
    void testDecrementarCuposExitoso() {
        // Ejecución
        asignatura.decrementarCupos();

        // Verificación: Debería bajar de 10 a 9
        assertEquals(9, asignatura.getCuposDisponibles());
    }

    @Test
    @DisplayName("decrementarCupos NO debe bajar de 0 (Límite Inferior)")
    void testDecrementarCuposLimiteCero() {
        // Preparación: Forzamos a que queden 0 cupos
        asignatura.setCuposDisponibles(0);

        // Ejecución: Intentamos restar
        asignatura.decrementarCupos();

        // Verificación: Debe mantenerse en 0, no bajar a -1
        assertEquals(0, asignatura.getCuposDisponibles(), "Los cupos no deben ser negativos");
    }

    @Test
    @DisplayName("incrementarCupos debe sumar 1 si no ha llegado al máximo")
    void testIncrementarCuposExitoso() {
        // Preparación: Ponemos 29 cupos (el máximo es 30)
        asignatura.setCuposDisponibles(29);

        // Ejecución
        asignatura.incrementarCupos();

        // Verificación: Debe subir a 30
        assertEquals(30, asignatura.getCuposDisponibles());
    }

    @Test
    @DisplayName("incrementarCupos NO debe superar el máximo permitido (Límite Superior)")
    void testIncrementarCuposLimiteMaximo() {
        // Preparación: Llenamos los cupos al máximo (30)
        asignatura.setCuposDisponibles(CUPOS_MAXIMOS);

        // Ejecución: Intentamos sumar uno más (simulando un error de desinscripción extra)
        asignatura.incrementarCupos();

        // Verificación: Debe mantenerse en 30
        assertEquals(CUPOS_MAXIMOS, asignatura.getCuposDisponibles(), "No se deben superar los cupos máximos definidos");
    }
}