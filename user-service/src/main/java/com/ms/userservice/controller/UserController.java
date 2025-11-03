package com.ms.userservice.controller;

import com.ms.userservice.dtos.UpdateUserDTO;
import com.ms.userservice.dtos.UserResponseDTO;
import com.ms.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable UUID userId){
        var userDto = userService.getUser(userId);
        return ResponseEntity.ok(userDto);
    }
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID userId, @Valid @RequestBody UpdateUserDTO updateUserDTO){
        var userDtoUpdated= userService.updateUser(userId, updateUserDTO);
        return ResponseEntity.ok(userDtoUpdated);
    }
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId){
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
