package com.example.modulecore.repository;

import com.example.modulecore.domain.PopularKeyword;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class PopularKeywordRepositoryTest {

    @Autowired
    private PopularKeywordRepository popularKeywordRepository;

    @Test
    @DisplayName("인기 키워드 중 상위 10개 조회 테스트")
    void testFindTop10ByOrderByCountDesc() {
        PopularKeyword popularKeyword1 = new PopularKeyword("keyword1", 5);
        PopularKeyword popularKeyword2 = new PopularKeyword("keyword2", 4);
        popularKeywordRepository.save(popularKeyword1);
        popularKeywordRepository.save(popularKeyword2);

        var result = popularKeywordRepository.findTop10ByOrderByCountDesc();

        assertEquals(2, result.size());
        assertEquals(popularKeyword1, result.get(0));
        assertEquals(popularKeyword2, result.get(1));
    }

    @Test
    @DisplayName("인기 키워드 조회 테스트")
    void testFindByKeyword() {
        PopularKeyword popularKeyword = new PopularKeyword("keyword1", 5);
        popularKeywordRepository.save(popularKeyword);

        Optional<PopularKeyword> result = popularKeywordRepository.findByKeyword("keyword1");

        assertTrue(result.isPresent());
        assertEquals(popularKeyword, result.get());
    }

    @Test
    @DisplayName("인기 키워드 저장 테스트")
    void testSave() {
        PopularKeyword popularKeyword = new PopularKeyword("keyword1", 5);
        popularKeywordRepository.save(popularKeyword);

        Optional<PopularKeyword> result = popularKeywordRepository.findById(popularKeyword.getId());

        assertTrue(result.isPresent());
        assertEquals(popularKeyword.getKeyword(), result.get().getKeyword());
        assertEquals(popularKeyword.getCount(), result.get().getCount());
    }
}