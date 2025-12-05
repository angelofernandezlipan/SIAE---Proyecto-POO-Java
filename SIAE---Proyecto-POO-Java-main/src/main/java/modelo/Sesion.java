package modelo;

public class Sesion {
    private Estudiante estudianteActual;

    public Sesion() {
        this.estudianteActual = null;
    }

    public void iniciarSesion(Estudiante estudiante) {
        this.estudianteActual = estudiante;
    }

    public void cerrarSesion() {
        this.estudianteActual = null;
    }

    public Estudiante getEstudianteActual() {
        return estudianteActual;
    }

    public boolean haySesionActiva() {
        return estudianteActual != null;
    }
}