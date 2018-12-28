package com.demo.springbootemployee.controller;

import javax.inject.Inject;
import javax.validation.Valid;

import com.demo.springbootemployee.dto.LoginDto;
import com.demo.springbootemployee.dto.ManagedUserDto;
import com.demo.springbootemployee.message.ApiResponse;
import com.demo.springbootemployee.message.JwtResponse;
import com.demo.springbootemployee.model.User;
import com.demo.springbootemployee.repository.UserRepository;
import com.demo.springbootemployee.security.jwt.JwtProvider;
import com.demo.springbootemployee.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    @Inject
    AuthenticationManager authenticationManager;

    @Inject
    UserRepository userRepository;

    @Inject
    UserService userService;

    @Inject
    JwtProvider jwtProvider;

    @ApiOperation(value="Authenticate a user")
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDto loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername().toLowerCase(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @ApiOperation(value="Register a user")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody ManagedUserDto managedUserDto) {

        if(userRepository.existsByUsername(managedUserDto.getUsername().toLowerCase())) {
            return new ResponseEntity<String>("Fail -> Username is already taken!",
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(managedUserDto.getEmail().toLowerCase())) {
            return new ResponseEntity<String>("Fail -> Email is already in use!",
                    HttpStatus.BAD_REQUEST);
        }

        final User result = userService.createUser(managedUserDto.getName(),managedUserDto.getUsername().toLowerCase(),managedUserDto.getEmail().toLowerCase(),managedUserDto.getPassword());

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }
}