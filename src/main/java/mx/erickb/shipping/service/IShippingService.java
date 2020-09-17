package mx.erickb.shipping.service;

import java.util.List;

public interface IShippingService {

    List<String> getPackageTypes();

    List<String> getTransportVelocities(String transportType);

    List<String> getPackageSizes(String packageType);

    List<String> getTransportTypes(String packageSize);

    List<String> getCities();

    String getRoute(String origin, String destination);

}
