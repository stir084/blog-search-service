package com.example.moduleclient.controller;

import com.example.modulecommon.dto.BlogSearchResultDto;
import com.example.modulecommon.dto.KakaoBlogSearchResultDto;
import com.example.modulecommon.dto.PopularKeywordDto;
import com.example.modulecore.service.BlogSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BlogSearchController.class)
@AutoConfigureMockMvc
public class BlogSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogSearchService blogSearchService;

    @Test
    void searchBlog() throws Exception {
        KakaoBlogSearchResultDto kakaoBlogSearchResultDto = getKakaoBlogSearchResultDto();
        BlogSearchResultDto mockResult = new BlogSearchResultDto(kakaoBlogSearchResultDto);

        when(blogSearchService.searchBlog(anyString(), anyString(), anyInt(), anyInt())).thenReturn(mockResult);

        mockMvc.perform(get("/search/blog")
                .param("query", "test")
                .param("sort", "accuracy")
                .param("page", "1")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.isPageEnd").isBoolean())
                .andExpect(jsonPath("$.documents.length()").isNotEmpty());
    }

    @Test
    void getPopularKeywords() throws Exception {
        PopularKeywordDto mockKeyword = new PopularKeywordDto("test", 5);
        List<PopularKeywordDto> mockKeywords = Collections.singletonList(mockKeyword);

        when(blogSearchService.getPopularKeywords()).thenReturn(mockKeywords);

        mockMvc.perform(get("/search/popular-keywords")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].keyword").value("test"))
                .andExpect(jsonPath("$[0].count").value(5));
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