package com.kaleido.GUI;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.kaleido.db.UserDAO;
import com.kaleido.models.User;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Map;


public class EditProfileFrame extends JFrame {

    private User currentUser;
    private Profile parentProfile;
    private JTextField firstNameField, lastNameField, phoneField;
    private JTextArea bioField;
    private JLabel pfpPreview;
    private String selectedImagePath = null;

    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "defyrn0le",
            "api_key", "731594393816882",
            "api_secret", "bqLiwDbBQmEaYQKqscMUmP_SkGU"
    ));

    public EditProfileFrame(User user, Profile parentProfile) {
        this.currentUser = user;
        this.parentProfile = parentProfile;

        setTitle("Edit Profile");
        setSize(400, 600);
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().setBackground(new Color(21,21,23));

        // FIRST NAME
        addLabel("First Name:");
        firstNameField = createTextField(user.getFirstName());
        add(firstNameField);

        // LAST NAME
        addLabel("Last Name:");
        lastNameField = createTextField(user.getLastName());
        add(lastNameField);

        // PHONE NUMBER
        addLabel("Phone Number:");
        phoneField = createTextField(user.getPhoneNumber());
        add(phoneField);

        // BIO
        addLabel("Bio:");
        bioField = new JTextArea(user.getBio());
        bioField.setLineWrap(true);
        bioField.setWrapStyleWord(true);
        bioField.setForeground(Color.WHITE);
        bioField.setBackground(new Color(30,30,30));
        bioField.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(bioField);

        // PROFILE PICTURE PREVIEW
        addLabel("Profile Picture:");
        pfpPreview = new JLabel();
        pfpPreview.setHorizontalAlignment(SwingConstants.LEFT);
        pfpPreview.setPreferredSize(new Dimension(140, 140));
        pfpPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        if (user.getProfilePicUrl() != null) {
            pfpPreview.setIcon(new ImageIcon(
                    new ImageIcon(user.getProfilePicUrl())
                            .getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH)
            ));
        }
        add(pfpPreview);


        // UPLOAD BUTTON
        JButton uploadBtn = new JButton("Upload Picture");
        uploadBtn.addActionListener(e -> selectImage());

        add(uploadBtn);

        // SAVE BUTTON
        JButton saveBtn = new JButton("Save Changes");
        saveBtn.addActionListener(e -> saveChanges());
        add(saveBtn);
    }

    // HELPER METHODS
    private void addLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.RIGHT_ALIGNMENT);
        add(label);
    }

    private JTextField createTextField(String value) {
        JTextField field = new JTextField(value);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setBackground(new Color(30,30,30));
        field.setForeground(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        return field;
    }

    private void selectImage() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();

            // Save local file path for later Cloud upload
            selectedImagePath = selectedFile.getAbsolutePath();

            // Local preview (scaled to 180×180 only for GUI)
            ImageIcon localIcon = new ImageIcon(selectedImagePath);
            Image scaled = localIcon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
            pfpPreview.setIcon(new ImageIcon(scaled));
        }
    }

    private void saveChanges() {
        currentUser.setFirstName(firstNameField.getText());
        currentUser.setLastName(lastNameField.getText());
        currentUser.setPhoneNumber(phoneField.getText());
        currentUser.setBio(bioField.getText());

        // Upload to Cloudinary ONLY if new image selected
        if (selectedImagePath != null && !selectedImagePath.startsWith("http")) {
            try {
                File file = new File(selectedImagePath);

                // Upload original image (no crop, no resize)
                Map result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());

                String url = (String) result.get("secure_url");
                currentUser.setProfilePicUrl(url);

                System.out.println("Generated Cloudinary URL: " + url);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Image upload failed!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Save everything to DB
        UserDAO userDAO = new UserDAO();
        boolean updated = userDAO.updateUser(currentUser);

        if (updated) {
            JOptionPane.showMessageDialog(this, "Profile updated successfully! Open profile again to refresh.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update profile!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}