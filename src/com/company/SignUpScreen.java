package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class SignUpScreen {
    private Socket socket;
    private PrintWriter out;
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField, confirmPasswordField;
    private JButton signUpButton;
    private JLabel loginLabel, infoLabel;

    public SignUpScreen() {
        frame = new JFrame("Sign Up");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        // Left Panel
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(0, 102, 102));
        leftPanel.setBounds(0, 0, 400, 500);
        leftPanel.setLayout(null);

        JLabel logoLabel = new JLabel(new ImageIcon("C:\\Users\\AHMED DAWOD\\Downloads\\logo.png")); // Replace with your path
        logoLabel.setBounds(145, 136, 100, 100);
        leftPanel.add(logoLabel);

        JLabel companyNameLabel = new JLabel("Chatting App");
        companyNameLabel.setFont(new Font("Showcard Gothic", Font.PLAIN, 24));
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

        JLabel titleLabel = new JLabel("SIGN UP");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(new Color(0, 102, 102));
        titleLabel.setBounds(138, 51, 150, 50);
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

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        confirmPasswordLabel.setBounds(30, 270, 150, 20);
        rightPanel.add(confirmPasswordLabel);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        confirmPasswordField.setBounds(30, 290, 343, 40);
        rightPanel.add(confirmPasswordField);
        frame.add(leftPanel);
        frame.add(rightPanel);
        // Sign-Up Button
        signUpButton = new JButton("Sign Up");
        signUpButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        signUpButton.setBackground(new Color(0, 102, 102));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setBounds(30, 350, 100, 36);
        rightPanel.add(signUpButton);

        // "Already have an account?" Label
        infoLabel = new JLabel("Already have an account?");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoLabel.setBounds(30, 420, 200, 20);
        rightPanel.add(infoLabel);

        // "Login" Label
        loginLabel = new JLabel("Login");
        loginLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loginLabel.setForeground(new Color(255, 51, 51));
        loginLabel.setBounds(190, 415, 90, 30);
        rightPanel.add(loginLabel);

        // Add Action Listeners
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (password.equals(confirmPassword)) {
                    // Communicate with the server to handle sign-up
                    boolean success = signUp(username, password);

                    if (success) {
                        JOptionPane.showMessageDialog(frame, "Sign-up successful!");
                        // Optionally notify the server about the new client
                        notifyServerOfNewClient(username);
                        // Clear fields
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

        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose(); // Close the sign-up screen
                new LoginScreen(); // Open the login screen
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                loginLabel.setText("<html><u>Login</u></html>"); // Underline effect
                loginLabel.setForeground(Color.BLUE); // Change color on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginLabel.setText("Login");
                loginLabel.setForeground(new Color(0, 123, 255)); // Reset color when not hovering
            }
        });

        frame.setVisible(true);
    }

    private boolean signUp(String username, String password) {
        try {
            // Send the sign-up request to the server
            Socket socket = new Socket("localhost", 1234); // Connect to the server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send sign-up request
            out.println("SIGNUP:" + username + ":" + password);

            // Read response from server
            String response = in.readLine();
            socket.close();

            return "SUCCESS".equals(response);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error communicating with the server.");
            return false;
        }
    }

    private void notifyServerOfNewClient(String username) {
        try {
            socket = new Socket("localhost", 1234); // Connect to the server
            out = new PrintWriter(socket.getOutputStream(), true);// send text data to the server.
            out.println("NEW_CLIENT:" + username); // Notify the server
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignUpScreen::new);
    }
}
