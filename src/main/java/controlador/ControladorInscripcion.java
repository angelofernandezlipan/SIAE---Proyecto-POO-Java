package controlador;

import modelo.Asignatura;
import modelo.Estudiante;
import modelo.SistemaInscripcion;
import vista.VentanaInscripcion;

import java.util.List;
import java.util.stream.Collectors;


public class ControladorInscripcion {

    // --- Atributos ---
    private final SistemaInscripcion sistema; // El Modelo
    private final Estudiante estudiante;     // El usuario
    private VentanaInscripcion vista;        // La Vista

    public ControladorInscripcion(SistemaInscripcion sistema, Estudiante estudiante) {
        this.sistema = sistema;
        this.estudiante = estudiante;
    }

    public void setVista(VentanaInscripcion vista) {
        this.vista = vista;
        // Inmediatamente después de conectar la vista, le pedimos que cargue los datos
        cargarDatosTablas();
    }

    public void cargarDatosTablas() {
        if (vista == null) return;

        // 1. Obtener datos para la tabla de "Disponibles"
        List<Asignatura> disponibles = sistema.getAsignaturas().stream()
                .filter(asig -> asig.getCuposDisponibles() > 0)
                .collect(Collectors.toList());
        vista.actualizarTablaDisponibles(disponibles);

        // 2. Obtener datos para la tabla de "Inscritas"
        List<Asignatura> inscritas = estudiante.getAsignaturasInscritas().stream()
                .map(sistema::buscarAsignaturaPorCodigo) // Busca el objeto Asignatura por su código
                .filter(asig -> asig != null) // Filtra por si acaso hubo un error
                .collect(Collectors.toList());
        vista.actualizarTablaInscritas(inscritas);
    }


    public void manejarInscripcion(String codigoAsignatura) {
        if (codigoAsignatura == null) {
            vista.mostrarMensajeError("Por favor, seleccione una asignatura de la lista DISPONIBLES.");
            return;
        }

        // El controlador le pide al modelo que haga la lógica
        String resultado = sistema.inscribirAsignatura(this.estudiante, codigoAsignatura);

        // El controlador le dice a la vista cómo reaccionar
        if (resultado.startsWith("¡Inscripción exitosa")) {
            vista.mostrarMensajeExito(resultado);
            cargarDatosTablas(); // Recarga ambas tablas
        } else {
            vista.mostrarMensajeError(resultado);
        }
    }


    public void manejarDesinscripcion(String codigoAsignatura, String nombreAsignatura) {
        if (codigoAsignatura == null) {
            vista.mostrarMensajeError("Por favor, seleccione una asignatura de la lista de SUS ASIGNATURAS.");
            return;
        }

        // 1. El controlador pide confirmación a la vista
        boolean confirmado = vista.mostrarConfirmacion(
                "¿Estás seguro de que deseas desinscribir '" + nombreAsignatura + "'?"
        );

        if (!confirmado) {
            return; // El usuario presionó "No"
        }

        // 2. El controlador le pide al modelo que haga la lógica
        String resultado = sistema.desinscribirAsignatura(this.estudiante, codigoAsignatura);

        // 3. El controlador le dice a la vista cómo reaccionar
        if (resultado.startsWith("¡Desinscripción exitosa")) {
            vista.mostrarMensajeExito(resultado);
            cargarDatosTablas(); // Recarga ambas tablas
        } else {
            vista.mostrarMensajeError(resultado);
        }
    }
}
