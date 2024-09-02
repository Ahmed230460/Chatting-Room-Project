package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginScreen {
    private JFrame frame;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel signUpLabel;  // Changed to JLabel

    public LoginScreen() {
        frame = new JFrame("LOGIN");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);  // Using absolute positioning for custom layout
        frame.setLocationRelativeTo(null);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBounds(0, 0, 800, 500);
        mainPanel.setLayout(null);

        // Right Panel
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(0, 102, 102));
        rightPanel.setBounds(0, 0, 400, 500);
        rightPanel.setLayout(null);

        JLabel logoLabel = new JLabel(new ImageIcon("C:\\Users\\AHMED DAWOD\\Downloads\\logo.png")); // Ensure the path is correct
        logoLabel.setBounds(145, 136, 100, 100);  // Adjust bounds as needed
        rightPanel.add(logoLabel);

        JLabel companyNameLabel = new JLabel("Chatting App");
        companyNameLabel.setFont(new Font("Showcard Gothic", Font.BOLD, 24));
        companyNameLabel.setForeground(Color.WHITE);
        companyNameLabel.setBounds(103, 262, 200, 30);
        rightPanel.add(companyNameLabel);

        JLabel copyrightLabel = new JLabel("copyright © Alamein International University All rights reserved");
        copyrightLabel.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
        copyrightLabel.setForeground(new Color(204, 204, 204));
        copyrightLabel.setBounds(40, 410, 320, 30);
        rightPanel.add(copyrightLabel);

        // Left Panel
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBounds(400, 0, 400, 500);
        leftPanel.setLayout(null);

        JLabel titleLabel = new JLabel("LOGIN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(new Color(0, 102, 102));
        titleLabel.setBounds(138, 51, 150, 50);
        leftPanel.add(titleLabel);

        JLabel emailLabel = new JLabel("Username");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailLabel.setBounds(30, 140, 100, 20);
        leftPanel.add(emailLabel);

        emailField = new JTextField();
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setBounds(30, 160, 343, 40);
        leftPanel.add(emailField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordLabel.setBounds(30, 210, 100, 20);
        leftPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBounds(30, 230, 343, 40);
        leftPanel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loginButton.setBackground(new Color(0, 102, 102));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBounds(30, 290, 93, 36);
        leftPanel.add(loginButton);

        JLabel infoLabel = new JLabel("I don't have an account ?");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoLabel.setBounds(30, 420, 200, 20);
        leftPanel.add(infoLabel);

        // Sign Up Label
        signUpLabel = new JLabel("Sign Up");
        signUpLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        signUpLabel.setForeground(new Color(255, 51, 51));
        signUpLabel.setBounds(190, 415, 90, 30);
        leftPanel.add(signUpLabel);

        // Add Panels to Main Panel
        mainPanel.add(rightPanel);
        mainPanel.add(leftPanel);

        // Add Main Panel to Frame
        frame.add(mainPanel);

        // Add Action Listeners
        loginButton.addActionListener(e -> {
            String username = emailField.getText();
            String password = new String(passwordField.getPassword());

            Database db = new Database();

            if (db.login(username, password)) {
                JOptionPane.showMessageDialog(frame, "Login successful!");
                new ChattingRoomScreen(username);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Login failed! Please check your credentials.");
            }
        });

        signUpLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                signUpLabel.setText("<html><u>Sign Up</u></html>"); // Underline effect
                signUpLabel.setForeground(Color.BLUE); // Hover color
            }

            @Override
            public void mouseExited(MouseEvent e) {
                signUpLabel.setText("Sign Up"); // Original text
                signUpLabel.setForeground(new Color(255, 51, 51)); // Original color
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose(); // Close the login screen
                new SignUpScreen(); // Open the sign-up screen
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginScreen::new);
    }
}
