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
            throw new RuntimeException("Email alreday exist");
        }


        User user =  new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        User save = userRepository.save(user);

        UserResponse userResponse = new UserResponse();

        userResponse.setId(save.getId());
        userResponse.setEmail(save.getEmail());
        userResponse.setPassword(save.getPassword());
        userResponse.setFirstName(save.getFirstName());
        userResponse.setLastName(save.getLastName());
        userResponse.setCreatedAt(save.getCreatedAt());
        userResponse.setUpdatedAt(save.getUpdatedAt());

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
}
