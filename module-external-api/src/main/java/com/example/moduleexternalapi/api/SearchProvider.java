package com.example.moduleexternalapi.api;


import com.example.modulecommon.dto.BlogSearchResultDto;
import org.springframework.web.reactive.function.client.WebClient;

public interface SearchProvider {
    BlogSearchResultDto search(String keyword, BlogSearchSortType sort, int page, int size);
    BlogSearchSortType getAccuracy();
    BlogSearchSortType getRecency();
    String getWebClientResponseErrorMsg(String responseBody);
    WebClient createWebClient();
}