package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DecimalFormat;

/**
 * View Fee Records
 * Panel for viewing historical fee records with search and filter functionality
 */
public class ViewFeeRecords extends JPanel {

    private JTable feeTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<String> cmbFilterBy;
    private JComboBox<String> cmbStatusFilter;
    private JButton btnSearch, btnRefresh, btnExport;

    public ViewFeeRecords() {
        initComponents();
        loadFeeRecords();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Filter panel
        JPanel filterPanel = createFilterPanel();
        add(filterPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(
            new Object[]{"Receipt No", "Student ID", "Student Name", "Fee Type", "Amount", "Paid Date", "Payment Method"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        feeTable = new JTable(tableModel);
        feeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        feeTable.setRowHeight(25);
        feeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(feeTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadFeeRecords());
        
        btnExport = new JButton("Export to Excel");
        btnExport.addActionListener(e -> exportToExcel());

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnExport);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Search & Filter"));

        // Search row
        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        
        cmbFilterBy = new JComboBox<>(new String[]{"Student ID", "Student Name", "Receipt No"});
        txtSearch = new JTextField(20);
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchRecords();
            }
        });
        
        btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> searchRecords());
        
        searchRow.add(new JLabel("Search by:"));
        searchRow.add(cmbFilterBy);
        searchRow.add(txtSearch);
        searchRow.add(btnSearch);

        // Filter row
        JPanel filterRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        
        cmbStatusFilter = new JComboBox<>(new String[]{"All", "Paid", "Pending", "Overdue"});
        cmbStatusFilter.addActionListener(e -> searchRecords());
        
        filterRow.add(new JLabel("Status:"));
        filterRow.add(cmbStatusFilter);

        // Combine rows
        JPanel rowsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        rowsPanel.add(searchRow);
        rowsPanel.add(filterRow);
        
        panel.add(rowsPanel, BorderLayout.CENTER);

        return panel;
    }

    private void loadFeeRecords() {
        try {
            tableModel.setRowCount(0);
            
            Connection conn = MySQL.getConnection();
            String query = "SELECT f.receipt_no, f.student_id, s.first_name, s.last_name, ft.fee_type_name, " +
                          "f.amount, f.paid_date, pm.method_name " +
                          "FROM fee f " +
                          "JOIN student s ON f.student_id = s.student_id " +
                          "JOIN fee_type ft ON f.fee_type_id = ft.fee_type_id " +
                          "LEFT JOIN payment_method pm ON f.payment_method_id = pm.payment_method_id " +
                          "WHERE f.status = 'Paid' " +
                          "ORDER BY f.paid_date DESC";
            
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            DecimalFormat df = new DecimalFormat("Rs. #,##0.00");
            
            while (rs.next()) {
                String receiptNo = rs.getString("receipt_no");
                String studentId = rs.getString("student_id");
                String studentName = rs.getString("first_name") + " " + rs.getString("last_name");
                String feeType = rs.getString("fee_type_name");
                double amount = rs.getDouble("amount");
                String paidDate = rs.getString("paid_date");
                String paymentMethod = rs.getString("method_name");
                
                tableModel.addRow(new Object[]{receiptNo, studentId, studentName, feeType, 
                                           df.format(amount), paidDate, paymentMethod});
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            JOptionIcon.showErrorDialog(this, "Error loading fee records: " + e.getMessage());
        }
    }

    private void searchRecords() {
        try {
            String searchTerm = txtSearch.getText().trim();
            String filterBy = (String) cmbFilterBy.getSelectedItem();
            String statusFilter = (String) cmbStatusFilter.getSelectedItem();
            
            tableModel.setRowCount(0);
            
            Connection conn = MySQL.getConnection();
            StringBuilder queryBuilder = new StringBuilder(
                "SELECT f.receipt_no, f.student_id, s.first_name, s.last_name, ft.fee_type_name, " +
                "f.amount, f.paid_date, pm.method_name " +
                "FROM fee f " +
                "JOIN student s ON f.student_id = s.student_id " +
                "JOIN fee_type ft ON f.fee_type_id = ft.fee_type_id " +
                "LEFT JOIN payment_method pm ON f.payment_method_id = pm.payment_method_id " +
                "WHERE f.status = 'Paid' "
            );
            
            if (!searchTerm.isEmpty()) {
                switch (filterBy) {
                    case "Student ID":
                        queryBuilder.append("AND f.student_id LIKE ? ");
                        break;
                    case "Student Name":
                        queryBuilder.append("AND (s.first_name LIKE ? OR s.last_name LIKE ?) ");
                        break;
                    case "Receipt No":
                        queryBuilder.append("AND f.receipt_no LIKE ? ");
                        break;
                }
            }
            
            if (!"All".equals(statusFilter)) {
                queryBuilder.append("AND f.status = ? ");
            }
            
            queryBuilder.append("ORDER BY f.paid_date DESC");
            
            PreparedStatement stmt = conn.prepareStatement(queryBuilder.toString());
            
            int paramIndex = 1;
            if (!searchTerm.isEmpty()) {
                if (filterBy.equals("Student Name")) {
                    stmt.setString(paramIndex++, "%" + searchTerm + "%");
                    stmt.setString(paramIndex++, "%" + searchTerm + "%");
                } else {
                    stmt.setString(paramIndex++, "%" + searchTerm + "%");
                }
            }
            
            if (!"All".equals(statusFilter)) {
                stmt.setString(paramIndex++, statusFilter);
            }
            
            ResultSet rs = stmt.executeQuery();
            
            DecimalFormat df = new DecimalFormat("Rs. #,##0.00");
            
            while (rs.next()) {
                String receiptNo = rs.getString("receipt_no");
                String studentId = rs.getString("student_id");
                String studentName = rs.getString("first_name") + " " + rs.getString("last_name");
                String feeType = rs.getString("fee_type_name");
                double amount = rs.getDouble("amount");
                String paidDate = rs.getString("paid_date");
                String paymentMethod = rs.getString("method_name");
                
                tableModel.addRow(new Object[]{receiptNo, studentId, studentName, feeType, 
                                           df.format(amount), paidDate, paymentMethod});
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            JOptionIcon.showErrorDialog(this, "Error searching fee records: " + e.getMessage());
        }
    }

    private void exportToExcel() {
        JOptionIcon.showInfoDialog(this, "Export to Excel - Coming Soon");
    }
}