package mx.erickb.shipping.util;

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
    public void addsAndRemovesConnection() {
        List<String> cities1 = routesGraph.getConnectedCities("Chihuahua");
        List<String> cities2 = routesGraph.getConnectedCities("Cancun");
        assertTrue(cities1.isEmpty());
        assertTrue(cities2.isEmpty());

        routesGraph.addRoute("Chihuahua", "Cancun", 0);
        cities1 = routesGraph.getConnectedCities("Chihuahua");
        cities2 = routesGraph.getConnectedCities("Cancun");
        assertEquals(1, cities1.size());
        assertEquals(1, cities2.size());

        routesGraph.removeRoute("Chihuahua", "Cancun");
        cities1 = routesGraph.getConnectedCities("Chihuahua");
        cities2 = routesGraph.getConnectedCities("Cancun");
        assertTrue(cities1.isEmpty());
        assertTrue(cities2.isEmpty());
    }

    @Test
    public void getConnectedCitiesReturnConnectedCities() {
        routesGraph.addRoute("Chihuahua", "Torreon", 1);
        routesGraph.addRoute("Chihuahua", "Mazatlan", 1);
        routesGraph.addRoute("Chihuahua", "Cancun", 1);

        List<String> cities = routesGraph.getConnectedCities("Chihuahua");
        assertTrue(cities.contains("Mazatlan"));
        assertTrue(cities.contains("Cancun"));
        assertTrue(cities.contains("Torreon"));
        assertFalse(cities.contains("Veracruz"));
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
