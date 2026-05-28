package com.easyprofile.authlib.service;

public class DefaultReferenceLookupService implements ReferenceLookupService {

    @Override
    public boolean exists(String target, String key, Object value) {
        return false;
    }
}
