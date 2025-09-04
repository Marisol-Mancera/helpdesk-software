package dev.marisol.helpdesk_software.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Request cannot be deleted while PENDING")
public class RequestConflictException extends RequestException {

    public RequestConflictException(String message) {
        super(message);
    }

    public RequestConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}

