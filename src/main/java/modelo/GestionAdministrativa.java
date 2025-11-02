package modelo;

import util.UtilidadesArchivo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GestionAdministrativa {
    private final SistemaInscripcion sistema; // Inyección de dependencia

    public GestionAdministrativa(SistemaInscripcion sistema) {
        this.sistema = sistema;
    }

    // Genera una contraseña simple (ej: "pass123")
    private String generarContrasenaSimple() {
        // Genera un número aleatorio de 3 dígitos (entre 100 y 999)
        return "pass" + (new Random().nextInt(900) + 100);
    }

    // Carga masiva de estudiantes desde un archivo TXT/CSV (Admin)
    public List<String> cargarListaCursos(String filePath) {
        List<String> errores = new ArrayList<>();
        List<String> lineas = UtilidadesArchivo.leerArchivo(filePath);
        List<String> listaExportar = new ArrayList<>();

        if (lineas == null || lineas.isEmpty()) {
            errores.add("Error: El archivo está vacío o no existe.");
            return errores;
        }

        listaExportar.add("RUT\tNOMBRE\tCURSO\tCONTRASEÑA");

        for (String linea : lineas) {
            String[] partes = linea.split(",");
            if (partes.length >= 3) {
                String rut = partes[0].trim();
                String nombre = partes[1].trim();
                String curso = partes[2].trim().toUpperCase();

                if (curso.matches("^[34][AB]$")) {
                    String password = generarContrasenaSimple();

                    // Uso de la validación del sistema (rut, "N/A" para solo verificar existencia)
                    if (sistema.validarCredenciales(rut, "N/A") != null) {
                        errores.add("Advertencia: Estudiante con RUT " + rut + " ya existe y fue omitido.");
                        continue;
                    }

                    Estudiante nuevoEst = new Estudiante(rut, nombre, password, curso);
                    sistema.agregarEstudiante(nuevoEst); // Añadir al modelo
                    listaExportar.add(String.format("%s\t%s\t%s\t%s", rut, nombre, curso, password));
                } else {
                    errores.add("Error: Formato de curso inválido en línea: " + linea);
                }
            } else {
                errores.add("Error: Línea incompleta (se esperaba RUT,Nombre,Curso): " + linea);
            }
        }

        // CLAVE: Persistir los nuevos estudiantes y exportar las credenciales
        if (errores.stream().noneMatch(s -> s.startsWith("Error:"))) { // Solo si no hay errores fatales
            sistema.guardarDatos(); // Persiste la lista completa de estudiantes actualizada
            UtilidadesArchivo.exportarArchivo("reporte_credenciales_admin.txt", listaExportar); // Exporta las credenciales
            errores.add(0, "Carga masiva exitosa. Estudiantes registrados y credenciales exportadas.");
        }
        return errores;
    }

    // Exporta reportes administrativos a archivos TXT
    public void exportarReportes(int tipoReporte) {
        List<String> reporte = new ArrayList<>();
        String filename = "";

        switch (tipoReporte) {
            case 1: // Reporte de Cupos Disponibles
                filename = "reporte_cupos_asignaturas.txt";
                reporte.add("CÓDIGO\tNOMBRE\tSECCIÓN\tCUPOS DISPONIBLES\tCUPOS MÁXIMOS");
                sistema.getAsignaturas().forEach(asig ->
                        reporte.add(String.format("%s\t%s\t%s\t%d\t%d",
                                asig.getCodigo(), asig.getNombre(), asig.getSeccion(),
                                asig.getCuposDisponibles(), asig.getCuposMaximos()))
                );
                break;

            case 2: // Reporte de Inscripciones por Estudiante y Curso
                filename = "reporte_inscripciones_estudiantes.txt";
                reporte.add("RUT\tNOMBRE\tCURSO\tASIGNATURAS INSCRITAS");
                sistema.getEstudiantes().forEach(est -> {
                    String asignaturas = est.getAsignaturasInscritas().isEmpty() ?
                            "Ninguna" : String.join(", ", est.getAsignaturasInscritas());
                    reporte.add(String.format("%s\t%s\t%s\t%s",
                            est.getRut(), est.getNombre(), est.getCurso(), asignaturas));
                });
                break;
            default:
                System.out.println("Tipo de reporte inválido.");
                return;
        }

        if (!reporte.isEmpty()) {
            UtilidadesArchivo.exportarArchivo(filename, reporte);
        }
    }
}