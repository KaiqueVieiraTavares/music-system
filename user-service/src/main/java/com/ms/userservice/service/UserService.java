package com.ms.userservice.service;

import com.ms.userservice.dtos.UpdateUserDTO;
import com.ms.userservice.dtos.UserResponseDTO;
import com.ms.userservice.exceptions.UserNotFoundException;
import com.ms.userservice.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }
    public UserResponseDTO getUser(UUID userID){
        var user = userRepository.findById(userID).orElseThrow(() -> new UserNotFoundException("User with id: " + userID + " not found"));
        return modelMapper.map(user, UserResponseDTO.class);
    }
    public List<UserResponseDTO> getAllUsers(){
        return userRepository.findAll().stream().map(user -> modelMapper.map(user, UserResponseDTO.class)).toList();
    }
    public void deleteUser(UUID userId){
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found"));
        userRepository.delete(user);
    }
    public UserResponseDTO updateUser(UUID userId, UpdateUserDTO updateUserDTO){
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found"));
        modelMapper.map(updateUserDTO, user);
        var savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserResponseDTO.class);
    }
}
