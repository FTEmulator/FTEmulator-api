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
package com.ftemulator.ftemulator_api.grpc;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ftemulator.ftemulator_api.proto.AuthGrpc;
import com.ftemulator.ftemulator_api.proto.ProfileGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

@Configuration
public class Config {

    // ----- Variables ----------------------------------------------------

    // Auth
    @Value("${ftemulator.auth.host}")
    private String authHost;

    @Value("${ftemulator.auth.port}")
    private int authPort;

    // Profile
    @Value("${ftemulator.profile.host}")
    private String profileHost;

    @Value("${ftemulator.profile.port}")
    private int profilePort;

    // ----- gRPC Channels ------------------------------------------------

    // Auth
    @Bean(destroyMethod = "shutdownNow")
    public ManagedChannel authManagedChannel() {
        return ManagedChannelBuilder.forAddress(authHost, authPort)
            .usePlaintext()
            .build();
    }

    @Bean
    public AuthGrpc.AuthBlockingStub authBlockingStub(
        @Qualifier("authManagedChannel") ManagedChannel authManagedChannel
    ) {
        return AuthGrpc.newBlockingStub(authManagedChannel);
    }

    // Profile
    @Bean(destroyMethod = "shutdownNow")
    public ManagedChannel profileManagedChannel() {
        return ManagedChannelBuilder.forAddress(profileHost, profilePort)
                .usePlaintext()
                .build();
    }

    @Bean
    public ProfileGrpc.ProfileBlockingStub profileBlockingStub(
        @Qualifier("profileManagedChannel") ManagedChannel channel
    ) {
        return ProfileGrpc.newBlockingStub(channel);
    }
}