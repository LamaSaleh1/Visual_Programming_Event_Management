package service;

import java.util.UUID;

public class TicketService {
    
    public static String generateTicketId() {
        return "TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    public static String generateQRCodeData(String ticketId, String eventTitle, String eventDate) {
        return String.format("TicketID: %s\nEvent: %s\nDate: %s", 
                           ticketId, eventTitle, eventDate);
    }
}