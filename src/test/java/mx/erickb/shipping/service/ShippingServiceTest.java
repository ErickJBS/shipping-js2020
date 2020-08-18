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
    public void getPackageTypes_shouldThrowInvalidResponseException() {
        when(sender.sendRequest(anyString())).thenReturn("");

        assertThrows(InvalidResponseException.class, () -> {
            service.getPackageTypes();
        });
    }

    @Test
    public void getPackageTypes_shouldReturnEmptyList() throws InvalidResponseException {
        when(sender.sendRequest(anyString())).thenReturn("[]");

        List<String> types = service.getPackageTypes();
        assertNotNull(types);
        assertTrue(types.isEmpty());
    }

    @Test
    public void getPackageTypes_shouldReturnPackageTypes() throws InvalidResponseException {
        when(sender.sendRequest(anyString())).thenReturn("[{ \"id\": \"2\", \"description\": \"test\", \"price\": \"10\" }]");

        List<String> types = service.getPackageTypes();
        assertNotNull(types);
        assertFalse(types.isEmpty());
    }
}
