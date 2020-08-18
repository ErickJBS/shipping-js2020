package mx.erickb.shipping.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mx.erickb.shipping.amqp.RabbitMqSender;
import mx.erickb.shipping.exception.InvalidResponseException;
import mx.erickb.shipping.model.PackageType;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShippingService implements IShippingService {
    private final Logger logger = LoggerFactory.getLogger(ShippingService.class);

    private final RabbitMqSender sender;
    private final ObjectMapper mapper;

    public ShippingService(final RabbitMqSender sender, final ObjectMapper mapper) {
        this.sender = sender;
        this.mapper = mapper;
    }

    public List<String> getPackageTypes() throws InvalidResponseException {
        JSONObject request = new JSONObject()
                .put("type", "packageType");
        String response = sender.sendRequest(request.toString());

        try {
            PackageType[] types = mapper.readValue(response, PackageType[].class);
            return Arrays.stream(types)
                    .map(PackageType::getDescription)
                    .collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            logger.error("Error when processing server response (" + e.getMessage() + ")");
            throw new InvalidResponseException("Invalid response: \"" + response + '"');
        }
    }
}
