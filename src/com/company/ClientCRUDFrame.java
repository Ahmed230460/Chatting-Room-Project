package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;

public class ClientCRUDFrame extends JFrame {
        private Runnable backAction;
        private Server server;

        public ClientCRUDFrame(Server server, Runnable backAction) {
                this.server = server;
                this.backAction = backAction;

                setTitle("Manage Clients");
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
                logoSpacerPanel.setPreferredSize(new Dimension(550, 30)); // Space between logo and buttons
                logoSpacerPanel.setBackground(new Color(0, 102, 102));
                mainPanel.add(logoSpacerPanel, BorderLayout.CENTER);

                // Create panel for buttons
                JPanel buttonPanel = new JPanel();
                buttonPanel.setBackground(new Color(0, 102, 102));
                buttonPanel.setLayout(new GridLayout(5, 1, 10, 10));  // Grid layout for buttons

                // CRUD Buttons for Clients
                JButton addClientButton = createStyledButton("Add Client");
                addClientButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                JButton editClientButton = createStyledButton("Edit Client");
                editClientButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                JButton deleteClientButton = createStyledButton("Delete Client");
                deleteClientButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                JButton viewClientsButton = createStyledButton("View Clients");
                viewClientsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

                // "Back" button
                JButton backButton = createStyledButton("Back");
                backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                backButton.setPreferredSize(new Dimension(100, 40));
                backButton.addActionListener(e -> {
                        dispose();
                        backAction.run();
                });

                // Add action listeners for CRUD operations
                addClientButton.addActionListener(e -> handleAddClient());
                editClientButton.addActionListener(e -> handleEditClient());
                deleteClientButton.addActionListener(e -> handleDeleteClient());
                viewClientsButton.addActionListener(e -> handleViewClients());

                // Add mouse listeners for hover effects
                addHoverEffect(addClientButton);
                addHoverEffect(editClientButton);
                addHoverEffect(deleteClientButton);
                addHoverEffect(viewClientsButton);
                addHoverEffect(backButton);

                // Add buttons to button panel
                buttonPanel.add(addClientButton);
                buttonPanel.add(editClientButton);
                buttonPanel.add(deleteClientButton);
                buttonPanel.add(viewClientsButton);

                // Add button panel to the main panel
                mainPanel.add(buttonPanel, BorderLayout.CENTER);
                mainPanel.add(backButton, BorderLayout.SOUTH);

                // Add main panel to frame
                add(mainPanel);
                setVisible(true);
        }
        private void addHoverEffect(JButton button) {
                button.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                                button.setBackground(new Color(147, 120, 120)); // Light gray background on hover
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                                button.setBackground(Color.WHITE); // Revert to original background
                        }
                });
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

        private void handleAddClient() {
                JTextField usernameField = new JTextField(20);
                JPasswordField passwordField = new JPasswordField(20);
                JPanel inputPanel = new JPanel(new GridLayout(2, 2));
                inputPanel.add(new JLabel("Username:"));
                inputPanel.add(usernameField);
                inputPanel.add(new JLabel("Password:"));
                inputPanel.add(passwordField);

                int result = JOptionPane.showConfirmDialog(null, inputPanel, "Add Client", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                        String username = usernameField.getText();
                        String password = new String(passwordField.getPassword());
                        boolean success = server.getDatabaseHelper().clientSignUp(username, password);
                        if (success) {
                                JOptionPane.showMessageDialog(null, "Client added successfully!");
                                server.addClient(username);
                                if (backAction instanceof AdminInterface) {
                                        ((AdminInterface) backAction).updateClientLists();
                                }
                        } else {
                                JOptionPane.showMessageDialog(null, "Failed to add client.");
                        }
                }
        }

        private void handleEditClient() {
                JTextField usernameField = new JTextField(20);
                JTextField newPasswordField = new JTextField(20);
                JPanel inputPanel = new JPanel(new GridLayout(2, 2));
                inputPanel.add(new JLabel("Username:"));
                inputPanel.add(usernameField);
                inputPanel.add(new JLabel("New Password:"));
                inputPanel.add(newPasswordField);

                int result = JOptionPane.showConfirmDialog(null, inputPanel, "Update Client", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                        String username = usernameField.getText();
                        String newPassword = newPasswordField.getText();
                        boolean success = server.getDatabaseHelper().updateClientPassword(username, newPassword);
                        if (success) {
                                JOptionPane.showMessageDialog(null, "Client updated successfully!");
                        } else {
                                JOptionPane.showMessageDialog(null, "Failed to update client.");
                        }
                }
        }

        private void handleDeleteClient() {
                JTextField usernameField = new JTextField(20);
                JPanel inputPanel = new JPanel(new GridLayout(1, 2));
                inputPanel.add(new JLabel("Username:"));
                inputPanel.add(usernameField);

                int result = JOptionPane.showConfirmDialog(null, inputPanel, "Delete Client", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                        String username = usernameField.getText();
                        boolean success = server.getDatabaseHelper().deleteClient(username);
                        if (success) {
                                JOptionPane.showMessageDialog(null, "Client deleted successfully!");
                                server.deleteClient(username);
                                if (backAction instanceof AdminInterface) {
                                        ((AdminInterface) backAction).updateClientLists();
                                }
                        } else {
                                JOptionPane.showMessageDialog(null, "Failed to delete client.");
                        }
                }
        }

        private void handleViewClients() {
                Set<String> clients = server.getDatabaseHelper().getAllClients();
                JTextArea textArea = new JTextArea(10, 30);
                textArea.setEditable(false);
                textArea.setText("Clients:\n");
                for (String client : clients) {
                        textArea.append(client + "\n");
                }
                JScrollPane scrollPane = new JScrollPane(textArea);
                JOptionPane.showMessageDialog(null, scrollPane, "View Clients", JOptionPane.INFORMATION_MESSAGE);
        }
}
