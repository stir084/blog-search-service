package com.example.modulecommon.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PopularKeywordDto {
    private String keyword;
    private int count;

    public PopularKeywordDto(String keyword, int count) {
        this.keyword = keyword;
        this.count = count;
    }
}