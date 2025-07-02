import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.awt.BorderLayout;

public class StationMapper {
    private final Map<String, String> idToName = new HashMap<>();

    public StationMapper(String url) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    idToName.put(parts[0], parts[1]);
                }
            }
        }
    }

    public String getName(String id) {
        return idToName.getOrDefault(id, id);
    }

    public Map<String, String> getAllStations() {
        return idToName;
    }
}
