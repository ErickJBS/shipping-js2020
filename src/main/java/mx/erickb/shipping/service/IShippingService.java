package mx.erickb.shipping.service;

import mx.erickb.shipping.exception.InvalidRequestException;
import mx.erickb.shipping.exception.InvalidResponseException;
import mx.erickb.shipping.exception.NotFoundException;

import java.util.List;

public interface IShippingService {
    List<String> getPackageTypes() throws InvalidResponseException;

    List<String> getTransportVelocities(String transportType) throws InvalidResponseException;

    List<String> getPackageSizes(String packageType) throws InvalidResponseException;

    List<String> getTransportTypes(String packageSize) throws InvalidResponseException;

    List<String> getCities() throws InvalidResponseException;

    String getRoute(String origin, String destination) throws InvalidResponseException, InvalidRequestException, NotFoundException;
}
