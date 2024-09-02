package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;

public class AdminCRUDFrame extends JFrame {
    private Runnable backAction;
    private Server server;

    public AdminCRUDFrame(Server server, Runnable backAction) {
        this.server = server;
        this.backAction = backAction;

        setTitle("Manage Admins");
        setSize(500, 500); // Adjusted size for better layout
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(0, 102, 102));

        // Create logo panel
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(0, 102, 102));

        JLabel logoLabel = new JLabel(new ImageIcon("C:\\Users\\AHMED DAWOD\\Downloads\\logo.png"));
        logoLabel.setPreferredSize(new Dimension(250, 150));
        logoPanel.add(logoLabel);
        mainPanel.add(logoPanel, BorderLayout.NORTH);

        // Create a panel to add spacing below the logo
        JPanel logoSpacerPanel = new JPanel();
        logoSpacerPanel.setPreferredSize(new Dimension(500, 30)); // Space between logo and buttons
        logoSpacerPanel.setBackground(new Color(0, 102, 102));
        mainPanel.add(logoSpacerPanel, BorderLayout.CENTER);

        // Create panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0, 102, 102));
        buttonPanel.setLayout(new GridLayout(5, 1, 10, 10));  // Grid layout for buttons

        // CRUD Buttons for Admins
        JButton addAdminButton = createStyledButton("Add Admin");
        addAdminButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JButton editAdminButton = createStyledButton("Edit Admin");
        editAdminButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JButton deleteAdminButton = createStyledButton("Delete Admin");
        deleteAdminButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JButton viewAdminsButton = createStyledButton("View Admins");
        viewAdminsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // "Back" button
        JButton backButton = createStyledButton("Back");
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.addActionListener(e -> {
            dispose();
            backAction.run();
        });

        // Add mouse listeners for hover effects
        addHoverEffect(addAdminButton);
        addHoverEffect(editAdminButton);
        addHoverEffect(deleteAdminButton);
        addHoverEffect(viewAdminsButton);
        addHoverEffect(backButton);

        // Add action listeners for CRUD operations
        addAdminButton.addActionListener(e -> handleAddAdmin());
        editAdminButton.addActionListener(e -> handleEditAdmin());
        deleteAdminButton.addActionListener(e -> handleDeleteAdmin());
        viewAdminsButton.addActionListener(e -> handleViewAdmins());

        // Add buttons to button panel
        buttonPanel.add(addAdminButton);
        buttonPanel.add(editAdminButton);
        buttonPanel.add(deleteAdminButton);
        buttonPanel.add(viewAdminsButton);

        // Panel to hold buttons and back button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(0, 102, 102));
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(backButton, BorderLayout.SOUTH);

        // Add bottom panel to main panel
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(0, 102, 102));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setPreferredSize(new Dimension(150, 40));
        return button;
    }

    private void addHoverEffect(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(114, 96, 96)); // Light gray background on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE); // Revert to original background
            }
        });
    }

    private void handleAddAdmin() {
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(usernameField);
        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(null, inputPanel, "Add Admin", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            boolean success = server.getDatabaseHelper().adminSignUp(username, password);
            if (success) {
                JOptionPane.showMessageDialog(null, "Admin added successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add admin.");
            }
        }
    }

    private void handleEditAdmin() {
        JTextField usernameField = new JTextField(20);
        JTextField newPasswordField = new JTextField(20);
        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(usernameField);
        inputPanel.add(new JLabel("New Password:"));
        inputPanel.add(newPasswordField);

        int result = JOptionPane.showConfirmDialog(null, inputPanel, "Update Admin", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String newPassword = newPasswordField.getText();
            boolean success = server.getDatabaseHelper().updateAdminPassword(username, newPassword);
            if (success) {
                JOptionPane.showMessageDialog(null, "Admin updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update admin.");
            }
        }
    }

    private void handleDeleteAdmin() {
        JTextField usernameField = new JTextField(20);
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Delete Admin", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            boolean success = server.getDatabaseHelper().deleteAdmin(username);
            if (success) {
                JOptionPane.showMessageDialog(null, "Admin deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete admin.");
            }
        }
    }

    private void handleViewAdmins() {
        Set<String> admins = server.getDatabaseHelper().getAllAdmins();
        JTextArea textArea = new JTextArea(10, 30);
        textArea.setEditable(false);
        textArea.setText("Admins:\n");
        for (String admin : admins) {
            textArea.append(admin + "\n");
        }
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(null, scrollPane, "View Admins", JOptionPane.INFORMATION_MESSAGE);
    }
}
