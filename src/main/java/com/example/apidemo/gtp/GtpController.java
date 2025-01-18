package com.example.apidemo.gtp;

import com.example.apidemo.gtp.dto.ChatGPTRequest;
import com.example.apidemo.gtp.dto.ChatGTPResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("gtp")
@AllArgsConstructor
public class GtpController {
    private final String url = "https://api.openai.com/v1/chat/completions";

    private final String model = "gpt-4o-mini";

    private final RestTemplate restTemplate;

    @GetMapping("/chat")
    public String chat(@RequestParam("prompt") String prompt) {
        ChatGPTRequest chatGPTRequest = ChatGPTRequest.builder().model(model).prompt(prompt).build();
        ChatGTPResponse chatGTPResponse = restTemplate.postForObject(url, chatGPTRequest, ChatGTPResponse.class);
        if (chatGTPResponse == null) {
            return null;
        }
        return chatGTPResponse.getChoices().getFirst().getMessage().getContent();
    }
}
