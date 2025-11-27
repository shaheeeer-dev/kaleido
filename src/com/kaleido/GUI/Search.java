package com.kaleido.GUI;

import com.kaleido.db.UserDAO;
import com.kaleido.models.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;

public class Search extends JPanel {

    private User currentUser;
    private MainFrame mainFrame;
    private JTextField searchField;
    private JPanel resultsPanel;
    private JScrollPane scrollPane;
    private UserDAO userDAO = new UserDAO();

    public Search(User user, MainFrame mainFrame) {
        this.currentUser = user;
        this.mainFrame = mainFrame;

        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(21, 21, 23));

        //TOP SEARCH BAR
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(new Color(21, 21, 23));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        searchField = new JTextField();
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        searchField.setBackground(new Color(35, 35, 37));
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60)),
                new EmptyBorder(10, 15, 10, 15)
        ));
        searchField.setToolTipText("Search users by name or username...");

        // Live search while typing
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateResults(searchField.getText().trim());
            }
        });

        searchPanel.add(searchField, BorderLayout.CENTER);

        //RESULTS SECTION
        resultsPanel = new JPanel();
        resultsPanel.setBackground(new Color(21, 21, 23));
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(21, 21, 23));
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Show all users initially
        updateResults("");
    }

    //UPDATE RESULTS
    private void updateResults(String query) {
        resultsPanel.removeAll();

        List<User> matchedUsers = userDAO.searchUsers(query, currentUser.getUserID());

        if (matchedUsers.isEmpty()) {
            JLabel noResult = new JLabel("No users found.");
            noResult.setForeground(Color.GRAY);
            noResult.setFont(new Font("SansSerif", Font.PLAIN, 16));
            noResult.setBorder(new EmptyBorder(20, 20, 20, 20));
            resultsPanel.add(noResult);
        } else {
            for (User user : matchedUsers) {
                resultsPanel.add(createUserCard(user));
                resultsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    //CREATE USER CARD
    private JPanel createUserCard(User user) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(35, 35, 37));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60)),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        // LEFT SIDE: PFP
        JLabel pfpLabel = new JLabel();
        pfpLabel.setPreferredSize(new Dimension(70, 70));

        loadProfilePicture(user.getProfilePicUrl(), pfpLabel);

        // USER INFO
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(35, 35, 37));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(new EmptyBorder(20, 20, 0, 0));

        JLabel name = new JLabel(user.getFirstName() + " " + user.getLastName());
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SansSerif", Font.BOLD, 16));

        JLabel username = new JLabel("@" + user.getUsername());
        username.setForeground(Color.GRAY);
        username.setFont(new Font("SansSerif", Font.PLAIN, 13));

        infoPanel.add(name);
        infoPanel.add(username);

        // BUTTON: VIEW PROFILE
        JButton viewProfileBtn = new JButton("View Profile");
        final User clickedUser = user; // final variable to capture current user
        viewProfileBtn.addActionListener(e -> {
            mainFrame.switchPage(new Profile(clickedUser, currentUser, mainFrame));
        });

        // Layout
        card.add(pfpLabel, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(viewProfileBtn, BorderLayout.EAST);

        return card;
    }

    //LOAD PFP SAFELY
    private void loadProfilePicture(String url, JLabel label) {
        new SwingWorker<BufferedImage, Void>() {
            @Override
            protected BufferedImage doInBackground() throws Exception {
                if (url != null && !url.isEmpty()) {   // <- url parameter
                    try {
                        return ImageIO.read(new URL(url));  // use the same parameter name
                    } catch (Exception ignored) {}
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    BufferedImage img = get();
                    if (img != null) {
                        Image scaled = img.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                        label.setIcon(new ImageIcon(scaled));
                    } else {
                        ImageIcon fallback = new ImageIcon("src/com/kaleido/GUI/user.png");
                        Image scaled = fallback.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                        label.setIcon(new ImageIcon(scaled));
                    }
                } catch (Exception ignored) {}
            }
        }.execute();
    }
}