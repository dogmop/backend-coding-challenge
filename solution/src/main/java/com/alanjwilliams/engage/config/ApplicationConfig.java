package com.alanjwilliams.engage.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Spring Application Configuration.
 */
@Configuration
public class ApplicationConfig {

    /**
     * Spring RestTemplate Client.
     *
     * RestTemplate is a convenient Spring template class to allow simple interaction with RESTful services.
     *
     * @param builder RestTemplateBuilder
     * @return RestTemplate
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
