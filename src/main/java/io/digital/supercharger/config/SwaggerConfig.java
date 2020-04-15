package io.digital.supercharger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@Profile("!test")
@Configuration
@EnableSwagger2
@Import({springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class})
public class SwaggerConfig {
    /**
     * Api docket.
     *
     * @return the docket
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .globalOperationParameters(getGlobalParams())
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .paths(PathSelectors.regex("^(?!.*error).*$"))
            .paths(PathSelectors.regex("^(?!.*actuator).*$"))
            .build().forCodeGeneration(true);
    }

    private List<Parameter> getGlobalParams() {
        return Arrays.asList(new ParameterBuilder()
            .name(HttpHeaders.AUTHORIZATION)
            .description("Authentication Token")
            .modelRef(new ModelRef("string"))
            .parameterType("header")
            .required(false)
            .build());
    }

}
