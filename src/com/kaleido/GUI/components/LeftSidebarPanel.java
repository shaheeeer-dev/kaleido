package com.kaleido.GUI.components;

import javax.swing.*;
import java.awt.*;
import static com.kaleido.GUI.components.UIUtils.createNavButton;

public class SidebarPanel extends JPanel {
    public SidebarPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(19, 19, 20));
        setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
        setPreferredSize(new Dimension(250, 0));

        String[] buttonLabels = {"Home", "Create", "Search", "App Info", "Communities"};
        for (String label : buttonLabels) {
            JButton navButton = createNavButton(label);
            add(navButton);
            add(Box.createRigidArea(new Dimension(0, 20)));
        }
    }
}