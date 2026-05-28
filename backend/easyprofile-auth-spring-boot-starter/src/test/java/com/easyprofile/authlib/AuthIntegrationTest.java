package com.easyprofile.authlib;

import cn.dev33.satoken.stp.StpUtil;
import com.easyprofile.authlib.api.DynamicProfileService;
import com.easyprofile.authlib.dto.request.ReferenceOptions;
import com.easyprofile.authlib.enums.DataType;
import com.easyprofile.authlib.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {TestStarterApplication.class, ReferenceLookupTestConfig.class})
@AutoConfigureMockMvc
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DynamicProfileService dynamicProfileService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void register_shouldCreateUserAndReturnToken() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        String payload = """
            {
              "username":"user_%s",
              "email":"%s@example.com",
              "password":"Pass12345!",
              "confirmPassword":"Pass12345!"
            }
            """.formatted(suffix, suffix);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.token").isString())
            .andExpect(jsonPath("$.data.user.password").doesNotExist());

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isConflict());
    }

    @Test
    void login_shouldSupportUsernameAndEmail() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        register(suffix);

        String byUsername = """
            {"usernameOrEmail":"user_%s","password":"Pass12345!"}
            """.formatted(suffix);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(byUsername))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.token").isString());

        String byEmail = """
            {"usernameOrEmail":"%s@example.com","password":"Pass12345!"}
            """.formatted(suffix);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(byEmail))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.user.username").value("user_" + suffix));
    }

    @Test
    void me_shouldReturnAuthenticatedUser() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        String token = registerAndGetToken(suffix);

        mockMvc.perform(get("/auth/me")
                .header("satoken", token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.user.username").value("user_" + suffix))
            .andExpect(jsonPath("$.data.token").isString())
            .andExpect(jsonPath("$.data.user.password").doesNotExist());
    }

    @Test
    void update_shouldPatchDefaultAndDynamicFields() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        dynamicProfileService.addColumn("age", DataType.INT);
        dynamicProfileService.addColumn("bio", DataType.TEXT);

        String token = registerAndGetToken(suffix);

        String payload = """
            {
              "username":"updated_%s",
              "age":30,
              "bio":"Hello profile"
            }
            """.formatted(suffix);

        mockMvc.perform(patch("/auth/update")
                .header("satoken", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.username").value("updated_" + suffix));

        Long userId = userRepository.findByUsername("updated_" + suffix).orElseThrow().getId();
        StpUtil.login(userId);
        Map<String, Object> values = dynamicProfileService.get("age", "bio");
        StpUtil.logout();

        assertThat(values.get("age")).isEqualTo(30);
        assertThat(values.get("bio")).isEqualTo("Hello profile");
    }

    @Test
    void resetPassword_shouldEnforceCooldown() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        String token = registerAndGetToken(suffix);

        String first = """
            {
              "oldPassword":"Pass12345!",
              "newPassword":"NewPass123!",
              "confirmPassword":"NewPass123!"
            }
            """;

        MvcResult firstResult = mockMvc.perform(post("/auth/resetPassword")
                .header("satoken", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(first))
            .andExpect(status().isOk())
            .andReturn();

        String newToken = extractToken(firstResult);

        String second = """
            {
              "oldPassword":"NewPass123!",
              "newPassword":"AnotherPass123!",
              "confirmPassword":"AnotherPass123!"
            }
            """;

        mockMvc.perform(post("/auth/resetPassword")
                .header("satoken", newToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(second))
            .andExpect(status().isBadRequest());
    }

    @Test
    void dynamicProfile_addColumnAndGet_shouldWorkAndBlockDuplicates() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        String fieldName = "f_" + suffix;
        dynamicProfileService.addColumn(fieldName, DataType.STRING);

        try {
            dynamicProfileService.addColumn(fieldName, DataType.STRING);
        } catch (Exception ex) {
            assertThat(ex.getMessage()).contains("already exists");
            return;
        }

        throw new AssertionError("Expected duplicate field creation to fail");
    }

    @Test
    void dynamicProfile_referenceRegistration_shouldRequireOptions() {
        try {
            dynamicProfileService.addColumn("favoriteForumId", DataType.REFERENCE);
        } catch (Exception ex) {
            assertThat(ex.getMessage()).contains("ReferenceOptions are required");
            return;
        }
        throw new AssertionError("Expected REFERENCE registration without options to fail");
    }

    @Test
    void dynamicProfile_referenceValue_shouldValidateAgainstLookupService() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        dynamicProfileService.addColumn(
            "favoriteForumId",
            DataType.REFERENCE,
            ReferenceOptions.of("forum", "id", true)
        );

        String token = registerAndGetToken(suffix);

        String okPayload = """
            {
              "favoriteForumId": 1
            }
            """;

        mockMvc.perform(patch("/auth/update")
                .header("satoken", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(okPayload))
            .andExpect(status().isOk());

        String badPayload = """
            {
              "favoriteForumId": 999
            }
            """;

        mockMvc.perform(patch("/auth/update")
                .header("satoken", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(badPayload))
            .andExpect(status().isBadRequest());
    }

    @Test
    void avatarUpload_shouldUpdateAvatarUrl() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        String token = registerAndGetToken(suffix);

        MockMultipartFile mockFile = new MockMultipartFile(
            "file",
            "avatar.png",
            "image/png",
            "fake-image".getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/auth/avatar")
                .file(mockFile)
                .header("satoken", token)
                .with(request -> {
                    request.setMethod("POST");
                    return request;
                }))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.avatarUrl").isString());
    }

    @Test
    void corsPreflight_shouldAllowConfiguredOriginForLogin() throws Exception {
        mockMvc.perform(options("/auth/login")
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "POST"))
            .andExpect(status().isOk())
            .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"));
    }

    @Test
    void corsPreflight_shouldRejectDisallowedOriginForLogin() throws Exception {
        mockMvc.perform(options("/auth/login")
                .header("Origin", "http://localhost:9999")
                .header("Access-Control-Request-Method", "POST"))
            .andExpect(status().isForbidden());
    }

    private void register(String suffix) throws Exception {
        String payload = """
            {
              "username":"user_%s",
              "email":"%s@example.com",
              "password":"Pass12345!",
              "confirmPassword":"Pass12345!"
            }
            """.formatted(suffix, suffix);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isCreated());
    }

    private String registerAndGetToken(String suffix) throws Exception {
        String payload = """
            {
              "username":"user_%s",
              "email":"%s@example.com",
              "password":"Pass12345!",
              "confirmPassword":"Pass12345!"
            }
            """.formatted(suffix, suffix);

        MvcResult result = mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isCreated())
            .andReturn();

        return extractToken(result);
    }

    private String extractToken(MvcResult result) throws Exception {
        Map<String, Object> json = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });

        Map<String, Object> data = (Map<String, Object>) json.get("data");
        return (String) data.get("token");
    }
}
