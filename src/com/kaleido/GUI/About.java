package com.kaleido.GUI;

import com.kaleido.models.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class About extends JPanel {
    User currentUser;
    private MainFrame mainFrame;
    private JLabel name;
    private JTextArea detailsLabel;
    private JLabel pfpLabel;
    private JScrollPane mainScrollPane;

    public About(User user, MainFrame mainFrame) {
        this.currentUser = user;
        this.mainFrame = mainFrame;
        try {
            initializeGUI();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Failed to initialize app info UI: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    void initializeGUI() throws Exception {
        this.setLayout(new BorderLayout());

        // main content panel(scrollable)
        JPanel scrollableContentPanel = new JPanel();
        scrollableContentPanel.setLayout(new BoxLayout(scrollableContentPanel, BoxLayout.Y_AXIS));
        scrollableContentPanel.setBackground(new Color(21, 21, 23));
        scrollableContentPanel.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 2, Color.GRAY));

        // PFP PANEL
        JPanel pfpPanel = new JPanel();
        pfpPanel.setBackground(new Color(21, 21, 23));
        pfpPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        pfpPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        pfpLabel = new JLabel();
        pfpPanel.add(pfpLabel);

        // Use your Cloudinary profile picture
        String profilePicPath = "https://res.cloudinary.com/defyrn0le/image/upload/v1764160321/Kaleidologo_geg5tn.png";
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
        nameDisplay.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        nameDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);

        name = new JLabel("Kaleido - Social Media Application");
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SansSerif", Font.BOLD, 24));

        nameDisplay.add(name);

        // Details panel
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBackground(new Color(21, 21, 23));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));
        detailsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String appInfoText =
                "Kaleido is a fully-featured social media application built with Java Swing, offering a modern and intuitive platform for content sharing and community engagement. Designed with a sleek dark theme, Kaleido provides users with a seamless social experience.\n\n"+
                        "Developer: Muhammad Shaheer\nStudent ID: FA24-BSE-104\nDegree Program: BSE (Software Engineering)\n" +
                        "COMSATS University Islamabad, Sahiwal Campus, Pakistan\n\n" +
                        "📚 Project Overview:\n" +
                        "• A full-stack social media platform built with Java Swing\n" +
                        "• Cloud-based image storage using Cloudinary\n" +
                        "• MySQL database for data persistence\n" +
                        "• Modern UI with dark theme design\n\n" +
                        "🛠️ Technologies Used:\n" +
                        "• Frontend: Java Swing, AWT\n" +
                        "• Backend: Core Java\n" +
                        "• Database: MySQL\n" +
                        "• Cloud Storage: Cloudinary API\n" +
                        "• Version Control: Git\n\n" +
                        "🎯 **Features**:\n" +
                        "• Authentication & User Profiles\n" +
                        "• Post Creation (Text/Images/GIFs)\n" +
                        "• Like & Comment System\n" +
                        "• Real-time Notifications\n" +
                        "• User Search & Discovery\n" +
                        "• Community Messaging\n" +
                        "• Shuffled Feed\n" +
                        "• Dark Theme UI\n\n" +
                        "📖 Learning Outcomes:\n" +
                        "• Object-Oriented Programming principles\n" +
                        "• Database design and management\n" +
                        "• GUI development with Swing\n" +
                        "• API integration\n" +
                        "• Software architecture patterns\n\n"+
                        "📌Developer's Portfolio:\n";

        detailsLabel = new JTextArea(appInfoText);
        detailsLabel.setLineWrap(true);
        detailsLabel.setWrapStyleWord(true);
        detailsLabel.setEditable(false);
        detailsLabel.setOpaque(false);
        detailsLabel.setForeground(Color.WHITE);
        detailsLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        detailsLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));

        // Add detailsLabel directly to detailsPanel (no scroll pane)
        detailsPanel.add(detailsLabel, BorderLayout.CENTER);

        // Portfolio button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(21, 21, 23));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 40, 20));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton PortfolioBtn = new JButton("View Portfolio");
        PortfolioBtn.setForeground(new Color(38, 98, 221));
        PortfolioBtn.setBackground(new Color(70, 70, 70));
        PortfolioBtn.setFocusPainted(false);
        PortfolioBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        PortfolioBtn.setPreferredSize(new Dimension(120, 40));

        PortfolioBtn.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new java.net.URI("https://shaheer-dev.netlify.app/"));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to open portfolio link", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(PortfolioBtn);

        // Assemble ALL content into the scrollable panel
        scrollableContentPanel.add(pfpPanel);
        scrollableContentPanel.add(nameDisplay);
        scrollableContentPanel.add(detailsPanel);
        scrollableContentPanel.add(buttonPanel);

        // Create main scroll pane that contains ALL content (image + text + button)
        mainScrollPane = new JScrollPane(scrollableContentPanel);
        mainScrollPane.setBorder(null);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Add the main scroll pane to this panel center
        this.add(mainScrollPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        // Test the AboutPage panel in a frame
        SwingUtilities.invokeLater(() -> {
            JFrame testFrame = new JFrame("Test AboutPage");
            testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            testFrame.setSize(1000, 700);
        });
    }
}