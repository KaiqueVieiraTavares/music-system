package com.ms.userservice.service;

import com.ms.userservice.dtos.UpdateUserDTO;
import com.ms.userservice.dtos.UserResponseDTO;
import com.ms.userservice.exceptions.UserNotFoundException;
import com.ms.userservice.model.UserEntity;
import com.ms.userservice.repository.UserRepository;

import org.modelmapper.ModelMapper;

import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    public static final String CACHE_USERS = "user";
    public static final String CACHE_USERS_LIST = "all";
    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }
    @Cacheable(value = CACHE_USERS, key = "#userId")
    @Transactional(readOnly = true)
    public UserResponseDTO getUser(UUID userId){
        logger.info("get user with id: {} ", userId);
        var user = findByIdOrThrow(userId);
        return modelMapper.map(user, UserResponseDTO.class);
    }
    @Cacheable( value = CACHE_USERS_LIST, key = "'allUsers'")
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers(){
        logger.info("get all users");
        return userRepository.findAll().stream().map(user -> modelMapper.map(user, UserResponseDTO.class)).toList();
    }
    @Caching(evict = {
            @CacheEvict(value = CACHE_USERS, key = "#userId"),
            @CacheEvict(value = CACHE_USERS_LIST, allEntries = true)
    })
    public void deleteUser(UUID userId){
        logger.info("deleting user with id: {}", userId);
        var user = findByIdOrThrow(userId);
        userRepository.delete(user);
    }
    @Caching(evict = {
            @CacheEvict(value = CACHE_USERS, key = "#userId"),
            @CacheEvict(value = CACHE_USERS_LIST, allEntries = true)
    })
    @Transactional
    public UserResponseDTO updateUser(UUID userId, UpdateUserDTO updateUserDTO){
        logger.info("updating user with id: {}", userId);
        var user = findByIdOrThrow(userId);
        modelMapper.map(updateUserDTO, user);
        var savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserResponseDTO.class);
    }
    private UserEntity findByIdOrThrow(UUID userId){
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found"));
    }
}
