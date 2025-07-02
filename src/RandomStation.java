import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.nio.file.*;

public class RandomStation {
    public static void main(String[] args) {
        try {
            // Stationen einlesen
            URL stationURL = new URL("https://progwebtec.github.io/classes/ss2025/info2/labs/assignment-09/stations.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stationURL.openStream()));
            List<String> stationIDs = new ArrayList<>();
            Map<String, String> stationNames = new HashMap<>();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    stationIDs.add(parts[0]);
                    stationNames.put(parts[0], parts[1]);
                }
            }

            reader.close();

            // Zwei zufällige Stationen auswählen
            Random rand = new Random();
            String start;
            String end;
            do {
                start = stationIDs.get(rand.nextInt(stationIDs.size()));
                end = stationIDs.get(rand.nextInt(stationIDs.size()));
            } while (start.equals(end));

            // Graph einlesen
            BufferedReader graphReader = new BufferedReader(new InputStreamReader(
                    new URL("https://progwebtec.github.io/classes/ss2025/info2/labs/assignment-09/bvg.txt").openStream()));

            // Zwischenpuffer schreiben und temporär abspeichern
            java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("graph", ".txt");
            java.nio.file.Files.write(tempFile, graphReader.lines().toList());

            WeightedGraph graph = GraphReader.readFromFile(tempFile.toString());

            graphReader.close();

            PathResult result;
            int attempts = 0;
            final int MAX_ATTEMPTS = 1000000;

            do {
                do {
                    start = stationIDs.get(rand.nextInt(stationIDs.size()));
                    end = stationIDs.get(rand.nextInt(stationIDs.size()));
                } while (start.equals(end));
                result = PathFinder.dijkstra(graph, start, end);
                attempts++;
            } while ((result.getTotalWeight() == Integer.MAX_VALUE || result.getPath().isEmpty()) && attempts < MAX_ATTEMPTS);

            if (result.getTotalWeight() == Integer.MAX_VALUE || result.getPath().isEmpty()) {
                System.out.println(">> Kein gültiger Pfad konnte nach " + MAX_ATTEMPTS + " Versuchen gefunden werden.");
            } else {
                System.out.println(">> Zufälliger Pfad von " + stationNames.getOrDefault(start, start)
                        + " (" + start + ") zu " + stationNames.getOrDefault(end, end) + " (" + end + ")");
                System.out.println("Pfad: " + String.join(" → ",
                        result.getPath().stream()
                                .map(id -> stationNames.getOrDefault(id, id) + " (" + id + ")")
                                .toList()));
                System.out.println("Gesamtdauer: " + result.getTotalWeight() + " Sekunden");
            }

        } catch (Exception e) {
            System.err.println("Fehler: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
