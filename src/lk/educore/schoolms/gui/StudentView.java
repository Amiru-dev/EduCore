package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * Student View Dialog
 * Comprehensive student details view with action buttons
 */
public class StudentView extends JDialog {

    private String studentId;
    private JButton btnEdit, btnDelete, btnEmail, btnQRCode, btnAttendance, btnFees, btnResults;

    public StudentView(String studentId) {
        this.studentId = studentId;
        initComponents();
        loadStudentSummary();
    }

    private void initComponents() {
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        
        JLabel titleLabel = new JLabel("Student View");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JLabel lblStudentId = new JLabel("Student ID: " + studentId);
        lblStudentId.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStudentId.setForeground(Color.GRAY);
        headerPanel.add(lblStudentId, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);

        // Content panel with tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Basic Info Tab
        JPanel basicInfoPanel = new StudentInformation(studentId);
        tabbedPane.addTab("Basic Info", basicInfoPanel);
        
        // Address Tab
        JPanel addressPanel = new StudentAddressView(studentId);
        tabbedPane.addTab("Address", addressPanel);
        
        // Academic Tab
        JPanel academicPanel = createAcademicPanel();
        tabbedPane.addTab("Academic", academicPanel);
        
        // Financial Tab
        JPanel financialPanel = createFinancialPanel();
        tabbedPane.addTab("Financial", financialPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Action button panel
        JPanel actionPanel = createActionPanel();
        add(actionPanel, BorderLayout.SOUTH);
    }

    private JPanel createAcademicPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("Academic Information");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        try {
            ResultSet rs = MySQL.executeSearch("SELECT g.grade_name, s.registration_date " +
                          "FROM student s " +
                          "JOIN grade g ON s.grade_id = g.grade_id " +
                          "WHERE s.student_id = '" + studentId + "'");
            
            if (rs.next()) {
                gbc.gridx = 0;
                gbc.gridy = 0;
                contentPanel.add(new JLabel("Grade:"), gbc);
                
                gbc.gridx = 1;
                gbc.weightx = 1.0;
                contentPanel.add(new JLabel(rs.getString("grade_name")), gbc);
                
                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.weightx = 0.0;
                contentPanel.add(new JLabel("Registration Date:"), gbc);
                
                gbc.gridx = 1;
                gbc.weightx = 1.0;
                contentPanel.add(new JLabel(rs.getString("registration_date")), gbc);
            }
            
            rs.close();
            
        } catch (Exception e) {
            contentPanel.add(new JLabel("Error loading academic information"), gbc);
        }
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createFinancialPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("Financial Information");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.add(new JLabel("Fee History - Coming Soon", SwingConstants.CENTER), BorderLayout.CENTER);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        btnEdit = new JButton("Edit Student");
        btnEdit.addActionListener(e -> editStudent());
        
        btnDelete = new JButton("Delete Student");
        btnDelete.addActionListener(e -> deleteStudent());
        
        btnEmail = new JButton("Send Email");
        btnEmail.addActionListener(e -> sendEmail());
        
        btnQRCode = new JButton("Generate QR");
        btnQRCode.addActionListener(e -> generateQRCode());
        
        btnAttendance = new JButton("View Attendance");
        btnAttendance.addActionListener(e -> viewAttendance());
        
        btnFees = new JButton("View Fees");
        btnFees.addActionListener(e -> viewFees());
        
        btnResults = new JButton("View Results");
        btnResults.addActionListener(e -> viewResults());

        panel.add(btnEdit);
        panel.add(btnDelete);
        panel.add(btnEmail);
        panel.add(btnQRCode);
        panel.add(btnAttendance);
        panel.add(btnFees);
        panel.add(btnResults);
        
        return panel;
    }

    private void loadStudentSummary() {
        // Load summary information if needed
    }

    private void editStudent() {
        Window parent = SwingUtilities.getWindowAncestor(this);
        new StudentEditDialog((Frame) parent, studentId).setVisible(true);
        loadStudentSummary(); 
    }

    private void deleteStudent() {
        int confirm = JOptionIcon.showConfirmDialog(this, "Are you sure you want to delete this student?");
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                MySQL.executeIUD("DELETE FROM student WHERE student_id = '" + studentId + "'");
                
                JOptionIcon.showSuccessDialog(this, "Student deleted successfully.");
                dispose();
                
            } catch (Exception e) {
                JOptionIcon.showErrorDialog(this, "Error deleting student: " + e.getMessage());
            }
        }
    }

    private void sendEmail() {
        try {
            ResultSet rs = MySQL.executeSearch("SELECT email, first_name, last_name FROM student WHERE student_id = '" + studentId + "'");
            
            if (rs.next()) {
                String email = rs.getString("email");
                String name = rs.getString("first_name") + " " + rs.getString("last_name");
                
                new StudentMailDialog((Frame) SwingUtilities.getWindowAncestor(this), email, name).setVisible(true);
            }
            
            rs.close();
            
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading student email: " + e.getMessage());
        }
    }

    private void generateQRCode() {
        try {
            ResultSet rs = MySQL.executeSearch("SELECT first_name, last_name FROM student WHERE student_id = '" + studentId + "'");
            
            if (rs.next()) {
                String name = rs.getString("first_name") + " " + rs.getString("last_name");
                lk.educore.schoolms.model.QRCodeGenerator.generateStudentQR(studentId, name);
                JOptionIcon.showSuccessDialog(this, "QR code generated successfully for " + name);
            }
            
            rs.close();
            
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error generating QR code: " + e.getMessage());
        }
    }

    private void viewAttendance() {
        JOptionIcon.showInfoDialog(this, "View Attendance - Coming Soon");
    }

    private void viewFees() {
        JOptionIcon.showInfoDialog(this, "View Fees - Coming Soon");
    }

    private void viewResults() {
        JOptionIcon.showInfoDialog(this, "View Results - Coming Soon");
    }
}