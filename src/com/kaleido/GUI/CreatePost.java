package com.kaleido.GUI;

import com.kaleido.GUI.components.LeftSidebarPanel;
import com.kaleido.models.User;
import com.kaleido.models.Post;
import com.kaleido.db.PostDAO;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Map;

public class CreatePost extends JFrame {

    private JTextArea contentText;
    private JLabel imagePreview;
    private File selectedImageFile;
    private LeftSidebarPanel parentProfile;
    private Cloudinary cloudinary;

    public CreatePost(User currentUser, LeftSidebarPanel parentProfile) {
        this.parentProfile = parentProfile;

        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "defyrn0le",
                "api_key", "731594393816882",
                "api_secret", "bqLiwDbBQmEaYQKqscMUmP_SkGU"
        ));

        initializeGUI(currentUser);
    }

    private void initializeGUI(User currentUser) {
        setTitle("Kaleido - Create Post");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(21, 21, 23));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel title = new JLabel("Create New Post");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Content text area
        contentText = new JTextArea(6, 30);
        contentText.setLineWrap(true);
        contentText.setWrapStyleWord(true);
        contentText.setBackground(new Color(30, 30, 32));
        contentText.setForeground(Color.WHITE);
        contentText.setCaretColor(Color.WHITE);
        contentText.setFont(new Font("SansSerif", Font.PLAIN, 14));
        contentText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(contentText);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        scrollPane.getViewport().setBackground(new Color(30, 30, 32));
        scrollPane.setPreferredSize(new Dimension(450, 150));
        mainPanel.add(scrollPane);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Image preview
        JPanel imageContainer = new JPanel(new BorderLayout());
        imageContainer.setBackground(Color.WHITE);
        imageContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Image Preview"));
        imageContainer.setPreferredSize(new Dimension(450, 250));

        imagePreview = new JLabel("No image selected", SwingConstants.CENTER);
        imagePreview.setForeground(Color.GRAY);
        imagePreview.setBackground(new Color(30, 30, 32));
        imagePreview.setOpaque(true);
        imagePreview.setPreferredSize(new Dimension(450, 250));
        imageContainer.add(imagePreview, BorderLayout.CENTER);
        mainPanel.add(imageContainer);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Buttons panel
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(21, 21, 23));
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));

        JButton addImageBtn = new JButton("Add Image");
        styleBlueButton(addImageBtn);
        addImageBtn.addActionListener(e -> loadImageFromUser());

        JButton removeImageBtn = new JButton("Remove Image");
        styleRedButton(removeImageBtn);
        removeImageBtn.addActionListener(e -> removeImage());

        JButton postBtn = new JButton("Post");
        styleBlueButton(postBtn);
        postBtn.addActionListener(e -> submitPost(currentUser));

        btnPanel.add(addImageBtn);
        btnPanel.add(removeImageBtn);
        btnPanel.add(postBtn);
        mainPanel.add(btnPanel);

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void loadImageFromUser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Image files", "jpg", "jpeg", "png"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                selectedImageFile = fileChooser.getSelectedFile();

                // Check file size (max 5MB)
                if (selectedImageFile.length() > 5 * 1024 * 1024) {
                    JOptionPane.showMessageDialog(this,
                            "Image too large! Please select an image under 5MB.",
                            "File Too Large",
                            JOptionPane.WARNING_MESSAGE);
                    selectedImageFile = null;
                    return;
                }

                BufferedImage selectedImage = ImageIO.read(selectedImageFile);
                if (selectedImage != null) {
                    Image scaledImage = scaleImageToFit(selectedImage, 430, 230);
                    imagePreview.setIcon(new ImageIcon(scaledImage));
                    imagePreview.setText(null);
                }
            } catch (Exception ex) {
                showError("Error loading image: " + ex.getMessage());
            }
        }
    }

    private void removeImage() {
        selectedImageFile = null;
        imagePreview.setIcon(null);
        imagePreview.setText("No image selected");
    }

    private Image scaleImageToFit(BufferedImage originalImage, int maxWidth, int maxHeight) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        double widthScale = (double) maxWidth / originalWidth;
        double heightScale = (double) maxHeight / originalHeight;
        double scale = Math.min(widthScale, heightScale);

        int scaledWidth = (int) (originalWidth * scale);
        int scaledHeight = (int) (originalHeight * scale);

        return originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
    }

    // OPTIMIZED SUBMIT POST METHOD
    private void submitPost(User currentUser) {
        String text = contentText.getText().trim();

        if (text.isEmpty() && selectedImageFile == null) {
            showWarning("Please write something or add an image to post.");
            return;
        }

        // DISABLE BUTTON TO PREVENT MULTIPLE CLICKS
        Component[] components = getContentPane().getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                disableButtons((JPanel) comp);
            }
        }

        // TEXT-ONLY POST: Fast path (no Cloudinary)
        if (selectedImageFile == null) {
            createTextPost(currentUser, text);
            return;
        }

        // IMAGE POST: Use Cloudinary with progress
        createImagePost(currentUser, text);
    }

    // FAST PATH: Text-only posts
    private void createTextPost(User currentUser, String text) {
        try {
            Post newPost = new Post();
            newPost.setUserId(currentUser.getUserID());
            newPost.setUsername(currentUser.getUsername());
            newPost.setContentText(text);
            newPost.setImageUrl(null); // No image

            PostDAO postDAO = new PostDAO();
            boolean success = postDAO.createPost(newPost);

            if (success) {
                showSuccess("Post created successfully!");
                refreshParentProfile();
                dispose();
            } else {
                showError("Failed to create post. Please try again.");
                enableButtons();
            }
        } catch (Exception ex) {
            showError("Error creating post: " + ex.getMessage());
            enableButtons();
            ex.printStackTrace();
        }
    }

    // SLOW PATH: Image posts with Cloudinary
    private void createImagePost(User currentUser, String text) {
        JDialog loadingDialog = createLoadingDialog();
        loadingDialog.setVisible(true);

        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                try {
                    System.out.println("Starting Cloudinary upload...");
                    long startTime = System.currentTimeMillis();

                    // Upload to Cloudinary
                    Map uploadResult = cloudinary.uploader().upload(selectedImageFile, ObjectUtils.emptyMap());
                    String cloudinaryUrl = (String) uploadResult.get("secure_url");

                    long endTime = System.currentTimeMillis();
                    System.out.println("Cloudinary upload completed in " + (endTime - startTime) + "ms");
                    System.out.println("Cloudinary URL: " + cloudinaryUrl);

                    return cloudinaryUrl;
                } catch (Exception e) {
                    System.err.println("Cloudinary upload failed: " + e.getMessage());
                    throw e;
                }
            }

            @Override
            protected void done() {
                loadingDialog.dispose();

                try {
                    String cloudinaryUrl = get();

                    // Save post with Cloudinary URL
                    Post newPost = new Post();
                    newPost.setUserId(currentUser.getUserID());
                    newPost.setUsername(currentUser.getUsername());
                    newPost.setContentText(text);
                    newPost.setImageUrl(cloudinaryUrl);

                    PostDAO postDAO = new PostDAO();
                    boolean success = postDAO.createPost(newPost);

                    if (success) {
                        showSuccess("Post with image created successfully!");
                        refreshParentProfile();
                        dispose();
                    } else {
                        showError("Failed to create post. Please try again.");
                        enableButtons();
                    }
                } catch (Exception e) {
                    showError("Failed to upload image: " + e.getMessage());
                    enableButtons();
                    e.printStackTrace();
                }
            }
        };

        worker.execute();
    }

    private void refreshParentProfile() {
        if (parentProfile != null) {
            // Use invokeLater to ensure UI thread safety
            SwingUtilities.invokeLater(() -> {
                parentProfile.refreshPosts();
            });
        }
    }

    private void disableButtons(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton) {
                comp.setEnabled(false);
            } else if (comp instanceof JPanel) {
                disableButtons((JPanel) comp);
            }
        }
    }

    private void enableButtons() {
        // Re-enable all buttons in the frame
        Component[] components = getContentPane().getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                enableButtonsInPanel((JPanel) comp);
            }
        }
    }

    private void enableButtonsInPanel(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton) {
                comp.setEnabled(true);
            } else if (comp instanceof JPanel) {
                enableButtonsInPanel((JPanel) comp);
            }
        }
    }

    private JDialog createLoadingDialog() {
        JDialog loadingDialog = new JDialog(this, "Uploading Image", true);
        loadingDialog.setSize(300, 100);
        loadingDialog.setLocationRelativeTo(this);
        loadingDialog.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(21, 21, 23));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("Uploading to Cloudinary...", SwingConstants.CENTER);
        label.setForeground(Color.WHITE);

        contentPanel.add(label, BorderLayout.CENTER);
        loadingDialog.add(contentPanel);

        return loadingDialog;
    }

    // Helper methods for dialogs
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // Button styling methods (same as before)
    private void styleBlueButton(JButton btn) {
        btn.setBackground(new Color(0, 122, 255));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0, 100, 220));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0, 122, 255));
            }
        });
    }

    private void styleRedButton(JButton btn) {
        btn.setBackground(new Color(220, 53, 69));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(200, 35, 51));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(220, 53, 69));
            }
        });
    }
}