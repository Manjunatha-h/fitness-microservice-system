package com.fitness.gateway.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class RegisterRequest {

    @NotBlank(message = "email not be blank ")
    @Email(message = "invalid email message ")
    private String email;

    private String keyCloakId;

    @NotBlank
    @Size(min = 6,message = "should be greater than 6 char")
    private String password;

    private String firstName;
    private String lastName;
}
