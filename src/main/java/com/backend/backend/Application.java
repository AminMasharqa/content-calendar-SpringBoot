package com.backend.backend;

import com.backend.backend.config.ContentCalendarProperties;
import com.backend.backend.model.Content;
import com.backend.backend.model.Status;
import com.backend.backend.model.Type;
import com.backend.backend.repository.ContentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@EnableConfigurationProperties(ContentCalendarProperties.class)
@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

    }

    @Bean
    CommandLineRunner commandLineRunner(ContentRepository repository){
        return args -> {
        //            Insert some data into the DB
            Content content = new Content(
                    null,
                    "HELLO CHATGPT",
                    "ALL ABOUT ChatGPT",
                    Status.IDEA,
                    Type.VIDEO,
                    LocalDateTime.now(),
                    null,
                    ""
            );
            repository.save(content);

        };
    }

}
