package com.easyprofile.authlib.repository;

import com.easyprofile.authlib.entity.UserProfileFieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserProfileFieldRepository extends JpaRepository<UserProfileFieldEntity, Long> {

    Optional<UserProfileFieldEntity> findByFieldName(String fieldName);

    List<UserProfileFieldEntity> findByFieldNameInAndEnabledTrue(Collection<String> fieldNames);

    List<UserProfileFieldEntity> findByEnabledTrue();
}
