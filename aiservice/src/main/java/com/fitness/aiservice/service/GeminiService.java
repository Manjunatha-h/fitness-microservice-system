package com.fitness.aiservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@Slf4j
public class GeminiService {

    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String  geminiApiUrl;

    @Value("${gemini.api.key}")
    private String getGeminiApiKey;

    public GeminiService(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }

    public String getAnswer(String prompt){

        //request body format
        Map<String,Object> requestBody = Map.of(
                "contents",new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );

        String answer = webClient.post()
                .uri(geminiApiUrl + getGeminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return answer;
    }
}
