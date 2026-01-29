package auth;

import javax.swing.*;
import java.awt.*;
import Model.User;
import dao.UserDAO;

public class ResetPasswordFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtNewPass;
    private JButton btnReset;
    private JButton btnBack;

    public ResetPasswordFrame() {
        setTitle("Reset Password");
        setSize(400, 300); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        txtUsername = new JTextField(20);
        txtNewPass = new JPasswordField(20);
        
        btnReset = new JButton("Reset Password");
        btnBack = new JButton("Back");

        Dimension btnSize = new Dimension(140, 35);
        btnReset.setPreferredSize(btnSize);
        btnBack.setPreferredSize(btnSize);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel titleLabel = new JLabel("Reset Password", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(2, 2, 15, 15));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        fieldsPanel.add(new JLabel("Username:"));
        fieldsPanel.add(txtUsername);
        fieldsPanel.add(new JLabel("New Password:"));
        fieldsPanel.add(txtNewPass);

        mainPanel.add(fieldsPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonsPanel.add(btnReset);
        buttonsPanel.add(btnBack);

        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(mainPanel);

        btnBack.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        btnReset.addActionListener(e -> handleReset());
    }

    private void handleReset() {
        String username = txtUsername.getText().trim();
        String newpass = new String(txtNewPass.getPassword()).trim();

        if (username.isEmpty() || newpass.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, 
                "Please fill in all fields.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        if (newpass.length() < 4) {
            JOptionPane.showMessageDialog(
                this,
                "Password must be at least 4 characters long.",
                "Weak Password",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            UserDAO userDAO = new UserDAO();
            User user = userDAO.getUserByUsername(username);
            
            if (user != null) {
                userDAO.updatePassword(user.getId(), newpass);
                JOptionPane.showMessageDialog(
                    this,
                    "Password updated successfully for user: " + username,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                dispose();
                new LoginFrame().setVisible(true);
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
                "Error updating password: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}