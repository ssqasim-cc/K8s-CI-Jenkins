package io.digital.supercharger.exception;

import io.digital.supercharger.api.CommonResponse;

public class IntegrationServiceException extends Exception {

    private final CommonResponse<Void> errorResponse;
    private final boolean isEsbError;

    /**
     *
     */
    private static final long serialVersionUID = -6018987325651356796L;

    /**
     * Constructor to handle exceptions that are not of type
     * HttpClientErrorException
     *
     * @param e Throwable cause of the exception
     */
    public IntegrationServiceException(Exception e) {
        super(e);
        this.isEsbError = false;
        this.errorResponse = null;
    }

    /**
     * Constructor for error response object in case of HttpClientErrorException
     *
     * @param errorResponse CommonResponse object containing the error body
     *                      information
     */
    public IntegrationServiceException(CommonResponse<Void> errorResponse) {
        super(errorResponse.getErrorMessage());
        this.errorResponse = errorResponse;
        this.isEsbError = true;
    }

    public CommonResponse<Void> getErrorResponse() {
        return this.errorResponse;
    }

    public boolean isEsbError() {
        return this.isEsbError;
    }

}
