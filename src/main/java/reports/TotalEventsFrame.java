package reports;

import javax.swing.*;
import java.awt.*;
import dao.EventDAO;
import admin.SystemReportsFrame;

public class TotalEventsFrame extends JFrame {

    private EventDAO eventDAO;
    private JLabel lblCount;
    private int userId;

    public TotalEventsFrame(int userId) {
        this.userId = userId;
        eventDAO = new EventDAO();

        setTitle("Events Statistics");
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

        btnRefresh.addActionListener(e -> loadEventsData());
        btnBack.addActionListener(e -> {
            new SystemReportsFrame(userId).setVisible(true);
            dispose();
        });

        loadEventsData();
    }

    private void loadEventsData() {
        try {
            int totalEvents = eventDAO.getTotalEventsCount();
            int availableEvents = eventDAO.getAvailableEventsCount();
            int fullEvents = eventDAO.getFullEventsCount();

            String html = "<html><div style='text-align: center;'>" +
                "<h2>Events Statistics</h2>" +
                "<p style='font-size: 20px; color: blue;'>Total Events: " + totalEvents + "</p>" +
                "<p style='font-size: 16px; color: green;'>Available: " + availableEvents + "</p>" +
                "<p style='font-size: 16px; color: red;'>Full: " + fullEvents + "</p>" +
                "</div></html>";

            lblCount.setText(html);

        } catch (Exception ex) {
            lblCount.setText("Error loading events data: " + ex.getMessage());
        }
    }
}