package StreamKeeper_Spring_Java.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Custom exception class for handling API errors.
 */
public class CustomException extends RuntimeException {

    private HttpStatus status;

    public CustomException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    // Getters and Setters

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
