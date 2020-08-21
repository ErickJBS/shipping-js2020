package mx.erickb.shipping.amqp;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RabbitMqSenderTest {

    @MockBean
    private RabbitTemplate template;

    @Autowired
    private RabbitMqSender sender;

    @Test
    public void sendRequestShouldReturnNonEmptyResponseWhenRequestSucceed() {
        String mockResponse = "[]";
        when(template.convertSendAndReceive(any())).thenReturn(mockResponse);

        String request = "{ \"type\": \"packageType\" }";
        String response = sender.sendRequest(request);

        assertNotEquals("", response);
    }

    @Test
    public void sendRequestShouldReturnEmptyResponseWhenRequestFails() {
        when(template.convertSendAndReceive(any())).thenThrow(AmqpException.class);

        String request = "{ \"type\": \"packageType\" }";
        String response = sender.sendRequest(request);

        assertEquals("", response);
    }
}
