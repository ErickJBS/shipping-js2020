package mx.erickb.shipping.util.route;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = RoutesGraph.class)
public class RoutesGraphTest {

    private RoutesGraph routesGraph;

    @BeforeEach
    public void init() {
        List<String> cities = Arrays.asList("Chihuahua", "Torreon", "Mazatlan", "Cancun");
        routesGraph = new RoutesGraph(cities);
    }

    @Test
    public void getConnectedCitiesReturnConnectedCities() {
        routesGraph.addRoute("Chihuahua", "Torreon", 1);
        routesGraph.addRoute("Chihuahua", "Mazatlan", 1);
        routesGraph.addRoute("Chihuahua", "Cancun", 1);
        routesGraph.removeRoute("Chihuahua", "Torreon");

        List<String> cities = routesGraph.getConnectedCities("Chihuahua");
        assertEquals(2, cities.size());
        assertTrue(cities.contains("Mazatlan"));
        assertTrue(cities.contains("Cancun"));
        assertFalse(cities.contains("Torreon"));
    }

    @Test
    public void getConnectedCitiesReturnEmptyList() {
        routesGraph.addRoute("Chihuahua", "Torreon", 1);
        routesGraph.addRoute("Torreon", "Mazatlan", 1);
        routesGraph.addRoute("Mazatlan", "Chihuahua", 1);

        List<String> cities = routesGraph.getConnectedCities("Cancun");
        assertTrue(cities.isEmpty());
    }

    @Test
    public void getConnectedCitiesReturnNotExistingCity() {
        List<String> cities = routesGraph.getConnectedCities("Philadelphia");
        assertNull(cities);
    }
}
