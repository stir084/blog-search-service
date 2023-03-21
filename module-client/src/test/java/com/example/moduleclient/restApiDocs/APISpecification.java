package com.example.moduleclient.restApiDocs;

import com.example.modulecommon.dto.BlogSearchResultDto;
import com.example.modulecommon.dto.KakaoBlogSearchResultDto;
import com.example.modulecommon.dto.PopularKeywordDto;
import com.example.modulecore.service.BlogSearchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@WebMvcTest
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
public class APISpecification {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogSearchService blogSearchService;

    @Test
    @DisplayName("블로그 검색 API 명세서 추출")
    public void testSearchBlog() throws Exception {

        KakaoBlogSearchResultDto kakaoBlogSearchResultDto = getKakaoBlogSearchResultDto();
        BlogSearchResultDto mockResult = new BlogSearchResultDto(kakaoBlogSearchResultDto);

        when(blogSearchService.searchBlog(anyString(), anyString(), anyInt(), anyInt())).thenReturn(mockResult);

        mockMvc.perform(get("/search/blog")
                .param("query", "test")
                .param("sort", "accuracy")
                .param("page", "1")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andDo(document("search-blog",
                        responseFields(
                                fieldWithPath("meta.count").description("총 검색 건수"),
                                fieldWithPath("meta.isPageEnd").description("현재 페이지가 마지막 페이지 여부"),
                                fieldWithPath("documents[].title").description("블로그 글 제목"),
                                fieldWithPath("documents[].contents").description("블로그 글 요약 내용"),
                                fieldWithPath("documents[].url").description("블로그 글 URL"),
                                fieldWithPath("documents[].blogname").description("블로그 이름"),
                                fieldWithPath("documents[].postdate").description("블로그 글 작성 일시")
                        )));
    }

    @Test
    @DisplayName("인기 검색어 API 명세서 출력")
    public void testGetPopularKeywords() throws Exception {
        List<PopularKeywordDto> mockResult = getPopularKeywordDtos();
        when(blogSearchService.getPopularKeywords()).thenReturn(mockResult);

        mockMvc.perform(get("/search/popular-keywords"))
                .andExpect(status().isOk())
                .andDo(document("get-popular-keywords",
                        responseFields(
                                fieldWithPath("[]").description("인기 검색어 목록"),
                                fieldWithPath("[].keyword").description("검색어"),
                                fieldWithPath("[].count").description("검색어에 대한 검색 건수")
                        )));
    }

    public KakaoBlogSearchResultDto getKakaoBlogSearchResultDto(){
        KakaoBlogSearchResultDto kakaoBlogSearchResultDto = new KakaoBlogSearchResultDto();

        KakaoBlogSearchResultDto.KakaoBlogSearchMeta kakaoBlogSearchMeta = new KakaoBlogSearchResultDto.KakaoBlogSearchMeta();
        kakaoBlogSearchMeta.setTotal_count(100);
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

    private List<PopularKeywordDto> getPopularKeywordDtos() {
        List<PopularKeywordDto> popularKeywordDtos = new ArrayList<>();
        popularKeywordDtos.add(new PopularKeywordDto("Spring", 100));
        popularKeywordDtos.add(new PopularKeywordDto("REST", 50));
        popularKeywordDtos.add(new PopularKeywordDto("Java", 200));
        return popularKeywordDtos;
    }
}