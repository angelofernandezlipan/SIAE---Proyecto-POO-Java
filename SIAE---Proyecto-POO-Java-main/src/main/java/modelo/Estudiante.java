package modelo;

import java.util.ArrayList;
import java.util.List;

public class Estudiante {
    private String rut;
    private String nombre;
    private String password;
    private String curso;
    private List<String> asignaturasInscritas;

    public Estudiante(String rut, String nombre, String password, String curso, List<String> asignaturasInscritas) {
        this.rut = rut != null ? rut.trim() : "";
        this.nombre = nombre != null ? nombre.trim() : "";
        this.password = password != null ? password.trim() : "";
        this.curso = curso != null ? curso.trim() : "";
        this.asignaturasInscritas = asignaturasInscritas != null ? asignaturasInscritas : new ArrayList<>();
    }

    public Estudiante(String rut, String nombre, String password, String curso) {
        this(rut, nombre, password, curso, null);
    }

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