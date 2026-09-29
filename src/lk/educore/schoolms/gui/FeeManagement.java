package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;
import lk.educore.schoolms.charts.PieChart;
import lk.educore.schoolms.email.SendEmail;
import lk.educore.schoolms.exceptions.EmailException;

import javax.swing.*;
import javax.swing.table.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        return new ViewFeeRecords();
    }

    private JPanel createAnalyticsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // Statistics panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Fee Collection Statistics"));
        
        JLabel lblTotalCollected = new JLabel("Rs. 0.00", SwingConstants.CENTER);
        JLabel lblTotalPending = new JLabel("Rs. 0.00", SwingConstants.CENTER);
        JLabel lblTotalOverdue = new JLabel("Rs. 0.00", SwingConstants.CENTER);
        JLabel lblCollectionRate = new JLabel("0%", SwingConstants.CENTER);
        
        // Style the labels
        Font statsFont = new Font("Segoe UI", Font.BOLD, 16);
        lblTotalCollected.setFont(statsFont);
        lblTotalCollected.setForeground(new Color(76, 175, 80)); // Green
        lblTotalPending.setFont(statsFont);
        lblTotalPending.setForeground(new Color(255, 193, 7)); // Yellow
        lblTotalOverdue.setFont(statsFont);
        lblTotalOverdue.setForeground(new Color(244, 67, 54)); // Red
        lblCollectionRate.setFont(statsFont);
        lblCollectionRate.setForeground(new Color(33, 150, 243)); // Blue
        
        JPanel collectedPanel = createStatCard("Total Collected", lblTotalCollected);
        JPanel pendingPanel = createStatCard("Total Pending", lblTotalPending);
        JPanel overduePanel = createStatCard("Total Overdue", lblTotalOverdue);
        JPanel ratePanel = createStatCard("Collection Rate", lblCollectionRate);
        
        statsPanel.add(collectedPanel);
        statsPanel.add(pendingPanel);
        statsPanel.add(overduePanel);
        statsPanel.add(ratePanel);
        
        panel.add(statsPanel, BorderLayout.NORTH);
        
        // Fee type breakdown table
        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setBorder(BorderFactory.createTitledBorder("Fee Type Breakdown"));
        
        DefaultTableModel breakdownModel = new DefaultTableModel(
            new Object[]{"Fee Type", "Count", "Total Amount", "Avg Amount"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable breakdownTable = new JTable(breakdownModel);
        breakdownTable.setRowHeight(25);
        breakdownTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JScrollPane tableScrollPane = new JScrollPane(breakdownTable);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
        
        panel.add(tablePanel, BorderLayout.CENTER);
        
        // Refresh button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRefresh = new JButton("Refresh Analytics");
        btnRefresh.addActionListener(e -> loadAnalytics(lblTotalCollected, lblTotalPending, lblTotalOverdue, 
                                                        lblCollectionRate, breakdownModel));
        buttonPanel.add(btnRefresh);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Load initial data
        SwingUtilities.invokeLater(() -> loadAnalytics(lblTotalCollected, lblTotalPending, lblTotalOverdue, 
                                                     lblCollectionRate, breakdownModel));
        
        return panel;
    }
    
    private JPanel createStatCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(100, 116, 139));
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private void loadAnalytics(JLabel lblTotalCollected, JLabel lblTotalPending, JLabel lblTotalOverdue, 
                             JLabel lblCollectionRate, DefaultTableModel breakdownModel) {
        try {
            // Calculate statistics
            ResultSet rs = MySQL.executeSearch(
                "SELECT status, SUM(amount) as total FROM fee WHERE status != 'Cancelled' GROUP BY status"
            );
            
            double collected = 0.0;
            double pending = 0.0;
            double overdue = 0.0;
            
            while (rs.next()) {
                String status = rs.getString("status");
                double total = rs.getDouble("total");
                
                switch (status) {
                    case "Paid":
                        collected = total;
                        break;
                    case "Pending":
                        pending = total;
                        break;
                    case "Overdue":
                        overdue = total;
                        break;
                }
            }
            rs.close();
            
            DecimalFormat df = new DecimalFormat("'Rs.' #,##0.00");
            lblTotalCollected.setText(df.format(collected));
            lblTotalPending.setText(df.format(pending));
            lblTotalOverdue.setText(df.format(overdue));
            
            double totalFees = collected + pending + overdue;
            double collectionRate = totalFees > 0 ? (collected / totalFees) * 100 : 0;
            lblCollectionRate.setText(String.format("%.1f%%", collectionRate));
            
            // Load fee type breakdown
            breakdownModel.setRowCount(0);
            rs = MySQL.executeSearch(
                "SELECT ft.fee_type_name, COUNT(f.fee_id) as count, SUM(f.amount) as total, AVG(f.amount) as avg " +
                "FROM fee f " +
                "JOIN fee_type ft ON f.fee_type_id = ft.fee_type_id " +
                "WHERE f.status != 'Cancelled' " +
                "GROUP BY ft.fee_type_name " +
                "ORDER BY total DESC"
            );
            
            while (rs.next()) {
                String feeTypeName = rs.getString("fee_type_name");
                int count = rs.getInt("count");
                double total = rs.getDouble("total");
                double avg = rs.getDouble("avg");
                
                breakdownModel.addRow(new Object[]{
                    feeTypeName, 
                    count, 
                    df.format(total), 
                    df.format(avg)
                });
            }
            rs.close();
            
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading analytics: " + e.getMessage());
        }
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
        // Create dialog components
        JComboBox<String> cmbStudent = new JComboBox<>();
        JComboBox<String> cmbFeeType = new JComboBox<>();
        JTextField txtAmount = new JTextField(15);
        JDateChooser dueDateChooser = new JDateChooser();
        dueDateChooser.setDateFormatString("yyyy-MM-dd");
        
        // Load students into dropdown
        try {
            ResultSet rs = MySQL.executeSearch("SELECT student_id, first_name, last_name FROM student WHERE is_active = TRUE ORDER BY first_name, last_name");
            while (rs.next()) {
                String studentId = rs.getString("student_id");
                String studentName = rs.getString("first_name") + " " + rs.getString("last_name");
                cmbStudent.addItem(studentId + " - " + studentName);
            }
            rs.close();
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading students: " + e.getMessage());
            return;
        }
        
        // Load fee types into dropdown
        try {
            ResultSet rs = MySQL.executeSearch("SELECT fee_type_id, fee_type_name, default_amount FROM fee_type");
            while (rs.next()) {
                String feeTypeId = rs.getString("fee_type_id");
                String feeTypeName = rs.getString("fee_type_name");
                double defaultAmount = rs.getDouble("default_amount");
                cmbFeeType.addItem(feeTypeId + " - " + feeTypeName + " (Rs. " + defaultAmount + ")");
            }
            rs.close();
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading fee types: " + e.getMessage());
            return;
        }
        
        // Set default amount when fee type is selected
        cmbFeeType.addActionListener(e -> {
            String selected = (String) cmbFeeType.getSelectedItem();
            if (selected != null) {
                String[] parts = selected.split("Rs\\. ");
                if (parts.length > 1) {
                    String amountStr = parts[1].replace(")", "").trim();
                    txtAmount.setText(amountStr);
                }
            }
        });
        
        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.add(new JLabel("Student:"));
        formPanel.add(cmbStudent);
        formPanel.add(new JLabel("Fee Type:"));
        formPanel.add(cmbFeeType);
        formPanel.add(new JLabel("Amount (Rs.):"));
        formPanel.add(txtAmount);
        formPanel.add(new JLabel("Due Date:"));
        formPanel.add(dueDateChooser);
        
        // Show dialog
        int result = JOptionPane.showConfirmDialog(this, formPanel, "Add New Fee", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Validate inputs
                if (cmbStudent.getSelectedItem() == null) {
                    JOptionIcon.showWarningDialog(this, "Please select a student.");
                    return;
                }
                
                if (cmbFeeType.getSelectedItem() == null) {
                    JOptionIcon.showWarningDialog(this, "Please select a fee type.");
                    return;
                }
                
                String amountStr = txtAmount.getText().trim();
                if (amountStr.isEmpty()) {
                    JOptionIcon.showWarningDialog(this, "Please enter an amount.");
                    return;
                }
                
                double amount;
                try {
                    amount = Double.parseDouble(amountStr);
                    if (amount <= 0) {
                        JOptionIcon.showWarningDialog(this, "Amount must be greater than 0.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    JOptionIcon.showWarningDialog(this, "Please enter a valid amount.");
                    return;
                }
                
                Date dueDate = dueDateChooser.getDate();
                if (dueDate == null) {
                    JOptionIcon.showWarningDialog(this, "Please select a due date.");
                    return;
                }
                
                // Extract values
                String studentStr = cmbStudent.getSelectedItem().toString();
                String studentId = studentStr.split(" - ")[0];
                
                String feeTypeStr = cmbFeeType.getSelectedItem().toString();
                String feeTypeId = feeTypeStr.split(" - ")[0];
                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dueDateStr = sdf.format(dueDate);
                
                // Insert into database
                String query = "INSERT INTO fee (student_id, fee_type_id, amount, due_date, status) VALUES ('" + 
                              studentId + "', " + feeTypeId + ", " + amount + ", '" + dueDateStr + "', 'Pending')";
                MySQL.executeIUD(query);
                
                JOptionIcon.showSuccessDialog(this, "Fee added successfully.");
                loadFeeData();
                
            } catch (Exception e) {
                JOptionIcon.showErrorDialog(this, "Error adding fee: " + e.getMessage());
            }
        }
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
        Object receiptNoObj = tableModel.getValueAt(selectedRow, 7);
        
        if (receiptNoObj == null || receiptNoObj.toString().isEmpty()) {
            JOptionIcon.showWarningDialog(this, "No receipt available for this fee. Only paid fees have receipts.");
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
        
        Object statusObj = tableModel.getValueAt(selectedRow, 6);
        Object receiptNoObj = tableModel.getValueAt(selectedRow, 7);
        
        if (statusObj == null || !"Paid".equals(statusObj.toString()) || 
            receiptNoObj == null || receiptNoObj.toString().isEmpty()) {
            JOptionIcon.showWarningDialog(this, "Cannot send receipt for unpaid fees.");
            return;
        }
        
        int feeId = (Integer) tableModel.getValueAt(selectedRow, 0);
        
        try {
            // Fetch fee details and student email
            ResultSet rs = MySQL.executeSearch(
                "SELECT f.receipt_no, f.student_id, s.first_name, s.last_name, s.email, " +
                "ft.fee_type_name, f.amount, f.due_date, f.paid_date " +
                "FROM fee f " +
                "JOIN student s ON f.student_id = s.student_id " +
                "JOIN fee_type ft ON f.fee_type_id = ft.fee_type_id " +
                "WHERE f.fee_id = '" + feeId + "'"
            );
            
            if (!rs.next()) {
                JOptionIcon.showErrorDialog(this, "Fee details not found.");
                rs.close();
                return;
            }
            
            String studentEmail = rs.getString("email");
            if (studentEmail == null || studentEmail.trim().isEmpty()) {
                JOptionIcon.showWarningDialog(this, "Student email not found. Cannot send receipt.");
                rs.close();
                return;
            }
            
            // Build receipt email content
            DecimalFormat df = new DecimalFormat("'Rs.' #,##0.00");
            String receiptNumber = rs.getString("receipt_no");
            String studentName = rs.getString("first_name") + " " + rs.getString("last_name");
            String feeType = rs.getString("fee_type_name");
            String amount = df.format(rs.getDouble("amount"));
            String dueDate = rs.getString("due_date");
            String paidDate = rs.getString("paid_date");
            String paymentMethod = "Cash"; // Only cash payments
            
            rs.close();
            
            // Create HTML email content
            String htmlContent = "<html><head><style>" +
                "body { font-family: Arial, sans-serif; margin: 20px; }" +
                ".container { max-width: 600px; margin: 0 auto; border: 1px solid #ddd; padding: 20px; }" +
                ".header { background-color: #4CAF50; color: white; padding: 15px; text-align: center; }" +
                ".content { padding: 20px 0; }" +
                ".row { margin: 10px 0; }" +
                ".label { font-weight: bold; width: 150px; display: inline-block; }" +
                ".value { display: inline-block; }" +
                ".footer { margin-top: 20px; padding-top: 15px; border-top: 1px solid #ddd; text-align: center; color: #666; }" +
                "</style></head><body>" +
                "<div class='container'>" +
                "<div class='header'><h2>Fee Payment Receipt</h2></div>" +
                "<div class='content'>" +
                "<div class='row'><span class='label'>Receipt No:</span><span class='value'>" + receiptNumber + "</span></div>" +
                "<div class='row'><span class='label'>Student Name:</span><span class='value'>" + studentName + "</span></div>" +
                "<div class='row'><span class='label'>Fee Type:</span><span class='value'>" + feeType + "</span></div>" +
                "<div class='row'><span class='label'>Amount:</span><span class='value'>" + amount + "</span></div>" +
                "<div class='row'><span class='label'>Due Date:</span><span class='value'>" + dueDate + "</span></div>" +
                "<div class='row'><span class='label'>Paid Date:</span><span class='value'>" + paidDate + "</span></div>" +
                "<div class='row'><span class='label'>Payment Method:</span><span class='value'>" + paymentMethod + "</span></div>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>Central College Anuradhapura</p>" +
                "<p>This is an automatically generated receipt. No signature required.</p>" +
                "</div></div></body></html>";
            
            // Send email
            SendEmail.sendHTMLEmail(studentEmail, "Fee Payment Receipt - " + receiptNumber, htmlContent);
            
            JOptionIcon.showSuccessDialog(this, "Receipt sent successfully to " + studentEmail);
            
        } catch (EmailException e) {
            JOptionIcon.showErrorDialog(this, "Failed to send email: " + e.getMessage());
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error sending receipt: " + e.getMessage());
        }
    }

    private void viewAnalytics() {
        tabbedPane.setSelectedIndex(2); // Switch to Analytics tab
    }
}