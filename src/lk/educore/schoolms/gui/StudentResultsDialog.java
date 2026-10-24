package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.logging.Level;
import javax.imageio.ImageIO;

/**
 * Student Results Dialog Displays exam results for a specific student
 */
public class StudentResultsDialog extends JDialog {

    private String studentId;
    private JTable resultsTable;
    private DefaultTableModel tableModel;

    public StudentResultsDialog(Frame parent, String studentId) {
        super(parent, "Student Results", true);
        this.studentId = studentId;
        initComponents();
        loadResults();
        setLocationRelativeTo(parent);

        try {
            InputStream imgStream = getClass().getResourceAsStream("/lk/educore/schoolms/resources/logo.png");
            if (imgStream == null) {
                SignIn.getLoggerObject().log(Level.WARNING, "EduCore logo image resource not found at /lk/educore/schoolms/resources/logo.png");
                return;
            }
            ImageIcon originalIcon = new ImageIcon(ImageIO.read(imgStream));
            this.setIconImage(originalIcon.getImage());

        } catch (IOException e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Failed to load educore logo image: " + e.getMessage(), e);
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Unexpected error loading educore logo image: " + e.getMessage(), e);
        }
    }

    private void initComponents() {
        setSize(700, 500);
        setLayout(new BorderLayout(10, 10));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Exam Results");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Student info
        try {
            ResultSet rs = MySQL.executeSearch(
                    "SELECT first_name, last_name FROM student WHERE student_id = '" + studentId + "'");
            if (rs.next()) {
                String studentName = rs.getString("first_name") + " " + rs.getString("last_name");
                JLabel studentLabel = new JLabel("Student: " + studentName + " (" + studentId + ")");
                studentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                studentLabel.setForeground(Color.GRAY);
                headerPanel.add(studentLabel, BorderLayout.EAST);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        add(headerPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(
                new Object[]{"Exam", "Subject", "Type", "Marks", "Grade", "Remarks"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        resultsTable = new JTable(tableModel);
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultsTable.setRowHeight(25);
        resultsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Color code grades
        resultsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 4 && !isSelected) { // Grade column
                    String grade = value != null ? value.toString() : "";
                    if (grade.startsWith("A")) {
                        setForeground(new Color(34, 197, 94)); // Green for A grades
                    } else if (grade.startsWith("B")) {
                        setForeground(new Color(59, 130, 246)); // Blue for B grades
                    } else if (grade.startsWith("C")) {
                        setForeground(new Color(234, 179, 8)); // Yellow for C grades
                    } else if (grade.startsWith("D") || grade.startsWith("E") || grade.startsWith("F")) {
                        setForeground(new Color(239, 68, 68)); // Red for failing grades
                    } else {
                        setForeground(Color.BLACK);
                    }
                } else if (!isSelected) {
                    setForeground(Color.BLACK);
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(resultsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());
        buttonPanel.add(btnClose);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadResults() {
        try {
            tableModel.setRowCount(0);

            String query = "SELECT e.exam_name, sub.subject_name, et.exam_type_name, "
                    + "r.marks_obtained, r.grade_letter, r.remarks "
                    + "FROM result r "
                    + "JOIN exam e ON r.exam_id = e.exam_id "
                    + "JOIN subject sub ON e.subject_id = sub.subject_id "
                    + "JOIN exam_type et ON e.exam_type_id = et.exam_type_id "
                    + "WHERE r.student_id = '" + studentId + "' "
                    + "ORDER BY e.exam_date DESC";

            ResultSet rs = MySQL.executeSearch(query);

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("exam_name"),
                    rs.getString("subject_name"),
                    rs.getString("exam_type_name"),
                    rs.getString("marks_obtained"),
                    rs.getString("grade_letter"),
                    rs.getString("remarks")
                });
            }

            rs.close();

            if (tableModel.getRowCount() == 0) {
                tableModel.addRow(new Object[]{"No results found", "", "", "", "", ""});
            }

        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading results: " + e.getMessage());
        }
    }
}
