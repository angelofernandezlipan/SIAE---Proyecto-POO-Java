package modelo;

import java.util.ArrayList;
import java.util.List;

public class Estudiante {
    // Atributos de la clase modelo.Estudiante
    private String rut;
    private String nombre;
    private String password; // Debe ser serializado
    private String curso;
    private List<String> asignaturasInscritas;

    // Constructor principal
    public Estudiante(String rut, String nombre, String password, String curso, List<String> asignaturasInscritas) {
        this.rut = rut;
        this.nombre = nombre;
        this.password = password; // Contraseña generada por Admin
        this.curso = curso;
        this.asignaturasInscritas = asignaturasInscritas != null ? asignaturasInscritas : new ArrayList<>();
    }

    // Constructor auxiliar para la carga masiva
    public Estudiante(String rut, String nombre, String password, String curso) {
        this(rut, nombre, password, curso, null);
    }

    // Getters y Setters
    public String getRut() {
        return rut;
    }

    public String getNombre() {
        return nombre;
    }

    /**
     * MÉTODO CLAVE: Permite a SistemaInscripcion y GSON acceder al password.
     */
    public String getPassword() {
        return password;
    }

    public String getCurso() {
        return curso;
    }

    public List<String> getAsignaturasInscritas() {
        return asignaturasInscritas;
    }

    // Método para añadir una asignatura a la lista de inscritas
    public void agregarAsignaturaInscrita(String codigoAsignatura) {
        this.asignaturasInscritas.add(codigoAsignatura);
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "rut='" + rut + '\'' +
                ", nombre='" + nombre + '\'' +
                ", curso='" + curso + '\'' +
                ", asignaturasInscritas=" + asignaturasInscritas +
                '}';
    }
}