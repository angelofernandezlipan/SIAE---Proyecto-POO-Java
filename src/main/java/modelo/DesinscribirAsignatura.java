package modelo;

import modelo.Asignatura;
import modelo.Estudiante;
import modelo.SistemaInscripcion;

public class DesinscribirAsignatura {

    public static String ejecutar(SistemaInscripcion sistema, Estudiante estudiante, String codigoAsignatura) {

        // 1. Buscar la asignatura en el sistema
        Asignatura asignatura = sistema.buscarAsignaturaPorCodigo(codigoAsignatura);

        if (asignatura == null) {
            return "Error: La asignatura no se encuentra en el sistema.";
        }

        // 2. Validar si el estudiante está realmente inscrito
        if (!estudiante.getAsignaturasInscritas().contains(codigoAsignatura)) {
            return "Error: No estás inscrito en esta asignatura.";
        }

        // 3. Realizar la desinscripción (removiendo de la lista del estudiante)
        boolean removido = estudiante.getAsignaturasInscritas().remove(codigoAsignatura);

        if (!removido) {
            // Esto es un fallback, no debería pasar si el 'contains' fue true
            return "Error: No se pudo remover la asignatura de tu lista.";
        }

        // 4. Devolver el cupo a la asignatura
        asignatura.setCuposDisponibles(asignatura.getCuposDisponibles() + 1);

        // 5. Guardar los cambios en el JSON
        sistema.guardarDatos();

        return "¡Desinscripción exitosa de " + asignatura.getNombre() + "!";
    }
}
