package com.example.apidemo.db.controller;

import com.example.apidemo.db.Keyword;
import com.example.apidemo.db.service.KeywordService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("search")
public class KeywordController {
    private KeywordService keywordService;

    @GetMapping("")
    List<String> getKeywords() {
        return  keywordService.getAllKeywords().stream().map(Keyword::getKeyword).toList();
    }


}
