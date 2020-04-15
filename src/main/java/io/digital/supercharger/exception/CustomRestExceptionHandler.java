package io.digital.supercharger.exception;

import io.digital.supercharger.util.JsonUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;


/**
 * A customer REST exception handler. Clients should extend this class and annotate the subclass with Spring
 * org.springframework.web.bind.annotation.ControllerAdvice annotation so that it's registered as an exception handler
 * for Controllers
 *
 * NB: This should be moved to a common library for reuse purposes by multiple microservices.
 *
 */
@Log4j2
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String ERROR_MESSAGE = "Handling exception";

    /**
     * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
     *
     * @param ex      HttpMediaTypeNotSupportedException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ErrorResponse object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
        HttpMediaTypeNotSupportedException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request) {
        log.error(ERROR_MESSAGE, ex);
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        return buildResponseEntity(new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
            builder.substring(0, builder.length() - 2), ex));
    }

    /**
     * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
     *
     * @param ex      the MethodArgumentNotValidException that is thrown when @Valid validation fails
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ErrorResponse object
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request) {
        log.error(ERROR_MESSAGE, ex);
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage("Validation error");
        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
        return buildResponseEntity(apiError);
    }

    /**
     * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
     *
     * @param ex      MissingServletRequestParameterException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ErrorResponse object
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        log.error(ERROR_MESSAGE, ex);
        String error = ex.getParameterName() + " parameter is missing";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }

    /**
     * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
     *
     * @param ex the ConstraintViolationException
     * @return the ErrorResponse object
     */
    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(javax.validation.ConstraintViolationException ex) {
        log.error(ERROR_MESSAGE, ex);
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage("Validation error");
        apiError.addValidationErrors(ex.getConstraintViolations());
        return buildResponseEntity(apiError);
    }

    /**
     * Handle Exception, handle generic Exception.class
     *
     * @param ex      the Exception
     * @param request WebRequest
     * @return the ErrorResponse object
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                      WebRequest request) {

        if (ex == null) {
            throw new IllegalArgumentException("Exception is required");
        }

        log.error(ERROR_MESSAGE, ex);

        String type = "";

        Class<?> typeClz = ex.getRequiredType();
        if (typeClz != null && typeClz.getSimpleName() != null) {
            type = typeClz.getSimpleName();
        }

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
            ex.getName(), ex.getValue(), type));
        apiError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Handle illegal argument exceptions
     *
     * @param e the exception
     * @return the response entity
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(ERROR_MESSAGE, e);
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, e.getMessage()));
    }


    /**
     * Handles when data not found in database
     *
     * @param ex exception
     * @return the ErrorResponse object
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(
        EntityNotFoundException ex) {
        log.error(ERROR_MESSAGE, ex);
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    /**
     * Handles permission errors.
     *
     * @param ex exception
     * @return the ErrorResponse object
     */
    @ExceptionHandler(PermissionException.class)
    public ResponseEntity<Object> handleMicroservicePermissionException(PermissionException ex) {
        log.warn(ERROR_MESSAGE, ex);
        return buildResponseEntity(new ApiError(HttpStatus.FORBIDDEN, ex.getMessage()));
    }

    /**
     * Build Exception response to catch all
     *
     * @param ex      exception
     * @param request WebRequest
     * @return formatted error of all exception with 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        log.error(ERROR_MESSAGE, ex);
        return buildResponseEntity(
            new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage()));
    }

    /**
     * Handle integration service exception
     *
     * @param e the exception
     * @return the response entity
     */
    @ExceptionHandler(IntegrationServiceException.class)
    public ResponseEntity<Object> handleIntegrationServiceException(IntegrationServiceException e) {
        log.error(ERROR_MESSAGE, e);
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    /**
     * Build the response entity
     *
     * @param apiError the error object
     * @return formatted error
     */
    protected ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    /**
     * Build the response entity
     *
     * @param e the error object
     * @return the Error Response
     */
    @ExceptionHandler({HttpClientErrorException.class})
    public ResponseEntity<Object> handleHttpClientException(HttpClientErrorException e)
        throws IOException {
        log.error(ERROR_MESSAGE, e);
        String httpExceptionBody = e.getResponseBodyAsString();
        if (StringUtils.isNotBlank(httpExceptionBody) && JsonUtil.isValidJsonSring(httpExceptionBody)) {
            try {
                return this.buildResponseEntity(JsonUtil.fromJson(httpExceptionBody, ApiError.class));
            } catch (Exception ex) {
                log.error("failed to parse error response for body {} ", httpExceptionBody);
                return this.buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Internal server error"));
            }
        } else {
            return this.buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error"));
        }
    }

    /**
     * Handle Saving exception exception
     *
     * @param e the exception
     * @return the response entity
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException e) {
        log.error("Handling saving Exception", e);
        return buildResponseEntity(new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e.getErrorCode()));
    }
}
