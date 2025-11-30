package com.kaleido.GUI.components;

import com.kaleido.models.User;
import com.kaleido.models.Notification;
import com.kaleido.db.NotificationDAO;
import com.kaleido.db.UserDAO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RightSidebarPanel extends JPanel {

    private User currentUser;
    private JPanel notificationsPanel;
    private NotificationDAO notificationDAO;
    private UserDAO userDAO;

    public RightSidebarPanel(User user) {
        this.currentUser = user;
        this.notificationDAO = new NotificationDAO();
        this.userDAO = new UserDAO();
        initializeUI();
        loadNotifications();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(300, 0));
        setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, Color.GRAY));

        // Main container
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBackground(Color.BLACK);
        mainContainer.setBorder(BorderFactory.createEmptyBorder(100, 15, 20, 15));

        // Notifications header
        JLabel titleLabel = new JLabel("🔔 Notifications");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        mainContainer.add(titleLabel);
        mainContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        // Notifications list
        notificationsPanel = new JPanel();
        notificationsPanel.setLayout(new BoxLayout(notificationsPanel, BoxLayout.Y_AXIS));
        notificationsPanel.setBackground(new Color(21, 21, 23));

        JScrollPane scrollPane = new JScrollPane(notificationsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(new Color(21, 21, 23));
        scrollPane.setPreferredSize(new Dimension(270, 400));

        mainContainer.add(scrollPane);
        add(mainContainer, BorderLayout.NORTH);
    }

    private void loadNotifications() {
        notificationsPanel.removeAll();

        // Use actual NotificationDAO
        List<Notification> notifications = notificationDAO.getNotificationsForUser(currentUser.getUserID());

        if (notifications.isEmpty()) {
            JLabel noNotificationsLabel = new JLabel("No notifications", SwingConstants.CENTER);
            noNotificationsLabel.setForeground(Color.GRAY);
            noNotificationsLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
            noNotificationsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            notificationsPanel.add(noNotificationsLabel);
        } else {
            for (Notification notification : notifications) {
                JPanel notificationCard = createNotificationCard(notification);
                notificationsPanel.add(notificationCard);
                notificationsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        }

        notificationsPanel.revalidate();
        notificationsPanel.repaint();
    }

    private JPanel createNotificationCard(Notification notification) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(35, 35, 37));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setMaximumSize(new Dimension(270, 70));

        // Icon based on type
        JLabel iconLabel = new JLabel();
        iconLabel.setPreferredSize(new Dimension(25, 25));

        if (notification.getType().equals("like")) {
            iconLabel.setText("💜");
        } else if (notification.getType().equals("comment")) {
            iconLabel.setText("💬");
        } else {
            iconLabel.setText("🔔");
        }

        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(35, 35, 37));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));

        // Get sender username
        String senderUsername = getSenderUsername(notification.getSenderId());

        // Username and action
        String actionText = getActionText(notification.getType(), senderUsername);
        JLabel messageLabel = new JLabel(actionText);
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        // Time
        JLabel timeLabel = new JLabel(formatTimeAgo(notification.getCreatedAt()));
        timeLabel.setForeground(Color.GRAY);
        timeLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));

        contentPanel.add(messageLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        contentPanel.add(timeLabel);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

    private String getSenderUsername(int senderId) {
        try {
            // Get sender's username from UserDAO
            com.kaleido.models.User sender = userDAO.getUserById(senderId);
            return sender != null ? sender.getUsername() : "Someone";
        } catch (Exception e) {
            return "Someone";
        }
    }

    private String getActionText(String type, String username) {
        switch (type) {
            case "like":
                return username + " liked your post";
            case "comment":
                return username + " commented on your post";
            default:
                return username + " interacted with your post";
        }
    }

    private String formatTimeAgo(java.time.LocalDateTime dateTime) {
        if (dateTime == null) return "Recently";

        java.time.Duration duration = java.time.Duration.between(dateTime, java.time.LocalDateTime.now());

        if (duration.toMinutes() < 1) {
            return "Just now";
        } else if (duration.toHours() < 1) {
            return duration.toMinutes() + "m ago";
        } else if (duration.toDays() < 1) {
            return duration.toHours() + "h ago";
        } else {
            return duration.toDays() + "d ago";
        }
    }

    public void refreshNotifications() {
        loadNotifications();
    }
}