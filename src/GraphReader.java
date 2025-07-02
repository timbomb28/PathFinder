import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GraphReader {

    public static WeightedGraph readFromFile(String filename) {
        WeightedGraph graph = new WeightedGraph();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] tokens = line.split("\\t+");
                if (tokens.length < 1) continue;

                String from = tokens[0].trim();

                for (int i = 1; i < tokens.length; i++) {
                    String edgeStr = tokens[i].trim();
                    if (edgeStr.isEmpty()) continue;

                    String[] edgeParts = edgeStr.split(",");
                    if (edgeParts.length != 2) continue;

                    String to = edgeParts[0].trim();
                    int weight = Integer.parseInt(edgeParts[1].trim());

                    graph.addEdge(from, to, weight);
                }
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Lesen der Datei: " + e.getMessage());
        }

        return graph;
    }
}
