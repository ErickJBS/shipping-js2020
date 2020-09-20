package mx.erickb.shipping.service;

import mx.erickb.shipping.util.RoutesGraph;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoutesManagerService implements IRoutesManagerService {

    // Breadth-first search using priority queue
    public Optional<List<String>> findOptimalRoute(RoutesGraph routesGraph, String origin, String destination) {
        // Priority queue that returns min instead of max element
        PriorityQueue<Pair<List<String>, Integer>> search
                = new PriorityQueue<>(Comparator.comparing(Pair::getRight));
        Set<String> visitedCities = new HashSet<>();

        // Inserting root node (BFS starting point)
        search.add(Pair.of(Collections.singletonList(origin), 0));

        while (!search.isEmpty()) {
            Pair<List<String>, Integer> currentNode = search.poll();
            List<String> currentRoute = currentNode.getLeft();
            String currentCity = currentRoute.get(currentRoute.size() - 1);
            Integer currentCost = currentNode.getRight();
            visitedCities.add(currentCity);
            if (currentCity.equals(destination)) {
                return Optional.of(currentRoute);
            }

            // Add all the (non-visited) child nodes to the  search
            for (String connectedCity : routesGraph.getConnectedCities(currentCity)) {
                if (visitedCities.contains(connectedCity)) {
                    // Skip if already visited in current route (avoid cycles)
                    continue;
                }
                // Include connected city in current route
                List<String> updatedRoute = new ArrayList<>(currentRoute);
                updatedRoute.add(connectedCity);
                Integer connectionCost = routesGraph.getDistance(currentCity, connectedCity);
                search.add(Pair.of(updatedRoute, currentCost + connectionCost));
            }
        }
        return Optional.empty();
    }
}
