package com.example.crud.model;

public class AuthResponse {
    private String username;
    private String status;  // "MFA_REQUIRED" o "SUCCESS"
    private String message;

    public AuthResponse(String username, String status, String message) {
        this.username = username;
        this.status = status;
        this.message = message;
    }

    // Getters y Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}