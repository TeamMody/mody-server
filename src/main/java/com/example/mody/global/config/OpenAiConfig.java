package com.example.mody.global.config;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OpenAiConfig {

    @Value("${openai.secret-key}")
    private String apiKey;

    private static final String OPENAI_BASE_URL = "https://api.openai.com/v1";

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(OPENAI_BASE_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
