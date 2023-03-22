package com.example.moduleexternalapi;

import com.example.modulecommon.dto.BlogSearchResultDto;
import com.example.modulecommon.dto.KakaoBlogSearchResultDto;
import com.example.modulecommon.dto.NaverBlogSearchResultDto;
import com.example.moduleexternalapi.api.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BlogSearchProviderTest {

    @Mock
    private KakaoBlogSearchRequest kakaoBlogSearchRequest;

    @Mock
    private NaverBlogSearchRequest naverBlogSearchRequest;

    @InjectMocks
    private KakaoBlogSearchProvider kakaoBlogSearchProvider;

    @InjectMocks
    private NaverBlogSearchProvider naverBlogSearchProvider;

    @Test
    @DisplayName("카카오 API 테스트")
    void testKakaoBlogSearchProvider() {
        // given
        KakaoBlogSearchResultDto stubResult = getKakaoBlogSearchResultDto();

        when(kakaoBlogSearchRequest.requestBlogSearch(any(), any())).thenReturn(stubResult);

        // when
        BlogSearchResultDto result = kakaoBlogSearchProvider.search("test", BlogSearchSortType.ACCURACY, 1, 10);

        // then
        assertEquals(true, result.getMeta().getIsPageEnd());
        assertEquals("carrot", result.getDocuments().get(0).getTitle());
    }

    @Test
    @DisplayName("네이버 API 테스트")
    void testNaverBlogSearchProvider() {
        // given
        NaverBlogSearchResultDto stubResult = getNaverBlogSearchResultDto();
        when(naverBlogSearchRequest.requestBlogSearch(any(), any())).thenReturn(stubResult);

        // when
        BlogSearchResultDto result = naverBlogSearchProvider.search("test", BlogSearchSortType.ACCURACY, 1, 10);

        // then
        assertEquals(false, result.getMeta().getIsPageEnd());
        assertEquals("java spring", result.getDocuments().get(0).getTitle());
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

    public NaverBlogSearchResultDto getNaverBlogSearchResultDto(){
        NaverBlogSearchResultDto naverBlogSearchResultDto = new NaverBlogSearchResultDto();

        List<NaverBlogSearchResultDto.NaverBlogSearchDocument> naverBlogSearchDocumentList = new ArrayList<>();

        NaverBlogSearchResultDto.NaverBlogSearchDocument naverBlogSearchDocument = new NaverBlogSearchResultDto.NaverBlogSearchDocument();
        naverBlogSearchDocument.setBloggerlink("https://stir.tistory.com");
        naverBlogSearchDocument.setBloggername("loose");
        naverBlogSearchDocument.setTitle("java spring");
        naverBlogSearchDocument.setPostdate("20230321");
        naverBlogSearchDocument.setDescription("study java");
        naverBlogSearchDocument.setLink("https://stir.tistory.com");

        naverBlogSearchDocumentList.add(naverBlogSearchDocument);

        naverBlogSearchResultDto.setItems(naverBlogSearchDocumentList);
        naverBlogSearchResultDto.setDisplay(300);
        naverBlogSearchResultDto.setStart(1);
        naverBlogSearchResultDto.setLastBuildDate("20221231");
        naverBlogSearchResultDto.setTotal(300000);
        naverBlogSearchResultDto.setIs_end(false);

        return naverBlogSearchResultDto;
    }
}