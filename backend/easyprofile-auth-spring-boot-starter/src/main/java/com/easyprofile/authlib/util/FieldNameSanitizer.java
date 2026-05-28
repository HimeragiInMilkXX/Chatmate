package com.easyprofile.authlib.util;

import com.easyprofile.authlib.exception.BadRequestException;

import java.util.Set;
import java.util.regex.Pattern;

public final class FieldNameSanitizer {

    private static final Pattern FIELD_NAME_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{1,63}$");

    private FieldNameSanitizer() {
    }

    public static String sanitize(String input, Set<String> reservedNames) {
        if (input == null || input.isBlank()) {
            throw new BadRequestException("Field name cannot be blank");
        }

        String normalized = input.trim();
        if (!FIELD_NAME_PATTERN.matcher(normalized).matches()) {
            throw new BadRequestException("Invalid field name: " + input);
        }

        if (reservedNames.contains(normalized.toLowerCase())) {
            throw new BadRequestException("Field name is reserved: " + normalized);
        }

        return normalized;
    }
}
