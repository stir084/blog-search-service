package com.example.moduleclient.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("com.example.modulecore.domain")
@EnableJpaRepositories(basePackages = "com.example.modulecore.repository")
@Configuration
public class JpaConfiguration {
}

