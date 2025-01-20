package com.example.apidemo.gtp.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatGPTRequest {
    private String model; //물어볼 모델
    private List<Message> messages; //물어볼 메시지들

    @Builder
    public ChatGPTRequest(String model, String prompt) {
        this.model = model != null ? model : "gpt-4o-mini";
        this.messages = new ArrayList<>();
        this.messages.add(new Message("user", prompt));
    }
}
