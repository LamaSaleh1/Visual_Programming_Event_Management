package admin;

import javax.swing.*;
import java.awt.*;

public class ManageUsersFrame extends JFrame {

    private JButton btnViewUsers;
    private JButton btnDeleteUser;
    private JButton btnBack;
    private int userId;

    public ManageUsersFrame(int userId) {
        this.userId = userId;

        setTitle("Manage Users");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        btnViewUsers = new JButton("View Users");
        btnDeleteUser = new JButton("Delete User");
        btnBack = new JButton("Back");

        Dimension btnSize = new Dimension(140, 35);
        btnViewUsers.setPreferredSize(btnSize);
        btnDeleteUser.setPreferredSize(btnSize);
        btnBack.setPreferredSize(btnSize);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("User Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel buttonsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        
        buttonsPanel.add(btnViewUsers);
        buttonsPanel.add(btnDeleteUser);
        buttonsPanel.add(btnBack);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(buttonsPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);

        btnViewUsers.addActionListener(e -> {
            new ViewUsersFrame(userId).setVisible(true);
            dispose();
        });

        btnDeleteUser.addActionListener(e -> {
            new DeleteUserFrame(userId).setVisible(true);
            dispose();
        });

        btnBack.addActionListener(e -> {
            new AdminFrame(userId).setVisible(true);
            dispose();
        });
    }    
}