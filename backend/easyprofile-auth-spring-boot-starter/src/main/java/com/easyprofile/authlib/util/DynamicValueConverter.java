package com.easyprofile.authlib.util;

import com.easyprofile.authlib.entity.UserProfileValueEntity;
import com.easyprofile.authlib.enums.DataType;
import com.easyprofile.authlib.exception.BadRequestException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class DynamicValueConverter {

    private DynamicValueConverter() {
    }

    public static void applyValue(UserProfileValueEntity entity, DataType dataType, Object value) {
        clear(entity);
        if (value == null) {
            return;
        }

        try {
            switch (dataType) {
                case STRING -> entity.setStringValue(String.valueOf(value));
                case INT -> entity.setIntValue(value instanceof Number number ? number.intValue() : Integer.parseInt(String.valueOf(value)));
                case LONG -> entity.setLongValue(value instanceof Number number ? number.longValue() : Long.parseLong(String.valueOf(value)));
                case DOUBLE -> entity.setDoubleValue(value instanceof Number number ? number.doubleValue() : Double.parseDouble(String.valueOf(value)));
                case BOOLEAN -> entity.setBooleanValue(value instanceof Boolean bool ? bool : Boolean.parseBoolean(String.valueOf(value)));
                case DATE -> entity.setDateValue(value instanceof LocalDate date ? date : LocalDate.parse(String.valueOf(value)));
                case DATETIME -> entity.setDatetimeValue(value instanceof LocalDateTime dateTime ? dateTime : LocalDateTime.parse(String.valueOf(value)));
                case TEXT -> entity.setTextValue(String.valueOf(value));
                case REFERENCE -> entity.setLongValue(value instanceof Number number ? number.longValue() : Long.parseLong(String.valueOf(value)));
                default -> throw new BadRequestException("Unsupported data type: " + dataType);
            }
        } catch (RuntimeException ex) {
            throw new BadRequestException("Invalid value for " + dataType + ": " + value);
        }
    }

    public static Object readValue(UserProfileValueEntity entity, DataType dataType) {
        return switch (dataType) {
            case STRING -> entity.getStringValue();
            case INT -> entity.getIntValue();
            case LONG -> entity.getLongValue();
            case DOUBLE -> entity.getDoubleValue();
            case BOOLEAN -> entity.getBooleanValue();
            case DATE -> entity.getDateValue();
            case DATETIME -> entity.getDatetimeValue();
            case TEXT -> entity.getTextValue();
            case REFERENCE -> entity.getLongValue();
        };
    }

    private static void clear(UserProfileValueEntity entity) {
        entity.setStringValue(null);
        entity.setIntValue(null);
        entity.setLongValue(null);
        entity.setDoubleValue(null);
        entity.setBooleanValue(null);
        entity.setDateValue(null);
        entity.setDatetimeValue(null);
        entity.setTextValue(null);
    }
}
