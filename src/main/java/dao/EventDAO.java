package dao;

import database.DBConnection;
import Model.Event;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {
    
    public void addEvent(Event event) throws Exception {
        String sql = "INSERT INTO events (title, category, date, location, capacity, seats_available) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, event.getTitle());
            ps.setString(2, event.getCategory());
            ps.setString(3, event.getDate());
            ps.setString(4, event.getLocation());
            ps.setInt(5, event.getCapacity());
            ps.setInt(6, event.getSeatsAvailable());
            ps.executeUpdate();
        } 
    }
    
    public void deleteEvent(int eventId) throws Exception {
        String sql = "DELETE FROM events WHERE event_id = ?";
        try (Connection conn = DBConnection.getConnection();  
             PreparedStatement ps = conn.prepareStatement(sql)) {
             ps.setInt(1, eventId);
             ps.executeUpdate();
        }
    }  
    
    public void updateEvent(Event event) throws Exception {
        String sql = "UPDATE events SET title = ?, category = ?, date = ?, location = ?, capacity = ?, seats_available = ? WHERE event_id = ?";
    
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
        
            ps.setString(1, event.getTitle());
            ps.setString(2, event.getCategory());
            ps.setString(3, event.getDate());
            ps.setString(4, event.getLocation());
            ps.setInt(5, event.getCapacity());
            ps.setInt(6, event.getSeatsAvailable());
            ps.setInt(7, event.getEventId());
        
            ps.executeUpdate();
        }
    }
    
   public synchronized boolean updateSeatsAvailable(int eventId, int changeBy) throws Exception {
        String sql = "UPDATE events SET seats_available = seats_available + ? WHERE event_id = ? AND seats_available + ? >= 0";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, changeBy);
            ps.setInt(2, eventId);
            ps.setInt(3, changeBy);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }
   
   public boolean hasAvailableSeats(int eventId) throws Exception {
        String sql = "SELECT seats_available FROM events WHERE event_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("seats_available") > 0;
                }
            }
        }
        return false;
    }
    
    public Event getEventById(int eventId) throws Exception {
        String sql = "SELECT * FROM events WHERE event_id = ?";
    
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
        
            ps.setInt(1, eventId);
        
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Event(
                        rs.getInt("event_id"),
                        rs.getString("title"),
                        rs.getString("category"),
                        rs.getString("date"),
                        rs.getString("location"),
                        rs.getInt("capacity"),
                        rs.getInt("seats_available")
                    );
                }
            }
        }
        return null; 
    }
    
    public List<Event> getAllEvents() throws Exception {
        String sql = "SELECT * FROM events";
        List<Event> events = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();  
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Event event = new Event(
                    rs.getInt("event_id"),
                    rs.getString("title"),
                    rs.getString("category"),
                    rs.getString("date"),
                    rs.getString("location"),
                    rs.getInt("capacity"),
                    rs.getInt("seats_available")
                );
                events.add(event);
            }
        }
        return events;
    }
    
    public int getTotalEventsCount() throws Exception {
        String sql = "SELECT COUNT(*) AS total FROM events";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
            return rs.getInt("total");
            }
        }
        return 0;
    }

    public int getAvailableEventsCount() throws Exception {
        String sql = "SELECT COUNT(*) AS available FROM events WHERE seats_available > 0";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("available");
            }
        }
        return 0;
    }

    public int getFullEventsCount() throws Exception {
        String sql = "SELECT COUNT(*) AS full_count FROM events WHERE seats_available <= 0";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("full_count");
            }
        }
        return 0;
    }

}

