package org.promocat.promocat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
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

    public static final String USER = "User";
    public static final String COMPANY = "Company";
    public static final String CAR = "Car";
    public static final String STOCK = "Stock";
    public static final String CITY = "City";
    public static final String PROMO_CODE = "Promo-code";
    public static final String LOGIN = "Login";
    public static final String ADMIN = "Admin";
    public static final String STOCK_CITY = "Stock and city";
    public static final String TOKEN = "Token";
    public static final String MOVEMENT = "Movement";
    public static final String USER_BAN = "User ban";
    public static final String PARAMETERS = "Parameters";
    public static final String NEWS_FEED = "News feed";
    public static final String NOTIFNPD = "Notification npd";
    public static final String RECEIPT = "Receipt";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/api/**"))
                .apis(RequestHandlerSelectors.basePackage("org.promocat.promocat.data_entities"))
                .build()
                .groupName("api")
                .tags(new Tag(CAR, "Car controller"))
                .tags(new Tag(USER, "User controller"))
                .tags(new Tag(STOCK, "Stock controller"))
                .tags(new Tag(STOCK_CITY, "Stock and city controller"))
                .tags(new Tag(MOVEMENT, "Movement controller"))
                .tags(new Tag(NEWS_FEED, "News feed controller"))
                .tags(new Tag(NOTIFNPD, "Notification npd controller"))
                .apiInfo(apiDetails())
                .useDefaultResponseMessages(false);
    }

    @Bean
    public Docket auth() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/auth/**"))
                .apis(RequestHandlerSelectors.basePackage("org.promocat.promocat.data_entities")
                        .or(RequestHandlerSelectors.basePackage("org.promocat.promocat.util_entities")))
                .build()
                .groupName("auth")
                .tags(new Tag(CITY, "City controller"))
                .tags(new Tag(COMPANY, "Company controller"))
                .tags(new Tag(LOGIN, "Login controller"))
                .tags(new Tag(USER, "User controller"))
                .tags(new Tag(TOKEN, "Token controller"))
                .apiInfo(apiDetails())
                .useDefaultResponseMessages(false);
    }

    @Bean
    public Docket admin() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/admin/**"))
                .apis(RequestHandlerSelectors.basePackage("org.promocat.promocat.data_entities"))
                .build()
                .groupName("admin")
                .tags(new Tag(CITY, "City controller"))
                .tags(new Tag(COMPANY, "Company controller"))
                .tags(new Tag(CAR, "Car controller"))
                .tags(new Tag(USER, "User controller"))
                .tags(new Tag(PROMO_CODE, "Promo-code controller"))
                .tags(new Tag(STOCK, "Stock controller"))
                .tags(new Tag(ADMIN, "Admin controller"))
                .tags(new Tag(USER_BAN, "UserBan controller"))
                .tags(new Tag(PARAMETERS, "Parameters controller"))
                .tags(new Tag(NEWS_FEED, "News feed controller"))
                .tags(new Tag(RECEIPT, "Receipt controller"))
                .apiInfo(apiDetails())
                .useDefaultResponseMessages(false);
    }

    @Bean
    public Docket data() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/data/**"))
                .apis(RequestHandlerSelectors.basePackage("org.promocat.promocat.data_entities"))
                .build()
                .groupName("data")
                .tags(new Tag(COMPANY, "Company controller"))
                .tags(new Tag(STOCK, "Stock controller"))
                .apiInfo(apiDetails())
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiDetails() {
        return new ApiInfo(
                "PromoCat application API",
                "Amazing promo application",
                "1.0",
                "https://promocatcompany.com/termsofuse",
                new springfox.documentation.service.Contact("Maxim", "https://promocatcompany.com",
                        "devru@promocatcompany.com"),
                "API License",
                "https://api.promocatcompany.com",
                Collections.emptyList()
        );
    }
}
