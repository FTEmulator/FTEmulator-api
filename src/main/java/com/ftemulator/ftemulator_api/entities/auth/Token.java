package com.ftemulator.ftemulator_api.entities.auth;

public class Token {
    private String userId;
    private String ipAddress;
    private String sessionType;

    // getters y setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getSessionType() { return sessionType; }
    public void setSessionType(String sessionType) { this.sessionType = sessionType; }
}
