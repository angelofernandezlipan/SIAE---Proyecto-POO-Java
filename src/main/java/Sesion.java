public class Sesion {
    private Estudiante estudianteActual;

    public Sesion() {
        this.estudianteActual = null;
    }

    // Inicia la sesión de un estudiante
    public void iniciarSesion(Estudiante estudiante) {
        this.estudianteActual = estudiante;
    }

    // Cierra la sesión
    public void cerrarSesion() {
        this.estudianteActual = null;
    }

    // Obtiene el estudiante de la sesión
    public Estudiante getEstudianteActual() {
        return estudianteActual;
    }

    // Verifica si hay una sesión activa
    public boolean haySesionActiva() {
        return estudianteActual != null;
    }
}