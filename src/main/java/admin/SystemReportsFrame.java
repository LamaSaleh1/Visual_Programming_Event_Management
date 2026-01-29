package admin;

import javax.swing.*;
import java.awt.*;
import reports.*;

public class SystemReportsFrame extends JFrame {

    private JButton btnTotalUsers;
    private JButton btnTotalEvents;
    private JButton btnTotalRegs;
    private JButton btnFullReport;
    private JButton btnAdvancedReports;
    private JButton btnBack;
    private int userId;
    
    public SystemReportsFrame(int userId) {
        this.userId = userId;

        setTitle("System Reports");
        setSize(350, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        btnTotalUsers = new JButton("Total Users");
        btnTotalEvents = new JButton("Total Events");
        btnTotalRegs = new JButton("Total Registrations");
        btnFullReport = new JButton("Generate Full Report");
        btnAdvancedReports = new JButton("Advanced Reports");
        btnBack = new JButton("Back");

        Dimension mainBtnSize = new Dimension(180, 35);
        btnTotalUsers.setPreferredSize(mainBtnSize);
        btnTotalEvents.setPreferredSize(mainBtnSize);
        btnTotalRegs.setPreferredSize(mainBtnSize);
        btnFullReport.setPreferredSize(mainBtnSize);
        btnAdvancedReports.setPreferredSize(mainBtnSize);
        btnBack.setPreferredSize(new Dimension(120, 30));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("System Reports", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel buttonsPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        
        buttonsPanel.add(btnTotalUsers);
        buttonsPanel.add(btnTotalEvents);
        buttonsPanel.add(btnTotalRegs);
        buttonsPanel.add(btnFullReport);
        buttonsPanel.add(btnAdvancedReports);

        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        backPanel.add(btnBack);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(buttonsPanel, BorderLayout.CENTER);
        mainPanel.add(backPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        btnTotalUsers.addActionListener(e -> {
            new TotalUsersFrame(userId).setVisible(true);
            dispose();
        });

        btnTotalEvents.addActionListener(e -> {
            new TotalEventsFrame(userId).setVisible(true);
            dispose();
        });

        btnTotalRegs.addActionListener(e -> {
            new TotalRegistrationsFrame(userId).setVisible(true);
            dispose();
        });

        btnFullReport.addActionListener(e -> {
            new FullReportFrame(userId).setVisible(true);
            dispose();
        });

        btnAdvancedReports.addActionListener(e -> {
            new AdvancedReportsFrame(userId).setVisible(true);
            dispose();
        });

        btnBack.addActionListener(e -> {
            new AdminFrame(userId).setVisible(true);
            dispose();
        });
    }
}