package com.easyprofile.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = {"com.easyprofile.demo.entity", "com.easyprofile.authlib.entity"})
@EnableJpaRepositories(basePackages = "com.easyprofile.demo.repository")
public class DemoJpaConfig {
}
