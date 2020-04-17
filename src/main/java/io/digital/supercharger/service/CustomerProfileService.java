package io.digital.supercharger.service;

import io.digital.supercharger.dto.SecurityTokenDto;
import io.digital.supercharger.dto.SecurityTokenWithCustomerDto;

/**
 * Authenticate a customer and retrieve their profile.
 *
 */
public interface CustomerProfileService {

    /**
     * Authenticate the customer and return their profile and the new token
     *
     * @param token - Token
     * @param async the async
     * @return SecurityTokenWithCustomerDto security token with customer dto
     */
    SecurityTokenWithCustomerDto authenticateWithCustomer(String token, boolean async);

    /**
     * Authenticate the customer and return the new token only
     *
     * @param token - Token
     * @param async the async
     * @return SecurityTokenDto security token dto
     */
    SecurityTokenDto authenticate(String token, boolean async);

}
