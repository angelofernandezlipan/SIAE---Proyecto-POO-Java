package modelo;

import util.UtilidadesArchivo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GestionAdministrativa {
    private final SistemaInscripcion sistema;

    public GestionAdministrativa(SistemaInscripcion sistema) {
        this.sistema = sistema;
    }

    private String generarContrasenaSimple() {
        return "pass" + (new Random().nextInt(900) + 100);
    }

    public List<String> cargarListaCursos(String filePath) {
        List<String> errores = new ArrayList<>();

        String lowerCasePath = filePath.toLowerCase();
        if (!lowerCasePath.endsWith(".csv") && !lowerCasePath.endsWith(".txt")) {
            errores.add("Error: El archivo debe ser un CSV o un TXT.");
            return errores;
        }

        List<String> lineas = UtilidadesArchivo.leerArchivo(filePath);

        if (lineas == null) {
            errores.add("Error: No se pudo leer el archivo. Verifique la ruta o los permisos de acceso.");
            return errores;
        }

        if (lineas.isEmpty()) {
            errores.add("Error: El archivo está vacío.");
            return errores;
        }

        List<String> listaExportar = new ArrayList<>();
        listaExportar.add("RUT\tNOMBRE\tCURSO\tCONTRASEÑA");

        for (String linea : lineas) {
            String[] partes = linea.split(",");
            if (partes.length >= 3) {
                String rut = partes[0].trim();
                String nombre = partes[1].trim();
                String curso = partes[2].trim().toUpperCase();

                if (curso.matches("^[34][AB]$")) {
                    String password = generarContrasenaSimple();

                    if (sistema.validarCredenciales(rut, "N/A") != null) {
                        errores.add("Advertencia: Estudiante con RUT " + rut + " ya existe y fue omitido.");
                        continue;
                    }

                    Estudiante nuevoEst = new Estudiante(rut, nombre, password, curso);
                    sistema.agregarEstudiante(nuevoEst);
                    listaExportar.add(String.format("%s\t%s\t%s\t%s", rut, nombre, curso, password));
                } else {
                    errores.add("Error: Formato de curso inválido en línea: " + linea);
                }
            } else {
                errores.add("Error: Línea incompleta (se esperaba RUT,Nombre,Curso): " + linea);
            }
        }

        if (errores.stream().noneMatch(s -> s.startsWith("Error:"))) {
            sistema.guardarDatos();
            UtilidadesArchivo.exportarArchivo("reporte_credenciales_admin.txt", listaExportar);
            errores.add(0, "Carga masiva exitosa. Estudiantes registrados y credenciales exportadas.");
        }
        return errores;
    }

    public List<String> exportarReportes(int tipoReporte) {
        List<String> reporte = new ArrayList<>();
        String filename = "";

        switch (tipoReporte) {
            case 1:
                filename = "reporte_cupos_asignaturas.txt";
                reporte.add("--- REPORTE DE CUPOS DISPONIBLES ---");
                reporte.add("CÓDIGO\tNOMBRE\tSECCIÓN\tCUPOS DISPONIBLES\tCUPOS MÁXIMOS");
                sistema.getAsignaturas().forEach(asig ->
                        reporte.add(String.format("%s\t%s\t%s\t%d\t%d",
                                asig.getCodigo(), asig.getNombre(), asig.getSeccion(),
                                asig.getCuposDisponibles(), asig.getCuposMaximos()))
                );
                break;

            case 2:
                filename = "reporte_inscripciones_estudiantes.txt";
                reporte.add("--- REPORTE DE INSCRIPCIONES POR ESTUDIANTE ---");
                reporte.add("RUT\tNOMBRE\tCURSO\tASIGNATURAS INSCRITAS");
                sistema.getEstudiantes().forEach(est -> {
                    String asignaturas = est.getAsignaturasInscritas().isEmpty() ?
                            "Ninguna" : String.join(", ", est.getAsignaturasInscritas());
                    reporte.add(String.format("%s\t%s\t%s\t%s",
                            est.getRut(), est.getNombre(), est.getCurso(), asignaturas));
                });
                break;
            default:
                reporte.add("Tipo de reporte inválido.");
                return reporte;
        }

        if (!reporte.isEmpty()) {
            UtilidadesArchivo.exportarArchivo(filename, reporte);
        }
        return reporte;
    }
}