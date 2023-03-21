package com.example.modulecore.service;


import com.example.modulecommon.dto.BlogSearchResultDto;
import com.example.modulecommon.dto.KakaoBlogSearchResultDto;
import com.example.modulecommon.dto.PopularKeywordDto;
import com.example.modulecommon.exception.AllSearchProvidersFailedException;
import com.example.modulecore.domain.PopularKeyword;
import com.example.modulecore.repository.PopularKeywordRepository;
import com.example.moduleexternalapi.api.BlogSearchSortType;
import com.example.moduleexternalapi.api.KakaoBlogSearchProvider;
import com.example.moduleexternalapi.api.NaverBlogSearchProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlogSearchServiceTest {

    @InjectMocks
    private BlogSearchService blogSearchService;

    @Mock
    private KakaoBlogSearchProvider kakaoSearchProvider;

    @Mock
    private NaverBlogSearchProvider naverSearchProvider;

    @Mock
    private PopularKeywordRepository popularKeywordRepository;

    private String keyword = "test";
    private String sort = "accuracy";
    private int page = 1;
    private int size = 10;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("블로그 검색 결과 반환하는지 테스트")
    void testSearchBlogReturnsResult() {
        String keyword = "test";
        String sort = "accuracy";
        int page = 1;
        int size = 10;

        KakaoBlogSearchResultDto kakaoBlogSearchResultDto = getKakaoBlogSearchResultDto();

        BlogSearchResultDto expectedBlogSearchResultDto = new BlogSearchResultDto(kakaoBlogSearchResultDto);

        when(kakaoSearchProvider.search(anyString(), any(BlogSearchSortType.class), anyInt(), anyInt())).thenReturn(expectedBlogSearchResultDto);
        when(kakaoSearchProvider.getAccuracy()).thenReturn(BlogSearchSortType.ACCURACY);

        BlogSearchResultDto actualBlogSearchResultDto = blogSearchService.searchBlog(keyword, sort, page, size);

        assertEquals(expectedBlogSearchResultDto.getDocuments().size(), actualBlogSearchResultDto.getDocuments().size());
        for (int i = 0; i < expectedBlogSearchResultDto.getDocuments().size(); i++) {
            assertEquals(expectedBlogSearchResultDto.getDocuments().get(i).getTitle(), actualBlogSearchResultDto.getDocuments().get(i).getTitle());
            assertEquals(expectedBlogSearchResultDto.getDocuments().get(i).getBlogname(), actualBlogSearchResultDto.getDocuments().get(i).getBlogname());
            assertEquals(expectedBlogSearchResultDto.getDocuments().get(i).getContents(), actualBlogSearchResultDto.getDocuments().get(i).getContents());
            assertEquals(expectedBlogSearchResultDto.getDocuments().get(i).getPostdate(), actualBlogSearchResultDto.getDocuments().get(i).getPostdate());
            assertEquals(expectedBlogSearchResultDto.getDocuments().get(i).getUrl(), actualBlogSearchResultDto.getDocuments().get(i).getUrl());
        }
    }

    @Test
    @DisplayName("카카오 블로그 API 성공 시 검색 결과 반환하는지 테스트")
    void searchBlog_when_KakaoProviderSuccess() {
        String keyword = "test";
        String sort = "accuracy";
        int page = 1;
        int size = 10;

        KakaoBlogSearchResultDto kakaoBlogSearchResultDto = getKakaoBlogSearchResultDto();

        BlogSearchResultDto blogSearchResultDto = new BlogSearchResultDto(kakaoBlogSearchResultDto);
        when(kakaoSearchProvider.search(anyString(), any(BlogSearchSortType.class), anyInt(), anyInt())).thenReturn(blogSearchResultDto);
        when(kakaoSearchProvider.getAccuracy()).thenReturn(BlogSearchSortType.ACCURACY);

        BlogSearchResultDto result = blogSearchService.searchBlog(keyword, sort, page, size);

        assertEquals(blogSearchResultDto, result);
        verify(kakaoSearchProvider, times(1)).search(keyword, BlogSearchSortType.ACCURACY, page, size);
    }

    @Test
    @DisplayName("카카오 블로그 API 실패, 네이버 블로그 API 성공 시 검색 결과 반환하는지 테스트")
    void searchBlog_when_KakaoProviderFailure_NaverProviderSuccess() {
        String keyword = "test";
        String sort = "accuracy";
        int page = 1;
        int size = 10;

        KakaoBlogSearchResultDto kakaoBlogSearchResultDto = getKakaoBlogSearchResultDto();
        BlogSearchResultDto blogSearchResultDto = new BlogSearchResultDto(kakaoBlogSearchResultDto);

        when(kakaoSearchProvider.search(anyString(), any(BlogSearchSortType.class), anyInt(), anyInt())).thenThrow(RuntimeException.class);
        when(kakaoSearchProvider.getAccuracy()).thenReturn(BlogSearchSortType.ACCURACY);
        when(naverSearchProvider.search(anyString(), any(BlogSearchSortType.class), anyInt(), anyInt())).thenReturn(blogSearchResultDto);
        when(naverSearchProvider.getAccuracy()).thenReturn(BlogSearchSortType.SIM);


        BlogSearchResultDto result = blogSearchService.searchBlog(keyword, sort, page, size);

        assertEquals(blogSearchResultDto, result);

        assertThrows(RuntimeException.class, () -> kakaoSearchProvider.search(keyword, BlogSearchSortType.ACCURACY, page, size));
        verify(naverSearchProvider, times(1)).search(keyword, BlogSearchSortType.SIM, page, size);
    }

    @Test
    @DisplayName("카카오 블로그 API 실패, 네이버 블로그 API 실패 시 예외 발생하는지 테스트")
    void searchBlog_when_KakaoProviderFailure_NaverProviderFailure() {
        String keyword = "test";
        String sort = "accuracy";
        int page = 1;
        int size = 10;

        when(kakaoSearchProvider.search(anyString(), any(BlogSearchSortType.class), anyInt(), anyInt())).thenThrow(RuntimeException.class);
        when(kakaoSearchProvider.getAccuracy()).thenReturn(BlogSearchSortType.ACCURACY);
        when(naverSearchProvider.search(anyString(), any(BlogSearchSortType.class), anyInt(), anyInt())).thenThrow(RuntimeException.class);
        when(naverSearchProvider.getAccuracy()).thenReturn(BlogSearchSortType.SIM);

        assertThrows(AllSearchProvidersFailedException.class,
                () -> blogSearchService.searchBlog(keyword, sort, page, size));

    }

    @Test
    @DisplayName("인기 검색어 목록 조회하는지 테스트")
    void testGetPopularKeywords() {
        List<PopularKeyword> popularKeywordList = Arrays.asList(
                new PopularKeyword("carrot", 5),
                new PopularKeyword("apple", 4)
        );

        when(popularKeywordRepository.findTop10ByOrderByCountDesc()).thenReturn(popularKeywordList);

        List<PopularKeywordDto> popularKeywordDtoList = blogSearchService.getPopularKeywords();

        assertEquals(popularKeywordList.size(), popularKeywordDtoList.size());
        for (int i = 0; i < popularKeywordList.size(); i++) {
            assertEquals(popularKeywordList.get(i).getKeyword(), popularKeywordDtoList.get(i).getKeyword());
            assertEquals(popularKeywordList.get(i).getCount(), popularKeywordDtoList.get(i).getCount());
        }
    }

    @Test
    @DisplayName("기존 인기 검색어 업데이트하는지 테스트")
    void testUpdatePopularKeywordWithExistingKeyword() {
        String keyword = "test";
        PopularKeyword existingPopularKeyword = new PopularKeyword(keyword, 1);

        when(popularKeywordRepository.findByKeyword(keyword)).thenReturn(Optional.of(existingPopularKeyword));

        blogSearchService.searchBlog(keyword, "accuracy", 1, 10);

        verify(popularKeywordRepository, times(1)).findByKeyword(keyword);
        verify(popularKeywordRepository, times(1)).save(existingPopularKeyword);
        assertEquals(2, existingPopularKeyword.getCount());
    }

    @Test
    @DisplayName("새로운 인기 검색어 추가하는지 테스트")
    void testUpdatePopularKeywordWithNewKeyword() {
        String keyword = "test";

        when(popularKeywordRepository.findByKeyword(keyword)).thenReturn(Optional.empty());

        blogSearchService.searchBlog(keyword, "accuracy", 1, 10);

        ArgumentCaptor<PopularKeyword> popularKeywordArgumentCaptor = ArgumentCaptor.forClass(PopularKeyword.class);
        verify(popularKeywordRepository, times(1)).findByKeyword(keyword);
        verify(popularKeywordRepository, times(1)).save(popularKeywordArgumentCaptor.capture());

        PopularKeyword newPopularKeyword = popularKeywordArgumentCaptor.getValue();
        assertEquals(keyword, newPopularKeyword.getKeyword());
        assertEquals(1, newPopularKeyword.getCount());
    }

    public KakaoBlogSearchResultDto getKakaoBlogSearchResultDto(){
        KakaoBlogSearchResultDto kakaoBlogSearchResultDto = new KakaoBlogSearchResultDto();

        KakaoBlogSearchResultDto.KakaoBlogSearchMeta kakaoBlogSearchMeta = new KakaoBlogSearchResultDto.KakaoBlogSearchMeta();
        kakaoBlogSearchMeta.setTotal_count(2);
        kakaoBlogSearchMeta.setIs_end(true);

        List<KakaoBlogSearchResultDto.KakaoBlogSearchDocument> kakaoBlogSearchDocumentList = new ArrayList<>();
        KakaoBlogSearchResultDto.KakaoBlogSearchDocument kakaoBlogSearchDocument = new KakaoBlogSearchResultDto.KakaoBlogSearchDocument();
        kakaoBlogSearchDocument.setTitle("carrot");
        kakaoBlogSearchDocument.setBlogname("farm");
        kakaoBlogSearchDocument.setContents("carrot farm");
        kakaoBlogSearchDocument.setThumbnail("https://stir.tistory.com/thumbnail");
        kakaoBlogSearchDocument.setDatetime("2023-02-18T23:56:53.000+09:00");
        kakaoBlogSearchDocument.setUrl("https://stir.tistory.com");
        kakaoBlogSearchDocumentList.add(kakaoBlogSearchDocument);

        kakaoBlogSearchResultDto.setMeta(kakaoBlogSearchMeta);
        kakaoBlogSearchResultDto.setDocuments(kakaoBlogSearchDocumentList);
        return kakaoBlogSearchResultDto;
    }
}