package mx.erickb.shipping.amqp;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class Sender {
    private final RabbitTemplate template;

    public Sender(RabbitTemplate template) {
        this.template = template;
    }

    public Object sendMessage(String message) {
        System.out.println("[Sender] Sending: " + message);
        return template.convertSendAndReceive(
                RabbitMqConfig.EXCHANGE_NAME,
                RabbitMqConfig.ROUTING_KEY,
                message
        );
    }
}
