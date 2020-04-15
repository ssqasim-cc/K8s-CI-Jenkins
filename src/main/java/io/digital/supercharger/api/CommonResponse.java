package io.digital.supercharger.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6908289071006886068L;

    @JsonProperty("date")
    private String date;
    @JsonProperty("response_status")
    private String responseStatus;
    @JsonProperty("error_message")
    private String errorMessage;
    @JsonProperty("error_code")
    private String errorCode;
    @JsonProperty("response_body")
    private transient T responseBody;

}
