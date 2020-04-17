package io.digital.supercharger.exception;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The type Api error.
 */
@Getter
@Setter
@NoArgsConstructor
public class ApiError {

    @ApiModelProperty(
        value = "The HTTP status",
        example = "404",
        notes = "The HTTP status associated with this error response",
        required = true)
    private HttpStatus status;

    @ApiModelProperty(
        value = "The error message",
        example = "An error has occurred.",
        notes = "A short error message describing what went wrong",
        required = true)
    private String message;

    @ApiModelProperty(
        value = "The debug error message",
        example = "An error has occurred.",
        notes = "A detailed error message describing what went wrong")
    private String debugMessage;

    @ApiModelProperty(
        value = "The Internal ERROR Code",
        example = "JAM-ERR-1",
        notes = "The Error codes")
    private Enum errorCode;

    @ApiModelProperty(value = "The validation errors")
    private List<Error> errors;

    /**
     * Instantiates a new Api error.
     *
     * @param status the status
     */
    public ApiError(HttpStatus status) {
        this.status = status;
    }

    /**
     * Instantiates a new Api error.
     *
     * @param status  the status
     * @param message the message
     */
    public ApiError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * @param status    HTTP status
     * @param message   The Error Message
     * @param errorCode The Error Code
     */
    public ApiError(HttpStatus status, String message, Enum errorCode) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }


    /**
     * @param status - HTTP status
     * @param ex     - the thrown exception
     */
    public ApiError(HttpStatus status, Throwable ex) {
        this.status = status;
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    /**
     * @param status  - HTTP status
     * @param message - the error message
     * @param ex      - the thrown exception
     */
    public ApiError(HttpStatus status, String message, Throwable ex) {
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    public void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(this::addValidationError);
    }

    public void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
        constraintViolations.forEach(this::addValidationError);
    }

    public void addValidationError(List<ObjectError> globalErrors) {
        globalErrors.forEach(this::addValidationError);
    }

    private void addValidationError(String object, String field, Object rejectedValue, String message) {
        addError(new Error(object, field, rejectedValue, message));
    }

    private void addValidationError(String object, String message) {
        addError(new Error(object, message));
    }

    private void addValidationError(FieldError fieldError) {
        this.addValidationError(
            fieldError.getObjectName(),
            fieldError.getField(),
            fieldError.getRejectedValue(),
            fieldError.getDefaultMessage());
    }

    private void addValidationError(ObjectError objectError) {
        this.addValidationError(
            objectError.getObjectName(),
            objectError.getDefaultMessage());
    }

    /**
     * Utility method for adding error of ConstraintViolation. Usually when a @Validated validation fails.
     *
     * @param cv the ConstraintViolation
     */
    private void addValidationError(ConstraintViolation<?> cv) {
        this.addValidationError(
            cv.getRootBeanClass().getSimpleName(),
            ((PathImpl) cv.getPropertyPath()).getLeafNode().asString(),
            cv.getInvalidValue(),
            cv.getMessage());
    }

    private void addError(Error error) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(error);
    }
}
