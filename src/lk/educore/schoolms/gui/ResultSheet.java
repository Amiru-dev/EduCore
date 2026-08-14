package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

/**
 * Result Sheet Dialog
 * Dialog for viewing and managing exam results for a specific exam
 */
public class ResultSheet extends JDialog {

    private int examId;
    private String examName;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private JButton btnAddResult, btnEditResult, btnDeleteResult, btnRefresh, btnCalculateGrades, btnPrintReport;

    public ResultSheet(Frame parent, int examId, String examName) {
        super(parent, "Results: " + examName, true);
        this.examId = examId;
        this.examName = examName;
        setSize(700, 500);
        setLocationRelativeTo(parent);
        initComponents();
        loadResults();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        
        JLabel titleLabel = new JLabel("Results: " + examName);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        
        btnAddResult = new JButton("Add Result");
        btnAddResult.addActionListener(e -> addResult());
        
        btnEditResult = new JButton("Edit Result");
        btnEditResult.addActionListener(e -> editResult());
        
        btnDeleteResult = new JButton("Delete Result");
        btnDeleteResult.addActionListener(e -> deleteResult());
        
        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadResults());
        
        btnCalculateGrades = new JButton("Calculate Grades");
        btnCalculateGrades.addActionListener(e -> calculateGrades());
        
        btnPrintReport = new JButton("Print Report");
        btnPrintReport.addActionListener(e -> printReport());

        buttonPanel.add(btnAddResult);
        buttonPanel.add(btnEditResult);
        buttonPanel.add(btnDeleteResult);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnCalculateGrades);
        buttonPanel.add(btnPrintReport);
        
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(
            new Object[]{"Student ID", "Student Name", "Marks Obtained", "Grade", "Teacher"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        resultTable = new JTable(tableModel);
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultTable.setRowHeight(25);
        resultTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(resultTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadResults() {
        try {
            tableModel.setRowCount(0);
            
            Connection conn = MySQL.getConnection();
            String query = "SELECT r.student_id, s.first_name, s.last_name, r.marks_obtained, r.grade_letter, " +
                          "t.first_name as teacher_first, t.last_name as teacher_last " +
                          "FROM result r " +
                          "JOIN student s ON r.student_id = s.student_id " +
                          "LEFT JOIN teacher t ON r.teacher_NIC = t.NIC " +
                          "WHERE r.exam_id = ? " +
                          "ORDER BY r.marks_obtained DESC";
            
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, examId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String studentId = rs.getString("student_id");
                String studentName = rs.getString("first_name") + " " + rs.getString("last_name");
                double marks = rs.getDouble("marks_obtained");
                String grade = rs.getString("grade_letter");
                String teacher = rs.getString("teacher_first") + " " + rs.getString("teacher_last");
                
                tableModel.addRow(new Object[]{studentId, studentName, marks, grade, teacher});
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            JOptionIcon.showErrorDialog(this, "Error loading results: " + e.getMessage());
        }
    }

    private void addResult() {
        JOptionIcon.showInfoDialog(this, "Add Result - Coming Soon");
    }

    private void editResult() {
        int selectedRow = resultTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a result to edit.");
            return;
        }
        
        String studentId = tableModel.getValueAt(selectedRow, 0).toString();
        JOptionIcon.showInfoDialog(this, "Edit Result for: " + studentId + " - Coming Soon");
    }

    private void deleteResult() {
        int selectedRow = resultTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a result to delete.");
            return;
        }
        
        String studentId = tableModel.getValueAt(selectedRow, 0).toString();
        String studentName = tableModel.getValueAt(selectedRow, 1).toString();
        
        int confirm = JOptionIcon.showConfirmDialog(this, 
            "Delete result for " + studentName + " (" + studentId + ")?");
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = MySQL.getConnection();
                String query = "DELETE FROM result WHERE exam_id = ? AND student_id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, examId);
                stmt.setString(2, studentId);
                stmt.executeUpdate();
                stmt.close();
                conn.close();
                
                JOptionIcon.showSuccessDialog(this, "Result deleted successfully.");
                loadResults();
                
            } catch (SQLException e) {
                JOptionIcon.showErrorDialog(this, "Error deleting result: " + e.getMessage());
            }
        }
    }

    private void calculateGrades() {
        try {
            Connection conn = MySQL.getConnection();
            
            // Get max marks for this exam
            String maxMarksQuery = "SELECT max_marks FROM exam WHERE exam_id = ?";
            PreparedStatement maxStmt = conn.prepareStatement(maxMarksQuery);
            maxStmt.setInt(1, examId);
            ResultSet maxRs = maxStmt.executeQuery();
            
            if (maxRs.next()) {
                double maxMarks = maxRs.getDouble("max_marks");
                
                // Update grades based on marks
                String updateQuery = "UPDATE result SET grade_letter = CASE " +
                                   "WHEN marks_obtained >= ? * 0.9 THEN 'A' " +
                                   "WHEN marks_obtained >= ? * 0.8 THEN 'B' " +
                                   "WHEN marks_obtained >= ? * 0.7 THEN 'C' " +
                                   "WHEN marks_obtained >= ? * 0.6 THEN 'D' " +
                                   "ELSE 'F' " +
                                   "END WHERE exam_id = ?";
                
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setDouble(1, maxMarks);
                updateStmt.setDouble(2, maxMarks);
                updateStmt.setDouble(3, maxMarks);
                updateStmt.setDouble(4, maxMarks);
                updateStmt.setInt(5, examId);
                updateStmt.executeUpdate();
                updateStmt.close();
                
                JOptionIcon.showSuccessDialog(this, "Grades calculated successfully.");
                loadResults();
            }
            
            maxRs.close();
            maxStmt.close();
            conn.close();
            
        } catch (SQLException e) {
            JOptionIcon.showErrorDialog(this, "Error calculating grades: " + e.getMessage());
        }
    }

    private void printReport() {
        JOptionIcon.showInfoDialog(this, "Print Report - Coming Soon\n" +
            "This would generate a result sheet report using JasperReports.");
    }
}