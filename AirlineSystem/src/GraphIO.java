import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class GraphIO {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void saveGraph(Graph graph, String filename) {
        try (Writer writer = new FileWriter(filename)) {
            Map<String, Object> data = new HashMap<>();
            data.put("airports", graph.getAirports());
            data.put("adjacencyList", graph.getAdjacencyList());
            gson.toJson(data, writer);
        } catch (IOException e) {
            System.out.println("Error saving graph: " + e.getMessage());
        }
    }

    public static void loadGraph(Graph graph, String filename) {
        try (Reader reader = new FileReader(filename)) {
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> data = gson.fromJson(reader, type);

            // Clear existing data
            graph.getAirports().clear();
            graph.getAdjacencyList().clear();

            // Load airports
            Map<String, Vertex> airportsData = gson.fromJson(gson.toJson(data.get("airports")), new TypeToken<Map<String, Vertex>>() {}.getType());
            graph.getAirports().putAll(airportsData);

            // Load adjacencyList
            Map<String, List<Edge>> adjacencyListData = gson.fromJson(gson.toJson(data.get("adjacencyList")), new TypeToken<Map<String, List<Edge>>>() {}.getType());
            graph.getAdjacencyList().putAll(adjacencyListData);

        } catch (IOException e) {
            System.out.println("Error loading graph: " + e.getMessage());
        }
    }
}
