package mx.erickb.shipping.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import mx.erickb.shipping.exception.InvalidResponseException;
import mx.erickb.shipping.model.Route;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RouteUtilsTest {

    ObjectMapper mapper = new ObjectMapper();
    RouteUtils routeUtils = new RouteUtils(mapper);

    @Test
    public void parseRoutesShouldReturnRoutes() throws InvalidResponseException {
        String response = "[{\"from\":\"Chihuahua\",\"to\":\"Durango\",\"distance\":\"36\"}," +
                "{\"from\":\"Zacatecas\",\"to\":\"Toluca\",\"distance\":\"49\"}," +
                "{\"from\":\"Toluca\",\"to\":\"Zacatecas\",\"distance\":\"69\"}]";
        List<Route> routes = routeUtils.parseRoutesResponse(response);
        assertNotNull(routes);
        assertFalse(routes.isEmpty());
    }

    @Test
    public void parseRoutesShouldReturnEmptyList() throws InvalidResponseException {
        List<Route> routes = routeUtils.parseRoutesResponse("[]");
        assertNotNull(routes);
        assertTrue(routes.isEmpty());
    }

    @Test
    public void parseRoutesShouldThrowInvalidResponseException() {
        assertThrows(InvalidResponseException.class, () -> {
            routeUtils.parseRoutesResponse("");
        });
    }

    @Test
    public void getRoutesGraphShouldReturnGraph() {
        Route route1 = new Route();
        route1.setFrom("Chihuahua");
        route1.setTo("Torreon");
        route1.setDistance(1);

        Route route2 = new Route();
        route2.setFrom("Chihuahua");
        route2.setTo("Veracruz");
        route2.setDistance(1);

        Route route3 = new Route();
        route3.setFrom("Chihuahua");
        route3.setTo("Hermosillo");
        route3.setDistance(1);

        List<Route> routes = Arrays.asList(route1, route2, route3);

        RoutesGraph graph = routeUtils.getRoutesGraph(routes);
        assertNotNull(graph);

        // Check that connections were added
        List<String> cities = graph.getConnectedCities("Chihuahua");
        assertEquals(3, cities.size());
    }
}
