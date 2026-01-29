package service;

import dao.EventDAO;
import dao.RegistrationDAO;
import dao.TicketDAO;
import database.DBConnection;
import java.sql.Connection;

public class TransactionService {
    
    public static boolean completeEventRegistration(int userId, int eventId) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); 
            
            EventDAO eventDAO = new EventDAO();
            TicketDAO ticketDAO = new TicketDAO();
            RegistrationDAO registrationDAO = new RegistrationDAO();
            
            if (!eventDAO.hasAvailableSeats(eventId)) {
                throw new Exception("No available seats");
            }
            
            ticketDAO.createTicket(userId, eventId);
            
            registrationDAO.register(userId, eventId);
            
            if (!eventDAO.updateSeatsAvailable(eventId, -1)) {
                throw new Exception("Failed to update seats");
            }
            
            conn.commit();
            return true;
            
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Transaction rolled back: " + e.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}