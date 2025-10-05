package com.kaleido.GUI;

import com.kaleido.services.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Registration extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField bioField;
    private JTextField phoneField;
    private JButton registerButton;
    private AuthService authService;

    public Registration() {
        authService = new AuthService();
        setTitle("Kaleido - Sign Up");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Create components
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        emailField = new JTextField(15);
        firstNameField = new JTextField(15);
        lastNameField = new JTextField(15);
        bioField = new JTextField(15);
        phoneField = new JTextField(15);
        registerButton = new JButton("Register");

        // Set layout
        setLayout(new GridLayout(8, 2, 10, 10));

        // Add components
        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("First Name:"));
        add(firstNameField);
        add(new JLabel("Last Name:"));
        add(lastNameField);
        add(new JLabel("Bio:"));
        add(bioField);
        add(new JLabel("Phone:"));
        add(phoneField);
        add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });
    }

    private void register() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String bio = bioField.getText();
        String phone = phoneField.getText();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in required fields (username, password, email).");
            return;
        }

        boolean success = authService.registerUser(username, password, email, firstName, lastName, bio, phone);
        if (success) {
            JOptionPane.showMessageDialog(this, "Registration successful! You can now login.");
            dispose(); // Close the registration window
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Username might be taken.");
        }
    }
}