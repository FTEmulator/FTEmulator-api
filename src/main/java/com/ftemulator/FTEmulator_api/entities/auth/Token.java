package com.ftemulator.FTEmulator_api.entities.auth;

public class Token {
    public String userId;
    public String ipAddress;
    public String sessionType;

    // Mandatory
    public String getUserId() {
        return userId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getSessionType() {
        return sessionType;
    }
}
