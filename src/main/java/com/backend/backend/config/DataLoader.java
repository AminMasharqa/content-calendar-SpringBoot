package com.backend.backend.config;

import com.backend.backend.model.Content;
import com.backend.backend.repository.ContentRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import java.io.InputStream;
import java.util.List;

@Component // enable the loader
public class DataLoader implements CommandLineRunner {

    private final ContentRepository repository;
    private final ObjectMapper objectMapper; // Spring’s pre-configured mapper

    public DataLoader(ContentRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello From DataLoader!");

        // Make sure the file is at: src/main/resources/data/content.json
        ClassPathResource resource = new ClassPathResource("data/content.json");

        if (!resource.exists()) {
            System.out.println("content.json not found on classpath");
            return;
        }

        try (InputStream is = resource.getInputStream()) {
            List<Content> contents = objectMapper.readValue(is, new TypeReference<List<Content>>() {});
            repository.saveAll(contents);
            System.out.printf("Inserted %d contents%n", contents.size());
        }
    }
}
