package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * Student Information Panel
 * Displays detailed student information in a read-only format
 */
public class StudentInformation extends JPanel {

    private String studentId;
    private JLabel lblStudentId, lblName, lblNIC, lblEmail, lblPhone, lblAddress, lblGrade, lblGuardian, lblGuardianPhone, lblRegistrationDate;

    public StudentInformation(String studentId) {
        this.studentId = studentId;
        initComponents();
        loadStudentInfo();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Student Information");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Info panel
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Student ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        infoPanel.add(new JLabel("Student ID:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblStudentId = new JLabel();
        lblStudentId.setFont(new Font("Segoe UI", Font.BOLD, 12));
        infoPanel.add(lblStudentId, gbc);

        // Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        infoPanel.add(new JLabel("Full Name:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblName = new JLabel();
        infoPanel.add(lblName, gbc);

        // NIC
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        infoPanel.add(new JLabel("NIC:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblNIC = new JLabel();
        infoPanel.add(lblNIC, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        infoPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblEmail = new JLabel();
        infoPanel.add(lblEmail, gbc);

        // Phone
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        infoPanel.add(new JLabel("Phone:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblPhone = new JLabel();
        infoPanel.add(lblPhone, gbc);

        // Address
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        infoPanel.add(new JLabel("Address:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblAddress = new JLabel();
        infoPanel.add(lblAddress, gbc);

        // Grade
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.0;
        infoPanel.add(new JLabel("Grade:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblGrade = new JLabel();
        infoPanel.add(lblGrade, gbc);

        // Guardian
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0.0;
        infoPanel.add(new JLabel("Guardian:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblGuardian = new JLabel();
        infoPanel.add(lblGuardian, gbc);

        // Guardian Phone
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.weightx = 0.0;
        infoPanel.add(new JLabel("Guardian Phone:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblGuardianPhone = new JLabel();
        infoPanel.add(lblGuardianPhone, gbc);

        // Registration Date
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.weightx = 0.0;
        infoPanel.add(new JLabel("Registration Date:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblRegistrationDate = new JLabel();
        infoPanel.add(lblRegistrationDate, gbc);

        add(infoPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JDialog) {
                ((JDialog) window).dispose();
            }
        });

        buttonPanel.add(btnClose);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadStudentInfo() {
        try {
            ResultSet rs = MySQL.executeSearch("SELECT s.student_id, s.first_name, s.last_name, s.nic, s.email, s.phone, s.address, " +
                          "g.grade_name, gu.first_name as guardian_first, gu.last_name as guardian_last, " +
                          "gu.phone as guardian_phone, s.registration_date " +
                          "FROM student s " +
                          "JOIN grade g ON s.grade_id = g.grade_id " +
                          "JOIN guardian gu ON s.guardian_id = gu.guardian_id " +
                          "WHERE s.student_id = '" + studentId + "'");
            
            if (rs.next()) {
                lblStudentId.setText(rs.getString("student_id"));
                lblName.setText(rs.getString("first_name") + " " + rs.getString("last_name"));
                lblNIC.setText(rs.getString("nic"));
                lblEmail.setText(rs.getString("email"));
                lblPhone.setText(rs.getString("phone"));
                lblAddress.setText(rs.getString("address"));
                lblGrade.setText(rs.getString("grade_name"));
                lblGuardian.setText(rs.getString("guardian_first") + " " + rs.getString("guardian_last"));
                lblGuardianPhone.setText(rs.getString("guardian_phone"));
                lblRegistrationDate.setText(rs.getString("registration_date"));
            } else {
                JOptionIcon.showErrorDialog(this, "Student not found: " + studentId);
            }
            
            rs.close();
            
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading student information: " + e.getMessage());
        }
    }
}