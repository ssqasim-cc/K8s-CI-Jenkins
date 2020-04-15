package io.digital.supercharger.interceptor.security;

import io.digital.supercharger.dto.SecurityTokenDto;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * When the controller finishes and returns a response, a Controller Advice (class that implements ResponseBodyAdvice),
 * will get the http servlet request part of Server Request, here we add the token. AuthenticationInterceptor postHandle
 * doesn't work because the response has already been handled by HttpMessageConverter.
 * Support could return returnType.getExecutable().getAnnotation(LoginRequired.class) != null, but this doesn't work
 *  in case of handling exceptions
 *
 * @see
 * <a href="https://stackoverflow.com/questions/48823794/spring-interceptor-doesnt-add-header-to-restcontroller-services">
 *     Spring Interceptor doesn't add header to rest controller
 * </a>
 * @see
 * <a href="https://stackoverflow.com/questions/46227751/spring-controllers-adding-a-response-header-parameter-called-elapsed-time">
 *     Spring adding a response header parameter
 * </a>
 *
 */
@ControllerAdvice
public class TokenResponseAdvice implements ResponseBodyAdvice<Object> {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,ServerHttpResponse response) {

        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        SecurityTokenDto securityToken =
                (SecurityTokenDto) servletRequest.getServletRequest().getAttribute(
                        AuthenticationInterceptor.REQUEST_ATTRIBUTE_SECURITY_TOKEN);

        if (securityToken != null) {
            if (body instanceof ResponseEntity) {
                ResponseEntity responseEntity = (ResponseEntity) body;
                responseEntity.getHeaders().add(SecurityTokenDto.X_DIGITAL_TOKEN,
                        securityToken.getSecret());

            } else {
                response.getHeaders().add(SecurityTokenDto.X_DIGITAL_TOKEN, securityToken.getSecret());
            }

        }

        return body;
    }

}
