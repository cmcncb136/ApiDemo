package com.example.apidemo.agent;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("agent")
public class AgentController {
    private final AgentService agentService;

    @GetMapping("")
    public ResponseEntity<String> agent(@RequestParam("query") String query) {
        return agentService.agentService(query);
    }
}
