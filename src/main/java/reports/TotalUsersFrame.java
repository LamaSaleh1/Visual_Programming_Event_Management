package reports;

import javax.swing.*;
import java.awt.*;
import dao.UserDAO;
import admin.SystemReportsFrame;

public class TotalUsersFrame extends JFrame {

    private UserDAO userDAO;
    private JLabel lblCount;
    private int userId;

    public TotalUsersFrame(int userId) {
        this.userId = userId;
        userDAO = new UserDAO();

        setTitle("Users Statistics");
        setSize(350, 300);
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

        btnRefresh.addActionListener(e -> loadUsersData());
        btnBack.addActionListener(e -> {
            new SystemReportsFrame(userId).setVisible(true);
            dispose();
        });

        loadUsersData();
    }

    private void loadUsersData() {
        try {
            int totalUsers = userDAO.getTotalUsersCount();
            int admins = userDAO.getUsersCountByRole("admin");
            int organizers = userDAO.getUsersCountByRole("organizer");
            int attendees = userDAO.getUsersCountByRole("attendee");

            String html = "<html><div style='text-align: center;'>" +
                "<h2>Users Statistics</h2>" +
                "<p style='font-size: 20px; color: orange;'>Total Users: " + totalUsers + "</p>" +
                "<p style='font-size: 14px; color: red;'>Admins: " + admins + "</p>" +
                "<p style='font-size: 14px; color: blue;'>Organizers: " + organizers + "</p>" +
                "<p style='font-size: 14px; color: green;'>Attendees: " + attendees + "</p>" +
                "</div></html>";

            lblCount.setText(html);

        } catch (Exception ex) {
            lblCount.setText("Error loading users data: " + ex.getMessage());
        }
    }
}