package com.easyprofile.authlib.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.easyprofile.authlib.autoconfigure.AuthLibProperties;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class AuthLibWebMvcConfig implements WebMvcConfigurer {

    private final AuthLibProperties authLibProperties;

    public AuthLibWebMvcConfig(AuthLibProperties authLibProperties) {
        this.authLibProperties = authLibProperties;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> SaRouter.match("/auth/**")
                .notMatch("/auth/register", "/auth/login")
                .check(r -> {
                    String method = SaHolder.getRequest().getMethod();
                    if ("OPTIONS".equalsIgnoreCase(method)) {
                        return;
                    }
                    StpUtil.checkLogin();
                })))
            .addPathPatterns("/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        AuthLibProperties.Cors cors = authLibProperties.getCors();
        if (!cors.isEnabled()) {
            return;
        }

        List<String> origins = cors.getAllowedOrigins();
        if (origins == null || origins.isEmpty()) {
            return;
        }

        registry.addMapping("/**")
            .allowedOrigins(origins.toArray(String[]::new))
            .allowedMethods(cors.getAllowedMethods().toArray(String[]::new))
            .allowedHeaders(cors.getAllowedHeaders().toArray(String[]::new))
            .allowCredentials(cors.isAllowCredentials())
            .maxAge(cors.getMaxAge());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String baseUrl = authLibProperties.getAvatar().getBaseUrl();
        if (!baseUrl.endsWith("/")) {
            baseUrl = baseUrl + "/";
        }

        Path uploadPath = Paths.get(authLibProperties.getAvatar().getUploadDir()).toAbsolutePath().normalize();
        String location = uploadPath.toUri().toString();

        registry.addResourceHandler(baseUrl + "**")
            .addResourceLocations(location);
    }
}
