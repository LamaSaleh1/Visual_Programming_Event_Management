package attendee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import dao.TicketDAO;

public class TicketsFrame extends JFrame {

    private JTable table;
    private TicketDAO ticketDAO;
    private JButton btnBack;
    private JButton btnRefresh;
    private int currentUserId;

    public TicketsFrame(int userId) {
        this.currentUserId = userId;
        ticketDAO = new TicketDAO();
        
        setTitle("My Tickets");
        setSize(650, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblHeader = new JLabel("My Tickets", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 18));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainPanel.add(lblHeader, BorderLayout.NORTH);

        String[] columns = {"Ticket ID", "Event Title", "Date", "Location", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(model);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(80);   // Ticket ID
        table.getColumnModel().getColumn(1).setPreferredWidth(180);  // Event Title
        table.getColumnModel().getColumn(2).setPreferredWidth(120);  // Date
        table.getColumnModel().getColumn(3).setPreferredWidth(120);  // Location
        table.getColumnModel().getColumn(4).setPreferredWidth(80);   // Status
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 250));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        btnRefresh = new JButton("Refresh");
        btnBack = new JButton("Back");
        
        Dimension btnSize = new Dimension(100, 30);
        btnRefresh.setPreferredSize(btnSize);
        btnBack.setPreferredSize(btnSize);
        
        btnRefresh.addActionListener(e -> loadTicketsFromDB());
        btnBack.addActionListener(e -> {
            dispose();
            new AttendeeFrame(currentUserId).setVisible(true);
        });
        
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnBack);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        loadTicketsFromDB();
    }

    private void loadTicketsFromDB() {
        try {
            List<String> tickets = ticketDAO.getTicketsForUser(currentUserId);
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);

            if (tickets.isEmpty()) {
                model.addRow(new Object[]{
                    "No tickets", "You haven't registered for any events", 
                    "", "", "No Data"
                });
            } else {
                for (String ticketInfo : tickets) {
                    String[] parts = ticketInfo.split(" \\| ");
                    if (parts.length >= 4) {
                        String ticketId = parts[0].replace("Ticket ID: ", "");
                        String eventTitle = parts[1].replace("Event: ", "");
                        String eventDate = parts[2].replace("Date: ", "");
                        String location = parts[3].replace("Location: ", "");
                        
                        String status = getTicketStatus(eventDate);
                        
                        model.addRow(new Object[]{
                            ticketId,
                            eventTitle,
                            eventDate,
                            location,
                            status
                        });
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error loading tickets: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getTicketStatus(String eventDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date eventDateObj = sdf.parse(eventDate);
            Date currentDate = new Date();
            
            long diff = eventDateObj.getTime() - currentDate.getTime();
            long daysDiff = diff / (24 * 60 * 60 * 1000);
            
            if (eventDateObj.before(currentDate)) {
                return "Expired";
            } else if (daysDiff == 0) {
                return "Today";
            } else if (daysDiff <= 7) {
                return "Soon (" + daysDiff + " days)";
            } else {
                return "Active";
            }
        } catch (Exception e) {
            return "Unknown";
        }
    }
}