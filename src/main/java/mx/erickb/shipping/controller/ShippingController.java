package mx.erickb.shipping.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mx.erickb.shipping.amqp.Sender;
import mx.erickb.shipping.model.PackageType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class ShippingController {

    private final Sender sender;
    private final ObjectMapper mapper;

    ShippingController(final Sender sender, final ObjectMapper mapper) {
        this.sender = sender;
        this.mapper = mapper;
    }

    @GetMapping(path = "types", produces = "application/json")
    public PackageType[] getPackageTypes() throws JsonProcessingException {
        String request = "{ \"type\": \"packageType\" }";
        String json = sender.sendMessage(request).toString();

        return mapper.readValue(json, PackageType[].class);
    }
}
