package attendee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import dao.EventDAO;
import dao.TicketDAO;
import Model.Event;

public class AttendeeReportsFrame extends JFrame {

    private EventDAO eventDAO;
    private TicketDAO ticketDAO;
    
    private JTable table;
    private JLabel lblTotalEvents, lblAvailable, lblRegistered;
    private JComboBox<String> cmbReportType;
    private JButton btnRefresh;
    private JButton btnBack;
    private int currentUserId;

    public AttendeeReportsFrame(int userId) {
        this.currentUserId = userId;
        eventDAO = new EventDAO();
        ticketDAO = new TicketDAO();
        
        setTitle("Attendee Reports & Statistics");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        lblTotalEvents = new JLabel("Total Events: 0", SwingConstants.CENTER);
        lblAvailable = new JLabel("Available Events: 0", SwingConstants.CENTER);
        lblRegistered = new JLabel("My Registrations: 0", SwingConstants.CENTER);
        
        Font statsFont = new Font("Arial", Font.BOLD, 12);
        lblTotalEvents.setFont(statsFont);
        lblAvailable.setFont(statsFont);
        lblRegistered.setFont(statsFont);
        
        lblTotalEvents.setBorder(BorderFactory.createTitledBorder("Total Events"));
        lblAvailable.setBorder(BorderFactory.createTitledBorder("Available"));
        lblRegistered.setBorder(BorderFactory.createTitledBorder("My Registrations"));
        
        statsPanel.add(lblTotalEvents);
        statsPanel.add(lblAvailable);
        statsPanel.add(lblRegistered);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        filterPanel.add(new JLabel("Report Type:"));
        cmbReportType = new JComboBox<>(new String[]{
            "All Events", 
            "Available Only", 
            "My Registrations"
        });
        cmbReportType.setPreferredSize(new Dimension(150, 25));
        filterPanel.add(cmbReportType);
        
        btnRefresh = new JButton("Refresh");
        btnRefresh.setPreferredSize(new Dimension(100, 25));
        filterPanel.add(btnRefresh);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(statsPanel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.SOUTH);

        String[] columns = {
            "Event ID", "Title", "Category", "Date", "Location", 
            "Capacity", "Available", "Status", "My Registration"
        };
        
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(60);   // Event
        table.getColumnModel().getColumn(1).setPreferredWidth(120);  // Title
        table.getColumnModel().getColumn(2).setPreferredWidth(80);   // Category
        table.getColumnModel().getColumn(3).setPreferredWidth(100);  // Date
        table.getColumnModel().getColumn(4).setPreferredWidth(80);   // Location
        table.getColumnModel().getColumn(5).setPreferredWidth(60);   // Capacity
        table.getColumnModel().getColumn(6).setPreferredWidth(70);   // Available
        table.getColumnModel().getColumn(7).setPreferredWidth(70);   // Status
        table.getColumnModel().getColumn(8).setPreferredWidth(100);  // My Registration
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(850, 350));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        btnBack = new JButton("Back");
        btnBack.setPreferredSize(new Dimension(100, 30));
        bottomPanel.add(btnBack);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        btnRefresh.addActionListener(e -> loadReports());
        cmbReportType.addActionListener(e -> loadReports());
        btnBack.addActionListener(e -> {
            dispose();
            new AttendeeFrame(currentUserId).setVisible(true);
        });

        loadReports();
    }

    private void loadReports() {
        try {
            List<Event> events = eventDAO.getAllEvents();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);

            int totalEvents = events.size();
            int availableEvents = 0;
            int myRegistrations = 0;

            String reportType = (String) cmbReportType.getSelectedItem();

            for (Event event : events) {
                boolean isAvailable = event.getSeatsAvailable() > 0;
                boolean isRegistered = ticketDAO.hasTicket(currentUserId, event.getEventId());
                
                if (isAvailable) availableEvents++;
                if (isRegistered) myRegistrations++;

                boolean showEvent = true;
                if ("Available Only".equals(reportType) && !isAvailable) {
                    showEvent = false;
                } else if ("My Registrations".equals(reportType) && !isRegistered) {
                    showEvent = false;
                }

                if (showEvent) {
                    String status = isAvailable ? "Available" : "Full";
                    String registrationStatus = isRegistered ? "Registered" : "Not Registered";
                    
                    model.addRow(new Object[]{
                        event.getEventId(),
                        event.getTitle(),
                        event.getCategory(),
                        event.getDate(),
                        event.getLocation(),
                        event.getCapacity(),
                        event.getSeatsAvailable(),
                        status,
                        registrationStatus
                    });
                }
            }

            lblTotalEvents.setText("Total Events: " + totalEvents);
            lblAvailable.setText("Available Events: " + availableEvents);
            lblRegistered.setText("My Registrations: " + myRegistrations);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error loading reports: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}