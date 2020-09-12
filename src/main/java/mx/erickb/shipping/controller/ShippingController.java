package mx.erickb.shipping.controller;

import mx.erickb.shipping.exception.InvalidRequestException;
import mx.erickb.shipping.exception.InvalidResponseException;
import mx.erickb.shipping.exception.NotFoundException;
import mx.erickb.shipping.model.ErrorResponse;
import mx.erickb.shipping.model.RouteRequest;
import mx.erickb.shipping.service.IShippingService;
import mx.erickb.shipping.service.ShippingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

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

    @GetMapping(path = "velocity/{transport}", produces = APPLICATION_JSON_VALUE)
    public List<String> getTransportVelocities(@PathVariable("transport") String transportType) throws InvalidResponseException {
        return service.getTransportVelocities(transportType);
    }

    @GetMapping(path = "sizes/{type}", produces = APPLICATION_JSON_VALUE)
    public List<String> getPackageSizes(@PathVariable("type") String packageType) throws InvalidResponseException {
        return service.getPackageSizes(packageType);
    }

    @GetMapping(path = "transport/{size}", produces = APPLICATION_JSON_VALUE)
    public List<String> getTransportTypes(@PathVariable("size") String packageSize) throws InvalidResponseException {
        return service.getTransportTypes(packageSize);
    }

    @GetMapping(path = "city", produces = APPLICATION_JSON_VALUE)
    public List<String> getCities() throws InvalidResponseException {
        return service.getCities();
    }

    @PostMapping(path = "route", produces = TEXT_PLAIN_VALUE)
    public String getRoute(@RequestBody RouteRequest request) throws InvalidResponseException, InvalidRequestException, NotFoundException {
        return service.getRoute(request.getOrigin(), request.getDestination());
    }

    @ExceptionHandler(InvalidResponseException.class)
    public ResponseEntity<ErrorResponse> handleInvalidResponseException(InvalidResponseException exception) {
        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestException(InvalidRequestException exception) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
