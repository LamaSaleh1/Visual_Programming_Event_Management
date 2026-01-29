package organizer;

import dao.EventDAO;
import Model.Event;
import admin.AdminFrame;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DeleteEventFrame extends JFrame {

    private JComboBox<String> cmbEvents;
    private JButton btnDelete;
    private JButton btnBack;
    
    private EventDAO eventDAO;
    private List<Event> events;
    
    private String userRole;
    private int userId;
    
    public DeleteEventFrame(String userRole, int userId) {
        this.userRole = userRole;
        this.userId = userId;
        eventDAO = new EventDAO();
        
        setTitle("Delete Event");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblHeader = new JLabel("Delete Event", JLabel.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 18));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainPanel.add(lblHeader, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.add(new JLabel("Select Event to Delete:"));
        cmbEvents = new JComboBox<>();
        centerPanel.add(cmbEvents);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        btnDelete = new JButton("Delete");
        btnBack = new JButton("Back");

        Dimension btnSize = new Dimension(90, 30);
        btnDelete.setPreferredSize(btnSize);
        btnBack.setPreferredSize(btnSize);

        btnPanel.add(btnDelete);
        btnPanel.add(btnBack);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        loadEventsFromDatabase();

        btnDelete.addActionListener(e -> handleDelete());
        btnBack.addActionListener(e -> {
            if ("admin".equals(userRole)) {
                new AdminFrame(userId).setVisible(true);
            } else {
                new OrganizerFrame(userId).setVisible(true);
            }
            dispose();
        });
    }

    private void loadEventsFromDatabase() {
        try {
            events = eventDAO.getAllEvents();
            cmbEvents.removeAllItems();

            if (events.isEmpty()) {
                cmbEvents.addItem("-- No Events Available --");
                cmbEvents.setEnabled(false);
                btnDelete.setEnabled(false);
            } else {
                cmbEvents.setEnabled(true);
                btnDelete.setEnabled(true);
                for (Event event : events) {
                    cmbEvents.addItem(event.getTitle() + " | " + event.getDate());
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error loading events: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        if (events.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No events to delete.",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int index = cmbEvents.getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(this, 
                "Please select an event.",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Event selectedEvent = events.get(index);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this event?\n" +
            "Title: " + selectedEvent.getTitle() + "\n" +
            "Date: " + selectedEvent.getDate(),
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            eventDAO.deleteEvent(selectedEvent.getEventId());
            JOptionPane.showMessageDialog(this,
                "Event deleted successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            loadEventsFromDatabase();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error deleting event: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}