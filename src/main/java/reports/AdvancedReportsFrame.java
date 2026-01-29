package reports;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import admin.SystemReportsFrame;
import service.ReportService;

public class AdvancedReportsFrame extends JFrame {
    private JTable table;
    private JComboBox<String> reportTypeCombo;
    private ReportService reportService;
    private int userId;

    public AdvancedReportsFrame(int userId) {
        this.userId = userId;
        reportService = new ReportService();
        
        setTitle("Advanced Reports");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel headerLabel = new JLabel("Advanced Event Reports", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        selectionPanel.add(new JLabel("Select Report:"));
        reportTypeCombo = new JComboBox<>(new String[]{
            "Most Popular Categories",
            "Capacity Utilization", 
            "Most Registered Events"
        });
        reportTypeCombo.setPreferredSize(new Dimension(200, 25));
        selectionPanel.add(reportTypeCombo);
        
        JButton generateBtn = new JButton("Generate");
        generateBtn.setPreferredSize(new Dimension(100, 25));
        selectionPanel.add(generateBtn);
        
        mainPanel.add(selectionPanel, BorderLayout.NORTH);

        String[] columns = {"Item", "Value", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(model);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(350);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton backBtn = new JButton("Back");
        backBtn.setPreferredSize(new Dimension(100, 30));
        buttonPanel.add(backBtn);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        generateBtn.addActionListener(e -> generateReport());
        reportTypeCombo.addActionListener(e -> generateReport());
        backBtn.addActionListener(e -> {
            dispose();
            new SystemReportsFrame(userId).setVisible(true);
        });
        
        generateReport();
    }
    
    private void generateReport() {
        try {
            String selectedReport = (String) reportTypeCombo.getSelectedItem();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            
            model.setRowCount(0);
            
            setTitle("Advanced Reports - " + selectedReport);
            
            if (selectedReport.equals("Most Popular Categories")) {
                loadPopularCategories(model);
            } 
            else if (selectedReport.equals("Capacity Utilization")) {
                loadCapacityUtilization(model);
            } 
            else if (selectedReport.equals("Most Registered Events")) {
                loadMostRegisteredEvents(model);
            }
            
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, 
                    "No data available for the selected report.",
                    "No Data", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error generating report: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadPopularCategories(DefaultTableModel model) throws Exception {
        List<Object[]> categories = reportService.getMostPopularCategories();
        
        for (Object[] report : categories) {
            String categoryName = (String) report[0];
            Long count = (Long) report[1];
            Double percentage = (Double) report[2];
            
            model.addRow(new Object[]{
                categoryName,
                count + " events",
                String.format("%.1f%%", percentage)
            });
        }
    }
    
    private void loadCapacityUtilization(DefaultTableModel model) throws Exception {
        List<Object[]> utilization = reportService.getCapacityUtilization();
        
        for (Object[] report : utilization) {
            String eventName = (String) report[0];
            Double utilizationRate = (Double) report[1];
            String status = getUtilizationStatus(utilizationRate);
            
            model.addRow(new Object[]{
                eventName,
                String.format("%.1f%%", utilizationRate),
                status
            });
        }
    }
    
    private void loadMostRegisteredEvents(DefaultTableModel model) throws Exception {
        List<Object[]> events = reportService.getMostRegisteredEvents();
        
        for (Object[] report : events) {
            String eventName = (String) report[0];
            Integer registrationCount = (Integer) report[1];
            Double registrationRate = (Double) report[2];
            String popularity = getPopularityStatus(registrationRate);
            
            model.addRow(new Object[]{
                eventName,
                String.format("%.1f%% registration rate (%d registrations)", 
                    registrationRate, registrationCount),
                popularity
            });
        }
    }
    
    private String getUtilizationStatus(double utilization) {
        if (utilization >= 80) {
            return "High";
        } else if (utilization >= 50) {
            return "Medium";
        } else {
            return "Low";
        }
    }
    
    private String getPopularityStatus(double registrationRate) {
        if (registrationRate >= 80) {
            return "Very Popular";
        } else if (registrationRate >= 50) {
            return "Popular";
        } else if (registrationRate >= 20) {
            return "Average";
        } else {
            return "Low";
        }
    }
}