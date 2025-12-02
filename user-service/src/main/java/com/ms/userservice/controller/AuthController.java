package com.ms.userservice.controller;

import com.ms.userservice.dtos.AuthResponseDto;
import com.ms.userservice.dtos.CreateUserDTO;
import com.ms.userservice.dtos.LoginUserDto;
import com.ms.userservice.dtos.UserResponseDTO;
import com.ms.userservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
   private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody CreateUserDTO createUserDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.createUser(createUserDTO));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> loginUser(@Valid @RequestBody LoginUserDto loginUserDto){
        return ResponseEntity.ok(authService.loginUser(loginUserDto));
    }
}
