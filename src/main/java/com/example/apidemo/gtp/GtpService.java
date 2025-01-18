package com.example.apidemo.gtp;

import com.example.apidemo.gtp.dto.ChatGPTRequest;
import com.example.apidemo.gtp.dto.ChatGTPResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class GtpService {
    @Value ("${openai.api.key}")
    private static String apiKey;

    private static final String API_URL = "https://api.gtpapi.com/2.0/";

    private final String MODEL = "gpt-4o-mini";

    private final RestTemplate restTemplate;


    public ResponseEntity<String> chat(String prompt) {
        ChatGPTRequest chatGPTRequest = ChatGPTRequest.builder().model(MODEL).prompt(prompt).build();
        ChatGTPResponse chatGTPResponse = restTemplate.postForObject(API_URL, chatGPTRequest, ChatGTPResponse.class);
        if (chatGTPResponse == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                chatGTPResponse.getChoices().getFirst().getMessage().getContent()
        );
    }

}




