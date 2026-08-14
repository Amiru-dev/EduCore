package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.email.SendQREmail;
import lk.educore.schoolms.email.SendEmail;
import lk.educore.schoolms.validate.JOptionIcon;
import lk.educore.schoolms.validate.DataValidator;
import lk.educore.schoolms.exceptions.ValidationException;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.security.SecureRandom;

/**
 * Student Registration Dialog
 * Registration form with auto-generated student ID, user account creation, and email notification
 */
public class StudentRegistration extends JDialog {

    private JTextField txtStudentId;
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtNIC;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextArea txtAddress;
    private JComboBox<String> cmbGrade;
    private JTextField txtGuardianName;
    private JTextField txtGuardianNIC;
    private JTextField txtGuardianPhone;
    private JTextField txtGuardianEmail;
    private JTextField txtGuardianAddress;
    private JTextField txtGuardianOccupation;
    private JComboBox<String> cmbGuardianRelationship;
    private JCheckBox chkSendEmail;
    private JCheckBox chkGenerateQR;

    public StudentRegistration(Frame parent) {
        super(parent, "Student Registration", true);
        initComponents();
        generateStudentId();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(700, 600);
        setLayout(new BorderLayout(10, 10));

        // Main panel with scroll
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Student Registration");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titlePanel.add(titlePanel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Student ID (auto-generated)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Student ID:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtStudentId = new JTextField(20);
        txtStudentId.setEditable(false);
        txtStudentId.setBackground(new Color(240, 240, 240));
        formPanel.add(txtStudentId, gbc);

        // First Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("First Name:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtFirstName = new JTextField(20);
        formPanel.add(txtFirstName, gbc);

        // Last Name
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Last Name:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtLastName = new JTextField(20);
        formPanel.add(txtLastName, gbc);

        // NIC
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("NIC:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtNIC = new JTextField(20);
        formPanel.add(txtNIC, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtEmail = new JTextField(20);
        formPanel.add(txtEmail, gbc);

        // Phone
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Phone:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtPhone = new JTextField(20);
        formPanel.add(txtPhone, gbc);

        // Address
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Address:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtAddress = new JTextArea(3, 20);
        JScrollPane addressScroll = new JScrollPane(txtAddress);
        formPanel.add(addressScroll, gbc);

        // Grade
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Grade:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cmbGrade = new JComboBox<>();
        loadGrades();
        formPanel.add(cmbGrade, gbc);

        // Guardian Section Label
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.weightx = 0.0;
        JLabel guardianLabel = new JLabel("Guardian Information");
        guardianLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        guardianLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        formPanel.add(guardianLabel, gbc);

        // Guardian Name
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Guardian Name:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtGuardianName = new JTextField(20);
        formPanel.add(txtGuardianName, gbc);

        // Guardian NIC
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Guardian NIC:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtGuardianNIC = new JTextField(20);
        formPanel.add(txtGuardianNIC, gbc);

        // Guardian Relationship
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Relationship:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cmbGuardianRelationship = new JComboBox<>(new String[]{"Father", "Mother", "Guardian"});
        formPanel.add(cmbGuardianRelationship, gbc);

        // Guardian Phone
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Guardian Phone:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtGuardianPhone = new JTextField(20);
        formPanel.add(txtGuardianPhone, gbc);

        // Guardian Email
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Guardian Email:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtGuardianEmail = new JTextField(20);
        formPanel.add(txtGuardianEmail, gbc);

        // Guardian Address
        gbc.gridx = 0;
        gbc.gridy = 14;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Guardian Address:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtGuardianAddress = new JTextField(20);
        formPanel.add(txtGuardianAddress, gbc);

        // Guardian Occupation
        gbc.gridx = 0;
        gbc.gridy = 15;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Guardian Occupation:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtGuardianOccupation = new JTextField(20);
        formPanel.add(txtGuardianOccupation, gbc);

        // Email and QR options
        gbc.gridx = 0;
        gbc.gridy = 16;
        gbc.gridwidth = 2;
        gbc.weightx = 0.0;
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        chkSendEmail = new JCheckBox("Send registration email");
        chkSendEmail.setSelected(true);
        
        chkGenerateQR = new JCheckBox("Generate QR code");
        chkGenerateQR.setSelected(true);
        
        optionsPanel.add(chkSendEmail);
        optionsPanel.add(chkGenerateQR);
        formPanel.add(optionsPanel, gbc);

        JScrollPane formScroll = new JScrollPane(formPanel);
        mainPanel.add(formScroll, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnSave = new JButton("Register Student");
        btnSave.addActionListener(e -> registerStudent());
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadGrades() {
        try {
            Connection conn = MySQL.getConnection();
            String query = "SELECT grade_name FROM grade ORDER BY grade_name";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                cmbGrade.addItem(rs.getString("grade_name"));
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            JOptionIcon.showErrorDialog(this, "Error loading grades: " + e.getMessage());
        }
    }

    private void generateStudentId() {
        try {
            Connection conn = MySQL.getConnection();
            String query = "SELECT student_id FROM student ORDER BY student_id DESC LIMIT 1";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            String newId = "STU-0001";
            if (rs.next()) {
                String lastId = rs.getString("student_id");
                String[] parts = lastId.split("-");
                int lastNumber = Integer.parseInt(parts[1]);
                newId = String.format("STU-%04d", lastNumber + 1);
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
            txtStudentId.setText(newId);
            
        } catch (SQLException e) {
            JOptionIcon.showErrorDialog(this, "Error generating student ID: " + e.getMessage());
        }
    }

    private String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    private void registerStudent() {
        try {
            // Validate inputs
            String studentId = txtStudentId.getText().trim();
            String firstName = txtFirstName.getText().trim();
            String lastName = txtLastName.getText().trim();
            String nic = txtNIC.getText().trim();
            String email = txtEmail.getText().trim();
            String phone = txtPhone.getText().trim();
            String address = txtAddress.getText().trim();
            String grade = (String) cmbGrade.getSelectedItem();
            String guardianName = txtGuardianName.getText().trim();
            String guardianNIC = txtGuardianNIC.getText().trim();
            String guardianPhone = txtGuardianPhone.getText().trim();
            String guardianEmail = txtGuardianEmail.getText().trim();
            String guardianAddress = txtGuardianAddress.getText().trim();
            String guardianOccupation = txtGuardianOccupation.getText().trim();
            String relationship = (String) cmbGuardianRelationship.getSelectedItem();

            DataValidator.isRequired(firstName);
            DataValidator.isRequired(lastName);
            DataValidator.isValidNIC(nic);
            DataValidator.isValidEmail(email);
            DataValidator.isValidPhone(phone);
            DataValidator.isRequired(address);
            DataValidator.isRequired(guardianName);
            DataValidator.isRequired(guardianPhone);

            Connection conn = MySQL.getConnection();
            conn.setAutoCommit(false);

            try {
                // Insert guardian
                String guardianQuery = "INSERT INTO guardian (first_name, last_name, nic, relationship, phone, email, address, occupation) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement guardianStmt = conn.prepareStatement(guardianQuery, Statement.RETURN_GENERATED_KEYS);
                
                String[] guardianNameParts = guardianName.split(" ", 2);
                String guardianFirstName = guardianNameParts[0];
                String guardianLastName = guardianNameParts.length > 1 ? guardianNameParts[1] : "";
                
                guardianStmt.setString(1, guardianFirstName);
                guardianStmt.setString(2, guardianLastName);
                guardianStmt.setString(3, guardianNIC.isEmpty() ? null : guardianNIC);
                guardianStmt.setString(4, relationship);
                guardianStmt.setString(5, guardianPhone);
                guardianStmt.setString(6, guardianEmail.isEmpty() ? null : guardianEmail);
                guardianStmt.setString(7, guardianAddress);
                guardianStmt.setString(8, guardianOccupation);
                
                guardianStmt.executeUpdate();
                ResultSet guardianRs = guardianStmt.getGeneratedKeys();
                int guardianId = -1;
                if (guardianRs.next()) {
                    guardianId = guardianRs.getInt(1);
                }
                guardianRs.close();
                guardianStmt.close();

                // Get grade ID
                String gradeQuery = "SELECT grade_id FROM grade WHERE grade_name = ?";
                PreparedStatement gradeStmt = conn.prepareStatement(gradeQuery);
                gradeStmt.setString(1, grade);
                ResultSet gradeRs = gradeStmt.executeQuery();
                int gradeId = -1;
                if (gradeRs.next()) {
                    gradeId = gradeRs.getInt("grade_id");
                }
                gradeRs.close();
                gradeStmt.close();

                // Create user account
                String username = email;
                String temporaryPassword = generateRandomPassword();
                
                String userQuery = "INSERT INTO user (username, password, email, user_type_id, is_active) VALUES (?, ?, ?, 3, 1)";
                PreparedStatement userStmt = conn.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS);
                userStmt.setString(1, username);
                userStmt.setString(2, temporaryPassword); // In production, hash this
                userStmt.setString(3, email);
                userStmt.executeUpdate();
                
                ResultSet userRs = userStmt.getGeneratedKeys();
                int userId = -1;
                if (userRs.next()) {
                    userId = userRs.getInt(1);
                }
                userRs.close();
                userStmt.close();

                // Insert student
                String studentQuery = "INSERT INTO student (student_id, first_name, last_name, dob, gender, nic, email, phone, address, grade_id, guardian_id, user_id, registration_date, is_active) VALUES (?, ?, ?, CURDATE(), 'Other', ?, ?, ?, ?, ?, ?, ?, CURDATE(), 1)";
                PreparedStatement studentStmt = conn.prepareStatement(studentQuery);
                studentStmt.setString(1, studentId);
                studentStmt.setString(2, firstName);
                studentStmt.setString(3, lastName);
                studentStmt.setString(4, nic.isEmpty() ? null : nic);
                studentStmt.setString(5, email);
                studentStmt.setString(6, phone);
                studentStmt.setString(7, address);
                studentStmt.setInt(8, gradeId);
                studentStmt.setInt(9, guardianId);
                studentStmt.setInt(10, userId);
                studentStmt.executeUpdate();
                studentStmt.close();

                conn.commit();

                // Send email if requested
                if (chkSendEmail.isSelected()) {
                    try {
                        if (chkGenerateQR.isSelected()) {
                            SendQREmail.sendRegistrationEmailWithQR(
                                email,
                                studentId,
                                firstName + " " + lastName,
                                grade,
                                username,
                                temporaryPassword
                            );
                        } else {
                            String message = "Dear " + firstName + " " + lastName + ",\n\n" +
                                "Welcome to Central College Anuradhapura!\n\n" +
                                "Your student account has been created successfully.\n\n" +
                                "Student ID: " + studentId + "\n" +
                                "Grade: " + grade + "\n" +
                                "Username: " + username + "\n" +
                                "Temporary Password: " + temporaryPassword + "\n\n" +
                                "Please change your password after your first login.\n\n" +
                                "Best regards,\n" +
                                "Central College Anuradhapura";
                            
                            SendEmail.sendEmail(email, "Student Registration Confirmation", message);
                        }
                    } catch (Exception emailEx) {
                        JOptionIcon.showWarningDialog(this, "Student registered successfully but email failed: " + emailEx.getMessage());
                    }
                }

                JOptionIcon.showSuccessDialog(this, "Student registered successfully! Student ID: " + studentId);
                dispose();

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
                conn.close();
            }

        } catch (ValidationException e) {
            JOptionIcon.showErrorDialog(this, e.getUserFriendlyMessage());
        } catch (SQLException e) {
            JOptionIcon.showErrorDialog(this, "Error registering student: " + e.getMessage());
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Unexpected error: " + e.getMessage());
        }
    }
}