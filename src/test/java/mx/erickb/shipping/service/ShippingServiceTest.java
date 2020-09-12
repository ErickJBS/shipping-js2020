package mx.erickb.shipping.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import mx.erickb.shipping.amqp.RabbitMqSender;
import mx.erickb.shipping.exception.InvalidRequestException;
import mx.erickb.shipping.exception.InvalidResponseException;
import mx.erickb.shipping.exception.NotFoundException;
import mx.erickb.shipping.util.ResponseMapper;
import mx.erickb.shipping.util.RouteUtils;
import mx.erickb.shipping.util.RoutesGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ShippingServiceTest {

    @Mock
    RabbitMqSender sender;

    @Mock
    RouteUtils routeUtils;

    ObjectMapper mapper = new ObjectMapper();
    ResponseMapper responseMapper = new ResponseMapper(mapper);

    ShippingService service;

    @BeforeEach
    public void init() {
        service = new ShippingService(sender, responseMapper, routeUtils);
    }

    @Test
    public void getPackageTypesShouldThrowInvalidResponseException() {
        when(sender.sendRequest(anyString())).thenReturn("");

        assertThrows(InvalidResponseException.class, () -> {
            service.getPackageTypes();
        });
    }

    @Test
    public void getPackageTypesShouldReturnEmptyList() {
        when(sender.sendRequest(anyString())).thenReturn("[]");

        List<String> types = service.getPackageTypes();
        assertNotNull(types);
        assertTrue(types.isEmpty());
    }

    @Test
    public void getPackageTypesShouldReturnPackageTypes() {
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
    public void getTransportVelocitiesShouldReturnEmptyList() {
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
    public void getPackageSizesShouldReturnEmptyList() {
        when(sender.sendRequest(anyString())).thenReturn("[]");

        List<String> sizes = service.getPackageSizes("testPackageType");
        assertNotNull(sizes);
        assertTrue(sizes.isEmpty());
    }

    @Test
    public void getPackageSizesShouldReturnPackageSizes() {
        when(sender.sendRequest(anyString())).thenReturn("[{ \"id\": \"2\", \"description\": \"test\", \"priceFactor\": \"5\" }]");

        List<String> sizes = service.getPackageSizes("testPackageType");
        assertNotNull(sizes);
        assertFalse(sizes.isEmpty());
    }

    @Test
    public void getTransportTypesShouldReturnEmptyList() {
        when(sender.sendRequest(anyString())).thenReturn("[]");

        List<String> types = service.getTransportTypes("testPackageSize");
        assertNotNull(types);
        assertTrue(types.isEmpty());
    }

    @Test
    public void getTransportVelocitiesShouldReturnTransportVelocities() {
        when(sender.sendRequest(anyString())).thenReturn("[{ \"id\": \"2\", \"description\": \"test\", \"priceFactor\": \"10\" }]");

        List<String> types = service.getTransportVelocities("testTransportType");
        assertNotNull(types);
        assertFalse(types.isEmpty());
    }

    @Test
    public void getTransportTypesShouldReturnTransportTypes() {
        when(sender.sendRequest(anyString())).thenReturn("[{ \"id\": \"2\", \"description\": \"test\", \"pricePerMile\": \"10\" }]");

        List<String> types = service.getTransportTypes("testPackageSize");
        assertNotNull(types);
        assertFalse(types.isEmpty());
    }

    @Test
    public void getCitiesShouldReturnCities() {
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
    public void getRouteShouldReturnRoute() {
        List<String> cities = Arrays.asList("City1", "City2", "City3");
        when(sender.sendRequest(anyString())).thenReturn("");
        when(routeUtils.parseRoutesResponse(anyString())).thenReturn(new ArrayList<>());
        when(routeUtils.getRoutesGraph(anyList())).thenReturn(new RoutesGraph(cities));
        when(
                routeUtils.findOptimalRoute(
                        any(RoutesGraph.class),
                        anyString(),
                        anyString()
                )
        ).thenReturn(Optional.of(cities));

        String route = service.getRoute("origin", "destination");
        assertEquals("City1 → City2 → City3", route);
    }

    @Test
    public void getRouteShouldThrowInvalidRequestExceptionWhenOriginEqualsDestination() {
        assertThrows(InvalidRequestException.class, () -> {
            service.getRoute("origin", "origin");
        });
    }

    @Test
    public void getRouteShouldThrowInvalidRequestExceptionWhenNullOrigin() {
        assertThrows(InvalidRequestException.class, () -> {
            service.getRoute(null, "destination");
        });
    }

    @Test
    public void getRouteShouldThrowInvalidRequestExceptionWhenNullDestination() {
        assertThrows(InvalidRequestException.class, () -> {
            service.getRoute("origin", null);
        });
    }

    @Test
    public void getRouteShouldThrowInvalidResponseException() {
        when(sender.sendRequest(anyString())).thenReturn("");
        when(routeUtils.parseRoutesResponse(anyString())).thenThrow(InvalidResponseException.class);

        assertThrows(InvalidResponseException.class, () -> {
            service.getRoute("origin", "destination");
        });
    }

    @Test
    public void getRouteShouldThrowNotFoundException() {
        when(sender.sendRequest(anyString())).thenReturn("");
        when(routeUtils.parseRoutesResponse(anyString())).thenReturn(new ArrayList<>());
        when(routeUtils.getRoutesGraph(anyList())).thenReturn(new RoutesGraph(new ArrayList<>()));
        when(
                routeUtils.findOptimalRoute(
                        any(RoutesGraph.class),
                        anyString(),
                        anyString()
                )
        ).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            service.getRoute("origin", "destination");
        });
    }
}
