package com.kaleido.GUI.components;

import javax.swing.*;
import java.awt.*;

public class UIUtils {
    public static JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(21, 21, 23));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setFont(new Font("SansSerif", Font.PLAIN, 16));
        button.setHorizontalAlignment(SwingConstants.LEFT);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(50, 50, 52));
                button.setContentAreaFilled(true);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(21, 21, 23));
                button.setContentAreaFilled(false);
            }
        });

        return button;
    }

    // Generic hover effect for panels
    public static void addHoverEffect(JPanel panel, Color normal, Color hover) {
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                panel.setBackground(hover);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                panel.setBackground(normal);
            }
        });
    }
}