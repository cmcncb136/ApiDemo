package com.example.apidemo.db.controller;

import com.example.apidemo.db.DataEntity;
import com.example.apidemo.db.Keyword;
import com.example.apidemo.db.controller.dto.DataResponse;
import com.example.apidemo.db.service.DataService;
import com.example.apidemo.db.service.KeywordAndDataService;
import com.example.apidemo.db.service.KeywordService;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("search")
public class KeywordController {
    private KeywordService keywordService;
    private KeywordAndDataService keywordAndDataService;
    private DataService dataService;

    @GetMapping("")
    List<String> getKeywords(@RequestParam("languageCode") String languageCode) {
        return  keywordService.getByLanguageCode(languageCode).stream().map(Keyword::getKeyword).toList();
    }


    @GetMapping("/data")
    List<DataResponse> getData(@RequestParam("keyword") String keyword) {
        ArrayList<DataResponse> dataResponses =  new ArrayList<>();

        keywordAndDataService.getKeywordAndDataByKeyword(keyword).forEach(it ->{
            DataEntity data = dataService.getDataById(it.getUrl());
            if(data != null) {
                dataResponses.add(DataResponse.from(data));
            }
        });

        return dataResponses;
    }


}
