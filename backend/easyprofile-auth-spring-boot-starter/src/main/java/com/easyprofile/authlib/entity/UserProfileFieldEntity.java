package com.easyprofile.authlib.entity;

import com.easyprofile.authlib.enums.DataType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_profile_fields")
public class UserProfileFieldEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "field_name", nullable = false, unique = true, length = 64)
    private String fieldName;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", nullable = false, length = 16)
    private DataType dataType;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(name = "reference_target", length = 128)
    private String referenceTarget;

    @Column(name = "reference_key", length = 64)
    private String referenceKey;

    @Column(name = "reference_required", nullable = false)
    private boolean referenceRequired;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getReferenceTarget() {
        return referenceTarget;
    }

    public void setReferenceTarget(String referenceTarget) {
        this.referenceTarget = referenceTarget;
    }

    public String getReferenceKey() {
        return referenceKey;
    }

    public void setReferenceKey(String referenceKey) {
        this.referenceKey = referenceKey;
    }

    public boolean isReferenceRequired() {
        return referenceRequired;
    }

    public void setReferenceRequired(boolean referenceRequired) {
        this.referenceRequired = referenceRequired;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
