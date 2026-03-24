package com.fitness.userservice.dto;

import com.fitness.userservice.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Data
public class RegisterRequest {

    @NotBlank(message = "email not be blank ")
    @Email(message = "invalid email message ")
    private String email;

    @NotBlank
    @Size(min = 6,message = "should be greater than 6 char")
    private String password;

    private String firstName;
    private String lastName;
}
