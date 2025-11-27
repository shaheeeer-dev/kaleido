package com.kaleido.GUI.components;

import com.kaleido.models.Post;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class PostPanel extends JPanel {

    public PostPanel(Post post) {
        setLayout(new BorderLayout());
        setBackground(new Color(25, 25, 27));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Set 4:5 aspect ratio
        int width = 468;
        int height = 585;
        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));

        // Header
        JPanel headerPanel = createHeader(post);

        // Content - dynamically handles text and images
        JPanel contentPanel = createContent(post, width);

        // Engagement buttons
        JPanel engagementPanel = createEngagementPanel();

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(engagementPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeader(Post post) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(25, 25, 27));

        JLabel usernameLabel = new JLabel(post.getUsername());
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        JLabel timeLabel = new JLabel(formatTimestamp(post.getCreatedAt()));
        timeLabel.setForeground(Color.GRAY);
        timeLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        headerPanel.add(usernameLabel, BorderLayout.WEST);
        headerPanel.add(timeLabel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createContent(Post post, int containerWidth) {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(25, 25, 27));

        // Text content
        JTextArea contentArea = new JTextArea(post.getContentText());
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setBackground(new Color(25, 25, 27));
        contentArea.setForeground(Color.WHITE);
        contentArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        contentArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

        contentPanel.add(contentArea, BorderLayout.NORTH);

        // Image content (if exists)
        if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
            JPanel imagePanel = createImagePanel(post, containerWidth);
            contentPanel.add(imagePanel, BorderLayout.CENTER);
        }

        return contentPanel;
    }

    private JPanel createImagePanel(Post post, int containerWidth) {
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(new Color(25, 25, 27));

        try {
            URL imageUrl = new URL(post.getImageUrl());
            ImageIcon originalIcon = new ImageIcon(imageUrl);

            // Calculate dimensions to maintain aspect ratio and fit within post
            int maxWidth = containerWidth - 30; // Account for padding
            int maxHeight = 350;

            int originalWidth = originalIcon.getIconWidth();
            int originalHeight = originalIcon.getIconHeight();

            // Calculate scaled dimensions maintaining aspect ratio
            int scaledWidth, scaledHeight;
            double aspectRatio = (double) originalWidth / originalHeight;

            if (originalWidth > originalHeight) {
                // Landscape image
                scaledWidth = Math.min(originalWidth, maxWidth);
                scaledHeight = (int) (scaledWidth / aspectRatio);
            } else {
                // Portrait or square image
                scaledHeight = Math.min(originalHeight, maxHeight);
                scaledWidth = (int) (scaledHeight * aspectRatio);
            }

            // Ensure scaled dimensions don't exceed limits
            if (scaledWidth > maxWidth) {
                scaledWidth = maxWidth;
                scaledHeight = (int) (scaledWidth / aspectRatio);
            }
            if (scaledHeight > maxHeight) {
                scaledHeight = maxHeight;
                scaledWidth = (int) (scaledHeight * aspectRatio);
            }

            Image scaledImage = originalIcon.getImage().getScaledInstance(
                    scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imagePanel.add(imageLabel, BorderLayout.CENTER);

        } catch (Exception e) {
            System.err.println("Error loading post image: " + e.getMessage());
            JLabel errorLabel = new JLabel("📷 Image not available", SwingConstants.CENTER);
            errorLabel.setForeground(Color.GRAY);
            imagePanel.add(errorLabel, BorderLayout.CENTER);
        }

        return imagePanel;
    }

    private JPanel createEngagementPanel() {
        JPanel engagementPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        engagementPanel.setBackground(new Color(25, 25, 27));
        engagementPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton likeButton = new JButton("♥ Like");
        JButton commentButton = new JButton("💬 Comment");

        for (JButton button : new JButton[]{likeButton, commentButton}) {
            button.setBackground(new Color(25, 25, 27));
            button.setForeground(Color.LIGHT_GRAY);
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            button.setFocusPainted(false);
            button.setFont(new Font("SansSerif", Font.PLAIN, 12));
        }

        engagementPanel.add(likeButton);
        engagementPanel.add(commentButton);

        return engagementPanel;
    }

    private String formatTimestamp(java.time.LocalDateTime dateTime) {
        java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a");
        return dateTime.format(formatter);
    }
}