package modelo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EstudianteTest {

    @Test
    void testLimpiezaDeEspacios() {
        // Verificar que el constructor elimine los espacios en blanco (trim)

        // 1. Datos con espacios "sucios" al principio y al final
        String rutSucio = "  12.345.678-9  ";
        String nombreSucio = "  Juan Perez  ";
        String passSucio = "  pass123  ";
        String curso = "4A";

        // 2. Crear el estudiante
        Estudiante estudiante = new Estudiante(rutSucio, nombreSucio, passSucio, curso);

        // 3. Verificaciones
        Assertions.assertEquals("12.345.678-9", estudiante.getRut(), "El RUT debe guardarse sin espacios");
        Assertions.assertEquals("Juan Perez", estudiante.getNombre(), "El nombre debe guardarse sin espacios");
        Assertions.assertEquals("pass123", estudiante.getPassword(), "La contraseña debe guardarse sin espacios");
    }

    @Test
    void testProteccionContraNulos() {
        // Verificar que si llegan datos nulos, se conviertan a cadenas vacías ""
        // para evitar que el programa se caiga con NullPointerException después

        // 1. Crear estudiante con ttodo en null
        Estudiante estudiante = new Estudiante(null, null, null, null);

        // 2. Verificaciones
        Assertions.assertNotNull(estudiante.getRut(), "El RUT no debe ser null");
        Assertions.assertEquals("", estudiante.getRut(), "El RUT null debe convertirse en string vacio");

        Assertions.assertNotNull(estudiante.getNombre(), "El Nombre no debe ser null");
        Assertions.assertEquals("", estudiante.getNombre(), "El Nombre null debe convertirse en string vacío");

        // Verificamos también la lista de asignaturas del constructor principal
        Assertions.assertNotNull(estudiante.getAsignaturasInscritas(), "La lista de asignaturas no debe ser null");
        Assertions.assertTrue(estudiante.getAsignaturasInscritas().isEmpty(), "La lista debe iniciarse vacía");
    }
}
