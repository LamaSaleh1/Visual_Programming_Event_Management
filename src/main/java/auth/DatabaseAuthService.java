package auth;

import java.sql.*;
import database.DBConnection;
import Model.User;
import dao.UserDAO;

public class DatabaseAuthService {
    private UserDAO userDAO;
    public DatabaseAuthService() {
        this.userDAO = new UserDAO();
    }
    
    public User login(String username, String password) throws Exception {
        String sql = "SELECT * FROM users WHERE username = ?";
    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
        
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    String salt = rs.getString("password_salt");
                
                    if (PasswordUtility.verifyPassword(password, storedHash, salt)) {
                        return new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            storedHash, 
                            rs.getString("role")
                        );
                    }
                }
            }
            User user = userDAO.getUserByUsername(username);
            if (user != null && userDAO.verifyUserPassword(username, password)) {
                return user;
            }
        }
        return null;
    }
}
