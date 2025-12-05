package modelo;

import modelo.Asignatura;
import modelo.Estudiante;
import modelo.SistemaInscripcion;

public class DesinscribirAsignatura {

    public static String ejecutar(SistemaInscripcion sistema, Estudiante estudiante, String codigoAsignatura) {
        Asignatura asignatura = sistema.buscarAsignaturaPorCodigo(codigoAsignatura);

        if (asignatura == null) {
            return "Error: La asignatura no se encuentra en el sistema.";
        }

        if (!estudiante.getAsignaturasInscritas().contains(codigoAsignatura)) {
            return "Error: No estás inscrito en esta asignatura.";
        }

        boolean removido = estudiante.getAsignaturasInscritas().remove(codigoAsignatura);

        if (!removido) {
            return "Error: No se pudo remover la asignatura de tu lista.";
        }

        asignatura.setCuposDisponibles(asignatura.getCuposDisponibles() + 1);
        sistema.guardarDatos();

        return "¡Desinscripción exitosa de " + asignatura.getNombre() + "!";
    }
}
