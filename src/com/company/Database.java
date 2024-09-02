package com.company;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class Database {
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:C:/Users/AHMED DAWOD/Downloads/sqlite-tools-win-x64-3460000/chatting_room.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public boolean signUp(String username, String password) {
        String sql = "INSERT INTO users(username, password) VALUES(?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Sign-up failed: " + e.getMessage());
            return false;
        }
    }

    public boolean login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); //if any matching records were found return true
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Set<String> getRegisteredUsers() {
        Set<String> users = new HashSet<>();
        String sql = "SELECT username FROM users";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    // Add this method for admin sign-up
    public boolean adminSignUp(String username, String password) {
        String sql = "INSERT INTO admins(username, password) VALUES(?, ?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Admin sign-up failed: " + e.getMessage());
            return false;
        }
    }

    // Add this method for admin login
    public boolean adminLogin(String username, String password) {
        String sql = "SELECT * FROM admins WHERE username = ? AND password = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean updateAdminPassword(String username, String newPassword) {
        String sql = "UPDATE admins SET password = ? WHERE username = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // return true if one or more row updated after update database
        } catch (SQLException e) {
            System.out.println("Update admin failed: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteAdmin(String username) {
        String sql = "DELETE FROM admins WHERE username = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Delete admin failed: " + e.getMessage());
            return false;
        }
    }

    public Set<String> getAllAdmins() {
        Set<String> admins = new HashSet<>(); // better complexity and Automatically ensures that all elements are unique.
        String sql = "SELECT username FROM admins";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();// simple query not contain a parameter
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                admins.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return admins;
    }

    // Add this method for client sign-up
    public boolean clientSignUp(String username, String password) {
        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Client sign-up failed: " + e.getMessage());
            return false;
        }
    }

    // Add this method for client login
    public boolean clientLogin(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean updateClientPassword(String username, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Update client failed: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteClient(String username) {
        String sql = "DELETE FROM users WHERE username = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Delete client failed: " + e.getMessage());
            return false;
        }
    }

    public Set<String> getAllClients() {
        Set<String> clients = new HashSet<>();
        String sql = "SELECT username FROM users";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clients.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return clients;
    }
}
