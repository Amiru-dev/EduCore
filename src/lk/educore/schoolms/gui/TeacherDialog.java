package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class TeacherDialog extends JDialog {

    private JTextField txtNIC, txtFirstName, txtLastName, txtDob, txtEmail, txtPhone, txtAddress, txtUsername, txtPassword, txtSalary, txtHireDate;
    private JComboBox<String> cmbGender, cmbType, cmbStatus;
    private JButton btnSave, btnCancel;
    private String teacherNIC;
    private boolean isEdit;

    public TeacherDialog(Window parent, String nic) {
        super(parent, nic == null ? "Add Teacher" : "Edit Teacher", ModalityType.APPLICATION_MODAL);
        this.teacherNIC = nic;
        this.isEdit = (nic != null);
        initComponents();
        if (isEdit) {
            loadTeacherData();
        }
    }

    private void initComponents() {
        setSize(500, 600);
        setLayout(new BorderLayout(10, 10));
        JPanel panel = new JPanel(new GridLayout(14, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtNIC = new JTextField();
        txtNIC.setEnabled(!isEdit);
        txtFirstName = new JTextField();
        txtLastName = new JTextField();
        txtDob = new JTextField();
        cmbGender = new JComboBox<>(new String[]{"Male", "Female"});
        txtEmail = new JTextField();
        txtPhone = new JTextField();
        txtAddress = new JTextField();
        cmbType = new JComboBox<>();
        txtUsername = new JTextField();
        txtPassword = new JTextField();
        txtSalary = new JTextField();
        txtHireDate = new JTextField();
        cmbStatus = new JComboBox<>();

        try {
            ResultSet rsType = MySQL.executeSearch("SELECT * FROM teacher_type");
            while (rsType.next()) {
                cmbType.addItem(rsType.getString("teacher_type_id") + " - " + rsType.getString("type_name"));
            }
            rsType.close();

            ResultSet rsStatus = MySQL.executeSearch("SELECT * FROM teacher_status");
            while (rsStatus.next()) {
                cmbStatus.addItem(rsStatus.getString("status_id") + " - " + rsStatus.getString("status_name"));
            }
            rsStatus.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        panel.add(new JLabel("NIC:"));
        panel.add(txtNIC);
        panel.add(new JLabel("First Name:"));
        panel.add(txtFirstName);
        panel.add(new JLabel("Last Name:"));
        panel.add(txtLastName);
        panel.add(new JLabel("Date of Birth (yyyy-MM-dd):"));
        panel.add(txtDob);
        panel.add(new JLabel("Gender:"));
        panel.add(cmbGender);
        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);
        panel.add(new JLabel("Phone:"));
        panel.add(txtPhone);
        panel.add(new JLabel("Address:"));
        panel.add(txtAddress);
        panel.add(new JLabel("Teacher Type:"));
        panel.add(cmbType);
        panel.add(new JLabel("Username:"));
        panel.add(txtUsername);
        panel.add(new JLabel("Password:"));
        panel.add(txtPassword);
        panel.add(new JLabel("Salary:"));
        panel.add(txtSalary);
        panel.add(new JLabel("Hire Date (yyyy-MM-dd):"));
        panel.add(txtHireDate);
        panel.add(new JLabel("Status:"));
        panel.add(cmbStatus);

        add(panel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSave = new JButton("Save");
        btnCancel = new JButton("Cancel");

        btnSave.addActionListener(e -> saveTeacher());
        btnCancel.addActionListener(e -> dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(getParent());
    }

    private void loadTeacherData() {
        try {
            ResultSet rs = MySQL.executeSearch("SELECT * FROM teacher WHERE NIC = '" + teacherNIC + "'");
            if (rs.next()) {
                txtNIC.setText(rs.getString("NIC"));
                txtFirstName.setText(rs.getString("first_name"));
                txtLastName.setText(rs.getString("last_name"));
                txtDob.setText(rs.getString("dob"));
                cmbGender.setSelectedItem(rs.getString("gender"));
                txtEmail.setText(rs.getString("email"));
                txtPhone.setText(rs.getString("phone"));
                txtAddress.setText(rs.getString("address"));
                txtUsername.setText(rs.getString("username"));
                txtPassword.setText(rs.getString("password"));
                txtSalary.setText(rs.getString("salary"));
                txtHireDate.setText(rs.getString("hire_date"));

                String typeId = rs.getString("teacher_type_id");
                for (int i = 0; i < cmbType.getItemCount(); i++) {
                    if (cmbType.getItemAt(i).startsWith(typeId + " - ")) {
                        cmbType.setSelectedIndex(i);
                        break;
                    }
                }

                String statusId = rs.getString("status_id");
                for (int i = 0; i < cmbStatus.getItemCount(); i++) {
                    if (cmbStatus.getItemAt(i).startsWith(statusId + " - ")) {
                        cmbStatus.setSelectedIndex(i);
                        break;
                    }
                }
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveTeacher() {
        try {
            String nic = txtNIC.getText().trim();
            String fName = txtFirstName.getText().trim();
            String lName = txtLastName.getText().trim();
            String dob = txtDob.getText().trim();
            String gender = cmbGender.getSelectedItem().toString();
            String email = txtEmail.getText().trim();
            String phone = txtPhone.getText().trim();
            String address = txtAddress.getText().trim();
            String type = cmbType.getSelectedItem().toString().split(" - ")[0];
            String user = txtUsername.getText().trim();
            String pass = txtPassword.getText().trim();
            String sal = txtSalary.getText().trim();
            String hire = txtHireDate.getText().trim();
            String stat = cmbStatus.getSelectedItem().toString().split(" - ")[0];

            if (nic.isEmpty() || fName.isEmpty() || lName.isEmpty() || dob.isEmpty() || phone.isEmpty() || sal.isEmpty() || hire.isEmpty()) {
                JOptionIcon.showWarningDialog(this, "Please fill all required fields.");
                return;
            }

            if (isEdit) {
                String query = "UPDATE teacher SET first_name='" + fName + "', last_name='" + lName + "', dob='" + dob + "', gender='" + gender
                        + "', email='" + email + "', phone='" + phone + "', address='" + address + "', teacher_type_id=" + type
                        + ", username='" + user + "', password='" + pass + "', salary=" + sal + ", hire_date='" + hire
                        + "', status_id=" + stat + " WHERE NIC='" + teacherNIC + "'";
                MySQL.executeIUD(query);
                JOptionIcon.showSuccessDialog(this, "Teacher updated successfully!");
            } else {
                String query = "INSERT INTO teacher (NIC, first_name, last_name, dob, gender, email, phone, address, teacher_type_id, username, password, salary, hire_date, status_id) "
                        + "VALUES ('" + nic + "', '" + fName + "', '" + lName + "', '" + dob + "', '" + gender + "', '" + email + "', '" + phone
                        + "', '" + address + "', " + type + ", '" + user + "', '" + pass + "', " + sal + ", '" + hire + "', " + stat + ")";
                MySQL.executeIUD(query);
                JOptionIcon.showSuccessDialog(this, "Teacher added successfully!");
            }
            dispose();
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error saving teacher: " + e.getMessage());
        }
    }
}
