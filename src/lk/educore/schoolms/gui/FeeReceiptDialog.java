package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.print.*;
import java.io.File;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;

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
                          "f.amount, f.due_date, f.paid_date " +
                          "FROM fee f " +
                          "JOIN student s ON f.student_id = s.student_id " +
                          "JOIN fee_type ft ON f.fee_type_id = ft.fee_type_id " +
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
                lblPaymentMethod.setText("Cash"); // Only cash payments
            }
            
            rs.close();
            
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading receipt details: " + e.getMessage());
        }
    }

    private void printReceipt() {
        try {
            // Try to use JasperReports if template exists
            File jasperFile = new File("reports/FeeReceipt.jasper");
            File jrxmlFile = new File("reports/FeeReceipt.jrxml");

            if (jasperFile.exists() || jrxmlFile.exists()) {
                try {
                    // Get database connection
                    Connection conn = MySQL.getConnection();

                    // Create parameters for JasperReports
                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put("fee_id", feeId);

                    JasperReport jasperReport;
                    if (jasperFile.exists()) {
                        jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
                    } else {
                        jasperReport = JasperCompileManager.compileReport(jrxmlFile.getAbsolutePath());
                    }

                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

                    // Display report in viewer
                    JFrame viewerFrame = new JFrame("Fee Receipt");
                    viewerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    viewerFrame.getContentPane().add(new net.sf.jasperreports.swing.JRViewer(jasperPrint));
                    viewerFrame.pack();
                    viewerFrame.setLocationRelativeTo(this);
                    viewerFrame.setVisible(true);

                    conn.close();
                    return;

                } catch (Exception e) {
                    System.out.println("JasperReports error: " + e.getMessage());
                    e.printStackTrace();
                    JOptionIcon.showErrorDialog(this, "JasperReports error: " + e.getMessage());
                    // Fall back to simple receipt if JasperReports fails
                }
            }

            // Fallback to simple printable receipt dialog
            ResultSet rs = MySQL.executeSearch("SELECT f.receipt_no, f.student_id, s.first_name, s.last_name, ft.fee_type_name, " +
                          "f.amount, f.due_date, f.paid_date " +
                          "FROM fee f " +
                          "JOIN student s ON f.student_id = s.student_id " +
                          "JOIN fee_type ft ON f.fee_type_id = ft.fee_type_id " +
                          "WHERE f.fee_id = '" + feeId + "'");

            if (!rs.next()) {
                JOptionIcon.showErrorDialog(this, "Error loading receipt details for printing.");
                rs.close();
                return;
            }

            DecimalFormat df = new DecimalFormat("'Rs.' #,##0.00");

            String receiptNo = rs.getString("receipt_no");
            String studentId = rs.getString("student_id");
            String studentName = rs.getString("first_name") + " " + rs.getString("last_name");
            String feeType = rs.getString("fee_type_name");
            String amount = df.format(rs.getDouble("amount"));
            String dueDate = rs.getString("due_date");
            String paidDate = rs.getString("paid_date");
            String paymentMethod = "Cash"; // Only cash payments

            rs.close();

            printSimpleReceipt(receiptNo, studentId, studentName, feeType, amount, dueDate, paidDate, paymentMethod);

        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error printing receipt: " + e.getMessage());
        }
    }
    
    private void printSimpleReceipt(String receiptNo, String studentId, String studentName, String feeType, 
                                   String amount, String dueDate, String paidDate, String paymentMethod) {
        // Create a simple printable receipt dialog
        JDialog printDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Print Receipt", true);
        printDialog.setSize(400, 500);
        printDialog.setLayout(new BorderLayout(10, 10));
        
        JPanel receiptPanel = new JPanel(new GridBagLayout());
        receiptPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("FEE RECEIPT", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        receiptPanel.add(titleLabel, gbc);
        
        // School name
        gbc.gridy = 1;
        JLabel schoolLabel = new JLabel("Central College Anuradhapura", SwingConstants.CENTER);
        schoolLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        receiptPanel.add(schoolLabel, gbc);
        
        // Separator
        gbc.gridy = 2;
        receiptPanel.add(new JLabel("--------------------------------------------------", SwingConstants.CENTER), gbc);
        
        // Receipt details
        gbc.gridwidth = 1;
        gbc.gridy = 3;
        receiptPanel.add(new JLabel("Receipt No:"), gbc);
        gbc.gridx = 1;
        receiptPanel.add(new JLabel(receiptNo), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        receiptPanel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        receiptPanel.add(new JLabel(studentId), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        receiptPanel.add(new JLabel("Student Name:"), gbc);
        gbc.gridx = 1;
        receiptPanel.add(new JLabel(studentName), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        receiptPanel.add(new JLabel("Fee Type:"), gbc);
        gbc.gridx = 1;
        receiptPanel.add(new JLabel(feeType), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        receiptPanel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1;
        JLabel amountLabel = new JLabel(amount);
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        receiptPanel.add(amountLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 8;
        receiptPanel.add(new JLabel("Due Date:"), gbc);
        gbc.gridx = 1;
        receiptPanel.add(new JLabel(dueDate), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 9;
        receiptPanel.add(new JLabel("Paid Date:"), gbc);
        gbc.gridx = 1;
        receiptPanel.add(new JLabel(paidDate), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 10;
        receiptPanel.add(new JLabel("Payment Method:"), gbc);
        gbc.gridx = 1;
        receiptPanel.add(new JLabel("Cash"), gbc);
        
        // Separator
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        receiptPanel.add(new JLabel("--------------------------------------------------", SwingConstants.CENTER), gbc);
        
        // Footer
        gbc.gridy = 12;
        JLabel footerLabel = new JLabel("Thank you for your payment!", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        receiptPanel.add(footerLabel, gbc);
        
        printDialog.add(receiptPanel, BorderLayout.CENTER);
        
        // Print button
        JButton btnPrint = new JButton("Print");
        btnPrint.addActionListener(e -> {
            // Print the dialog content
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setJobName("Fee Receipt - " + receiptNo);
            
            if (job.printDialog()) {
                try {
                    job.setPrintable(new Printable() {
                        @Override
                        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                            if (pageIndex > 0) {
                                return NO_SUCH_PAGE;
                            }
                            
                            Graphics2D g2d = (Graphics2D) graphics;
                            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                            
                            receiptPanel.print(g2d);
                            return PAGE_EXISTS;
                        }
                    });
                    
                    job.print();
                    JOptionIcon.showSuccessDialog(printDialog, "Receipt sent to printer.");
                    printDialog.dispose();
                    
                } catch (PrinterException ex) {
                    JOptionIcon.showErrorDialog(printDialog, "Error printing: " + ex.getMessage());
                }
            }
        });
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> printDialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(btnPrint);
        buttonPanel.add(btnCancel);
        
        printDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        printDialog.setLocationRelativeTo(this);
        printDialog.setVisible(true);
    }
}