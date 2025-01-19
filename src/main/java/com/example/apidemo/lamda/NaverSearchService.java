package com.example.apidemo.lamda;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class NaverSearchService {
    @Value("${aws.lambda.url.naver.search}")
    private static String url;

    private final RestTemplate restTemplate = new RestTemplate();

    public String runArn(){
        return  restTemplate.getForObject(url, String.class);
    }
}
