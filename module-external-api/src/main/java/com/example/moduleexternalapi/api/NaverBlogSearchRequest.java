package com.example.moduleexternalapi.api;

import com.example.modulecommon.dto.KakaoBlogSearchResultDto;
import com.example.modulecommon.dto.NaverBlogSearchResultDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class NaverBlogSearchRequest {
    private static final String NAVER_ID_API_KEY = "sGiEIy0nq_I1kNxT9WdJ";
    private static final String NAVER_SECRET_API_KEY= "ptTQIuWDRB";

    public NaverBlogSearchResultDto requestBlogSearch(WebClient webClient, UriComponentsBuilder builder) {
        return webClient.get()
                .uri(builder.build(false).toUriString())
                .header( "CONTENT_TYPE", MediaType.APPLICATION_JSON_VALUE)
                .header("X-Naver-Client-Id", NAVER_ID_API_KEY)
                .header("X-Naver-Client-Secret", NAVER_SECRET_API_KEY)
                .retrieve()
                .bodyToMono(NaverBlogSearchResultDto.class)
                .block();
    }
}
