package com.kaleido.GUI.components;

import com.kaleido.models.Post;

import javax.swing.*;
import java.awt.*;

public class PostPanel extends JPanel {

    public PostPanel(Post post) {
        setLayout(new BorderLayout());
        setBackground(new Color(25, 25, 27));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JLabel userLabel = new JLabel(post.getUsername() + "    " + post.getCreatedAt());
        userLabel.setForeground(Color.LIGHT_GRAY);
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JTextArea contentArea = new JTextArea(post.getContentText());
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setBackground(new Color(25, 25, 27));
        contentArea.setForeground(Color.WHITE);
        contentArea.setFont(new Font("SansSerif", Font.PLAIN, 14));


        add(userLabel, BorderLayout.NORTH);
        add(contentArea, BorderLayout.CENTER);
    }
}