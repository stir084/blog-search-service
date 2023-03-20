package com.example.moduleexternalapi.api;

import com.example.modulecommon.dto.BlogSearchResultDto;
import com.example.modulecommon.dto.KakaoBlogSearchResultDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoBlogSearchProvider implements SearchProvider {


    @Override
    public BlogSearchResultDto search(String keyword, BlogSearchSortType sort, int page, int size) {
        WebClient webClient = WebClient.create("https://dapi.kakao.com/v2/search/blog");
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("https://dapi.kakao.com/v2/search/blog");
        builder.queryParam("query", keyword)
                .queryParam("sort", sort)
                .queryParam("page", page)
                .queryParam("size", size);
        KakaoBlogSearchResultDto kakaoBlogSearchResultDto;
        try {
            kakaoBlogSearchResultDto = webClient.get()
                .uri(builder.build(false).toUriString())
                .header("Authorization", "KakaoAK d326c8eb5c802411de9602adfb3c2f25")
                .retrieve()
                .bodyToMono(KakaoBlogSearchResultDto.class)
                .block();

        } catch (WebClientResponseException e) {
            e.printStackTrace();
            throw e;
        }
        BlogSearchResultDto blogSearchResult = kakaoBlogSearchResultDto.toBlogSearchResult();
        return blogSearchResult;
    }

    @Override
    public BlogSearchSortType getAccuracy() {
        return BlogSearchSortType.ACCURACY;
    }

    @Override
    public BlogSearchSortType getRecency() {
        return BlogSearchSortType.RECENCY;
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
        return rootNode.get("message").asText();
    }
}