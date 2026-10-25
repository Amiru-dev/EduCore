package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.model.UserSession;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;

/**
 * Exam Management Panel
 * Main interface for exam scheduling, marks entry, result generation, and grade calculation
 */
public class ExamManagement extends JPanel {

    private JTable examTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<String> cmbSearchBy;
    private JButton btnAddExam, btnViewResults, btnRefresh, btnPublishResults;
    private JTabbedPane tabbedPane;

    public ExamManagement() {
        initComponents();
        loadExamData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Exam Schedule Tab
        JPanel schedulePanel = createSchedulePanel();
        tabbedPane.addTab("Exam Schedule", schedulePanel);
        
        // Marks Entry Tab
        JPanel marksPanel = createMarksPanel();
        tabbedPane.addTab("Marks Entry", marksPanel);
        
        // Results Tab
        JPanel resultsPanel = createResultsPanel();
        tabbedPane.addTab("Results", resultsPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Title
        JLabel titleLabel = new JLabel("Exam Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.WEST);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        cmbSearchBy = new JComboBox<>(new String[]{"Exam Name", "Subject", "Grade", "Exam Type"});
        txtSearch = new JTextField(20);
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchExams();
            }
        });
        
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> searchExams());
        
        searchPanel.add(new JLabel("Search by:"));
        searchPanel.add(cmbSearchBy);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        
        panel.add(searchPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createSchedulePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        btnAddExam = new JButton("Add Exam");
        btnAddExam.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAddExam.addActionListener(e -> addExam());
        applyPrimaryButtonEffect(btnAddExam);
        
        btnViewResults = new JButton("View Results");
        btnViewResults.setEnabled(false);
        btnViewResults.addActionListener(e -> viewResults());
        applySecondaryButtonEffect(btnViewResults);
        
        btnPublishResults = new JButton("Publish Results");
        btnPublishResults.setEnabled(false);
        btnPublishResults.addActionListener(e -> publishResults());
        applySecondaryButtonEffect(btnPublishResults);
        
        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadExamData());
        applySecondaryButtonEffect(btnRefresh);

        buttonPanel.add(btnAddExam);
        buttonPanel.add(btnViewResults);
        buttonPanel.add(btnPublishResults);
        buttonPanel.add(btnRefresh);

        if (UserSession.get() != null && UserSession.get().isTeacher()) {
            btnAddExam.setVisible(false);
            btnPublishResults.setVisible(false);
        }

        panel.add(buttonPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(
            new Object[]{"Exam ID", "Exam Name", "Subject", "Grade", "Exam Type", "Date", "Max Marks"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        examTable = new JTable(tableModel);
        examTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        examTable.setRowHeight(30);
        examTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        examTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Add selection listener to enable/disable buttons
        examTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelection = examTable.getSelectedRow() != -1;
                btnViewResults.setEnabled(hasSelection);
                btnPublishResults.setEnabled(hasSelection);
            }
        });
        
        // Add mouse listener for double-click
        examTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewResults();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(examTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMarksPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Top panel for exam selection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.add(new JLabel("Select Exam:"));

        JComboBox<String> cmbExam = new JComboBox<>();
        topPanel.add(cmbExam);

        JButton btnLoadStudents = new JButton("Load Students");
        topPanel.add(btnLoadStudents);

        panel.add(topPanel, BorderLayout.NORTH);

        // Table for marks entry
        DefaultTableModel marksModel = new DefaultTableModel(
            new Object[]{"Student ID", "Student Name", "Marks Obtained", "Grade", "Remarks"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 4; // Only marks and remarks are editable
            }
        };

        JTable marksTable = new JTable(marksModel);
        marksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        marksTable.setRowHeight(25);
        marksTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(marksTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel for actions
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnSaveMarks = new JButton("Save Marks");
        JButton btnCalculateGrades = new JButton("Calculate Grades");
        JButton btnRefresh = new JButton("Refresh");

        bottomPanel.add(btnSaveMarks);
        bottomPanel.add(btnCalculateGrades);
        bottomPanel.add(btnRefresh);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Load exams into dropdown
        try {
            String cmbQuery = "SELECT e.exam_id, e.exam_name, sub.subject_name, g.grade_name " +
                    "FROM exam e " +
                    "JOIN subject sub ON e.subject_id = sub.subject_id " +
                    "JOIN grade g ON e.grade_id = g.grade_id ";
            if (UserSession.get() != null && UserSession.get().isTeacher()) {
                cmbQuery += "JOIN teacher_subject ts ON e.subject_id = ts.subject_id AND e.grade_id = ts.grade_id " +
                            "WHERE ts.teacher_NIC = '" + UserSession.get().getTeacherNIC() + "' ";
            }
            cmbQuery += "ORDER BY e.exam_date DESC";

            ResultSet rs = MySQL.executeSearch(cmbQuery);
            while (rs.next()) {
                String examInfo = rs.getString("exam_name") + " - " + rs.getString("subject_name") + " (" + rs.getString("grade_name") + ")";
                cmbExam.addItem(examInfo);
            }
            rs.close();
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading exams: " + e.getMessage());
        }

        // Load students when exam is selected
        btnLoadStudents.addActionListener(e -> {
            String selectedExam = (String) cmbExam.getSelectedItem();
            if (selectedExam == null) {
                JOptionIcon.showWarningDialog(this, "Please select an exam.");
                return;
            }

            try {
                marksModel.setRowCount(0);

                // Extract exam ID from the selected item
                String examName = selectedExam.split(" - ")[0];
                ResultSet examRs = MySQL.executeSearch("SELECT exam_id, grade_id FROM exam WHERE exam_name = '" + examName + "'");
                if (examRs.next()) {
                    int examId = examRs.getInt("exam_id");
                    int gradeId = examRs.getInt("grade_id");
                    examRs.close();

                    // Load students in the grade
                    ResultSet studentRs = MySQL.executeSearch("SELECT student_id, first_name, last_name FROM student WHERE grade_id = '" + gradeId + "' AND is_active = 1 ORDER BY first_name, last_name");
                    while (studentRs.next()) {
                        String studentId = studentRs.getString("student_id");
                        String studentName = studentRs.getString("first_name") + " " + studentRs.getString("last_name");
                        marksModel.addRow(new Object[]{studentId, studentName, "", "", ""});
                    }
                    studentRs.close();
                }
            } catch (Exception ex) {
                JOptionIcon.showErrorDialog(this, "Error loading students: " + ex.getMessage());
            }
        });

        // Save marks
        btnSaveMarks.addActionListener(e -> {
            String selectedExam = (String) cmbExam.getSelectedItem();
            if (selectedExam == null) {
                JOptionIcon.showWarningDialog(this, "Please select an exam.");
                return;
            }

            try {
                String examName = selectedExam.split(" - ")[0];
                ResultSet examRs = MySQL.executeSearch("SELECT exam_id, max_marks FROM exam WHERE exam_name = '" + examName + "'");
                if (examRs.next()) {
                    int examId = examRs.getInt("exam_id");
                    double maxMarks = examRs.getDouble("max_marks");
                    examRs.close();

                    int savedCount = 0;
                    for (int i = 0; i < marksModel.getRowCount(); i++) {
                        String studentId = marksModel.getValueAt(i, 0).toString();
                        String marksStr = marksModel.getValueAt(i, 2).toString();
                        String remarks = marksModel.getValueAt(i, 4).toString();

                        if (!marksStr.isEmpty()) {
                            try {
                                double marks = Double.parseDouble(marksStr);
                                if (marks < 0 || marks > maxMarks) {
                                    JOptionIcon.showWarningDialog(this, "Marks for " + studentId + " must be between 0 and " + maxMarks);
                                    return;
                                }

                                // Calculate grade
                                String grade = calculateGrade(marks, maxMarks);

                                // Check if result already exists
                                ResultSet checkRs = MySQL.executeSearch("SELECT result_id FROM result WHERE exam_id = '" + examId + "' AND student_id = '" + studentId + "'");
                                if (checkRs.next()) {
                                    // Update existing result
                                    String updateQuery = "UPDATE result SET marks_obtained = '" + marks + "', grade_letter = '" + grade + "', remarks = '" + remarks + "' WHERE exam_id = '" + examId + "' AND student_id = '" + studentId + "'";
                                    MySQL.executeIUD(updateQuery);
                                } else {
                                    // Insert new result
                                    String insertQuery = "INSERT INTO result (exam_id, student_id, marks_obtained, grade_letter, remarks) VALUES ('" + examId + "', '" + studentId + "', '" + marks + "', '" + grade + "', '" + remarks + "')";
                                    MySQL.executeIUD(insertQuery);
                                }
                                checkRs.close();
                                savedCount++;
                            } catch (NumberFormatException ex) {
                                JOptionIcon.showWarningDialog(this, "Invalid marks for " + studentId);
                                return;
                            }
                        }
                    }

                    JOptionIcon.showSuccessDialog(this, "Marks saved for " + savedCount + " students.");
                }
            } catch (Exception ex) {
                JOptionIcon.showErrorDialog(this, "Error saving marks: " + ex.getMessage());
            }
        });

        // Calculate grades
        btnCalculateGrades.addActionListener(e -> {
            String selectedExam = (String) cmbExam.getSelectedItem();
            if (selectedExam == null) {
                JOptionIcon.showWarningDialog(this, "Please select an exam.");
                return;
            }

            try {
                String examName = selectedExam.split(" - ")[0];
                ResultSet examRs = MySQL.executeSearch("SELECT exam_id, max_marks FROM exam WHERE exam_name = '" + examName + "'");
                if (examRs.next()) {
                    double maxMarks = examRs.getDouble("max_marks");
                    examRs.close();

                    for (int i = 0; i < marksModel.getRowCount(); i++) {
                        String marksStr = marksModel.getValueAt(i, 2).toString();
                        if (!marksStr.isEmpty()) {
                            try {
                                double marks = Double.parseDouble(marksStr);
                                String grade = calculateGrade(marks, maxMarks);
                                marksModel.setValueAt(grade, i, 3);
                            } catch (NumberFormatException ex) {
                                // Skip invalid marks
                            }
                        }
                    }

                    JOptionIcon.showSuccessDialog(this, "Grades calculated successfully.");
                }
            } catch (Exception ex) {
                JOptionIcon.showErrorDialog(this, "Error calculating grades: " + ex.getMessage());
            }
        });

        // Refresh
        btnRefresh.addActionListener(e -> {
            if (cmbExam.getSelectedItem() != null) {
                btnLoadStudents.doClick();
            }
        });

        return panel;
    }

    private String calculateGrade(double marks, double maxMarks) {
        try {
            return lk.educore.schoolms.service.AcademicService.calculateGrade(marks, maxMarks);
        } catch (Exception e) {
            return "W";
        }
    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Top panel for exam selection and statistics
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        selectionPanel.add(new JLabel("Select Exam:"));

        JComboBox<String> cmbExam = new JComboBox<>();
        selectionPanel.add(cmbExam);

        JButton btnLoadResults = new JButton("Load Results");
        selectionPanel.add(btnLoadResults);

        topPanel.add(selectionPanel, BorderLayout.NORTH);

        // Statistics panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));

        JLabel lblTotalStudents = new JLabel("Total Students: 0", SwingConstants.CENTER);
        lblTotalStudents.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTotalStudents.setOpaque(true);
        lblTotalStudents.setBackground(new Color(200, 200, 255));

        JLabel lblAverageMarks = new JLabel("Average: 0.00", SwingConstants.CENTER);
        lblAverageMarks.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblAverageMarks.setOpaque(true);
        lblAverageMarks.setBackground(new Color(200, 255, 200));

        JLabel lblPassCount = new JLabel("Passed: 0", SwingConstants.CENTER);
        lblPassCount.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPassCount.setOpaque(true);
        lblPassCount.setBackground(new Color(150, 255, 150));

        JLabel lblFailCount = new JLabel("Failed: 0", SwingConstants.CENTER);
        lblFailCount.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblFailCount.setOpaque(true);
        lblFailCount.setBackground(new Color(255, 200, 200));

        statsPanel.add(lblTotalStudents);
        statsPanel.add(lblAverageMarks);
        statsPanel.add(lblPassCount);
        statsPanel.add(lblFailCount);

        topPanel.add(statsPanel, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);

        // Table for results
        DefaultTableModel resultsModel = new DefaultTableModel(
            new Object[]{"Student ID", "Student Name", "Marks Obtained", "Grade", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable resultsTable = new JTable(resultsModel);
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultsTable.setRowHeight(25);
        resultsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(resultsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel for actions
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnExport = new JButton("Export Results");
        JButton btnRefresh = new JButton("Refresh");

        bottomPanel.add(btnExport);
        bottomPanel.add(btnRefresh);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Load exams into dropdown
        try {
            String analyticsQuery = "SELECT e.exam_id, e.exam_name, sub.subject_name, g.grade_name " +
                    "FROM exam e " +
                    "JOIN subject sub ON e.subject_id = sub.subject_id " +
                    "JOIN grade g ON e.grade_id = g.grade_id ";
            if (UserSession.get() != null && UserSession.get().isTeacher()) {
                analyticsQuery += "JOIN teacher_subject ts ON e.subject_id = ts.subject_id AND e.grade_id = ts.grade_id " +
                                  "WHERE ts.teacher_NIC = '" + UserSession.get().getTeacherNIC() + "' ";
            }
            analyticsQuery += "ORDER BY e.exam_date DESC";

            ResultSet rs = MySQL.executeSearch(analyticsQuery);
            while (rs.next()) {
                String examInfo = rs.getString("exam_name") + " - " + rs.getString("subject_name") + " (" + rs.getString("grade_name") + ")";
                cmbExam.addItem(examInfo);
            }
            rs.close();
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading exams: " + e.getMessage());
        }

        // Load results when exam is selected
        btnLoadResults.addActionListener(e -> {
            String selectedExam = (String) cmbExam.getSelectedItem();
            if (selectedExam == null) {
                JOptionIcon.showWarningDialog(this, "Please select an exam.");
                return;
            }

            try {
                resultsModel.setRowCount(0);

                String examName = selectedExam.split(" - ")[0];
                ResultSet examRs = MySQL.executeSearch("SELECT exam_id, max_marks FROM exam WHERE exam_name = '" + examName + "'");
                if (examRs.next()) {
                    int examId = examRs.getInt("exam_id");
                    double maxMarks = examRs.getDouble("max_marks");
                    examRs.close();

                    // Load results
                    ResultSet resultRs = MySQL.executeSearch("SELECT r.student_id, s.first_name, s.last_name, r.marks_obtained, r.grade_letter " +
                            "FROM result r " +
                            "JOIN student s ON r.student_id = s.student_id " +
                            "WHERE r.exam_id = '" + examId + "' " +
                            "ORDER BY r.marks_obtained DESC");

                    int totalStudents = 0;
                    double totalMarks = 0;
                    int passCount = 0;
                    int failCount = 0;

                    while (resultRs.next()) {
                        String studentId = resultRs.getString("student_id");
                        String studentName = resultRs.getString("first_name") + " " + resultRs.getString("last_name");
                        double marks = resultRs.getDouble("marks_obtained");
                        String grade = resultRs.getString("grade_letter");

                        totalStudents++;
                        totalMarks += marks;

                        String status = (grade.equals("A") || grade.equals("B") || grade.equals("C") || grade.equals("S")) ? "Pass" : "Fail";
                        if (status.equals("Pass")) {
                            passCount++;
                        } else {
                            failCount++;
                        }

                        resultsModel.addRow(new Object[]{studentId, studentName, marks, grade, status});
                    }
                    resultRs.close();

                    // Update statistics
                    lblTotalStudents.setText("Total Students: " + totalStudents);
                    double average = totalStudents > 0 ? totalMarks / totalStudents : 0;
                    lblAverageMarks.setText("Average: " + String.format("%.2f", average));
                    lblPassCount.setText("Passed: " + passCount);
                    lblFailCount.setText("Failed: " + failCount);
                }
            } catch (Exception ex) {
                JOptionIcon.showErrorDialog(this, "Error loading results: " + ex.getMessage());
            }
        });

        // Export results
        btnExport.addActionListener(e -> {
            String selectedExam = (String) cmbExam.getSelectedItem();
            if (selectedExam == null) {
                JOptionIcon.showWarningDialog(this, "Please select an exam.");
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Export Results");
            String examName = selectedExam.split(" - ")[0];
            fileChooser.setSelectedFile(new java.io.File(examName + "_results.csv"));

            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                try {
                    java.io.PrintWriter writer = new java.io.PrintWriter(fileToSave);

                    // Write headers
                    writer.println("Student ID,Student Name,Marks Obtained,Grade,Status");

                    // Write data
                    for (int i = 0; i < resultsModel.getRowCount(); i++) {
                        String studentId = resultsModel.getValueAt(i, 0).toString();
                        String studentName = resultsModel.getValueAt(i, 1).toString();
                        String marks = resultsModel.getValueAt(i, 2).toString();
                        String grade = resultsModel.getValueAt(i, 3).toString();
                        String status = resultsModel.getValueAt(i, 4).toString();

                        writer.println(studentId + "," + studentName + "," + marks + "," + grade + "," + status);
                    }

                    writer.close();
                    JOptionIcon.showSuccessDialog(this, "Results exported successfully to: " + fileToSave.getAbsolutePath());
                } catch (Exception ex) {
                    JOptionIcon.showErrorDialog(this, "Error exporting results: " + ex.getMessage());
                }
            }
        });

        // Refresh
        btnRefresh.addActionListener(e -> {
            if (cmbExam.getSelectedItem() != null) {
                btnLoadResults.doClick();
            }
        });

        return panel;
    }

    private void loadExamData() {
        try {
            tableModel.setRowCount(0);
            
            String query = "SELECT e.exam_id, e.exam_name, sub.subject_name, g.grade_name, " +
                          "et.exam_type_name, e.exam_date, e.max_marks " +
                          "FROM exam e " +
                          "JOIN subject sub ON e.subject_id = sub.subject_id " +
                          "JOIN grade g ON e.grade_id = g.grade_id " +
                          "JOIN exam_type et ON e.exam_type_id = et.exam_type_id ";
            if (UserSession.get() != null && UserSession.get().isTeacher()) {
                query += "JOIN teacher_subject ts ON e.subject_id = ts.subject_id AND e.grade_id = ts.grade_id " +
                         "WHERE ts.teacher_NIC = '" + UserSession.get().getTeacherNIC() + "' ";
            }
            query += "ORDER BY e.exam_date DESC";

            ResultSet rs = MySQL.executeSearch(query);
            
            while (rs.next()) {
                int examId = rs.getInt("exam_id");
                String examName = rs.getString("exam_name");
                String subject = rs.getString("subject_name");
                String grade = rs.getString("grade_name");
                String examType = rs.getString("exam_type_name");
                String examDate = rs.getString("exam_date");
                double maxMarks = rs.getDouble("max_marks");
                
                tableModel.addRow(new Object[]{examId, examName, subject, grade, examType, examDate, maxMarks});
            }
            
            rs.close();
            
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading exam data: " + e.getMessage());
        }
    }

    private void searchExams() {
        try {
            String searchTerm = txtSearch.getText().trim();
            String searchBy = (String) cmbSearchBy.getSelectedItem();
            
            if (searchTerm.isEmpty()) {
                loadExamData();
                return;
            }
            
            tableModel.setRowCount(0);
            
            boolean isTeacher = UserSession.get() != null && UserSession.get().isTeacher();
            String teacherJoin = isTeacher ? "JOIN teacher_subject ts ON e.subject_id = ts.subject_id AND e.grade_id = ts.grade_id " : "";
            String teacherFilter = isTeacher ? "AND ts.teacher_NIC = '" + UserSession.get().getTeacherNIC() + "' " : "";
            String teacherWhere = isTeacher ? "WHERE ts.teacher_NIC = '" + UserSession.get().getTeacherNIC() + "' " : "";

            String query;
            
            switch (searchBy) {
                case "Exam Name":
                    query = "SELECT e.exam_id, e.exam_name, sub.subject_name, g.grade_name, " +
                            "et.exam_type_name, e.exam_date, e.max_marks " +
                            "FROM exam e " +
                            "JOIN subject sub ON e.subject_id = sub.subject_id " +
                            "JOIN grade g ON e.grade_id = g.grade_id " +
                            "JOIN exam_type et ON e.exam_type_id = et.exam_type_id " +
                            teacherJoin +
                            "WHERE e.exam_name LIKE '%" + searchTerm + "%' " +
                            teacherFilter +
                            "ORDER BY e.exam_date DESC";
                    break;
                case "Subject":
                    query = "SELECT e.exam_id, e.exam_name, sub.subject_name, g.grade_name, " +
                            "et.exam_type_name, e.exam_date, e.max_marks " +
                            "FROM exam e " +
                            "JOIN subject sub ON e.subject_id = sub.subject_id " +
                            "JOIN grade g ON e.grade_id = g.grade_id " +
                            "JOIN exam_type et ON e.exam_type_id = et.exam_type_id " +
                            teacherJoin +
                            "WHERE sub.subject_name LIKE '%" + searchTerm + "%' " +
                            teacherFilter +
                            "ORDER BY e.exam_date DESC";
                    break;
                case "Grade":
                    query = "SELECT e.exam_id, e.exam_name, sub.subject_name, g.grade_name, " +
                            "et.exam_type_name, e.exam_date, e.max_marks " +
                            "FROM exam e " +
                            "JOIN subject sub ON e.subject_id = sub.subject_id " +
                            "JOIN grade g ON e.grade_id = g.grade_id " +
                            "JOIN exam_type et ON e.exam_type_id = et.exam_type_id " +
                            teacherJoin +
                            "WHERE g.grade_name LIKE '%" + searchTerm + "%' " +
                            teacherFilter +
                            "ORDER BY e.exam_date DESC";
                    break;
                case "Exam Type":
                    query = "SELECT e.exam_id, e.exam_name, sub.subject_name, g.grade_name, " +
                            "et.exam_type_name, e.exam_date, e.max_marks " +
                            "FROM exam e " +
                            "JOIN subject sub ON e.subject_id = sub.subject_id " +
                            "JOIN grade g ON e.grade_id = g.grade_id " +
                            "JOIN exam_type et ON e.exam_type_id = et.exam_type_id " +
                            teacherJoin +
                            "WHERE et.exam_type_name LIKE '%" + searchTerm + "%' " +
                            teacherFilter +
                            "ORDER BY e.exam_date DESC";
                    break;
                default:
                    query = "SELECT e.exam_id, e.exam_name, sub.subject_name, g.grade_name, " +
                            "et.exam_type_name, e.exam_date, e.max_marks " +
                            "FROM exam e " +
                            "JOIN subject sub ON e.subject_id = sub.subject_id " +
                            "JOIN grade g ON e.grade_id = g.grade_id " +
                            "JOIN exam_type et ON e.exam_type_id = et.exam_type_id " +
                            teacherJoin +
                            teacherWhere +
                            "ORDER BY e.exam_date DESC";
            }
            
            ResultSet rs = MySQL.executeSearch(query);
            
            while (rs.next()) {
                int examId = rs.getInt("exam_id");
                String examName = rs.getString("exam_name");
                String subject = rs.getString("subject_name");
                String grade = rs.getString("grade_name");
                String examType = rs.getString("exam_type_name");
                String examDate = rs.getString("exam_date");
                double maxMarks = rs.getDouble("max_marks");
                
                tableModel.addRow(new Object[]{examId, examName, subject, grade, examType, examDate, maxMarks});
            }
            
            rs.close();
            
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error searching exams: " + e.getMessage());
        }
    }

    private void addExam() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Exam", true);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Exam Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Exam Name:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField txtExamName = new JTextField(20);
        formPanel.add(txtExamName, gbc);

        // Exam Type
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Exam Type:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JComboBox<String> cmbExamType = new JComboBox<>();
        try {
            ResultSet rs = MySQL.executeSearch("SELECT exam_type_name FROM exam_type ORDER BY exam_type_id");
            while (rs.next()) {
                cmbExamType.addItem(rs.getString("exam_type_name"));
            }
            rs.close();
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading exam types: " + e.getMessage());
        }
        formPanel.add(cmbExamType, gbc);

        // Grade
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Grade:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JComboBox<String> cmbGrade = new JComboBox<>();
        try {
            ResultSet rs = MySQL.executeSearch("SELECT DISTINCT grade_name FROM grade ORDER BY grade_name");
            while (rs.next()) {
                cmbGrade.addItem(rs.getString("grade_name"));
            }
            rs.close();
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading grades: " + e.getMessage());
        }
        formPanel.add(cmbGrade, gbc);

        // Subject (filtered by grade)
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Subject:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JComboBox<String> cmbSubject = new JComboBox<>();
        formPanel.add(cmbSubject, gbc);

        // Load subjects when grade changes
        cmbGrade.addActionListener(e -> {
            cmbSubject.removeAllItems();
            String selectedGrade = (String) cmbGrade.getSelectedItem();
            if (selectedGrade != null) {
                try {
                    ResultSet rs = MySQL.executeSearch("SELECT s.subject_name FROM subject s " +
                            "JOIN grade g ON s.grade_id = g.grade_id " +
                            "WHERE g.grade_name = '" + selectedGrade + "' ORDER BY s.subject_name");
                    while (rs.next()) {
                        cmbSubject.addItem(rs.getString("subject_name"));
                    }
                    rs.close();
                } catch (Exception ex) {
                    JOptionIcon.showErrorDialog(this, "Error loading subjects: " + ex.getMessage());
                }
            }
        });

        // Exam Date
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Exam Date:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JDateChooser examDateChooser = new JDateChooser();
        examDateChooser.setDateFormatString("yyyy-MM-dd");
        formPanel.add(examDateChooser, gbc);

        // Max Marks
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Max Marks:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField txtMaxMarks = new JTextField("100", 20);
        formPanel.add(txtMaxMarks, gbc);

        // Duration (minutes)
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Duration (min):"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField txtDuration = new JTextField("60", 20);
        formPanel.add(txtDuration, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.gridheight = 2;
        JTextArea txtDescription = new JTextArea(3, 20);
        JScrollPane descScrollPane = new JScrollPane(txtDescription);
        formPanel.add(descScrollPane, gbc);

        dialog.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        btnSave.addActionListener(e -> {
            String examName = txtExamName.getText().trim();
            String examType = (String) cmbExamType.getSelectedItem();
            String grade = (String) cmbGrade.getSelectedItem();
            String subject = (String) cmbSubject.getSelectedItem();
            java.util.Date examDate = examDateChooser.getDate();
            String maxMarksStr = txtMaxMarks.getText().trim();
            String durationStr = txtDuration.getText().trim();
            String description = txtDescription.getText().trim();

            // Validation
            if (examName.isEmpty()) {
                JOptionIcon.showWarningDialog(dialog, "Please enter exam name.");
                return;
            }
            if (examType == null) {
                JOptionIcon.showWarningDialog(dialog, "Please select exam type.");
                return;
            }
            if (grade == null) {
                JOptionIcon.showWarningDialog(dialog, "Please select grade.");
                return;
            }
            if (subject == null) {
                JOptionIcon.showWarningDialog(dialog, "Please select subject.");
                return;
            }
            if (examDate == null) {
                JOptionIcon.showWarningDialog(dialog, "Please select exam date.");
                return;
            }
            if (maxMarksStr.isEmpty()) {
                JOptionIcon.showWarningDialog(dialog, "Please enter max marks.");
                return;
            }
            if (durationStr.isEmpty()) {
                JOptionIcon.showWarningDialog(dialog, "Please enter duration.");
                return;
            }

            try {
                double maxMarks = Double.parseDouble(maxMarksStr);
                int duration = Integer.parseInt(durationStr);

                if (maxMarks <= 0) {
                    JOptionIcon.showWarningDialog(dialog, "Max marks must be greater than 0.");
                    return;
                }
                if (duration <= 0) {
                    JOptionIcon.showWarningDialog(dialog, "Duration must be greater than 0.");
                    return;
                }

                // Get IDs
                ResultSet typeRs = MySQL.executeSearch("SELECT exam_type_id FROM exam_type WHERE exam_type_name = '" + examType + "'");
                int typeId = typeRs.next() ? typeRs.getInt("exam_type_id") : -1;
                typeRs.close();

                ResultSet gradeRs = MySQL.executeSearch("SELECT grade_id FROM grade WHERE grade_name = '" + grade + "'");
                int gradeId = gradeRs.next() ? gradeRs.getInt("grade_id") : -1;
                gradeRs.close();

                ResultSet subjectRs = MySQL.executeSearch("SELECT subject_id FROM subject WHERE subject_name = '" + subject + "'");
                int subjectId = subjectRs.next() ? subjectRs.getInt("subject_id") : -1;
                subjectRs.close();

                if (typeId == -1 || gradeId == -1 || subjectId == -1) {
                    JOptionIcon.showErrorDialog(dialog, "Error getting IDs for exam type, grade, or subject.");
                    return;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = sdf.format(examDate);

                String query = "INSERT INTO exam (exam_name, exam_type_id, subject_id, grade_id, exam_date, max_marks, duration_minutes, description) " +
                        "VALUES ('" + examName + "', '" + typeId + "', '" + subjectId + "', '" + gradeId + "', '" +
                        formattedDate + "', '" + maxMarks + "', '" + duration + "', '" + description + "')";

                MySQL.executeIUD(query);

                JOptionIcon.showSuccessDialog(dialog, "Exam added successfully!");
                dialog.dispose();
                loadExamData();

            } catch (NumberFormatException ex) {
                JOptionIcon.showWarningDialog(dialog, "Max marks and duration must be valid numbers.");
            } catch (Exception ex) {
                JOptionIcon.showErrorDialog(dialog, "Error adding exam: " + ex.getMessage());
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void viewResults() {
        int selectedRow = examTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select an exam to view results.");
            return;
        }
        
        int examId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String examName = tableModel.getValueAt(selectedRow, 1).toString();
        
        Window parent = SwingUtilities.getWindowAncestor(this);
        new ResultSheet((Frame) parent, examId, examName).setVisible(true);
    }

    private void publishResults() {
        int selectedRow = examTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select an exam to publish results.");
            return;
        }

        int examId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String examName = tableModel.getValueAt(selectedRow, 1).toString();

        try {
            // Get exam details
            ResultSet examRs = MySQL.executeSearch("SELECT grade_id, max_marks FROM exam WHERE exam_id = '" + examId + "'");
            if (examRs.next()) {
                int gradeId = examRs.getInt("grade_id");
                double maxMarks = examRs.getDouble("max_marks");
                examRs.close();

                // Count total students in the grade
                ResultSet studentRs = MySQL.executeSearch("SELECT COUNT(*) as total FROM student WHERE grade_id = '" + gradeId + "' AND is_active = 1");
                int totalStudents = studentRs.next() ? studentRs.getInt("total") : 0;
                studentRs.close();

                // Count students with results
                ResultSet resultRs = MySQL.executeSearch("SELECT COUNT(*) as count FROM result WHERE exam_id = '" + examId + "'");
                int resultCount = resultRs.next() ? resultRs.getInt("count") : 0;
                resultRs.close();

                // Check if all students have results
                if (resultCount < totalStudents) {
                    JOptionIcon.showWarningDialog(this, "Cannot publish results. Only " + resultCount + " out of " + totalStudents + " students have marks entered.\nPlease enter marks for all students before publishing.");
                    return;
                }

                // Calculate statistics
                ResultSet statsRs = MySQL.executeSearch("SELECT AVG(marks_obtained) as avg, COUNT(CASE WHEN grade_letter IN ('A', 'B', 'C', 'S') THEN 1 END) as pass, COUNT(CASE WHEN grade_letter = 'W' THEN 1 END) as fail FROM result WHERE exam_id = '" + examId + "'");
                if (statsRs.next()) {
                    double average = statsRs.getDouble("avg");
                    int passCount = statsRs.getInt("pass");
                    int failCount = statsRs.getInt("fail");
                    statsRs.close();

                    int confirm = JOptionIcon.showConfirmDialog(this,
                            "Publish results for: " + examName + "\n\n" +
                            "Total Students: " + totalStudents + "\n" +
                            "Average Marks: " + String.format("%.2f", average) + "\n" +
                            "Passed: " + passCount + "\n" +
                            "Failed: " + failCount + "\n\n" +
                            "Are you sure you want to publish these results?");

                    if (confirm == JOptionPane.YES_OPTION) {
                        // Results are considered published when all students have marks
                        // We could add a published flag to the database if needed
                        JOptionIcon.showSuccessDialog(this, "Results published successfully!\n\n" +
                                "Total Students: " + totalStudents + "\n" +
                                "Average Marks: " + String.format("%.2f", average) + "\n" +
                                "Passed: " + passCount + "\n" +
                                "Failed: " + failCount);
                    }
                }
            }
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error publishing results: " + e.getMessage());
        }
    }

    private void applyPrimaryButtonEffect(JButton button) {
        button.setBackground(new Color(37, 99, 235));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(new Color(29, 78, 216));
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(new Color(37, 99, 235));
                }
            }
        });
    }

    private void applySecondaryButtonEffect(JButton button) {
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(71, 85, 105));
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225), 1));
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(new Color(248, 250, 252));
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(148, 163, 184), 1),
                        BorderFactory.createEmptyBorder(8, 16, 8, 16)
                    ));
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(Color.WHITE);
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                        BorderFactory.createEmptyBorder(8, 16, 8, 16)
                    ));
                }
            }
        });
    }
}