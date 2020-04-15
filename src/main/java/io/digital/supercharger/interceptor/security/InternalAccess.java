package io.digital.supercharger.interceptor.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker interface to specify that a particular controller method will be invoked internally
 * by other microservices, rather by the end user. This gives a clear marker to developer, but
 * also provide a mechanism where these methods can be protected by Basic Auth,
 * preventing someone with internal access on the Kubernetes cluster from invoking these without
 * the correct credentials
 *
 * This interface is used by the AuthenticationInterceptor class.
 *
 * @see io.digital.supercharger.interceptor.security.AuthenticationInterceptor
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InternalAccess {

    /**
     * (Optional) Indicate if authentication is required for accessing this internal API.
     * If set to the true then basic auth username/password property keys need to be provided
     * @return a boolean to indicate if authentication is required for this method
     */
    boolean authRequired() default false;


    /**
     * The Basic auth username property, required if the authRequired is set to true.
     */
    String usernameProp() default "";

    /**
     * The Basic auth password property, required if the authRequired is set to true.
     */
    String passProp() default "";

}
