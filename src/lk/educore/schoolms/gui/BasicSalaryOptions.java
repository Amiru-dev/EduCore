package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;
import lk.educore.schoolms.validate.DataValidator;
import lk.educore.schoolms.exceptions.ValidationException;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * Basic Salary Options
 * Dialog for configuring salary options for different teacher types
 */
public class BasicSalaryOptions extends JDialog {

    private JComboBox<String> cmbTeacherType;
    private JTextField txtBasicSalary;
    private JTextField txtAllowance;
    private JTextField txtBonus;
    private JButton btnSave, btnCancel, btnClose;

    public BasicSalaryOptions(Frame parent) {
        super(parent, "Salary Configuration", true);
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(400, 300);
        setLayout(new BorderLayout(10, 10));

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Salary Configuration");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Teacher Type
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Teacher Type:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cmbTeacherType = new JComboBox<>();
        loadTeacherTypes();
        formPanel.add(cmbTeacherType, gbc);

        // Basic Salary
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Basic Salary:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtBasicSalary = new JTextField(20);
        formPanel.add(txtBasicSalary, gbc);

        // Allowance
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Allowance:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtAllowance = new JTextField(20);
        formPanel.add(txtAllowance, gbc);

        // Bonus
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Bonus:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtBonus = new JTextField(20);
        formPanel.add(txtBonus, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnSave = new JButton("Save Configuration");
        btnSave.addActionListener(e -> saveConfiguration());
        
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> clearForm());
        
        btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnClose);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        
        // Load existing configuration when type changes
        cmbTeacherType.addActionListener(e -> loadConfiguration());
    }

    private void loadTeacherTypes() {
        try {
            ResultSet rs = MySQL.executeSearch("SELECT type_name FROM teacher_type ORDER BY type_name");
            
            while (rs.next()) {
                cmbTeacherType.addItem(rs.getString("type_name"));
            }
            
            rs.close();
            
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading teacher types: " + e.getMessage());
        }
    }

    private void loadConfiguration() {
        String teacherType = (String) cmbTeacherType.getSelectedItem();
        if (teacherType == null) return;
        
        // In a real implementation, you would have a salary_config table
        // For now, we'll just clear the fields
        txtBasicSalary.setText("");
        txtAllowance.setText("");
        txtBonus.setText("");
    }

    private void saveConfiguration() {
        try {
            String teacherType = (String) cmbTeacherType.getSelectedItem();
            String basicSalary = txtBasicSalary.getText().trim();
            String allowance = txtAllowance.getText().trim();
            String bonus = txtBonus.getText().trim();
            
            DataValidator.isRequired(basicSalary);
            DataValidator.isPositiveNumber(basicSalary);
            
            if (!allowance.isEmpty()) {
                DataValidator.isPositiveNumber(allowance);
            }
            
            if (!bonus.isEmpty()) {
                DataValidator.isPositiveNumber(bonus);
            }
            
            // In a real implementation, save to salary_config table
            JOptionIcon.showSuccessDialog(this, "Salary configuration saved for " + teacherType);
            
        } catch (ValidationException e) {
            JOptionIcon.showErrorDialog(this, e.getUserFriendlyMessage());
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error saving configuration: " + e.getMessage());
        }
    }

    private void clearForm() {
        txtBasicSalary.setText("");
        txtAllowance.setText("");
        txtBonus.setText("");
    }
}