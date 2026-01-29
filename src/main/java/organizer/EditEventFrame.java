package organizer;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import dao.EventDAO;
import Model.Event;
import admin.AdminFrame;

public class EditEventFrame extends JFrame {
    private String userRole;
    private int userId;

    private EventDAO eventDAO;
    private List<Event> events;

    private JComboBox<String> cmbEvents;
    private JTextField txtTitle;
    private JTextField txtDateTime;
    private JComboBox<String> cmbCategory;
    private JComboBox<String> cmbLocation;
    private JTextField txtCapacity;

    private JButton btnUpdate;
    private JButton btnClear;
    private JButton btnBack;

    public EditEventFrame(String userRole, int userId) {
        this.userRole = userRole;
        this.userId = userId;
        eventDAO = new EventDAO();
        
        setTitle("Edit Event");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblHeader = new JLabel("Edit Existing Event", JLabel.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 18));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainPanel.add(lblHeader, BorderLayout.NORTH);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        topPanel.add(new JLabel("Select Event:"));
        cmbEvents = new JComboBox<>();
        cmbEvents.setPreferredSize(new Dimension(250, 25));
        topPanel.add(cmbEvents);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        
        txtTitle = new JTextField();
        txtDateTime = new JTextField();
        txtDateTime.setToolTipText("Format: yyyy-MM-dd HH:mm");

        cmbCategory = new JComboBox<>(new String[]{
            "-- Select Category --",
            "Workshop", "Conference", "Seminar", 
            "Sports", "Entertainment", "Other"
        });

        cmbLocation = new JComboBox<>(new String[]{
            "-- Select Location --",
            "Riyadh",
            "Jeddah", 
            "Qassim",
            "Dammam",
            "Mecca",
        });

        txtCapacity = new JTextField();

        formPanel.add(new JLabel("Title:"));
        formPanel.add(txtTitle);
        formPanel.add(new JLabel("Date & Time:"));
        formPanel.add(txtDateTime);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(cmbCategory);
        formPanel.add(new JLabel("Location:"));
        formPanel.add(cmbLocation);
        formPanel.add(new JLabel("Capacity:"));
        formPanel.add(txtCapacity);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        btnUpdate = new JButton("Update");
        btnClear = new JButton("Clear");
        btnBack = new JButton("Back");

        Dimension btnSize = new Dimension(90, 30);
        btnUpdate.setPreferredSize(btnSize);
        btnClear.setPreferredSize(btnSize);
        btnBack.setPreferredSize(btnSize);

        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnBack);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        loadEventsFromDatabase();

        cmbEvents.addActionListener(e -> loadSelectedEvent());
        btnUpdate.addActionListener(e -> handleUpdate());
        btnClear.addActionListener(e -> clearFields());
        btnBack.addActionListener(e -> {
            if ("admin".equals(userRole)) {
                new AdminFrame(userId).setVisible(true);
            } else {
                new OrganizerFrame(userId).setVisible(true);
            }
            dispose();
        });

        if (cmbEvents.getItemCount() > 0) {
            cmbEvents.setSelectedIndex(0);
        }
    }

    private void loadEventsFromDatabase() {
        try {
            events = eventDAO.getAllEvents();
            cmbEvents.removeAllItems();

            if (events.isEmpty()) {
                cmbEvents.addItem("No events available");
                cmbEvents.setEnabled(false);
            } else {
                cmbEvents.setEnabled(true);
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

    private void loadSelectedEvent() {
        int index = cmbEvents.getSelectedIndex();
        if (index < 0 || index >= events.size()) return;

        Event event = events.get(index);
        txtTitle.setText(event.getTitle());
        txtDateTime.setText(event.getDate());
        cmbCategory.setSelectedItem(event.getCategory());
        
        String location = event.getLocation();
        boolean found = false;
        for (int i = 0; i < cmbLocation.getItemCount(); i++) {
            if (cmbLocation.getItemAt(i).equals(location)) {
                cmbLocation.setSelectedIndex(i);
                found = true;
                break;
            }
        }
        if (!found) {
            cmbLocation.setSelectedIndex(0);
        }
        
        txtCapacity.setText(String.valueOf(event.getCapacity()));
    }

    private void handleUpdate() {
        if (events.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No events to edit.",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int index = cmbEvents.getSelectedIndex();
        if (index < 0 || index >= events.size()) return;

        Event originalEvent = events.get(index);

        String title = txtTitle.getText().trim();
        String dateTime = txtDateTime.getText().trim();
        String category = (String) cmbCategory.getSelectedItem();
        String location = (String) cmbLocation.getSelectedItem();
        String capacityStr = txtCapacity.getText().trim();

        if (title.isEmpty() || dateTime.isEmpty() || capacityStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill all required fields.",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (category.equals("-- Select Category --")) {
            JOptionPane.showMessageDialog(this,
                "Please select a category.",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (location.equals("-- Select Location --")) {
            JOptionPane.showMessageDialog(this,
                "Please select a location.",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int capacity;
        try {
            capacity = Integer.parseInt(capacityStr);
            if (capacity <= 0) {
                JOptionPane.showMessageDialog(this,
                    "Capacity must be positive.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Capacity must be a number.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            sdf.setLenient(false);
            Date eventDate = sdf.parse(dateTime);
            
            if (eventDate.before(new Date())) {
                JOptionPane.showMessageDialog(this,
                    "Event date must be in the future.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Date originalDate = sdf.parse(originalEvent.getDate());
            if (originalDate.before(new Date())) {
                JOptionPane.showMessageDialog(this,
                    "Cannot edit past events!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this,
                "Invalid date format. Use: yyyy-MM-dd HH:mm",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Event updatedEvent = new Event(
                originalEvent.getEventId(), title, category, dateTime, 
                location, capacity, capacity
            );
            
            eventDAO.updateEvent(updatedEvent);
            
            JOptionPane.showMessageDialog(this,
                "Event updated successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
                    
            loadEventsFromDatabase();
            if (cmbEvents.getItemCount() > 0) {
                cmbEvents.setSelectedIndex(Math.min(index, cmbEvents.getItemCount() - 1));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error updating event: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        txtTitle.setText("");
        txtDateTime.setText("");
        cmbCategory.setSelectedIndex(0);
        cmbLocation.setSelectedIndex(0);
        txtCapacity.setText("");
    }
}