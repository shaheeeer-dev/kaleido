package com.kaleido.GUI.components;

import com.kaleido.GUI.*;
import com.kaleido.models.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

import static com.kaleido.GUI.components.UIUtils.createNavButton;

public class LeftSidebarPanel extends JPanel {

    JPanel upperPanel;
    JPanel lowerPanel;
    User currentUser;
    MainFrame mainFrame;


    public LeftSidebarPanel(User user, MainFrame mainFrame) {
        this.currentUser = user;
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(250, 0));

        // UPPER PANEL (Nav Buttons)
        upperPanel = new JPanel();
        upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.Y_AXIS));
        upperPanel.setBackground(Color.BLACK);
        upperPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        String[] buttonLabels = {"Home", "Create", "Search", "About", "Communities"};

        for (String label : buttonLabels) {
            JButton navButton = createNavButton(label);
            navButton.setAlignmentX(Component.LEFT_ALIGNMENT);

            navButton.addActionListener(e -> {
                switch (label) {
                    case "Home":
                        System.out.println("Home clicked!");
                        mainFrame.switchPage(new Feed(currentUser, mainFrame));
                        break;

                    case "Create":
                        new CreatePost(currentUser, this);
                        break;

                    case "Search":
                        System.out.println("Search clicked!");
                        mainFrame.switchPage(new Search(currentUser, mainFrame));
                        break;

                    case "About":
                        System.out.println("App About clicked!");
                        mainFrame.switchPage(new About(currentUser, mainFrame));
                        break;

                    case "Communities":
                        System.out.println("Communities clicked!");
                        break;
                }
            });

            upperPanel.add(navButton);
            upperPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        add(upperPanel, BorderLayout.NORTH);

        // LOWER PANEL (Profile Button)
        lowerPanel = new JPanel(new BorderLayout());
        lowerPanel.setBackground(Color.BLACK);
        lowerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 0, 0, Color.GRAY),
                BorderFactory.createEmptyBorder(15, 30, 15, 10)
        ));
        lowerPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // PFP
        ImageIcon profilePic = null;
        String picPath = currentUser != null ? currentUser.getProfilePicUrl() : null;

        BufferedImage img = null;

        if (picPath != null && !picPath.trim().isEmpty()) {
            try {
                URL url = new URL(picPath);
                img = ImageIO.read(url);

                if (img != null) {
                    profilePic = new ImageIcon(img);
                } else {
                    System.out.println("ImageIO returned null — using fallback.");
                }

            } catch (Exception ex) {
                System.out.println("Failed to load profile picture from URL:");
                System.out.println(picPath);
                ex.printStackTrace();
            }
        }

        // Fallback image if URL failed
        if (profilePic == null) {
            profilePic = new ImageIcon("src/com/kaleido/GUI/user.png");
        }

        // Scale to 40x40 for sidebar
        Image scaled = profilePic.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel pfpLabel = new JLabel(new ImageIcon(scaled));

        // Text
        JLabel profileText = new JLabel("Profile");
        profileText.setForeground(Color.WHITE);
        profileText.setFont(new Font("SansSerif", Font.BOLD, 16));
        profileText.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        lowerPanel.add(pfpLabel, BorderLayout.WEST);
        lowerPanel.add(profileText, BorderLayout.CENTER);

        // Hover
        lowerPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                lowerPanel.setBackground(new Color(50, 50, 55));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                lowerPanel.setBackground(Color.BLACK);
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                System.out.println("Profile Clicked");
                mainFrame.switchPage(new Profile(currentUser, currentUser, mainFrame));
            }
        });

        add(lowerPanel, BorderLayout.CENTER);

        // RATIO FIX
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {

                int totalHeight = getHeight();
                int upperHeight = (int) (totalHeight * 0.87);

                upperPanel.setPreferredSize(new Dimension(250, upperHeight));
                revalidate();
            }
        });
    }

    public void refreshPosts () {
    }
}
