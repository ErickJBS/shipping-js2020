package mx.erickb.shipping.controller;

import mx.erickb.shipping.exception.InvalidRequestException;
import mx.erickb.shipping.exception.InvalidResponseException;
import mx.erickb.shipping.exception.NotFoundException;
import mx.erickb.shipping.service.ShippingService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ShippingControllerTest {

    @MockBean
    ShippingService shippingService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getPackageTypesShouldReturnPackages() throws Exception {
        List<String> types = new ArrayList<>();
        when(shippingService.getPackageTypes()).thenReturn(types);

        mockMvc.perform(get("/types"))
                .andExpect(status().isOk());
    }

    @Test
    public void getPackageTypesShouldThrowInvalidResponseException() throws Exception {
        when(shippingService.getPackageTypes()).thenThrow(InvalidResponseException.class);

        mockMvc.perform(get("/types"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void getTransportVelocitiesShouldReturnVelocities() throws Exception {
        List<String> types = new ArrayList<>();
        when(shippingService.getTransportVelocities(anyString())).thenReturn(types);

        mockMvc.perform(get("/velocity/testTransportType"))
                .andExpect(status().isOk());
    }

    @Test
    public void getPackageSizesShouldReturnSizes() throws Exception {
        List<String> sizes = new ArrayList<>();
        when(shippingService.getPackageSizes(anyString())).thenReturn(sizes);

        mockMvc.perform(get("/sizes/testPackageType"))
                .andExpect(status().isOk());
    }

    @Test
    public void getTransportTypesShouldReturnTransports() throws Exception {
        List<String> types = new ArrayList<>();
        when(shippingService.getTransportTypes(anyString())).thenReturn(types);

        mockMvc.perform(get("/transport/testPackageSize"))
                .andExpect(status().isOk());
    }

    @Test
    public void getTransportVelocitiesShouldThrowInvalidResponseException() throws Exception {
        when(shippingService.getTransportVelocities(anyString()))
                .thenThrow(InvalidResponseException.class);

        mockMvc.perform(get("/velocity/testTransportType"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void getPackageSizesShouldThrowInvalidResponseException() throws Exception {
        when(shippingService.getPackageSizes(anyString()))
                .thenThrow(InvalidResponseException.class);

        mockMvc.perform(get("/sizes/testPackageType"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void getTransportTypesShouldThrowInvalidResponseException() throws Exception {
        when(shippingService.getTransportTypes(anyString()))
                .thenThrow(InvalidResponseException.class);

        mockMvc.perform(get("/transport/testPackageSize"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void getCitiesShouldReturnCities() throws Exception {
        List<String> cities = new ArrayList<>();
        when(shippingService.getCities()).thenReturn(cities);

        mockMvc.perform(get("/city"))
                .andExpect(status().isOk());
    }

    @Test
    public void getCitiesShouldThrowInvalidResponseException() throws Exception {
        when(shippingService.getCities())
                .thenThrow(InvalidResponseException.class);

        mockMvc.perform(get("/city"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void getRouteShouldReturnRoute() throws Exception {
        String mockRoute = "test → test";
        when(shippingService.getRoute(anyString(), anyString())).thenReturn(mockRoute);

        String data = new JSONObject().put("origin", "testCity1")
                .put("destination", "testCity2").toString();
        mockMvc.perform(post("/route")
                .contentType(MediaType.APPLICATION_JSON)
                .content(data))
                .andExpect(status().isOk());
    }

    @Test
    public void getRouteShouldThrowInvalidResponseException() throws Exception {
        when(shippingService.getRoute(anyString(), anyString()))
                .thenThrow(InvalidResponseException.class);

        String data = new JSONObject().put("origin", "testCity1")
                .put("destination", "testCity2").toString();
        mockMvc.perform(post("/route")
                .contentType(MediaType.APPLICATION_JSON)
                .content(data))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void getRouteShouldThrowInvalidRequestException() throws Exception {
        when(shippingService.getRoute(anyString(), anyString()))
                .thenThrow(InvalidRequestException.class);

        String data = new JSONObject().put("origin", "testCity")
                .put("destination", "testCity").toString();
        mockMvc.perform(post("/route")
                .contentType(MediaType.APPLICATION_JSON)
                .content(data))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getRouteShouldThrowNotFoundException() throws Exception {
        when(shippingService.getRoute(anyString(), anyString()))
                .thenThrow(NotFoundException.class);

        String data = new JSONObject().put("origin", "testCity1")
                .put("destination", "testCity2").toString();
        mockMvc.perform(post("/route")
                .contentType(MediaType.APPLICATION_JSON)
                .content(data))
                .andExpect(status().isNotFound());
    }
}
