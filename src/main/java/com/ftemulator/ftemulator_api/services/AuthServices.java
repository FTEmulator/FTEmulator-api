package com.ftemulator.ftemulator_api.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ftemulator.ftemulator_api.proto.AuthGrpc;
import com.ftemulator.ftemulator_api.proto.AuthOuterClass;
import com.ftemulator.ftemulator_api.proto.AuthOuterClass.VerifyTokenRequest;
import com.ftemulator.ftemulator_api.proto.AuthOuterClass.VerifyTokenResponse;

@Service
public class AuthServices {

    private final AuthGrpc.AuthBlockingStub authStub;
    
    public AuthServices(@Qualifier("authBlockingStub") AuthGrpc.AuthBlockingStub authStub) {
        this.authStub = authStub;
    }

    // Verify token
    public VerifyTokenResponse verifyToken(String token) {
        try {
            VerifyTokenRequest request = VerifyTokenRequest.newBuilder()
                .setToken(token)
                .build();

            return authStub.verifyToken(request);
            
        } catch (io.grpc.StatusRuntimeException e) {
            if (e.getStatus().getCode() == io.grpc.Status.Code.UNAUTHENTICATED) {
                // Token inválido - devolver respuesta vacía
                return VerifyTokenResponse.newBuilder().build();
            }
            throw new RuntimeException("Auth service error: " + e.getStatus(), e);
        }
    }

    // Create token
    public AuthOuterClass.CreateTokenResponse createToken(String userId, String ipAddress, String sessionType) {
        AuthOuterClass.CreateTokenRequest request = AuthOuterClass.CreateTokenRequest.newBuilder()
            .setUserId(userId)
            .setIpAddress(ipAddress)
            .setSessionType(sessionType)
            .build();
        
        return authStub.createToken(request);
    }
}
