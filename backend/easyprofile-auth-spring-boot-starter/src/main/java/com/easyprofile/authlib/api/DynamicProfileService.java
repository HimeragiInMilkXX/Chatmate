package com.easyprofile.authlib.api;

import com.easyprofile.authlib.dto.request.ReferenceOptions;
import com.easyprofile.authlib.enums.DataType;

import java.util.Map;

public interface DynamicProfileService {

    void addColumn(String columnName, DataType dataType);

    void addColumn(String columnName, DataType dataType, ReferenceOptions options);

    Map<String, Object> get(String... columns);

    Map<String, Object> getForUser(Long userId, String... columns);

    void setForCurrentUser(Map<String, Object> updates);
}
