package com.easyprofile.authlib.service;

public interface ReferenceLookupService {

    boolean exists(String target, String key, Object value);
}
