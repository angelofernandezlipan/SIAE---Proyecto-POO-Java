import java.util.ArrayList;
import java.util.List;

public class Estudiante {
    // Atributos de la clase Estudiante
    private String rut;
    private String nombre;
    private String password;
    private List<String> asignaturasInscritas;

    // Constructor de la clase
    public Estudiante(String rut, String nombre, String password, List<String> asignaturasInscritas) {
        this.rut = rut;
        this.nombre = nombre;
        this.password = password;
        this.asignaturasInscritas = asignaturasInscritas != null ? asignaturasInscritas : new ArrayList<>();
    }

    // Getters
    public String getRut() {
        return rut;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getAsignaturasInscritas() {
        return asignaturasInscritas;
    }

    // Méthod para añadir una asignatura a la lista de inscritas
    public void agregarAsignaturaInscrita(String codigoAsignatura) {
        this.asignaturasInscritas.add(codigoAsignatura);
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "rut='" + rut + '\'' +
                ", nombre='" + nombre + '\'' +
                ", asignaturasInscritas=" + asignaturasInscritas +
                '}';
    }
}