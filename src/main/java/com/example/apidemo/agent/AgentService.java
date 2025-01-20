    package com.example.apidemo.agent;

    import com.example.apidemo.gtp.GtpService;
    import com.example.apidemo.gtp.dto.ChatGPTRequest;
    import com.example.apidemo.gtp.dto.ChatGTPResponse;
    import com.example.apidemo.gtp.dto.Message;
    import com.example.apidemo.naver.NaverBlogScraperService;
    import com.example.apidemo.naver.NaverBlogSearchService;
    import com.example.apidemo.naver.dto.NaverBlogFastScraperService;
    import com.example.apidemo.naver.dto.NaverBlogItem;
    import com.example.apidemo.naver.dto.NaverBlogSearchResponse;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import lombok.AllArgsConstructor;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.stereotype.Service;


    import java.io.IOException;
    import java.util.List;
    import java.util.regex.Matcher;
    import java.util.regex.Pattern;

    @Service
    @AllArgsConstructor
    public class AgentService {
        private final GtpService gtpService;

        private final NaverBlogSearchService naverBlogSearchService;
        private final NaverBlogScraperService naverBlogScraperService;
        private final NaverBlogFastScraperService naverBlogFastScraperService;

        private final String GET_LANGUAGE_CODE_QUERY = "첫 줄의 언어가 어떤 언어 코드인지를 반환해 그리고 그 언어 코드만을 반환해.";
        private final String SEARCH_RECOMMEND_QUERY = "위 내용을 네이버에 검색할 생각인데 검색할 때 해당 내용 목적에 맞게 검색할 한국어 내용을 1개만 추천해주고 추천한 내용만 반환해줘";
        private final String SEARCH_SUMMARY_QUERY = """
                위에 내용을 바탕으로 다음 JSON 데이터를 채워줘.
                {
                   "title" : "",
                   "keywords" : [],
                   "content" : "",
                   "highlight" : [],
                   "isAd" : true/false
                }
                """;
        private final String SEARCH_TRANSLATE_QUERY = "위 내용을 번역해줘. 단, JSON 형태가 유지되어야해";


        private final String SEARCH_TOGETHER_QUERY = "위에 쓰인 언어로 해당 내용을 정리해줘 그리고, div 태크를 하나만 사용한 html 코드로 만들어서 코드만 반환해줘";

        public String getLanguage(String query) {
            System.out.println("query : " + query);
            ChatGPTRequest chatGPTRequest = ChatGPTRequest.builder()
                    .prompt(query)
                    .build();

            chatGPTRequest.getMessages().clear();
            chatGPTRequest.getMessages().add(Message.builder().role("system").content("의미는 고려하지 말고 글자 자체가 어떤 언어 인지 판별하고 언어 코드만을 반환해").build());
            chatGPTRequest.getMessages().add(Message.builder().role("user").content(query).build());

            ChatGTPResponse chatGTPResponse = gtpService.chat(chatGPTRequest).getBody();
            return chatGTPResponse.getChoices().getFirst().getMessage().getContent();
        }

        public ResponseEntity<String> agentService(String query)  {
            String language = getLanguage(query);
            System.out.println("language : " + language);

            ChatGPTRequest chatGPTRequest = ChatGPTRequest.builder()
                    .prompt(query)
                    .build();

            chatGPTRequest.getMessages().clear();
            chatGPTRequest.getMessages().add(Message.builder().role("system").content("응답은 한국어으로만 해").build());
            chatGPTRequest.getMessages().add(Message.builder().role("user").content("\"" + query + "\"" + SEARCH_RECOMMEND_QUERY).build());


            ResponseEntity<ChatGTPResponse> searchQueryResponse = gtpService.chat(chatGPTRequest);

            if (searchQueryResponse.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(searchQueryResponse.getStatusCode()).body("GTP를 사용하는데 문제가 발생했습니다.");
            }
            String searchQuery = searchQueryResponse.getBody().getChoices().getFirst().getMessage().getContent();
            searchQuery = searchQuery.replaceAll("\"", "");
            System.out.println("searchQuery: " + searchQuery);

            ResponseEntity<String> searchSourceResponse = naverBlogSearchService.invokeNaverBlogSearch(searchQuery);
            if (searchSourceResponse.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(searchSourceResponse.getStatusCode()).body("Naver Search API를 사용하는데 문제가 발생했습니다.");
            }
            //조회
            String searchSource = searchSourceResponse.getBody();
            System.out.println("searchSource: " + searchSource);

            NaverBlogSearchResponse naverBlogSearchResponse;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                naverBlogSearchResponse = objectMapper.readValue(searchSource, NaverBlogSearchResponse.class);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }

            naverBlogSearchResponse.getItems().stream().forEach(item ->
                    item.setLink(item.getLink().replaceAll("\\\\", ""))
            );
            System.out.println("naverBlogSearchResponse: " + naverBlogSearchResponse);

            //urls 목록들
            List<String> blogUrls = naverBlogSearchResponse.getItems().stream().map(NaverBlogItem::getLink).toList();
            //값 가져오기
            ResponseEntity<List<String>> blogContents = naverBlogFastScraperService.getBlogContents(blogUrls);


            if (blogContents.getStatusCode() != HttpStatus.OK || blogContents.getBody() == null)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

            //요약
            List<String> summaryContent = summaryBlog(blogContents.getBody());
            if (summaryContent == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            System.out.println("summaryContent: " + summaryContent);

            //번역
            List<String> translatedContents = translateBlog(language, summaryContent);
            if (translatedContents == null) {
                System.out.println("translatedContents: " + null);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            System.out.println("translatedContents: " + translatedContents);

            //취합
            String rst = togetherBlog(language, translatedContents);
            if(rst == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            return ResponseEntity.status(HttpStatus.OK).body(rst);
        }

        private List<String> summaryBlog(List<String> blogContents) {
            List<ChatGPTRequest> chatGPTRequests = blogContents.stream().map(
                    content ->
                            ChatGPTRequest.builder().prompt(content + "\n\n위 내용을 30% 정도로 요약하고 요약한 내용만을 반환해줘").build()
            ).toList();

            ResponseEntity<List<ChatGTPResponse>> chatGTPResponses = gtpService.chat(chatGPTRequests);
            if (chatGTPResponses.getStatusCode() != HttpStatus.OK || chatGTPResponses.getBody() == null)
                return null;


            List<String> summaryPromptList = chatGTPResponses.getBody().stream().map(
                    it -> it.getChoices().getFirst().getMessage().getContent() + SEARCH_SUMMARY_QUERY
            ).toList();


            ResponseEntity<List<String>> summaryContents = gtpService.chatSimple(summaryPromptList);
            if (summaryContents.getStatusCode() != HttpStatus.OK || summaryContents.getBody() == null)
                return null;

            summaryContents.getBody().forEach(System.out::println);
            //Todo. 진짜 혹시나 잘못된 경우 처리 생각해야 됨
            return summaryContents.getBody().stream().map(summaryContent -> {
                String tmp = getJSONString(summaryContent);
                return tmp != null ? tmp : "";
            }).toList();
        }

        private List<String> translateBlog(String language, List<String> summaryContents) {


            ResponseEntity<List<ChatGTPResponse>> translateContents = gtpService.chat(summaryContents.stream().map(
                    it -> {
                        ChatGPTRequest chatGPTRequest = ChatGPTRequest.builder()
                                .prompt("tmp")
                                .build();

                        chatGPTRequest.getMessages().clear();
                        chatGPTRequest.getMessages().add(Message.builder().role("system").content("\"" + language + "\"" + " 해당 언어 코드에 맞게 번역해").build());

                        String query = it + "\n\n" + SEARCH_TRANSLATE_QUERY;
                        chatGPTRequest.getMessages().add(Message.builder().role("user").content(query).build());
                        return  chatGPTRequest;
                    }
            ).toList());

            if (translateContents.getStatusCode() != HttpStatus.OK || translateContents.getBody() == null)
                return null;

            return translateContents.getBody().stream().map(translateContent -> {
                String tmp = getJSONString(translateContent.getChoices().getFirst().getMessage().getContent());
                return tmp != null ? tmp : "";
            }).toList();
        }

        private String togetherBlog(String language, List<String> summaryContents) {
            ChatGPTRequest chatGPTRequest = ChatGPTRequest.builder()
                    .prompt("tmp")
                    .build();

            chatGPTRequest.getMessages().clear();
            chatGPTRequest.getMessages().add(Message.builder().role("system").content(language + " 로 번역해줘").build());

            String query = summaryContents.toString() + "\n\n" + SEARCH_TOGETHER_QUERY;

            chatGPTRequest.getMessages().add(Message.builder().role("user").content(query).build());


            ResponseEntity<ChatGTPResponse> rst = gtpService.chat(chatGPTRequest);
            if(rst.getStatusCode() != HttpStatus.OK || rst.getBody() == null)
                return null;

            return rst.getBody().getChoices().getFirst().getMessage().getContent();
        }


        private static Pattern pattern = Pattern.compile("\\{[^}]*\\}");

        private String getJSONString(String target) {
            Matcher matcher = pattern.matcher(target);
            return matcher.find() ? matcher.group() : null;
        }
    }
