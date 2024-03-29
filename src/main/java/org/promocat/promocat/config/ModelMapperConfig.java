package org.promocat.promocat.config;

import org.hibernate.collection.spi.PersistentCollection;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 18:52 06.05.2020
 */
@Configuration
@EnableTransactionManagement
@Import(SpringDataRestConfiguration.class)
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PUBLIC);
//                .setPropertyCondition(context -> !(context.getSource() instanceof PersistentCollection));
        return mapper;
    }

}
