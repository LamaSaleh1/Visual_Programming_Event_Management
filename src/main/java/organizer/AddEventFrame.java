package organizer;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import dao.EventDAO;
import Model.Event;
import admin.AdminFrame;

public class AddEventFrame extends JFrame {
    private String userRole;
    private int userId;
    
    private JTextField txtTitle;
    private JTextField txtDateTime;
    private JComboBox<String> cmbCategory;
    private JComboBox<String> cmbLocation; 
    private JTextField txtCapacity;

    private JButton btnSave;
    private JButton btnClear;
    private JButton btnBack;
    
    private EventDAO eventDAO;

    public AddEventFrame(String userRole, int userId) {
        this.userRole = userRole;
        this.userId = userId;
        eventDAO = new EventDAO();
        
        setTitle("Add Event");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblHeader = new JLabel("Create New Event", JLabel.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 18));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(lblHeader, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        txtTitle = new JTextField();
        txtDateTime = new JTextField();
        txtDateTime.setToolTipText("Format: yyyy-MM-dd HH:mm");

        cmbCategory = new JComboBox<>(new String[]{
            "-- Select Category --",
            "Workshop",
            "Seminar",
            "Conference",
            "Sports",
            "Entertainment",
            "Other"
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

        btnSave = new JButton("Save");
        btnClear = new JButton("Clear");
        btnBack = new JButton("Back");

        Dimension btnSize = new Dimension(90, 30);
        btnSave.setPreferredSize(btnSize);
        btnClear.setPreferredSize(btnSize);
        btnBack.setPreferredSize(btnSize);

        buttonPanel.add(btnSave);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnBack);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        btnSave.addActionListener(e -> handleSave());
        btnClear.addActionListener(e -> clearFields());
        btnBack.addActionListener(e -> {
            if ("admin".equals(userRole)) {
                new AdminFrame(userId).setVisible(true);
            } else {
                new OrganizerFrame(userId).setVisible(true);
            }
            dispose();
        });
    }

    private void handleSave() {
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
                    "Capacity must be a positive number.", 
                    "Warning", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Capacity must be a valid number.", 
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
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, 
                "Invalid date format. Use: yyyy-MM-dd HH:mm", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Event event = new Event(0, title, category, dateTime, location, capacity, capacity);
            eventDAO.addEvent(event);
            
            JOptionPane.showMessageDialog(this,
                "Event added successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
                    
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error saving event: " + ex.getMessage(),
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