package mx.erickb.shipping.util.route;

import java.util.ArrayList;
import java.util.List;

public class RoutesGraph {

    private final List<String> cities;
    private final int[][] adjacencyMatrix;

    public RoutesGraph(final List<String> cities) {
        this.cities = cities;
        int size = cities.size();
        this.adjacencyMatrix = new int[size][size];
    }

    public void addRoute(String origin, String destination, int weight) {
        int i = findCityIndex(origin);
        int j = findCityIndex(destination);
        if (i != -1 && j != -1) {
            this.adjacencyMatrix[i][j] = weight;
            this.adjacencyMatrix[j][i] = weight;
        }
    }

    public void removeRoute(String origin, String destination) {
        int i = findCityIndex(origin);
        int j = findCityIndex(destination);
        if (i != -1 && j != -1) {
            this.adjacencyMatrix[i][j] = 0;
            this.adjacencyMatrix[j][i] = 0;
        }
    }

    public List<String> getConnectedCities(String origin) {
        int originIdx = findCityIndex(origin);
        if (originIdx == -1) {
            // Not existing city
            return null;
        }

        List<String> connectedCities = new ArrayList<>();
        for (int i = 0; i < adjacencyMatrix[originIdx].length; i++) {
            if (adjacencyMatrix[originIdx][i] > 0) {
                // There is a connection to origin
                String city = cities.get(i);
                connectedCities.add(city);
            }
        }
        return connectedCities;
    }

    private int findCityIndex(String city) {
        return cities.indexOf(city);
    }
}
