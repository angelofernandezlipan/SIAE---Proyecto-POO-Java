package modelo;

import java.util.ArrayList;
import java.util.List;

public class Estudiante {
    // Atributos de la clase modelo.Estudiante
    private String rut;
    private String nombre;
    private String password;
    private String curso; // Nuevo atributo
    private List<String> asignaturasInscritas;

    // Constructor de la clase
    public Estudiante(String rut, String nombre, String password, String curso, List<String> asignaturasInscritas) {
        this.rut = rut;
        this.nombre = nombre;
        this.password = password;
        this.curso = curso; // Asignación del nuevo atributo
        this.asignaturasInscritas = asignaturasInscritas != null ? asignaturasInscritas : new ArrayList<>();
    }

    // Constructor auxiliar para la carga masiva (sin lista de inscritas al inicio)
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
        return "modelo.Estudiante{" +
                "rut='" + rut + '\'' +
                ", nombre='" + nombre + '\'' +
                ", curso='" + curso + '\'' +
                ", asignaturasInscritas=" + asignaturasInscritas +
                '}';
    }
}
