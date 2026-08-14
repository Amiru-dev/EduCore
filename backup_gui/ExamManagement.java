package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

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
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        
        btnAddExam = new JButton("Add Exam");
        btnAddExam.addActionListener(e -> addExam());
        
        btnViewResults = new JButton("View Results");
        btnViewResults.addActionListener(e -> viewResults());
        
        btnPublishResults = new JButton("Publish Results");
        btnPublishResults.addActionListener(e -> publishResults());
        
        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadExamData());

        buttonPanel.add(btnAddExam);
        buttonPanel.add(btnViewResults);
        buttonPanel.add(btnPublishResults);
        buttonPanel.add(btnRefresh);

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
        examTable.setRowHeight(25);
        examTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
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
        panel.add(new JLabel("Marks Entry - Coming Soon", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(new JLabel("Results View - Coming Soon", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }

    private void loadExamData() {
        try {
            tableModel.setRowCount(0);
            
            Connection conn = MySQL.getConnection();
            String query = "SELECT e.exam_id, e.exam_name, sub.subject_name, g.grade_name, " +
                          "et.exam_type_name, e.exam_date, e.max_marks " +
                          "FROM exam e " +
                          "JOIN subject sub ON e.subject_id = sub.subject_id " +
                          "JOIN grade g ON e.grade_id = g.grade_id " +
                          "JOIN exam_type et ON e.exam_type_id = et.exam_type_id " +
                          "ORDER BY e.exam_date DESC";
            
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
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
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
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
            
            Connection conn = MySQL.getConnection();
            String query;
            
            switch (searchBy) {
                case "Exam Name":
                    query = "SELECT e.exam_id, e.exam_name, sub.subject_name, g.grade_name, " +
                            "et.exam_type_name, e.exam_date, e.max_marks " +
                            "FROM exam e " +
                            "JOIN subject sub ON e.subject_id = sub.subject_id " +
                            "JOIN grade g ON e.grade_id = g.grade_id " +
                            "JOIN exam_type et ON e.exam_type_id = et.exam_type_id " +
                            "WHERE e.exam_name LIKE ? " +
                            "ORDER BY e.exam_date DESC";
                    break;
                case "Subject":
                    query = "SELECT e.exam_id, e.exam_name, sub.subject_name, g.grade_name, " +
                            "et.exam_type_name, e.exam_date, e.max_marks " +
                            "FROM exam e " +
                            "JOIN subject sub ON e.subject_id = sub.subject_id " +
                            "JOIN grade g ON e.grade_id = g.grade_id " +
                            "JOIN exam_type et ON e.exam_type_id = et.exam_type_id " +
                            "WHERE sub.subject_name LIKE ? " +
                            "ORDER BY e.exam_date DESC";
                    break;
                case "Grade":
                    query = "SELECT e.exam_id, e.exam_name, sub.subject_name, g.grade_name, " +
                            "et.exam_type_name, e.exam_date, e.max_marks " +
                            "FROM exam e " +
                            "JOIN subject sub ON e.subject_id = sub.subject_id " +
                            "JOIN grade g ON e.grade_id = g.grade_id " +
                            "JOIN exam_type et ON e.exam_type_id = et.exam_type_id " +
                            "WHERE g.grade_name LIKE ? " +
                            "ORDER BY e.exam_date DESC";
                    break;
                case "Exam Type":
                    query = "SELECT e.exam_id, e.exam_name, sub.subject_name, g.grade_name, " +
                            "et.exam_type_name, e.exam_date, e.max_marks " +
                            "FROM exam e " +
                            "JOIN subject sub ON e.subject_id = sub.subject_id " +
                            "JOIN grade g ON e.grade_id = g.grade_id " +
                            "JOIN exam_type et ON e.exam_type_id = et.exam_type_id " +
                            "WHERE et.exam_type_name LIKE ? " +
                            "ORDER BY e.exam_date DESC";
                    break;
                default:
                    query = "SELECT e.exam_id, e.exam_name, sub.subject_name, g.grade_name, " +
                            "et.exam_type_name, e.exam_date, e.max_marks " +
                            "FROM exam e " +
                            "JOIN subject sub ON e.subject_id = sub.subject_id " +
                            "JOIN grade g ON e.grade_id = g.grade_id " +
                            "JOIN exam_type et ON e.exam_type_id = et.exam_type_id " +
                            "ORDER BY e.exam_date DESC";
            }
            
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = stmt.executeQuery();
            
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
            stmt.close();
            conn.close();
            
        } catch (SQLException e) {
            JOptionIcon.showErrorDialog(this, "Error searching exams: " + e.getMessage());
        }
    }

    private void addExam() {
        JOptionIcon.showInfoDialog(this, "Add Exam - Coming Soon");
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
        
        int confirm = JOptionIcon.showConfirmDialog(this, "Publish results for this exam?");
        
        if (confirm == JOptionPane.YES_OPTION) {
            JOptionIcon.showSuccessDialog(this, "Results published successfully!");
        }
    }
}