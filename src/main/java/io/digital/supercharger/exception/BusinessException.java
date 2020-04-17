package io.digital.supercharger.exception;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;

@Getter
@Log4j2
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 8121725435940351260L;

    protected final Enum errorCode;

    public BusinessException(Enum errorCode) {
        super(getMessage(errorCode));
        this.errorCode = errorCode;
    }

    public BusinessException(Enum errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    private static String getMessage(Enum errorCode) {
        try {
            Field field = errorCode.getClass().getDeclaredField("message");
            field.setAccessible(true);
            return (String) field.get(errorCode);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("Exception not found a message in Enum {}", errorCode, e);
            return errorCode.name();
        }
    }
}
