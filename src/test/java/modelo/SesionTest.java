package modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SesionTest {

    private Sesion sesion;
    private Estudiante estudiantePrueba;

    @BeforeEach
    void setUp() {
        // Inicializa una nueva sesión vacía antes de cada prueba.
        sesion = new Sesion();

        // Inicializa un objeto Estudiante para usar en las pruebas.
        estudiantePrueba = new Estudiante("11111111-1", "Usuario Test", "pass123", "4toA", new ArrayList<>());
    }

    // ===============================================
    // PRUEBAS DE ESTADO INICIAL
    // ===============================================

    @Test
    void test1_EstadoInicial_NoHaySesion() {
        // Verifica que haySesionActiva es falso al inicio.
        assertFalse(sesion.haySesionActiva());

        // Verifica que getEstudianteActual retorna nulo al inicio.
        assertNull(sesion.getEstudianteActual());
    }

    // ===============================================
    // PRUEBAS DE INICIO DE SESIÓN
    // ===============================================

    @Test
    void test2_IniciarSesion_Exitoso() {
        // Inicia la sesión con el estudiante de prueba.
        sesion.iniciarSesion(estudiantePrueba);

        // Verifica que haySesionActiva es verdadero.
        assertTrue(sesion.haySesionActiva());

        // Verifica que el estudiante actual es el estudiante de prueba.
        assertEquals(estudiantePrueba, sesion.getEstudianteActual());
    }

    @Test
    void test3_IniciarSesion_ConEstudianteNulo() {
        // Intenta iniciar sesión con un objeto nulo.
        sesion.iniciarSesion(null);

        // Verifica que haySesionActiva es falso.
        assertFalse(sesion.haySesionActiva());

        // Verifica que getEstudianteActual es nulo.
        assertNull(sesion.getEstudianteActual());
    }

    // ===============================================
    // PRUEBAS DE CIERRE DE SESIÓN
    // ===============================================

    @Test
    void test4_CerrarSesion_FuncionalidadCorrecta() {
        // Inicia la sesión.
        sesion.iniciarSesion(estudiantePrueba);

        // Cierra la sesión.
        sesion.cerrarSesion();

        // Verifica que haySesionActiva es falso.
        assertFalse(sesion.haySesionActiva());

        // Verifica que el estudiante actual es nulo.
        assertNull(sesion.getEstudianteActual());
    }
}