package com.kaleido.GUI;

import com.kaleido.models.*;
import javax.swing.*;
import java.awt.*;

public class Feed extends JPanel{
    private User currentUser;
    private MainFrame mainFrame;

    public Feed(User user, MainFrame mainFrame){
        this.currentUser = user;
        this.mainFrame = mainFrame;
        initializeGUI();
    }

    void initializeGUI(){
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);

        //Main Container (this panel itself)

        //Header
        JPanel header = new JPanel();
        header.setBackground(Color.BLACK);
        header.setPreferredSize(new Dimension(0,60));
        header.setBorder(BorderFactory.createEmptyBorder(15,20,15,20));

        //Username Display
        JLabel userDisplay = new JLabel();

        //Main Content
        JPanel mainContent = new JPanel();

        //ASSEMBLE EVERYTHING
        this.add(header, BorderLayout.NORTH);
        this.add(mainContent, BorderLayout.CENTER);
    }
}
