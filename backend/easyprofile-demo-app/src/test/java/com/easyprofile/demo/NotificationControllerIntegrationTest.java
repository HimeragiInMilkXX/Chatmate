package com.easyprofile.demo;

import com.easyprofile.authlib.entity.UserEntity;
import com.easyprofile.authlib.repository.UserRepository;
import com.easyprofile.demo.repository.NotificationRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EasyProfileDemoApplication.class)
@AutoConfigureMockMvc
class NotificationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createNotification_shouldPersistAndReturnData() throws Exception {
        UserEntity user = createUser();

        String payload = """
            {
              "userId": %d,
              "content": "Welcome notification"
            }
            """.formatted(user.getId());

        mockMvc.perform(post("/notification/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.userId").value(user.getId()))
            .andExpect(jsonPath("$.data.content").value("Welcome notification"))
            .andExpect(jsonPath("$.data.confirmed").value(false));

        assertThat(notificationRepository.findByUserIdOrderByNotificationAtDesc(user.getId())).hasSize(1);
    }

    @Test
    void listByUser_shouldReturnOnlyThatUsersNotifications() throws Exception {
        UserEntity userA = createUser();
        UserEntity userB = createUser();

        createNotification(userA.getId(), "A1");
        createNotification(userA.getId(), "A2");
        createNotification(userB.getId(), "B1");

        mockMvc.perform(get("/notification/user/{userId}", userA.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.length()").value(2))
            .andExpect(jsonPath("$.data[0].userId").value(userA.getId()))
            .andExpect(jsonPath("$.data[1].userId").value(userA.getId()));
    }

    @Test
    void createNotification_shouldReturn404_whenUserMissing() throws Exception {
        String payload = """
            {
              "userId": 999999,
              "content": "Hello"
            }
            """;

        mockMvc.perform(post("/notification/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void confirmNotification_shouldToggleConfirmedTrue() throws Exception {
        UserEntity user = createUser();
        Long notificationId = createNotification(user.getId(), "Please confirm");

        mockMvc.perform(patch("/notification/confirm/{id}", notificationId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.id").value(notificationId))
            .andExpect(jsonPath("$.data.confirmed").value(true));
    }

    private UserEntity createUser() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        UserEntity user = new UserEntity();
        user.setUsername("notify_" + suffix);
        user.setEmail("notify_" + suffix + "@example.com");
        user.setPasswordHash("$2a$10$placeholderHashForNotificationTests");
        return userRepository.save(user);
    }

    private Long createNotification(Long userId, String content) throws Exception {
        String payload = """
            {
              "userId": %d,
              "content": "%s"
            }
            """.formatted(userId, content);

        MvcResult result = mockMvc.perform(post("/notification/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isOk())
            .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        return root.path("data").path("id").asLong();
    }
}
