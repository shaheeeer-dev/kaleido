package com.kaleido.app;

import com.kaleido.services.AuthService;

public class TestAuthService {
    public static void main(String[] args) {
        AuthService authService = new AuthService();

        System.out.println("=== Testing Registration ===");
        // Test registering a new user
        boolean registered = authService.registerUser(
                "test_user1",
                "password123",
                "test@kaleido1.com",
                "Test",
                "User",
                "Just testing the auth service",
                "123-456-7890"
        );
        System.out.println("Registration result: " + registered);

        System.out.println("\n=== Testing Login ===");
        // Test logging in with correct password
        boolean loginSuccess = authService.login("test_user", "password123");
        System.out.println("Login with correct password: " + loginSuccess);
        System.out.println("Current user: " + (authService.getCurrentUser() != null ?
                authService.getCurrentUser().getFirstName() : "None"));

        System.out.println("\n=== Testing Wrong Password ===");
        // Test logging in with wrong password
        boolean loginFail = authService.login("test_user", "wrongpassword");
        System.out.println("Login with wrong password: " + loginFail);

        System.out.println("\n=== Testing Logout ===");
        // Test logout
        authService.logout();
        System.out.println("Is logged in after logout: " + authService.isLoggedIn());

        System.out.println("\n=== Testing Duplicate Registration ===");
        // Try to register same username again
        boolean duplicate = authService.registerUser(
                "test_user",
                "anotherpass",
                "test2@kaleido.com",
                "Test2",
                "User2",
                "Duplicate test",
                "987-654-3210"
        );
        System.out.println("Duplicate registration allowed: " + duplicate);
    }
}