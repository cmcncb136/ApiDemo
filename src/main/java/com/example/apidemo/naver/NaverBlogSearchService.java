package com.example.apidemo.naver;

import lombok.AllArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class NaverBlogSearchService {
    @Value("${naver.api.key}")
    private String clientId;

    @Value("${naver.api.secret}")
    private String clientSecret;

    private static final String apiURL = "https://openapi.naver.com/v1/search/blog?query=";

    public ResponseEntity<String> invokeNaverBlogSearch(String query) {
        String api = null;
        api = apiURL + URLEncoder.encode(query, StandardCharsets.UTF_8);

        try(CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(api);
            request.setHeader("X-Naver-Client-Id", clientId);
            request.setHeader("X-Naver-Client-Secret", clientSecret);

            try(CloseableHttpResponse response = client.execute(request)) {
                if(response.getStatusLine().getStatusCode() == 200) {
                    return ResponseEntity.ok(EntityUtils.toString(response.getEntity()));
                }else{
                   return ResponseEntity.status(response.getStatusLine().getStatusCode()).build();
                }
            }catch (Exception e) {
               return ResponseEntity.status(500).build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }

    }

}
