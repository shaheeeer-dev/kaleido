package com.kaleido.services;

import com.kaleido.db.UserDAO;
import com.kaleido.models.User;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    private UserDAO userDAO;
    private User currentUser;

    public AuthService() {
        this.userDAO = new UserDAO();
        this.currentUser = null;
    }

    public boolean registerUser(String username, String password, String email,
                                String firstName, String lastName, String bio,
                                String phoneNumber) {

        User existingUser = userDAO.getUserByUsername(username);
        if (existingUser != null) {
            System.out.println("Username already exists.");
            return false;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(hashedPassword);
        newUser.setEmail(email);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setBio(bio);
        newUser.setPhoneNumber(phoneNumber);

        boolean success = userDAO.createUser(newUser);
        if (success) {
            System.out.println("User registered successfully!");
        } else {
            System.out.println("Failed to register user.");
        }
        return success;
    }

    public boolean login(String username, String password) {
        User user = userDAO.getUserByUsername(username);
        if (user == null) {
            System.out.println("User not found.");
            return false;
        }

        boolean passwordMatches = BCrypt.checkpw(password, user.getPassword());
        if (passwordMatches) {
            this.currentUser = user;
            System.out.println("Login successful! Welcome " + user.getFirstName());
            return true;
        } else {
            System.out.println("Invalid password.");
            return false;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        if (currentUser != null) {
            System.out.println("You have logged_out from: " + currentUser.getFirstName());
        }
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}