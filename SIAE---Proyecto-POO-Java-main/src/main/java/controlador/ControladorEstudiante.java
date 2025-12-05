package controlador;

import modelo.Asignatura;
import modelo.Estudiante;
import modelo.Sesion;
import modelo.SistemaInscripcion;
import vista.VentanaLogin;
import vista.VentanaPrincipal;

import javax.swing.JFrame; // <--- ¡NUEVO IMPORT AÑADIDO!

import java.util.List;
import java.util.stream.Collectors;

public class ControladorEstudiante {
    private final SistemaInscripcion sistema;
    private final Sesion sesion;
    private ControladorEstudiante contEstudianteGlobal;
    private ControladorAdmin contAdminGlobal;

    public ControladorEstudiante(SistemaInscripcion sistema, Sesion sesion) {
        this.sistema = sistema;
        this.sesion = sesion;
    }

    public SistemaInscripcion getSistema() {
        return sistema;
    }

    public void setControladoresGlobales(ControladorEstudiante contEstudianteGlobal, ControladorAdmin contAdminGlobal) {
        this.contEstudianteGlobal = contEstudianteGlobal;
        this.contAdminGlobal = contAdminGlobal;
    }

    public Estudiante login(String rut, String password) {
        Estudiante estudiante = sistema.validarCredenciales(rut, password);
        if (estudiante != null) {
            sesion.iniciarSesion(estudiante);
        }
        return estudiante;
    }

    public void manejarLogout(JFrame vistaActual) {
        sesion.cerrarSesion();

        if (vistaActual != null) {
            vistaActual.dispose();
        }

        ControladorLogin contLogin = new ControladorLogin(this.contEstudianteGlobal, this.contAdminGlobal);
        new VentanaLogin(contLogin);
    }

    public Estudiante getEstudianteActual() {
        return sesion.getEstudianteActual();
    }

    public boolean haySesionActiva() {
        return sesion.haySesionActiva();
    }

    public List<String> obtenerAsignaturasDisponiblesInfo() {
        return sistema.obtenerAsignaturasDisponibles().stream()
                .map(Asignatura::toString)
                .collect(Collectors.toList());
    }

    public String inscribirAsignatura(String codigoAsignatura) {
        Estudiante estudiante = sesion.getEstudianteActual();
        if (estudiante == null) {
            return "Error: No hay sesión activa.";
        }
        return sistema.inscribirAsignatura(estudiante, codigoAsignatura);
    }

    public List<String> obtenerInscripcionesEstudiante() {
        Estudiante estudiante = sesion.getEstudianteActual();
        if (estudiante == null) {
            return List.of("Error: No hay sesión activa.");
        }

        if (estudiante.getAsignaturasInscritas().isEmpty()) {
            return List.of("No tienes asignaturas inscritas.");
        } else {
            return estudiante.getAsignaturasInscritas().stream()
                    .map(sistema::buscarAsignaturaPorCodigo)
                    .filter(asig -> asig != null)
                    .map(asig -> "- " + asig.getNombre() + " (" + asig.getSeccion() + ")")
                    .collect(Collectors.toList());
        }
    }
}