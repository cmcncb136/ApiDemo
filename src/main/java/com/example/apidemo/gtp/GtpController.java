package com.example.apidemo.gtp;

import com.example.apidemo.gtp.dto.ChatGPTRequest;
import com.example.apidemo.gtp.dto.ChatGTPResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("gtp")
@AllArgsConstructor
public class GtpController {
    private final GtpService gtpService;

    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam("prompt") String prompt) {
        return gtpService.chat(prompt);
    }
}
