package io.digital.supercharger.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.digital.supercharger.interceptor.LoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("Duplicates")
@Configuration
public class RestTemplateConfig {

    public static final long API_TIMEOUT_MILLI_SECONDS = TimeUnit.SECONDS.toMillis(60);

    @Autowired
    private CommonConfig commonConfig;

    /**
     *
     */
    @Bean
    @Primary
    public ObjectMapper nonNullObjectMapper() {
        return Jackson2ObjectMapperBuilder.json()
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();
    }

    /**
     * Method for REST template generation
     *
     * @param objectMapper the ObjectMapper use for converting objects
     * @return Generates REST template.
     */
    @Bean
    @Primary
    @Qualifier("genericRestTemplate")
    public RestTemplate genericRestTemplate(ObjectMapper objectMapper) {

        RestTemplate restTemplate = buildGenericRestTemplate(objectMapper);

        if (commonConfig.isInternalRequestLog()) {
            restTemplate.getInterceptors().add(new LoggingInterceptor("Internal", true, true) {

                @Override
                protected String replaceSensitiveData(String reqBody) {
                    // can use this to mask any sensitive data
                    return reqBody;
                }
            });
        }

        return restTemplate;
    }

    /**
     * Helper method to set common attributes between different REST templates producers.
     *
     * @param objectMapper the objectmapper use for converting objects
     * @return REST template with default configurations like (timeout, interceptors, converters ...etc)
     */
    private RestTemplate buildGenericRestTemplate(final ObjectMapper objectMapper) {

        SimpleClientHttpRequestFactory clientConfig = new SimpleClientHttpRequestFactory();
        clientConfig.setConnectTimeout((int) API_TIMEOUT_MILLI_SECONDS);
        clientConfig.setReadTimeout((int) API_TIMEOUT_MILLI_SECONDS);

        RestTemplate restTemplate = new RestTemplate(
            new BufferingClientHttpRequestFactory(clientConfig));

        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter(objectMapper));
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());

        return restTemplate;
    }

}
