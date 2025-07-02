import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

import java.util.stream.Collectors;

public class PathFinder {
    public static void main(String[] args) {
        try {
            URL url = new URL("https://progwebtec.github.io/classes/ss2025/info2/labs/assignment-09/bvg.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StationMapper mapper = new StationMapper("https://progwebtec.github.io/classes/ss2025/info2/labs/assignment-09/stations.txt");
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

            // Startpunkt: S Schöneweide
            String start = "060192001006";

            // Zielstationen
            String[] ziele = {
                "060068201511", // S+U Tempelhof
                "060066102852", // S Botanischer Garten
                "060053301433", // S Wannsee
                "060120003653"  // S Ostkreuz
            };

            for (String ziel : ziele) {
                PathResult result = dijkstra(graph, start, ziel);
                System.out.println(">> Von " + mapper.getName(start) + " (" + start + ") zu " + mapper.getName(ziel) + " (" + ziel + ")");
                System.out.println("Pfad: " + result.getPath().stream()
                    .map(id -> mapper.getName(id) + " (" + id + ")")
                    .collect(Collectors.joining(" → ")));
                System.out.println("Gesamtdauer: " + result.getTotalWeight() + " Sekunden");
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PathResult dijkstra(WeightedGraph graph, String start, String ziel) {
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));
        Set<String> visited = new HashSet<>();

        for (String node : graph.getAllVertices()) {
            dist.put(node, Integer.MAX_VALUE);
        }
        dist.put(start, 0);
        pq.add(start);

        while (!pq.isEmpty()) {
            String current = pq.poll();
            if (visited.contains(current)) continue;
            visited.add(current);

            if (current.equals(ziel)) break;

            for (WeightedGraph.Edge neighbor : graph.getNeighbors(current)) {
                String next = neighbor.to;
                int weight = neighbor.weight;
                int newDist = dist.get(current) + weight;

                if (newDist < dist.getOrDefault(next, Integer.MAX_VALUE)) {
                    dist.put(next, newDist);
                    prev.put(next, current);
                    pq.add(next);
                }
            }
        }

        List<String> path = new LinkedList<>();
        String at = ziel;
        while (at != null && prev.containsKey(at)) {
            path.add(0, at);
            at = prev.get(at);
        }
        if (start.equals(ziel)) path.add(start);
        else if (!path.isEmpty()) path.add(0, start);

        return new PathResult(path, dist.getOrDefault(ziel, Integer.MAX_VALUE));
    }
}
