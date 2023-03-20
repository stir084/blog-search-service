package com.example.moduleexternalapi.api;

import com.example.modulecommon.dto.BlogSearchResultDto;
import com.example.modulecommon.dto.NaverBlogSearchResultDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class NaverBlogSearchProvider implements SearchProvider {

    @Override
    public BlogSearchResultDto search(String keyword, BlogSearchSortType sort, int page, int size) {
        WebClient webClient = WebClient.create("https://openapi.naver.com/v1/search/blog.json");
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("https://openapi.naver.com/v1/search/blog.json");
        builder.queryParam("query", keyword)
                .queryParam("sort", sort)
                .queryParam("start", page)
                .queryParam("display", size);
        NaverBlogSearchResultDto naverBlogSearchResultDto;
        try {
            naverBlogSearchResultDto = webClient.get()
                .uri(builder.build(false).toUriString())
                .header( "CONTENT_TYPE", MediaType.APPLICATION_JSON_VALUE)
                .header("X-Naver-Client-Id", "sGiEIy0nq_I1kNxT9WdJ")
                .header("X-Naver-Client-Secret", "ptTQIuWDRB")
                .retrieve()
                .bodyToMono(NaverBlogSearchResultDto.class)
                .block();
        } catch (WebClientResponseException e) {
            e.printStackTrace();
            throw e;
        }
        int responseSize = naverBlogSearchResultDto.getDisplay();
        if(size > responseSize) {
            naverBlogSearchResultDto.setIs_end(true);
        } else {
            naverBlogSearchResultDto.setIs_end(false);
        }
        BlogSearchResultDto blogSearchResult = naverBlogSearchResultDto.toBlogSearchResult();

        return blogSearchResult;

    }

    @Override
    public BlogSearchSortType getAccuracy() {
        return BlogSearchSortType.SIM;
    }

    @Override
    public BlogSearchSortType getRecency() {
        return BlogSearchSortType.DATE;
    }

    @Override
    public String getWebClientResponseErrorMsg(String responseBody){
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException jsonProcessingException) {
            jsonProcessingException.printStackTrace();
        }
        return rootNode.get("errorMessage").asText();
    }
}