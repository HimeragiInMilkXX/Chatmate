package com.easyprofile.authlib.storage;

public class AvatarFile {

    private final String storedName;
    private final String url;

    public AvatarFile(String storedName, String url) {
        this.storedName = storedName;
        this.url = url;
    }

    public String getStoredName() {
        return storedName;
    }

    public String getUrl() {
        return url;
    }
}
