package mx.erickb.shipping.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqSender {
    private final Logger logger = LoggerFactory.getLogger(RabbitMqSender.class);

    private final RabbitTemplate template;

    public RabbitMqSender(RabbitTemplate template) {
        this.template = template;
    }

    public String sendRequest(String message) {
        logger.debug("Sending: " + message);
        String response = "";
        try {
            response = (String) template.convertSendAndReceive(message);
        } catch (AmqpException e) {
            logger.error(e.getMessage());
        }
        return response;
    }
}
