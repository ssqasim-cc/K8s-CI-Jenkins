package io.digital.supercharger.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.digital.supercharger.util.LocalDateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class CustomerProfileDto {
    private Long id;
    private String username;
    private Long customerIdentityNumber;
    private String nationalityId;
    private String genderId;
    @JsonSerialize(using = LocalDateUtil.Serializer.class)
    @JsonDeserialize(using = LocalDateUtil.Deserializer.class)
    private LocalDate dateOfBirth;
    private String mobileNumber;
    private String fullName;
}

