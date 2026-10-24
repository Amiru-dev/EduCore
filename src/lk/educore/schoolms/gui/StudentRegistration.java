package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.model.Student;
import lk.educore.schoolms.util.PasswordUtil;
import lk.educore.schoolms.email.SendQREmail;
import lk.educore.schoolms.email.SendEmail;
import lk.educore.schoolms.validate.JOptionIcon;
import lk.educore.schoolms.validate.DataValidator;
import lk.educore.schoolms.exceptions.ValidationException;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import com.toedter.calendar.JDateChooser;

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
    private JTextField txtGuardianFirstName;
    private JTextField txtGuardianLastName;
    private JTextField txtGuardianNIC;
    private JTextField txtGuardianPhone;
    private JTextField txtGuardianEmail;
    private JTextField txtGuardianAddress;
    private JTextField txtGuardianOccupation;
    private JComboBox<String> cmbGuardianRelationship;
    private JCheckBox chkSendEmail;
    private JCheckBox chkGenerateQR;
    private JDateChooser dateChooserDOB;
    private JRadioButton rdoMale;
    private JRadioButton rdoFemale;
    private ButtonGroup genderGroup;
    
    // Guardian search components
    private JTextField txtGuardianSearchNIC;
    private JButton btnSearchGuardian;
    private JCheckBox chkUseExistingGuardian;
    private int selectedGuardianId = -1;

    public StudentRegistration(Frame parent) {
        super(parent, "Student Registration", true);
        initComponents();
        generateStudentId();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(700, 650);
        setLayout(new BorderLayout(10, 10));

        // Main panel with scroll
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Student Registration");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titlePanel.add(titleLabel);
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

        // DOB
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Date of Birth:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        dateChooserDOB = new JDateChooser();
        dateChooserDOB.setDateFormatString("yyyy-MM-dd");
        formPanel.add(dateChooserDOB, gbc);

        // Gender
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Gender:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        rdoMale = new JRadioButton("Male");
        rdoFemale = new JRadioButton("Female");
        rdoMale.setSelected(true);
        genderGroup = new ButtonGroup();
        genderGroup.add(rdoMale);
        genderGroup.add(rdoFemale);
        genderPanel.add(rdoMale);
        genderPanel.add(rdoFemale);
        formPanel.add(genderPanel, gbc);

        // NIC
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("NIC:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtNIC = new JTextField(20);
        formPanel.add(txtNIC, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtEmail = new JTextField(20);
        formPanel.add(txtEmail, gbc);

        // Phone
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Phone:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtPhone = new JTextField(20);
        formPanel.add(txtPhone, gbc);

        // Address
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Address:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtAddress = new JTextArea(3, 20);
        JScrollPane addressScroll = new JScrollPane(txtAddress);
        formPanel.add(addressScroll, gbc);

        // Grade
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Grade:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cmbGrade = new JComboBox<>();
        loadGrades();
        formPanel.add(cmbGrade, gbc);

        // Guardian Section Label
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.weightx = 0.0;
        JLabel guardianLabel = new JLabel("Guardian Information");
        guardianLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        guardianLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        formPanel.add(guardianLabel, gbc);

        // Guardian Search Panel
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        gbc.weightx = 0.0;
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        searchPanel.add(new JLabel("Search Guardian by NIC:"));
        txtGuardianSearchNIC = new JTextField(15);
        searchPanel.add(txtGuardianSearchNIC);
        btnSearchGuardian = new JButton("Search");
        btnSearchGuardian.addActionListener(e -> searchGuardianByNIC());
        searchPanel.add(btnSearchGuardian);
        formPanel.add(searchPanel, gbc);

        // Use Existing Guardian Checkbox
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        gbc.weightx = 0.0;
        chkUseExistingGuardian = new JCheckBox("Use Existing Guardian");
        chkUseExistingGuardian.addActionListener(e -> toggleGuardianMode());
        formPanel.add(chkUseExistingGuardian, gbc);

        // Guardian First Name
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Guardian First Name:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtGuardianFirstName = new JTextField(20);
        formPanel.add(txtGuardianFirstName, gbc);

        // Guardian Last Name
        gbc.gridx = 0;
        gbc.gridy = 14;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Guardian Last Name:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtGuardianLastName = new JTextField(20);
        formPanel.add(txtGuardianLastName, gbc);

        // Guardian NIC
        gbc.gridx = 0;
        gbc.gridy = 15;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Guardian NIC:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtGuardianNIC = new JTextField(20);
        formPanel.add(txtGuardianNIC, gbc);

        // Guardian Relationship
        gbc.gridx = 0;
        gbc.gridy = 16;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Relationship:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cmbGuardianRelationship = new JComboBox<>(new String[]{"Father", "Mother", "Guardian"});
        formPanel.add(cmbGuardianRelationship, gbc);

        // Guardian Phone
        gbc.gridx = 0;
        gbc.gridy = 17;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Guardian Phone:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtGuardianPhone = new JTextField(20);
        formPanel.add(txtGuardianPhone, gbc);

        // Guardian Email
        gbc.gridx = 0;
        gbc.gridy = 18;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Guardian Email:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtGuardianEmail = new JTextField(20);
        formPanel.add(txtGuardianEmail, gbc);

        // Guardian Address
        gbc.gridx = 0;
        gbc.gridy = 19;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Guardian Address:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtGuardianAddress = new JTextField(20);
        formPanel.add(txtGuardianAddress, gbc);

        // Guardian Occupation
        gbc.gridx = 0;
        gbc.gridy = 20;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Guardian Occupation:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtGuardianOccupation = new JTextField(20);
        formPanel.add(txtGuardianOccupation, gbc);

        // Email and QR options
        gbc.gridx = 0;
        gbc.gridy = 21;
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
            String query = "SELECT grade_name, section FROM grade ORDER BY grade_name, section";
            ResultSet rs = MySQL.executeSearch(query);
            
            while (rs.next()) {
                String gName = rs.getString("grade_name");
                String sec = rs.getString("section");
                if (sec != null && !sec.isEmpty()) {
                    cmbGrade.addItem(gName + " - " + sec);
                } else {
                    cmbGrade.addItem(gName);
                }
            }
            
            rs.close();
            
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading grades: " + e.getMessage());
        }
    }

    private void searchGuardianByNIC() {
        String nic = txtGuardianSearchNIC.getText().trim();
        
        if (nic.isEmpty()) {
            JOptionIcon.showWarningDialog(this, "Please enter a NIC to search.");
            return;
        }
        
        try {
            String query = "SELECT guardian_id, first_name, last_name, nic, relationship, phone, email, address, occupation " +
                          "FROM guardian WHERE nic = '" + nic.replace("'", "''") + "'";
            ResultSet rs = MySQL.executeSearch(query);
            
            if (rs.next()) {
                selectedGuardianId = rs.getInt("guardian_id");
                txtGuardianFirstName.setText(rs.getString("first_name"));
                txtGuardianLastName.setText(rs.getString("last_name"));
                txtGuardianNIC.setText(rs.getString("nic"));
                cmbGuardianRelationship.setSelectedItem(rs.getString("relationship"));
                txtGuardianPhone.setText(rs.getString("phone"));
                txtGuardianEmail.setText(rs.getString("email"));
                txtGuardianAddress.setText(rs.getString("address"));
                txtGuardianOccupation.setText(rs.getString("occupation"));
                
                chkUseExistingGuardian.setSelected(true);
                toggleGuardianMode();
                
                JOptionIcon.showSuccessDialog(this, "Guardian found: " + rs.getString("first_name") + " " + rs.getString("last_name"));
            } else {
                JOptionIcon.showWarningDialog(this, "No guardian found with NIC: " + nic + "\nYou can add a new guardian manually.");
                selectedGuardianId = -1;
                chkUseExistingGuardian.setSelected(false);
                toggleGuardianMode();
            }
            
            rs.close();
            
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error searching guardian: " + e.getMessage());
        }
    }

    private void toggleGuardianMode() {
        boolean useExisting = chkUseExistingGuardian.isSelected();
        
        txtGuardianFirstName.setEditable(!useExisting);
        txtGuardianLastName.setEditable(!useExisting);
        txtGuardianNIC.setEditable(!useExisting);
        cmbGuardianRelationship.setEnabled(!useExisting);
        txtGuardianPhone.setEditable(!useExisting);
        txtGuardianEmail.setEditable(!useExisting);
        txtGuardianAddress.setEditable(!useExisting);
        txtGuardianOccupation.setEditable(!useExisting);
        
        if (useExisting) {
            txtGuardianFirstName.setBackground(new Color(240, 240, 240));
            txtGuardianLastName.setBackground(new Color(240, 240, 240));
            txtGuardianNIC.setBackground(new Color(240, 240, 240));
            txtGuardianPhone.setBackground(new Color(240, 240, 240));
            txtGuardianEmail.setBackground(new Color(240, 240, 240));
            txtGuardianAddress.setBackground(new Color(240, 240, 240));
            txtGuardianOccupation.setBackground(new Color(240, 240, 240));
        } else {
            txtGuardianFirstName.setBackground(Color.WHITE);
            txtGuardianLastName.setBackground(Color.WHITE);
            txtGuardianNIC.setBackground(Color.WHITE);
            txtGuardianPhone.setBackground(Color.WHITE);
            txtGuardianEmail.setBackground(Color.WHITE);
            txtGuardianAddress.setBackground(Color.WHITE);
            txtGuardianOccupation.setBackground(Color.WHITE);
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
            String guardianFirstName = txtGuardianFirstName.getText().trim();
            String guardianLastName = txtGuardianLastName.getText().trim();
            String guardianNIC = txtGuardianNIC.getText().trim();
            String guardianPhone = txtGuardianPhone.getText().trim();
            String guardianEmail = txtGuardianEmail.getText().trim();
            String guardianAddress = txtGuardianAddress.getText().trim();
            String guardianOccupation = txtGuardianOccupation.getText().trim();
            String relationship = (String) cmbGuardianRelationship.getSelectedItem();

            java.util.Date dobDate = dateChooserDOB.getDate();
            if (dobDate == null) {
                throw new ValidationException("Date of Birth is required");
            }
            String dob = new SimpleDateFormat("yyyy-MM-dd").format(dobDate);
            String gender = rdoMale.isSelected() ? "Male" : "Female";

            // Build Student domain object (inherits from Person)
            Student student = new Student(studentId, firstName, lastName, email, phone, nic, dob, gender, grade, address);
            // Polymorphic validation from Person and Student
            student.validate();

            // Determine guardian handling
            int guardianId = -1;
            boolean useExistingGuardian = chkUseExistingGuardian.isSelected();
            
            if (useExistingGuardian) {
                // Using existing guardian - validate that one is selected
                if (selectedGuardianId == -1) {
                    throw new ValidationException("Please search and select an existing guardian, or uncheck 'Use Existing Guardian' to add a new one.");
                }
                guardianId = selectedGuardianId;
            } else {
                // Validate guardian inputs for new guardian
                DataValidator.isRequired(guardianFirstName, "Guardian First Name");
                DataValidator.isRequired(guardianLastName, "Guardian Last Name");
                DataValidator.isValidNIC(guardianNIC, "Guardian NIC");
                DataValidator.isValidEmail(guardianEmail, "Guardian Email");
                DataValidator.isValidPhone(guardianPhone, "Guardian Phone");
                DataValidator.isRequired(guardianAddress, "Guardian Address");
                DataValidator.isRequired(guardianOccupation, "Guardian Occupation");
            }

            Connection conn = MySQL.getConnection();
            conn.setAutoCommit(false);

            try {
                // Insert guardian if not using existing
                if (!useExistingGuardian) {
                    String guardianQuery = "INSERT INTO guardian (first_name, last_name, nic, relationship, phone, email, address, occupation) VALUES ('"
                            + guardianFirstName + "', '"
                            + guardianLastName + "', '"
                            + guardianNIC + "', '"
                            + relationship + "', '"
                            + guardianPhone + "', '"
                            + guardianEmail + "', '"
                            + guardianAddress + "', '"
                            + guardianOccupation + "')";
                    Statement guardianStmt = conn.createStatement();
                    guardianStmt.executeUpdate(guardianQuery, Statement.RETURN_GENERATED_KEYS);
                    
                    ResultSet guardianRs = guardianStmt.getGeneratedKeys();
                    if (guardianRs.next()) {
                        guardianId = guardianRs.getInt(1);
                    }
                    guardianRs.close();
                    guardianStmt.close();
                }

                // Get grade ID
                String gradeName = grade;
                String section = null;
                if (grade.contains(" - ")) {
                    String[] gradeParts = grade.split(" - ", 2);
                    gradeName = gradeParts[0];
                    section = gradeParts[1];
                }

                String gradeQuery;
                if (section != null) {
                    gradeQuery = "SELECT grade_id FROM grade WHERE grade_name = '" + gradeName + "' AND section = '" + section + "'";
                } else {
                    gradeQuery = "SELECT grade_id FROM grade WHERE grade_name = '" + gradeName + "' AND (section IS NULL OR section = '')";
                }

                ResultSet gradeRs = MySQL.executeSearch(gradeQuery);
                int gradeId = -1;
                if (gradeRs.next()) {
                    gradeId = gradeRs.getInt("grade_id");
                }
                gradeRs.close();

                // Create user account
                String username = email;
                String temporaryPassword = generateRandomPassword();
                String hashedPassword = PasswordUtil.hashPassword(temporaryPassword);
                
                String userQuery = "INSERT INTO user (username, password, email, user_type_id, is_active) VALUES ('" + username + "', '" + hashedPassword + "', '" + email + "', 3, 1)";
                Statement userStmt = conn.createStatement();
                userStmt.executeUpdate(userQuery, Statement.RETURN_GENERATED_KEYS);
                
                ResultSet userRs = userStmt.getGeneratedKeys();
                int userId = -1;
                if (userRs.next()) {
                    userId = userRs.getInt(1);
                }
                userRs.close();
                userStmt.close();

                // Save student using the validated Student domain object
                student.setGradeId(gradeId);
                student.setGuardianId(guardianId);
                student.setUserId(userId);

                String studentQuery = "INSERT INTO student (student_id, first_name, last_name, dob, gender, nic, email, phone, address, grade_id, guardian_id, user_id, registration_date, is_active) VALUES ('" 
                        + student.getStudentId() + "', '" 
                        + student.getFirstName() + "', '" 
                        + student.getLastName() + "', '" 
                        + student.getDateOfBirth() + "', '" 
                        + student.getGender() + "', " 
                        + (student.getNic() == null || student.getNic().isEmpty() ? "NULL" : "'" + student.getNic() + "'") + ", '" 
                        + student.getEmail() + "', '" 
                        + student.getPhone() + "', '" 
                        + student.getAddress() + "', " 
                        + student.getGradeId() + ", " 
                        + student.getGuardianId() + ", " 
                        + student.getUserId() + ", CURDATE(), 1)";
                Statement studentStmt = conn.createStatement();
                studentStmt.executeUpdate(studentQuery);
                studentStmt.close();

                conn.commit();

                // Send email if requested
                if (chkSendEmail.isSelected()) {
                    try {
                        if (chkGenerateQR.isSelected()) {
                            SendQREmail.sendRegistrationEmailWithQR(
                                student.getEmail(),
                                student.getStudentId(),
                                student.getFullName(),
                                student.getGrade(),
                                username,
                                temporaryPassword
                            );
                        } else {
                            String message = "Dear " + student.getFullName() + ",\n\n" +
                                "Welcome to Central College Anuradhapura!\n\n" +
                                "Your student account has been created successfully.\n\n" +
                                "Student ID: " + student.getStudentId() + "\n" +
                                "Grade: " + student.getGrade() + "\n" +
                                "Username: " + username + "\n" +
                                "Temporary Password: " + temporaryPassword + "\n\n" +
                                "Please change your password after your first login.\n\n" +
                                "Best regards,\n" +
                                "Central College Anuradhapura";
                            
                            SendEmail.sendEmail(student.getEmail(), "Student Registration Confirmation", message);
                        }
                    } catch (Exception emailEx) {
                        JOptionIcon.showWarningDialog(this, "Student registered successfully but email failed: " + emailEx.getMessage());
                    }
                }

                JOptionIcon.showSuccessDialog(this, "Student registered successfully! Student ID: " + student.getStudentId());
                dispose();

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
                conn.close();
            }

        } catch (ValidationException e) {
            JOptionIcon.showWarningDialog(this, e.getUserFriendlyMessage());
        } catch (SQLException e) {
            JOptionIcon.showErrorDialog(this, "Error registering student: " + e.getMessage());
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Unexpected error: " + e.getMessage());
        }
    }
}