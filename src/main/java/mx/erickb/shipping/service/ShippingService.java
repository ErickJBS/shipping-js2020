package mx.erickb.shipping.service;

import mx.erickb.shipping.amqp.RabbitMqSender;
import mx.erickb.shipping.exception.InvalidRequestException;
import mx.erickb.shipping.exception.InvalidResponseException;
import mx.erickb.shipping.exception.NotFoundException;
import mx.erickb.shipping.model.*;
import mx.erickb.shipping.util.ResponseMapper;
import mx.erickb.shipping.util.route.Route;
import mx.erickb.shipping.util.route.RouteUtils;
import mx.erickb.shipping.util.route.RoutesGraph;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShippingService implements IShippingService {
    private final RabbitMqSender sender;
    private final ResponseMapper mapper;
    private final RouteUtils utils;

    public ShippingService(final RabbitMqSender sender, final ResponseMapper mapper, final RouteUtils utils) {
        this.sender = sender;
        this.mapper = mapper;
        this.utils = utils;
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

    public List<String> getCities() throws InvalidResponseException {
        JSONObject request = new JSONObject()
                .put("type", "city");
        String response = sender.sendRequest(request.toString());

        return mapper.map(response, City[].class);
    }

    @Override
    public String getRoute(String origin, String destination) throws InvalidRequestException, InvalidResponseException, NotFoundException {
        if (origin.equals(destination)) {
            throw new InvalidRequestException("Origin cannot be equal to destination");
        }
        JSONObject request = new JSONObject()
                .put("type", "routesList")
                .put("origin", origin)
                .put("destination", destination);
        String response = sender.sendRequest(request.toString());

        List<Route> routes = utils.parseRoutes(response);
        RoutesGraph routesGraph = utils.getRoutesGraph(routes);
        List<String> path = utils.findOptimalRoute(routesGraph, origin, destination);

        if (path == null) {
            String msg = String.format("Route between %s and %s not found", origin, destination);
            throw new NotFoundException(msg);
        }
        return String.join(" → ", path);
    }
}
