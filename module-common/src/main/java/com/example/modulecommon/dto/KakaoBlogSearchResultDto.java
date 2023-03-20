package com.example.modulecommon.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
public class KakaoBlogSearchResultDto {
    private KakaoBlogSearchMeta meta;
    private List<KakaoBlogSearchDocument> documents;

    @Getter
    @Setter
    public static class KakaoBlogSearchMeta {
        private Integer total_count;
        private Integer pageable_count;
        private Boolean is_end;
    }

    @Getter
    @Setter
    public static class KakaoBlogSearchDocument {
        private String title;
        private String contents;
        private String url;
        private String blogname;
        private String thumbnail;
        private LocalDateTime datetime;

        public void setDatetime(String datetime) {
            this.datetime = LocalDateTime.parse(datetime, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }

    }


    public BlogSearchResultDto toBlogSearchResult() {
        return new BlogSearchResultDto(this);
    }
}
