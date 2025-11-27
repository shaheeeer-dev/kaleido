package com.kaleido.GUI;

import com.kaleido.GUI.components.PostPanel;
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
    private User viewedUser;

    public Profile(User viewedUser, User currentUser, MainFrame mainFrame) {
        this.currentUser = currentUser;
        this.viewedUser = viewedUser;
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

        String profilePicPath = viewedUser.getProfilePicUrl();
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
        String fullName = viewedUser.getFirstName() + " " + viewedUser.getLastName();
        name.setText(fullName);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SansSerif", Font.BOLD, 18));

        nameDisplay.add(name);

        // Bio panel
        JPanel bioPanel = new JPanel(new BorderLayout());
        bioPanel.setBackground(new Color(21, 21, 23));
        bioPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 1, 20));

        bioLabel = new JTextArea(viewedUser.getBio());
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
        if (currentUser.getUserID() == viewedUser.getUserID()) {
            profileContent.add(editPanel);
        }

        // Scrollable posts section
        JPanel postsPanel = new JPanel();
        postsPanel.setBackground(new Color(21, 21, 23));
        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));

        PostDAO postDAO = new PostDAO();
        List<Post> userPosts = postDAO.getPostsByUser(viewedUser.getUserID());

        // Center alignment wrapper for posts
        JPanel centeredPostsPanel = new JPanel();
        centeredPostsPanel.setLayout(new BoxLayout(centeredPostsPanel, BoxLayout.Y_AXIS));
        centeredPostsPanel.setBackground(new Color(21, 21, 23));

        for (Post post : userPosts) {
            // Use the PostPanel component with 4:5 ratio
            PostPanel postPanel = new PostPanel(post);
            postPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            centeredPostsPanel.add(postPanel);
            centeredPostsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        // If no posts, show message
        if (userPosts.isEmpty()) {
            JLabel noPostsLabel = new JLabel("No posts yet", SwingConstants.CENTER);
            noPostsLabel.setForeground(Color.GRAY);
            noPostsLabel.setFont(new Font("SansSerif", Font.ITALIC, 26));
            noPostsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            centeredPostsPanel.add(noPostsLabel);
        }

        postsPanel.add(centeredPostsPanel);

        scrollPane = new JScrollPane(postsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        profileContent.add(scrollPane);

        add(profileContent, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        User dummyUser = new User();
        dummyUser.setFirstName("Muhammad");
        dummyUser.setLastName("Shaheer");
        dummyUser.setUsername("_Shaheer");
        dummyUser.setProfilePicUrl("https://res.cloudinary.com/defyrn0le/image/upload/v1763494798/ahgwizfvo06ab3ndt9zf.jpg");
        dummyUser.setBio("BSE'28 COMSATS | Aspiring Backend Developer | Focused on Java, OOP, and Database Systems | Eager to Build Real-World Projects");
    }
}