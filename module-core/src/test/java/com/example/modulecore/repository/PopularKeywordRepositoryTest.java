package com.example.modulecore.repository;

import com.example.modulecore.domain.PopularKeyword;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PopularKeywordRepositoryTest {

    @Autowired
    private PopularKeywordRepository popularKeywordRepository;

    @Test
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
    void testFindByKeyword() {
        PopularKeyword popularKeyword = new PopularKeyword("keyword1", 5);
        popularKeywordRepository.save(popularKeyword);

        Optional<PopularKeyword> result = popularKeywordRepository.findByKeyword("keyword1");

        assertTrue(result.isPresent());
        assertEquals(popularKeyword, result.get());
    }

    @Test
    void testSave() {
        PopularKeyword popularKeyword = new PopularKeyword("keyword1", 5);
        popularKeywordRepository.save(popularKeyword);

        Optional<PopularKeyword> result = popularKeywordRepository.findById(popularKeyword.getId());

        assertTrue(result.isPresent());
        assertEquals(popularKeyword, result.get());
    }
}