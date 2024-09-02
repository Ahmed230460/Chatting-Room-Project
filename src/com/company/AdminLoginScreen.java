package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminLoginScreen {
    private Server server;
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel signUpLabel;
    private JButton cancelButton;
    public AdminLoginScreen(Server server, Runnable onLoginSuccess) {
        this.server = server;

        frame = new JFrame("Admin Login");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBounds(0, 0, 800, 500);
        mainPanel.setLayout(null);

        // Left Panel
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(0, 102, 102));
        leftPanel.setBounds(0, 0, 400, 500);
        leftPanel.setLayout(null);

        JLabel logoLabel = new JLabel(new ImageIcon("C:\\Users\\AHMED DAWOD\\Downloads\\logo.png")); // Ensure the path is correct
        logoLabel.setBounds(145, 136, 100, 100);
        leftPanel.add(logoLabel);

        JLabel companyNameLabel = new JLabel("Chatting App");
        companyNameLabel.setFont(new Font("Showcard Gothic", Font.BOLD, 24));
        companyNameLabel.setForeground(Color.WHITE);
        companyNameLabel.setBounds(103, 262, 200, 30);
        leftPanel.add(companyNameLabel);

        JLabel copyrightLabel = new JLabel("copyright © Alamein International University All rights reserved");
        copyrightLabel.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
        copyrightLabel.setForeground(new Color(204, 204, 204));
        copyrightLabel.setBounds(40, 410, 320, 30);
        leftPanel.add(copyrightLabel);

        // Right Panel
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBounds(400, 0, 400, 500);
        rightPanel.setLayout(null);

        JLabel titleLabel = new JLabel("ADMIN LOGIN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(new Color(0, 102, 102));
        titleLabel.setBounds(80, 51, 250, 50);
        rightPanel.add(titleLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameLabel.setBounds(30, 130, 100, 20);
        rightPanel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBounds(30, 150, 343, 40);
        rightPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordLabel.setBounds(30, 200, 100, 20);
        rightPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBounds(30, 220, 343, 40);
        rightPanel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loginButton.setBackground(new Color(0, 102, 102));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBounds(30, 290, 93, 36);
        rightPanel.add(loginButton);


        JLabel infoLabel = new JLabel("Don't have an account?");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoLabel.setBounds(30, 405, 200, 20);
        rightPanel.add(infoLabel);

        signUpLabel = new JLabel("Sign Up");
        signUpLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        signUpLabel.setForeground(new Color(255, 51, 51));
        signUpLabel.setBounds(180, 400, 90, 30);
        rightPanel.add(signUpLabel);

        // Add Panels to Main Panel
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        // Add Main Panel to Frame
        frame.add(mainPanel);


        // Add mouse listener for hover effects
        signUpLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                signUpLabel.setText("<html><u>Sign Up</u></html>"); // Underline effect
                signUpLabel.setForeground(Color.BLUE); // Hover color
            }

            @Override
            public void mouseExited(MouseEvent e) {
                signUpLabel.setText("Sign Up"); // Original text
                signUpLabel.setForeground(new Color(0, 123, 255)); // Original color
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                new AdminSignUpScreen(server);
            }
        });



        frame.add(mainPanel, BorderLayout.CENTER);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (server.getDatabaseHelper().adminLogin(username, password)) {
                    JOptionPane.showMessageDialog(frame, "Login successful!");
                    frame.dispose();
                    new AdminInterface(server);
                } else {
                    JOptionPane.showMessageDialog(frame, "Login failed! Check your username and password.");
                }
            }
        });


        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminLoginScreen(null, () -> {}));
    }
}
