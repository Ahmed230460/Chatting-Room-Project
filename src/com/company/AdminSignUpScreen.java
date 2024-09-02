

package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminSignUpScreen {
    private Server server;
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton signUpButton;
    private JButton cancelButton;
    private JLabel loginLabel;

    public AdminSignUpScreen(Server server) {
        this.server = server;

        frame = new JFrame("Admin Sign Up");
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

        JLabel titleLabel = new JLabel("CREATE ADMIN ACCOUNT");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 102, 102));
        titleLabel.setBounds(20, 50, 350, 50);
        rightPanel.add(titleLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameLabel.setBounds(30, 130, 100, 20);
        rightPanel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBounds(30, 150, 340, 40);
        rightPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordLabel.setBounds(30, 200, 100, 20);
        rightPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBounds(30, 220, 340, 40);
        rightPanel.add(passwordField);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        confirmPasswordLabel.setBounds(30, 270, 150, 20);
        rightPanel.add(confirmPasswordLabel);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        confirmPasswordField.setBounds(30, 290, 340, 40);
        rightPanel.add(confirmPasswordField);

        signUpButton = new JButton("Sign Up");
        signUpButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        signUpButton.setBackground(new Color(0, 102, 102));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setBounds(30, 350, 120, 36);
        rightPanel.add(signUpButton);

        JLabel infoLabel = new JLabel("Already have an account?");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoLabel.setBounds(30, 400, 200, 20);
        rightPanel.add(infoLabel);

        loginLabel = new JLabel("Login");
        loginLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loginLabel.setForeground(new Color(255, 51, 51));
        loginLabel.setBounds(190, 395, 90, 30);
        rightPanel.add(loginLabel);

        // Add Panels to Main Panel
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        // Add Main Panel to Frame
        frame.add(mainPanel);

        // Add mouse listener for hover effects
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginLabel.setText("<html><u>Login</u></html>"); // Underline effect
                loginLabel.setForeground(Color.BLUE); // Hover color
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginLabel.setText("Login"); // Original text
                loginLabel.setForeground(new Color(255, 51, 51)); // Original color
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose(); // Close the sign-up screen
                new AdminLoginScreen(server, () -> {});
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (password.equals(confirmPassword)) {
                    if (server.getDatabaseHelper().adminSignUp(username, password)) {
                        JOptionPane.showMessageDialog(frame, "Sign-up successful!");
                        usernameField.setText("");
                        passwordField.setText("");
                        confirmPasswordField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Sign-up failed! Username might already exist.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Passwords do not match.");
                }
            }
        });


        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminSignUpScreen(new Server(1234)));
    }
}
