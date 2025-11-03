package com.ms.userservice.service;

import com.ms.userservice.dtos.AuthResponseDto;
import com.ms.userservice.dtos.CreateUserDTO;
import com.ms.userservice.dtos.LoginUserDto;
import com.ms.userservice.dtos.UserResponseDTO;
import com.ms.userservice.model.UserEntity;
import com.ms.userservice.exceptions.EmailAlreadyExistsException;
import com.ms.userservice.repository.UserRepository;
import com.ms.userservice.security.TokenService;
import com.ms.userservice.security.UserDetailsImpl;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    public AuthService(UserRepository userRepository, ModelMapper modelMapper, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public UserResponseDTO createUser(CreateUserDTO createUserDTO){
        if(userRepository.existsByEmail(createUserDTO.email())){
            throw new EmailAlreadyExistsException("Email: " + createUserDTO.email() + " already exists");
        }
        var userEntity = modelMapper.map(createUserDTO, UserEntity.class);
        userEntity.setPasswordHash(passwordEncoder.encode(createUserDTO.password()));
        return modelMapper.map(userRepository.save(userEntity), UserResponseDTO.class);
    }
    public AuthResponseDto loginUser(LoginUserDto loginUserDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginUserDto.email(), loginUserDto.password()
        ));
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String token = tokenService.generateToken(userDetails);
        return new AuthResponseDto(userDetails.getId(), userDetails.getUsername(), userDetails.getRole(), token);
    }
}
