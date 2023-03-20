package com.example.moduleclient;


import com.example.modulecore.domain.PopularKeyword;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class InitDb {

    private final InitService initService;

    @Bean
    public ApplicationRunner init() {
        return args -> initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit1() {
            em.persist(PopularKeyword.createPopularKeyword("사과", (int) (Math.random() * 100) + 1));
            em.persist(PopularKeyword.createPopularKeyword("바나나", (int) (Math.random() * 100) + 1));
            em.persist(PopularKeyword.createPopularKeyword("당근", (int) (Math.random() * 100) + 1));
            em.persist(PopularKeyword.createPopularKeyword("포도", (int) (Math.random() * 100) + 1));
            em.persist(PopularKeyword.createPopularKeyword("양파", (int) (Math.random() * 100) + 1));
            em.persist(PopularKeyword.createPopularKeyword("블루베리", (int) (Math.random() * 100) + 1));
            em.persist(PopularKeyword.createPopularKeyword("레몬", (int) (Math.random() * 100) + 1));
            em.persist(PopularKeyword.createPopularKeyword("석류", (int) (Math.random() * 100) + 1));
            em.persist(PopularKeyword.createPopularKeyword("수박", (int) (Math.random() * 100) + 1));
            em.persist(PopularKeyword.createPopularKeyword("오렌지", (int) (Math.random() * 100) + 1));
            em.persist(PopularKeyword.createPopularKeyword("감귤", (int) (Math.random() * 100) + 1));
        }
    }
}