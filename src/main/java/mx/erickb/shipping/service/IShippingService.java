package mx.erickb.shipping.service;

import mx.erickb.shipping.exception.InvalidResponseException;

import java.util.List;

public interface IShippingService {
    List<String> getPackageTypes() throws InvalidResponseException;
}
