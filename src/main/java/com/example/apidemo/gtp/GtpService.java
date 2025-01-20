package com.example.apidemo.gtp;

import com.example.apidemo.config.ChatGTPRestTemplate;
import com.example.apidemo.gtp.dto.ChatGPTRequest;
import com.example.apidemo.gtp.dto.ChatGTPResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class GtpService {
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    private final String MODEL = "gpt-4o-mini";

    private final ChatGTPRestTemplate chatGTPRestTemplate;


    public ResponseEntity<String> chatSimple(String prompt, String model) {
        ChatGPTRequest chatGPTRequest = ChatGPTRequest.builder().model(model).prompt(prompt).build();
        ChatGTPResponse chatGTPResponse = chatGTPRestTemplate.postForObject(API_URL, chatGPTRequest, ChatGTPResponse.class);
        if (chatGTPResponse == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                chatGTPResponse.getChoices().getFirst().getMessage().getContent()
        );
    }


    public ResponseEntity<String> chatSimple(String prompt) {
        return chatSimple(prompt, MODEL);
    }

    public ResponseEntity<ChatGTPResponse> chat(ChatGPTRequest chatGPTRequest) {
        ResponseEntity<List<ChatGTPResponse>> responseEntity = chat(List.of(chatGPTRequest));
        if(responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return ResponseEntity.ok(Objects.requireNonNull(responseEntity.getBody()).getFirst());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    public ResponseEntity<List<ChatGTPResponse>> chat(List<ChatGPTRequest> chatGPTRequestList){
        List<CompletableFuture<ChatGTPResponse>> futures = chatGPTRequestList.stream().map(
                chatGPTRequest ->
                        CompletableFuture.supplyAsync(() ->
                                chatGTPRestTemplate.postForObject(API_URL, chatGPTRequest, ChatGTPResponse.class)
                        )
        ).toList();

        List<ChatGTPResponse> responses = futures.stream().map(CompletableFuture::join).toList();
        return ResponseEntity.ok(responses);
    }

    public ResponseEntity<List<String>> chatSimple(List<String> promptList) {
        List<ChatGPTRequest> chatGPTRequests = promptList.stream().map(prompt ->
                ChatGPTRequest.builder().model(MODEL).prompt(prompt).build()).toList();

        List<ChatGTPResponse> responses = chat(chatGPTRequests).getBody();
        if(responses == null){ return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); }

        return ResponseEntity.ok(
                responses.stream().map(response ->
                        response.getChoices().getFirst().getMessage().getContent()).toList());
    }

}




