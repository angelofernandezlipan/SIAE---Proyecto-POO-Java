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
    public List<String> obtenerAsignaturasDisponiblesInfo() {
        return sistema.obtenerAsignaturasDisponibles().stream()
                .map(asig -> asig.toString())
                .collect(Collectors.toList());
    }

    public List<String> obtenerInscripcionesEstudiante() {
        return estudiante.getAsignaturasInscritas().stream()
                .map(sistema::buscarAsignaturaPorCodigo)
                .filter(asig -> asig != null)
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
}