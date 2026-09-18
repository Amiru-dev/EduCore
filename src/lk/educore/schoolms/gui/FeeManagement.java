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
 * Fee Management Panel
 * Main interface for fee collection, payment tracking, receipt generation, and fee analytics
 */
public class FeeManagement extends JPanel {

    private JTable feeTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<String> cmbSearchBy;
    private JButton btnAddFee, btnMarkPaid, btnViewReceipt, btnRefresh, btnAnalytics, btnSendReceipt;
    private JTabbedPane tabbedPane;

    public FeeManagement() {
        initComponents();
        loadFeeData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Fee Collection Tab
        JPanel collectionPanel = createCollectionPanel();
        tabbedPane.addTab("Fee Collection", collectionPanel);
        
        // Fee History Tab
        JPanel historyPanel = createHistoryPanel();
        tabbedPane.addTab("Fee History", historyPanel);
        
        // Analytics Tab
        JPanel analyticsPanel = createAnalyticsPanel();
        tabbedPane.addTab("Analytics", analyticsPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Title
        JLabel titleLabel = new JLabel("Fee Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.WEST);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        cmbSearchBy = new JComboBox<>(new String[]{"Student ID", "Student Name", "Fee Type", "Status"});
        txtSearch = new JTextField(20);
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchFees();
            }
        });
        
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> searchFees());
        
        searchPanel.add(new JLabel("Search by:"));
        searchPanel.add(cmbSearchBy);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        
        panel.add(searchPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createCollectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        
        btnAddFee = new JButton("Add Fee");
        btnAddFee.addActionListener(e -> addFee());
        
        btnMarkPaid = new JButton("Mark as Paid");
        btnMarkPaid.addActionListener(e -> markAsPaid());
        
        btnViewReceipt = new JButton("View Receipt");
        btnViewReceipt.addActionListener(e -> viewReceipt());
        
        btnSendReceipt = new JButton("Email Receipt");
        btnSendReceipt.addActionListener(e -> sendReceipt());
        
        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadFeeData());
        
        btnAnalytics = new JButton("View Analytics");
        btnAnalytics.addActionListener(e -> viewAnalytics());

        buttonPanel.add(btnAddFee);
        buttonPanel.add(btnMarkPaid);
        buttonPanel.add(btnViewReceipt);
        buttonPanel.add(btnSendReceipt);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnAnalytics);

        panel.add(buttonPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(
            new Object[]{"Fee ID", "Student ID", "Student Name", "Fee Type", "Amount", "Due Date", "Status", "Receipt No"}, 0
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
        
        // Add mouse listener for double-click
        feeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewReceipt();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(feeTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(new JLabel("Fee History - Coming Soon", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAnalyticsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(new JLabel("Fee Analytics - Coming Soon", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }

    private void loadFeeData() {
        try {
            tableModel.setRowCount(0);
            
            ResultSet rs = MySQL.executeSearch("SELECT f.fee_id, f.student_id, s.first_name, s.last_name, ft.fee_type_name, " +
                          "f.amount, f.due_date, f.status, f.receipt_no " +
                          "FROM fee f " +
                          "JOIN student s ON f.student_id = s.student_id " +
                          "JOIN fee_type ft ON f.fee_type_id = ft.fee_type_id " +
                          "WHERE f.status != 'Cancelled' " +
                          "ORDER BY f.due_date DESC");
            
            DecimalFormat df = new DecimalFormat("'Rs.' #,##0.00");
            
            while (rs.next()) {
                int feeId = rs.getInt("fee_id");
                String studentId = rs.getString("student_id");
                String studentName = rs.getString("first_name") + " " + rs.getString("last_name");
                String feeType = rs.getString("fee_type_name");
                double amount = rs.getDouble("amount");
                String dueDate = rs.getString("due_date");
                String status = rs.getString("status");
                String receiptNo = rs.getString("receipt_no");
                
                tableModel.addRow(new Object[]{feeId, studentId, studentName, feeType, 
                                           df.format(amount), dueDate, status, receiptNo});
            }
            
            rs.close();
            
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading fee data: " + e.getMessage());
        }
    }

    private void searchFees() {
        try {
            String searchTerm = txtSearch.getText().trim();
            String searchBy = (String) cmbSearchBy.getSelectedItem();
            
            if (searchTerm.isEmpty()) {
                loadFeeData();
                return;
            }
            
            tableModel.setRowCount(0);
            
            String query;
            
            switch (searchBy) {
                case "Student ID":
                    query = "SELECT f.fee_id, f.student_id, s.first_name, s.last_name, ft.fee_type_name, " +
                            "f.amount, f.due_date, f.status, f.receipt_no " +
                            "FROM fee f " +
                            "JOIN student s ON f.student_id = s.student_id " +
                            "JOIN fee_type ft ON f.fee_type_id = ft.fee_type_id " +
                            "WHERE f.student_id LIKE '%" + searchTerm + "%' AND f.status != 'Cancelled' " +
                            "ORDER BY f.due_date DESC";
                    break;
                case "Student Name":
                    query = "SELECT f.fee_id, f.student_id, s.first_name, s.last_name, ft.fee_type_name, " +
                            "f.amount, f.due_date, f.status, f.receipt_no " +
                            "FROM fee f " +
                            "JOIN student s ON f.student_id = s.student_id " +
                            "JOIN fee_type ft ON f.fee_type_id = ft.fee_type_id " +
                            "WHERE (s.first_name LIKE '%" + searchTerm + "%' OR s.last_name LIKE '%" + searchTerm + "%') AND f.status != 'Cancelled' " +
                            "ORDER BY f.due_date DESC";
                    break;
                case "Fee Type":
                    query = "SELECT f.fee_id, f.student_id, s.first_name, s.last_name, ft.fee_type_name, " +
                            "f.amount, f.due_date, f.status, f.receipt_no " +
                            "FROM fee f " +
                            "JOIN student s ON f.student_id = s.student_id " +
                            "JOIN fee_type ft ON f.fee_type_id = ft.fee_type_id " +
                            "WHERE ft.fee_type_name LIKE '%" + searchTerm + "%' AND f.status != 'Cancelled' " +
                            "ORDER BY f.due_date DESC";
                    break;
                case "Status":
                    query = "SELECT f.fee_id, f.student_id, s.first_name, s.last_name, ft.fee_type_name, " +
                            "f.amount, f.due_date, f.status, f.receipt_no " +
                            "FROM fee f " +
                            "JOIN student s ON f.student_id = s.student_id " +
                            "JOIN fee_type ft ON f.fee_type_id = ft.fee_type_id " +
                            "WHERE f.status LIKE '%" + searchTerm + "%' AND f.status != 'Cancelled' " +
                            "ORDER BY f.due_date DESC";
                    break;
                default:
                    query = "SELECT f.fee_id, f.student_id, s.first_name, s.last_name, ft.fee_type_name, " +
                            "f.amount, f.due_date, f.status, f.receipt_no " +
                            "FROM fee f " +
                            "JOIN student s ON f.student_id = s.student_id " +
                            "JOIN fee_type ft ON f.fee_type_id = ft.fee_type_id " +
                            "WHERE f.status != 'Cancelled' " +
                            "ORDER BY f.due_date DESC";
            }
            
            ResultSet rs = MySQL.executeSearch(query);
            
            DecimalFormat df = new DecimalFormat("'Rs.' #,##0.00");
            
            while (rs.next()) {
                int feeId = rs.getInt("fee_id");
                String studentId = rs.getString("student_id");
                String studentName = rs.getString("first_name") + " " + rs.getString("last_name");
                String feeType = rs.getString("fee_type_name");
                double amount = rs.getDouble("amount");
                String dueDate = rs.getString("due_date");
                String status = rs.getString("status");
                String receiptNo = rs.getString("receipt_no");
                
                tableModel.addRow(new Object[]{feeId, studentId, studentName, feeType, 
                                           df.format(amount), dueDate, status, receiptNo});
            }
            
            rs.close();
            
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error searching fees: " + e.getMessage());
        }
    }

    private void addFee() {
        JOptionIcon.showInfoDialog(this, "Add Fee - Coming Soon");
    }

    private void markAsPaid() {
        int selectedRow = feeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a fee to mark as paid.");
            return;
        }
        
        int feeId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String status = tableModel.getValueAt(selectedRow, 6).toString();
        
        if ("Paid".equals(status)) {
            JOptionIcon.showWarningDialog(this, "This fee is already marked as paid.");
            return;
        }
        
        int confirm = JOptionIcon.showConfirmDialog(this, "Mark this fee as paid?");
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Generate receipt number
                String receiptNo = "RCP-" + System.currentTimeMillis();
                
                String query = "UPDATE fee SET status = 'Paid', paid_date = CURDATE(), receipt_no = '" + receiptNo + "' WHERE fee_id = '" + feeId + "'";
                MySQL.executeIUD(query);
                
                JOptionIcon.showSuccessDialog(this, "Fee marked as paid. Receipt No: " + receiptNo);
                loadFeeData();
                
            } catch (Exception e) {
                JOptionIcon.showErrorDialog(this, "Error marking fee as paid: " + e.getMessage());
            }
        }
    }

    private void viewReceipt() {
        int selectedRow = feeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a fee to view receipt.");
            return;
        }
        
        int feeId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String receiptNo = tableModel.getValueAt(selectedRow, 7).toString();
        
        if (receiptNo == null || receiptNo.isEmpty()) {
            JOptionIcon.showWarningDialog(this, "No receipt available for this fee.");
            return;
        }
        
        Window parent = SwingUtilities.getWindowAncestor(this);
        new FeeReceiptDialog((Frame) parent, feeId).setVisible(true);
    }

    private void sendReceipt() {
        int selectedRow = feeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a fee to send receipt.");
            return;
        }
        
        String status = tableModel.getValueAt(selectedRow, 6).toString();
        String receiptNo = tableModel.getValueAt(selectedRow, 7).toString();
        
        if (!"Paid".equals(status) || receiptNo == null || receiptNo.isEmpty()) {
            JOptionIcon.showWarningDialog(this, "Cannot send receipt for unpaid fees.");
            return;
        }
        
        JOptionIcon.showInfoDialog(this, "Send Receipt - Coming Soon");
    }

    private void viewAnalytics() {
        tabbedPane.setSelectedIndex(2); // Switch to Analytics tab
    }
}