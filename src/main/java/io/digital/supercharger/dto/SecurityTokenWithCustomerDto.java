package io.digital.supercharger.dto;

import io.digital.supercharger.model.CustomerProfileDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SecurityTokenWithCustomerDto {
    private SecurityTokenDto securityToken;
    private CustomerProfileDto customerProfile;
}
