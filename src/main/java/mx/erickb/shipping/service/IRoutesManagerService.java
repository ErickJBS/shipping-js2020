package mx.erickb.shipping.service;

import mx.erickb.shipping.util.RoutesGraph;

import java.util.List;
import java.util.Optional;

public interface IRoutesManagerService {
    Optional<List<String>> findOptimalRoute(RoutesGraph routesGraph, String origin, String destination);
}
