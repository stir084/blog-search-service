package com.example.modulecore.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PopularKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String keyword;

    @Version
    private int count;

    public PopularKeyword(String keyword, int count) {
        this.keyword = keyword;
        this.count = count;
    }

    public static PopularKeyword createPopularKeyword(String keyword, int count) {
        return new PopularKeyword(keyword, count);
    }


    public void incrementCount() {
        this.count += 1;
    }
}