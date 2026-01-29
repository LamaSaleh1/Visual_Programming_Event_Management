package organizer;

import auth.LoginFrame;
import dao.UserDAO;
import javax.swing.*;
import java.awt.*;

public class OrganizerFrame extends JFrame {

    private JButton btnAddEvent;
    private JButton btnEditEvent;
    private JButton btnDeleteEvent;
    private JButton btnReports;   
    private JButton btnLogout;
    private int currentUserId;
    
    public OrganizerFrame(int userId) {
        if (!checkOrganizerAccess(userId)) {
            return;
        }
        
        this.currentUserId = userId;

        setTitle("Organizer Dashboard - User ID: " + userId);
        setSize(380, 380); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Organizer Panel", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        btnAddEvent = new JButton("Add Event");
        btnEditEvent = new JButton("Edit Event");
        btnDeleteEvent = new JButton("Delete Event");
        btnReports = new JButton("Reports / Statistics");
        btnLogout = new JButton("Logout");

        Dimension mainBtnSize = new Dimension(180, 35);
        btnAddEvent.setPreferredSize(mainBtnSize);
        btnEditEvent.setPreferredSize(mainBtnSize);
        btnDeleteEvent.setPreferredSize(mainBtnSize);
        btnReports.setPreferredSize(mainBtnSize);
        btnLogout.setPreferredSize(new Dimension(120, 30));

        centerPanel.add(btnAddEvent);
        centerPanel.add(btnEditEvent);
        centerPanel.add(btnDeleteEvent);
        centerPanel.add(btnReports);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        bottomPanel.add(btnLogout);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        btnAddEvent.addActionListener(e -> {
            dispose();
            new AddEventFrame("organizer", currentUserId).setVisible(true);
        });
        
        btnEditEvent.addActionListener(e -> {
            dispose();
            new EditEventFrame("organizer", currentUserId).setVisible(true);
        });
        
        btnDeleteEvent.addActionListener(e -> {
            dispose();
            new DeleteEventFrame("organizer", currentUserId).setVisible(true);
        });
        
        btnReports.addActionListener(e -> {
            dispose();
            new OrganizerReportsFrame(currentUserId).setVisible(true);
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

    private boolean checkOrganizerAccess(int userId) {
        try {
            UserDAO userDAO = new UserDAO();
            String role = userDAO.getUserRole(userId);
            
            if ("organizer".equals(role)) {
                return true;
            }
            
            JOptionPane.showMessageDialog(null,
                "Access Denied!\nOrganizer privileges required.",
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