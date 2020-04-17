package io.digital.supercharger.interceptor;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class HeaderRequestInterceptor implements ClientHttpRequestInterceptor {

    private static final String ACCEPT_KEY = "Accept";
    private static final String ACCEPT_VALUE = "application/json";
    private static final String FC_SOURCE_KEY = "Fc-Source";
    private static final String FC_SOURCE_VALUE = "EBANKING";
    private static final String FC_USER_ID_KEY = "Fc-UserId";
    private static final String FC_USER_ID_VALUE = "EBANKING";

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        request.getHeaders().set(ACCEPT_KEY, ACCEPT_VALUE);
        request.getHeaders().set(FC_SOURCE_KEY, FC_SOURCE_VALUE);
        request.getHeaders().set(FC_USER_ID_KEY, FC_USER_ID_VALUE);

        return execution.execute(request, body);

    }

}
