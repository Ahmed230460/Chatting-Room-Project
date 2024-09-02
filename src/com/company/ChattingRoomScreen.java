package com.company;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChattingRoomScreen {
    private JFrame frame;
    private JTextPane chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton saveLogButton;
    private JButton logoutButton;
    private JComboBox<String> statusComboBox;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;

    private StyledDocument doc;
    private Map<String, Color> clientColors;

    public ChattingRoomScreen(String username) {
        this.username = username;
        clientColors = new HashMap<>();

        // Set up the frame
        frame = new JFrame("Chatting Room");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBounds(0, 0, 800, 600);
        mainPanel.setLayout(null);

        // Right Panel (Full Area)
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBounds(0, 0, 800, 600);
        rightPanel.setLayout(null);

        JLabel titleLabel = new JLabel("Welcome " + username);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 102));
        titleLabel.setBounds(30, 20, 500, 30);
        rightPanel.add(titleLabel);

        chatArea = new JTextPane();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chatArea.setBackground(new Color(248, 248, 248));
        doc = chatArea.getStyledDocument();
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBounds(30, 60, 740, 350);
        rightPanel.add(scrollPane);

        messageField = new JTextField();
        messageField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageField.setBounds(30, 420, 590, 40);
        rightPanel.add(messageField);

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sendButton.setBackground(new Color(0, 102, 102));
        sendButton.setForeground(Color.WHITE);
        sendButton.setBounds(630, 420, 93, 36);
        rightPanel.add(sendButton);

        statusComboBox = new JComboBox<>(new String[]{"Available", "Busy"});
        statusComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusComboBox.setBounds(30, 470, 150, 30);
        rightPanel.add(statusComboBox);

        saveLogButton = new JButton("Save Chat Log");
        saveLogButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        saveLogButton.setBackground(new Color(0, 102, 102));
        saveLogButton.setForeground(Color.WHITE);
        saveLogButton.setBounds(200, 470, 150, 30);
        rightPanel.add(saveLogButton);

        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        logoutButton.setBackground(new Color(255, 51, 51));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBounds(30, 510, 100, 30);
        rightPanel.add(logoutButton);

        // Add Right Panel to Main Panel
        mainPanel.add(rightPanel);

        // Add Main Panel to Frame
        frame.add(mainPanel);

        // Connect to the server
        try {
            socket = new Socket("localhost", 1234);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send initial status to server
            out.println("LOGIN:" + username);

            // Update status when the combo box changes
            statusComboBox.addActionListener(e -> {
                String selectedStatus = (String) statusComboBox.getSelectedItem();
                out.println("STATUS_UPDATE:" + username + ":" + selectedStatus);
            });

            // Send message to server when send button is pressed
            sendButton.addActionListener(e -> {
                String message = messageField.getText();
                if (!message.isEmpty()) {
                    String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
                    out.println("MESSAGE:" + username + " (" + timestamp + "): " + message);
                    messageField.setText("");
                }
            });

            // Save chat log to a file
            saveLogButton.addActionListener(e -> saveChatLog());

            // Logout button action
            logoutButton.addActionListener(e -> logout());

            // Start a thread to listen for incoming messages from the server
            new Thread(() -> {
                try {
                    String incomingMessage;
                    while ((incomingMessage = in.readLine()) != null) {
                        if (incomingMessage.startsWith("MESSAGE:")) {
                            appendMessage(incomingMessage.substring("MESSAGE:".length()));
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }).start();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        frame.setVisible(true);
    }

    private void appendMessage(String message) {
        try {
            // Split message into parts
            String[] parts = message.split(" ", 2);
            if (parts.length < 2) return;

            String sender = parts[0];
            String content = parts[1];

            // Replace the sender's username with "You" if it's the current user's message
            String displaySender = sender.equals(username) ? "You" : sender;

            // Define or get existing color for the sender
            Color color = clientColors.computeIfAbsent(sender, k -> new Color((int) (Math.random() * 0x1000000)));

            // Define style for the sender
            Style senderStyle = doc.addStyle(sender + "_sender", null);
            StyleConstants.setForeground(senderStyle, color);
            StyleConstants.setFontSize(senderStyle, 14);
            StyleConstants.setFontFamily(senderStyle, "Segoe UI");
            StyleConstants.setBold(senderStyle, true);

            // Define style for the content
            Style contentStyle = doc.addStyle(sender + "_content", null);
            StyleConstants.setForeground(contentStyle, color);
            StyleConstants.setFontSize(contentStyle, 16); // Larger font size for content
            StyleConstants.setFontFamily(contentStyle, "Segoe UI");
            StyleConstants.setBold(contentStyle, true);

            // Append sender
            doc.insertString(doc.getLength(), displaySender + ": ", senderStyle);

            // Append message content
            doc.insertString(doc.getLength(), content + "\n", contentStyle);

            // Scroll to the end to show the latest message
            chatArea.setCaretPosition(chatArea.getDocument().getLength());

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void saveChatLog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Chat Log");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));

        int userSelection = fileChooser.showSaveDialog(frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            if (!fileToSave.getAbsolutePath().endsWith(".txt")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                writer.write(chatArea.getText());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void logout() {
        try {
            // Send logout message to server
            out.println("LOGOUT:" + username);
            out.flush();

            // Close resources
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }

            // Dispose of the current frame and return to login screen
            SwingUtilities.invokeLater(() -> {
                frame.dispose();
                new LoginScreen(); // Launch login screen
            });

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error during logout: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChattingRoomScreen("User1"));
    }
}
