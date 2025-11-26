package com.kaleido.GUI;

import com.kaleido.models.*;
import com.kaleido.db.PostDAO;
import com.kaleido.models.Post;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class Profile extends JPanel {

    private User currentUser;
    private MainFrame mainFrame;
    private JLabel name;
    private JTextArea bioLabel;
    private JLabel pfpLabel;
    private JPanel profileContent;
    private JScrollPane scrollPane;

    public Profile(User user, MainFrame mainFrame) {
        this.currentUser = user;
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // Profile content panel
        profileContent = new JPanel();
        profileContent.setLayout(new BoxLayout(profileContent, BoxLayout.Y_AXIS));
        profileContent.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 2, Color.GRAY));
        profileContent.setBackground(new Color(21, 21, 23));
        // PFP PANEL
        JPanel pfpPanel = new JPanel();
        pfpPanel.setBackground(new Color(21, 21, 23));

        pfpLabel = new JLabel();
        pfpPanel.add(pfpLabel);

        String profilePicPath = currentUser.getProfilePicUrl();
        ImageIcon pfpIcon = null;

        // Try loading profile image safely
        if (profilePicPath != null && !profilePicPath.trim().isEmpty()) {
            try {
                URL imageUrl = new URL(profilePicPath);
                BufferedImage img = ImageIO.read(imageUrl);

                if (img != null) {
                    pfpIcon = new ImageIcon(img);
                } else {
                    System.out.println("ImageIO returned null — using default image.");
                }

            } catch (Exception e) {
                System.out.println("Failed to load profile picture from URL:");
                System.out.println(profilePicPath);
                e.printStackTrace();
            }
        }

        // If URL image failed → load fallback
        if (pfpIcon == null) {
            pfpIcon = new ImageIcon("src/com/kaleido/GUI/user.png");
        }

        // Scale for display
        Image scaled = pfpIcon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
        pfpLabel.setIcon(new ImageIcon(scaled));


        // Name panel
        JPanel nameDisplay = new JPanel();
        nameDisplay.setBackground(new Color(21, 21, 23));
        nameDisplay.setBorder(BorderFactory.createEmptyBorder(5, 20, 1, 20));

        name = new JLabel();
        String fullName = currentUser.getFirstName() + " " + currentUser.getLastName();
        name.setText(fullName);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SansSerif", Font.BOLD, 18));

        nameDisplay.add(name);

        // Bio panel
        JPanel bioPanel = new JPanel(new BorderLayout());
        bioPanel.setBackground(new Color(21, 21, 23));
        bioPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 1, 20));

        bioLabel = new JTextArea(currentUser.getBio());
        bioLabel.setLineWrap(true);
        bioLabel.setWrapStyleWord(true);
        bioLabel.setEditable(false);
        bioLabel.setOpaque(false);
        bioLabel.setForeground(Color.WHITE);
        bioLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        bioPanel.add(bioLabel, BorderLayout.CENTER);

        // Edit profile button panel
        JPanel editPanel = new JPanel();
        editPanel.setBackground(new Color(21, 21, 23));
        editPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        JButton editbtn = new JButton("Edit Profile");
        editbtn.setForeground(new Color(21, 21, 23));
        editPanel.add(editbtn);

        editbtn.addActionListener(e -> {
            EditProfileFrame editFrame = new EditProfileFrame(currentUser, this);
            editFrame.setLocationRelativeTo(mainFrame);
            editFrame.setVisible(true);
        });

        // Assemble profile panels
        profileContent.add(pfpPanel);
        profileContent.add(nameDisplay);
        profileContent.add(bioPanel);
        profileContent.add(editPanel);

        // Scrollable posts section
        JPanel postsPanel= new JPanel();
        postsPanel.setBackground(new Color(21, 21, 23));
        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));

        PostDAO postDAO = new PostDAO();
        List<Post> userPosts = postDAO.getPostsByUser(currentUser.getUserID());

        for (Post post : userPosts) {
            JPanel postPanel = createPostPanel(post); // Use the new method
            postsPanel.add(postPanel);
            postsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        scrollPane = new JScrollPane(postsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);

        profileContent.add(scrollPane);

        // Add profileContent panel to this JPanel
        add(profileContent, BorderLayout.CENTER);
    }

    private JPanel createPostPanel(Post post) {
        JPanel postPanel = new JPanel();
        postPanel.setLayout(new BorderLayout());
        postPanel.setBackground(new Color(35, 35, 37));
        postPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        postPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400)); // Increased height for images

        // Header with username and timestamp
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(35, 35, 37));

        JLabel usernameLabel = new JLabel(post.getUsername());
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        JLabel timeLabel = new JLabel(formatTimestamp(post.getCreatedAt()));
        timeLabel.setForeground(Color.GRAY);
        timeLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        headerPanel.add(usernameLabel, BorderLayout.WEST);
        headerPanel.add(timeLabel, BorderLayout.EAST);

        // Post content
        JTextArea contentArea = new JTextArea(post.getContentText());
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setBackground(new Color(35, 35, 37));
        contentArea.setForeground(Color.WHITE);
        contentArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        contentArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Image display (if exists)
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(new Color(35, 35, 37));

        if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
            try {
                // Load image from Cloudinary URL
                ImageIcon originalIcon = new ImageIcon(new java.net.URL(post.getImageUrl()));
                Image scaledImage = originalIcon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                imagePanel.add(imageLabel, BorderLayout.CENTER);

                // Add some spacing between text and image
                contentArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

            } catch (Exception e) {
                System.err.println("Error loading post image: " + e.getMessage());
                // If image fails to load, show error placeholder
                JLabel errorLabel = new JLabel("📷 Image not available", SwingConstants.CENTER);
                errorLabel.setForeground(Color.GRAY);
                imagePanel.add(errorLabel, BorderLayout.CENTER);
            }
        }

        // Assemble the post panel
        postPanel.add(headerPanel, BorderLayout.NORTH);
        postPanel.add(contentArea, BorderLayout.CENTER);

        // Only add image panel if there's an image
        if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
            postPanel.add(imagePanel, BorderLayout.SOUTH);
        }

        return postPanel;
    }

    // Helper method to format timestamp
    private String formatTimestamp(java.time.LocalDateTime dateTime) {
        java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a");
        return dateTime.format(formatter);
    }

    public static void main(String[] args) {
        User dummyUser = new User();
        dummyUser.setFirstName("Muhammad");
        dummyUser.setLastName("Shaheer");
        dummyUser.setUsername("_Shaheer");
        dummyUser.setProfilePicUrl("https://res.cloudinary.com/defyrn0le/image/upload/v1763494798/ahgwizfvo06ab3ndt9zf.jpg");
        dummyUser.setBio("BSE’28 COMSATS | Aspiring Backend Developer | Focused on Java, OOP, and Database Systems | Eager to Build Real-World Projects");
    }
}