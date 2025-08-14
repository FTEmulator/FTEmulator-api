package com.ftemulator.FTEmulator_api.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftemulator.FTEmulator_api.proto.AuthGrpc;
import com.ftemulator.FTEmulator_api.proto.AuthOuterClass.AuthStatusRequest;
import com.ftemulator.FTEmulator_api.proto.AuthOuterClass.AuthStatusResponse;

@RestController
@RequestMapping("/api/utils")
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
            // Hace la peticion
            AuthStatusResponse response = authStub.authStatus(
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

}
