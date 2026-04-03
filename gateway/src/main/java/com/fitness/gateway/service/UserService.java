package com.fitness.gateway.service;

import com.fitness.gateway.dto.RegisterRequest;
import com.fitness.gateway.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final WebClient userServiceWebClient;

    public Mono<Boolean> validateUser(String userId){

            return userServiceWebClient.get()
                    .uri("/api/users/{userId}/validate", userId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .onErrorResume(WebClientResponseException.class,e -> {
                            if(e.getStatusCode() == HttpStatus.NOT_FOUND){
                                throw new RuntimeException("User Not found with id" + userId);
                            }
                            else if(e.getStatusCode() == HttpStatus.BAD_REQUEST){
                                throw new RuntimeException("Invalid Request");
                            }
                            return Mono.error(new RuntimeException("Unexpected error : "+e.getMessage()));
                        });

    }

    public Mono<UserResponse> registerUser(RegisterRequest request) {
        log.info("calling user Registration API for email : {}",request.getEmail());
        return userServiceWebClient.post()
                .uri("/api/users/register")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .onErrorResume(WebClientResponseException.class,e -> {
                    if(e.getStatusCode() == HttpStatus.BAD_REQUEST){
                        throw new RuntimeException("Bad request " + e.getMessage());
                    }
                    else if(e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR){
                        throw new RuntimeException("Internal server error"+ e.getMessage());
                    }
                    return Mono.error(new RuntimeException("Unexpected error : "+  e.getMessage()));
                });
    }
}
