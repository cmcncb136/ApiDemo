package com.example.apidemo.naver;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("naver")
public class NaverSearchController {
    private final NaverBlogSearchService naverBlogSearchService;

    @GetMapping("/blog")
    public ResponseEntity<String> naverBlogSearch(@RequestParam("query") String query) {
        return naverBlogSearchService.invokeNaverBlogSearch(query);
    }
}
