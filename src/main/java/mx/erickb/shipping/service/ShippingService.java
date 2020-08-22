package mx.erickb.shipping.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mx.erickb.shipping.amqp.RabbitMqSender;
import mx.erickb.shipping.exception.InvalidResponseException;
import mx.erickb.shipping.model.PackageSize;
import mx.erickb.shipping.model.PackageType;
import mx.erickb.shipping.model.TransportVelocity;
import mx.erickb.shipping.model.TransportType;
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

    public List<String> getTransportVelocities() throws InvalidResponseException {
        JSONObject request = new JSONObject()
                .put("type", "transportVelocity");
        String response = sender.sendRequest(request.toString());

        try {
            TransportVelocity[] types = mapper.readValue(response, TransportVelocity[].class);
            return Arrays.stream(types)
                    .map(TransportVelocity::getDescription)
                                  .collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            logger.error("Error when processing server response (" + e.getMessage() + ")");
            throw new InvalidResponseException("Invalid response: \"" + response + '"');
        }
    }
  
    public List<String> getPackageSizes(String packageType) throws InvalidResponseException {
        JSONObject request = new JSONObject()
                .put("type", "packageSizeByType")
                .put("packageType", packageType);
        String response = sender.sendRequest(request.toString());

        try {
            PackageSize[] sizes = mapper.readValue(response, PackageSize[].class);
            return Arrays.stream(sizes)
                    .map(PackageSize::getDescription)
                    .collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            logger.error("Error when processing server response (" + e.getMessage() + ")");
            throw new InvalidResponseException("Invalid response: \"" + response + '"');
        }
    }

    public List<String> getTransportTypes() throws InvalidResponseException {
        JSONObject request = new JSONObject()
                .put("type", "transportType");
        String response = sender.sendRequest(request.toString());

        try {
            TransportType[] transportTypes = mapper.readValue(response, TransportType[].class);
            return Arrays.stream(transportTypes)
                    .map(TransportType::getDescription)
                    .collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            logger.error("Error when processing server response (" + e.getMessage() + ")");
            throw new InvalidResponseException("Invalid response: \"" + response + '"');
        }
    }
}
