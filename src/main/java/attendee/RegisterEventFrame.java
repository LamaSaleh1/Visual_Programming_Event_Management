package attendee;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import dao.*;
import Model.Event;
import Model.User;

public class RegisterEventFrame extends JFrame {

    private JComboBox<String> cmbEvents;
    private JTextField txtAttendeeName;
    private JTextField txtAttendeeUsername;
    private JButton btnRegister;
    private JButton btnBack;

    private EventDAO eventDAO;
    private UserDAO userDAO;
    private TicketDAO ticketDAO; 
    private RegistrationDAO registrationDAO;
    private List<Event> events;
    private int currentUserId;

    public RegisterEventFrame(int userId) {
        this.currentUserId = userId;
        
        eventDAO = new EventDAO();
        userDAO = new UserDAO();
        ticketDAO = new TicketDAO(); 
        registrationDAO = new RegistrationDAO();

        setTitle("Register in Event");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblHeader = new JLabel("Register in Event", JLabel.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 18));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainPanel.add(lblHeader, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        formPanel.add(new JLabel("Select Event:"));
        cmbEvents = new JComboBox<>();
        cmbEvents.setPreferredSize(new Dimension(200, 25));
        formPanel.add(cmbEvents);

        formPanel.add(new JLabel("Your Name:"));
        txtAttendeeName = new JTextField();
        formPanel.add(txtAttendeeName);

        formPanel.add(new JLabel("Your Username:"));
        txtAttendeeUsername = new JTextField();
        formPanel.add(txtAttendeeUsername);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        btnRegister = new JButton("Register");
        btnBack = new JButton("Back");
        
        Dimension btnSize = new Dimension(100, 30);
        btnRegister.setPreferredSize(btnSize);
        btnBack.setPreferredSize(btnSize);
        
        btnPanel.add(btnRegister);
        btnPanel.add(btnBack);

        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        loadEventsFromDatabase();

        btnRegister.addActionListener(e -> handleRegister());
        btnBack.addActionListener(e -> {
            dispose();
            new AttendeeFrame(currentUserId).setVisible(true);
        });
    }

    private void loadEventsFromDatabase() {
        try {
            events = eventDAO.getAllEvents();
            cmbEvents.removeAllItems();

            if (events.isEmpty()) {
                cmbEvents.addItem("-- No Events Available --");
                cmbEvents.setEnabled(false);
                btnRegister.setEnabled(false);
            } else {
                cmbEvents.setEnabled(true);
                btnRegister.setEnabled(true);
                for (Event event : events) {
                    cmbEvents.addItem(event.getTitle() + " | " + event.getDate() + " | " + event.getLocation());
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error loading events: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

 private void handleRegister() {
    if (events.isEmpty()) {
        JOptionPane.showMessageDialog(this,
                "No events available.",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
        return;
    }

    int eventIndex = cmbEvents.getSelectedIndex();
    if (eventIndex < 0) {
        JOptionPane.showMessageDialog(this,
                "Please select an event.",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
        return;
    }

    Event selectedEvent = events.get(eventIndex);

    if (selectedEvent.getSeatsAvailable() <= 0) {
        JOptionPane.showMessageDialog(this,
                "Sorry, this event is full. No available seats.",
                "Event Full",
                JOptionPane.WARNING_MESSAGE);
        return;
    }

    String name = txtAttendeeName.getText().trim();
    String username = txtAttendeeUsername.getText().trim();

    if (name.isEmpty()) {
        JOptionPane.showMessageDialog(this,
                "Name is required.",
                "Validation",
                JOptionPane.WARNING_MESSAGE);
        return;
    }

    if (username.isEmpty()) {
        JOptionPane.showMessageDialog(this,
                "Username is required.",
                "Validation",
                JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        User user = userDAO.getUserByUsername(username);
        if (user == null) {
            JOptionPane.showMessageDialog(this,
                    "User not found! Please check your username.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (user.getId() != currentUserId) {
            JOptionPane.showMessageDialog(this,
                    "You can only register with your own account!",
                    "Authorization Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Event currentEvent = eventDAO.getEventById(selectedEvent.getEventId());
        if (currentEvent.getSeatsAvailable() <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Sorry, this event is now full. Please select another event.",
                    "Event Full",
                    JOptionPane.WARNING_MESSAGE);
            loadEventsFromDatabase(); 
            return;
        }
        
        boolean hasTicket = ticketDAO.hasTicket(currentUserId, selectedEvent.getEventId());
        if (hasTicket) {
            JOptionPane.showMessageDialog(this,
                    "You already have a ticket for this event!\n\n" +
                    "Event: " + selectedEvent.getTitle() + "\n" +
                    "Date: " + selectedEvent.getDate() + "\n" +
                    "Please check 'My Tickets' section.",
                    "Ticket Already Exists",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        registrationDAO.register(currentUserId, selectedEvent.getEventId());
        
        eventDAO.updateSeatsAvailable(selectedEvent.getEventId(), -1);
        
        ticketDAO.createTicket(currentUserId, selectedEvent.getEventId());

        JOptionPane.showMessageDialog(this,
                    "Registered Successfully!\n" +
                    "Event: " + selectedEvent.getTitle() + "\n" +
                    "Date: " + selectedEvent.getDate() + "\n" +
                    "Name: " + name + "\n" +
                    "Username: " + username + "\n" +
                    "Ticket number: TKT-" + System.currentTimeMillis() + "\n\n" +
                    "Please save your ticket number for the future.",
                    "Registration Successful",
                    JOptionPane.INFORMATION_MESSAGE);

        txtAttendeeName.setText("");
        txtAttendeeUsername.setText("");
        
        loadEventsFromDatabase();

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this,
                "Registration failed: " + ex.getMessage(),
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
}