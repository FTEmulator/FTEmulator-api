package com.ftemulator.FTEmulator_api.grpc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ftemulator.FTEmulator_api.proto.auth.UtilsGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

@Configuration
public class config {
    // ----- Variables ---------------------------------------------------

    // Auth-service
    @Value("${ftemulator.auth.host}")
    private String authHost;

    @Value("${ftemulator.auth.port}")
    private int authPort;

    // ----- Canales gRPC ------------------------------------------------

    // Auth-service
    @Bean
    public ManagedChannel authManagedChannel() {
        return ManagedChannelBuilder.forAddress(authHost, authPort)
                .usePlaintext()
                .build();
    }

    @Bean
    public UtilsGrpc.UtilsBlockingStub authUtilsBlockingStub(ManagedChannel channel) {
        return UtilsGrpc.newBlockingStub(channel);
    }
}
