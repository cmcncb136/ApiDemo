package com.example.apidemo;
// 네이버 검색 API 예제 - 블로그 검색

public class ApiExamSearchBlog {

//    public static void main(String[] args) {
//        String text = null;
//        try {
//            text = URLEncoder.encode("그린팩토리", StandardCharsets.UTF_8);
//        } catch (Exception e) {
//            throw new RuntimeException("검색어 인코딩 실패", e);
//        }
//
//        String apiURL = "https://openapi.naver.com/v1/search/blog?query=" + text;    // JSON 결과
//        //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // XML 결과
//
//
//        Map<String, String> requestHeaders = new HashMap<>();
//        requestHeaders.put("X-Naver-Client-Id", clientId);
//        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
//        String responseBody = get(apiURL,requestHeaders);
//
////        System.out.println(responseBody);
//        parseAndPrintJson(responseBody);
//    }

//    private static void parseAndPrintJson(String responseBody) {
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
//
//        // 전체 JSON 보기 좋게 출력
//        String prettyJson = gson.toJson(jsonObject);
//        System.out.println("전체 JSON 출력:");
//        System.out.println(prettyJson);
//
//        // "items" 배열 파싱
//        System.out.println("\n개별 블로그 데이터 추출:");
//        if (jsonObject.has("items") && jsonObject.get("items").isJsonArray()) {
//            JsonArray items = jsonObject.getAsJsonArray("items");
//            for (JsonElement item : items) {
//                JsonObject blog = item.getAsJsonObject();
//                String title = blog.get("title").getAsString();
//                String link = blog.get("link").getAsString();
//                String description = blog.get("description").getAsString();
//
//                // 출력
//                System.out.println("Title: " + title);
//                System.out.println("Link: " + link);
//                System.out.println("Description: " + description);
//                System.out.println("--------------------------------");
//            }
//        } else {
//            System.out.println("`items` 배열이 JSON 응답에 없습니다.");
//        }
//    }
//
//    private static String get(String apiUrl, Map<String, String> requestHeaders){
//        HttpURLConnection con = connect(apiUrl);
//        try {
//            con.setRequestMethod("GET");
//            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
//                con.setRequestProperty(header.getKey(), header.getValue());
//            }
//
//
//            int responseCode = con.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
//                return readBody(con.getInputStream());
//            } else { // 오류 발생
//                return readBody(con.getErrorStream());
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("API 요청과 응답 실패", e);
//        } finally {
//            con.disconnect();
//        }
//    }
//
//
//    private static HttpURLConnection connect(String apiUrl){
//        try {
//            URL url = new URL(apiUrl);
//            return (HttpURLConnection)url.openConnection();
//        } catch (MalformedURLException e) {
//            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
//        } catch (IOException e) {
//            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
//        }
//    }
//
//
//    private static String readBody(InputStream body) {
//        try (BufferedReader lineReader = new BufferedReader(new InputStreamReader(body, StandardCharsets.UTF_8))) {
//            StringBuilder responseBody = new StringBuilder();
//            String line;
//            while ((line = lineReader.readLine()) != null) {
//                responseBody.append(line);
//            }
//            return responseBody.toString();
//        } catch (IOException e) {
//            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
//        }
//    }
}