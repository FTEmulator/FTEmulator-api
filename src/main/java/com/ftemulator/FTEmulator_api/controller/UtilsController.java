package com.ftemulator.FTEmulator_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftemulator.FTEmulator_api.proto.auth.UtilsGrpc;
import com.ftemulator.FTEmulator_api.proto.auth.UtilsOuterClass.AuthStatusRequest;
import com.ftemulator.FTEmulator_api.proto.auth.UtilsOuterClass.AuthStatusResponse;

@RestController
@RequestMapping("/api/utils")
public class UtilsController {

    private final UtilsGrpc.UtilsBlockingStub utilsStub;

    public UtilsController(UtilsGrpc.UtilsBlockingStub utilsStub) {
        this.utilsStub = utilsStub;
    }

    // ----- Endpoints --------------------------------------------------

    @GetMapping("/status")
    public ResponseEntity<Void> status() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/authStatus")
    public ResponseEntity<Void> authStatus() {

        try {
            // Hace la peticion
            AuthStatusResponse response = utilsStub.authStatus(
                AuthStatusRequest.newBuilder().build()
            );

            // Maneja la repuesta
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
}
