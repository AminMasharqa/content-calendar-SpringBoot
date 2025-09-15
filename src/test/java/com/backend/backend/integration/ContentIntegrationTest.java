package com.backend.backend.integration;

import com.backend.backend.model.Content;
import com.backend.backend.model.Status;
import com.backend.backend.model.Type;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class ContentIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void fullCrudWorkflow_ShouldWorkCorrectly() throws Exception {
        Content newContent = new Content(
            null,
            "Integration Test Content",
            "Test Description",
            Status.IDEA,
            Type.ARTICLE,
            LocalDateTime.now(),
            null,
            "https://test.com"
        );

        // CREATE
        String response = mockMvc.perform(post("/api/content")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newContent)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // READ ALL - should contain our new content
        mockMvc.perform(get("/api/content"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.title == 'Integration Test Content')]").exists());

        // Assuming we can get the ID from the response or database
        // For now, we'll use ID 1 (adjust based on your setup)
        int testId = 1;

        // READ BY ID
        mockMvc.perform(get("/api/content/" + testId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists());

        // UPDATE
        Content updatedContent = new Content(
            testId,
            "Updated Title",
            "Updated Description",
            Status.IN_PROGRESS,
            Type.VIDEO,
            LocalDateTime.now(),
            LocalDateTime.now(),
            "https://updated.com"
        );

        mockMvc.perform(put("/api/content/" + testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedContent)))
                .andExpect(status().isNoContent());

        // VERIFY UPDATE
        mockMvc.perform(get("/api/content/" + testId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));

        // SEARCH BY TITLE
        mockMvc.perform(get("/api/content/filter/Updated"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Updated Title"));

        // SEARCH BY STATUS
        mockMvc.perform(get("/api/content/filter/status/IN_PROGRESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("IN_PROGRESS"));

        // DELETE
        mockMvc.perform(delete("/api/content/" + testId))
                .andExpect(status().isNoContent());

        // VERIFY DELETION
        mockMvc.perform(get("/api/content/" + testId))
                .andExpect(status().isNotFound());
    }

    @Test
    void homeEndpoint_ShouldReturnProperties() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.welcomeMessage").exists())
                .andExpect(jsonPath("$.about").exists());
    }
}
