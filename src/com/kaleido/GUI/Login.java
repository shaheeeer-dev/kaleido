package com.kaleido.GUI;

import javax.swing.*;
import java.awt.*;

public class Login {
    public static void main(String[] args) {
        JFrame frame = new JFrame();

        frame.setTitle("Kaleido | Login");
//        frame.setResizable(false);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(25, 25, 25));

        JLabel label = new JLabel();
        label.setText("Log in to Kaleido");
        label.setVerticalTextPosition(JLabel.CENTER);
        label.setHorizontalTextPosition(JLabel.RIGHT);
        label.setForeground(new Color(0xFFFFFF));
        label.setFont(new Font("SF Pro Display", Font.BOLD,20));


        frame.add(label);
        frame.setVisible(true);
    }
}