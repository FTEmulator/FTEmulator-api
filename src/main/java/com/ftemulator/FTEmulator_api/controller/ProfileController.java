/*
 * FTEmulator - FTEmulator is a high-performance stock market investment simulator designed with extreme technical efficiency
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
package com.ftemulator.FTEmulator_api.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ftemulator.FTEmulator_api.proto.ProfileGrpc;
import com.ftemulator.FTEmulator_api.proto.ProfileOuterClass.UserRequest;
import com.ftemulator.FTEmulator_api.proto.ProfileOuterClass.UserResponse;
import com.google.protobuf.util.JsonFormat;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    
    private final ProfileGrpc.ProfileBlockingStub profileStub;

    public ProfileController(
        @Qualifier("profileBlockingStub") ProfileGrpc.ProfileBlockingStub profileStub
    ) 
    {
        this.profileStub = profileStub;
    }

    // ----- Endpoints --------------------------------------------------

    // Get user
    @GetMapping("/user")
    public ResponseEntity<?> getUser(@RequestParam String userId) {
        try {
            
            // Defines the request
            UserRequest request = UserRequest.newBuilder()
                .setUserId(userId)
                .build();

            // Sends the request to gRPC server
            UserResponse response = profileStub.getUser(request);

            // Converts Protobuf to JSON
            String json = JsonFormat.printer().print(response);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al obtener el usuario: " + e.getMessage());
        }
    }
}

