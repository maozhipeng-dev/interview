package com.example.interview.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "ai.bailian")
public class AiConfig {

    private String apiKey;

    private String model = "qwen-turbo";

    private Double temperature = 0.7;

    private Integer maxTokens = 2000;
}
