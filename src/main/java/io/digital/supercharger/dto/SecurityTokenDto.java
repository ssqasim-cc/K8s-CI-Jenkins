package io.digital.supercharger.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.digital.supercharger.util.LocalDateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecurityTokenDto {

    public static final String X_DIGITAL_TOKEN = "X-DIGITAL-TOKEN";

    private String username;
    private String secret;
    @JsonSerialize(using = LocalDateTimeUtil.Serializer.class)
    @JsonDeserialize(using = LocalDateTimeUtil.Deserializer.class)
    private LocalDateTime createdDate;
    private Boolean isClean;
    private Boolean isStrong;
    private Boolean isAuthVerified;

    /**
     * Build the token for the controller in Authorization header
     *
     * @return Authorization HttpHeaders
     */
    @JsonIgnore
    public HttpHeaders getHeaderToken() {
        final HttpHeaders responseHeaders = new HttpHeaders();
        if (!StringUtils.isEmpty(this.secret)) {
            responseHeaders.add(X_DIGITAL_TOKEN, this.secret);
        }
        return responseHeaders;
    }
}
