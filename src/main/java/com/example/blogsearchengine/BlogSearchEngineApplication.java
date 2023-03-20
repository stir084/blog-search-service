package com.example.blogsearchengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BlogSearchEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogSearchEngineApplication.class, args);
	}

}
