package io.digital.supercharger.interceptor.security;

import io.digital.supercharger.config.CommonConfig;
import io.digital.supercharger.dto.SecurityTokenDto;
import io.digital.supercharger.dto.SecurityTokenWithCustomerDto;
import io.digital.supercharger.exception.PermissionException;
import io.digital.supercharger.service.CustomerProfileService;
import io.digital.supercharger.util.RestUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This Request Interceptor will validate the IB authentication token and loads the customer
 * profile
 *
 */
@Log4j2
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    public static final String REQUEST_ATTRIBUTE_CUSTOMER_PROFILE =
        "io.digital.supercharger.interceptor.security.customerProfile";

    public static final String REQUEST_ATTRIBUTE_SECURITY_TOKEN =
        "io.digital.supercharger.interceptor.security.securityToken";

    private static final String REQUEST_ATTRIBUTE_STOPWATCH =
        "io.digital.supercharger.interceptor.security.stopwatch";

    @Autowired
    private CommonConfig commonConfig;

    @Autowired
    private CustomerProfileService customerProfileService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {


        LoginRequired login = getLoginAnnotation(handler);
        if (login == null) {
            return true;
        }

        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isEmpty(accessToken) && commonConfig.isLocalProfile()) {
            accessToken = "Bearer dummy-access-token";
        }

        if (StringUtils.isEmpty(accessToken)) {
            log.warn("Missing access token");
            throw new PermissionException("missing access token");
        }

        // get the token without the bearer header
        accessToken = RestUtil.getAuthorizationTokenWithoutBearer(accessToken);

        try {
            final StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            // authenticate and retrieve both the token and profile
            SecurityTokenWithCustomerDto tokenAndProfile = customerProfileService
                .authenticateWithCustomer(accessToken, login.async());

            if (tokenAndProfile == null) {
                log.warn("Customer profile is not found");
                throw new PermissionException("Customer profile is not found");
            }

            request.setAttribute(REQUEST_ATTRIBUTE_CUSTOMER_PROFILE, tokenAndProfile.getCustomerProfile());
            request.setAttribute(REQUEST_ATTRIBUTE_SECURITY_TOKEN, tokenAndProfile.getSecurityToken());
            request.setAttribute(REQUEST_ATTRIBUTE_STOPWATCH, stopWatch);

        } catch (Exception ex) {
            log.warn("Unable to load customer profile", ex);
            throw new PermissionException("Unable to load customer profile");

        }

        return super.preHandle(request, response, handler);
    }

    /**
     * Add the custom header with the refreshed token
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) {

        SecurityTokenDto securityToken =
            (SecurityTokenDto) request.getAttribute(REQUEST_ATTRIBUTE_SECURITY_TOKEN);

        String username = "NOT_FOUND";
        if (securityToken != null) {
            response.setHeader(SecurityTokenDto.X_DIGITAL_TOKEN, securityToken.getSecret());
            username = securityToken.getUsername();
        }

        StopWatch stopWatch = (StopWatch) request.getAttribute(REQUEST_ATTRIBUTE_STOPWATCH);
        if (stopWatch != null && stopWatch.isRunning()) {
            stopWatch.stop();
            log.info("Auth after completion | url: {} | username: {} "
                    + "| method: {} | status: {} | Total: {} seconds",
                request.getRequestURI(),
                username,
                request.getMethod(),
                response.getStatus(),
                stopWatch.getTotalTimeSeconds());
        }

    }

    /**
     * Get the LoginREquired handler if present
     *
     * @param handler - the controller handler
     * @return LoginRequired annotation if present, otherwise null
     */
    private LoginRequired getLoginAnnotation(Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return null;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        return handlerMethod.getMethod().getAnnotation(LoginRequired.class);
    }

}
