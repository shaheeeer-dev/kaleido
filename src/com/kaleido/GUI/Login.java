package com.kaleido.GUI;

import com.kaleido.services.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Login {

    private AuthService authService;
    private JFrame frame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new);
    }

    public Login() {
        this.authService = new AuthService();
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("Kaleido | Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(43, 86, 136),
                        width, height, new Color(205, 84, 86)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        bgPanel.setLayout(new GridBagLayout());
        frame.setContentPane(bgPanel);

        JPanel loginBox = new JPanel(new GridBagLayout());
        loginBox.setBackground(new Color(0x202020));
        loginBox.setPreferredSize(new Dimension(400, 500));
        loginBox.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 6, 10, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Logo
        JLabel logoLabel = new JLabel();
        ImageIcon logo = new ImageIcon("src/com/kaleido/GUI/Kaleidologo.png");
        if (logo.getImageLoadStatus() == MediaTracker.COMPLETE) {
            Image scaledLogo = logo.getImage().getScaledInstance(110, 110, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledLogo));
        } else {
            logoLabel.setText("KALEIDO");
            logoLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
            logoLabel.setForeground(Color.WHITE);
        }
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weighty = 0.0;
        loginBox.add(logoLabel, gbc);

        gbc.gridy = 1;
        loginBox.add(Box.createRigidArea(new Dimension(0, 6)), gbc);

        // Username Field
        JTextField usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 40));
        usernameField.setBackground(new Color(0x353535));
        usernameField.setForeground(Color.WHITE);
        usernameField.setCaretColor(Color.WHITE);
        usernameField.setBorder(BorderFactory.createTitledBorder(null, "Username", 0, 0,
                new Font("SansSerif", Font.PLAIN, 12), Color.WHITE));
        gbc.gridy = 2;
        loginBox.add(usernameField, gbc);

        // Password Field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 40));
        passwordField.setBackground(new Color(0x353535));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setBorder(BorderFactory.createTitledBorder(null, "Password", 0, 0,
                new Font("SansSerif", Font.PLAIN, 12), Color.WHITE));
        gbc.gridy = 3;
        loginBox.add(passwordField, gbc);

        // Login Button
        JButton loginBtn = new JButton("Login");
        loginBtn.setPreferredSize(new Dimension(0, 44));
        loginBtn.setBackground(new Color(43, 86, 136));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setOpaque(true);
        loginBtn.setContentAreaFilled(true);
        loginBtn.setBorderPainted(false);
        loginBtn.setFocusPainted(false);
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Login Button Hover Effects
        loginBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginBtn.setBackground(new Color(35, 70, 120));
                loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginBtn.setBackground(new Color(43, 86, 136));
                loginBtn.setCursor(Cursor.getDefaultCursor());
            }
        });

        // Login Button Action
        loginBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            // Input validation
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Please fill in all fields",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Attempt login
            boolean loginSuccess = authService.login(username, password);

            if (loginSuccess) {
                JOptionPane.showMessageDialog(frame,
                        "Login successful! Welcome to Kaleido.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // TODO: Open main application window
                System.out.println("Opening main application...");
                // frame.dispose(); // Close login window
                // new MainApplication(); // Open main app

            } else {
                JOptionPane.showMessageDialog(frame,
                        "Invalid username or password. Please try again.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);

                // Clear password field
                passwordField.setText("");
            }
        });

        gbc.gridy = 4;
        gbc.insets = new Insets(18, 6, 6, 6);
        loginBox.add(loginBtn, gbc);

        gbc.gridy = 5;
        gbc.weighty = 1.0;
        loginBox.add(Box.createVerticalGlue(), gbc);

        // Sign Up Label
        JLabel signupLabel = new JLabel("Don't have an account? Sign up");
        signupLabel.setForeground(new Color(0x3999c1));
        signupLabel.setHorizontalAlignment(SwingConstants.CENTER);
        signupLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Sign Up Label Hover Effects
        signupLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO: Open registration window
                JOptionPane.showMessageDialog(frame,
                        "Registration feature coming soon!",
                        "Sign Up",
                        JOptionPane.INFORMATION_MESSAGE);

                // For now, you can test with:
                // frame.dispose();
                // new Registration();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                signupLabel.setForeground(new Color(0x57b8e0));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                signupLabel.setForeground(new Color(0x3999c1));
            }
        });

        gbc.gridy = 6;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(10, 6, 6, 6);
        loginBox.add(signupLabel, gbc);

        bgPanel.add(loginBox);

        // Center the frame on screen
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Set focus to username field
        SwingUtilities.invokeLater(() -> usernameField.requestFocus());
    }
}