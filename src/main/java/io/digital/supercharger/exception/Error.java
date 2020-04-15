package io.digital.supercharger.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The type Error.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Error {

    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    /**
     * Instantiates a new Error.
     *
     * @param object  the object
     * @param message the message
     */
    public Error(String object, String message) {
        this.object = object;
        this.message = message;
    }
}
