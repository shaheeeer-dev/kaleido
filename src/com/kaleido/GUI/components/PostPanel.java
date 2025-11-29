package com.kaleido.GUI.components;

import com.kaleido.models.Post;
import com.kaleido.models.User;
import com.kaleido.models.Comment;
import com.kaleido.db.LikeDAO;
import com.kaleido.db.CommentDAO;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;

public class PostPanel extends JPanel {
    private Post post;
    private User currentUser;
    private LikeDAO likeDAO;
    private CommentDAO commentDAO;
    private JButton likeButton;
    private JButton commentButton;
    private int likeCount;
    private int commentCount;
    private boolean isLiked;

    public PostPanel(Post post, User currentUser) {
        this.post = post;
        this.currentUser = currentUser;
        this.likeDAO = new LikeDAO();
        this.commentDAO = new CommentDAO();

        // Initialize counts
        this.likeCount = likeDAO.countLikes(post.getPostId());
        this.commentCount = commentDAO.getComments(post.getPostId()).size();
        this.isLiked = likeDAO.isLikedByUser(post.getPostId(), currentUser.getUserID());

        initializeUI();
        updateButtonTexts();
    }

    private void initializeUI() {
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
        JPanel headerPanel = createHeader();

        // Content
        JPanel contentPanel = createContent(width);

        // Engagement panel
        JPanel engagementPanel = createEngagementPanel();

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(engagementPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
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

    private JPanel createContent(int containerWidth) {
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
            JPanel imagePanel = createImagePanel(containerWidth);
            contentPanel.add(imagePanel, BorderLayout.CENTER);
        }

        return contentPanel;
    }

    private JPanel createEngagementPanel() {
        JPanel engagementPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        engagementPanel.setBackground(new Color(25, 25, 27));
        engagementPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Like button
        likeButton = new JButton();
        likeButton.setBackground(new Color(25, 25, 27));
        likeButton.setForeground(isLiked ? new Color(255, 48, 64) : Color.LIGHT_GRAY);
        likeButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        likeButton.setFocusPainted(false);
        likeButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        likeButton.addActionListener(e -> handleLike());

        // Comment button
        commentButton = new JButton();
        commentButton.setBackground(new Color(25, 25, 27));
        commentButton.setForeground(Color.LIGHT_GRAY);
        commentButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        commentButton.setFocusPainted(false);
        commentButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        commentButton.addActionListener(e -> showCommentsDialog());

        engagementPanel.add(likeButton);
        engagementPanel.add(commentButton);

        return engagementPanel;
    }

    private void handleLike() {
        if (isLiked) {
            // Unlike the post
            likeDAO.removeLike(post.getPostId(), currentUser.getUserID());
            likeCount--;
            isLiked = false;
            likeButton.setForeground(Color.LIGHT_GRAY);
        } else {
            // Like the post
            likeDAO.addLike(post.getPostId(), currentUser.getUserID(), post.getUserId());
            likeCount++;
            isLiked = true;
            likeButton.setForeground(new Color(255, 48, 64)); // Red color for liked state
        }
        updateButtonTexts();
    }

    private void updateButtonTexts() {
        likeButton.setText("♥ " + likeCount);
        commentButton.setText("💬 " + commentCount);
    }

    private void showCommentsDialog() {
        CommentsDialog dialog = new CommentsDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                post,
                currentUser
        );
        dialog.setVisible(true);

        // Refresh comment count after dialog closes
        commentCount = commentDAO.getComments(post.getPostId()).size();
        updateButtonTexts();
    }

    private JPanel createImagePanel(int containerWidth) {
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(new Color(25, 25, 27));

        try {
            URL imageUrl = new URL(post.getImageUrl());
            ImageIcon originalIcon = new ImageIcon(imageUrl);

            // Calculate dimensions
            int maxWidth = containerWidth - 30;
            int maxHeight = 350;

            int originalWidth = originalIcon.getIconWidth();
            int originalHeight = originalIcon.getIconHeight();

            int scaledWidth, scaledHeight;
            double aspectRatio = (double) originalWidth / originalHeight;

            if (originalWidth > originalHeight) {
                scaledWidth = Math.min(originalWidth, maxWidth);
                scaledHeight = (int) (scaledWidth / aspectRatio);
            } else {
                scaledHeight = Math.min(originalHeight, maxHeight);
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

    private String formatTimestamp(java.time.LocalDateTime dateTime) {
        java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a");
        return dateTime.format(formatter);
    }
}