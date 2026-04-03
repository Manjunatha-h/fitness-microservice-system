package com.fitness.activityservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
public class UserValidationService {
    private final WebClient userServiceWebClient;

    public Boolean validateUser(String userId){
        try{
            return userServiceWebClient.get()
                    .uri("/api/users/{userId}/validate", userId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            //Step-by-step:
            //
            //WebClient sees:
            //
            //http://USER-SERVICE/api/users/{userId}/validate
            //
            //It asks Eureka:
            //
            //“Where is USER-SERVICE?”
            //
            //Eureka returns:
            //
            //localhost:8081
            //
            //WebClient sends request to:
            //
            //http://localhost:8081/api/users/{userId}/validate

        }
        catch (WebClientResponseException e){
            if(e.getStatusCode() == HttpStatus.NOT_FOUND){
                throw new RuntimeException("User Not found with id" + userId);
            }
            else if(e.getStatusCode() == HttpStatus.BAD_REQUEST){
                throw new RuntimeException("Invalid Request");
            }
        }
        return false;
    }

}
