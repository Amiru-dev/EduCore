package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;
import lk.educore.schoolms.validate.DataValidator;
import lk.educore.schoolms.exceptions.ValidationException;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * Student Address View
 * Panel for viewing and editing student address information
 */
public class StudentAddressView extends JPanel {

    private String studentId;
    private JTextArea txtAddress;
    private JButton btnSave, btnCancel;

    public StudentAddressView(String studentId) {
        this.studentId = studentId;
        initComponents();
        loadAddress();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Student Address");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Address panel
        JPanel addressPanel = new JPanel(new BorderLayout(10, 10));
        
        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        txtAddress = new JTextArea(5, 30);
        txtAddress.setLineWrap(true);
        txtAddress.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtAddress);
        
        addressPanel.add(lblAddress, BorderLayout.NORTH);
        addressPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(addressPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnSave = new JButton("Save Address");
        btnSave.addActionListener(e -> saveAddress());
        
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> cancelEdit());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadAddress() {
        try {
            Connection conn = MySQL.getConnection();
            String query = "SELECT address FROM student WHERE student_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                txtAddress.setText(rs.getString("address"));
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            JOptionIcon.showErrorDialog(this, "Error loading address: " + e.getMessage());
        }
    }

    private void saveAddress() {
        try {
            String address = txtAddress.getText().trim();
            DataValidator.isRequired(address);
            
            Connection conn = MySQL.getConnection();
            String query = "UPDATE student SET address = ? WHERE student_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, address);
            stmt.setString(2, studentId);
            stmt.executeUpdate();
            stmt.close();
            conn.close();
            
            JOptionIcon.showSuccessDialog(this, "Address updated successfully!");
            
        } catch (ValidationException e) {
            JOptionIcon.showErrorDialog(this, e.getUserFriendlyMessage());
        } catch (SQLException e) {
            JOptionIcon.showErrorDialog(this, "Error saving address: " + e.getMessage());
        }
    }

    private void cancelEdit() {
        loadAddress();
    }
}