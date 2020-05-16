package org.promocat.promocat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.Collections;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 18:52 06.05.2020
 */
@Configuration
@EnableSwagger2WebMvc
@Import(SpringDataRestConfiguration.class)
public class SpringFoxConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/api/**"))
                .apis(RequestHandlerSelectors.basePackage("org.promocat.promocat.data_entities"))
                .build()
                .groupName("api")
                .apiInfo(apiDetails())
                .useDefaultResponseMessages(false);
    }

    @Bean
    public Docket auth() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/auth/**"))
                .apis(RequestHandlerSelectors.basePackage("org.promocat.promocat.data_entities"))
                .build()
                .groupName("auth")
                .apiInfo(apiDetails())
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiDetails() {
        return new ApiInfo(
                "PromoCat application API",
                "Amazing promo application",
                "1.0",
                "https://api.promocatcompany.com",
                new springfox.documentation.service.Contact("Alexandr", "https://api.promocatcompany.com",
                        "pavlishen4b@gmail.com"),
                "API License",
                "https://api.promocatcompany.com",
                Collections.emptyList()
        );
    }
}
