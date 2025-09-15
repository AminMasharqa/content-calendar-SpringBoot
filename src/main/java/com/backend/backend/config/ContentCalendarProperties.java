package com.backend.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("cc")
public record ContentCalendarProperties(
        String welcomeMessage,
        String about
) {}
