package organizer;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import dao.EventDAO;
import dao.RegistrationDAO;
import Model.Event;

public class OrganizerReportsFrame extends JFrame {

    private JComboBox<String> comboEvents;
    private JTextArea txtStats;
    private JButton btnBack;
    private JButton btnRefresh;

    private EventDAO eventDAO;
    private RegistrationDAO registrationDAO;
    private List<Event> events;
    
    private int userId;

    public OrganizerReportsFrame(int userId) {
        this.userId = userId;
        
        eventDAO = new EventDAO();
        registrationDAO = new RegistrationDAO();
        
        setTitle("Event Reports - User ID: " + userId);
        setSize(450, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel headerLabel = new JLabel("Event Statistics Report", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        selectionPanel.add(new JLabel("Select Event:"));
        comboEvents = new JComboBox<>();
        comboEvents.setPreferredSize(new Dimension(250, 25));
        selectionPanel.add(comboEvents);
        centerPanel.add(selectionPanel, BorderLayout.NORTH);

        txtStats = new JTextArea(15, 30);
        txtStats.setEditable(false);
        txtStats.setFont(new Font("Arial", Font.PLAIN, 12));
        txtStats.setBorder(BorderFactory.createTitledBorder("Event Statistics"));
        centerPanel.add(new JScrollPane(txtStats), BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        btnRefresh = new JButton("Refresh");
        btnBack = new JButton("Back");
        
        Dimension btnSize = new Dimension(100, 30);
        btnRefresh.setPreferredSize(btnSize);
        btnBack.setPreferredSize(btnSize);
        
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnBack);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        loadEventsFromDatabase();

        btnRefresh.addActionListener(e -> loadEventsFromDatabase());
        btnBack.addActionListener(e -> {
            dispose();
            new OrganizerFrame(userId).setVisible(true);
        });
        
        comboEvents.addActionListener(e -> showStats());
    }

    private void loadEventsFromDatabase() {
        try {
            events = eventDAO.getAllEvents();
            comboEvents.removeAllItems();

            if (events.isEmpty()) {
                comboEvents.addItem("No events available");
                txtStats.setText("No events found in the system.");
            } else {
                comboEvents.addItem("-- Select Event --");
                for (Event event : events) {
                    comboEvents.addItem(event.getTitle() + " (ID: " + event.getEventId() + ")");
                }
                comboEvents.setSelectedIndex(0);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error loading events: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showStats() {
        int selectedIndex = comboEvents.getSelectedIndex();
        if (selectedIndex <= 0 || events == null || events.isEmpty()) {
            txtStats.setText("Please select an event from the list.");
            return;
        }

        Event selectedEvent = events.get(selectedIndex - 1);

        try {
            int registeredCount = registrationDAO.countRegistrations(selectedEvent.getEventId());
            int totalCapacity = selectedEvent.getCapacity();
            int remainingSeats = totalCapacity - registeredCount;
            double occupancyRate = totalCapacity > 0 ? (registeredCount * 100.0) / totalCapacity : 0;

            StringBuilder stats = new StringBuilder();
            stats.append("Event: ").append(selectedEvent.getTitle()).append("\n");
            stats.append("Date: ").append(selectedEvent.getDate()).append("\n");
            stats.append("Location: ").append(selectedEvent.getLocation()).append("\n");
            stats.append("Category: ").append(selectedEvent.getCategory()).append("\n");
            stats.append("────────────────────────────\n\n");
            
            stats.append("REGISTRATION STATISTICS:\n");
            stats.append("• Total Capacity: ").append(totalCapacity).append(" seats\n");
            stats.append("• Registered: ").append(registeredCount).append(" attendees\n");
            stats.append("• Remaining: ").append(remainingSeats).append(" seats\n");
            stats.append("• Occupancy Rate: ").append(String.format("%.1f", occupancyRate)).append("%\n\n");
            
            stats.append("STATUS: ");
            if (remainingSeats == 0) {
                stats.append("FULLY BOOKED\n");
            } else if (occupancyRate >= 80) {
                stats.append("ALMOST FULL\n");
            } else if (occupancyRate >= 50) {
                stats.append("GOOD\n");
            } else {
                stats.append("AVAILABLE\n");
            }

            txtStats.setText(stats.toString());

        } catch (Exception ex) {
            txtStats.setText("Error loading statistics: " + ex.getMessage());
        }
    }
}