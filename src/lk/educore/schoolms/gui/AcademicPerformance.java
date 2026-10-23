package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.model.UserSession;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

/**
 * Academic Performance Panel
 * Subject-wise analysis, student performance tracking, and academic reports
 */
public class AcademicPerformance extends JPanel {

    private JTable performanceTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbGrade;
    private JComboBox<String> cmbSubject;
    private JButton btnAnalyze, btnRefresh;

    public AcademicPerformance() {
        initComponents();
        loadPerformanceData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        
        JLabel titleLabel = new JLabel("Academic Performance");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        cmbGrade = new JComboBox<>();
        loadGrades();
        
        cmbSubject = new JComboBox<>();
        loadSubjects();
        
        btnAnalyze = new JButton("Analyze");
        btnAnalyze.addActionListener(e -> analyzePerformance());
        
        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadPerformanceData());

        filterPanel.add(new JLabel("Grade:"));
        filterPanel.add(cmbGrade);
        filterPanel.add(new JLabel("Subject:"));
        filterPanel.add(cmbSubject);
        filterPanel.add(btnAnalyze);
        filterPanel.add(btnRefresh);
        
        headerPanel.add(filterPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(
            new Object[]{"Student ID", "Student Name", "Subject", "Exam", "Marks", "Grade"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        performanceTable = new JTable(tableModel);
        performanceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        performanceTable.setRowHeight(25);
        performanceTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(performanceTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadGrades() {
        try {
            String query = "SELECT DISTINCT g.grade_name FROM grade g ";
            if (UserSession.get() != null && UserSession.get().isTeacher()) {
                query += "JOIN teacher_class tc ON g.grade_id = tc.grade_id " +
                         "WHERE tc.teacher_NIC = '" + UserSession.get().getTeacherNIC() + "' ";
            }
            query += "ORDER BY g.grade_name";

            ResultSet rs = MySQL.executeSearch(query);
            
            while (rs.next()) {
                cmbGrade.addItem(rs.getString("grade_name"));
            }
            
            rs.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSubjects() {
        try {
            String query = "SELECT DISTINCT s.subject_name FROM subject s ";
            if (UserSession.get() != null && UserSession.get().isTeacher()) {
                query += "JOIN teacher_subject ts ON s.subject_id = ts.subject_id " +
                         "WHERE ts.teacher_NIC = '" + UserSession.get().getTeacherNIC() + "' ";
            }
            query += "ORDER BY s.subject_name";

            ResultSet rs = MySQL.executeSearch(query);
            
            while (rs.next()) {
                cmbSubject.addItem(rs.getString("subject_name"));
            }
            
            rs.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPerformanceData() {
        try {
            tableModel.setRowCount(0);
            
            String query = "SELECT r.student_id, s.first_name, s.last_name, sub.subject_name, e.exam_name, " +
                          "r.marks_obtained, r.grade_letter " +
                          "FROM result r " +
                          "JOIN student s ON r.student_id = s.student_id " +
                          "JOIN exam e ON r.exam_id = e.exam_id " +
                          "JOIN subject sub ON e.subject_id = sub.subject_id ";
            if (UserSession.get() != null && UserSession.get().isTeacher()) {
                query += "JOIN teacher_subject ts ON e.subject_id = ts.subject_id AND e.grade_id = ts.grade_id " +
                         "WHERE ts.teacher_NIC = '" + UserSession.get().getTeacherNIC() + "' ";
            }
            query += "ORDER BY r.marks_obtained DESC " +
                     "LIMIT 50";

            ResultSet rs = MySQL.executeSearch(query);
            
            while (rs.next()) {
                String studentId = rs.getString("student_id");
                String studentName = rs.getString("first_name") + " " + rs.getString("last_name");
                String subject = rs.getString("subject_name");
                String exam = rs.getString("exam_name");
                double marks = rs.getDouble("marks_obtained");
                String grade = rs.getString("grade_letter");
                
                tableModel.addRow(new Object[]{studentId, studentName, subject, exam, marks, grade});
            }
            
            rs.close();
            
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading performance data: " + e.getMessage());
        }
    }

    private void analyzePerformance() {
        String grade = (String) cmbGrade.getSelectedItem();
        String subject = (String) cmbSubject.getSelectedItem();
        
        if (grade == null || subject == null) {
            JOptionIcon.showWarningDialog(this, "Please select both grade and subject.");
            return;
        }
        
        try {
            tableModel.setRowCount(0);
            
            String query = "SELECT r.student_id, s.first_name, s.last_name, sub.subject_name, e.exam_name, " +
                          "r.marks_obtained, r.grade_letter " +
                          "FROM result r " +
                          "JOIN student s ON r.student_id = s.student_id " +
                          "JOIN exam e ON r.exam_id = e.exam_id " +
                          "JOIN subject sub ON e.subject_id = sub.subject_id " +
                          "JOIN grade g ON e.grade_id = g.grade_id " +
                          "WHERE g.grade_name = '" + grade + "' AND sub.subject_name = '" + subject + "' " +
                          "ORDER BY r.marks_obtained DESC " +
                          "LIMIT 50";
            
            ResultSet rs = MySQL.executeSearch(query);
            
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                String studentId = rs.getString("student_id");
                String studentName = rs.getString("first_name") + " " + rs.getString("last_name");
                String sub = rs.getString("subject_name");
                String exam = rs.getString("exam_name");
                double marks = rs.getDouble("marks_obtained");
                String gradeLetter = rs.getString("grade_letter");
                
                tableModel.addRow(new Object[]{studentId, studentName, sub, exam, marks, gradeLetter});
            }
            
            rs.close();
            
            if (!hasData) {
                JOptionIcon.showInfoDialog(this, "No performance records found for " + grade + " - " + subject);
            }
            
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error analyzing performance data: " + e.getMessage());
        }
    }
}