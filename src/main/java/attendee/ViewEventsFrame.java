package attendee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import dao.EventDAO;
import Model.Event;

public class ViewEventsFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private EventDAO eventDAO = new EventDAO();

    private JTextField txtSearch;
    private JComboBox<String> categoryFilter;
    private JComboBox<String> locationFilter;
    private JCheckBox seatsOnly;

    private List<Event> allEvents;
    private JButton btnBack;
    private JButton btnRegister;
    private JButton btnReports;
    private int currentUserId;

    public ViewEventsFrame(int userId) {
        this.currentUserId = userId;

        setTitle("View Events");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Search");
        btnSearch.setPreferredSize(new Dimension(80, 25));
        searchPanel.add(new JLabel("Search:"), BorderLayout.WEST);
        searchPanel.add(txtSearch, BorderLayout.CENTER);
        searchPanel.add(btnSearch, BorderLayout.EAST);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        
        String[] categories = {"All", "Workshop", "Seminar", "Conference", "Sports", "Entertainment", "Other"};
        categoryFilter = new JComboBox<>(categories);
        categoryFilter.setPreferredSize(new Dimension(120, 25));

        String[] locations = {"All", "Riyadh", "Jeddah", "Qassim", "Dammam"};
        locationFilter = new JComboBox<>(locations);
        locationFilter.setPreferredSize(new Dimension(100, 25));

        seatsOnly = new JCheckBox("Available Only");

        filterPanel.add(new JLabel("Category:"));
        filterPanel.add(categoryFilter);
        filterPanel.add(new JLabel("Location:"));
        filterPanel.add(locationFilter);
        filterPanel.add(seatsOnly);

        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Title", "Category", "Date", "Location", "Capacity", "Available"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(90);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.getColumnModel().getColumn(5).setPreferredWidth(60);
        table.getColumnModel().getColumn(6).setPreferredWidth(70);
        
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        btnRegister = new JButton("Register");
        btnReports = new JButton("Reports");
        btnBack = new JButton("Back");
        
        Dimension btnSize = new Dimension(100, 30);
        btnRegister.setPreferredSize(btnSize);
        btnReports.setPreferredSize(btnSize);
        btnBack.setPreferredSize(btnSize);
        
        bottomPanel.add(btnRegister);
        bottomPanel.add(btnReports);
        bottomPanel.add(btnBack);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        btnSearch.addActionListener(e -> filterTable());
        categoryFilter.addActionListener(e -> filterTable());
        locationFilter.addActionListener(e -> filterTable());
        seatsOnly.addActionListener(e -> filterTable());
        
        btnRegister.addActionListener(e -> openRegisterFrame());
        btnReports.addActionListener(e -> {
            dispose();
            new AttendeeReportsFrame(currentUserId).setVisible(true);
        });
        btnBack.addActionListener(e -> {
            dispose();
            new AttendeeFrame(currentUserId).setVisible(true);
        });

        loadEvents();
    }

    private void loadEvents() {
        try {
            allEvents = eventDAO.getAllEvents();
            filterTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error loading events: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterTable() {
        if (allEvents == null) return;

        String text = txtSearch.getText().toLowerCase();
        String selectedCategory = categoryFilter.getSelectedItem().toString();
        String selectedLocation = locationFilter.getSelectedItem().toString();
        boolean onlySeats = seatsOnly.isSelected();

        model.setRowCount(0);

        for (Event e : allEvents) {
            boolean match = true;

            if (!text.isEmpty()) {
                String rowText = (e.getEventId() + " " +
                                  e.getTitle() + " " +
                                  e.getCategory() + " " +
                                  e.getDate() + " " +
                                  e.getLocation()).toLowerCase();
                if (!rowText.contains(text)) match = false;
            }

            if (!selectedCategory.equals("All") && !selectedCategory.equals(e.getCategory()))
                match = false;

            if (!selectedLocation.equals("All") && !selectedLocation.equals(e.getLocation()))
                match = false;

            if (onlySeats && e.getSeatsAvailable() <= 0)
                match = false;

            if (match) {
                model.addRow(new Object[]{
                    e.getEventId(),
                    e.getTitle(),
                    e.getCategory(),
                    e.getDate(),
                    e.getLocation(),
                    e.getCapacity(),
                    e.getSeatsAvailable()
                });
            }
        }
    }

    private void openRegisterFrame() {
        new RegisterEventFrame(currentUserId).setVisible(true);
        dispose();
    }
}