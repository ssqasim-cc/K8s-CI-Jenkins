package io.digital.supercharger.service;

import io.digital.supercharger.config.CommonConfig;
import io.digital.supercharger.dto.SecurityTokenDto;
import io.digital.supercharger.dto.SecurityTokenWithCustomerDto;
import io.digital.supercharger.util.RestUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Customer profile service is responsible for retrieving a customer profile
 * from the Auth microservice. The customer is retrieved using an authentication
 * token
 *
 */
@Service("customerProfileService")
@Log4j2
public class RestCustomerProfileService implements CustomerProfileService {

    public static final String ASYNC_PARAM = "?async=";

    @Qualifier("genericRestTemplate")
    private RestTemplate restTemplate;

    private CommonConfig configuration;

    @Autowired
    public RestCustomerProfileService(RestTemplate restTemplate, CommonConfig configuration) {
        this.restTemplate = restTemplate;
        this.configuration = configuration;
    }

    @Override
    public SecurityTokenDto authenticate(String token, boolean async) {
        log.debug("Authenticating user...");
        ResponseEntity<SecurityTokenDto> response = restTemplate
            .exchange(configuration.getAuthServiceAuthUrl() + ASYNC_PARAM + async,
                HttpMethod.GET,
                RestUtil.getHttpEntityWithBearerToken(null, token),
                SecurityTokenDto.class);

        return response.getBody();
    }

    @Override
    public SecurityTokenWithCustomerDto authenticateWithCustomer(String token, boolean async) {

        log.debug("loading customer profile info...");
        ResponseEntity<SecurityTokenWithCustomerDto> response = restTemplate
            .exchange(configuration.getAuthServiceCustomerUrl() + ASYNC_PARAM + async,
                HttpMethod.GET,
                RestUtil.getHttpEntityWithBearerToken(null, token),
                SecurityTokenWithCustomerDto.class);
        return response.getBody();
    }

}
