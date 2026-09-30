package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.print.*;
import java.io.File;
import java.sql.*;
import java.util.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperExportManager;

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
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        headerPanel.add(titleLabel, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 10));
        
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
        
        headerPanel.add(buttonPanel, BorderLayout.CENTER);
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
            
            ResultSet rs = MySQL.executeSearch("SELECT r.student_id, s.first_name, s.last_name, r.marks_obtained, r.grade_letter, " +
                          "t.first_name as teacher_first, t.last_name as teacher_last " +
                          "FROM result r " +
                          "JOIN student s ON r.student_id = s.student_id " +
                          "LEFT JOIN teacher t ON r.teacher_NIC = t.NIC " +
                          "WHERE r.exam_id = '" + examId + "' " +
                          "ORDER BY r.marks_obtained DESC");
            
            while (rs.next()) {
                String studentId = rs.getString("student_id");
                String studentName = rs.getString("first_name") + " " + rs.getString("last_name");
                double marks = rs.getDouble("marks_obtained");
                String grade = rs.getString("grade_letter");
                String teacher = rs.getString("teacher_first") + " " + rs.getString("teacher_last");
                
                tableModel.addRow(new Object[]{studentId, studentName, marks, grade, teacher});
            }
            
            rs.close();
            
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading results: " + e.getMessage());
        }
    }

    private void addResult() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Result", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Student selection
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Student:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JComboBox<String> cmbStudent = new JComboBox<>();
        formPanel.add(cmbStudent, gbc);

        // Load students in the exam's grade
        try {
            ResultSet examRs = MySQL.executeSearch("SELECT grade_id FROM exam WHERE exam_id = '" + examId + "'");
            if (examRs.next()) {
                int gradeId = examRs.getInt("grade_id");
                examRs.close();

                ResultSet studentRs = MySQL.executeSearch("SELECT student_id, first_name, last_name FROM student WHERE grade_id = '" + gradeId + "' AND is_active = 1 ORDER BY first_name, last_name");
                while (studentRs.next()) {
                    String studentId = studentRs.getString("student_id");
                    String studentName = studentRs.getString("first_name") + " " + studentRs.getString("last_name");
                    cmbStudent.addItem(studentId + " - " + studentName);
                }
                studentRs.close();
            }
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading students: " + e.getMessage());
        }

        // Marks obtained
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Marks Obtained:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField txtMarks = new JTextField(20);
        formPanel.add(txtMarks, gbc);

        // Remarks
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Remarks:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.gridheight = 2;
        JTextArea txtRemarks = new JTextArea(3, 20);
        JScrollPane remarksScrollPane = new JScrollPane(txtRemarks);
        formPanel.add(remarksScrollPane, gbc);

        dialog.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        btnSave.addActionListener(e -> {
            String selectedStudent = (String) cmbStudent.getSelectedItem();
            String marksStr = txtMarks.getText().trim();
            String remarks = txtRemarks.getText().trim();

            if (selectedStudent == null) {
                JOptionIcon.showWarningDialog(dialog, "Please select a student.");
                return;
            }

            if (marksStr.isEmpty()) {
                JOptionIcon.showWarningDialog(dialog, "Please enter marks obtained.");
                return;
            }

            try {
                double marks = Double.parseDouble(marksStr);

                // Get max marks for this exam
                ResultSet maxRs = MySQL.executeSearch("SELECT max_marks FROM exam WHERE exam_id = '" + examId + "'");
                if (maxRs.next()) {
                    double maxMarks = maxRs.getDouble("max_marks");
                    maxRs.close();

                    if (marks < 0 || marks > maxMarks) {
                        JOptionIcon.showWarningDialog(dialog, "Marks must be between 0 and " + maxMarks);
                        return;
                    }

                    // Calculate grade
                    String grade = calculateGrade(marks, maxMarks);

                    // Extract student ID
                    String studentId = selectedStudent.split(" - ")[0];

                    // Check if result already exists
                    ResultSet checkRs = MySQL.executeSearch("SELECT result_id FROM result WHERE exam_id = '" + examId + "' AND student_id = '" + studentId + "'");
                    if (checkRs.next()) {
                        JOptionIcon.showWarningDialog(dialog, "Result already exists for this student. Use Edit instead.");
                        checkRs.close();
                        return;
                    }
                    checkRs.close();

                    // Insert result
                    String query = "INSERT INTO result (exam_id, student_id, marks_obtained, grade_letter, remarks) VALUES ('" +
                            examId + "', '" + studentId + "', '" + marks + "', '" + grade + "', '" + remarks + "')";

                    MySQL.executeIUD(query);

                    JOptionIcon.showSuccessDialog(dialog, "Result added successfully!");
                    dialog.dispose();
                    loadResults();
                }
            } catch (NumberFormatException ex) {
                JOptionIcon.showWarningDialog(dialog, "Marks must be a valid number.");
            } catch (Exception ex) {
                JOptionIcon.showErrorDialog(dialog, "Error adding result: " + ex.getMessage());
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private String calculateGrade(double marks, double maxMarks) {
        double percentage = (marks / maxMarks) * 100;
        if (percentage >= 75) return "A";
        if (percentage >= 65) return "B";
        if (percentage >= 50) return "C";
        if (percentage >= 35) return "S";
        return "W";
    }

    private void editResult() {
        int selectedRow = resultTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a result to edit.");
            return;
        }

        String studentId = resultTable.getValueAt(selectedRow, 0).toString();
        String studentName = resultTable.getValueAt(selectedRow, 1).toString();
        double currentMarks = (Double) resultTable.getValueAt(selectedRow, 2);
        String currentGrade = resultTable.getValueAt(selectedRow, 3).toString();

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Result", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Student info (read-only)
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Student:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField txtStudent = new JTextField(studentId + " - " + studentName);
        txtStudent.setEditable(false);
        formPanel.add(txtStudent, gbc);

        // Current grade (read-only)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Current Grade:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField txtCurrentGrade = new JTextField(currentGrade);
        txtCurrentGrade.setEditable(false);
        formPanel.add(txtCurrentGrade, gbc);

        // Marks obtained
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Marks Obtained:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField txtMarks = new JTextField(String.valueOf(currentMarks), 20);
        formPanel.add(txtMarks, gbc);

        // Remarks
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Remarks:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.gridheight = 2;
        JTextArea txtRemarks = new JTextArea(3, 20);
        JScrollPane remarksScrollPane = new JScrollPane(txtRemarks);
        formPanel.add(remarksScrollPane, gbc);

        dialog.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        btnSave.addActionListener(e -> {
            String marksStr = txtMarks.getText().trim();
            String remarks = txtRemarks.getText().trim();

            if (marksStr.isEmpty()) {
                JOptionIcon.showWarningDialog(dialog, "Please enter marks obtained.");
                return;
            }

            try {
                double marks = Double.parseDouble(marksStr);

                // Get max marks for this exam
                ResultSet maxRs = MySQL.executeSearch("SELECT max_marks FROM exam WHERE exam_id = '" + examId + "'");
                if (maxRs.next()) {
                    double maxMarks = maxRs.getDouble("max_marks");
                    maxRs.close();

                    if (marks < 0 || marks > maxMarks) {
                        JOptionIcon.showWarningDialog(dialog, "Marks must be between 0 and " + maxMarks);
                        return;
                    }

                    // Calculate new grade
                    String newGrade = calculateGrade(marks, maxMarks);

                    // Update result
                    String query = "UPDATE result SET marks_obtained = '" + marks + "', grade_letter = '" + newGrade + "', remarks = '" + remarks + "' WHERE exam_id = '" + examId + "' AND student_id = '" + studentId + "'";

                    MySQL.executeIUD(query);

                    JOptionIcon.showSuccessDialog(dialog, "Result updated successfully!");
                    dialog.dispose();
                    loadResults();
                }
            } catch (NumberFormatException ex) {
                JOptionIcon.showWarningDialog(dialog, "Marks must be a valid number.");
            } catch (Exception ex) {
                JOptionIcon.showErrorDialog(dialog, "Error updating result: " + ex.getMessage());
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
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
                MySQL.executeIUD("DELETE FROM result WHERE exam_id = '" + examId + "' AND student_id = '" + studentId + "'");
                
                JOptionIcon.showSuccessDialog(this, "Result deleted successfully.");
                loadResults();
                
            } catch (Exception e) {
                JOptionIcon.showErrorDialog(this, "Error deleting result: " + e.getMessage());
            }
        }
    }

    private void calculateGrades() {
        try {
            // Get max marks for this exam
            ResultSet maxRs = MySQL.executeSearch("SELECT max_marks FROM exam WHERE exam_id = '" + examId + "'");
            
            if (maxRs.next()) {
                double maxMarks = maxRs.getDouble("max_marks");
                
                // Update grades based on marks
                String updateQuery = "UPDATE result SET grade_letter = CASE " +
                                   "WHEN marks_obtained >= " + maxMarks + " * 0.75 THEN 'A' " +
                                   "WHEN marks_obtained >= " + maxMarks + " * 0.65 THEN 'B' " +
                                   "WHEN marks_obtained >= " + maxMarks + " * 0.50 THEN 'C' " +
                                   "WHEN marks_obtained >= " + maxMarks + " * 0.35 THEN 'S' " +
                                   "ELSE 'W' " +
                                   "END WHERE exam_id = '" + examId + "'";
                
                MySQL.executeIUD(updateQuery);
                
                JOptionIcon.showSuccessDialog(this, "Grades calculated successfully.");
                loadResults();
            }
            
            maxRs.close();
            
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error calculating grades: " + e.getMessage());
        }
    }

    private void printReport() {
        try {
            // Get exam details
            ResultSet examRs = MySQL.executeSearch("SELECT e.exam_name, et.exam_type_name, sub.subject_name, g.grade_name, e.exam_date, e.max_marks " +
                    "FROM exam e " +
                    "JOIN exam_type et ON e.exam_type_id = et.exam_type_id " +
                    "JOIN subject sub ON e.subject_id = sub.subject_id " +
                    "JOIN grade g ON e.grade_id = g.grade_id " +
                    "WHERE e.exam_id = '" + examId + "'");

            if (examRs.next()) {
                String examName = examRs.getString("exam_name");
                String examType = examRs.getString("exam_type_name");
                String subject = examRs.getString("subject_name");
                String grade = examRs.getString("grade_name");
                String examDate = examRs.getString("exam_date");
                double maxMarks = examRs.getDouble("max_marks");
                examRs.close();

                // Try to use JasperReports if template exists
                File jasperFile = new File("reports/ResultSheet.jasper");
                File jrxmlFile = new File("reports/ResultSheet.jrxml");

                if (jasperFile.exists() || jrxmlFile.exists()) {
                    // Create parameters for JasperReports
                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put("examId", examId);
                    parameters.put("examName", examName);
                    parameters.put("examType", examType);
                    parameters.put("subject", subject);
                    parameters.put("grade", grade);
                    parameters.put("examDate", examDate);
                    parameters.put("maxMarks", maxMarks);
                    parameters.put("schoolName", "Central College Anuradhapura");

                    try {
                        JasperReport jasperReport;
                        if (jasperFile.exists()) {
                            jasperReport = (JasperReport) JRLoader.loadObject(jasperFile);
                        } else {
                            jasperReport = JasperCompileManager.compileReport(jrxmlFile.getAbsolutePath());
                        }

                        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

                        // Create PDF file
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setDialogTitle("Save Result Sheet as PDF");
                        fileChooser.setSelectedFile(new java.io.File(examName + "_ResultSheet.pdf"));

                        int userSelection = fileChooser.showSaveDialog(this);
                        if (userSelection == JFileChooser.APPROVE_OPTION) {
                            java.io.File fileToSave = fileChooser.getSelectedFile();
                            if (!fileToSave.getName().toLowerCase().endsWith(".pdf")) {
                                fileToSave = new File(fileToSave.getParent(), fileToSave.getName() + ".pdf");
                            }

                            // Export to PDF using JasperReports
                            JasperExportManager.exportReportToPdfFile(jasperPrint, fileToSave.getAbsolutePath());

                            JOptionIcon.showSuccessDialog(this, "Result sheet saved as PDF:\n" + fileToSave.getAbsolutePath());
                        }
                        return;

                    } catch (Exception e) {
                        System.out.println("JasperReports error: " + e.getMessage());
                        // Fall back to simple receipt if JasperReports fails
                    }
                }

                // Fallback: Create printable dialog
                JDialog printDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Result Sheet - " + examName, true);
                printDialog.setSize(800, 600);
                printDialog.setLocationRelativeTo(this);
                printDialog.setLayout(new BorderLayout(10, 10));

                // Main panel with printable content
                JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
                mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                // Header
                JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
                headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

                JLabel schoolLabel = new JLabel("Central College Anuradhapura", SwingConstants.CENTER);
                schoolLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
                headerPanel.add(schoolLabel, BorderLayout.NORTH);

                JLabel titleLabel = new JLabel("EXAMINATION RESULT SHEET", SwingConstants.CENTER);
                titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
                headerPanel.add(titleLabel, BorderLayout.CENTER);

                mainPanel.add(headerPanel, BorderLayout.NORTH);

                // Exam details
                JPanel detailsPanel = new JPanel(new GridLayout(6, 2, 5, 5));
                detailsPanel.setBorder(BorderFactory.createTitledBorder("Exam Details"));

                detailsPanel.add(new JLabel("Exam Name:"));
                detailsPanel.add(new JLabel(examName));
                detailsPanel.add(new JLabel("Exam Type:"));
                detailsPanel.add(new JLabel(examType));
                detailsPanel.add(new JLabel("Subject:"));
                detailsPanel.add(new JLabel(subject));
                detailsPanel.add(new JLabel("Grade:"));
                detailsPanel.add(new JLabel(grade));
                detailsPanel.add(new JLabel("Exam Date:"));
                detailsPanel.add(new JLabel(examDate));
                detailsPanel.add(new JLabel("Maximum Marks:"));
                detailsPanel.add(new JLabel(String.valueOf(maxMarks)));

                mainPanel.add(detailsPanel, BorderLayout.CENTER);

                // Results table
                DefaultTableModel printModel = new DefaultTableModel(
                    new Object[]{"Student ID", "Student Name", "Marks Obtained", "Grade", "Remarks"}, 0
                );

                ResultSet resultRs = MySQL.executeSearch("SELECT r.student_id, s.first_name, s.last_name, r.marks_obtained, r.grade_letter, r.remarks " +
                        "FROM result r " +
                        "JOIN student s ON r.student_id = s.student_id " +
                        "WHERE r.exam_id = '" + examId + "' " +
                        "ORDER BY r.marks_obtained DESC");

                while (resultRs.next()) {
                    String studentId = resultRs.getString("student_id");
                    String studentName = resultRs.getString("first_name") + " " + resultRs.getString("last_name");
                    double marks = resultRs.getDouble("marks_obtained");
                    String resultGrade = resultRs.getString("grade_letter");
                    String remarks = resultRs.getString("remarks");

                    printModel.addRow(new Object[]{studentId, studentName, marks, resultGrade, remarks});
                }
                resultRs.close();

                JTable printTable = new JTable(printModel);
                printTable.setRowHeight(25);
                printTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

                JScrollPane tableScrollPane = new JScrollPane(printTable);
                mainPanel.add(tableScrollPane, BorderLayout.SOUTH);

                printDialog.add(mainPanel, BorderLayout.CENTER);

                // Button panel
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
                JButton btnPrint = new JButton("Print");
                JButton btnClose = new JButton("Close");

                btnPrint.addActionListener(e -> {
                    try {
                        PrinterJob printerJob = PrinterJob.getPrinterJob();
                        if (printerJob.printDialog()) {
                            printerJob.setPrintable(new Printable() {
                                @Override
                                public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                                    if (pageIndex > 0) {
                                        return NO_SUCH_PAGE;
                                    }
                                    Graphics2D g2d = (Graphics2D) graphics;
                                    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                                    double scale = pageFormat.getImageableWidth() / printDialog.getWidth();
                                    g2d.scale(scale, scale);
                                    printDialog.printAll(g2d);
                                    return PAGE_EXISTS;
                                }
                            });
                            printerJob.print();
                            JOptionIcon.showSuccessDialog(printDialog, "Result sheet printed successfully!");
                        } else {
                            JOptionIcon.showWarningDialog(printDialog, "Printing cancelled.");
                        }
                    } catch (Exception ex) {
                        JOptionIcon.showErrorDialog(printDialog, "Error printing: " + ex.getMessage());
                    }
                });

                btnClose.addActionListener(e -> printDialog.dispose());

                buttonPanel.add(btnPrint);
                buttonPanel.add(btnClose);
                printDialog.add(buttonPanel, BorderLayout.SOUTH);

                printDialog.setVisible(true);
            }
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error generating result sheet: " + e.getMessage());
        }
    }
}