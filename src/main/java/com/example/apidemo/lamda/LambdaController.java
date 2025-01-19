package com.example.apidemo.lamda;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("lambda")
public class LambdaController {
    private final NaverSearchService naverSearchService;

    @GetMapping("/naver-search")
    public String naverSearch() {
        return naverSearchService.runArn();
    }
}
