package com.company;

import com.company.AdminLoginScreen;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;

public class AdminInterface {
    private JFrame frame;
    private JTextArea offlineClientsArea;
    private JTextArea onlineClientsArea;
    private JTextArea busyClientsArea;
    private Server server;

    public AdminInterface(Server server) {
        this.server = server;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Admin Interface");
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(20, 20));
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(0, 102, 102)); // Background color

        // Main Panel to hold all content
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(0, 102, 102));
        mainPanel.setLayout(new BorderLayout(20, 20));

        // Panel for Client Lists
        JPanel clientPanel = new JPanel();
        clientPanel.setLayout(new GridLayout(1, 3, 10, 10)); // Arrange text areas side-by-side
        clientPanel.setOpaque(false); // Ensure background color of mainPanel shows through

        offlineClientsArea = createClientTextArea("Offline Clients");
        onlineClientsArea = createClientTextArea("Online Clients");
        busyClientsArea = createClientTextArea("Busy Clients");

        JScrollPane offlineScrollPane = new JScrollPane(offlineClientsArea);
        JScrollPane onlineScrollPane = new JScrollPane(onlineClientsArea);
        JScrollPane busyScrollPane = new JScrollPane(busyClientsArea);

        clientPanel.add(offlineScrollPane);
        clientPanel.add(onlineScrollPane);
        clientPanel.add(busyScrollPane);

        mainPanel.add(clientPanel, BorderLayout.CENTER);

        // Panel for Manage Buttons, Refresh and Logout buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridBagLayout()); // Layout for bottom panel
        bottomPanel.setOpaque(false); // Ensure background color of mainPanel shows through

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding around components

        // Manage Admins Button
        JButton manageAdminsButton = createStyledButton("Manage Admins", new Color(0, 153, 153), e -> {

            frame.dispose(); // Close AdminInterface
            AdminCRUDFrame adminCRUDFrame = new AdminCRUDFrame(server, this::reopenAdminInterface);
            adminCRUDFrame.setVisible(true);
        });

        // Manage Clients Button
        JButton manageClientsButton = createStyledButton("Manage Clients", new Color(0, 153, 153), e -> {
            frame.dispose(); // Close AdminInterface
            ClientCRUDFrame clientCRUDFrame = new ClientCRUDFrame(server, this::reopenAdminInterface);
            clientCRUDFrame.setVisible(true);
        });

        // Refresh Button
        JButton refreshButton = createStyledButton("Refresh", new Color(255, 255, 255), e -> updateClientLists());
        refreshButton.setForeground(Color.BLACK);
        // Logout Button
        JButton logoutButton = createStyledButton("Logout", new Color(117, 28, 28), e -> logout());
        logoutButton.setForeground(Color.WHITE);

        // Add Manage Admins Button to the bottom panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        bottomPanel.add(manageAdminsButton, gbc);

        // Add Manage Clients Button to the bottom panel, below Manage Admins Button
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        bottomPanel.add(manageClientsButton, gbc);

        // Add Refresh Button to the bottom panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = .1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        bottomPanel.add(refreshButton, gbc);

        // Add Logout Button to the bottom panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        bottomPanel.add(logoutButton, gbc);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        frame.add(mainPanel);
        frame.setVisible(true);

        // Initialize the client lists
        updateClientLists();
    }






    private JTextArea createClientTextArea(String title) {
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setBackground(Color.WHITE);
        textArea.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16),
                Color.DARK_GRAY
        ));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        return textArea;
    }

    private JButton createStyledButton(String text, Color backgroundColor, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setPreferredSize(new Dimension(150, 40)); // Same size for all buttons
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 215), 2));

        button.addActionListener(actionListener);
        return button;
    }

    public void updateClientLists() {
        Set<String> offlineUsers = server.getOfflineUsers();
        Set<String> onlineUsers = server.getOnlineUsers();
        Set<String> busyUsers = server.getBusyUsers();

        // Update text areas with new data
        offlineClientsArea.setText(formatClientList(offlineUsers));
        onlineClientsArea.setText(formatClientList(onlineUsers));
        busyClientsArea.setText(formatClientList(busyUsers));
    }

    private String formatClientList(Set<String> clients) {
        StringBuilder sb = new StringBuilder();
        for (String client : clients) {
            sb.append(client).append("\n");
            sb.append("------------------------------\n"); // Line between clients
        }
        return sb.toString();
    }

    private void logout() {
        // Dispose of the current frame and return to login screen
        SwingUtilities.invokeLater(() -> {
            frame.dispose();
            new AdminLoginScreen(server, () -> {
                // On successful login, show Admin Interface
                SwingUtilities.invokeLater(() -> new AdminInterface(server));
            });
        });
    }

    private void reopenAdminInterface() {
        new AdminInterface(server);
    }
}
