package admin;

import javax.swing.*;
import java.awt.*;
import Model.User;
import dao.UserDAO;

public class DeleteUserFrame extends JFrame {

    private JTextField txtUsername;
    private JButton btnDelete;
    private JButton btnBack;
    private UserDAO userDAO;
    private int currentUserId;

    public DeleteUserFrame(int currentUserId) {
        this.currentUserId = currentUserId;

        setTitle("Delete User");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        userDAO = new UserDAO();

        txtUsername = new JTextField(20);
        btnDelete = new JButton("Delete");
        btnBack = new JButton("Back");

        Dimension btnSize = new Dimension(120, 30);
        btnDelete.setPreferredSize(btnSize);
        btnBack.setPreferredSize(btnSize);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(txtUsername);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnBack);

        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        btnBack.addActionListener(e -> {
            dispose();
            new ManageUsersFrame(currentUserId).setVisible(true);
        });

        btnDelete.addActionListener(e -> handleDelete());
    }

    private void handleDelete() {
        String username = txtUsername.getText().trim();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, 
                "Please enter a username",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            User user = userDAO.getUserByUsername(username);
            
            if (user != null) {
                int confirmation = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete user:\n" +
                    "Username: " + user.getUsername() + "\n" +
                    "Role: " + user.getRole() + "\n\n" +
                    "This action cannot be undone!",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );

                if (confirmation == JOptionPane.YES_OPTION) {
                    if (user.getRole().equalsIgnoreCase("ADMIN")) {
                        int adminCount = userDAO.getAdminCount();
                        if (adminCount <= 1) {
                            JOptionPane.showMessageDialog(
                                this,
                                "Cannot delete the only admin user!\n" +
                                "There must be at least one admin in the system.",
                                "Deletion Blocked",
                                JOptionPane.ERROR_MESSAGE
                            );
                            return;
                        }
                    }

                    userDAO.deleteUser(user.getId());
                    
                    JOptionPane.showMessageDialog(
                        this,
                        "User '" + username + "' has been deleted successfully!",
                        "Deletion Successful",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    
                    txtUsername.setText("");
                    dispose();
                    new ManageUsersFrame(currentUserId).setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "User '" + username + "' not found!",
                    "User Not Found",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Error deleting user: " + ex.getMessage(),
                "Deletion Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}