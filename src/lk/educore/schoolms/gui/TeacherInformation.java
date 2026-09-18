package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * Teacher Information Panel
 * Displays detailed teacher information in a read-only format
 */
public class TeacherInformation extends JPanel {

    private String teacherNIC;
    private JLabel lblNIC, lblName, lblEmail, lblPhone, lblAddress, lblType, lblSalary, lblStatus, lblHireDate;

    public TeacherInformation(String teacherNIC) {
        this.teacherNIC = teacherNIC;
        initComponents();
        loadTeacherInfo();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Teacher Information");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Info panel
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // NIC
        gbc.gridx = 0;
        gbc.gridy = 0;
        infoPanel.add(new JLabel("NIC:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblNIC = new JLabel();
        lblNIC.setFont(new Font("Segoe UI", Font.BOLD, 12));
        infoPanel.add(lblNIC, gbc);

        // Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        infoPanel.add(new JLabel("Full Name:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblName = new JLabel();
        infoPanel.add(lblName, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        infoPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblEmail = new JLabel();
        infoPanel.add(lblEmail, gbc);

        // Phone
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        infoPanel.add(new JLabel("Phone:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblPhone = new JLabel();
        infoPanel.add(lblPhone, gbc);

        // Address
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        infoPanel.add(new JLabel("Address:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblAddress = new JLabel();
        infoPanel.add(lblAddress, gbc);

        // Type
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        infoPanel.add(new JLabel("Type:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblType = new JLabel();
        infoPanel.add(lblType, gbc);

        // Salary
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.0;
        infoPanel.add(new JLabel("Salary:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblSalary = new JLabel();
        infoPanel.add(lblSalary, gbc);

        // Status
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0.0;
        infoPanel.add(new JLabel("Status:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblStatus = new JLabel();
        infoPanel.add(lblStatus, gbc);

        // Hire Date
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.weightx = 0.0;
        infoPanel.add(new JLabel("Hire Date:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblHireDate = new JLabel();
        infoPanel.add(lblHireDate, gbc);

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

    private void loadTeacherInfo() {
        try {
            ResultSet rs = MySQL.executeSearch("SELECT t.NIC, t.first_name, t.last_name, t.email, t.phone, t.address, " +
                          "tt.type_name, t.salary, ts.status_name, t.hire_date " +
                          "FROM teacher t " +
                          "JOIN teacher_type tt ON t.teacher_type_id = tt.teacher_type_id " +
                          "JOIN teacher_status ts ON t.status_id = ts.status_id " +
                          "WHERE t.NIC = '" + teacherNIC + "'");
            
            if (rs.next()) {
                lblNIC.setText(rs.getString("NIC"));
                lblName.setText(rs.getString("first_name") + " " + rs.getString("last_name"));
                lblEmail.setText(rs.getString("email"));
                lblPhone.setText(rs.getString("phone"));
                lblAddress.setText(rs.getString("address"));
                lblType.setText(rs.getString("type_name"));
                lblSalary.setText(String.format("Rs. %,.2f", rs.getDouble("salary")));
                lblStatus.setText(rs.getString("status_name"));
                lblHireDate.setText(rs.getString("hire_date"));
            } else {
                JOptionIcon.showErrorDialog(this, "Teacher not found: " + teacherNIC);
            }
            
            rs.close();
            
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading teacher information: " + e.getMessage());
        }
    }
}