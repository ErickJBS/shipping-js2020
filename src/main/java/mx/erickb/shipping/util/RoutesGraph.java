package mx.erickb.shipping.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoutesGraph {

    private final Map<String, Map<String, Integer>> adjacencyMatrix = new HashMap<>();

    public RoutesGraph(final List<String> cities) {
        for (String city : cities) {
            adjacencyMatrix.put(city, new HashMap<>());
        }
    }

    public void addRoute(String origin, String destination, int weight) {
        Map<String, Integer> originCity = adjacencyMatrix.get(origin);
        Map<String, Integer> destinationCity = adjacencyMatrix.get(destination);
        if (originCity != null && destinationCity != null) {
            originCity.put(destination, weight);
            destinationCity.put(origin, weight);
        }
    }

    public void removeRoute(String origin, String destination) {
        Map<String, Integer> originCity = adjacencyMatrix.get(origin);
        Map<String, Integer> destinationCity = adjacencyMatrix.get(destination);
        if (originCity != null && destinationCity != null) {
            originCity.remove(destination);
            destinationCity.remove(origin);
        }
    }

    public List<String> getConnectedCities(String origin) {
        Map<String, Integer> cities = adjacencyMatrix.get(origin);
        if (cities == null) {
            return null;
        }
        return new ArrayList<>(cities.keySet());
    }

}
