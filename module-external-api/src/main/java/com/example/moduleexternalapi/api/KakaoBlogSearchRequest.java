package com.example.moduleexternalapi.api;

import com.example.modulecommon.dto.KakaoBlogSearchResultDto;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class KakaoBlogSearchRequest {
    private static final String KAKAO_API_KEY = "KakaoAK d326c8eb5c802411de9602adfb3c2f25";
    public KakaoBlogSearchResultDto requestBlogSearch(WebClient webClient, UriComponentsBuilder builder) {
        return webClient.get()
                .uri(builder.build(false).toUriString())
                .header("Authorization", KAKAO_API_KEY)
                .retrieve()
                .bodyToMono(KakaoBlogSearchResultDto.class)
                .block();
    }
}
