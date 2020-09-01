package mx.erickb.shipping.service;

import mx.erickb.shipping.amqp.RabbitMqSender;
import mx.erickb.shipping.exception.InvalidRequestException;
import mx.erickb.shipping.exception.InvalidResponseException;
import mx.erickb.shipping.exception.NotFoundException;
import mx.erickb.shipping.util.route.RouteUtils;
import mx.erickb.shipping.util.route.RoutesGraph;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ShippingServiceTest {

    @MockBean
    RabbitMqSender sender;

    @MockBean
    RouteUtils routeUtils;

    @Autowired
    ShippingService service;

    @Test
    public void getPackageTypesShouldThrowInvalidResponseException() {
        when(sender.sendRequest(anyString())).thenReturn("");

        assertThrows(InvalidResponseException.class, () -> {
            service.getPackageTypes();
        });
    }

    @Test
    public void getPackageTypesShouldReturnEmptyList() throws InvalidResponseException {
        when(sender.sendRequest(anyString())).thenReturn("[]");

        List<String> types = service.getPackageTypes();
        assertNotNull(types);
        assertTrue(types.isEmpty());
    }

    @Test
    public void getPackageTypesShouldReturnPackageTypes() throws InvalidResponseException {
        when(sender.sendRequest(anyString())).thenReturn("[{ \"id\": \"2\", \"description\": \"test\", \"price\": \"10\" }]");

        List<String> types = service.getPackageTypes();
        assertNotNull(types);
        assertFalse(types.isEmpty());
    }

    @Test
    public void getTransportVelocitiesShouldThrowInvalidResponseException() {
        when(sender.sendRequest(anyString())).thenReturn("");

        assertThrows(InvalidResponseException.class, () -> {
            service.getTransportVelocities("testTransportType");
        });
    }

    @Test
    public void getPackageSizesShouldThrowInvalidResponseException() {
        when(sender.sendRequest(anyString())).thenReturn("");

        assertThrows(InvalidResponseException.class, () -> {
            service.getPackageSizes("testPackageType");
        });
    }

    @Test
    public void getTransportVelocitiesShouldReturnEmptyList() throws InvalidResponseException {
        when(sender.sendRequest(anyString())).thenReturn("[]");

        List<String> types = service.getTransportVelocities("testTransportType");
        assertNotNull(types);
        assertTrue(types.isEmpty());
    }

    @Test
    public void getTransportTypesShouldThrowInvalidResponseException() {
        when(sender.sendRequest(anyString())).thenReturn("");

        assertThrows(InvalidResponseException.class, () -> {
            service.getTransportTypes("testPackageSize");
        });
    }

    @Test
    public void getPackageSizesShouldReturnEmptyList() throws InvalidResponseException {
        when(sender.sendRequest(anyString())).thenReturn("[]");

        List<String> sizes = service.getPackageSizes("testPackageType");
        assertNotNull(sizes);
        assertTrue(sizes.isEmpty());
    }

    @Test
    public void getPackageSizesShouldReturnPackageSizes() throws InvalidResponseException {
        when(sender.sendRequest(anyString())).thenReturn("[{ \"id\": \"2\", \"description\": \"test\", \"priceFactor\": \"5\" }]");

        List<String> sizes = service.getPackageSizes("testPackageType");
        assertNotNull(sizes);
        assertFalse(sizes.isEmpty());
    }

    @Test
    public void getTransportTypesShouldReturnEmptyList() throws InvalidResponseException {
        when(sender.sendRequest(anyString())).thenReturn("[]");

        List<String> types = service.getTransportTypes("testPackageSize");
        assertNotNull(types);
        assertTrue(types.isEmpty());
    }

    @Test
    public void getTransportVelocitiesShouldReturnTransportVelocities() throws InvalidResponseException {
        when(sender.sendRequest(anyString())).thenReturn("[{ \"id\": \"2\", \"description\": \"test\", \"priceFactor\": \"10\" }]");

        List<String> types = service.getTransportVelocities("testTransportType");
        assertNotNull(types);
        assertFalse(types.isEmpty());
    }

    @Test
    public void getTransportTypesShouldReturnTransportTypes() throws InvalidResponseException {
        when(sender.sendRequest(anyString())).thenReturn("[{ \"id\": \"2\", \"description\": \"test\", \"pricePerMile\": \"10\" }]");

        List<String> types = service.getTransportTypes("testPackageSize");
        assertNotNull(types);
        assertFalse(types.isEmpty());
    }

    @Test
    public void getCitiesShouldReturnCities() throws InvalidResponseException {
        when(sender.sendRequest(anyString())).thenReturn("[{\"id\":33,\"name\":\"La Paz\",\"tax\":10,\"seaport\":true,\"airport\":true}]");

        List<String> cities = service.getCities();
        assertNotNull(cities);
        assertFalse(cities.isEmpty());
    }

    @Test
    public void getCitiesShouldThrowInvalidResponseException() {
        when(sender.sendRequest(anyString())).thenReturn("");

        assertThrows(InvalidResponseException.class, () -> {
            service.getCities();
        });
    }

    @Test
    public void getRouteShouldReturnRoute() throws Exception {
        List<String> cities = Arrays.asList("City1", "City2", "City3");
        when(sender.sendRequest(anyString())).thenReturn("");
        when(routeUtils.parseRoutes(anyString())).thenReturn(new ArrayList<>());
        when(routeUtils.getRoutesGraph(anyList())).thenReturn(new RoutesGraph(cities));
        when(
                routeUtils.findOptimalRoute(
                        any(RoutesGraph.class),
                        anyString(),
                        anyString()
                )
        ).thenReturn(cities);

        String route = service.getRoute("origin", "destination");
        assertEquals("City1 → City2 → City3", route);
    }

    @Test
    public void getRouteShouldThrowInvalidRequestException() {
        assertThrows(InvalidRequestException.class, () -> {
            service.getRoute("origin", "origin");
        });
    }

    @Test
    public void getRouteShouldThrowInvalidResponseException() throws InvalidResponseException {
        when(sender.sendRequest(anyString())).thenReturn("");
        when(routeUtils.parseRoutes(anyString())).thenThrow(InvalidResponseException.class);

        assertThrows(InvalidResponseException.class, () -> {
            service.getRoute("origin", "destination");
        });
    }

    @Test
    public void getRouteShouldThrowNotFoundException() throws InvalidResponseException {
        when(sender.sendRequest(anyString())).thenReturn("");
        when(routeUtils.parseRoutes(anyString())).thenReturn(new ArrayList<>());
        when(routeUtils.getRoutesGraph(anyList())).thenReturn(new RoutesGraph(new ArrayList<>()));
        when(
                routeUtils.findOptimalRoute(
                        any(RoutesGraph.class),
                        anyString(),
                        anyString()
                )
        ).thenReturn(null);

        assertThrows(NotFoundException.class, () -> {
            service.getRoute("origin", "destination");
        });
    }
}
