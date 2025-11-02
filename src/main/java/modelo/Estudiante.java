package modelo;

import java.util.ArrayList;
import java.util.List;

public class Estudiante {
    private String rut;
    private String nombre;
    private String password; // Se mantiene para simplificar la carga masiva
    private String curso;
    private List<String> asignaturasInscritas;

    // Constructor principal
    public Estudiante(String rut, String nombre, String password, String curso, List<String> asignaturasInscritas) {
        this.rut = rut;
        this.nombre = nombre;
        this.password = password;
        this.curso = curso;
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
        return "Estudiante{" +
                "rut='" + rut + '\'' +
                ", nombre='" + nombre + '\'' +
                ", curso='" + curso + '\'' +
                ", asignaturasInscritas=" + asignaturasInscritas +
                '}';
    }
}