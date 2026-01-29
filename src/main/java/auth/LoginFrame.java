package auth;

import Model.User;
import admin.AdminFrame;
import attendee.AttendeeFrame;
import organizer.OrganizerFrame;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnSignUp;
    private JButton btnForgotPassword;
    private DatabaseAuthService authService;

    public LoginFrame() {
        setTitle("Login");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        authService = new DatabaseAuthService();

        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);
        btnLogin = new JButton("Login");
        btnSignUp = new JButton("Sign Up");
        btnForgotPassword = new JButton("Forgot Password?");

        Dimension btnSize = new Dimension(140, 35);
        btnLogin.setPreferredSize(btnSize);
        btnSignUp.setPreferredSize(btnSize);
        
        Dimension forgotBtnSize = new Dimension(180, 35);
        btnForgotPassword.setPreferredSize(forgotBtnSize);

        JPanel mainPanel = new JPanel(new GridLayout(5, 1, 15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Login to Ticket Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel);

        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        usernamePanel.add(new JLabel("Username:"));
        usernamePanel.add(txtUsername);
        mainPanel.add(usernamePanel);

        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        passwordPanel.add(new JLabel("Password:"));
        passwordPanel.add(txtPassword);
        mainPanel.add(passwordPanel);

        JPanel loginButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        loginButtonsPanel.add(btnLogin);
        loginButtonsPanel.add(btnSignUp);
        mainPanel.add(loginButtonsPanel);

        JPanel forgotPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        forgotPanel.add(btnForgotPassword);
        mainPanel.add(forgotPanel);

        add(mainPanel);

        btnLogin.addActionListener(e -> handleLogin());
        btnSignUp.addActionListener(e -> {
            new SignUpFrame().setVisible(true);
            dispose();
        });
        btnForgotPassword.addActionListener(e -> {
            new ResetPasswordFrame().setVisible(true);
            dispose();
        });
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please fill in all fields.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            User user = authService.login(username, password);

            if (user == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "Invalid username or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            JOptionPane.showMessageDialog(
                this,
                "Welcome " + user.getUsername() + "!",
                "Login Successful",
                JOptionPane.INFORMATION_MESSAGE
            );

            switch (user.getRole().toUpperCase()) {
                case "ADMIN":
                    new AdminFrame(user.getId()).setVisible(true);
                    break;
                case "ORGANIZER":
                    new OrganizerFrame(user.getId()).setVisible(true);
                    break;
                case "ATTENDEE":
                    new AttendeeFrame(user.getId()).setVisible(true);
                    break;
                default:
                    JOptionPane.showMessageDialog(
                        this,
                        "Unknown role!",
                        "Role Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
            }
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "An unexpected error occurred.\n" + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}