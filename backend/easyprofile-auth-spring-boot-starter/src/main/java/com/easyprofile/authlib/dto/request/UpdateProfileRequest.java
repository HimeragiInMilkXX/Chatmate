package com.easyprofile.authlib.dto.request;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class UpdateProfileRequest {

    @Size(min = 3, max = 64)
    private String username;

    @Email
    @Size(max = 128)
    private String email;

    private final Map<String, Object> dynamicFields = new LinkedHashMap<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonAnySetter
    public void captureUnknownField(String key, Object value) {
        if (!"username".equals(key) && !"email".equals(key)) {
            dynamicFields.put(key, value);
        }
    }

    public Map<String, Object> getDynamicFields() {
        return Collections.unmodifiableMap(dynamicFields);
    }
}
