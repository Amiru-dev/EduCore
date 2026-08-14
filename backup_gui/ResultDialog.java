package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * Result Dialog
 * Dialog for viewing individual student result details
 */
public class ResultDialog extends JDialog {

    private int resultId;
    private JLabel lblStudentId, lblStudentName, lblExamName, lblSubject, lblMarks, lblGrade, lblTeacher, lblRemarks;

    public ResultDialog(Frame parent, int resultId) {
        super(parent, "Result Details", true);
        this.resultId = resultId;
        initComponents();
        loadResultDetails();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(400, 350);
        setLayout(new BorderLayout(10, 10));

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Result details panel
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Student ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        detailsPanel.add(new JLabel("Student ID:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblStudentId = new JLabel();
        lblStudentId.setFont(new Font("Segoe UI", Font.BOLD, 12));
        detailsPanel.add(lblStudentId, gbc);

        // Student Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        detailsPanel.add(new JLabel("Student Name:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblStudentName = new JLabel();
        detailsPanel.add(lblStudentName, gbc);

        // Exam Name
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        detailsPanel.add(new JLabel("Exam Name:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblExamName = new JLabel();
        detailsPanel.add(lblExamName, gbc);

        // Subject
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        detailsPanel.add(new JLabel("Subject:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblSubject = new JLabel();
        detailsPanel.add(lblSubject, gbc);

        // Marks
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        detailsPanel.add(new JLabel("Marks Obtained:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblMarks = new JLabel();
        lblMarks.setFont(new Font("Segoe UI", Font.BOLD, 14));
        detailsPanel.add(lblMarks, gbc);

        // Grade
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        detailsPanel.add(new JLabel("Grade:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblGrade = new JLabel();
        lblGrade.setFont(new Font("Segoe UI", Font.BOLD, 16));
        detailsPanel.add(lblGrade, gbc);

        // Teacher
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.0;
        detailsPanel.add(new JLabel("Teacher:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblTeacher = new JLabel();
        detailsPanel.add(lblTeacher, gbc);

        // Remarks
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0.0;
        detailsPanel.add(new JLabel("Remarks:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblRemarks = new JLabel();
        detailsPanel.add(lblRemarks, gbc);

        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());

        buttonPanel.add(btnClose);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadResultDetails() {
        try {
            Connection conn = MySQL.getConnection();
            String query = "SELECT r.student_id, s.first_name, s.last_name, e.exam_name, sub.subject_name, " +
                          "r.marks_obtained, r.grade_letter, t.first_name as teacher_first, t.last_name as teacher_last, r.remarks " +
                          "FROM result r " +
                          "JOIN student s ON r.student_id = s.student_id " +
                          "JOIN exam e ON r.exam_id = e.exam_id " +
                          "JOIN subject sub ON e.subject_id = sub.subject_id " +
                          "LEFT JOIN teacher t ON r.teacher_NIC = t.NIC " +
                          "WHERE r.result_id = ?";
            
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, resultId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                lblStudentId.setText(rs.getString("student_id"));
                lblStudentName.setText(rs.getString("first_name") + " " + rs.getString("last_name"));
                lblExamName.setText(rs.getString("exam_name"));
                lblSubject.setText(rs.getString("subject_name"));
                lblMarks.setText(String.valueOf(rs.getDouble("marks_obtained")));
                lblGrade.setText(rs.getString("grade_letter"));
                lblTeacher.setText(rs.getString("teacher_first") + " " + rs.getString("teacher_last"));
                lblRemarks.setText(rs.getString("remarks"));
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            JOptionIcon.showErrorDialog(this, "Error loading result details: " + e.getMessage());
        }
    }
}