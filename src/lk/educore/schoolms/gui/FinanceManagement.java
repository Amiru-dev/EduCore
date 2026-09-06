package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.text.DecimalFormat;

/**
 * Finance Management Panel
 * Income/expense tracking, salary processing, fee collection analytics, and pie chart
 */
public class FinanceManagement extends JPanel {

    private JTable financeTable;
    private DefaultTableModel tableModel;
    private JButton btnAddTransaction, btnRefresh, btnViewSummary;
    private JPanel chartPanel;

    public FinanceManagement() {
        initComponents();
        loadFinanceData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        
        JLabel titleLabel = new JLabel("Finance Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        
        btnAddTransaction = new JButton("Add Transaction");
        btnAddTransaction.addActionListener(e -> addTransaction());
        
        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadFinanceData());
        
        btnViewSummary = new JButton("View Summary");
        btnViewSummary.addActionListener(e -> viewSummary());

        buttonPanel.add(btnAddTransaction);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnViewSummary);
        
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Split pane for table and chart
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        
        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        
        tableModel = new DefaultTableModel(
            new Object[]{"Transaction ID", "Description", "Type", "Amount", "Date", "Method"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        financeTable = new JTable(tableModel);
        financeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        financeTable.setRowHeight(25);
        financeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(financeTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Chart panel
        chartPanel = new JPanel(new BorderLayout(10, 10));
        chartPanel.setBorder(BorderFactory.createTitledBorder("Financial Overview"));
        chartPanel.add(new JLabel("Finance Chart - Coming Soon", SwingConstants.CENTER), BorderLayout.CENTER);
        
        splitPane.setTopComponent(tablePanel);
        splitPane.setBottomComponent(chartPanel);
        splitPane.setResizeWeight(0.7);
        
        add(splitPane, BorderLayout.CENTER);
    }

    private void loadFinanceData() {
        try {
            tableModel.setRowCount(0);
            
            Connection conn = MySQL.getConnection();
            String query = "SELECT f.finance_id, f.description, tt.type_name, f.amount, f.transaction_date, pm.method_name " +
                          "FROM finance f " +
                          "JOIN transaction_type tt ON f.transaction_type_id = tt.transaction_type_id " +
                          "LEFT JOIN payment_method pm ON f.payment_method_id = pm.payment_method_id " +
                          "ORDER BY f.transaction_date DESC";
            
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            DecimalFormat df = new DecimalFormat("'Rs.' #,##0.00");
            
            while (rs.next()) {
                int financeId = rs.getInt("finance_id");
                String description = rs.getString("description");
                String type = rs.getString("type_name");
                double amount = rs.getDouble("amount");
                String date = rs.getString("transaction_date");
                String method = rs.getString("method_name");
                
                tableModel.addRow(new Object[]{financeId, description, type, df.format(amount), date, method});
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            JOptionIcon.showErrorDialog(this, "Error loading finance data: " + e.getMessage());
        }
    }

    private void addTransaction() {
        JOptionIcon.showInfoDialog(this, "Add Transaction - Coming Soon");
    }

    private void viewSummary() {
        JOptionIcon.showInfoDialog(this, "Financial Summary - Coming Soon");
    }
}