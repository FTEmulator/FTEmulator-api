package com.ftemulator.FTEmulator_api.grpc.auth;

import org.springframework.grpc.server.service.GrpcService;

import com.ftemulator.FTEmulator_api.proto.auth.UtilsGrpc;
import com.ftemulator.FTEmulator_api.proto.auth.UtilsOuterClass;
import com.ftemulator.FTEmulator_api.proto.auth.UtilsOuterClass.StatusResponse;

import io.grpc.stub.StreamObserver;

@GrpcService
public class UtilsImpl extends UtilsGrpc.UtilsImplBase {
    @Override
    public void status(UtilsOuterClass.StatusRequest request, StreamObserver<StatusResponse> responseObserver) {
        UtilsOuterClass.StatusResponse response = StatusResponse.newBuilder().setOk(true).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
}