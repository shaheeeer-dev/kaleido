package com.kaleido.GUI;

import com.kaleido.GUI.components.HeaderPanel;
import com.kaleido.GUI.components.LeftSidebarPanel;
import com.kaleido.GUI.components.PostPanel;
import com.kaleido.models.*;
import com.kaleido.db.PostDAO;
import com.kaleido.models.Post;
import java.util.List;
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
        frame = new JFrame("Kaleido | Profile");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel mainContainer = new JPanel(new BorderLayout());

        // Header + Layout
        mainContainer.add(new HeaderPanel(true), BorderLayout.NORTH);
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContainer.add(mainContent, BorderLayout.CENTER);
        mainContent.add(new LeftSidebarPanel(currentUser), BorderLayout.WEST);

        JPanel rightSidebar = new JPanel();
        rightSidebar.setBackground(Color.BLACK);
        rightSidebar.setPreferredSize(new Dimension(300, 0));
        mainContent.add(rightSidebar, BorderLayout.EAST);


        // Profile
        JPanel profile = new JPanel();
        profile.setLayout(new BoxLayout(profile, BoxLayout.Y_AXIS));
        profile.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 2, Color.GRAY));
//        profile.setBackground(new Color(21,21,23));

        // PFP PANEL
        JPanel pfpPanel = new JPanel();
        pfpPanel.setBackground(new Color(21, 21, 23));
//        pfpPanel.setPreferredSize(new Dimension(0, 200));

        String profilePicPath = currentUser.getProfilePicUrl();
        ImageIcon pfpIcon;
        if (profilePicPath != null && !profilePicPath.isEmpty()) {
            pfpIcon = new ImageIcon(profilePicPath);
        } else {
            pfpIcon = new ImageIcon("src/com/kaleido/GUI/user.png");
        }
        Image img = pfpIcon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
        JLabel pfpLabel = new JLabel(new ImageIcon(img));

        pfpPanel.add(pfpLabel);

        //Name
        JPanel nameDisplay = new JPanel();
        nameDisplay.setBackground(new Color(21, 21, 23));
//        nameDisplay.setPreferredSize(new Dimension(886, 60));
        nameDisplay.setBorder(BorderFactory.createEmptyBorder(5,20,1,20));

        JLabel name = new JLabel();
        String fullName = currentUser.getFirstName() + " " + currentUser.getLastName();
        name.setText(fullName);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SansSerif", Font.BOLD, 18));

        nameDisplay.add(name);

        // Bio section
        JPanel bioPanel = new JPanel(new BorderLayout());
        bioPanel.setBackground(new Color(21, 21, 23));
        bioPanel.setBorder(BorderFactory.createEmptyBorder(5,20,1,20));

        JTextArea bioLabel = new JTextArea(currentUser.getBio());
        bioLabel.setLineWrap(true);
        bioLabel.setWrapStyleWord(true);
        bioLabel.setEditable(false);
        bioLabel.setOpaque(false);
        bioLabel.setForeground(Color.WHITE);
        bioLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        bioPanel.add(bioLabel, BorderLayout.CENTER);

        //Edit profile
        JPanel editPanel = new JPanel();
        editPanel.setBackground(new Color(21, 21, 23));
        editPanel.setBorder(BorderFactory.createEmptyBorder(5,20,5,20));

        JButton editbtn = new JButton("Edit Profile");
        editbtn.setForeground(new Color(21,21,23));
        editPanel.add(editbtn);

        // Panels in order
        profile.add(pfpPanel);
        profile.add(nameDisplay);
        profile.add(bioPanel);
        profile.add(editPanel);

        // Scrollable posts section
        JPanel postsPanel = new JPanel();
        postsPanel.setBackground(new Color(21, 21, 23));
        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));

        PostDAO postDAO = new PostDAO();
        List<Post> userPosts = postDAO.getPostsByUser(currentUser.getUserID());

        for(Post post : userPosts){
            PostPanel postPanel = new PostPanel(post);
            postsPanel.add(postPanel);
            postsPanel.add(Box.createRigidArea(new Dimension(0,10)));
        }

        JScrollPane scrollPane = new JScrollPane(postsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);

        profile.add(scrollPane);

        //Assemble Everything
        mainContent.add(profile, BorderLayout.CENTER);

        frame.setContentPane(mainContainer);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        User dummyUser = new User();
        dummyUser.setFirstName("Muhammad");
        dummyUser.setLastName("Shaheer");
        dummyUser.setUsername("_Shaheer");
        dummyUser.setBio("BSE’28 COMSATS | Aspiring Backend Developer | Focused on Java, OOP, and Database Systems | Eager to Build Real-World Projects");
        new Profile(dummyUser);
    }
}
