package com.fitness.activityservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.beans.JavaBean;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced // to resolve the service name via eureka so "port may change but not the name"
    public WebClient.Builder webClientBuilder(){
        return WebClient.builder();
    }

    @Bean
    public WebClient userServiceWebClient(WebClient.Builder webClientBuilder){
        return webClientBuilder
                .baseUrl("http://USER-SERVICE")
                .build();
    }
}
