package com.accenture.sms.swagger;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Import(BeanValidatorPluginsConfiguration.class)
@Configuration
public class Config {

    @Bean
    public Docket swaggerConfiguration() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.accenture.sms.subscription"))
                .paths(PathSelectors.ant("/error").negate())
                .build()
                .apiInfo(apiInfo());
        docket.useDefaultResponseMessages(false);
        return appendTags(docket);
    }

    private Docket appendTags(Docket docket) {
        return docket.tags(
                new Tag(DescriptionVariables.SUBSCRIPTION_INFO_CONTROLLER,
                        "Controller for managing subscriptions"),
                new Tag(DescriptionVariables.SUBSCRIPTION_INFO_MODEL,
                        "Model representing subscription information")
        );
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Subscription Management System API")
                .description("Accenture Subscription Management System API")
                .version("1.0")
                .build();
    }
}
