package admin;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import Model.User;
import dao.UserDAO;

public class ViewUsersFrame extends JFrame {

    private JTable usersTable;
    private JButton btnBack;
    private UserDAO userDAO;
    private int currentUserId;

    public ViewUsersFrame(int currentUserId) {
        this.currentUserId = currentUserId;

        setTitle("Users List");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        userDAO = new UserDAO();

        String[] columnNames = {"User ID", "Username", "Role"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        usersTable = new JTable(model);
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        usersTable.getColumnModel().getColumn(0).setPreferredWidth(60);   
        usersTable.getColumnModel().getColumn(1).setPreferredWidth(200);  
        usersTable.getColumnModel().getColumn(2).setPreferredWidth(120);  

        JScrollPane scrollPane = new JScrollPane(usersTable);
        scrollPane.setPreferredSize(new Dimension(480, 300));

        btnBack = new JButton("Back");
        btnBack.setPreferredSize(new Dimension(100, 30));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("System Users", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        bottomPanel.add(btnBack);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        loadUsersData();

        btnBack.addActionListener(e -> {
            dispose();
            new ManageUsersFrame(currentUserId).setVisible(true);
        });
    }

    private void loadUsersData() {
        try {
            List<User> users = userDAO.getAllUsers();
            DefaultTableModel model = (DefaultTableModel) usersTable.getModel();
            model.setRowCount(0); 

            if (users.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "No users found in the system.",
                    "No Data",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            for (User user : users) {
                model.addRow(new Object[]{
                    user.getId(),
                    user.getUsername(),
                    user.getRole()
                });
            }

            setTitle("Users List - Total: " + users.size() + " users");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Error loading users: " + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}