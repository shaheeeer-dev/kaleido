package com.kaleido.GUI;

import javax.swing.*;
import java.awt.*;

public class Registration{
    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.setTitle("Kaleido | Registration");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel bgPanel = new JPanel();
        bgPanel.setBackground(new Color(205, 84, 86));

        bgPanel.setLayout(new GridBagLayout());

        JPanel registerBox = new JPanel(new GridBagLayout());
        registerBox.setBackground(new Color(0x202020));
        registerBox.setPreferredSize(new Dimension(400,630));
        registerBox.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 6, 10, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Logo
        JLabel logoLabel = new JLabel();
        ImageIcon logo = new ImageIcon("src/com/kaleido/GUI/Kaleidologo.png");
        Image scaledLogo = logo.getImage().getScaledInstance(110, 110, Image.SCALE_SMOOTH);
        logoLabel.setIcon(new ImageIcon(scaledLogo));
        logoLabel.setText("KALEIDO");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);logoLabel.add(Box.createRigidArea(new Dimension(0, 10)));
        logoLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        logoLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weighty = 0.0;
        registerBox.add(logoLabel, gbc);

        gbc.gridy = 1;
        registerBox.add(Box.createRigidArea(new Dimension(0, 0)), gbc);

        // First Name Field
        JTextField fNameField = new JTextField();
        fNameField.setPreferredSize(new Dimension(200, 40));
        fNameField.setBackground(new Color(0x353535));
        fNameField.setForeground(Color.WHITE);
        fNameField.setCaretColor(Color.WHITE);
        fNameField.setBorder(BorderFactory.createTitledBorder(null, "First Name", 0, 0,
                new Font("SansSerif", Font.PLAIN, 12), Color.WHITE));
        gbc.gridy = 2;
        registerBox.add(fNameField, gbc);

        // Last Name Field
        JTextField lNameField = new JTextField();
        lNameField.setPreferredSize(new Dimension(200, 40));
        lNameField.setBackground(new Color(0x353535));
        lNameField.setForeground(Color.WHITE);
        lNameField.setCaretColor(Color.WHITE);
        lNameField.setBorder(BorderFactory.createTitledBorder(null, "Last Name", 0, 0,
                new Font("SansSerif", Font.PLAIN, 12), Color.WHITE));
        gbc.gridy = 3;
        registerBox.add(lNameField, gbc);

        // Username Field
        JTextField usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 40));
        usernameField.setBackground(new Color(0x353535));
        usernameField.setForeground(Color.WHITE);
        usernameField.setCaretColor(Color.WHITE);
        usernameField.setBorder(BorderFactory.createTitledBorder(null, "Username", 0, 0,
                new Font("SansSerif", Font.PLAIN, 12), Color.WHITE));
        gbc.gridy = 4;
        registerBox.add(usernameField, gbc);

        // Email Field
        JTextField emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(200, 40));
        emailField.setBackground(new Color(0x353535));
        emailField.setForeground(Color.WHITE);
        emailField.setCaretColor(Color.WHITE);
        emailField.setBorder(BorderFactory.createTitledBorder(null, "Email", 0, 0,
                new Font("SansSerif", Font.PLAIN, 12), Color.WHITE));
        gbc.gridy = 5;
        registerBox.add(emailField, gbc);

        // Password Field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 40));
        passwordField.setBackground(new Color(0x353535));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setBorder(BorderFactory.createTitledBorder(null, "Password", 0, 0,
                new Font("SansSerif", Font.PLAIN, 12), Color.WHITE));
        gbc.gridy = 6;
        registerBox.add(passwordField, gbc);

        JButton register = new JButton("Register");
        register.setBackground(new Color(43, 86, 136));
        register.setPreferredSize(new Dimension(0,44));
        register.setForeground(Color.white);
        register.setOpaque(true);
        register.setBorderPainted(false);
        register.setFocusPainted(false);
        register.setFont(new Font("SansSerif", Font.BOLD, 14));

        gbc.gridy = 7;
        gbc.insets = new Insets(18, 6, 6, 6);
        registerBox.add(register, gbc);

        // Sign Up Label
        JLabel loginLabel = new JLabel("Already have an account? Login");
        loginLabel.setForeground(new Color(0x3999c1));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        gbc.gridy = 8;
        gbc.weighty = 0;
        gbc.insets = new Insets(10, 6, 6, 6);
        registerBox.add(loginLabel, gbc);

        bgPanel.add(registerBox);
        frame.add(bgPanel);
        frame.setVisible(true);
    }
}