package io.digital.supercharger.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Getter
public class CommonConfig implements Serializable {

    private static final long serialVersionUID = -2880227647420744510L;

    private static final String LOCAL_PROFILE = "local";
    private static final String DEV_PROFILE = "dev";
    public static final String NAME = "config";

    @Value("${spring.profiles.active:Unknown}")
    private String activeProfile;

    @Value("${microservice.auth.base.url:}")
    private String authServiceBaseUrl;
    @Value("${microservice.auth.auth.path:/api/auth}")
    private String authServiceAuthPath;
    @Value("${microservice.auth.customer.path:/api/auth/customer}")
    private String authServiceCustomerPath;
    @Value("${microservice.esb.user:}")
    private String esbUsername;
    @Value("${microservice.esb.pass:}")
    private String esbPassword;
    @Value("${microservice.esb.url:}")
    private String esbUrl;
    @Value("${microservice.internal.request.log:true}")
    private boolean internalRequestLog;

    public String getAuthServiceAuthUrl() {
        return authServiceBaseUrl + authServiceAuthPath;
    }

    public String getAuthServiceCustomerUrl() {
        return authServiceBaseUrl + authServiceCustomerPath;
    }

    /**
     * Utility to check if currently active profile is local
     *
     * @return true if is local
     */
    public boolean isLocalProfile() {
        return LOCAL_PROFILE.equalsIgnoreCase(activeProfile);
    }

    /**
     * Utility to check if currently active profile is dev
     *
     * @return true if is dev
     */
    public boolean isDevProfile() {
        return DEV_PROFILE.equalsIgnoreCase(activeProfile);
    }
}
