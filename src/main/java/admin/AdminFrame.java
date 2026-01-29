package admin;

import javax.swing.*;
import java.awt.*;
import dao.UserDAO;
import auth.LoginFrame;

public class AdminFrame extends JFrame {

    private JButton btnManageUsers;
    private JButton btnManageEvents;
    private JButton btnSystemReports;
    private JButton btnLogout;
    private int currentUserId;

    public AdminFrame(int userId) {
        if (!checkAdminAccess(userId)) {
            return; 
        }
        
        this.currentUserId = userId;

        setTitle("Admin Dashboard - User ID: " + userId);
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel titleLabel = new JLabel("Admin Panel", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 15, 15));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));

        Dimension mainBtnSize = new Dimension(200, 45);
        btnManageUsers = new JButton("Manage Users");
        btnManageEvents = new JButton("Manage Events");
        btnSystemReports = new JButton("System Reports");
        btnLogout = new JButton("Logout");

        btnManageUsers.setPreferredSize(mainBtnSize);
        btnManageEvents.setPreferredSize(mainBtnSize);
        btnSystemReports.setPreferredSize(mainBtnSize);
        btnLogout.setPreferredSize(new Dimension(120, 35));

        centerPanel.add(btnManageUsers);
        centerPanel.add(btnManageEvents);
        centerPanel.add(btnSystemReports);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        bottomPanel.add(btnLogout);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        btnManageUsers.addActionListener(e -> {
            new ManageUsersFrame(currentUserId).setVisible(true);
            dispose();
        });

        btnManageEvents.addActionListener(e -> {
            new ManageEventsFrame("admin", currentUserId).setVisible(true);
            dispose();
        });
        
        btnSystemReports.addActionListener(e -> {
            new SystemReportsFrame(currentUserId).setVisible(true);
            dispose();
        });

        btnLogout.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Logged out successfully!",
                "Logout",
                JOptionPane.INFORMATION_MESSAGE);
            new LoginFrame().setVisible(true);
            dispose();
        });
    }

    private boolean checkAdminAccess(int userId) {
        try {
            UserDAO userDAO = new UserDAO();
            String role = userDAO.getUserRole(userId);
            
            if ("admin".equals(role)) {
                return true;
            }
            
            JOptionPane.showMessageDialog(null,
                "Access Denied!\nAdministrator privileges required.",
                "Authorization Failed",
                JOptionPane.ERROR_MESSAGE);
                
            new LoginFrame().setVisible(true);
            return false;
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Error checking permissions: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}