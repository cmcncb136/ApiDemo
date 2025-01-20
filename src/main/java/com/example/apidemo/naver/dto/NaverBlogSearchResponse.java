package com.example.apidemo.naver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class NaverBlogSearchResponse {
    private String lastBuildDate;
    private int total;
    private int start;
    private int display;

    @JsonProperty("items")
    private List<NaverBlogItem> items;

}
