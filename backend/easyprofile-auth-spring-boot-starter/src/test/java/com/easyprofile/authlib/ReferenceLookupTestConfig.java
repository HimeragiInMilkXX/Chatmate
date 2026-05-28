package com.easyprofile.authlib;

import com.easyprofile.authlib.service.ReferenceLookupService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class ReferenceLookupTestConfig {

    @Bean
    @Primary
    public ReferenceLookupService referenceLookupService() {
        return (target, key, value) -> {
            if (!"forum".equalsIgnoreCase(target)) {
                return false;
            }
            if (value == null) {
                return false;
            }
            long id = value instanceof Number number ? number.longValue() : Long.parseLong(String.valueOf(value));
            return id == 1L || id == 2L;
        };
    }
}
