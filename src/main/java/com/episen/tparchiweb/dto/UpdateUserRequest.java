package com.episen.tparchiweb.dto;

public class UpdateUserRequest {
    private String username;
    private String email;
    private Boolean isAdmin;
    private String password;

    public UpdateUserRequest() {}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Boolean getIsAdmin() { return isAdmin; }
    public void setIsAdmin(Boolean isAdmin) { this.isAdmin = isAdmin; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

