package com.easyprofile.demo;

import com.easyprofile.authlib.api.DynamicProfileService;
import com.easyprofile.authlib.dto.request.ReferenceOptions;
import com.easyprofile.authlib.enums.DataType;
import com.easyprofile.authlib.exception.ResourceConflictException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoProfileFieldInitializer {

    private static final Logger log = LoggerFactory.getLogger(DemoProfileFieldInitializer.class);

    @Bean
    public CommandLineRunner registerDemoProfileFields(DynamicProfileService dynamicProfileService) {
        return args -> {
            registerField(dynamicProfileService, "age", DataType.INT);
            registerField(dynamicProfileService, "bio", DataType.TEXT);
            registerField(dynamicProfileService, "birthday", DataType.DATE);
            registerField(dynamicProfileService, "nationality", DataType.TEXT);
            registerField(dynamicProfileService, "interest", DataType.TEXT);
            registerField(dynamicProfileService, "region", DataType.TEXT);
            registerReferenceField(dynamicProfileService, "favoriteForumId", ReferenceOptions.of("forum", "id", false));
        };
    }

    private void registerField(DynamicProfileService dynamicProfileService, String fieldName, DataType dataType) {
        try {
            dynamicProfileService.addColumn(fieldName, dataType);
            log.info("Registered demo profile field '{}' with type {}", fieldName, dataType);
        } catch (ResourceConflictException ex) {
            log.info("Profile field '{}' already exists, skipping registration", fieldName);
        }
    }

    private void registerReferenceField(
        DynamicProfileService dynamicProfileService,
        String fieldName,
        ReferenceOptions options
    ) {
        try {
            dynamicProfileService.addColumn(fieldName, DataType.REFERENCE, options);
            log.info("Registered demo reference field '{}' -> {}.{}", fieldName, options.getTarget(), options.getKey());
        } catch (ResourceConflictException ex) {
            log.info("Profile field '{}' already exists, skipping registration", fieldName);
        }
    }
}
