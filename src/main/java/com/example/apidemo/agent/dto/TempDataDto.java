package com.example.apidemo.agent.dto;

import com.example.apidemo.db.DataEntity;

import java.util.List;

public class TempDataDto {
    public String title;
    public String content;
    public List<String> keywords;
    public List<String> highlight;
    public boolean isAd;

    public DataEntity toDataEntity(String url, String language) {
        return DataEntity.builder()
                .title(title)
                .content(content)
                .count(1)
                .url(url)
                .language_code(language)
                .build();
    }
}
