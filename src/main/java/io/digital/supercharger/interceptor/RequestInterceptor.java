package io.digital.supercharger.interceptor;

import io.digital.supercharger.dto.RequestDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Log4j2
public class RequestInterceptor extends HandlerInterceptorAdapter {

    public static final String REQUEST_ATTRIBUTE =
        "io.digital.supercharger.interceptor.request";
    public static final String ENGLISH_LANGUAGE_KEY = "en";
    public static final String ARABIC_LANGUAGE_KEY = "ar";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        final RequestDto requestDto = new RequestDto();

        final Locale locale = getLocale(request.getHeader("Accept-Language"));
        final String channelId = request.getHeader("channelId");
        String channelType = request.getHeader("channeltype");
        final String userAgent = request.getHeader("User-Agent") != null
            ? request.getHeader("User-Agent").toLowerCase() : "";
        final String version = request.getHeader("App-Version") != null
            ? request.getHeader("App-Version").toLowerCase() : "";

        if (userAgent != null && channelType == null) {
            if (userAgent.toLowerCase().contains("msie")) {
                channelType = "IE";
            } else if (userAgent.toLowerCase().contains("chrome")) {
                channelType = "Chrome";
            } else if (userAgent.toLowerCase().contains("firefox")) {
                channelType = "Firefox";
            } else if (userAgent.toLowerCase().contains("opera")) {
                channelType = "opera";
            } else if (userAgent.toLowerCase().contains("iphone")) {
                channelType = "iPhone";
            } else if (userAgent.toLowerCase().contains("ipad")) {
                channelType = "iPad";
            } else if (userAgent.toLowerCase().contains("safari")
                && !userAgent.toLowerCase().contains("mobile")) {
                channelType = "Safari";
            } else if (userAgent.toLowerCase().contains("android")) {
                channelType = "Android";
            } else {
                channelType = "other";
            }
        }

        requestDto.setUserAgent(userAgent);
        requestDto.setChannelId(channelId);
        requestDto.setChannelType(channelType);
        requestDto.setLocale(locale);
        requestDto.setVersion(version);

        request.setAttribute(REQUEST_ATTRIBUTE, requestDto);
        return true;
    }

    private Locale getLocale(String lang) {
        if (lang == null || lang.isEmpty()) {
            return new Locale(ENGLISH_LANGUAGE_KEY);

        } else if (lang.equalsIgnoreCase(ARABIC_LANGUAGE_KEY)) {
            return new Locale(lang);

        } else {
            return new Locale(ENGLISH_LANGUAGE_KEY);
        }
    }
}
