package com.example.moduleclient.controller;


import com.example.modulecommon.dto.BlogSearchResultDto;
import com.example.modulecommon.dto.PopularKeywordDto;
import com.example.modulecore.service.BlogSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequiredArgsConstructor
public class BlogSearchController {

    private final BlogSearchService blogSearchService;

    @GetMapping("/search/blog")
    public ResponseEntity<BlogSearchResultDto> searchBlog(@RequestParam String query,
                                                          @RequestParam(defaultValue = "accuracy") String sort,
                                                          @RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        BlogSearchResultDto blogSearchResult = blogSearchService.searchBlog(query, sort, page, size);
        return ResponseEntity.ok().body(blogSearchResult);
    }

    @GetMapping("/search/popular-keywords")
    public ResponseEntity<List<PopularKeywordDto>> getPopularKeywords() {
        List<PopularKeywordDto> popularKeywords = blogSearchService.getPopularKeywords();
        return ResponseEntity.ok().body(popularKeywords);
    }
}