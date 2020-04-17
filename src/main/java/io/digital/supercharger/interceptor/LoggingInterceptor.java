package io.digital.supercharger.interceptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Log log = LogFactory.getLog(LoggingInterceptor.class);

    private boolean logRequest;
    private boolean logResponse;
    private String context;

    /**
     * Constructor for loggingInterceptor
     *
     * @param context     of request
     * @param logRequest  to enable logging of request
     * @param logResponse to enable logging of response
     */
    public LoggingInterceptor(String context, boolean logRequest, boolean logResponse) {
        super();
        this.logRequest = logRequest;
        this.logResponse = logResponse;
        this.context = context;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
        throws IOException {

        if (this.logRequest) {
            traceRequest(request, body);
        }

        ClientHttpResponse response = execution.execute(request, body);

        if (this.logResponse) {
            traceResponse(response);
        }

        return response;
    }

    private void traceRequest(HttpRequest request, byte[] body) {

        // process sensitive data if needed

        final String reqBody = this.replaceSensitiveData(new String(body, StandardCharsets.UTF_8));

        // remove the Authorization header
        Map<String, String> headers = request.getHeaders().toSingleValueMap();
        headers.remove("Authorization");

        log.info("===========================" + context
            + " request begin================================================");
        log.info("URI         : " + request.getURI());
        log.info("Method      : " + request.getMethod());
        log.info("Headers     : " + headers);
        log.info("Request body: " + reqBody);
        log.info("===========================" + context
            + " request end==================================================");
    }

    private void traceResponse(ClientHttpResponse response) {

        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(response.getBody(), Charset.forName("UTF-8")))) {

            log.info("============================" + context
                + " response begin==========================================");
            log.info("Status code  : " + response.getStatusCode());
            log.info("Status text  : " + response.getStatusText());
            log.info("Headers      : " + response.getHeaders());

            String body = br.lines().parallel().collect(Collectors.joining("\n"));

            log.info("Response body: " + body);

        } catch (Exception e) {
            log.error("An error occurred when printing the response: " + e.getMessage());
        }

        log.info(
            "============================" + context + " response end============================================");

    }

    protected String replaceSensitiveData(String reqBody) {
        return reqBody;
    }

}
