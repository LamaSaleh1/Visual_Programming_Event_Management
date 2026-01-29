package dao;

import database.DBConnection;
import Model.User;
import java.sql.*;
import java.util.*;
import auth.PasswordUtility;

public class UserDAO {
    
    public void addUser(User user) throws Exception {
        String sql = "INSERT INTO users (username, password, password_salt, role) VALUES (?, ?, ?, ?)";
        String salt = PasswordUtility.generateSalt();
        String hashedPassword = PasswordUtility.hashPassword(user.getPassword(), salt);
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, hashedPassword); 
            ps.setString(3, salt);           
            ps.setString(4, user.getRole());
            ps.executeUpdate();
    }
}
    
    public void deleteUser(int userId) throws Exception {
        String sql = "DELETE FROM users WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
        
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }
    
    public void updatePassword(int userId, String newPassword) throws Exception {
        String sql = "UPDATE users SET password = ?, password_salt = ? WHERE user_id = ?";
        String salt = PasswordUtility.generateSalt();
        String hashedPassword = PasswordUtility.hashPassword(newPassword, salt);
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hashedPassword);
            ps.setString(2, salt);
            ps.setInt(3, userId);    
            ps.executeUpdate();
        }
    }
    
    public boolean verifyUserPassword(String username, String password) throws Exception {
        String sql = "SELECT password, password_salt FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    String salt = rs.getString("password_salt");
                    return PasswordUtility.verifyPassword(password, storedHash, salt);
                }
            }
        }
        return false;
    }    
    
    public User getUserByUsername(String username) throws Exception {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        "********", 
                        rs.getString("role")
                    );
                }
            }
        }
        return null;
    }

    public List<User> getAllUsers() throws Exception {
        String sql = "SELECT * FROM users ORDER BY username";
        List<User> users = new ArrayList<>();
    
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
        
            while (rs.next()) {
                User user = new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    "********", 
                    rs.getString("role")
                );
                users.add(user);
            }
        }
        return users;
    }

 public String getUserRole(int userId) throws Exception {
        String sql = "SELECT role FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role");
                }
            }
        }
        return null; 
    }
    
    public int getTotalUsersCount() throws Exception {
        String sql = "SELECT COUNT(*) as count FROM users";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        }
        return 0;
    }
    
    public int getAdminCount() throws Exception {
        String sql = "SELECT COUNT(*) as count FROM users WHERE UPPER(role) = 'ADMIN'";
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        }
        return 0;
    }
    
    public int getUsersCountByRole(String role) throws Exception {
        String sql = "SELECT COUNT(*) as count FROM users WHERE role = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, role);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count");
                }
            }
        }
        return 0;
    }
}