package mx.erickb.shipping.service;

import mx.erickb.shipping.amqp.RabbitMqSender;
import mx.erickb.shipping.exception.InvalidResponseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ShippingServiceTest {

    @MockBean
    RabbitMqSender sender;

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
    public void getTransportVelocitiesShouldReturnPackageTypes() throws InvalidResponseException {
        when(sender.sendRequest(anyString())).thenReturn("[{ \"id\": \"2\", \"description\": \"test\", \"priceFactor\": \"10\" }]");

        List<String> types = service.getTransportVelocities("testTransportType");
        assertNotNull(types);
        assertFalse(types.isEmpty());
    }

    @Test
    public void getTransportTypesShouldReturnPackageTypes() throws InvalidResponseException {
        when(sender.sendRequest(anyString())).thenReturn("[{ \"id\": \"2\", \"description\": \"test\", \"pricePerMile\": \"10\" }]");

        List<String> types = service.getTransportTypes("testPackageSize");
        assertNotNull(types);
        assertFalse(types.isEmpty());
    }
}
