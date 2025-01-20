package com.example.apidemo.agent;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("agent")
public class AgentController {
    private final AgentService agentService;

    @GetMapping("")
    public ResponseBodyEmitter agent(@RequestParam("query") String query) {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        try {
            agentService.agentService(query, emitter);
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }


}
