/*
 * ftemulator - ftemulator is a high-performance stock market investment simulator designed with extreme technical efficiency
 * 
 * Copyright (C) 2025-2025 Álex Frías (alexwebdev05)
 * Licensed under GNU Affero General Public License v3.0
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * For commercial licensing inquiries, please contact: alexwebdev05@proton.me
 * GitHub: https://github.com/alexwebdev05
 */
package com.ftemulator.ftemulator_api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftemulator.ftemulator_api.entities.profile.User;
import com.ftemulator.ftemulator_api.proto.ProfileGrpc;
import com.ftemulator.ftemulator_api.proto.ProfileOuterClass.LoginRequest;
import com.ftemulator.ftemulator_api.proto.ProfileOuterClass.LoginResponse;
import com.ftemulator.ftemulator_api.proto.ProfileOuterClass.ProfileStatusRequest;
import com.ftemulator.ftemulator_api.proto.ProfileOuterClass.ProfileStatusResponse;
import com.ftemulator.ftemulator_api.proto.ProfileOuterClass.RegisterUserRequest;
import com.ftemulator.ftemulator_api.proto.ProfileOuterClass.RegisterUserResponse;
import com.ftemulator.ftemulator_api.proto.ProfileOuterClass.UserRequest;
import com.ftemulator.ftemulator_api.proto.ProfileOuterClass.UserResponse;
import com.google.protobuf.util.JsonFormat;

import jakarta.servlet.http.HttpServletRequest;

// # Dependent modules
// authController: /login (profile) depend of /createtoken (auth)

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Value("${ftemulator.api.host}")
    private String apiHost;

    @Value("${ftemulator.api.port}")
    private String apiPort;

    @Value("${ftemulator.auth.host}")
    private String authHost;

    @Value("${ftemulator.auth.port}")
    private String authPort;

    @Value("${server.port}")
    private String localPort;
    
    private final ProfileGrpc.ProfileBlockingStub profileStub;

    public ProfileController(
        @Qualifier("profileBlockingStub") ProfileGrpc.ProfileBlockingStub profileStub
    ) 
    {
        this.profileStub = profileStub;
    }

    // ----- Endpoints --------------------------------------------------

    @GetMapping("/profileStatus")
    public ResponseEntity<Void> profileStatus() {

        try {
            // Hace la peticion
            ProfileStatusResponse response = profileStub.profileStatus(
                ProfileStatusRequest.newBuilder().build()
            );

            // Maneja la repuesta
            if (response.getOk()) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(503).build();
            }

            // Manejar errores
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(503).build();
        }
    }

    // Get user
    @GetMapping("/user")
    public ResponseEntity<String> getUser(@RequestParam String userId) {
        try {
            
            // Defines the request
            UserRequest request = UserRequest.newBuilder()
                .setUserId(userId)
                .build();

            // Sends the request via gRPC
            UserResponse response = profileStub.getUser(request);

            // Parse response to Json
            String json = JsonFormat.printer().print(response);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al obtener el usuario: " + e.getMessage());
        }
    }

    // Register user
    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody User userData,  HttpServletRequest requestHttp) {
        try {
            
            // Defines the request
            RegisterUserRequest.Builder requestBuilder = RegisterUserRequest.newBuilder()
                .setName(userData.name)
                .setEmail(userData.email)
                .setPassword(userData.password)
                .setCountry(userData.country);
            
            if (userData.country != null) {requestBuilder.setCountry(userData.country);}
            if (userData.experience > 0) {requestBuilder.setExperience(userData.experience);}
            if (userData.photo != null) {requestBuilder.setPhoto(userData.photo);}
            if (userData.biography != null) {requestBuilder.setBiography(userData.biography);}

            RegisterUserRequest request = requestBuilder.build();

            // Send request
            RegisterUserResponse response = profileStub.createUser(request);

            // Check if email is already used
            if (response.getUserId().equals("User already exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("User or email already exists");
            }

            // Confirm user creation
            if (!response.getCreated()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                         .body("User not created");
            }

            String userId = response.getUserId();

            // Get ip and user-agent
            String ipAddress = requestHttp.getHeader("X-Forwarded-For");
            if (ipAddress == null) {
                ipAddress = requestHttp.getRemoteAddr();
            }
            String sessionType = requestHttp.getHeader("User-Agent");

            // Send userId to auth
            Map<String, String> bodyMap = new HashMap<>();
            bodyMap.put("userId", userId);
            bodyMap.put("ipAddress", ipAddress);
            bodyMap.put("sessionType", sessionType);

            String jsonBody = new ObjectMapper().writeValueAsString(bodyMap);

            // Send token request
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

            // Auth
            String url = "http://" + apiHost + ":" + apiPort + "/api/auth/createtoken";

            String authResponse = restTemplate.postForObject(url, entity, String.class);

            return ResponseEntity.ok(authResponse);

        } catch (io.grpc.StatusRuntimeException e) {
            // Manejar errores específicos de gRPC
            if (e.getStatus().getCode() == io.grpc.Status.Code.ALREADY_EXISTS) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("User or email already exists");
            }
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error gRPC: " + e.getStatus().getDescription());
                
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al registrar el usuario: " + e.getMessage());
        }
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User userData, HttpServletRequest requestHttp) {
        try {

            // Defines the request
            LoginRequest request = LoginRequest.newBuilder()
                .setEmail(userData.getEmail())
                .setPassword(userData.getPassword())
                .build();

            // Send request
            LoginResponse response = profileStub.login(request);

            // Parse response to Json
            String userId = response.getUserId();

            if (userId.equals("invalid")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
            }

            // Get ip and user-agent
            String ipAddress = requestHttp.getHeader("X-Forwarded-For");
            if (ipAddress == null) {
                ipAddress = requestHttp.getRemoteAddr();
            }
            String sessionType = requestHttp.getHeader("User-Agent");

            // Send userId to auth
            Map<String, String> bodyMap = new HashMap<>();
            bodyMap.put("userId", userId);
            bodyMap.put("ipAddress", ipAddress);
            bodyMap.put("sessionType", sessionType);

            String jsonBody = new ObjectMapper().writeValueAsString(bodyMap);

            // Send token request
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

            String url = "http://" + apiHost + ":" + apiPort + "/api/auth/createtoken";

            String authResponse = restTemplate.postForObject(url, entity, String.class);

            return ResponseEntity.ok(authResponse);
            
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al verificar el usuario: " + e.getMessage());
        }
    }
}

