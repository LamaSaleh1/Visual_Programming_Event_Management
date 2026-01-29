package attendee;

import javax.swing.*;
import java.awt.*;
import auth.LoginFrame;

public class AttendeeFrame extends JFrame {

    private JButton btnViewEvents;
    private JButton btnRegister;
    private JButton btnTickets;
    private JButton btnReports;
    private JButton btnLogout;
    private int currentUserId;
    
    public AttendeeFrame(int userId) {
        this.currentUserId = userId;

        setTitle("Attendee Dashboard");
        setSize(380, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Attendee Panel", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        btnViewEvents = new JButton("View Events");
        btnRegister = new JButton("Register in Event");
        btnTickets = new JButton("My Tickets");
        btnReports = new JButton("My Reports");
        btnLogout = new JButton("Logout");

        Dimension btnSize = new Dimension(160, 35);
        btnViewEvents.setPreferredSize(btnSize);
        btnRegister.setPreferredSize(btnSize);
        btnTickets.setPreferredSize(btnSize);
        btnReports.setPreferredSize(btnSize);
        btnLogout.setPreferredSize(new Dimension(120, 30));

        centerPanel.add(btnViewEvents);
        centerPanel.add(btnRegister);
        centerPanel.add(btnTickets);
        centerPanel.add(btnReports);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        bottomPanel.add(btnLogout);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        btnViewEvents.addActionListener(e -> {
            new ViewEventsFrame(currentUserId).setVisible(true);
            dispose();
        });
        
        btnRegister.addActionListener(e -> {
            new RegisterEventFrame(currentUserId).setVisible(true);
            dispose();
        });

        btnTickets.addActionListener(e -> {
            new TicketsFrame(currentUserId).setVisible(true);
            dispose();
        });

        btnReports.addActionListener(e -> {
            new AttendeeReportsFrame(currentUserId).setVisible(true);
            dispose();
        });

        btnLogout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }
}