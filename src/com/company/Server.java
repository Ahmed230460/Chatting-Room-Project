package com.company;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.SwingUtilities;

public class Server {
    private ServerSocket serverSocket;
    private List<ClientHandler> clients;
    private Map<String, String> userStatuses;
    private Set<String> onlineUsers;
    private Set<String> offlineUsers;
    private Set<String> busyUsers;
    private Database dbHelper;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port); // listen for incoming client connections , Each new connection is accepted as a Socket object.
            clients = new ArrayList<>();
            userStatuses = new HashMap<>();
            onlineUsers = new HashSet<>();
            offlineUsers = new HashSet<>();
            busyUsers = new HashSet<>();
            dbHelper = new Database();
            loadOfflineUsers(); // Load offline users from the database

            // Show Admin Login Screen
            SwingUtilities.invokeLater(() -> new AdminLoginScreen(this, () -> {
                // On successful login, show Admin Interface
                SwingUtilities.invokeLater(() -> new AdminInterface(this));
            }));

            System.out.println("Server started on port " + port);

            while (true) {
                Socket socket = serverSocket.accept(); //When a new client connects, the server accepts the connection and creates a ClientHandler for it.
                ClientHandler clientHandler = new ClientHandler(socket, this);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadOfflineUsers() {
        Set<String> registeredUsers = dbHelper.getAllClients(); // Ensure this method returns current clients
        offlineUsers.clear();
        offlineUsers.addAll(registeredUsers);
        System.out.println("Loaded offline users: " + offlineUsers);
    }
    public synchronized void broadcastMessage(String message) { // if multiple threads try to broadcast messages at the same time, it could lead to inconsistent or corrupted results.
        System.out.println("Broadcasting message: " + message);
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public Database getDatabaseHelper() {
        return dbHelper;
    }

    public synchronized void updateUserStatus(String username, String status) {
        System.out.println("Updating status for user: " + username + " to " + status);
        if (status.equals("Busy")) {
            busyUsers.add(username);
        } else {
            busyUsers.remove(username);
        }

        if (status.equals("Online")) {
            onlineUsers.add(username);
            offlineUsers.remove(username);
        } else if (status.equals("Offline")) {
            offlineUsers.add(username);
            onlineUsers.remove(username);
        }

        userStatuses.put(username, status);
        broadcastUserList();
    }

    public synchronized void removeUser(String username) {
        System.out.println("Removing user: " + username);
        userStatuses.remove(username);
        onlineUsers.remove(username);
        offlineUsers.add(username);
        busyUsers.remove(username);
        broadcastUserList();
    }

    private synchronized void broadcastUserList() {
        StringBuilder userList = new StringBuilder("USERS_LIST:");
        for (String user : onlineUsers) {
            userList.append(user).append(":").append(userStatuses.get(user)).append(";");
        }
        for (String user : offlineUsers) {
            userList.append(user).append(":Offline;");
        }
        for (String user : busyUsers) {
            userList.append(user).append(":Busy;");
        }
        broadcastMessage(userList.toString());
    }

    public synchronized void handleNewClient(String username) {
        System.out.println("Adding new client: " + username);
        offlineUsers.add(username);
        broadcastUserList();
    }

    public Set<String> getOnlineUsers() {
        return Collections.unmodifiableSet(onlineUsers);
    }

    public Set<String> getOfflineUsers() {
        return Collections.unmodifiableSet(offlineUsers);
    }

    public Set<String> getBusyUsers() {
        return Collections.unmodifiableSet(busyUsers);
    }

    public boolean addAdmin(String username, String password) {
        return dbHelper.adminSignUp(username, password);
    }

    public boolean updateAdmin(String username, String newPassword) {
        return dbHelper.updateAdminPassword(username, newPassword);
    }

    public boolean deleteAdmin(String username) {
        return dbHelper.deleteAdmin(username);
    }
    public synchronized void deleteClient(String username) {
        System.out.println("Deleting client: " + username);
        offlineUsers.remove(username);
        onlineUsers.remove(username);
        busyUsers.remove(username);
        userStatuses.remove(username);
        broadcastUserList();
    }

    public synchronized void addClient(String username) {
        System.out.println("Adding client: " + username);
        offlineUsers.add(username);
        userStatuses.put(username, "Offline");
        broadcastUserList();
    }

    public Set<String> listAllAdmins() {
        return dbHelper.getAllAdmins();
    }

    public static void main(String[] args) {
        new Server(1234);
    }
}
