import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class GraphTest {
    public static void main(String[] args) {
        String url = "https://progwebtec.github.io/classes/ss2025/info2/labs/assignment-09/graph1.txt";
        WeightedGraph graph = readGraphFromURL(url);

        for (String node : graph.getNodes()) {
            System.out.println("Knoten: " + node + " -> " + graph.getNeighbors(node));
        }
    }

    public static WeightedGraph readGraphFromURL(String urlString) {
        WeightedGraph graph = new WeightedGraph();

        try {
            URL url = new URL(urlString);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
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

            reader.close();
        } catch (Exception e) {
            System.err.println("Fehler beim Laden der URL: " + e.getMessage());
        }

        return graph;
    }
}
