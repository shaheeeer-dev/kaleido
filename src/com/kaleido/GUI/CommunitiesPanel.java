package com.kaleido.GUI;

import com.kaleido.models.User;
import com.kaleido.models.Community;
import com.kaleido.models.CommunityMessage;
import com.kaleido.db.CommunitiesDAO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CommunitiesPanel extends JPanel {

    private User currentUser;
    private CommunitiesDAO communitiesDAO;
    private JTextArea messageText;
    private JTextField communityField;
    private JPanel communitiesDisplayPanel;

    public CommunitiesPanel(User user) {
        this.currentUser = user;
        this.communitiesDAO = new CommunitiesDAO();
        initializeUI();
        loadCommunities();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(21, 21, 23));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top: Message input area
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(new Color(21, 21, 23));

        // Message text area
        messageText = new JTextArea(3, 30);
        messageText.setLineWrap(true);
        messageText.setWrapStyleWord(true);
        messageText.setBackground(new Color(35, 35, 37));
        messageText.setForeground(Color.WHITE);
        messageText.setCaretColor(Color.WHITE);
        messageText.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Community tag and share button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(21, 21, 23));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        communityField = new JTextField();
        communityField.setBackground(new Color(35, 35, 37));
        communityField.setForeground(Color.WHITE);
        communityField.setCaretColor(Color.WHITE);
        communityField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        communityField.setToolTipText("Enter community name");

        JButton shareButton = new JButton("Share");
        shareButton.setBackground(new Color(0, 122, 255));
        shareButton.setForeground(Color.BLACK);
        shareButton.setFocusPainted(false);
        shareButton.setPreferredSize(new Dimension(80, 35));
        shareButton.addActionListener(e -> shareMessage());

        JLabel communityLabel = new JLabel("Community:");
        communityLabel.setForeground(Color.WHITE);

        bottomPanel.add(communityLabel, BorderLayout.WEST);
        bottomPanel.add(communityField, BorderLayout.CENTER);
        bottomPanel.add(shareButton, BorderLayout.EAST);

        inputPanel.add(new JScrollPane(messageText), BorderLayout.CENTER);
        inputPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Center: Communities display
        communitiesDisplayPanel = new JPanel();
        communitiesDisplayPanel.setLayout(new BoxLayout(communitiesDisplayPanel, BoxLayout.Y_AXIS));
        communitiesDisplayPanel.setBackground(new Color(21, 21, 23));

        JScrollPane scrollPane = new JScrollPane(communitiesDisplayPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60)),
                "All Communities"
        ));
        scrollPane.getViewport().setBackground(new Color(21, 21, 23));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void shareMessage() {
        String content = messageText.getText().trim();
        String communityName = communityField.getText().trim();

        if (content.isEmpty() || communityName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both message and community name");
            return;
        }

        // Find or create community
        Community community = communitiesDAO.getCommunityByName(communityName);
        if (community == null) {
            // Create new community
            community = new Community(communityName);
            communitiesDAO.createCommunity(community);
        }

        // Add message to community
        CommunityMessage message = new CommunityMessage(
                community.getCommunityId(),
                currentUser.getUserID(),
                currentUser.getUsername(),
                content
        );
        communitiesDAO.addMessage(message);

        // Clear inputs
        messageText.setText("");
        communityField.setText("");

        // Refresh display
        loadCommunities();

        JOptionPane.showMessageDialog(this, "Message shared to " + communityName);
    }

    private void loadCommunities() {
        communitiesDisplayPanel.removeAll();

        List<Community> communities = communitiesDAO.getAllCommunities();

        if (communities.isEmpty()) {
            JLabel noCommunitiesLabel = new JLabel("No communities yet. Share a message to create one!");
            noCommunitiesLabel.setForeground(Color.GRAY);
            noCommunitiesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            communitiesDisplayPanel.add(noCommunitiesLabel);
        } else {
            for (Community community : communities) {
                communitiesDisplayPanel.add(createCommunitySection(community));
                communitiesDisplayPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }

        communitiesDisplayPanel.revalidate();
        communitiesDisplayPanel.repaint();
    }

    private JPanel createCommunitySection(Community community) {
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
        sectionPanel.setBackground(Color.ORANGE);
        sectionPanel.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));

        // Community header
        JLabel communityLabel = new JLabel("👥 " + community.getName());
        communityLabel.setForeground(Color.WHITE);
        communityLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        communityLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        communityLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        sectionPanel.add(communityLabel);

        // Messages in this community
        List<CommunityMessage> messages = communitiesDAO.getMessagesByCommunity(community.getCommunityId());

        for (CommunityMessage message : messages) {
            JPanel messagePanel = createMessagePanel(message);
            sectionPanel.add(messagePanel);
        }

        return sectionPanel;
    }

    private JPanel createMessagePanel(CommunityMessage message) {
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(new Color(35, 35, 37));
        messagePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(60, 60, 60)),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        messagePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel userLabel = new JLabel(message.getUsername() + ":");
        userLabel.setForeground(new Color(0, 122, 255));
        userLabel.setFont(new Font("SansSerif", Font.BOLD, 12));

        JTextArea contentArea = new JTextArea(message.getContent());
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setBackground(new Color(35, 35, 37));
        contentArea.setForeground(Color.WHITE);
        contentArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
        contentArea.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        messagePanel.add(userLabel, BorderLayout.NORTH);
        messagePanel.add(contentArea, BorderLayout.CENTER);

        return messagePanel;
    }
}