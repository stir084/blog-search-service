package com.example.modulecommon.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class BlogSearchResultDto {

    private BlogSearchMeta meta;
    private List<BlogSearchDocument> documents;

    @Getter
    @Setter
    public static class BlogSearchMeta {
        private int count;
        private Boolean isPageEnd;
    }

    @Getter
    @Setter
    @Builder
    public static class BlogSearchDocument {
        private String title;
        private String url;
        private String contents;
        private String blogname;
        private LocalDateTime postdate;

    }


    public BlogSearchResultDto(KakaoBlogSearchResultDto kakaoBlogSearchResultDto) {
        BlogSearchMeta blogSearchMeta = new BlogSearchMeta();
        blogSearchMeta.setCount(kakaoBlogSearchResultDto.getMeta().getTotal_count());
        blogSearchMeta.setIsPageEnd(kakaoBlogSearchResultDto.getMeta().getIs_end());
        this.meta = blogSearchMeta;
        this.documents = kakaoBlogSearchResultDto.getDocuments().stream().map(document ->
                BlogSearchDocument.builder()
                        .title(document.getTitle())
                        .contents(document.getContents())
                        .url(document.getUrl())
                        .postdate(document.getDatetime())
                        .blogname(document.getBlogname()).build()).collect(Collectors.toList());
    }
    public BlogSearchResultDto(NaverBlogSearchResultDto naverBlogSearchResultDto) {
        BlogSearchMeta blogSearchMeta = new BlogSearchMeta();
        blogSearchMeta.setCount(naverBlogSearchResultDto.getTotal());
        blogSearchMeta.setIsPageEnd(naverBlogSearchResultDto.getIs_end());
        this.meta = blogSearchMeta;
        this.documents = naverBlogSearchResultDto.getItems().stream().map(item ->
                BlogSearchDocument.builder()
                .title(item.getTitle())
                .contents(item.getDescription())
                .url(item.getLink())
                .postdate(item.getPostdate())
                .blogname(item.getBloggername()).build()).collect(Collectors.toList());
    }

}
