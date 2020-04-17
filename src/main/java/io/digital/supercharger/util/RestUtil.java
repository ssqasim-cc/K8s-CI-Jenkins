package io.digital.supercharger.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic REST utilities such as Auth and Headers
 */
public class RestUtil {

    public static final String AUTH_HEADER_BEARER = "Bearer ";

    /**
     * Private Default constructor to prevent initialization from this class
     */
    private RestUtil() {
        super();
    }

    /**
     * Add Basic authentication credentials to restTemplate.
     *
     * @param restTemplate for which auth to be added
     * @param username for the basic auth
     * @param password for the basic auth
     */
    public static void addAuthentication(RestTemplate restTemplate, String username, String password) {
        if (username == null) {
            return;
        }
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();

        interceptors = new ArrayList<>(interceptors);
        interceptors.removeIf(requestInterceptor -> requestInterceptor instanceof BasicAuthenticationInterceptor);

        interceptors.add(new BasicAuthenticationInterceptor(username, password));
        restTemplate.setInterceptors(interceptors);
    }

    /**
     * Return an authorization header token without the Bearer prefix
     * @param authenticationToken to put in the header
     * @return the full header with the "Bearer " prefix
     */
    public static String getAuthorizationTokenWithoutBearer(String authenticationToken) {
        return authenticationToken.replaceFirst(AUTH_HEADER_BEARER, "").trim();
    }

    /**
     * Return the http entity for the provided request
     * @param request - generic type for the request object
     * @return HttpEntity with the auth header connected
     */
    public static <T> HttpEntity<T> getHttpEntity(T request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(request, headers);
    }

    /**
     * Return the http entity with bearer token for the provided request
     * @param request - generic type for the request object
     * @return HttpEntity with the auth header connected
     */
    public static <T> HttpEntity<T> getHttpEntityWithBearerToken(T request, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, AUTH_HEADER_BEARER + accessToken);
        return new HttpEntity<>(request, headers);
    }
}
