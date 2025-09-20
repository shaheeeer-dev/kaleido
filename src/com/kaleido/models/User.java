package com.kaleido.models;

import java.time.LocalDateTime;

public class User {
    private int userId;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String bio;
    private String profilePicUrl;
    private LocalDateTime createdAt;
    private String phoneNumber;


    public User() {
    }

    public User(int userId, String username, String password, String email, String fullName, String bio, String profilePicUrl, LocalDateTime createdAt, String phoneNumber) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.bio = bio;
        this.profilePicUrl = profilePicUrl;
        this.createdAt = createdAt;
        this.phoneNumber = phoneNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}