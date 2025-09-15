package com.backend.backend.controller;

import com.backend.backend.config.ContentCalendarProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    private final ContentCalendarProperties properties;

    public HomeController(ContentCalendarProperties properties){
        this.properties=properties;
    }




    @GetMapping("/")
    public ContentCalendarProperties home() {
        return properties;
    }





}
