package mx.erickb.shipping.controller;

import mx.erickb.shipping.exception.InvalidResponseException;
import mx.erickb.shipping.service.ShippingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
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
    public void getPackageSizesShouldReturnSizes() throws Exception {
        List<String> sizes = new ArrayList<>();
        when(shippingService.getPackageSizes("testType")).thenReturn(sizes);

        mockMvc.perform(get("/sizes/testType"))
                .andExpect(status().isOk());
    }

    @Test
    public void getPackageSizesShouldThrowInvalidResponseException() throws Exception {
        when(shippingService.getPackageSizes(anyString())).thenThrow(InvalidResponseException.class);

        mockMvc.perform(get("/sizes/test"))
                .andExpect(status().isInternalServerError());
    }
}
