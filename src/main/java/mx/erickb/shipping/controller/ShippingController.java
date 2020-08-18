package mx.erickb.shipping.controller;

import mx.erickb.shipping.exception.InvalidResponseException;
import mx.erickb.shipping.model.ErrorResponse;
import mx.erickb.shipping.service.IShippingService;
import mx.erickb.shipping.service.ShippingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@CrossOrigin
public class ShippingController {

    private final IShippingService service;

    ShippingController(final ShippingService service) {
        this.service = service;
    }

    @GetMapping(path = "types", produces = APPLICATION_JSON_VALUE)
    public List<String> getPackageTypes() throws InvalidResponseException {
        return service.getPackageTypes();
    }

    @ExceptionHandler(InvalidResponseException.class)
    public ResponseEntity<ErrorResponse> handleException(InvalidResponseException exception) {
        ErrorResponse error = new ErrorResponse("Invalid server response", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
