package com.kaleido.GUI;

import com.kaleido.models.*;
import javax.swing.*;
import java.awt.*;

public class Profile {
    JFrame frame;
    User currentUser;

    public Profile(User user){
        this.currentUser = user;
        initializeGUI();
    }
    void initializeGUI(){
        frame = new JFrame();
        frame.setTitle("Kaleido | Profile");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        //Main Container
        JPanel mainContainer = new JPanel(new BorderLayout());

        //Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.BLACK);
        header.setPreferredSize(new Dimension(0,60));
        header.setBorder(BorderFactory.createEmptyBorder(5,20,5,20));

        //LOGO
        JLabel logoLabel = new JLabel("Kaleido");
        ImageIcon logo = new ImageIcon("src/com/kaleido/GUI/Kaleidologo.png");
        if (logo.getImageLoadStatus() == MediaTracker.COMPLETE) {
            Image scaledLogo = logo.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledLogo));
        }
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        header.add(logoLabel, BorderLayout.WEST);

        //Logout btn
        JPanel logOutPanel = new JPanel();
        logOutPanel.setBackground(Color.BLACK);
        logOutPanel.setBorder(BorderFactory.createEmptyBorder(5,20,5,20));

        JButton logOut = new JButton("Logout");
        logOut.setForeground(Color.BLACK);
        logOutPanel.add(logOut);

        header.add(logOutPanel, BorderLayout.EAST);

        // MAIN CONTENT
        JPanel mainContent = new JPanel(new BorderLayout());

        // Left Sidebar Container
        JPanel leftSidebarContainer = new JPanel();
        leftSidebarContainer.setLayout(new BorderLayout());
        leftSidebarContainer.setBackground(new Color(19, 19, 20));
        leftSidebarContainer.setPreferredSize(new Dimension(250, 0));

        // Right Sidebar
        JPanel rightSidebar = new JPanel();
        rightSidebar.setBackground(new Color(21,21,23));
        rightSidebar.setPreferredSize(new Dimension(300, 0));

        // Profile
        JPanel profile = new JPanel();
        profile.setLayout(new BorderLayout());
        profile.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 2, Color.GRAY));
//        profile.setBackground(new Color(21,21,23));

        // PFP PANEL
        JPanel pfpPanel = new JPanel();
        pfpPanel.setBackground(new Color(21, 21, 23));
        pfpPanel.setPreferredSize(new Dimension(0, 200));

        pfpPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        String profilePicPath = currentUser.getProfilePicUrl();
        ImageIcon pfpIcon;
        if (profilePicPath != null && !profilePicPath.isEmpty()) {
            pfpIcon = new ImageIcon(profilePicPath);
        } else {
            pfpIcon = new ImageIcon("src/com/kaleido/GUI/user.png");
        }
        Image img = pfpIcon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
        JLabel pfpLabel = new JLabel(new ImageIcon(img));

        pfpPanel.add(pfpLabel, gbc);
        profile.add(pfpPanel, BorderLayout.NORTH);

        //Assemble Everything
        mainContainer.add(header, BorderLayout.NORTH);
        mainContainer.add(mainContent, BorderLayout.CENTER);
        mainContent.add(leftSidebarContainer, BorderLayout.WEST);
        mainContent.add(profile, BorderLayout.CENTER);
        mainContent.add(rightSidebar, BorderLayout.EAST);

        frame.setContentPane(mainContainer);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        User dummyUser = new User();
        dummyUser.setUsername("_Shaheer");
        new Profile(dummyUser).initializeGUI();
    }
}
