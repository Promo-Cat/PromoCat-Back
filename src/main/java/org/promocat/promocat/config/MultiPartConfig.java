package org.promocat.promocat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 14:42 18.07.2020
 */
@Configuration
@Import(SpringDataRestConfiguration.class)
public class MultiPartConfig {
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver commonsMultipartResolver() {
        return new CommonsMultipartResolver();
    }
}
