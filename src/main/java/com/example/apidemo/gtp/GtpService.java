package com.example.apidemo.gtp;

import com.example.apidemo.config.ChatGTPRestTemplate;
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
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    private final String MODEL = "gpt-4o-mini";

    private final ChatGTPRestTemplate chatGTPRestTemplate;


    public ResponseEntity<String> chat(String prompt) {
        ChatGPTRequest chatGPTRequest = ChatGPTRequest.builder().model(MODEL).prompt(prompt).build();
        ChatGTPResponse chatGTPResponse = chatGTPRestTemplate.postForObject(API_URL, chatGPTRequest, ChatGTPResponse.class);
        if (chatGTPResponse == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                chatGTPResponse.getChoices().getFirst().getMessage().getContent()
        );
    }

}




