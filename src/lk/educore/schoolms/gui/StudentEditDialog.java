package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.model.Student;
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
 * Student Edit Dialog
 * Edit form for updating existing student details
 */
public class StudentEditDialog extends JDialog {

    private int guardianId = -1;

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
    private JDateChooser dateChooserDOB;
    private JRadioButton rdoMale;
    private JRadioButton rdoFemale;
    private ButtonGroup genderGroup;

    public StudentEditDialog(Frame parent, String studentId) {
        super(parent, "Edit Student", true);
        initComponents();
        txtStudentId.setText(studentId);
        loadStudentData(studentId);
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
        JLabel titleLabel = new JLabel("Edit Student");
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

        // Guardian First Name
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Guardian First Name:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtGuardianFirstName = new JTextField(20);
        formPanel.add(txtGuardianFirstName, gbc);

        // Guardian Last Name
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Guardian Last Name:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtGuardianLastName = new JTextField(20);
        formPanel.add(txtGuardianLastName, gbc);

        // Guardian NIC
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Guardian NIC:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtGuardianNIC = new JTextField(20);
        formPanel.add(txtGuardianNIC, gbc);

        // Guardian Relationship
        gbc.gridx = 0;
        gbc.gridy = 14;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Relationship:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cmbGuardianRelationship = new JComboBox<>(new String[]{"Father", "Mother", "Guardian"});
        formPanel.add(cmbGuardianRelationship, gbc);

        // Guardian Phone
        gbc.gridx = 0;
        gbc.gridy = 15;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Guardian Phone:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtGuardianPhone = new JTextField(20);
        formPanel.add(txtGuardianPhone, gbc);

        // Guardian Email
        gbc.gridx = 0;
        gbc.gridy = 16;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Guardian Email:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtGuardianEmail = new JTextField(20);
        formPanel.add(txtGuardianEmail, gbc);

        // Guardian Address
        gbc.gridx = 0;
        gbc.gridy = 17;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Guardian Address:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtGuardianAddress = new JTextField(20);
        formPanel.add(txtGuardianAddress, gbc);

        // Guardian Occupation
        gbc.gridx = 0;
        gbc.gridy = 18;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Guardian Occupation:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtGuardianOccupation = new JTextField(20);
        formPanel.add(txtGuardianOccupation, gbc);

        // Email and QR options
        gbc.gridx = 0;
        gbc.gridy = 19;
        gbc.gridwidth = 2;
        gbc.weightx = 0.0;
        // Checkboxes removed for Edit dialog
        formPanel.add(new JLabel(), gbc);

        JScrollPane formScroll = new JScrollPane(formPanel);
        mainPanel.add(formScroll, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnSave = new JButton("Update Student");
        btnSave.addActionListener(e -> updateStudent());
        
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

    private void loadStudentData(String studentId) {
        try {
            String query = "SELECT s.*, g.grade_name, g.section, gd.first_name as gd_fname, gd.last_name as gd_lname, gd.nic as gd_nic, gd.relationship, gd.phone as gd_phone, gd.email as gd_email, gd.address as gd_address, gd.occupation " +
                           "FROM student s " +
                           "INNER JOIN grade g ON s.grade_id = g.grade_id " +
                           "INNER JOIN guardian gd ON s.guardian_id = gd.guardian_id " +
                           "WHERE s.student_id = '" + studentId + "'";
            ResultSet rs = MySQL.executeSearch(query);
            
            if (rs.next()) {
                Student student = new Student();
                student.setStudentId(rs.getString("student_id"));
                student.setFirstName(rs.getString("first_name"));
                student.setLastName(rs.getString("last_name"));
                student.setDateOfBirth(rs.getString("dob"));
                student.setGender(rs.getString("gender"));
                student.setNic(rs.getString("nic"));
                student.setEmail(rs.getString("email"));
                student.setPhone(rs.getString("phone"));
                student.setAddress(rs.getString("address"));
                student.setStatus(rs.getBoolean("is_active") ? "Active" : "Inactive");
                
                String gradeName = rs.getString("grade_name");
                String section = rs.getString("section");
                String gradeCombo = gradeName;
                if (section != null && !section.isEmpty()) {
                    gradeCombo += " - " + section;
                }
                student.setGrade(gradeCombo);

                txtFirstName.setText(student.getFirstName());
                txtLastName.setText(student.getLastName());
                
                if (student.getDateOfBirth() != null && !student.getDateOfBirth().isEmpty()) {
                    dateChooserDOB.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(student.getDateOfBirth()));
                }
                
                if ("Female".equalsIgnoreCase(student.getGender())) {
                    rdoFemale.setSelected(true);
                } else {
                    rdoMale.setSelected(true);
                }
                
                txtNIC.setText(student.getNic() != null ? student.getNic() : "");
                txtEmail.setText(student.getEmail());
                txtPhone.setText(student.getPhone());
                txtAddress.setText(student.getAddress());
                cmbGrade.setSelectedItem(student.getGrade());
                
                this.guardianId = rs.getInt("guardian_id");
                
                txtGuardianFirstName.setText(rs.getString("gd_fname"));
                txtGuardianLastName.setText(rs.getString("gd_lname"));
                txtGuardianNIC.setText(rs.getString("gd_nic") != null ? rs.getString("gd_nic") : "");
                cmbGuardianRelationship.setSelectedItem(rs.getString("relationship"));
                txtGuardianPhone.setText(rs.getString("gd_phone"));
                txtGuardianEmail.setText(rs.getString("gd_email") != null ? rs.getString("gd_email") : "");
                txtGuardianAddress.setText(rs.getString("gd_address"));
                txtGuardianOccupation.setText(rs.getString("occupation"));
            }
            
            rs.close();
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading student data: " + e.getMessage());
        }
    }

    private void updateStudent() {
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

            // 1. Build Student domain object
            Student student = new Student(studentId, firstName, lastName, email, phone, nic, dob, gender, grade, address);

            // 2. Validate Student object (uses polymorphic Person & Student validation)
            student.validate();

            // Validate guardian inputs
            DataValidator.isRequired(guardianFirstName, "Guardian First Name");
            DataValidator.isRequired(guardianLastName, "Guardian Last Name");
            DataValidator.isRequired(guardianPhone, "Guardian Phone");
            if (!guardianNIC.isEmpty()) DataValidator.isValidNIC(guardianNIC);
            if (!guardianEmail.isEmpty()) DataValidator.isValidEmail(guardianEmail);

            Connection conn = MySQL.getConnection();
            conn.setAutoCommit(false);

            try {
                // Update guardian
                String guardianQuery = "UPDATE guardian SET first_name = '" + guardianFirstName + "', " +
                        "last_name = '" + guardianLastName + "', " +
                        "nic = " + (guardianNIC.isEmpty() ? "NULL" : "'" + guardianNIC + "'") + ", " +
                        "relationship = '" + relationship + "', " +
                        "phone = '" + guardianPhone + "', " +
                        "email = " + (guardianEmail.isEmpty() ? "NULL" : "'" + guardianEmail + "'") + ", " +
                        "address = '" + guardianAddress + "', " +
                        "occupation = '" + guardianOccupation + "' " +
                        "WHERE guardian_id = " + this.guardianId;
                Statement guardianStmt = conn.createStatement();
                guardianStmt.executeUpdate(guardianQuery);
                guardianStmt.close();

                // Get grade ID
                String gradeName = grade;
                String section = null;
                if (grade != null && grade.contains(" - ")) {
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

                // 3. Save Student object
                student.setGradeId(gradeId);

                String studentQuery = "UPDATE student SET first_name = '" + student.getFirstName() + "', " +
                        "last_name = '" + student.getLastName() + "', " +
                        "dob = '" + student.getDateOfBirth() + "', " +
                        "gender = '" + student.getGender() + "', " +
                        "nic = " + (student.getNic() == null || student.getNic().isEmpty() ? "NULL" : "'" + student.getNic() + "'") + ", " +
                        "email = '" + student.getEmail() + "', " +
                        "phone = '" + student.getPhone() + "', " +
                        "address = '" + student.getAddress() + "', " +
                        "grade_id = " + student.getGradeId() + " " +
                        "WHERE student_id = '" + student.getStudentId() + "'";
                Statement studentStmt = conn.createStatement();
                studentStmt.executeUpdate(studentQuery);
                studentStmt.close();

                conn.commit();

                JOptionIcon.showSuccessDialog(this, "Student updated successfully! Student ID: " + student.getStudentId());
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
            JOptionIcon.showErrorDialog(this, "Error updating student: " + e.getMessage());
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Unexpected error: " + e.getMessage());
        }
    }
}