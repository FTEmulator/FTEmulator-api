package com.ftemulator.ftemulator_api.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ftemulator.ftemulator_api.entities.auth.Token;
import com.ftemulator.ftemulator_api.proto.AuthGrpc;
import com.ftemulator.ftemulator_api.proto.AuthOuterClass.AuthStatusRequest;
import com.ftemulator.ftemulator_api.proto.AuthOuterClass.AuthStatusResponse;
import com.ftemulator.ftemulator_api.proto.AuthOuterClass.CreateTokenRequest;
import com.ftemulator.ftemulator_api.proto.AuthOuterClass.CreateTokenResponse;
import com.ftemulator.ftemulator_api.proto.AuthOuterClass.VerifyTokenRequest;
import com.ftemulator.ftemulator_api.proto.AuthOuterClass.VerifyTokenResponse;
import com.google.protobuf.util.JsonFormat;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthGrpc.AuthBlockingStub authStub;
    
    public AuthController(
        @Qualifier("authBlockingStub") AuthGrpc.AuthBlockingStub authStub
    ) {
        this.authStub = authStub;
    }

    // ----- Endpoints --------------------------------------------------

    // Status
    @GetMapping("/authStatus")
    public ResponseEntity<Void> authStatus() {
        try {
            // Make request
            AuthStatusResponse response = authStub.authStatus(
                AuthStatusRequest.newBuilder().build()
            );

            // Manage response
            if (response.getOk()) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(503).build();
            }

        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(503).build();
        }
    }

    // Verify token
    @GetMapping("/verifytoken")
    public ResponseEntity<String> verifyToken(@RequestParam String token) {
        try {

            // Defines the request
            VerifyTokenRequest request = VerifyTokenRequest.newBuilder()
                .setToken(token)
                .build();
            
            // Send request via gRPC
            VerifyTokenResponse response = authStub.verifyToken(request);

            if (response.getUserId().isEmpty()) {
                return ResponseEntity.status(401).body("{\"error\":\"Token inválido o expirado\"}");
            }

            // Parse response to Json
            String json = JsonFormat.printer()
                .includingDefaultValueFields()
                .print(response);
            
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(503).build();
        }
    }

    // Create token
    @PostMapping("/createtoken")
    public ResponseEntity<String> createToken(@RequestBody Token userData) {
        try {
            
            // Defines the request
            CreateTokenRequest request = CreateTokenRequest.newBuilder()
                .setUserId(userData.getUserId())
                .setIpAddress(userData.getIpAddress())
                .setSessionType(userData.getSessionType())
                .build();

            // Send Request via gRPC
            CreateTokenResponse response = authStub.createToken(request);

            // Parse response to Json
            String json = JsonFormat.printer()
                .includingDefaultValueFields()
                .print(response);
            
            return ResponseEntity.ok(json);


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(503).build();
        }
    }
}