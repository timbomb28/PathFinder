import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class KnotenEntfernung {

    public static Set<String> getVerticesAtDistanceN(WeightedGraph graph, String start, int n) {
        Set<String> result = new HashSet<>();
        Map<String, Integer> distance = new HashMap<>();
        Queue<String> queue = new LinkedList<>();

        distance.put(start, 0);
        queue.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            int dist = distance.get(current);

            if (dist == n) {
                result.add(current);
                // keine weiteren Nachbarn hinzufügen, da nur genau n Schritte
                continue;
            } else if (dist > n) {
                continue;
            }

            for (WeightedGraph.Edge edge : graph.getNeighbors(current)) {
                String neighbor = edge.to;
                if (!distance.containsKey(neighbor)) {
                    distance.put(neighbor, dist + 1);
                    queue.add(neighbor);
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        try {
            // Graph laden
            URL graphUrl = new URL("https://progwebtec.github.io/classes/ss2025/info2/labs/assignment-09/bvg.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(graphUrl.openStream()));
            WeightedGraph graph = new WeightedGraph();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                String from = parts[0];
                for (int i = 1; i < parts.length; i++) {
                    String[] edgeParts = parts[i].split(",");
                    String to = edgeParts[0];
                    int weight = Integer.parseInt(edgeParts[1]);
                    graph.addEdge(from, to, weight);
                }
            }
            reader.close();

            // Stationen laden
            StationMapper mapper = new StationMapper("https://progwebtec.github.io/classes/ss2025/info2/labs/assignment-09/stations.txt");

            // Startknoten Schöneweide (ID)
            String start = "060192001006";

            // Ausgabe Knoten in Entfernung 1 bis 3
            for (int n = 1; n <= 30; n++) {
                Set<String> nodesAtDistance = getVerticesAtDistanceN(graph, start, n);
                System.out.println("Knoten in Entfernung " + n + ":");
                for (String id : nodesAtDistance) {
                    System.out.println("  " + mapper.getName(id) + " (" + id + ")");
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
