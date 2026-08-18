package ru.ncheranev.otus.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.web.client.RestOperations;

@Configuration
public class AppConfig {
    @Bean
    public RestOperations restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public RecordMessageConverter converter() {
        return new JsonMessageConverter();
    }
}
