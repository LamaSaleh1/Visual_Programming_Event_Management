package dao;

import Model.User;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistrationDAO {

    public void register(int userId, int eventId) throws Exception {
        String sql = "INSERT INTO registrations(user_id, event_id) VALUES(?, ?)";
        try (Connection conn = DBConnection.getConnection();  
             PreparedStatement ps = conn.prepareStatement(sql)) {
             ps.setInt(1, userId);      
             ps.setInt(2, eventId);     
             ps.executeUpdate();
        }
    }

    public int countRegistrations(int eventId) throws Exception {
        String sql = "SELECT COUNT(*) AS total FROM registrations WHERE event_id = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, eventId);    
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");  
                }
            }
        }
        return 0;
    }
    
    public List<User> getRegisteredUsersForEvent(int eventId) throws Exception {
        String sql = "SELECT u.* FROM users u " +
                     "JOIN registrations r ON u.user_id = r.user_id " +
                     "WHERE r.event_id = ?";
    
        List<User> users = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
        
            ps.setInt(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                    );
                    users.add(user);
                }
            }
        }
        return users;
    }
    
    public int getTotalRegistrationsCount() throws Exception {
        String sql = "SELECT COUNT(*) AS total FROM registrations";
    
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }
}
