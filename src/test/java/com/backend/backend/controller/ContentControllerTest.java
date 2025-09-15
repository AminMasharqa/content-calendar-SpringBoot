package com.backend.backend.controller;

import com.backend.backend.model.Content;
import com.backend.backend.model.Status;
import com.backend.backend.model.Type;
import com.backend.backend.repository.ContentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContentController.class)
class ContentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContentRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private Content testContent;

    @BeforeEach
    void setUp() {
        testContent = new Content(
            1,
            "Test Title",
            "Test Description",
            Status.IDEA,
            Type.ARTICLE,
            LocalDateTime.now(),
            null,
            "https://example.com"
        );
    }

    @Test
    void findAll_ShouldReturnAllContent() throws Exception {
        List<Content> contentList = Arrays.asList(testContent);
        when(repository.findAll()).thenReturn(contentList);

        mockMvc.perform(get("/api/content"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Title"));

        verify(repository).findAll();
    }

    @Test
    void findById_ShouldReturnContent_WhenExists() throws Exception {
        when(repository.findById(1)).thenReturn(Optional.of(testContent));

        mockMvc.perform(get("/api/content/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"));

        verify(repository).findById(1);
    }

    @Test
    void findById_ShouldReturn404_WhenNotExists() throws Exception {
        when(repository.findById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/content/999"))
                .andExpect(status().isNotFound());

        verify(repository).findById(999);
    }

    @Test
    void create_ShouldCreateContent() throws Exception {
        when(repository.save(any(Content.class))).thenReturn(testContent);

        mockMvc.perform(post("/api/content")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testContent)))
                .andExpect(status().isCreated());

        verify(repository).save(any(Content.class));
    }

    @Test
    void update_ShouldUpdateContent_WhenExists() throws Exception {
        when(repository.existsById(1)).thenReturn(true);
        when(repository.save(any(Content.class))).thenReturn(testContent);

        mockMvc.perform(put("/api/content/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testContent)))
                .andExpect(status().isNoContent());

        verify(repository).existsById(1);
        verify(repository).save(any(Content.class));
    }

    @Test
    void update_ShouldReturn404_WhenNotExists() throws Exception {
        when(repository.existsById(999)).thenReturn(false);

        mockMvc.perform(put("/api/content/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testContent)))
                .andExpect(status().isNotFound());

        verify(repository).existsById(999);
        verify(repository, never()).save(any());
    }

    @Test
    void delete_ShouldDeleteContent_WhenExists() throws Exception {
        when(repository.existsById(1)).thenReturn(true);

        mockMvc.perform(delete("/api/content/1"))
                .andExpect(status().isNoContent());

        verify(repository).existsById(1);
        verify(repository).deleteById(1);
    }

    @Test
    void delete_ShouldReturn404_WhenNotExists() throws Exception {
        when(repository.existsById(999)).thenReturn(false);

        mockMvc.perform(delete("/api/content/999"))
                .andExpect(status().isNotFound());

        verify(repository).existsById(999);
        verify(repository, never()).deleteById(anyInt());
    }

    @Test
    void findByTitle_ShouldReturnMatchingContent() throws Exception {
        List<Content> contentList = Arrays.asList(testContent);
        when(repository.findAllByTitleContains("Test")).thenReturn(contentList);

        mockMvc.perform(get("/api/content/filter/Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Title"));

        verify(repository).findAllByTitleContains("Test");
    }

    @Test
    void findByStatus_ShouldReturnMatchingContent() throws Exception {
        List<Content> contentList = Arrays.asList(testContent);
        when(repository.listByStatus(Status.IDEA)).thenReturn(contentList);

        mockMvc.perform(get("/api/content/filter/status/IDEA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("IDEA"));

        verify(repository).listByStatus(Status.IDEA);
    }
}
