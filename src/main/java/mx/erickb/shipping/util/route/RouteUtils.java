package mx.erickb.shipping.util.route;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mx.erickb.shipping.exception.InvalidResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RouteUtils {

    private final Logger logger = LoggerFactory.getLogger(RouteUtils.class);
    private final ObjectMapper mapper;

    public RouteUtils(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public List<Route> parseRoutes(String routesResponse) throws InvalidResponseException {
        try {
            Route[] routes = mapper.readValue(routesResponse, Route[].class);
            return Arrays.asList(routes);
        } catch (JsonProcessingException e) {
            logger.error("Error when processing server response (" + e.getMessage() + ")");
            throw new InvalidResponseException("Invalid response: \"" + routesResponse + '"');
        }
    }

    public RoutesGraph getRoutesGraph(List<Route> routes) {
        // Count unique cities
        Set<String> citiesSet = new HashSet<>();
        for (Route route : routes) {
            citiesSet.add(route.getFrom());
            citiesSet.add(route.getTo());
        }

        List<String> cities = new ArrayList<>(citiesSet);
        RoutesGraph routesGraph = new RoutesGraph(cities);
        // Filling connections
        for (Route route : routes) {
            routesGraph.addRoute(route.getFrom(), route.getTo(), route.getDistance());
        }
        return routesGraph;
    }

    public List<String> findOptimalRoute(RoutesGraph routesGraph, String origin, String destination) {
        return this.findOptimalRoute(new ArrayList<>(), routesGraph, origin, destination);
    }

    // Depth first search (recursive) to find route (weights are discarded)
    private List<String> findOptimalRoute(List<String> currentRoute, RoutesGraph routesGraph, String currentCity, String destination) {
        // Clone current route to avoid interfering with recursion
        List<String> route = new ArrayList<>(currentRoute);
        route.add(currentCity);
        if (currentCity.equals(destination)) {
            return route;
        }

        // Recursively visit each connected city
        for (String connectedCity : routesGraph.getConnectedCities(currentCity)) {
            if (route.contains(connectedCity)) {
                // Skip if already visited in current route (avoid cycles)
                continue;
            }
            List<String> result = findOptimalRoute(route, routesGraph, connectedCity, destination);
            if (result != null) {
                return result;
            }
        }

        // No route found
        return null;
    }
}
