package com.example.apidemo.naver;

import com.microsoft.playwright.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class NaverBlogScraperService {

    public ResponseEntity<List<String>> getBlogContents(List<String> blogUrls){
        ExecutorService executor = Executors.newFixedThreadPool(10); // 스레드 풀 생성

        try {//비동기 처리
            List<CompletableFuture<String>> futures = blogUrls.stream().map(
                    url -> CompletableFuture.supplyAsync(() -> {
                        try (Playwright playwright = Playwright.create();){
                            return processUrl( playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true)).newContext(), url);
                        }catch (Exception e) {
                            return e.getMessage();
                        }
                    }, executor)
            ).toList();

            //모든 처리가 완료될 때까지 반복한다.
            return ResponseEntity.ok(futures.stream().map(CompletableFuture::join).toList());
        }finally {
            executor.shutdown();
        }
    }

    private String processUrl(BrowserContext context, String blogUrl){
        try(Page page = context.newPage()) { //새로운 페이지를 연다.
            page.navigate(blogUrl);

            //body iframe으로 이동(본문에 내용이 ifrma안에 있음)
            Frame iframe = page.frameByUrl(url -> url.contains("PostView.naver"));
            if(iframe == null)
                return "not Fond URL : " + blogUrl;

            //본문 컨테이너가 로드될 때까지 대기한다.
            iframe.waitForSelector(".se-main-container");

            String content = iframe.textContent(".se-main-container");
            if(content == null || content.isEmpty())
                return "not Fond Content form. " + blogUrl;

            return content.trim().replaceAll("\u200B", "\n").replaceAll("(?m)^[\\s]*$", "");
        }catch (Exception e){
            return e.getMessage() +  "from. " + blogUrl;
        }
    }


    public ResponseEntity<String> getBlogContent(String blogUrl) {
        return ResponseEntity.ok(getBlogContents(List.of(blogUrl)).getBody().getFirst());
    }
}
