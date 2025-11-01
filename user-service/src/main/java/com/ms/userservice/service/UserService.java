package com.ms.userservice.service;

import com.ms.userservice.dtos.CreateUserDTO;
import com.ms.userservice.dtos.UserResponseDTO;
import com.ms.userservice.entity.UserEntity;
import com.ms.userservice.exceptions.EmailAlreadyExistsException;
import com.ms.userservice.exceptions.UserNotFoundException;
import com.ms.userservice.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public UserResponseDTO createUser(CreateUserDTO createUserDTO){
        if(userRepository.existsByEmail(createUserDTO.email())){
            throw new EmailAlreadyExistsException("Email: " + createUserDTO.email() + " already exists");
        }
        var savedUserEntity =  userRepository.save(modelMapper.map(createUserDTO, UserEntity.class));
        return modelMapper.map(savedUserEntity, UserResponseDTO.class);
    }
    public UserResponseDTO getUser(UUID userID){
        var user = userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException("User with id: " + userID + " not found"));
        return modelMapper.map(user, UserResponseDTO.class);
    }
    public UserResponseDTO deleteUser(UUID userId){
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found"));
    }
}
