package com.company;

import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private Server server;
    private PrintWriter out;
    private BufferedReader in;
    private String username;
    private boolean isOnline;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.isOnline = false;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//reading text data sent by the client.
            out = new PrintWriter(socket.getOutputStream(), true); // sending text data to the client.
            handleClient();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }

    private void handleClient() throws IOException {
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Received: " + inputLine);

            if (inputLine.startsWith("SIGNUP:")) {
                handleSignUp(inputLine);
            } else if (inputLine.startsWith("LOGIN:")) {
                handleLogin(inputLine);
            } else if (inputLine.startsWith("MESSAGE:")) {
                handleMessage(inputLine);
            } else if (inputLine.startsWith("STATUS_UPDATE:")) {
                handleStatusUpdate(inputLine);
            } else if (inputLine.startsWith("LOGOUT:")) {
                handleLogout();
                break;
            } else if (inputLine.startsWith("NEW_CLIENT:")) {
                handleNewClient(inputLine);
            }
        }
    }

    private void handleSignUp(String inputLine) {
        String[] parts = inputLine.split(":", 3);
        if (parts.length == 3) {
            String username = parts[1];
            String password = parts[2];

            boolean success = server.getDatabaseHelper().signUp(username, password);
            if (success) {
                out.println("SUCCESS");
                server.handleNewClient(username);
            } else {
                out.println("FAILURE");
            }
        }
    }

    private void handleLogin(String inputLine) {
        String[] parts = inputLine.split(":", 2);
        if (parts.length == 2) {
            this.username = parts[1];
            isOnline = true;
            server.updateUserStatus(username, "Online"); // Update status to Online
        }
    }

    private void handleMessage(String inputLine) {
        server.broadcastMessage("MESSAGE:" + inputLine);
    }

    private void handleStatusUpdate(String inputLine) {
        String[] parts = inputLine.split(":", 3);
        if (parts.length == 3) {
            String statusMessage = parts[2];
            server.updateUserStatus(username, statusMessage);
        }
    }

    private void handleLogout() {
        System.out.println("Handling logout for user: " + username); // Debugging line
        if (isOnline) {
            server.removeUser(username);
            isOnline = false;
        }
    }

    private void handleNewClient(String inputLine) {
        String[] parts = inputLine.split(":", 2);
        if (parts.length == 2) {
            String newClientUsername = parts[1];
            server.handleNewClient(newClientUsername);
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    private void cleanup() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (isOnline) {
            server.removeUser(username);
        }
    }
}
