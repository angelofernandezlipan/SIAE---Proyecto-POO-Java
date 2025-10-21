import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UtilidadesArchivo {

    // Lee un archivo TXT/CSV y retorna una lista de líneas
    public static List<String> leerArchivo(String filePath) {
        List<String> lineas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    lineas.add(linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            return null;
        }
        return lineas;
    }

    // Exporta una lista de strings a un archivo TXT
    public static void exportarArchivo(String filePath, List<String> contenido) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (String linea : contenido) {
                writer.write(linea + System.lineSeparator());
            }
            System.out.println("Reporte exportado exitosamente a: " + filePath);
        } catch (IOException e) {
            System.err.println("Error al exportar el archivo: " + e.getMessage());
        }
    }
}
