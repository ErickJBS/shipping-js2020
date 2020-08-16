package mx.erickb.shipping.service;

import mx.erickb.shipping.exception.InvalidResponseException;
import mx.erickb.shipping.model.PackageType;

import java.util.List;

public interface IShippingService {
    List<PackageType> getPackageTypes() throws InvalidResponseException;
}
