package com.fitness.userservice.service;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.entity.User;
import com.fitness.userservice.respository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;



    public UserResponse register(@Valid RegisterRequest request) {

        if(userRepository.existsByEmail(request.getEmail())){

            User existingUser = userRepository.findByEmail(request.getEmail());

            UserResponse userResponse = new UserResponse();

            userResponse.setId(existingUser.getId());
            userResponse.setEmail(existingUser.getEmail());
            userResponse.setPassword(existingUser.getPassword());
            userResponse.setFirstName(existingUser.getFirstName());
            userResponse.setLastName(existingUser.getLastName());
            userResponse.setCreatedAt(existingUser.getCreatedAt());
            userResponse.setUpdatedAt(existingUser.getUpdatedAt());
            userResponse.setKeyCloakId(existingUser.getKeyCloakId());
            return userResponse;
        }


        User user =  new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setKeyCloakId(request.getKeyCloakId());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        User savedUser = userRepository.save(user);

        UserResponse userResponse = new UserResponse();

        userResponse.setId(savedUser.getId());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setPassword(savedUser.getPassword());
        userResponse.setKeyCloakId(savedUser.getKeyCloakId());
        userResponse.setFirstName(savedUser.getFirstName());
        userResponse.setLastName(savedUser.getLastName());
        userResponse.setCreatedAt(savedUser.getCreatedAt());
        userResponse.setUpdatedAt(savedUser.getUpdatedAt());

        return userResponse;
    }


    public UserResponse getUserById(String userID) {
        User referenceById = userRepository.findById(userID)
                .orElseThrow(
                    ()-> new RuntimeException("user not found")
                );

        UserResponse userResponse = new UserResponse();

        userResponse.setId(referenceById.getId());
        userResponse.setEmail(referenceById.getEmail());
        userResponse.setPassword(referenceById.getPassword());
        userResponse.setFirstName(referenceById.getFirstName());
        userResponse.setLastName(referenceById.getLastName());
        userResponse.setCreatedAt(referenceById.getCreatedAt());
        userResponse.setUpdatedAt(referenceById.getUpdatedAt());

        return userResponse;
    }

    public Boolean existByUerId(String userId) {
        return userRepository.existsById(userId);
    }

    public Boolean existByKeyCloakId(String userId) {
        return userRepository.existsByKeyCloakId(userId);
    }
}
