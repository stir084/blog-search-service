package com.example.modulecommon.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
public class NaverBlogSearchResultDto {
    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<NaverBlogSearchDocument> items;
    private Boolean is_end;

    @Getter
    @Setter
    public static class NaverBlogSearchDocument {
        private String title;
        private String link;
        private String description;
        private String bloggername;
        private String bloggerlink;
        private LocalDateTime postdate;

        public void setPostdate(String datetime) {
            LocalDate date = LocalDate.parse(datetime, DateTimeFormatter.ofPattern("yyyyMMdd"));
            this.postdate = date.atStartOfDay();
        }
    }


    public BlogSearchResultDto toBlogSearchResult() {
        return new BlogSearchResultDto(this);
    }
}
