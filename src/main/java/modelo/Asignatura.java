package modelo;

public class Asignatura {
    private String codigo;
    private String nombre;
    private String seccion;
    private int cuposMaximos;
    private int cuposDisponibles; // Se mantiene, pero su gestión debe ser atómica.

    // Constructor de la clase
    public Asignatura(String codigo, String nombre, String seccion, int cuposMaximos, int cuposDisponibles) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.seccion = seccion;
        this.cuposMaximos = cuposMaximos;
        this.cuposDisponibles = cuposDisponibles;
    }

    // Getters y Setters (incluye un getter para la lista de estudiantes inscritos, que ahora es inútil)
    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getSeccion() {
        return seccion;
    }

    public int getCuposDisponibles() {
        return cuposDisponibles;
    }

    // Nuevo: Método para decrementar cupos de forma controlada
    public void decrementarCupos() {
        if (this.cuposDisponibles > 0) {
            this.cuposDisponibles--;
        }
    }

    // Nuevo: Método para incrementar cupos (si fuera necesario anular una inscripción)
    public void incrementarCupos() {
        if (this.cuposDisponibles < this.cuposMaximos) {
            this.cuposDisponibles++;
        }
    }

    public int getCuposMaximos() {
        return cuposMaximos;
    }

    @Override
    public String toString() {
        return String.format("Código: %s, Nombre: %s, Sección: %s, Cupos Disponibles: %d/%d",
                codigo, nombre, seccion, cuposDisponibles, cuposMaximos);
    }
}