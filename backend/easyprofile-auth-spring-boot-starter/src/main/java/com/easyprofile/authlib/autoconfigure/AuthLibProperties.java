package com.easyprofile.authlib.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "authlib")
public class AuthLibProperties {

    private boolean enabled = true;

    private long tokenTimeout = 60L * 60 * 24 * 7;

    private final Avatar avatar = new Avatar();

    private final ResetPassword resetPassword = new ResetPassword();

    private final Security security = new Security();

    private final Cors cors = new Cors();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public long getTokenTimeout() {
        return tokenTimeout;
    }

    public void setTokenTimeout(long tokenTimeout) {
        this.tokenTimeout = tokenTimeout;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public ResetPassword getResetPassword() {
        return resetPassword;
    }

    public Security getSecurity() {
        return security;
    }

    public Cors getCors() {
        return cors;
    }

    public static class Avatar {

        private String uploadDir = "uploads/avatars";

        private String baseUrl = "/uploads/avatars";

        private long maxSizeBytes = 5 * 1024 * 1024;

        public String getUploadDir() {
            return uploadDir;
        }

        public void setUploadDir(String uploadDir) {
            this.uploadDir = uploadDir;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public long getMaxSizeBytes() {
            return maxSizeBytes;
        }

        public void setMaxSizeBytes(long maxSizeBytes) {
            this.maxSizeBytes = maxSizeBytes;
        }
    }

    public static class ResetPassword {

        private int cooldownDays = 7;

        public int getCooldownDays() {
            return cooldownDays;
        }

        public void setCooldownDays(int cooldownDays) {
            this.cooldownDays = cooldownDays;
        }
    }

    public static class Security {

        private boolean requireOldPasswordOnReset = true;

        public boolean isRequireOldPasswordOnReset() {
            return requireOldPasswordOnReset;
        }

        public void setRequireOldPasswordOnReset(boolean requireOldPasswordOnReset) {
            this.requireOldPasswordOnReset = requireOldPasswordOnReset;
        }
    }

    public static class Cors {

        private boolean enabled = true;

        private List<String> allowedOrigins = new ArrayList<>();

        private List<String> allowedMethods = new ArrayList<>(List.of("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS"));

        private List<String> allowedHeaders = new ArrayList<>(List.of("*"));

        private boolean allowCredentials = true;

        private long maxAge = 3600;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public List<String> getAllowedOrigins() {
            return allowedOrigins;
        }

        public void setAllowedOrigins(List<String> allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }

        public List<String> getAllowedMethods() {
            return allowedMethods;
        }

        public void setAllowedMethods(List<String> allowedMethods) {
            this.allowedMethods = allowedMethods;
        }

        public List<String> getAllowedHeaders() {
            return allowedHeaders;
        }

        public void setAllowedHeaders(List<String> allowedHeaders) {
            this.allowedHeaders = allowedHeaders;
        }

        public boolean isAllowCredentials() {
            return allowCredentials;
        }

        public void setAllowCredentials(boolean allowCredentials) {
            this.allowCredentials = allowCredentials;
        }

        public long getMaxAge() {
            return maxAge;
        }

        public void setMaxAge(long maxAge) {
            this.maxAge = maxAge;
        }
    }
}
