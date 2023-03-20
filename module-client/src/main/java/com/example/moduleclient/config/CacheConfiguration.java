package com.example.moduleclient.config;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.URL;

@Configuration
@EnableCaching
@EnableScheduling
public class CacheConfiguration {

    @Bean
    public CacheManager ehCacheManager() {
        URL ehcacheConfigUrl = getClass().getResource("/ehcache.xml");
        XmlConfiguration ehcacheConfig = new XmlConfiguration(ehcacheConfigUrl);
        CacheManager cacheManager = CacheManagerBuilder.newCacheManager(ehcacheConfig);
        cacheManager.init();
        return cacheManager;
    }
}