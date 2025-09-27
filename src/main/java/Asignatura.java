import java.util.ArrayList;
import java.util.List;

public class Asignatura {
    // Atributos de la clase Asignatura
    private String codigo;
    private String nombre;
    private String seccion;
    private int cuposMaximos;
    private int cuposDisponibles;
    private List<String> estudiantesInscritos;

    // Constructor de la clase
    public Asignatura(String codigo, String nombre, String seccion, int cuposMaximos, int cuposDisponibles) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.seccion = seccion;
        this.cuposMaximos = cuposMaximos;
        this.cuposDisponibles = cuposDisponibles;
        this.estudiantesInscritos = new ArrayList<>();
    }

    // Getters y Setters
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

    public void setCuposDisponibles(int cuposDisponibles) {
        this.cuposDisponibles = cuposDisponibles;
    }

    public int getCuposMaximos() {
        return cuposMaximos;
    }

    public List<String> getEstudiantesInscritos() {
        return estudiantesInscritos;
    }

    // Méthod para inscribir a un estudiante
    public void inscribirEstudiante(String rutEstudiante) {
        if (this.cuposDisponibles > 0) {
            this.estudiantesInscritos.add(rutEstudiante);
            this.cuposDisponibles--;
        }
    }

    @Override
    public String toString() {
        return String.format("Código: %s, Nombre: %s, Sección: %s, Cupos Disponibles: %d/%d",
                codigo, nombre, seccion, cuposDisponibles, cuposMaximos);
    }
}
