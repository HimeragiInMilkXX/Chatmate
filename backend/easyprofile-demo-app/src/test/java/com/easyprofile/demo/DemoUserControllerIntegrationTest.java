package com.easyprofile.demo;

import com.easyprofile.authlib.api.DynamicProfileService;
import com.easyprofile.authlib.entity.UserEntity;
import com.easyprofile.authlib.entity.UserProfileFieldEntity;
import com.easyprofile.authlib.entity.UserProfileValueEntity;
import com.easyprofile.authlib.enums.DataType;
import com.easyprofile.authlib.repository.UserProfileFieldRepository;
import com.easyprofile.authlib.repository.UserProfileValueRepository;
import com.easyprofile.authlib.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EasyProfileDemoApplication.class)
@AutoConfigureMockMvc
class DemoUserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DynamicProfileService dynamicProfileService;

    @Autowired
    private UserProfileFieldRepository userProfileFieldRepository;

    @Autowired
    private UserProfileValueRepository userProfileValueRepository;

    @Test
    void getUserById_shouldReturnUserWithDynamicProfile_publicly() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        UserEntity user = new UserEntity();
        user.setUsername("demo_" + suffix);
        user.setEmail("demo_" + suffix + "@example.com");
        user.setPasswordHash("$2a$10$placeholderHashForTestOnly");
        user = userRepository.save(user);

        String fieldName = "bio_" + suffix;
        dynamicProfileService.addColumn(fieldName, DataType.TEXT);
        UserProfileFieldEntity field = userProfileFieldRepository.findByFieldName(fieldName).orElseThrow();

        UserProfileValueEntity value = new UserProfileValueEntity();
        value.setUserId(user.getId());
        value.setField(field);
        value.setTextValue("Forum lover");
        userProfileValueRepository.save(value);

        mockMvc.perform(get("/user/get/{id}", user.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.id").value(user.getId()))
            .andExpect(jsonPath("$.data.username").value("demo_" + suffix))
            .andExpect(jsonPath("$.data.email").value("demo_" + suffix + "@example.com"))
            .andExpect(jsonPath("$.data.password").doesNotExist())
            .andExpect(jsonPath("$.data.profile." + fieldName).value("Forum lover"));
    }

    @Test
    void getUserById_shouldReturn404_whenUserNotFound() throws Exception {
        mockMvc.perform(get("/user/get/{id}", 999999L))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").value("User not found"));
    }
}
