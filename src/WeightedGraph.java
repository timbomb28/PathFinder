import java.util.*;

public class WeightedGraph {

    private Map<String, List<Edge>> adjList = new HashMap<>();

    public void addNode(String name) {
        adjList.putIfAbsent(name, new ArrayList<>());
    }

    public void addEdge(String from, String to, int weight) {
        addNode(from);
        addNode(to);
        adjList.get(from).add(new Edge(to, weight));
    }

    public List<Edge> getNeighbors(String node) {
        return adjList.getOrDefault(node, new ArrayList<>());
    }

    public Integer getWeight(String from, String to) {
        for (Edge edge : getNeighbors(from)) {
            if (edge.to.equals(to)) return edge.weight;
        }
        return null;
    }

    public Set<String> getNodes() {
        return adjList.keySet();
    }

    public Set<String> getAllVertices() {
        return getNodes();
    }

    public static class Edge {
        public final String to;
        public final int weight;

        public Edge(String to, int weight) {
            this.to = to;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return to + " (" + weight + ")";
        }
    }
}
