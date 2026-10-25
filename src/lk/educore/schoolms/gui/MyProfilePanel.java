package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.model.UserSession;
import lk.educore.schoolms.util.PasswordUtil;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * My Profile Panel (Teacher Self-Service)
 * Displays the logged-in teacher's own information in a read-only view,
 * including personal details, employment info, and assigned classes/subjects.
 */
public class MyProfilePanel extends JPanel {

    private static Logger logger = Logger.getLogger(MyProfilePanel.class.getName());

    public MyProfilePanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);

        // Header
        JLabel titleLabel = new JLabel("My Profile");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Main content - split into left (info) and right (assignments)
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setBackground(Color.WHITE);

        contentPanel.add(buildPersonalInfoPanel());
        contentPanel.add(buildAssignmentsPanel());

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel buildPersonalInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel sectionTitle = new JLabel("Personal & Employment Details");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionTitle.setForeground(new Color(37, 99, 235));
        panel.add(sectionTitle, BorderLayout.NORTH);

        // Add Change Password button in a panel at the top
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        JButton btnChangePassword = new JButton("Change Password");
        btnChangePassword.setBackground(new Color(22, 163, 74));
        btnChangePassword.setForeground(Color.WHITE);
        btnChangePassword.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnChangePassword.setFocusPainted(false);
        btnChangePassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnChangePassword.addActionListener(e -> openChangePasswordDialog());
        buttonPanel.add(btnChangePassword);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(sectionTitle, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        UserSession session = UserSession.get();
        if (session == null || session.getTeacherNIC() == null) {
            gbc.gridx = 0; gbc.gridy = 0;
            detailsPanel.add(new JLabel("No teacher data available."), gbc);
            panel.add(detailsPanel, BorderLayout.CENTER);
            return panel;
        }

        try {
            ResultSet rs = MySQL.executeSearch(
                "SELECT t.NIC, t.first_name, t.last_name, t.dob, t.gender, t.email, t.phone, "
                + "t.address, tt.type_name, t.salary, t.hire_date, ts.status_name "
                + "FROM teacher t "
                + "JOIN teacher_type tt ON t.teacher_type_id = tt.teacher_type_id "
                + "JOIN teacher_status ts ON t.status_id = ts.status_id "
                + "WHERE t.NIC = '" + session.getTeacherNIC() + "'");

            if (rs.next()) {
                int row = 0;
                addDetailRow(detailsPanel, gbc, row++, "Full Name", rs.getString("first_name") + " " + rs.getString("last_name"));
                addDetailRow(detailsPanel, gbc, row++, "NIC", rs.getString("NIC"));
                addDetailRow(detailsPanel, gbc, row++, "Date of Birth", rs.getString("dob"));
                addDetailRow(detailsPanel, gbc, row++, "Gender", rs.getString("gender"));
                addDetailRow(detailsPanel, gbc, row++, "Email", rs.getString("email"));
                addDetailRow(detailsPanel, gbc, row++, "Phone", rs.getString("phone"));
                addDetailRow(detailsPanel, gbc, row++, "Address", rs.getString("address"));
                addDetailRow(detailsPanel, gbc, row++, "Role", rs.getString("type_name"));
                addDetailRow(detailsPanel, gbc, row++, "Salary", String.format("Rs. %,.2f", rs.getDouble("salary")));
                addDetailRow(detailsPanel, gbc, row++, "Hire Date", rs.getString("hire_date"));
                addDetailRow(detailsPanel, gbc, row++, "Status", rs.getString("status_name"));
            }
            rs.close();
        } catch (Exception e) {
            gbc.gridx = 0; gbc.gridy = 0;
            detailsPanel.add(new JLabel("Error loading profile: " + e.getMessage()), gbc);
        }

        panel.add(detailsPanel, BorderLayout.CENTER);
        return panel;
    }

    private void addDetailRow(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        JLabel lblKey = new JLabel(label + ":");
        lblKey.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblKey.setForeground(new Color(100, 116, 139));
        panel.add(lblKey, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        JLabel lblValue = new JLabel(value != null ? value : "—");
        lblValue.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(lblValue, gbc);
    }

    private JPanel buildAssignmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel sectionTitle = new JLabel("My Assignments (Current Year)");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionTitle.setForeground(new Color(37, 99, 235));
        panel.add(sectionTitle, BorderLayout.NORTH);

        JPanel listsPanel = new JPanel(new GridLayout(2, 1, 0, 15));
        listsPanel.setBackground(Color.WHITE);

        // Classes list
        JPanel classesCard = new JPanel(new BorderLayout(5, 5));
        classesCard.setBackground(new Color(248, 250, 252));
        classesCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        JLabel classesTitle = new JLabel("Assigned Classes");
        classesTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        classesCard.add(classesTitle, BorderLayout.NORTH);

        DefaultListModel<String> classListModel = new DefaultListModel<>();
        JList<String> classList = new JList<>(classListModel);
        classList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        classList.setBackground(new Color(248, 250, 252));
        classesCard.add(new JScrollPane(classList), BorderLayout.CENTER);
        listsPanel.add(classesCard);

        // Subjects list
        JPanel subjectsCard = new JPanel(new BorderLayout(5, 5));
        subjectsCard.setBackground(new Color(248, 250, 252));
        subjectsCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        JLabel subjectsTitle = new JLabel("Assigned Subjects");
        subjectsTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        subjectsCard.add(subjectsTitle, BorderLayout.NORTH);

        DefaultListModel<String> subjectListModel = new DefaultListModel<>();
        JList<String> subjectList = new JList<>(subjectListModel);
        subjectList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subjectList.setBackground(new Color(248, 250, 252));
        subjectsCard.add(new JScrollPane(subjectList), BorderLayout.CENTER);
        listsPanel.add(subjectsCard);

        panel.add(listsPanel, BorderLayout.CENTER);

        // Load data
        UserSession session = UserSession.get();
        if (session != null && session.getTeacherNIC() != null) {
            try {
                ResultSet rs = MySQL.executeSearch(
                    "SELECT g.grade_name, g.section, tc.is_class_teacher "
                    + "FROM teacher_class tc "
                    + "JOIN grade g ON tc.grade_id = g.grade_id "
                    + "WHERE tc.teacher_NIC = '" + session.getTeacherNIC() + "' "
                    + "AND tc.academic_year = YEAR(CURDATE()) "
                    + "ORDER BY g.grade_name, g.section");
                while (rs.next()) {
                    String entry = rs.getString("grade_name") + " " + rs.getString("section");
                    if (rs.getBoolean("is_class_teacher")) {
                        entry += "  - Class Teacher";
                    }
                    classListModel.addElement(entry);
                }
                rs.close();

                if (classListModel.isEmpty()) {
                    classListModel.addElement("No classes assigned yet.");
                }

                ResultSet rs2 = MySQL.executeSearch(
                    "SELECT sub.subject_name, g.grade_name, g.section "
                    + "FROM teacher_subject ts "
                    + "JOIN subject sub ON ts.subject_id = sub.subject_id "
                    + "JOIN grade g ON ts.grade_id = g.grade_id "
                    + "WHERE ts.teacher_NIC = '" + session.getTeacherNIC() + "' "
                    + "AND ts.academic_year = YEAR(CURDATE()) "
                    + "ORDER BY g.grade_name, sub.subject_name");
                while (rs2.next()) {
                    subjectListModel.addElement(
                        rs2.getString("subject_name") + " - " +
                        rs2.getString("grade_name") + " " + rs2.getString("section"));
                }
                rs2.close();

                if (subjectListModel.isEmpty()) {
                    subjectListModel.addElement("No subjects assigned yet.");
                }

            } catch (Exception e) {
                classListModel.addElement("Error: " + e.getMessage());
            }
        }

        return panel;
    }

    private void openChangePasswordDialog() {
        UserSession session = UserSession.get();
        if (session == null || session.getUsername() == null) {
            JOptionIcon.showWarningDialog(this, "User session not found!");
            return;
        }

        String username = session.getUsername();
        String teacherNIC = session.getTeacherNIC();

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Change Password", true);
        dialog.setSize(450, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Change your password:");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        p.add(lblTitle, gbc);

        JLabel lblCurrent = new JLabel("Current Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        p.add(lblCurrent, gbc);

        JPasswordField txtCurrent = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        p.add(txtCurrent, gbc);

        JLabel lblNew = new JLabel("New Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        p.add(lblNew, gbc);

        JPasswordField txtNew = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        p.add(txtNew, gbc);

        JLabel lblConfirm = new JLabel("Confirm New Password:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        p.add(lblConfirm, gbc);

        JPasswordField txtConfirm = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        p.add(txtConfirm, gbc);

        dialog.add(p, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton btnSave = new JButton("Change Password");
        btnSave.setBackground(new Color(39, 174, 96));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSave.addActionListener(ev -> {
            String currentPassword = new String(txtCurrent.getPassword());
            String newPassword = new String(txtNew.getPassword());
            String confirmPassword = new String(txtConfirm.getPassword());

            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                JOptionIcon.showWarningDialog(dialog, "All fields are required!");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                JOptionIcon.showWarningDialog(dialog, "New passwords do not match!");
                return;
            }

            if (newPassword.length() < 6) {
                JOptionIcon.showWarningDialog(dialog, "Password must be at least 6 characters!");
                return;
            }

            try {
                // Verify current password
                ResultSet rs = MySQL.executeSearch(
                    "SELECT password FROM user WHERE username = '" + username + "'");
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    
                    // Use checkPassword instead of hashPassword for verification
                    if (!PasswordUtil.checkPassword(currentPassword, storedPassword)) {
                        JOptionIcon.showWarningDialog(dialog, "Current password is incorrect!");
                        rs.close();
                        return;
                    }
                }
                rs.close();

                // Update password in user table
                String hashedNew = PasswordUtil.hashPassword(newPassword);
                MySQL.executeIUD("UPDATE user SET password='" + hashedNew + "' WHERE username = '" + username + "'");

                // Also update password in teacher table for consistency
                if (teacherNIC != null) {
                    MySQL.executeIUD("UPDATE teacher SET password='" + hashedNew + "' WHERE NIC = '" + teacherNIC + "'");
                }

                logger.log(Level.INFO, "Teacher " + username + " (NIC: " + teacherNIC + ") changed their password successfully.");

                JOptionIcon.showSuccessDialog(dialog, "Password changed successfully!");
                dialog.dispose();
            } catch (Exception ex) {
                JOptionIcon.showErrorDialog(dialog, "Failed to change password: " + ex.getMessage());
            }
        });

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(ev -> dialog.dispose());

        buttons.add(btnCancel);
        buttons.add(btnSave);
        dialog.add(buttons, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
