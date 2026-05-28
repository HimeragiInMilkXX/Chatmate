package com.easyprofile.authlib.autoconfigure;

import cn.dev33.satoken.config.SaTokenConfig;
import com.easyprofile.authlib.controller.AuthController;
import com.easyprofile.authlib.exception.GlobalExceptionHandler;
import com.easyprofile.authlib.mapper.UserViewMapper;
import com.easyprofile.authlib.repository.UserProfileFieldRepository;
import com.easyprofile.authlib.repository.UserProfileValueRepository;
import com.easyprofile.authlib.repository.UserRepository;
import com.easyprofile.authlib.service.AuthService;
import com.easyprofile.authlib.service.CurrentUserProvider;
import com.easyprofile.authlib.service.CurrentUserProviderImpl;
import com.easyprofile.authlib.service.DefaultReferenceLookupService;
import com.easyprofile.authlib.service.DynamicProfileServiceImpl;
import com.easyprofile.authlib.service.ReferenceLookupService;
import com.easyprofile.authlib.storage.AvatarStorageService;
import com.easyprofile.authlib.storage.LocalAvatarStorageService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AutoConfiguration
@EnableConfigurationProperties(AuthLibProperties.class)
@ConditionalOnProperty(prefix = "authlib", name = "enabled", havingValue = "true", matchIfMissing = true)
@EntityScan(basePackages = "com.easyprofile.authlib.entity")
@EnableJpaRepositories(basePackages = "com.easyprofile.authlib.repository")
public class AuthLibAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean
    public UserViewMapper userViewMapper() {
        return new UserViewMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    public CurrentUserProvider currentUserProvider(UserRepository userRepository) {
        return new CurrentUserProviderImpl(userRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReferenceLookupService referenceLookupService() {
        return new DefaultReferenceLookupService();
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamicProfileServiceImpl dynamicProfileService(
        UserProfileFieldRepository fieldRepository,
        UserProfileValueRepository valueRepository,
        UserRepository userRepository,
        CurrentUserProvider currentUserProvider,
        ReferenceLookupService referenceLookupService
    ) {
        return new DynamicProfileServiceImpl(fieldRepository, valueRepository, userRepository, currentUserProvider, referenceLookupService);
    }

    @Bean
    @ConditionalOnMissingBean
    public AvatarStorageService avatarStorageService(AuthLibProperties authLibProperties) {
        return new LocalAvatarStorageService(authLibProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthService authService(
        UserRepository userRepository,
        BCryptPasswordEncoder passwordEncoder,
        CurrentUserProvider currentUserProvider,
        DynamicProfileServiceImpl dynamicProfileService,
        AvatarStorageService avatarStorageService,
        UserViewMapper userViewMapper,
        AuthLibProperties authLibProperties
    ) {
        return new AuthService(
            userRepository,
            passwordEncoder,
            currentUserProvider,
            dynamicProfileService,
            avatarStorageService,
            userViewMapper,
            authLibProperties
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthController authController(AuthService authService) {
        return new AuthController(authService);
    }

    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean(name = "authLibWebMvcConfigurer")
    public WebMvcConfigurer authLibWebMvcConfigurer(AuthLibProperties authLibProperties) {
        return new com.easyprofile.authlib.config.AuthLibWebMvcConfig(authLibProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public SaTokenConfig saTokenConfig(AuthLibProperties authLibProperties) {
        SaTokenConfig config = new SaTokenConfig();
        config.setTimeout(authLibProperties.getTokenTimeout());
        config.setIsConcurrent(false);
        config.setIsShare(false);
        return config;
    }
}
