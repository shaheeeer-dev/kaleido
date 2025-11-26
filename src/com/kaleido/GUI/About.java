package com.kaleido.GUI;

import com.kaleido.GUI.components.HeaderPanel;
import com.kaleido.GUI.components.LeftSidebarPanel;
import com.kaleido.models.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class info {
    JFrame frame;
    User currentUser;
    private JLabel name;
    private JTextArea detailsLabel;
    private JLabel pfpLabel;
    private JPanel appInfoPanel;
    private JScrollPane mainScrollPane;

    public info(User user) {
        this.currentUser = user;
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
        frame = new JFrame("Kaleido | App Info");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel mainContainer = new JPanel(new BorderLayout());

        // Header + Layout
        mainContainer.add(new HeaderPanel(true), BorderLayout.NORTH);
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContainer.add(mainContent, BorderLayout.CENTER);
        mainContent.add(new LeftSidebarPanel(currentUser), BorderLayout.WEST);

        JPanel rightSidebar = new JPanel();
        rightSidebar.setBackground(Color.BLACK);
        rightSidebar.setPreferredSize(new Dimension(300, 0));
        mainContent.add(rightSidebar, BorderLayout.EAST);

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
        String profilePicPath = "https://res.cloudinary.com/defyrn0le/image/upload/v1763494798/ahgwizfvo06ab3ndt9zf.jpg";
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

        name = new JLabel("Muhammad Shaheer");
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SansSerif", Font.BOLD, 24));

        nameDisplay.add(name);

        // Details panel
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBackground(new Color(21, 21, 23));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));
        detailsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String appInfoText =
                "FA24-BSE-104\n\n" +
                        "Object Oriented Programming & Database Project\n\n" +
                        "🌐 Kaleido - Social Media Application\n\n" +
                        "📚 Project Overview:\n" +
                        "• A full-stack social media platform built with Java Swing\n" +
                        "• Cloud-based image storage using Cloudinary\n" +
                        "• MySQL database for data persistence\n" +
                        "• Modern UI with dark theme design\n\n" +
                        "🛠️ Technologies Used:\n" +
                        "• Frontend: Java Swing, AWT\n" +
                        "• Backend: Core Java, JDBC\n" +
                        "• Database: MySQL\n" +
                        "• Cloud Storage: Cloudinary API\n" +
                        "• Version Control: Git\n\n" +
                        "🎯 Features:\n" +
                        "• User authentication and profiles\n" +
                        "• Create and share posts with images\n" +
                        "• Real-time feed updates\n" +
                        "• Profile customization\n" +
                        "• Interactive GUI components\n\n" +
                        "📖 Learning Outcomes:\n" +
                        "• Object-Oriented Programming principles\n" +
                        "• Database design and management\n" +
                        "• GUI development with Swing\n" +
                        "• API integration\n" +
                        "• Software architecture patterns";

        detailsLabel = new JTextArea(appInfoText);
        detailsLabel.setLineWrap(true);
        detailsLabel.setWrapStyleWord(true);
        detailsLabel.setEditable(false);
        detailsLabel.setOpaque(false);
        detailsLabel.setForeground(Color.WHITE);
        detailsLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        detailsLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add scroll pane for details in case content is too long
        JScrollPane detailsScrollPane = new JScrollPane(detailsLabel);
        detailsScrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
        detailsScrollPane.setBackground(new Color(21, 21, 23));
        detailsScrollPane.getViewport().setBackground(new Color(21, 21, 23));
        detailsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        detailsScrollPane.setPreferredSize(new Dimension(800, 400));

        detailsPanel.add(detailsScrollPane, BorderLayout.CENTER);

        // Close button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(21, 21, 23));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 40, 20));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton closeBtn = new JButton("Close");
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBackground(new Color(70, 70, 70));
        closeBtn.setFocusPainted(false);
        closeBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        closeBtn.setPreferredSize(new Dimension(120, 40));

        closeBtn.addActionListener(e -> frame.dispose());

        buttonPanel.add(closeBtn);

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

        // Add the main scroll pane to center
        mainContent.add(mainScrollPane, BorderLayout.CENTER);

        frame.setContentPane(mainContainer);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Test the AppInfo frame
        SwingUtilities.invokeLater(() -> {
            User dummyUser = new User();
            dummyUser.setFirstName("Test");
            dummyUser.setLastName("User");
            new info(dummyUser);
        });
    }
}