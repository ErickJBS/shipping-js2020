package mx.erickb.shipping.service;

import mx.erickb.shipping.amqp.RabbitMqSender;
import mx.erickb.shipping.exception.InvalidResponseException;
import mx.erickb.shipping.model.PackageSize;
import mx.erickb.shipping.model.PackageType;
import mx.erickb.shipping.model.TransportType;
import mx.erickb.shipping.model.TransportVelocity;
import mx.erickb.shipping.util.ResponseMapper;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShippingService implements IShippingService {
    private final RabbitMqSender sender;
    private final ResponseMapper mapper;

    public ShippingService(final RabbitMqSender sender, final ResponseMapper mapper) {
        this.sender = sender;
        this.mapper = mapper;
    }

    public List<String> getPackageTypes() throws InvalidResponseException {
        JSONObject request = new JSONObject()
                .put("type", "packageType");
        String response = sender.sendRequest(request.toString());

        return mapper.map(response, PackageType[].class);
    }

    public List<String> getTransportVelocities(String transportType) throws InvalidResponseException {
        JSONObject request = new JSONObject()
                .put("type", "transportVelocity");
        String response = sender.sendRequest(request.toString());

        return mapper.map(response, TransportVelocity[].class);
    }

    public List<String> getPackageSizes(String packageType) throws InvalidResponseException {
        JSONObject request = new JSONObject()
                .put("type", "packageSizeByType")
                .put("packageType", packageType);
        String response = sender.sendRequest(request.toString());

        return mapper.map(response, PackageSize[].class);
    }

    public List<String> getTransportTypes(String packageSize) throws InvalidResponseException {
        JSONObject request = new JSONObject()
                .put("type", "transportType");
        String response = sender.sendRequest(request.toString());

        return mapper.map(response, TransportType[].class);
    }
}
