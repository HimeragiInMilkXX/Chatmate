package com.easyprofile.authlib.service;

import com.easyprofile.authlib.api.DynamicProfileService;
import com.easyprofile.authlib.dto.request.ReferenceOptions;
import com.easyprofile.authlib.entity.UserEntity;
import com.easyprofile.authlib.entity.UserProfileFieldEntity;
import com.easyprofile.authlib.entity.UserProfileValueEntity;
import com.easyprofile.authlib.enums.DataType;
import com.easyprofile.authlib.exception.BadRequestException;
import com.easyprofile.authlib.exception.NotFoundException;
import com.easyprofile.authlib.exception.ResourceConflictException;
import com.easyprofile.authlib.repository.UserProfileFieldRepository;
import com.easyprofile.authlib.repository.UserProfileValueRepository;
import com.easyprofile.authlib.repository.UserRepository;
import com.easyprofile.authlib.util.DynamicValueConverter;
import com.easyprofile.authlib.util.FieldNameSanitizer;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
public class DynamicProfileServiceImpl implements DynamicProfileService {

    private static final Set<String> RESERVED_NAMES = Set.of(
        "id", "username", "email", "password", "passwordhash", "password_hash", "avatarurl", "avatar_url",
        "createdat", "created_at", "updatedat", "updated_at", "lastpasswordresetat", "last_password_reset_at",
        "lastlogin", "last_login"
    );

    private static final Set<String> DEFAULT_FIELDS = Set.of("id", "username", "email", "avatarUrl", "lastLogin", "createdAt", "updatedAt");

    private final UserProfileFieldRepository userProfileFieldRepository;
    private final UserProfileValueRepository userProfileValueRepository;
    private final UserRepository userRepository;
    private final CurrentUserProvider currentUserProvider;
    private final ReferenceLookupService referenceLookupService;

    public DynamicProfileServiceImpl(
        UserProfileFieldRepository userProfileFieldRepository,
        UserProfileValueRepository userProfileValueRepository,
        UserRepository userRepository,
        CurrentUserProvider currentUserProvider,
        ReferenceLookupService referenceLookupService
    ) {
        this.userProfileFieldRepository = userProfileFieldRepository;
        this.userProfileValueRepository = userProfileValueRepository;
        this.userRepository = userRepository;
        this.currentUserProvider = currentUserProvider;
        this.referenceLookupService = referenceLookupService;
    }

    @Override
    public void addColumn(String columnName, DataType dataType) {
        addColumn(columnName, dataType, null);
    }

    @Override
    public void addColumn(String columnName, DataType dataType, ReferenceOptions options) {
        String sanitized = FieldNameSanitizer.sanitize(columnName, RESERVED_NAMES);
        userProfileFieldRepository.findByFieldName(sanitized).ifPresent(existing -> {
            throw new ResourceConflictException("Dynamic field already exists: " + sanitized);
        });

        UserProfileFieldEntity field = new UserProfileFieldEntity();
        field.setFieldName(sanitized);
        field.setDataType(dataType);
        field.setEnabled(true);
        if (DataType.REFERENCE.equals(dataType)) {
            ReferenceOptions validated = validateReferenceOptions(options);
            field.setReferenceTarget(validated.getTarget().trim());
            field.setReferenceKey(validated.getKey().trim());
            field.setReferenceRequired(validated.isRequired());
        } else {
            field.setReferenceTarget(null);
            field.setReferenceKey(null);
            field.setReferenceRequired(false);
        }
        userProfileFieldRepository.save(field);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> get(String... columns) {
        return getForUser(currentUserProvider.getCurrentUserId(), columns);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getForUser(Long userId, String... columns) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found"));

        Set<String> requested = resolveRequestedColumns(columns);
        Map<String, Object> result = new LinkedHashMap<>();

        if (requested.contains("id")) {
            result.put("id", user.getId());
        }
        if (requested.contains("username")) {
            result.put("username", user.getUsername());
        }
        if (requested.contains("email")) {
            result.put("email", user.getEmail());
        }
        if (requested.contains("avatarUrl")) {
            result.put("avatarUrl", user.getAvatarUrl());
        }
        if (requested.contains("createdAt")) {
            result.put("createdAt", user.getCreatedAt());
        }
        if (requested.contains("updatedAt")) {
            result.put("updatedAt", user.getUpdatedAt());
        }
        if (requested.contains("lastLogin")) {
            result.put("lastLogin", user.getLastLogin());
        }

        Set<String> dynamicFields = requested.stream()
            .filter(name -> !DEFAULT_FIELDS.contains(name))
            .collect(Collectors.toCollection(LinkedHashSet::new));

        if (!dynamicFields.isEmpty()) {
            List<UserProfileValueEntity> values = userProfileValueRepository.findByUserIdAndFieldFieldNameIn(userId, dynamicFields);
            for (UserProfileValueEntity valueEntity : values) {
                String fieldName = valueEntity.getField().getFieldName();
                result.put(fieldName, DynamicValueConverter.readValue(valueEntity, valueEntity.getField().getDataType()));
            }
            for (String fieldName : dynamicFields) {
                result.putIfAbsent(fieldName, null);
            }
        }

        return result;
    }

    @Override
    public void setForCurrentUser(Map<String, Object> updates) {
        setForUser(currentUserProvider.getCurrentUserId(), updates);
    }

    public void setForUser(Long userId, Map<String, Object> updates) {
        if (updates == null || updates.isEmpty()) {
            return;
        }

        Map<String, Object> sanitizedUpdates = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String sanitized = FieldNameSanitizer.sanitize(entry.getKey(), RESERVED_NAMES);
            sanitizedUpdates.put(sanitized, entry.getValue());
        }

        List<UserProfileFieldEntity> fields = userProfileFieldRepository.findByFieldNameInAndEnabledTrue(sanitizedUpdates.keySet());
        if (fields.size() != sanitizedUpdates.size()) {
            Set<String> found = fields.stream().map(UserProfileFieldEntity::getFieldName).collect(Collectors.toSet());
            List<String> missing = new ArrayList<>();
            for (String key : sanitizedUpdates.keySet()) {
                if (!found.contains(key)) {
                    missing.add(key);
                }
            }
            throw new BadRequestException("Unknown dynamic fields: " + String.join(",", missing));
        }

        Map<Long, UserProfileFieldEntity> fieldById = fields.stream().collect(Collectors.toMap(UserProfileFieldEntity::getId, f -> f));
        Map<String, UserProfileFieldEntity> fieldByName = fields.stream().collect(Collectors.toMap(UserProfileFieldEntity::getFieldName, f -> f));

        Collection<Long> fieldIds = fieldById.keySet();
        List<UserProfileValueEntity> existingValues = userProfileValueRepository.findByUserIdAndFieldIdIn(userId, fieldIds);
        Map<Long, UserProfileValueEntity> existingByFieldId = existingValues.stream()
            .collect(Collectors.toMap(value -> value.getField().getId(), value -> value));

        List<UserProfileValueEntity> toSave = new ArrayList<>();
        for (Map.Entry<String, Object> entry : sanitizedUpdates.entrySet()) {
            UserProfileFieldEntity field = fieldByName.get(entry.getKey());
            validateReferenceValue(field, entry.getValue());
            UserProfileValueEntity valueEntity = existingByFieldId.get(field.getId());
            if (valueEntity == null) {
                valueEntity = new UserProfileValueEntity();
                valueEntity.setUserId(userId);
                valueEntity.setField(field);
            }
            DynamicValueConverter.applyValue(valueEntity, field.getDataType(), entry.getValue());
            toSave.add(valueEntity);
        }

        userProfileValueRepository.saveAll(toSave);
    }

    private Set<String> resolveRequestedColumns(String... columns) {
        if (columns == null || columns.length == 0) {
            Set<String> all = new LinkedHashSet<>(DEFAULT_FIELDS);
            List<UserProfileFieldEntity> dynamicFields = userProfileFieldRepository.findByEnabledTrue();
            for (UserProfileFieldEntity field : dynamicFields) {
                all.add(field.getFieldName());
            }
            return all;
        }

        Set<String> requested = Arrays.stream(columns)
            .filter(column -> column != null && !column.isBlank())
            .map(String::trim)
            .collect(Collectors.toCollection(LinkedHashSet::new));

        if (requested.isEmpty()) {
            return resolveRequestedColumns();
        }

        for (String field : requested) {
            String lower = field.toLowerCase();
            if (RESERVED_NAMES.contains(lower) && !DEFAULT_FIELDS.contains(field)) {
                throw new BadRequestException("Access denied for column: " + field);
            }
        }

        return requested;
    }

    private ReferenceOptions validateReferenceOptions(ReferenceOptions options) {
        if (options == null) {
            throw new BadRequestException("ReferenceOptions are required for REFERENCE data type");
        }
        if (options.getTarget() == null || options.getTarget().isBlank()) {
            throw new BadRequestException("Reference target is required for REFERENCE data type");
        }
        if (options.getKey() == null || options.getKey().isBlank()) {
            options.setKey("id");
        }
        return options;
    }

    private void validateReferenceValue(UserProfileFieldEntity field, Object value) {
        if (!DataType.REFERENCE.equals(field.getDataType())) {
            return;
        }
        if (value == null) {
            if (field.isReferenceRequired()) {
                throw new BadRequestException("Reference field '" + field.getFieldName() + "' is required");
            }
            return;
        }
        if (field.getReferenceTarget() == null || field.getReferenceTarget().isBlank()) {
            throw new BadRequestException("Reference field '" + field.getFieldName() + "' has no target metadata");
        }
        String key = (field.getReferenceKey() == null || field.getReferenceKey().isBlank()) ? "id" : field.getReferenceKey();
        boolean exists = referenceLookupService.exists(field.getReferenceTarget(), key, value);
        if (!exists) {
            throw new BadRequestException(
                "Invalid reference for field '" + field.getFieldName() + "': target="
                    + field.getReferenceTarget() + ", key=" + key + ", value=" + value
            );
        }
    }
}
