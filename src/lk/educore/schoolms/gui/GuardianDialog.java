package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;
import lk.educore.schoolms.validate.DataValidator;
import lk.educore.schoolms.exceptions.ValidationException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Guardian Dialog
 * Dialog for viewing and editing guardian details
 */
public class GuardianDialog extends JDialog {

    private int guardianId;
    private JTextField txtFirstName, txtLastName, txtNIC, txtPhone, txtEmail, txtOccupation;
    private JComboBox<String> cmbRelationship;
    private JTextArea txtAddress;
    private JButton btnSave, btnCancel;

    public GuardianDialog(Frame parent, int guardianId) {
        super(parent, "Guardian Details", true);
        this.guardianId = guardianId;
        initComponents();
        loadGuardianDetails();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(450, 400);
        setLayout(new BorderLayout(10, 10));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // First Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("First Name:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtFirstName = new JTextField(20);
        formPanel.add(txtFirstName, gbc);

        // Last Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Last Name:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtLastName = new JTextField(20);
        formPanel.add(txtLastName, gbc);

        // NIC
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("NIC:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtNIC = new JTextField(20);
        formPanel.add(txtNIC, gbc);

        // Relationship
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Relationship:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cmbRelationship = new JComboBox<>(new String[]{"Father", "Mother", "Guardian"});
        formPanel.add(cmbRelationship, gbc);

        // Phone
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Phone:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtPhone = new JTextField(20);
        formPanel.add(txtPhone, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtEmail = new JTextField(20);
        formPanel.add(txtEmail, gbc);

        // Occupation
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Occupation:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtOccupation = new JTextField(20);
        formPanel.add(txtOccupation, gbc);

        // Address
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Address:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtAddress = new JTextArea(3, 20);
        txtAddress.setLineWrap(true);
        txtAddress.setWrapStyleWord(true);
        JScrollPane addressScroll = new JScrollPane(txtAddress);
        formPanel.add(addressScroll, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnSave = new JButton("Save");
        btnSave.addActionListener(e -> saveGuardian());
        
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadGuardianDetails() {
        try {
            Connection conn = MySQL.getConnection();
            String query = "SELECT first_name, last_name, nic, relationship, phone, email, address, occupation " +
                          "FROM guardian WHERE guardian_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, guardianId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                txtFirstName.setText(rs.getString("first_name"));
                txtLastName.setText(rs.getString("last_name"));
                txtNIC.setText(rs.getString("nic"));
                cmbRelationship.setSelectedItem(rs.getString("relationship"));
                txtPhone.setText(rs.getString("phone"));
                txtEmail.setText(rs.getString("email"));
                txtOccupation.setText(rs.getString("occupation"));
                txtAddress.setText(rs.getString("address"));
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            JOptionIcon.showErrorDialog(this, "Error loading guardian details: " + e.getMessage());
        }
    }

    private void saveGuardian() {
        try {
            String firstName = txtFirstName.getText().trim();
            String lastName = txtLastName.getText().trim();
            String nic = txtNIC.getText().trim();
            String relationship = (String) cmbRelationship.getSelectedItem();
            String phone = txtPhone.getText().trim();
            String email = txtEmail.getText().trim();
            String occupation = txtOccupation.getText().trim();
            String address = txtAddress.getText().trim();
            
            DataValidator.isRequired(firstName);
            DataValidator.isRequired(lastName);
            DataValidator.isRequired(phone);
            DataValidator.isRequired(address);
            
            if (!nic.isEmpty()) {
                DataValidator.isValidNIC(nic);
            }
            
            if (!email.isEmpty()) {
                DataValidator.isValidEmail(email);
            }
            
            Connection conn = MySQL.getConnection();
            String query = "UPDATE guardian SET first_name = ?, last_name = ?, nic = ?, relationship = ?, " +
                          "phone = ?, email = ?, occupation = ?, address = ? WHERE guardian_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, nic.isEmpty() ? null : nic);
            stmt.setString(4, relationship);
            stmt.setString(5, phone);
            stmt.setString(6, email.isEmpty() ? null : email);
            stmt.setString(7, occupation.isEmpty() ? null : occupation);
            stmt.setString(8, address);
            stmt.setInt(9, guardianId);
            stmt.executeUpdate();
            stmt.close();
            conn.close();
            
            JOptionIcon.showSuccessDialog(this, "Guardian details updated successfully!");
            dispose();
            
        } catch (ValidationException e) {
            JOptionIcon.showErrorDialog(this, e.getUserFriendlyMessage());
        } catch (SQLException e) {
            JOptionIcon.showErrorDialog(this, "Error saving guardian details: " + e.getMessage());
        }
    }
}