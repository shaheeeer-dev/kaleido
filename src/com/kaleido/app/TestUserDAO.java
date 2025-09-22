package com.kaleido.app;

import com.kaleido.db.UserDAO;
import com.kaleido.models.User;

public class TestUserDAO {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();

        // Test creating a user
        User testUser = new User();
        testUser.setUsername("joe_smith");
        testUser.setPassword("test123");
        testUser.setEmail("john@email.com");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setBio("First test user");
        testUser.setPhoneNumber("123-456-7890");

        boolean created = userDAO.createUser(testUser);
        System.out.println("User created: " + created);

        // Test retrieving the user
        User foundUser = userDAO.getUserByUsername("john_doe");
        if (foundUser != null) {
            System.out.println("SUCCESS! Found user: " + foundUser.getFirstName());
            System.out.println("Email: " + foundUser.getEmail());
        } else {
            System.out.println("FAILED: User not found");
        }
    }
}