package mx.erickb.shipping.controller;

import mx.erickb.shipping.exception.InvalidResponseException;
import mx.erickb.shipping.model.PackageType;
import mx.erickb.shipping.service.ShippingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ShippingControllerTest {

    @MockBean
    ShippingService shippingService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getPackageTypes_shouldReturnsPackages() throws Exception {
        List<PackageType> types = new ArrayList<>();
        when(shippingService.getPackageTypes()).thenReturn(types);

        mockMvc.perform(get("/types"))
                .andExpect(status().isOk());
    }

    @Test
    public void getPackageTypes_shouldThrowInvalidResponseException() throws Exception {
        when(shippingService.getPackageTypes()).thenThrow(InvalidResponseException.class);

        mockMvc.perform(get("/types"))
                .andExpect(status().isInternalServerError());
    }
}
