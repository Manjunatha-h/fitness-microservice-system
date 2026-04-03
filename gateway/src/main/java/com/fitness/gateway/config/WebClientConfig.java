package com.fitness.gateway.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced  // to resolve the service name via eureka so "port may change but not the name"
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


//### 🔹 WebClient Configuration (Service-to-Service Communication) — Simple Explanation
//
//#### 1. What this class does
//
//* This configuration creates **WebClient beans** used by the Gateway to call other microservices.
//* Instead of hardcoding IP/port, it uses **service names** via Eureka.
//
//---
//
//#### 2. What is `WebClient`?
//
//* It is a **non-blocking HTTP client** (used in WebFlux).
//* Used to call APIs of other microservices asynchronously.
//
//---
//
//#### 3. What is `WebClient.Builder` bean?
//
//```java
//@Bean
//@LoadBalanced
//public WebClient.Builder webClientBuilder()
//```
//
//* Creates a reusable builder for WebClient.
//
//* `@LoadBalanced` means:
//  → You can use **service names instead of IP/port**
//  → Example:
//  `http://USER-SERVICE` instead of `http://localhost:8081`
//
//* Internally:
//
//  * It uses Eureka to resolve service instances
//  * Handles load balancing automatically
//
//---
//
//#### 4. Why use Builder instead of direct WebClient?
//
//* Builder allows:
//
//  * Reuse common configuration
//  * Create multiple WebClients for different services
//* Think of it like:
//  → “Base template to create clients”
//
//---
//
//#### 5. What is `userServiceWebClient`?
//
//```java
//@Bean
//public WebClient userServiceWebClient(WebClient.Builder webClientBuilder)
//```
//
//* This creates a **specific WebClient for USER-SERVICE**
//
//* Sets base URL:
//  `http://USER-SERVICE`
//
//* So later you can call APIs like:
//
//  ```java
//  webClient.get().uri("/api/users")
//  ```
//
//  → It becomes:
//  `http://USER-SERVICE/api/users`
//
//---
//
//#### 6. How it works internally
//
//1. Gateway calls:
//   `http://USER-SERVICE/api/...`
//2. Eureka resolves:
//   → actual instance (like localhost:8081)
//3. Request is sent to that service
//4. If multiple instances exist:
//   → Load balancer chooses one
//
//---
//
//#### 7. Why this is useful
//
//* No need to worry about:
//
//  * Changing ports
//  * Multiple instances
//* Makes microservices **loosely coupled**
//
//---
//
//#### 8. Final flow
//
//Gateway → WebClient → Service Name
//→ Eureka resolves → Actual Service Instance
//→ Request sent → Response received
//
//---
//
//#### 9. One-line summary
//
//“This configuration allows the Gateway to call microservices using service names instead of fixed URLs, with built-in load balancing.”
//
//---