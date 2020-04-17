package io.digital.supercharger.interceptor.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker interface to de-note that authentication is required for a particular
 * controller method. This interface is used by the AuthenticationInterceptor class.
 * This interface provides support for optionally indicating if the customer profile
 * should be returned and if the authenticate request is made for an async call to not refresh
 * the auth token.
 * @see AuthenticationInterceptor
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginRequired {

    /**
     * In addition to authenticating the token, specify if the customer profile should also
     * be returned and injected as a request attribute
     * @return a boolean to indicate if the customer profile should also be returned or not
     */
    boolean profile() default false;

    /**
     * Specify if the token should not be refreshed for this request, indicating that multiple
     * Asynchronous requests are made in parallel
     * @return a boolean to indicate if the token should be refreshed or not
     */
    boolean async() default false;
}
