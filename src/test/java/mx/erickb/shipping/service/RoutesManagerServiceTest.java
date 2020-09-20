package mx.erickb.shipping.service;

import mx.erickb.shipping.util.RoutesGraph;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RoutesManagerServiceTest {

    RoutesManagerService routesManager = new RoutesManagerService();

    @Test
    public void findOptimalRouteShouldReturnNoRoute() {
        // No route between origin and destination
        List<String> cities = Arrays.asList("Chihuahua", "Torreon", "Hermosillo", "La Paz", "Veracruz");
        RoutesGraph graph = new RoutesGraph(cities);
        graph.addRoute("Chihuahua", "Torreon", 1);
        graph.addRoute("Chihuahua", "Hermosillo", 1);
        graph.addRoute("La Paz", "Hermosillo", 1);

        Optional<List<String>> route = routesManager.findOptimalRoute(graph, "La Paz", "Veracruz");
        assertFalse(route.isPresent());
    }

    @Test
    public void findOptimalRouteShouldReturnLongerButOptimalRoute() {
        // There is a route with less nodes but higher weights
        // and a longer route with lighter weights (should choose this one)
        List<String> cities = Arrays.asList("Pachuca", "Tepic", "Chihuahua", "Hermosillo", "Saltillo", "Guadalajara",
                "San Luis", "Veracruz", "La Paz", "Cancun", "Culiacan", "Obregon", "Juarez", "Queretaro");
        RoutesGraph graph = new RoutesGraph(cities);
        graph.addRoute("Pachuca", "Chihuahua", 10);
        graph.addRoute("Chihuahua", "Tepic", 20);
        graph.addRoute("Hermosillo", "Chihuahua", 30);
        graph.addRoute("Chihuahua", "Saltillo", 5);
        graph.addRoute("La Paz", "Hermosillo", 10);
        graph.addRoute("Obregon", "La Paz", 150);
        graph.addRoute("Saltillo", "Guadalajara", 9);
        graph.addRoute("Saltillo", "San Luis", 15);
        graph.addRoute("Veracruz", "Saltillo", 20);
        graph.addRoute("Guadalajara", "Cancun", 5);
        graph.addRoute("Cancun", "San Luis", 8);
        graph.addRoute("Obregon", "Cancun", 2);
        graph.addRoute("Cancun", "Juarez", 8);
        graph.addRoute("Culiacan", "Cancun", 15);
        graph.addRoute("Juarez", "Culiacan", 3);
        graph.addRoute("Queretaro", "Juarez", 9);

        // La paz - Obregon - Cancun route has less nodes but more distance
        Optional<List<String>> optionalRoute = routesManager.findOptimalRoute(graph, "La Paz", "Cancun");
        assertTrue(optionalRoute.isPresent());

        List<String> actualRoute = optionalRoute.get();
        List<String> expectedRoute = Arrays.asList("La Paz", "Hermosillo", "Chihuahua", "Saltillo", "Guadalajara", "Cancun");
        assertEquals(expectedRoute.size(), actualRoute.size());

        for (int i = 0; i < expectedRoute.size(); i++) {
            assertEquals(expectedRoute.get(i), actualRoute.get(i));
        }
    }

    @Test
    public void findOptimalRouteShouldReturnShortestRoute() {
        // Two routes with the same number of nodes but one weights' are lighter
        List<String> cities = Arrays.asList("Chihuahua", "Monterrey", "Guadalajara", "Cancun", "Torreon", "CDMX");
        RoutesGraph graph = new RoutesGraph(cities);
        graph.addRoute("Chihuahua", "Monterrey", 3);
        graph.addRoute("Monterrey", "Guadalajara", 3);
        graph.addRoute("Guadalajara", "CDMX", 4);
        graph.addRoute("Chihuahua", "Cancun", 4);
        graph.addRoute("Cancun", "Torreon", 4);
        graph.addRoute("Torreon", "CDMX", 1);

        Optional<List<String>> optionalRoute = routesManager.findOptimalRoute(graph, "Chihuahua", "CDMX");
        assertTrue(optionalRoute.isPresent());

        List<String> actualRoute = optionalRoute.get();
        List<String> expectedRoute = Arrays.asList("Chihuahua", "Cancun", "Torreon", "CDMX");
        assertEquals(expectedRoute.size(), actualRoute.size());

        for (int i = 0; i < expectedRoute.size(); i++) {
            assertEquals(expectedRoute.get(i), actualRoute.get(i));
        }
    }

    @Test
    public void findOptimalRouteShouldReturnOptimalRoute() {
        List<String> cities = Arrays.asList("Chihuahua", "Hermosillo", "Monterrey", "Mazatlan");
        RoutesGraph graph = new RoutesGraph(cities);
        graph.addRoute("Chihuahua", "Hermosillo", 1);
        graph.addRoute("Mazatlan", "Hermosillo", 1);
        graph.addRoute("Monterrey", "Chihuahua", 1);
        graph.addRoute("Chihuahua", "Mazatlan", 3);
        graph.addRoute("Monterrey", "Mazatlan", 2);

        Optional<List<String>> optionalRoute = routesManager.findOptimalRoute(graph, "Chihuahua", "Mazatlan");
        assertTrue(optionalRoute.isPresent());

        List<String> actualRoute = optionalRoute.get();
        List<String> expectedRoute = Arrays.asList("Chihuahua", "Hermosillo", "Mazatlan");
        assertEquals(expectedRoute.size(), actualRoute.size());

        for (int i = 0; i < expectedRoute.size(); i++) {
            assertEquals(expectedRoute.get(i), actualRoute.get(i));
        }
    }

    @Test
    public void findOptimalRouteShouldReturnShortestRouteEqualDistances() {
        // All connections have the same length, should return the one with less nodes
        List<String> cities = Arrays.asList("A", "B", "C", "D", "E", "F");
        RoutesGraph graph = new RoutesGraph(cities);
        graph.addRoute("A", "C", 1);
        graph.addRoute("A", "F", 1);
        graph.addRoute("B", "C", 1);
        graph.addRoute("C", "F", 1);
        graph.addRoute("D", "E", 1);
        graph.addRoute("D", "F", 1);
        graph.addRoute("E", "F", 1);

        Optional<List<String>> optionalRoute = routesManager.findOptimalRoute(graph, "A", "D");
        assertTrue(optionalRoute.isPresent());

        List<String> actualRoute = optionalRoute.get();
        List<String> expectedRoute = Arrays.asList("A", "F", "D");
        assertEquals(expectedRoute.size(), actualRoute.size());

        for (int i = 0; i < expectedRoute.size(); i++) {
            assertEquals(expectedRoute.get(i), actualRoute.get(i));
        }
    }
}
