package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import javax.swing.table.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Teacher Management Panel Main CRUD interface for teacher management with tabs
 * for info, attendance, leave, salary, and charts
 */
public class TeacherManagement extends JPanel {

    private JTable teacherTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<String> cmbSearchBy;
    private JButton btnAdd, btnEdit, btnDelete, btnView, btnRefresh, btnAttendance, btnSalary, btnQRCode;
    private JTabbedPane tabbedPane;
    private JTable leaveTable;
    private DefaultTableModel leaveModel;

    public TeacherManagement() {
        initComponents();
        loadTeacherData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Tabbed pane
        tabbedPane = new JTabbedPane();

        // Teacher Info Tab
        JPanel infoPanel = createInfoPanel();
        tabbedPane.addTab("Teacher Info", infoPanel);

        // Attendance Tab
        JPanel attendancePanel = createAttendancePanel();
        tabbedPane.addTab("Attendance", attendancePanel);

        // Leave Tab
        JPanel leavePanel = createLeavePanel();
        tabbedPane.addTab("Leave Management", leavePanel);

        // Salary Tab
        JPanel salaryPanel = createSalaryPanel();
        tabbedPane.addTab("Salary", salaryPanel);

        // Charts Tab
        JPanel chartsPanel = createChartsPanel();
        tabbedPane.addTab("Analytics", chartsPanel);

        // Class & Subject Assignment Tab (Phase 1.3 Admin UI)
        JPanel assignmentPanel = createAssignmentPanel();
        tabbedPane.addTab("Assignments", assignmentPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Title
        JLabel titleLabel = new JLabel("Teacher Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.WEST);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        cmbSearchBy = new JComboBox<>(new String[]{"NIC", "Name", "Type", "Status"});
        txtSearch = new JTextField(20);
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchTeachers();
            }
        });

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> searchTeachers());

        searchPanel.add(new JLabel("Search by:"));
        searchPanel.add(cmbSearchBy);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        panel.add(searchPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        btnAdd = new JButton("Add Teacher");
        btnAdd.addActionListener(e -> addTeacher());

        btnEdit = new JButton("Edit");
        btnEdit.addActionListener(e -> editTeacher());

        btnDelete = new JButton("Disable");
        btnDelete.addActionListener(e -> disableTeacher());

        btnView = new JButton("View Details");
        btnView.addActionListener(e -> viewTeacherDetails());

        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadTeacherData());

        btnAttendance = new JButton("Mark Attendance");
        btnAttendance.addActionListener(e -> markAttendance());

        btnSalary = new JButton("Manage Salary");
        btnSalary.addActionListener(e -> manageSalary());

        btnQRCode = new JButton("Generate QR");
        btnQRCode.addActionListener(e -> generateQRCode());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnView);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnAttendance);
        buttonPanel.add(btnSalary);
        buttonPanel.add(btnQRCode);

        panel.add(buttonPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(
                new Object[]{"NIC", "Name", "Type", "Email", "Phone", "Salary", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        teacherTable = new JTable(tableModel);
        teacherTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        teacherTable.setRowHeight(25);
        teacherTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Add mouse listener for double-click and right-click context menu
        teacherTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewTeacherDetails();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = teacherTable.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        teacherTable.setRowSelectionInterval(row, row);
                        showContextMenu(e);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(teacherTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAttendancePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRefresh = new JButton("Refresh Attendance");
        topPanel.add(btnRefresh);
        panel.add(topPanel, BorderLayout.NORTH);

        DefaultTableModel attendanceModel = new DefaultTableModel(
                new Object[]{"ID", "NIC", "Name", "Date", "Check In", "Check Out"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable attendanceTable = new JTable(attendanceModel);
        attendanceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        attendanceTable.setRowHeight(25);
        attendanceTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        java.awt.event.ActionListener loadAction = e -> {
            try {
                attendanceModel.setRowCount(0);
                ResultSet rs = MySQL.executeSearch("SELECT a.attendance_id, a.teacher_NIC, "
                        + "CONCAT(t.first_name, ' ', t.last_name) as name, "
                        + "a.attendance_date, a.check_in, a.check_out "
                        + "FROM teacher_attendance a "
                        + "INNER JOIN teacher t ON a.teacher_NIC = t.NIC "
                        + "ORDER BY a.attendance_date DESC LIMIT 100");

                while (rs.next()) {
                    attendanceModel.addRow(new Object[]{
                        rs.getString("attendance_id"),
                        rs.getString("teacher_NIC"),
                        rs.getString("name"),
                        rs.getString("attendance_date"),
                        rs.getString("check_in"),
                        rs.getString("check_out")
                    });
                }
                rs.close();
            } catch (Exception ex) {
                JOptionIcon.showErrorDialog(this, "Error loading attendance data: " + ex.getMessage());
            }
        };

        btnRefresh.addActionListener(loadAction);
        SwingUtilities.invokeLater(() -> loadAction.actionPerformed(null));

        return panel;
    }

    private JPanel createLeavePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnApprove = new JButton("Approve");
        btnApprove.setBackground(new Color(22, 163, 74));
        btnApprove.setForeground(Color.WHITE);
        btnApprove.setFocusPainted(false);
        btnApprove.addActionListener(e -> approveLeave());
        topPanel.add(btnApprove);
        
        JButton btnReject = new JButton("Reject");
        btnReject.setBackground(new Color(220, 38, 38));
        btnReject.setForeground(Color.WHITE);
        btnReject.setFocusPainted(false);
        btnReject.addActionListener(e -> rejectLeave());
        topPanel.add(btnReject);
        
        JButton btnRefresh = new JButton("Refresh Leave");
        btnRefresh.addActionListener(e -> loadLeaveData());
        topPanel.add(btnRefresh);
        panel.add(topPanel, BorderLayout.NORTH);

        DefaultTableModel leaveModel = new DefaultTableModel(
                new Object[]{"ID", "NIC", "Name", "Type", "Start Date", "End Date", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.leaveModel = leaveModel;

        JTable leaveTable = new JTable(leaveModel);
        leaveTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        leaveTable.setRowHeight(25);
        leaveTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        this.leaveTable = leaveTable;

        JScrollPane scrollPane = new JScrollPane(leaveTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Load initial data
        loadLeaveData();

        return panel;
    }

    private void loadLeaveData() {
        try {
            leaveModel.setRowCount(0);
            ResultSet rs = MySQL.executeSearch("SELECT l.leave_id, l.teacher_NIC, "
                    + "CONCAT(t.first_name, ' ', t.last_name) as name, "
                    + "lt.leave_type_name, l.start_date, l.end_date, l.status "
                    + "FROM teacher_leave l "
                    + "INNER JOIN teacher t ON l.teacher_NIC = t.NIC "
                    + "INNER JOIN leave_type lt ON l.leave_type_id = lt.leave_type_id "
                    + "ORDER BY l.leave_id DESC LIMIT 100");

            while (rs.next()) {
                leaveModel.addRow(new Object[]{
                    rs.getString("leave_id"),
                    rs.getString("teacher_NIC"),
                    rs.getString("name"),
                    rs.getString("leave_type_name"),
                    rs.getString("start_date"),
                    rs.getString("end_date"),
                    rs.getString("status")
                });
            }
            rs.close();
        } catch (Exception ex) {
            JOptionIcon.showErrorDialog(this, "Error loading leave data: " + ex.getMessage());
        }
    }

    private void approveLeave() {
        int selectedRow = leaveTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a leave request to approve.");
            return;
        }

        String leaveId = leaveModel.getValueAt(selectedRow, 0).toString();
        String status = leaveModel.getValueAt(selectedRow, 6).toString();

        if (!"Pending".equals(status)) {
            JOptionIcon.showWarningDialog(this, "Only pending leave requests can be approved.");
            return;
        }

        int confirm = JOptionIcon.showConfirmDialog(this, "Are you sure you want to approve this leave request?");
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                MySQL.executeIUD("UPDATE teacher_leave SET status = 'Approved' WHERE leave_id = " + leaveId);
                JOptionIcon.showSuccessDialog(this, "Leave request approved successfully.");
                loadLeaveData();
            } catch (Exception e) {
                JOptionIcon.showErrorDialog(this, "Error approving leave: " + e.getMessage());
            }
        }
    }

    private void rejectLeave() {
        int selectedRow = leaveTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a leave request to reject.");
            return;
        }

        String leaveId = leaveModel.getValueAt(selectedRow, 0).toString();
        String status = leaveModel.getValueAt(selectedRow, 6).toString();

        if (!"Pending".equals(status)) {
            JOptionIcon.showWarningDialog(this, "Only pending leave requests can be rejected.");
            return;
        }

        int confirm = JOptionIcon.showConfirmDialog(this, "Are you sure you want to reject this leave request?");
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                MySQL.executeIUD("UPDATE teacher_leave SET status = 'Rejected' WHERE leave_id = " + leaveId);
                JOptionIcon.showSuccessDialog(this, "Leave request rejected successfully.");
                loadLeaveData();
            } catch (Exception e) {
                JOptionIcon.showErrorDialog(this, "Error rejecting leave: " + e.getMessage());
            }
        }
    }

    private JPanel createSalaryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRefresh = new JButton("Refresh Salary");
        topPanel.add(btnRefresh);
        panel.add(topPanel, BorderLayout.NORTH);

        DefaultTableModel salaryModel = new DefaultTableModel(
                new Object[]{"NIC", "Name", "Type", "Salary"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable salaryTable = new JTable(salaryModel);
        salaryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        salaryTable.setRowHeight(25);
        salaryTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(salaryTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        java.awt.event.ActionListener loadAction = e -> {
            try {
                salaryModel.setRowCount(0);
                ResultSet rs = MySQL.executeSearch("SELECT t.NIC, "
                        + "CONCAT(t.first_name, ' ', t.last_name) as name, "
                        + "tt.type_name, t.salary "
                        + "FROM teacher t "
                        + "INNER JOIN teacher_type tt ON t.teacher_type_id = tt.teacher_type_id "
                        + "ORDER BY t.first_name ASC LIMIT 100");

                while (rs.next()) {
                    salaryModel.addRow(new Object[]{
                        rs.getString("NIC"),
                        rs.getString("name"),
                        rs.getString("type_name"),
                        String.format("Rs. %,.2f", rs.getDouble("salary"))
                    });
                }
                rs.close();
            } catch (Exception ex) {
                JOptionIcon.showErrorDialog(this, "Error loading salary data: " + ex.getMessage());
            }
        };

        btnRefresh.addActionListener(loadAction);
        SwingUtilities.invokeLater(() -> loadAction.actionPerformed(null));

        return panel;
    }

    private JPanel createChartsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));

        JPanel cardTotal = createStatCard("Total Teachers", "Loading...");
        JPanel cardActive = createStatCard("Active Teachers", "Loading...");
        JPanel cardMale = createStatCard("Male Teachers", "Loading...");
        JPanel cardFemale = createStatCard("Female Teachers", "Loading...");

        statsPanel.add(cardTotal);
        statsPanel.add(cardActive);
        statsPanel.add(cardMale);
        statsPanel.add(cardFemale);

        panel.add(statsPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRefresh = new JButton("Refresh Analytics");
        bottomPanel.add(btnRefresh);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        java.awt.event.ActionListener loadAction = e -> {
            try {
                ResultSet rs = MySQL.executeSearch("SELECT COUNT(*) as total, SUM(IF(status_id=1, 1, 0)) as active, SUM(IF(gender='Male', 1, 0)) as male, SUM(IF(gender='Female', 1, 0)) as female FROM teacher");
                if (rs.next()) {
                    ((JLabel) cardTotal.getComponent(1)).setText(rs.getString("total") == null ? "0" : rs.getString("total"));
                    ((JLabel) cardActive.getComponent(1)).setText(rs.getString("active") == null ? "0" : rs.getString("active"));
                    ((JLabel) cardMale.getComponent(1)).setText(rs.getString("male") == null ? "0" : rs.getString("male"));
                    ((JLabel) cardFemale.getComponent(1)).setText(rs.getString("female") == null ? "0" : rs.getString("female"));
                }
                rs.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };

        btnRefresh.addActionListener(loadAction);
        SwingUtilities.invokeLater(() -> loadAction.actionPerformed(null));

        return panel;
    }

    private JPanel createStatCard(String title, String initialValue) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(100, 116, 139));

        JLabel lblValue = new JLabel(initialValue, SwingConstants.CENTER);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblValue.setForeground(new Color(37, 99, 235));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        return card;
    }

    private void loadTeacherData() {
        try {
            tableModel.setRowCount(0);

            ResultSet rs = MySQL.executeSearch("SELECT t.NIC, t.first_name, t.last_name, tt.type_name, t.email, t.phone, t.salary, ts.status_name "
                    + "FROM teacher t "
                    + "JOIN teacher_type tt ON t.teacher_type_id = tt.teacher_type_id "
                    + "JOIN teacher_status ts ON t.status_id = ts.status_id "
                    + "ORDER BY t.first_name, t.last_name");

            while (rs.next()) {
                String nic = rs.getString("NIC");
                String name = rs.getString("first_name") + " " + rs.getString("last_name");
                String type = rs.getString("type_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                double salary = rs.getDouble("salary");
                String status = rs.getString("status_name");

                tableModel.addRow(new Object[]{nic, name, type, email, phone, String.format("Rs. %,.2f", salary), status});
            }

            rs.close();

        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading teacher data: " + e.getMessage());
        }
    }

    private void searchTeachers() {
        try {
            String searchTerm = txtSearch.getText().trim();
            String searchBy = (String) cmbSearchBy.getSelectedItem();

            if (searchTerm.isEmpty()) {
                loadTeacherData();
                return;
            }

            tableModel.setRowCount(0);

            String query;

            switch (searchBy) {
                case "NIC":
                    query = "SELECT t.NIC, t.first_name, t.last_name, tt.type_name, t.email, t.phone, t.salary, ts.status_name "
                            + "FROM teacher t "
                            + "JOIN teacher_type tt ON t.teacher_type_id = tt.teacher_type_id "
                            + "JOIN teacher_status ts ON t.status_id = ts.status_id "
                            + "WHERE t.NIC LIKE '%" + searchTerm + "%' "
                            + "ORDER BY t.first_name, t.last_name";
                    break;
                case "Name":
                    query = "SELECT t.NIC, t.first_name, t.last_name, tt.type_name, t.email, t.phone, t.salary, ts.status_name "
                            + "FROM teacher t "
                            + "JOIN teacher_type tt ON t.teacher_type_id = tt.teacher_type_id "
                            + "JOIN teacher_status ts ON t.status_id = ts.status_id "
                            + "WHERE t.first_name LIKE '%" + searchTerm + "%' OR t.last_name LIKE '%" + searchTerm + "%' "
                            + "ORDER BY t.first_name, t.last_name";
                    break;
                case "Type":
                    query = "SELECT t.NIC, t.first_name, t.last_name, tt.type_name, t.email, t.phone, t.salary, ts.status_name "
                            + "FROM teacher t "
                            + "JOIN teacher_type tt ON t.teacher_type_id = tt.teacher_type_id "
                            + "JOIN teacher_status ts ON t.status_id = ts.status_id "
                            + "WHERE tt.type_name LIKE '%" + searchTerm + "%' "
                            + "ORDER BY t.first_name, t.last_name";
                    break;
                case "Status":
                    query = "SELECT t.NIC, t.first_name, t.last_name, tt.type_name, t.email, t.phone, t.salary, ts.status_name "
                            + "FROM teacher t "
                            + "JOIN teacher_type tt ON t.teacher_type_id = tt.teacher_type_id "
                            + "JOIN teacher_status ts ON t.status_id = ts.status_id "
                            + "WHERE ts.status_name LIKE '%" + searchTerm + "%' "
                            + "ORDER BY t.first_name, t.last_name";
                    break;
                default:
                    query = "SELECT t.NIC, t.first_name, t.last_name, tt.type_name, t.email, t.phone, t.salary, ts.status_name "
                            + "FROM teacher t "
                            + "JOIN teacher_type tt ON t.teacher_type_id = tt.teacher_type_id "
                            + "JOIN teacher_status ts ON t.status_id = ts.status_id "
                            + "ORDER BY t.first_name, t.last_name";
            }

            ResultSet rs = MySQL.executeSearch(query);

            while (rs.next()) {
                String nic = rs.getString("NIC");
                String name = rs.getString("first_name") + " " + rs.getString("last_name");
                String type = rs.getString("type_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                double salary = rs.getDouble("salary");
                String status = rs.getString("status_name");

                tableModel.addRow(new Object[]{nic, name, type, email, phone, String.format("Rs. %,.2f", salary), status});
            }

            rs.close();

        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error searching teachers: " + e.getMessage());
        }
    }

    private void addTeacher() {
        Window parent = SwingUtilities.getWindowAncestor(this);
        TeacherDialog dialog = new TeacherDialog(parent, null);
        dialog.setVisible(true);
        loadTeacherData();
    }

    private void editTeacher() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a teacher to edit.");
            return;
        }

        String nic = tableModel.getValueAt(selectedRow, 0).toString();
        Window parent = SwingUtilities.getWindowAncestor(this);
        TeacherDialog dialog = new TeacherDialog(parent, nic);
        dialog.setVisible(true);
        loadTeacherData();
    }

    private void disableTeacher() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a teacher to disable.");
            return;
        }

        String nic = tableModel.getValueAt(selectedRow, 0).toString();
        String teacherName = tableModel.getValueAt(selectedRow, 1).toString();
        String status = tableModel.getValueAt(selectedRow, 6).toString();

        // Check if teacher is already disabled
        if ("Inactive".equals(status)) {
            JOptionIcon.showWarningDialog(this, "Teacher " + teacherName + " is already disabled.");
            return;
        }

        disableTeacher(nic, teacherName);
    }

    private void disableTeacher(String nic, String teacherName) {
        int confirm = JOptionIcon.showConfirmDialog(this,
                "Are you sure you want to disable teacher " + teacherName + " (" + nic + ")?");

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String query = "UPDATE teacher SET status_id = 2 WHERE NIC = '" + nic + "'"; // 2 = Inactive
                MySQL.executeIUD(query);

                loadTeacherData();
                JOptionIcon.showSuccessDialog(this, "Teacher disabled successfully.");

            } catch (Exception e) {
                JOptionIcon.showErrorDialog(this, "Error disabling teacher: " + e.getMessage());
            }
        }
    }

    private void showContextMenu(MouseEvent e) {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        String status = tableModel.getValueAt(selectedRow, 6).toString();
        String nic = tableModel.getValueAt(selectedRow, 0).toString();
        String teacherName = tableModel.getValueAt(selectedRow, 1).toString();

        JPopupMenu contextMenu = new JPopupMenu();

        // Show "Enable" option only if teacher is disabled
        if ("Inactive".equals(status)) {
            JMenuItem enableItem = new JMenuItem("Enable Teacher");
            enableItem.addActionListener(ev -> enableTeacher(nic, teacherName));
            contextMenu.add(enableItem);
        }

        // Always show "Disable" option for active teachers
        if ("Active".equals(status)) {
            JMenuItem disableItem = new JMenuItem("Disable Teacher");
            disableItem.addActionListener(ev -> disableTeacher(nic, teacherName));
            contextMenu.add(disableItem);
        }

        contextMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    private void enableTeacher(String nic, String teacherName) {
        // Check if teacher is already enabled
        try {
            ResultSet rs = MySQL.executeSearch("SELECT status_id FROM teacher WHERE NIC = '" + nic + "'");
            if (rs.next()) {
                int statusId = rs.getInt("status_id");
                rs.close();
                if (statusId == 1) { // 1 = Active
                    JOptionIcon.showWarningDialog(this, "Teacher " + teacherName + " is already enabled.");
                    return;
                }
            }
        } catch (Exception e) {
            // Continue with the enable operation if check fails
        }

        int confirm = JOptionIcon.showConfirmDialog(this,
                "Are you sure you want to enable teacher " + teacherName + " (" + nic + ")?");

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String query = "UPDATE teacher SET status_id = 1 WHERE NIC = '" + nic + "'"; // 1 = Active
                MySQL.executeIUD(query);

                loadTeacherData();
                JOptionIcon.showSuccessDialog(this, "Teacher enabled successfully.");

            } catch (Exception ex) {
                JOptionIcon.showErrorDialog(this, "Error enabling teacher: " + ex.getMessage());
            }
        }
    }

    private void viewTeacherDetails() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a teacher to view.");
            return;
        }

        String nic = tableModel.getValueAt(selectedRow, 0).toString();
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Teacher Details", JDialog.ModalityType.APPLICATION_MODAL);
        dialog.add(new TeacherInformation(nic));
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void markAttendance() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a teacher to mark attendance.");
            return;
        }

        String nic = tableModel.getValueAt(selectedRow, 0).toString();

        int confirm = JOptionIcon.showConfirmDialog(this, "Mark attendance for today (Check In/Out) for teacher " + nic + "?");
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                java.text.SimpleDateFormat sdfDate = new java.text.SimpleDateFormat("yyyy-MM-dd");
                java.text.SimpleDateFormat sdfTime = new java.text.SimpleDateFormat("HH:mm:ss");
                java.text.SimpleDateFormat sdfDateTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date now = new java.util.Date();

                String date = sdfDate.format(now);
                String time = sdfTime.format(now);
                String datetime = sdfDateTime.format(now);

                ResultSet rs = MySQL.executeSearch("SELECT * FROM teacher_attendance WHERE teacher_NIC = '" + nic + "' AND attendance_date = '" + date + "'");
                if (rs.next()) {
                    if (rs.getString("check_out") == null) {
                        MySQL.executeIUD("UPDATE teacher_attendance SET check_out = '" + time + "' WHERE teacher_NIC = '" + nic + "' AND attendance_date = '" + date + "'");
                        JOptionIcon.showSuccessDialog(this, "Check Out recorded successfully.");
                    } else {
                        JOptionIcon.showWarningDialog(this, "Attendance already fully marked for today.");
                    }
                } else {
                    MySQL.executeIUD("INSERT INTO teacher_attendance (teacher_NIC, date_time, attendance_date, check_in) VALUES ('" + nic + "', '" + datetime + "', '" + date + "', '" + time + "')");
                    JOptionIcon.showSuccessDialog(this, "Check In recorded successfully.");
                }
                rs.close();
            } catch (Exception e) {
                JOptionIcon.showErrorDialog(this, "Error marking attendance: " + e.getMessage());
            }
        }
    }

    private void manageSalary() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a teacher to manage salary.");
            return;
        }

        String nic = tableModel.getValueAt(selectedRow, 0).toString();
        String currentSalary = tableModel.getValueAt(selectedRow, 5).toString().replace("Rs. ", "").replace(",", "");

        String newSalary = JOptionPane.showInputDialog(this, "Enter new salary for teacher " + nic + ":", currentSalary);
        if (newSalary != null && !newSalary.trim().isEmpty()) {
            try {
                double salary = Double.parseDouble(newSalary);
                MySQL.executeIUD("UPDATE teacher SET salary = " + salary + " WHERE NIC = '" + nic + "'");
                JOptionIcon.showSuccessDialog(this, "Salary updated successfully.");
                loadTeacherData();
            } catch (NumberFormatException e) {
                JOptionIcon.showErrorDialog(this, "Invalid salary amount.");
            } catch (Exception e) {
                JOptionIcon.showErrorDialog(this, "Error updating salary: " + e.getMessage());
            }
        }
    }

    private void generateQRCode() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a teacher to generate QR code.");
            return;
        }

        Object nicObj = tableModel.getValueAt(selectedRow, 0);
        Object nameObj = tableModel.getValueAt(selectedRow, 1);

        if (nicObj == null || nicObj.toString().trim().isEmpty()) {
            JOptionIcon.showErrorDialog(this, "Teacher NIC is missing. Cannot generate QR code.");
            return;
        }

        String nic = nicObj.toString();
        String teacherName = nameObj != null ? nameObj.toString() : "Unknown";

        try {
            lk.educore.schoolms.model.QRCodeGenerator.generateTeacherQR(nic, teacherName);
            JOptionIcon.showSuccessDialog(this, "QR code generated successfully for " + teacherName);
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error generating QR code: " + e.getMessage());
        }
    }

    private JPanel createAssignmentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top bar for selecting a teacher
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topBar.add(new JLabel("Select Teacher:"));

        JComboBox<String> cmbTeacher = new JComboBox<>();
        cmbTeacher.setPreferredSize(new Dimension(280, 28));
        topBar.add(cmbTeacher);

        JButton btnRefreshTeachers = new JButton("Refresh");
        topBar.add(btnRefreshTeachers);
        panel.add(topBar, BorderLayout.NORTH);

        // Split container: Left = Class Assignments, Right = Subject Assignments
        JPanel splitContainer = new JPanel(new GridLayout(1, 2, 10, 10));

        // --- Left: Class Assignments Panel ---
        JPanel classPanel = new JPanel(new BorderLayout(5, 5));
        classPanel.setBorder(BorderFactory.createTitledBorder("Assigned Classes (teacher_class)"));

        DefaultTableModel classModel = new DefaultTableModel(
                new Object[]{"ID", "Grade", "Class Teacher?", "Year"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        JTable classTable = new JTable(classModel);
        classTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        classTable.setRowHeight(24);
        classPanel.add(new JScrollPane(classTable), BorderLayout.CENTER);

        // Class Controls
        JPanel classControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JComboBox<String> cmbClassGrade = new JComboBox<>();
        JCheckBox chkClassTeacher = new JCheckBox("Class Teacher");
        JSpinner spinClassYear = new JSpinner(new SpinnerNumberModel(2026, 2020, 2050, 1));
        JButton btnAssignClass = new JButton("Assign Class");
        JButton btnRemoveClass = new JButton("Remove Selected");
        btnRemoveClass.setForeground(new Color(220, 53, 69));

        classControlPanel.add(new JLabel("Grade:"));
        classControlPanel.add(cmbClassGrade);
        classControlPanel.add(chkClassTeacher);
        classControlPanel.add(new JLabel("Year:"));
        classControlPanel.add(spinClassYear);
        classControlPanel.add(btnAssignClass);
        classControlPanel.add(btnRemoveClass);
        classPanel.add(classControlPanel, BorderLayout.SOUTH);

        // --- Right: Subject Assignments Panel ---
        JPanel subjectPanel = new JPanel(new BorderLayout(5, 5));
        subjectPanel.setBorder(BorderFactory.createTitledBorder("Assigned Subjects (teacher_subject)"));

        DefaultTableModel subjectModel = new DefaultTableModel(
                new Object[]{"ID", "Grade", "Subject", "Year"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        JTable subjectTable = new JTable(subjectModel);
        subjectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        subjectTable.setRowHeight(24);
        subjectPanel.add(new JScrollPane(subjectTable), BorderLayout.CENTER);

        // Subject Controls
        JPanel subControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JComboBox<String> cmbSubGrade = new JComboBox<>();
        JComboBox<String> cmbSubSubject = new JComboBox<>();
        JSpinner spinSubYear = new JSpinner(new SpinnerNumberModel(2026, 2020, 2050, 1));
        JButton btnAssignSub = new JButton("Assign Subject");
        JButton btnRemoveSub = new JButton("Remove Selected");
        btnRemoveSub.setForeground(new Color(220, 53, 69));

        subControlPanel.add(new JLabel("Grade:"));
        subControlPanel.add(cmbSubGrade);
        subControlPanel.add(new JLabel("Subject:"));
        subControlPanel.add(cmbSubSubject);
        subControlPanel.add(new JLabel("Year:"));
        subControlPanel.add(spinSubYear);
        subControlPanel.add(btnAssignSub);
        subControlPanel.add(btnRemoveSub);
        subjectPanel.add(subControlPanel, BorderLayout.SOUTH);

        splitContainer.add(classPanel);
        splitContainer.add(subjectPanel);
        panel.add(splitContainer, BorderLayout.CENTER);

        // Helper: get selected teacher NIC
        java.util.function.Supplier<String> getSelectedNIC = () -> {
            String sel = (String) cmbTeacher.getSelectedItem();
            if (sel == null || !sel.contains(" - ")) {
                return null;
            }
            return sel.split(" - ")[0].trim();
        };

        // Load tables
        Runnable reloadAssignments = () -> {
            String nic = getSelectedNIC.get();
            classModel.setRowCount(0);
            subjectModel.setRowCount(0);
            if (nic == null) {
                return;
            }

            try {
                // Load classes
                ResultSet rsClass = MySQL.executeSearch(
                        "SELECT tc.id, g.grade_name, tc.is_class_teacher, tc.academic_year "
                        + "FROM teacher_class tc JOIN grade g ON tc.grade_id = g.grade_id "
                        + "WHERE tc.teacher_NIC = '" + nic + "' ORDER BY tc.academic_year DESC, g.grade_name"
                );
                while (rsClass.next()) {
                    classModel.addRow(new Object[]{
                        rsClass.getInt("id"),
                        rsClass.getString("grade_name"),
                        rsClass.getBoolean("is_class_teacher") ? "Yes" : "No",
                        rsClass.getInt("academic_year")
                    });
                }
                rsClass.close();

                // Load subjects
                ResultSet rsSub = MySQL.executeSearch(
                        "SELECT ts.id, g.grade_name, s.subject_name, ts.academic_year "
                        + "FROM teacher_subject ts "
                        + "JOIN grade g ON ts.grade_id = g.grade_id "
                        + "JOIN subject s ON ts.subject_id = s.subject_id "
                        + "WHERE ts.teacher_NIC = '" + nic + "' ORDER BY ts.academic_year DESC, g.grade_name, s.subject_name"
                );
                while (rsSub.next()) {
                    subjectModel.addRow(new Object[]{
                        rsSub.getInt("id"),
                        rsSub.getString("grade_name"),
                        rsSub.getString("subject_name"),
                        rsSub.getInt("academic_year")
                    });
                }
                rsSub.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };

        // Load teacher dropdown
        Runnable reloadTeacherDropdown = () -> {
            cmbTeacher.removeAllItems();
            try {
                ResultSet rs = MySQL.executeSearch("SELECT NIC, first_name, last_name FROM teacher ORDER BY first_name, last_name");
                while (rs.next()) {
                    cmbTeacher.addItem(rs.getString("NIC") + " - " + rs.getString("first_name") + " " + rs.getString("last_name"));
                }
                rs.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };

        // Load grades & subjects dropdowns
        Runnable reloadGradeSubjectDropdowns = () -> {
            cmbClassGrade.removeAllItems();
            cmbSubGrade.removeAllItems();
            cmbSubSubject.removeAllItems();
            try {
                ResultSet rsG = MySQL.executeSearch("SELECT grade_name FROM grade ORDER BY grade_name");
                while (rsG.next()) {
                    String g = rsG.getString("grade_name");
                    cmbClassGrade.addItem(g);
                    cmbSubGrade.addItem(g);
                }
                rsG.close();

                ResultSet rsS = MySQL.executeSearch("SELECT subject_name FROM subject ORDER BY subject_name");
                while (rsS.next()) {
                    cmbSubSubject.addItem(rsS.getString("subject_name"));
                }
                rsS.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };

        cmbTeacher.addActionListener(e -> reloadAssignments.run());
        btnRefreshTeachers.addActionListener(e -> {
            reloadTeacherDropdown.run();
            reloadGradeSubjectDropdowns.run();
            reloadAssignments.run();
        });

        // Assign Class Action
        btnAssignClass.addActionListener(e -> {
            String nic = getSelectedNIC.get();
            String grade = (String) cmbClassGrade.getSelectedItem();
            if (nic == null || grade == null) {
                JOptionIcon.showWarningDialog(this, "Please select both a Teacher and a Grade.");
                return;
            }
            int year = (Integer) spinClassYear.getValue();
            boolean isClassTeacher = chkClassTeacher.isSelected();

            try {
                ResultSet rs = MySQL.executeSearch("SELECT grade_id FROM grade WHERE grade_name = '" + grade + "'");
                if (rs.next()) {
                    int gradeId = rs.getInt("grade_id");
                    rs.close();

                    MySQL.executeIUD(
                            "INSERT INTO teacher_class (teacher_NIC, grade_id, is_class_teacher, academic_year) "
                            + "VALUES ('" + nic + "', " + gradeId + ", " + (isClassTeacher ? 1 : 0) + ", " + year + ") "
                            + "ON DUPLICATE KEY UPDATE is_class_teacher = VALUES(is_class_teacher)"
                    );
                    reloadAssignments.run();
                    JOptionIcon.showSuccessDialog(this, "Class assigned successfully!");

                } else {
                    rs.close();
                }
            } catch (Exception ex) {
                JOptionIcon.showErrorDialog(this, "Error assigning class: " + ex.getMessage());
            }
        });

        // Remove Class Action
        btnRemoveClass.addActionListener(e -> {
            int row = classTable.getSelectedRow();
            if (row == -1) {
                JOptionIcon.showWarningDialog(this, "Please select a class assignment to remove.");
                return;
            }
            int id = (Integer) classModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Remove this class assignment?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    MySQL.executeIUD("DELETE FROM teacher_class WHERE id = " + id);
                    reloadAssignments.run();
                    JOptionIcon.showSuccessDialog(this, "Assignment removed.");
                } catch (Exception ex) {
                    JOptionIcon.showErrorDialog(this, "Error removing class assignment: " + ex.getMessage());
                }
            }
        });

        // Assign Subject Action
        btnAssignSub.addActionListener(e -> {
            String nic = getSelectedNIC.get();
            String grade = (String) cmbSubGrade.getSelectedItem();
            String subject = (String) cmbSubSubject.getSelectedItem();
            if (nic == null || grade == null || subject == null) {
                JOptionIcon.showWarningDialog(this, "Please select Teacher, Grade, and Subject.");
                return;
            }
            int year = (Integer) spinSubYear.getValue();

            try {
                int gradeId = -1;
                ResultSet rsG = MySQL.executeSearch("SELECT grade_id FROM grade WHERE grade_name = '" + grade + "'");
                if (rsG.next()) {
                    gradeId = rsG.getInt("grade_id");
                }
                rsG.close();

                int subjectId = -1;
                ResultSet rsS = MySQL.executeSearch("SELECT subject_id FROM subject WHERE subject_name = '" + subject + "'");
                if (rsS.next()) {
                    subjectId = rsS.getInt("subject_id");
                }
                rsS.close();

                if (gradeId != -1 && subjectId != -1) {
                    MySQL.executeIUD(
                            "INSERT INTO teacher_subject (teacher_NIC, subject_id, grade_id, academic_year) "
                            + "VALUES ('" + nic + "', " + subjectId + ", " + gradeId + ", " + year + ") "
                            + "ON DUPLICATE KEY UPDATE academic_year = VALUES(academic_year)"
                    );
                    reloadAssignments.run();
                    JOptionIcon.showSuccessDialog(this, "Subject assigned successfully!");
                }
            } catch (Exception ex) {
                JOptionIcon.showErrorDialog(this, "Error assigning subject: " + ex.getMessage());
            }
        });

        // Remove Subject Action
        btnRemoveSub.addActionListener(e -> {
            int row = subjectTable.getSelectedRow();
            if (row == -1) {
                JOptionIcon.showWarningDialog(this, "Please select a subject assignment to remove.");
                return;
            }
            int id = (Integer) subjectModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Remove this subject assignment?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    MySQL.executeIUD("DELETE FROM teacher_subject WHERE id = " + id);
                    reloadAssignments.run();
                    JOptionIcon.showSuccessDialog(this, "Assignment removed.");
                    
                } catch (Exception ex) {
                    JOptionIcon.showErrorDialog(this, "Error removing subject assignment: " + ex.getMessage());
                }
            }
        });

        // Initial populate
        SwingUtilities.invokeLater(() -> {
            reloadTeacherDropdown.run();
            reloadGradeSubjectDropdowns.run();
            reloadAssignments.run();
        });

        return panel;
    }
}
