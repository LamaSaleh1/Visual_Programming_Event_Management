package admin;

import javax.swing.*;
import java.awt.*;
import organizer.*;

public class ManageEventsFrame extends JFrame {

    private JButton btnAddEvent;
    private JButton btnEditEvent;
    private JButton btnDeleteEvent;
    private JButton btnBack;
    private String userRole; 
    private int userId;

    public ManageEventsFrame(String userRole, int userId) {
        this.userRole = userRole;
        this.userId = userId;

        setTitle("Manage Events");
        setSize(350, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        btnAddEvent = new JButton("Add Event");
        btnEditEvent = new JButton("Edit Event");
        btnDeleteEvent = new JButton("Delete Event");
        btnBack = new JButton("Back");

        Dimension btnSize = new Dimension(160, 35);
        btnAddEvent.setPreferredSize(btnSize);
        btnEditEvent.setPreferredSize(btnSize);
        btnDeleteEvent.setPreferredSize(btnSize);
        btnBack.setPreferredSize(new Dimension(120, 30));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Event Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel buttonsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        
        buttonsPanel.add(btnAddEvent);
        buttonsPanel.add(btnEditEvent);
        buttonsPanel.add(btnDeleteEvent);

        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        backPanel.add(btnBack);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(buttonsPanel, BorderLayout.CENTER);
        mainPanel.add(backPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        btnAddEvent.addActionListener(e -> {
            dispose();
            new AddEventFrame(userRole, userId).setVisible(true);
        });

        btnEditEvent.addActionListener(e -> {
            dispose();
            new EditEventFrame(userRole, userId).setVisible(true);
        });

        btnDeleteEvent.addActionListener(e -> {
            dispose();
            new DeleteEventFrame(userRole, userId).setVisible(true);
        });

        btnBack.addActionListener(e -> {
            if ("admin".equals(userRole)) {
                new AdminFrame(userId).setVisible(true);
            } else {
                new OrganizerFrame(userId).setVisible(true); 
            }
            dispose();
        });
    }
}