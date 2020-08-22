package mx.erickb.shipping.service;

import mx.erickb.shipping.exception.InvalidResponseException;

import java.util.List;

public interface IShippingService {
    List<String> getPackageTypes() throws InvalidResponseException;
    List<String> getTransportVelocities() throws InvalidResponseException;
    List<String> getPackageSizes(String packageType) throws InvalidResponseException;
    List<String> getTransportTypes() throws InvalidResponseException;
}
