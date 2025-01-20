package com.example.apidemo.naver.dto;

import com.microsoft.playwright.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class NaverBlogFastScraperService {
    private static final String PREFIX = "https://blog.naver.com";

    public ResponseEntity<List<String>> getBlogContents(List<String> blogUrls) {
        ExecutorService executor = Executors.newFixedThreadPool(10); // 스레드 풀 생성

        try {//비동기 처리
            List<CompletableFuture<String>> futures = blogUrls.stream().map(
                    url -> CompletableFuture.supplyAsync(() -> {
                        return processUrl(url);
                    }, executor)
            ).toList();

            //모든 처리가 완료될 때까지 반복한다.
            return ResponseEntity.ok(futures.stream().map(CompletableFuture::join).toList());
        } finally {
            executor.shutdown();
        }
    }

    private String processUrl(String blogUrl) {
        // 1. 블로그 페이지의 HTML 문서 가져오기
        Document document = null;
        try {
            document = Jsoup.connect(blogUrl).get();

            // 2. iframe 태그 찾기
            Element iframe = document.select("iframe#mainFrame").first(); // id가 mainFrame인 첫 번째 iframe

            // 3. 첫 번째 iframe의 src(URL) 추출
            if (iframe != null) {
                // 3. iframe의 src 속성 값 가져오기
                String iframeUrl = iframe.attr("src");
                System.out.println("Iframe URL: " + iframeUrl);

                // 4. iframe URL을 통해 내용 가져오기 (iframe 내의 콘텐츠를 추출)
                Document iframeDocument = Jsoup.connect(PREFIX + iframeUrl).get();
                Element mainContainer = iframeDocument.select(".se-main-container").first();
                if (mainContainer == null) return "not Fond container URL : " + blogUrl;

                System.out.println("mainContainer: " + mainContainer.text());

                return mainContainer.text();
            } else {
                return "not Fond iframe form. " + blogUrl;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
