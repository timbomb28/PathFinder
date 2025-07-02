import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class TravelTimes {

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

            String startId = "060192001006"; // S Schöneweide

            printTravelTimesToAllStations(graph, mapper, startId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printTravelTimesToAllStations(WeightedGraph graph, StationMapper mapper, String startId) {
        Map<String, Integer> dist = new HashMap<>();
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));
        Set<String> visited = new HashSet<>();

        for (String node : graph.getAllVertices()) {
            dist.put(node, Integer.MAX_VALUE);
        }
        dist.put(startId, 0);
        pq.add(startId);

        while (!pq.isEmpty()) {
            String current = pq.poll();
            if (visited.contains(current)) continue;
            visited.add(current);

            for (WeightedGraph.Edge neighbor : graph.getNeighbors(current)) {
                String next = neighbor.to;
                int weight = neighbor.weight;
                int newDist = dist.get(current) + weight;

                if (newDist < dist.getOrDefault(next, Integer.MAX_VALUE)) {
                    dist.put(next, newDist);
                    pq.add(next);
                }
            }
        }

        System.out.println("Reisezeiten von " + mapper.getName(startId) + " (" + startId + "):");
        dist.entrySet().stream()
            .sorted(Map.Entry.comparingByValue())
            .forEach(entry -> {
                String stationId = entry.getKey();
                int travelTime = entry.getValue();
                String name = mapper.getName(stationId);
                if (travelTime == Integer.MAX_VALUE) {
                    System.out.println(name + " (" + stationId + "): nicht erreichbar");
                } else {
                    System.out.println(name + " (" + stationId + "): " + travelTime + " Sekunden");
                }
            });
    }
}
