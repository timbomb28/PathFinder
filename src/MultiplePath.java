import java.util.*;
import java.util.stream.Collectors;

public class MultiplePath {

    private WeightedGraph graph;
    private StationMapper mapper;

    public MultiplePath(WeightedGraph graph, StationMapper mapper) {
        this.graph = graph;
        this.mapper = mapper;
    }


    public List<List<String>> findAllShortestPaths(String start, String ziel) {
        Map<String, Integer> dist = new HashMap<>();
        Map<String, List<String>> predecessors = new HashMap<>();

        for (String node : graph.getAllVertices()) {
            dist.put(node, Integer.MAX_VALUE);
            predecessors.put(node, new ArrayList<>());
        }
        dist.put(start, 0);

        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));
        pq.add(start);

        while (!pq.isEmpty()) {
            String current = pq.poll();

            if (current.equals(ziel)) break;

            for (WeightedGraph.Edge edge : graph.getNeighbors(current)) {
                String neighbor = edge.to;
                int weight = edge.weight;
                int newDist = dist.get(current) + weight;

                if (newDist < dist.get(neighbor)) {
                    dist.put(neighbor, newDist);
                    predecessors.put(neighbor, new ArrayList<>(Arrays.asList(current)));
                    pq.add(neighbor);
                } else if (newDist == dist.get(neighbor)) {

                    predecessors.get(neighbor).add(current);
                }
            }
        }

        List<List<String>> paths = new ArrayList<>();
        LinkedList<String> path = new LinkedList<>();
        Set<String> seenPaths = new HashSet<>();
        buildPaths(ziel, start, predecessors, path, paths, seenPaths);
        return paths;
    }

    private void buildPaths(String current, String start, Map<String, List<String>> predecessors,
                            LinkedList<String> path, List<List<String>> paths, Set<String> seenPaths) {
        path.addFirst(current);
        if (current.equals(start)) {
            String pathSignature = String.join("->", path);
            if (!seenPaths.contains(pathSignature)) {
                paths.add(new ArrayList<>(path));
                seenPaths.add(pathSignature);
            }
        } else {
            for (String pred : predecessors.get(current)) {
                buildPaths(pred, start, predecessors, path, paths, seenPaths);
            }
        }
        path.removeFirst();
    }

    public void printAllShortestPaths(String start, String ziel) {
        List<List<String>> paths = findAllShortestPaths(start, ziel);

        if (paths.isEmpty()) {
            System.out.println("Kein Pfad gefunden zwischen " + mapper.getName(start) + " und " + mapper.getName(ziel));
            return;
        }

        System.out.println("Alle minimalen Pfade von " + mapper.getName(start) + " zu " + mapper.getName(ziel) + ":");
        for (List<String> path : paths) {
            String pathStr = path.stream()
                .map(id -> mapper.getName(id) + " (" + id + ")")
                .collect(Collectors.joining(" → "));
            System.out.println(pathStr);
        }
    }


    public void printStationsWithMultipleShortestPaths(String start) {
        Set<String> allVertices = graph.getAllVertices();

        boolean foundAny = false;
        for (String ziel : allVertices) {
            if (ziel.equals(start)) continue;

            List<List<String>> paths = findAllShortestPaths(start, ziel);

            if (paths.size() > 1) {
                foundAny = true;
                System.out.println("\nStation mit mehreren minimalen Pfaden: " + mapper.getName(ziel) + " (" + ziel + ")");
                System.out.println("Anzahl minimaler Pfade: " + paths.size());

                int travelTime = calculatePathWeight(paths.get(0));
                System.out.println("Gesamtreisezeit: " + travelTime + " Sekunden");

                for (List<String> path : paths) {
                    String pathStr = path.stream()
                        .map(id -> mapper.getName(id) + " (" + id + ")")
                        .collect(java.util.stream.Collectors.joining(" → "));
                    System.out.println("  " + pathStr);
                }
            }
        }

        if (!foundAny) {
            System.out.println("Keine Stationen mit mehreren minimalen Pfaden ab " + mapper.getName(start) + " gefunden.");
        }
    }

    private int calculatePathWeight(List<String> path) {
        int total = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            Integer weight = graph.getWeight(path.get(i), path.get(i+1));
            if (weight != null) total += weight;
        }
        return total;
    }

    public static void main(String[] args) {
        try {
            WeightedGraph graph = new WeightedGraph();
            java.net.URL graphUrl = new java.net.URL("https://progwebtec.github.io/classes/ss2025/info2/labs/assignment-09/bvg.txt");
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(graphUrl.openStream()))) {
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
            }

            StationMapper mapper = new StationMapper("https://progwebtec.github.io/classes/ss2025/info2/labs/assignment-09/stations.txt");
            MultiplePath multiplePath = new MultiplePath(graph, mapper);

            String startId = "060192001006"; // S Schöneweide Bhf

            multiplePath.printStationsWithMultipleShortestPaths(startId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
