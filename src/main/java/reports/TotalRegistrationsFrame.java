package reports;

import javax.swing.*;
import java.awt.*;
import dao.*;
import admin.SystemReportsFrame;

public class TotalRegistrationsFrame extends JFrame {

    private EventDAO eventDAO;
    private RegistrationDAO registrationDAO;
    private JLabel lblCount;
    private int userId;

    public TotalRegistrationsFrame(int userId) {
        this.userId = userId;
        eventDAO = new EventDAO();
        registrationDAO = new RegistrationDAO();

        setTitle("Registrations Statistics");
        setSize(350, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblCount = new JLabel("", JLabel.CENTER);
        lblCount.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(lblCount, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton btnRefresh = new JButton("Refresh");
        JButton btnBack = new JButton("Back");
        
        Dimension btnSize = new Dimension(100, 30);
        btnRefresh.setPreferredSize(btnSize);
        btnBack.setPreferredSize(btnSize);
        
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnBack);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        btnRefresh.addActionListener(e -> loadRegistrationsData());
        btnBack.addActionListener(e -> {
            new SystemReportsFrame(userId).setVisible(true);
            dispose();
        });

        loadRegistrationsData();
    }

    private void loadRegistrationsData() {
        try {
            int totalRegistrations = registrationDAO.getTotalRegistrationsCount();
            int totalEvents = eventDAO.getTotalEventsCount();
            double avgPerEvent = totalEvents > 0 ? (double) totalRegistrations / totalEvents : 0;

            String html = "<html><div style='text-align: center;'>" +
                "<h2>Registrations Statistics</h2>" +
                "<p style='font-size: 20px; color: purple;'>Total Registrations: " + totalRegistrations + "</p>" +
                "<p style='font-size: 16px;'>Across " + totalEvents + " events</p>" +
                "<p style='font-size: 16px; color: blue;'>Average: " + String.format("%.1f", avgPerEvent) + " per event</p>" +
                "</div></html>";

            lblCount.setText(html);

        } catch (Exception ex) {
            lblCount.setText("Error loading registrations data: " + ex.getMessage());
        }
    }
}