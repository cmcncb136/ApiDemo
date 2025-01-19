package com.example.apidemo.config;


import org.springframework.web.client.RestTemplate;

public class ChatGTPRestTemplate extends RestTemplate {

    ChatGTPRestTemplate(String apiKey) {
        getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + apiKey);
            return execution.execute(request, body);
        });
    }

}
