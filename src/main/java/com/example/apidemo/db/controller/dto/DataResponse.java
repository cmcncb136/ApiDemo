package com.example.apidemo.db.controller.dto;

import com.example.apidemo.db.DataEntity;
import lombok.Builder;

@Builder
public class DataResponse {
    public String url;

    public String title;

    public String content;

    public String language_code;

    public long count;

    public static DataResponse from(DataEntity entity) {
        return DataResponse.builder()
                .url(entity.getUrl())
                .title(entity.getTitle())
                .content(entity.getContent())
                .language_code(entity.getLanguage_code())
                .count(entity.getCount())
                .build();
    }
}
