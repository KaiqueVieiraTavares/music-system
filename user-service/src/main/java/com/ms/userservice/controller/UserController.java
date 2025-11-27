package com.ms.userservice.controller;

import com.ms.userservice.dtos.UpdateUserDTO;
import com.ms.userservice.dtos.UserResponseDTO;
import com.ms.userservice.service.UserService;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(userService.getUser(userId));
    }
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID userId, @Valid @RequestBody UpdateUserDTO updateUserDTO){
        return ResponseEntity.ok(userService.updateUser(userId, updateUserDTO));
    }
    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(@PageableDefault(size = 10, page = 0, sort = "title")Pageable pageable){
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId){
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
