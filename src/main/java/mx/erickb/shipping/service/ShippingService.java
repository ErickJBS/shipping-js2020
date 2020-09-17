package mx.erickb.shipping.service;

import mx.erickb.shipping.amqp.RabbitMqSender;
import mx.erickb.shipping.exception.InvalidRequestException;
import mx.erickb.shipping.exception.NotFoundException;
import mx.erickb.shipping.model.*;
import mx.erickb.shipping.util.ResponseMapper;
import mx.erickb.shipping.util.RouteUtils;
import mx.erickb.shipping.util.RoutesGraph;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ShippingService implements IShippingService {
    private static final String TYPE_VELOCITY = "transportVelocity";
    private static final String TYPE_PACKAGE = "packageType";
    private static final String TYPE_SIZE = "packageSizeByType";
    private static final String TYPE_TRANSPORT = "transportType";
    private static final String TYPE_CITY = "city";
    private static final String TYPE_ROUTE = "routesList";

    private final Logger logger = LoggerFactory.getLogger(ShippingService.class);
    private final RabbitMqSender sender;
    private final ResponseMapper mapper;
    private final RouteUtils utils;

    public ShippingService(RabbitMqSender sender, ResponseMapper mapper, RouteUtils utils) {
        this.sender = sender;
        this.mapper = mapper;
        this.utils = utils;
    }

    public List<String> getPackageTypes() {
        JSONObject request = new JSONObject()
                .put("type", TYPE_PACKAGE);
        String response = sender.sendRequest(request.toString());

        return mapper.map(response, PackageType[].class);
    }

    public List<String> getTransportVelocities(String transportType) {
        JSONObject request = new JSONObject()
                .put("type", TYPE_VELOCITY);
        String response = sender.sendRequest(request.toString());

        return mapper.map(response, TransportVelocity[].class);
    }

    public List<String> getPackageSizes(String packageType) {
        JSONObject request = new JSONObject()
                .put("type", TYPE_SIZE)
                .put("packageType", packageType);
        String response = sender.sendRequest(request.toString());

        return mapper.map(response, PackageSize[].class);
    }

    public List<String> getTransportTypes(String packageSize) {
        JSONObject request = new JSONObject()
                .put("type", TYPE_TRANSPORT);
        String response = sender.sendRequest(request.toString());

        return mapper.map(response, TransportType[].class);
    }

    public List<String> getCities() {
        JSONObject request = new JSONObject()
                .put("type", TYPE_CITY);
        String response = sender.sendRequest(request.toString());

        List<String> cities = mapper.map(response, City[].class);
        Collections.sort(cities);
        return cities;
    }

    @Override
    public String getRoute(String origin, String destination) {
        if (origin == null || destination == null) {
            logger.error("origin and destination must be valid not empty city names");
            throw new InvalidRequestException("Null origin/destination provided");
        }
        if (origin.equals(destination)) {
            logger.error("Origin cannot be equal to destination ({})", origin);
            throw new InvalidRequestException("Origin cannot be equal to destination");
        }
        JSONObject request = new JSONObject()
                .put("type", TYPE_ROUTE)
                .put("origin", origin)
                .put("destination", destination);
        String response = sender.sendRequest(request.toString());

        List<Route> routes = utils.parseRoutesResponse(response);
        RoutesGraph routesGraph = utils.getRoutesGraph(routes);
        Optional<List<String>> path = utils.findOptimalRoute(routesGraph, origin, destination);

        if (!path.isPresent()) {
            String msg = String.format("Route between %s and %s not found", origin, destination);
            logger.info(msg);
            throw new NotFoundException(msg);
        }
        return String.join(" → ", path.get());
    }
}
