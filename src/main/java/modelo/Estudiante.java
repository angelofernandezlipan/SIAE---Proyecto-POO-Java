package modelo;

import java.util.ArrayList;
import java.util.List;

public class Estudiante {
    // Atributos de la clase modelo.Estudiante
    private String rut; // Guardado en formato original (e.g., 12.345.678-9)
    private String nombre;
    private String password;
    private String curso;
    private List<String> asignaturasInscritas;

    // Constructor principal
    public Estudiante(String rut, String nombre, String password, String curso, List<String> asignaturasInscritas) {
        // Mantenemos el rut y password tal como vienen para evitar doble sanitización
        // La sanitización de espacios la hicimos, eso es correcto.
        this.rut = rut != null ? rut.trim() : "";
        this.nombre = nombre != null ? nombre.trim() : "";
        this.password = password != null ? password.trim() : "";
        this.curso = curso != null ? curso.trim() : "";
        this.asignaturasInscritas = asignaturasInscritas != null ? asignaturasInscritas : new ArrayList<>();
    }

    // Constructor auxiliar para la carga masiva
    public Estudiante(String rut, String nombre, String password, String curso) {
        this(rut, nombre, password, curso, null);
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