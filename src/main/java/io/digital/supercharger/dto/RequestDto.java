package io.digital.supercharger.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Locale;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class RequestDto {
    private String managedServerName = null;
    private String browserType = null;
    private String userAgent = null;
    private String localServerAddress = null;
    private String localServerName = null;
    private int localPort;
    private String cookie = null;
    private String remoteIpAddress = null;
    private String channelId;
    private String channelType;
    private Locale locale;
    private String version;

}
