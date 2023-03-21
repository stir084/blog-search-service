package com.example.modulecore.service;


import com.example.modulecommon.dto.BlogSearchResultDto;
import com.example.modulecommon.dto.PopularKeywordDto;
import com.example.modulecore.exception.AllSearchProvidersFailedException;
import com.example.modulecore.exception.BlogSearchEngineResponseException;
import com.example.modulecore.domain.PopularKeyword;
import com.example.modulecore.repository.PopularKeywordRepository;
import com.example.moduleexternalapi.api.BlogSearchSortType;
import com.example.moduleexternalapi.api.KakaoBlogSearchProvider;
import com.example.moduleexternalapi.api.NaverBlogSearchProvider;
import com.example.moduleexternalapi.api.SearchProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogSearchService {


    private final KakaoBlogSearchProvider kakaoSearchProvider;
    private final NaverBlogSearchProvider naverSearchProvider;
    private final PopularKeywordRepository popularKeywordRepository;
    public static final String POPULAR_KEYWORDS_CACHE = "popularKeywords";


    @Transactional
    public BlogSearchResultDto searchBlog(String keyword, String sort, int page, int size) {
        List<SearchProvider> providers = Arrays.asList(kakaoSearchProvider, naverSearchProvider);

        for (SearchProvider provider : providers) {
            try {
                BlogSearchSortType blogSearchSortType = null;
                if (sort.startsWith("accuracy")) {
                    blogSearchSortType = provider.getAccuracy();
                } else if(sort.startsWith("recency")) {
                    blogSearchSortType = provider.getRecency();
                }
                BlogSearchResultDto result = provider.search(keyword, blogSearchSortType, page, size);
                updatePopularKeyword(keyword);
                return result;
            } catch (WebClientResponseException e) {
                String errorMessage = provider.getWebClientResponseErrorMsg(e.getResponseBodyAsString());
                if(e.getStatusCode() != HttpStatus.INTERNAL_SERVER_ERROR){
                    throw new BlogSearchEngineResponseException(errorMessage, e.getStatusCode());
                }
            } catch (Exception e) {
            }
        }

        throw new AllSearchProvidersFailedException("All search providers failed to provide results");
    }


    private void updatePopularKeyword(String keyword) {
        Optional<PopularKeyword> popularKeywordOptional = popularKeywordRepository.findByKeyword(keyword);
        if (popularKeywordOptional.isPresent()) {
            PopularKeyword popularKeyword = popularKeywordOptional.get();
            popularKeyword.incrementCount();
            popularKeywordRepository.save(popularKeyword);
        } else {
            popularKeywordRepository.save(PopularKeyword.createPopularKeyword(keyword, 1));
        }
    }

    @Transactional(readOnly = true)
    @Cacheable(value = POPULAR_KEYWORDS_CACHE)
    public List<PopularKeywordDto> getPopularKeywords() {
        List<PopularKeyword> popularKeywordList = popularKeywordRepository.findTop10ByOrderByCountDesc();
        return popularKeywordList.stream()
                .map(popularKeyword -> new PopularKeywordDto(popularKeyword.getKeyword(), popularKeyword.getCount()))
                .collect(Collectors.toList());

    }

    @Scheduled(fixedRate = 30000) // 1분마다 실행 (60000ms = 1분)
    @CacheEvict(value = POPULAR_KEYWORDS_CACHE, allEntries = true)
    public void evictPopularKeywordsCache() {

    }
}