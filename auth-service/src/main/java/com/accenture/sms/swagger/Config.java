package com.accenture.sms.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@Import(BeanValidatorPluginsConfiguration.class)
public class Config {

    @Bean
    public Docket swaggerConfiguration() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.accenture.sms.controllers"))
                .paths(PathSelectors.ant("/auth/**"))
                .build()
                .apiInfo(apiInfo());
        docket.useDefaultResponseMessages(false);
        return appendTags(docket);
    }

    private Docket appendTags(Docket docket) {
        return docket.tags(
                new Tag(DescriptionVariables.AUTH_CONTROLLER,
                        "Controller for managing authentication"),
                new Tag(DescriptionVariables.LOGIN_REQUEST_MODEL,
                        "Model for user login requests"),
                new Tag(DescriptionVariables.USER_INFO_MODEL,
                        "Model representing user information")
        );
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Authentication Management API")
                .description("API for user authentication management")
                .version("1.0")
                .build();
    }
}
