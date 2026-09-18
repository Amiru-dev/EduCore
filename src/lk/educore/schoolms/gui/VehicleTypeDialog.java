package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;
import lk.educore.schoolms.validate.DataValidator;
import lk.educore.schoolms.exceptions.ValidationException;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * Vehicle Type Dialog
 * Dialog for adding/editing vehicle types
 */
public class VehicleTypeDialog extends JDialog {

    private JTextField txtTypeName;
    private JTextField txtDescription;
    private JButton btnSave, btnCancel;

    public VehicleTypeDialog(Frame parent) {
        super(parent, "Vehicle Type", true);
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(350, 200);
        setLayout(new BorderLayout(10, 10));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Type Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Type Name:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtTypeName = new JTextField(20);
        formPanel.add(txtTypeName, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtDescription = new JTextField(20);
        formPanel.add(txtDescription, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnSave = new JButton("Save");
        btnSave.addActionListener(e -> saveVehicleType());
        
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void saveVehicleType() {
        try {
            String typeName = txtTypeName.getText().trim();
            String description = txtDescription.getText().trim();
            
            DataValidator.isRequired(typeName);
            
            String descValue = description.isEmpty() ? "NULL" : "'" + description + "'";
            MySQL.executeIUD("INSERT INTO vehicle_type (type_name, description) VALUES ('" + typeName + "', " + descValue + ")");
            
            JOptionIcon.showSuccessDialog(this, "Vehicle type added successfully!");
            dispose();
            
        } catch (ValidationException e) {
            JOptionIcon.showErrorDialog(this, e.getUserFriendlyMessage());
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error saving vehicle type: " + e.getMessage());
        }
    }
}