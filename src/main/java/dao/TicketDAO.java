package dao;

import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import service.TicketService;

public class TicketDAO {
    
    public void createTicket(int userId, int eventId) throws Exception {
        String sql = "INSERT INTO tickets (user_id, event_id, ticket_unique_id, ticket_status, qr_code_data) VALUES (?, ?, ?, ?, ?)";
        String ticketId = TicketService.generateTicketId();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, eventId);
            ps.setString(3, ticketId);
            ps.setString(4, "ACTIVE");
            ps.setString(5, "QR_DATA_" + ticketId); 
            ps.executeUpdate();
        }
    }
    
    public List<String> getTicketsForUser(int userId) throws Exception {
        String sql = "SELECT t.ticket_id, t.ticket_unique_id, e.title, e.date, e.location, t.ticket_status " +
                     "FROM tickets t JOIN events e ON t.event_id = e.event_id " +
                     "WHERE t.user_id = ?";
        
        List<String> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String ticketInfo =
                            "Ticket ID: " + rs.getString("ticket_unique_id") +
                            " | Event: " + rs.getString("title") +
                            " | Date: " + rs.getString("date") +
                            " | Location: " + rs.getString("location") +
                            " | Status: " + rs.getString("ticket_status");
                    list.add(ticketInfo);
                }
            }
        }
        return list;
    }

    public boolean hasTicket(int userId, int eventId) throws Exception {
        String sql = "SELECT * FROM tickets WHERE user_id = ? AND event_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); 
            }
        }
    }
    public String getTicketDetails(String ticketUniqueId) throws Exception {
        String sql = "SELECT t.ticket_unique_id, e.title, e.date, e.location, u.username " +
                     "FROM tickets t " +
                     "JOIN events e ON t.event_id = e.event_id " +
                     "JOIN users u ON t.user_id = u.user_id " +
                     "WHERE t.ticket_unique_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ticketUniqueId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return String.format(
                        "Ticket Details:\n" +
                        "ID: %s\n" +
                        "Event: %s\n" +
                        "Date: %s\n" +
                        "Location: %s\n" +
                        "Attendee: %s",
                        rs.getString("ticket_unique_id"),
                        rs.getString("title"),
                        rs.getString("date"),
                        rs.getString("location"),
                        rs.getString("username")
                    );
                }
            }
        }
        return "Ticket not found!";
    }    
}
