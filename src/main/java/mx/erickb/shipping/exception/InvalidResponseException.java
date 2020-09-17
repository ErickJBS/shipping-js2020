package mx.erickb.shipping.exception;

public class InvalidResponseException extends RuntimeException {
    public InvalidResponseException(String message) {
        super(message);
    }
}
