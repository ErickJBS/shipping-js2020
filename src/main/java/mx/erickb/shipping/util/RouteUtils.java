package mx.erickb.shipping.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mx.erickb.shipping.exception.InvalidResponseException;
import mx.erickb.shipping.model.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
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
}
