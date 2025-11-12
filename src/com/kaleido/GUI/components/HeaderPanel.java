package com.kaleido.GUI.components;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel {
    public HeaderPanel(boolean showLogoutButton) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(0, 60));
        setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        // Logo
        JLabel logoLabel = new JLabel("Kaleido");
        ImageIcon logo = new ImageIcon("src/com/kaleido/GUI/Kaleidologo.png");
        if (logo.getImageLoadStatus() == MediaTracker.COMPLETE) {
            Image scaledLogo = logo.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledLogo));
        }
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(logoLabel, BorderLayout.WEST);

        if (showLogoutButton) {
            JButton logOut = new JButton("Logout");
            JPanel rightPanel = new JPanel();
            rightPanel.setBackground(Color.BLACK);
            rightPanel.add(logOut);
            add(rightPanel, BorderLayout.EAST);
        }
    }
}