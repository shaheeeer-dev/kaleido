package com.kaleido.GUI;

import com.kaleido.GUI.components.PostPanel;
import com.kaleido.models.*;
import com.kaleido.db.PostDAO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Feed extends JPanel {
    private User currentUser;
    private MainFrame mainFrame;
    private JPanel postsPanel;
    private JScrollPane scrollPane;
    private PostDAO postDAO;

    public Feed(User user, MainFrame mainFrame) {
        this.currentUser = user;
        this.mainFrame = mainFrame;
        this.postDAO = new PostDAO();
        initializeGUI();
        loadShuffledPosts();
    }

    void initializeGUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(21, 21, 23));

        // Create posts panel
        postsPanel = new JPanel();
        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));
        postsPanel.setBackground(new Color(21, 21, 23));

        // Center align wrapper for posts
        JPanel centeredPostsPanel = new JPanel();
        centeredPostsPanel.setLayout(new BoxLayout(centeredPostsPanel, BoxLayout.Y_AXIS));
        centeredPostsPanel.setBackground(new Color(21, 21, 23));

        postsPanel.add(centeredPostsPanel);

        // Scroll pane
        scrollPane = new JScrollPane(postsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(new Color(21, 21, 23));

        // Set the scrollbar to top position initially
        SwingUtilities.invokeLater(() -> {
            JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
            verticalBar.setValue(0);
        });

        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadShuffledPosts() {
        // Get the centered posts panel
        JPanel centeredPostsPanel = (JPanel) postsPanel.getComponent(0);
        centeredPostsPanel.removeAll();

        // Get random posts
        List<Post> randomPosts = postDAO.getRandomPosts(20, currentUser.getUserID());

        if (randomPosts.isEmpty()) {
            JLabel noPostsLabel = new JLabel("No posts to show. Be the first to post!", SwingConstants.CENTER);
            noPostsLabel.setForeground(Color.GRAY);
            noPostsLabel.setFont(new Font("SansSerif", Font.ITALIC, 16));
            noPostsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            centeredPostsPanel.add(noPostsLabel);
        } else {
            for (Post post : randomPosts) {
                PostPanel postPanel = new PostPanel(post, currentUser);
                postPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                centeredPostsPanel.add(postPanel);
                centeredPostsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }

        centeredPostsPanel.revalidate();
        centeredPostsPanel.repaint();

        // Force scroll to top after content is loaded
        SwingUtilities.invokeLater(() -> {
            scrollPane.getVerticalScrollBar().setValue(0);
        });
    }
}