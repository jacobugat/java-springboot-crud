package com.example.crud.model;

public class AuthResponse {
    private String username;
    private String status;
    private String token; // Agregamos este campo
    private String message;

    // Actualizamos el constructor para incluir el token
    public AuthResponse(String username, String status, String token, String message) {
        this.username = username;
        this.status = status;
        this.token = token;
        this.message = message;
    }

    // Getters y Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getToken() { return token; } // Getter para el token
    public void setToken(String token) { this.token = token; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}