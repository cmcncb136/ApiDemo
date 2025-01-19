package com.example.apidemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenApiConfig {
    @Value("${openai.api.key}")
    private String apiKey;

    @Bean
    public ChatGTPRestTemplate restTemplate() {
        return new ChatGTPRestTemplate(apiKey);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
