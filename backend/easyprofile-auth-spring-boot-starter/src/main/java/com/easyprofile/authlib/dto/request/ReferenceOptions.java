package com.easyprofile.authlib.dto.request;

public class ReferenceOptions {

    private String target;

    private String key = "id";

    private boolean required;

    public ReferenceOptions() {
    }

    public ReferenceOptions(String target, String key, boolean required) {
        this.target = target;
        if (key != null && !key.isBlank()) {
            this.key = key;
        }
        this.required = required;
    }

    public static ReferenceOptions of(String target) {
        return new ReferenceOptions(target, "id", false);
    }

    public static ReferenceOptions of(String target, String key, boolean required) {
        return new ReferenceOptions(target, key, required);
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
