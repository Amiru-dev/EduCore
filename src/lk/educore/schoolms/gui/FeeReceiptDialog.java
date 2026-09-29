package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.DecimalFormat;

/**
 * Fee Receipt Dialog
 * Dialog for displaying fee receipt details
 */
public class FeeReceiptDialog extends JDialog {

    private int feeId;
    private JLabel lblReceiptNo, lblStudentId, lblStudentName, lblFeeType, lblAmount, lblDueDate, lblPaidDate, lblPaymentMethod;

    public FeeReceiptDialog(Frame parent, int feeId) {
        super(parent, "Fee Receipt", true);
        this.feeId = feeId;
        initComponents();
        loadReceiptDetails();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(450, 400);
        setLayout(new BorderLayout(10, 10));

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Fee Receipt");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Receipt details panel
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Receipt No
        gbc.gridx = 0;
        gbc.gridy = 0;
        detailsPanel.add(new JLabel("Receipt No:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblReceiptNo = new JLabel();
        lblReceiptNo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        detailsPanel.add(lblReceiptNo, gbc);

        // Student ID
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        detailsPanel.add(new JLabel("Student ID:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblStudentId = new JLabel();
        detailsPanel.add(lblStudentId, gbc);

        // Student Name
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        detailsPanel.add(new JLabel("Student Name:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblStudentName = new JLabel();
        detailsPanel.add(lblStudentName, gbc);

        // Fee Type
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        detailsPanel.add(new JLabel("Fee Type:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblFeeType = new JLabel();
        detailsPanel.add(lblFeeType, gbc);

        // Amount
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        detailsPanel.add(new JLabel("Amount:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblAmount = new JLabel();
        lblAmount.setFont(new Font("Segoe UI", Font.BOLD, 14));
        detailsPanel.add(lblAmount, gbc);

        // Due Date
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        detailsPanel.add(new JLabel("Due Date:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblDueDate = new JLabel();
        detailsPanel.add(lblDueDate, gbc);

        // Paid Date
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.0;
        detailsPanel.add(new JLabel("Paid Date:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblPaidDate = new JLabel();
        detailsPanel.add(lblPaidDate, gbc);

        // Payment Method
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0.0;
        detailsPanel.add(new JLabel("Payment Method:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblPaymentMethod = new JLabel();
        detailsPanel.add(lblPaymentMethod, gbc);

        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnPrint = new JButton("Print Receipt");
        btnPrint.addActionListener(e -> printReceipt());
        
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());

        buttonPanel.add(btnPrint);
        buttonPanel.add(btnClose);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadReceiptDetails() {
        try {
            ResultSet rs = MySQL.executeSearch("SELECT f.receipt_no, f.student_id, s.first_name, s.last_name, ft.fee_type_name, " +
                          "f.amount, f.due_date, f.paid_date, pm.method_name " +
                          "FROM fee f " +
                          "JOIN student s ON f.student_id = s.student_id " +
                          "JOIN fee_type ft ON f.fee_type_id = ft.fee_type_id " +
                          "LEFT JOIN payment_method pm ON f.payment_method_id = pm.payment_method_id " +
                          "WHERE f.fee_id = '" + feeId + "'");
            
            DecimalFormat df = new DecimalFormat("'Rs.' #,##0.00");
            
            if (rs.next()) {
                lblReceiptNo.setText(rs.getString("receipt_no"));
                lblStudentId.setText(rs.getString("student_id"));
                lblStudentName.setText(rs.getString("first_name") + " " + rs.getString("last_name"));
                lblFeeType.setText(rs.getString("fee_type_name"));
                lblAmount.setText(df.format(rs.getDouble("amount")));
                lblDueDate.setText(rs.getString("due_date"));
                lblPaidDate.setText(rs.getString("paid_date"));
                lblPaymentMethod.setText(rs.getString("method_name"));
            }
            
            rs.close();
            
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading receipt details: " + e.getMessage());
        }
    }

    private void printReceipt() {
        JOptionIcon.showInfoDialog(this, "Print Receipt - Coming Soon\n" +
            "This would generate a printable receipt using JasperReports.");
    }
}