package com.easyprofile.authlib.repository;

import com.easyprofile.authlib.entity.UserProfileValueEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserProfileValueRepository extends JpaRepository<UserProfileValueEntity, Long> {

    @EntityGraph(attributePaths = "field")
    List<UserProfileValueEntity> findByUserId(Long userId);

    @EntityGraph(attributePaths = "field")
    List<UserProfileValueEntity> findByUserIdAndFieldFieldNameIn(Long userId, Collection<String> fieldNames);

    @EntityGraph(attributePaths = "field")
    List<UserProfileValueEntity> findByUserIdAndFieldIdIn(Long userId, Collection<Long> fieldIds);

    @EntityGraph(attributePaths = "field")
    Optional<UserProfileValueEntity> findByUserIdAndFieldId(Long userId, Long fieldId);
}
