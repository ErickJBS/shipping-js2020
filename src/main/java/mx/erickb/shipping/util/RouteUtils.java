package mx.erickb.shipping.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mx.erickb.shipping.exception.InvalidResponseException;
import mx.erickb.shipping.model.Route;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class RouteUtils {

    private final Logger logger = LoggerFactory.getLogger(RouteUtils.class);
    private final ObjectMapper mapper;

    public RouteUtils(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public List<Route> parseRoutesResponse(String routesResponse) {
        try {
            Route[] routes = mapper.readValue(routesResponse, Route[].class);
            return Arrays.asList(routes);
        } catch (JsonProcessingException e) {
            logger.error("Error when processing server response", e);
            throw new InvalidResponseException("Invalid response: \"" + routesResponse + '"');
        }
    }

    public RoutesGraph getRoutesGraph(List<Route> routes) {
        // Get unique cities
        List<String> cities = routes.stream()
                .flatMap(route -> Stream.of(route.getTo(), route.getFrom()))
                .distinct().collect(Collectors.toList());

        RoutesGraph routesGraph = new RoutesGraph(cities);
        // Filling connections
        for (Route route : routes) {
            routesGraph.addRoute(route.getFrom(), route.getTo(), route.getDistance());
        }
        return routesGraph;
    }

    // Breadth-first search using priority queue
    public Optional<List<String>> findOptimalRoute(RoutesGraph routesGraph, String origin, String destination) {
        // Priority queue that returns min instead of max element
        PriorityQueue<Pair<List<String>, Integer>> search
                = new PriorityQueue<>(Comparator.comparing(Pair::getRight));

        // Inserting root node (BFS starting point)
        search.add(Pair.of(Collections.singletonList(origin), 0));

        while (!search.isEmpty()) {
            Pair<List<String>, Integer> currentNode = search.poll();
            List<String> currentRoute = currentNode.getLeft();
            String currentCity = currentRoute.get(currentRoute.size() - 1);
            if (currentCity.equals(destination)) {
                return Optional.of(currentRoute);
            }

            // Add all the (non-visited) child nodes to the  search
            Integer currentCost = currentNode.getRight();
            for (String connectedCity : routesGraph.getConnectedCities(currentCity)) {
                if (currentRoute.contains(connectedCity)) {
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
