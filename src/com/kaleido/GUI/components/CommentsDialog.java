package com.kaleido.GUI.components;

import com.kaleido.models.Post;
import com.kaleido.models.User;
import com.kaleido.models.Comment;
import com.kaleido.db.CommentDAO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CommentsDialog extends JDialog {
    private Post post;
    private User currentUser;
    private CommentDAO commentDAO;
    private JPanel commentsPanel;
    private JTextArea commentInput;

    public CommentsDialog(JFrame parent, Post post, User currentUser) {
        super(parent, "Comments", true);
        this.post = post;
        this.currentUser = currentUser;
        this.commentDAO = new CommentDAO();

        initializeUI();
        loadComments();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setSize(400, 500);
        setLocationRelativeTo(getParent());
        setBackground(new Color(25, 25, 27));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(25, 25, 27));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("Comments");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        JButton closeButton = new JButton("✕");
        closeButton.setBackground(new Color(25, 25, 27));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        closeButton.addActionListener(e -> dispose());

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(closeButton, BorderLayout.EAST);

        // Comments display area
        commentsPanel = new JPanel();
        commentsPanel.setLayout(new BoxLayout(commentsPanel, BoxLayout.Y_AXIS));
        commentsPanel.setBackground(new Color(25, 25, 27));

        JScrollPane scrollPane = new JScrollPane(commentsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(new Color(25, 25, 27));

        // Comment input area
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(new Color(25, 25, 27));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        commentInput = new JTextArea(3, 20);
        commentInput.setLineWrap(true);
        commentInput.setWrapStyleWord(true);
        commentInput.setBackground(new Color(35, 35, 37));
        commentInput.setForeground(Color.WHITE);
        commentInput.setCaretColor(Color.WHITE);
        commentInput.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        JButton postButton = new JButton("Post");
        postButton.setBackground(new Color(70, 70, 70));
        postButton.setForeground(Color.BLACK);
        postButton.addActionListener(e -> postComment());

        inputPanel.add(new JScrollPane(commentInput), BorderLayout.CENTER);
        inputPanel.add(postButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
    }

    private void loadComments() {
        commentsPanel.removeAll();

        List<Comment> comments = commentDAO.getComments(post.getPostId());
        if (comments.isEmpty()) {
            JLabel noCommentsLabel = new JLabel("No comments yet", SwingConstants.CENTER);
            noCommentsLabel.setForeground(Color.GRAY);
            noCommentsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            commentsPanel.add(noCommentsLabel);
        } else {
            for (Comment comment : comments) {
                commentsPanel.add(createCommentPanel(comment));
                commentsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        commentsPanel.revalidate();
        commentsPanel.repaint();
    }

    private JPanel createCommentPanel(Comment comment) {
        JPanel commentPanel = new JPanel(new BorderLayout());
        commentPanel.setBackground(new Color(35, 35, 37));
        commentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Comment header with username and delete button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(35, 35, 37));

        // Use the username directly from comment object
        JLabel usernameLabel = new JLabel("@" + comment.getUsername());
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("SansSerif", Font.BOLD, 12));

        headerPanel.add(usernameLabel, BorderLayout.WEST);

        // Add delete button if this is the current user's comment
        if (comment.getUserId() == currentUser.getUserID()) {
            JButton deleteButton = new JButton("Delete");
            deleteButton.setBackground(new Color(70, 70, 70));
            deleteButton.setForeground(Color.BLACK);
            deleteButton.setFont(new Font("SansSerif", Font.PLAIN, 10));
            deleteButton.addActionListener(e -> deleteComment(comment));
            headerPanel.add(deleteButton, BorderLayout.EAST);
        }

        // Comment content
        JTextArea contentArea = new JTextArea(comment.getCommentText());
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setBackground(new Color(35, 35, 37));
        contentArea.setForeground(Color.WHITE);
        contentArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
        contentArea.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        commentPanel.add(headerPanel, BorderLayout.NORTH);
        commentPanel.add(contentArea, BorderLayout.CENTER);

        return commentPanel;
    }

    private void postComment() {
        String content = commentInput.getText().trim();
        if (!content.isEmpty()) {
            commentDAO.addComment(post.getPostId(), currentUser.getUserID(), content, post.getUserId());
            commentInput.setText("");
            loadComments(); // Refresh comments
        }
    }

    private void deleteComment(Comment comment) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this comment?",
                "Delete Comment",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            commentDAO.deleteComment(comment.getCommentId());
            loadComments(); // Refresh comments
        }
    }
}