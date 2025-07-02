import java.util.List;

public class PathResult {
    private final List<String> path;
    private final int totalWeight;

    public PathResult(List<String> path, int totalWeight) {
        this.path = path;
        this.totalWeight = totalWeight;
    }

    public List<String> getPath() {
        return path;
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    @Override
    public String toString() {
        return "Pfad: " + String.join(" → ", path) + "\nGesamtdauer: " + totalWeight + " Sekunden";
    }
}
