package io.digital.supercharger.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Invalid credentials")
@Log4j2
public class PermissionException extends Exception {

    private static final long serialVersionUID = 8121725435940351260L;

    public PermissionException(String message) {
        super(message);
    }

    /**
     * Allows logging on the server without exposing the message
     *
     * @param message     Message to be shown with the error
     * @param internalLog Logging on the server
     */
    public PermissionException(String message, String internalLog) {
        super(message);
        if (!StringUtils.isEmpty(internalLog)) {
            log.warn(internalLog);
        }
    }
}

