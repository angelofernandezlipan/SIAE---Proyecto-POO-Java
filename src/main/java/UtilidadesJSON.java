import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UtilidadesJSON {

    // Méthod para leer datos de un archivo JSON y devolver un objeto JsonObject
    public static JsonObject leerJSON(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException e) {
            System.err.println("Error al leer el archivo JSON: " + e.getMessage());
            return null;
        }
    }

    // Méthod para escribir un JsonObject en un archivo JSON
    public static void escribirJSON(String filePath, JsonObject data) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(data, writer);
            System.out.println("Datos guardados en " + filePath);
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo JSON: " + e.getMessage());
        }
    }

    // Méthod genérico para convertir un JsonArray a una lista de objetos
    public static <T> List<T> jsonArrayToList(JsonArray jsonArray, Class<T> clazz) {
        Gson gson = new Gson();
        Type listType = TypeToken.getParameterized(ArrayList.class, clazz).getType();
        return gson.fromJson(jsonArray, listType);
    }

    // Méthod genérico para convertir una lista de objetos a un JsonArray
    public static <T> JsonArray listToJsonArray(List<T> list) {
        Gson gson = new Gson();
        return (JsonArray) gson.toJsonTree(list);
    }
}
