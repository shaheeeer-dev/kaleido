package com.kaleido.GUI;

import com.kaleido.GUI.components.LeftSidebarPanel;
import com.kaleido.GUI.components.HeaderPanel;
import com.kaleido.models.User;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JPanel centerPanel;
    private User currentUser;

    public MainFrame(User user) {
        this.currentUser = user;

        setTitle("Kaleido");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        // Header
        add(new HeaderPanel(true), BorderLayout.NORTH);

        // Left Sidebar
        add(new LeftSidebarPanel(currentUser, this), BorderLayout.WEST);

        // Right sidebar
        JPanel rightSidebar = new JPanel();
        rightSidebar.setPreferredSize(new Dimension(300, 0));
        rightSidebar.setBackground(Color.BLACK);
        add(rightSidebar, BorderLayout.EAST);

        // Center content
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.BLACK);
        add(centerPanel, BorderLayout.CENTER);

        // DEFAULT PAGE: FEED
        switchPage(new Feed(currentUser, this));

        setVisible(true);
    }

    // Switching pages
    public void switchPage(JPanel newPage) {
        centerPanel.removeAll();
        centerPanel.add(newPage, BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();
    }
}