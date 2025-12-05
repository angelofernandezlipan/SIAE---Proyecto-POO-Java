package controlador;

import modelo.Estudiante;
import modelo.SistemaInscripcion;
import vista.VentanaInscripcion;

import java.util.List;
import java.util.stream.Collectors;

public class ControladorInscripcion {

    private final SistemaInscripcion sistema;
    private final Estudiante estudiante;
    private VentanaInscripcion vista;

    public ControladorInscripcion(SistemaInscripcion sistema, Estudiante estudiante) {
        this.sistema = sistema;
        this.estudiante = estudiante;
    }

    public void setVista(VentanaInscripcion vista) {
        this.vista = vista;
    }

    public String getNombreEstudiante() {
        return estudiante.getNombre();
    }

    // --- Lógica de Negocio ---

    public List<String> obtenerAsignaturasDisponiblesInfo() {
        return sistema.obtenerAsignaturasDisponibles().stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    public List<String> obtenerInscripcionesEstudiante() {
        return estudiante.getAsignaturasInscritas().stream()
                .map(sistema::buscarAsignaturaPorCodigo)
                .filter(java.util.Objects::nonNull)
                .map(asig -> String.format("Código: %s, Nombre: %s, Sección: %s",
                        asig.getCodigo(), asig.getNombre(), asig.getSeccion()))
                .collect(Collectors.toList());
    }

    public void intentarInscribir(String codigoAsignatura) {
        String resultado = sistema.inscribirAsignatura(estudiante, codigoAsignatura);
        vista.mostrarResultado(resultado);
        vista.actualizarListas();
    }

    public void intentarDesinscribir(String codigoAsignatura) {
        String resultado = sistema.desinscribirAsignatura(estudiante, codigoAsignatura);
        vista.mostrarResultado(resultado);
        vista.actualizarListas();
    }

    /**
     * Gestiona el cierre de la ventana de inscripción.
     * Verifica que el estudiante tenga al menos 3 asignaturas antes de cerrar.
     */
    public void manejarSalida() {
        int cantidad = estudiante.getAsignaturasInscritas().size();

        if (cantidad < 3) {
            String mensaje = "⚠️ AVISO ACADÉMICO:\n" +
                    "Solo tienes " + cantidad + " asignatura(s) inscrita(s).\n" +
                    "Se recomienda inscribir al menos 3.";

            // Pregunta al usuario si quiere salir igual
            boolean salirIgual = vista.mostrarAdvertenciaSalida(mensaje);

            if (salirIgual) {
                vista.dispose(); // Cierra solo si confirma
            }
        } else {
            vista.dispose(); // Cierra normal si cumple requisito
        }
    }
}