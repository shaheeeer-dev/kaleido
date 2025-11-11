package com.kaleido.GUI;

import com.kaleido.models.User;
import javax.swing.*;
import java.awt.*;

public class Feed {
    private JFrame frame;
    private User currentUser;

    public Feed(User user) {
        this.currentUser = user;
        initializeGUI();
    }

    void initializeGUI(){
        frame = new JFrame();
        frame.setTitle("Kaleido | Feed");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main container with BorderLayout
        JPanel mainContainer = new JPanel(new BorderLayout());

        //header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.BLACK);
        header.setPreferredSize(new Dimension(0, 60));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Left: Logo
        JLabel logoLabel = new JLabel("Kaleido");
        ImageIcon logo = new ImageIcon("src/com/kaleido/GUI/Kaleidologo.png");
        if (logo.getImageLoadStatus() == MediaTracker.COMPLETE) {
            Image scaledLogo = logo.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledLogo));
        }
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        header.add(logoLabel, BorderLayout.WEST);

        // Right: User Info
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(Color.BLACK);

        JLabel userName = new JLabel(currentUser.getUsername());
        userName.setForeground(Color.WHITE);
        userPanel.add(userName);

        header.add(userPanel, BorderLayout.EAST);

        // MAIN CONTENT
        JPanel mainContent = new JPanel(new BorderLayout());

        // Left Sidebar Container
        JPanel leftSidebarContainer = new JPanel();
        leftSidebarContainer.setLayout(new BorderLayout());
        leftSidebarContainer.setBackground(new Color(19, 19, 20));
        leftSidebarContainer.setPreferredSize(new Dimension(250, 0)); // total sidebar width

        // Upper sidebar
        JPanel leftSidebar = new JPanel();
        leftSidebar.setBackground(new Color(21, 21, 23));
        leftSidebar.setPreferredSize(new Dimension(250, 600));
        leftSidebar.setLayout(new BoxLayout(leftSidebar, BoxLayout.Y_AXIS));
        leftSidebar.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        // styled navigation buttons
        String[] buttonLabels = {"Home", "Create", "Search", "App Info", "Communities"};

        for (String label : buttonLabels) {
            JButton navButton = createNavButton(label);
            navButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            navButton.setMaximumSize(new Dimension(200, 40));
            leftSidebar.add(navButton);
            leftSidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        // Profile sidebar
        JPanel lowerPanel = new JPanel();
        lowerPanel.setBackground(Color.BLACK);
        lowerPanel.setLayout(new BorderLayout());
        lowerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        try {
            String picPath = currentUser.getProfilePicUrl();
            ImageIcon profilePic;

            if (picPath != null && picPath.startsWith("http")) {
                // Load from URL
                profilePic = new ImageIcon(new java.net.URL(picPath));
            } else if (picPath != null && !picPath.isEmpty()) {
                // Load from local file
                profilePic = new ImageIcon(picPath);
            } else {
                // Default fallback image
                profilePic = new ImageIcon("src/com/kaleido/GUI/user.png");
            }

            Image scaledPic = profilePic.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            JLabel pfpLabel = new JLabel(new ImageIcon(scaledPic), SwingConstants.CENTER);
            pfpLabel.setHorizontalAlignment(SwingConstants.CENTER);
            lowerPanel.add(pfpLabel, BorderLayout.NORTH);

        } catch (Exception ex) {
            System.out.println("Error loading profile picture: " + ex.getMessage());
        }

        // === Profile Button ===
        JButton profileBtn = new JButton("Profile");
        profileBtn.setForeground(Color.WHITE);
        profileBtn.setBackground(new Color(30, 30, 32));
        profileBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        profileBtn.setFocusPainted(false);
        profileBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Center the button horizontally
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.BLACK);
        btnPanel.add(profileBtn);

        lowerPanel.add(btnPanel, BorderLayout.CENTER);

        // Add both parts to container
        leftSidebarContainer.add(leftSidebar, BorderLayout.NORTH);
        leftSidebarContainer.add(lowerPanel, BorderLayout.CENTER);


        // Add some vertical space at the bottom
        leftSidebar.add(Box.createVerticalGlue());

        // Main Feed
        JPanel mainFeed = new JPanel();
        mainFeed.setBackground(new Color(19, 19, 20));
        mainFeed.setLayout(new BorderLayout());

        // Create scrollable content panel
        JPanel feedContent = new JPanel();
        feedContent.setBackground(new Color(19, 19, 20));
        feedContent.setLayout(new BoxLayout(feedContent, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(feedContent);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smooth scrolling
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        mainFeed.add(scrollPane, BorderLayout.CENTER);

        feedContent.add(Box.createVerticalStrut(20));

        // Right Sidebar
        JPanel rightSidebar = new JPanel();
        rightSidebar.setBackground(new Color(21,21,23));
        rightSidebar.setPreferredSize(new Dimension(300, 0));

        // Add panels to main content
        mainContent.add(leftSidebarContainer, BorderLayout.WEST);
        mainContent.add(mainFeed, BorderLayout.CENTER);
        mainContent.add(rightSidebar, BorderLayout.EAST);

        // ===== ASSEMBLE EVERYTHING =====
        mainContainer.add(header, BorderLayout.NORTH);
        mainContainer.add(mainContent, BorderLayout.CENTER);

        frame.setContentPane(mainContainer);
        frame.setVisible(true);
    }

    //styled navigation buttons
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(21,21,23));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setFont(new Font("SansSerif", Font.PLAIN, 16));
        button.setHorizontalAlignment(SwingConstants.LEFT);

        //hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(50, 50, 52));
                button.setContentAreaFilled(true);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(21,21,23));
                button.setContentAreaFilled(false);
            }
        });

        return button;
    }

    public static void main(String[] args) {
        User dummyUser = new User();
        dummyUser.setUsername("_Shaheer");
        new Feed(dummyUser);
    }
}