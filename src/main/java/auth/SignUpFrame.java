package auth;

import Model.User;
import dao.UserDAO;
import javax.swing.*;
import java.awt.*;

public class SignUpFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JComboBox<String> roleBox;
    private JButton btnRegister;
    private JButton btnBack;

    public SignUpFrame() {
        setTitle("Sign Up");
        setSize(450, 400); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);
        txtConfirmPassword = new JPasswordField(20);

        String[] roles = {"ADMIN", "ORGANIZER", "ATTENDEE"};
        roleBox = new JComboBox<>(roles);

        btnRegister = new JButton("Register");
        btnBack = new JButton("Back");

        Dimension btnSize = new Dimension(120, 35);
        btnRegister.setPreferredSize(btnSize);
        btnBack.setPreferredSize(btnSize);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel titleLabel = new JLabel("Create New Account", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        fieldsPanel.add(new JLabel("Username:"));
        fieldsPanel.add(txtUsername);
        fieldsPanel.add(new JLabel("Password:"));
        fieldsPanel.add(txtPassword);
        fieldsPanel.add(new JLabel("Confirm Password:"));
        fieldsPanel.add(txtConfirmPassword);
        fieldsPanel.add(new JLabel("Role:"));
        fieldsPanel.add(roleBox);

        mainPanel.add(fieldsPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonsPanel.add(btnBack);
        buttonsPanel.add(btnRegister);

        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(mainPanel);

        btnRegister.addActionListener(e -> handleRegister());

        btnBack.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
    }

    private void handleRegister() {
        String username = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();
        String confirm = new String(txtConfirmPassword.getPassword()).trim();
        String role = roleBox.getSelectedItem().toString();

        if (username.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please fill all fields.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        if (pass.length() < 4) {
            JOptionPane.showMessageDialog(
                this,
                "Password must be at least 4 characters long.",
                "Weak Password",
                JOptionPane.WARNING_MESSAGE
            );
            txtPassword.requestFocus();
            return;
        }
        
        if (!pass.equals(confirm)) {
            JOptionPane.showMessageDialog(
                this,
                "Passwords do not match!",
                "Password Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            
            User newUser = new User(0, username, pass, role);
            UserDAO userDAO = new UserDAO();
            userDAO.addUser(newUser);
            JOptionPane.showMessageDialog(
                this,
                "User Registered Successfully!\n\n" +
                "Username: " + username + "\n" +
                "Role: " + role + "\n\n" +
                "You can now login with your new account.",
                "Registration Successful",
                JOptionPane.INFORMATION_MESSAGE
            );

            dispose();
            new LoginFrame().setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Error creating user: " + ex.getMessage() + 
                "\n\nPlease try again with a different username.",
                "Registration Failed",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}