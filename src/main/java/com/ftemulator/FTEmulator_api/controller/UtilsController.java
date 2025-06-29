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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftemulator.FTEmulator_api.proto.auth.UtilsGrpc;
import com.ftemulator.FTEmulator_api.proto.auth.UtilsOuterClass.AuthStatusRequest;
import com.ftemulator.FTEmulator_api.proto.auth.UtilsOuterClass.AuthStatusResponse;
import com.ftemulator.FTEmulator_api.proto.auth.UtilsOuterClass.ProfileStatusRequest;
import com.ftemulator.FTEmulator_api.proto.auth.UtilsOuterClass.ProfileStatusResponse;

@RestController
@RequestMapping("/api/utils")
public class UtilsController {

    private final UtilsGrpc.UtilsBlockingStub authUtilsStub;
    // private final UtilsGrpc.UtilsBlockingStub profileUtilsStub;

    public UtilsController(
        @Qualifier("authUtilsBlockingStub") UtilsGrpc.UtilsBlockingStub authUtilsStub
        // @Qualifier("profileUtilsBlockingStub") UtilsGrpc.UtilsBlockingStub profileUtilsStub
    ) {
        this.authUtilsStub = authUtilsStub;
        // this.profileUtilsStub = profileUtilsStub;
    }

    // ----- Endpoints --------------------------------------------------

    // Status
    @GetMapping("/status")
    public ResponseEntity<Void> status() {
        return ResponseEntity.ok().build();
    }

    // AuthStatus
    @GetMapping("/authStatus")
    public ResponseEntity<Void> authStatus() {

        try {
            // Hace la peticion
            AuthStatusResponse response = authUtilsStub.authStatus(
                AuthStatusRequest.newBuilder().build()
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

    // // ProfileStatus
    // @GetMapping("/profileStatus")
    // public ResponseEntity<Void> profileStatus() {

    //     try {
    //         // Hace la peticion
    //         ProfileStatusResponse response = profileUtilsStub.profileStatus(
    //             ProfileStatusRequest.newBuilder().build()
    //         );

    //         // Maneja la repuesta
    //         if (response.getOk()) {
    //             return ResponseEntity.ok().build();
    //         } else {
    //             return ResponseEntity.status(503).build();
    //         }

    //         // Manejar errores
    //     } catch(Exception e) {
    //         e.printStackTrace();
    //         return ResponseEntity.status(503).build();
    //     }
    // }
}
