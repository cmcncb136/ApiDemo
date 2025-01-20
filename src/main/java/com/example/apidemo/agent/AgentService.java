package com.example.apidemo.agent;

import com.example.apidemo.gtp.GtpService;
import com.example.apidemo.naver.NaverBlogScraperService;
import com.example.apidemo.naver.NaverBlogSearchService;
import com.example.apidemo.naver.dto.NaverBlogSearchResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AgentService {
    private final GtpService gtpService;;
    private final NaverBlogSearchService naverBlogSearchService;
    private final NaverBlogScraperService naverBlogScraperService;

    private final String SEARCH_RECOMMEND_QUERY = "위 내용을 네이버에 검색할 생각인데 검색할 때 해당 내용 목적에 맞게 검색할 내용을 1개만 추천해주고 추천한 내용만 반환해줘" ;

    public ResponseEntity<String> agentService(String query) {
        ResponseEntity<String> searchQueryResponse = gtpService.chat("\"" + query + "\"" + SEARCH_RECOMMEND_QUERY);

        if (searchQueryResponse.getStatusCode() != HttpStatus.OK)
            return ResponseEntity.status(searchQueryResponse.getStatusCode()).body("GTP를 사용하는데 문제가 발생했습니다.");

        String searchQuery = searchQueryResponse.getBody();
        searchQuery = searchQuery.replaceAll("\"", "");
        System.out.println("searchQuery: " + searchQuery);

        ResponseEntity<String> searchSourceResponse = naverBlogSearchService.invokeNaverBlogSearch(searchQuery);
        if (searchSourceResponse.getStatusCode() != HttpStatus.OK)
            return ResponseEntity.status(searchSourceResponse.getStatusCode()).body("Naver Search API를 사용하는데 문제가 발생했습니다.");

        String searchSource = searchSourceResponse.getBody();
        System.out.println("searchSource: " + searchSource);

        NaverBlogSearchResponse naverBlogSearchResponse;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            naverBlogSearchResponse = objectMapper.readValue(searchSource, NaverBlogSearchResponse.class);
            System.out.println("naverBlogSearchResponse: " + naverBlogSearchResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        //List<String> blogUrls = naverBlogSearchResponse.getItems().stream().map(it -> it.getLink());
        //ResponseEntity<List<String>> blogContents = naverBlogScraperService.getBlogContents()

        return ResponseEntity.status(HttpStatus.OK).body("test");
    }
}
