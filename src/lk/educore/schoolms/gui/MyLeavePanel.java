package lk.educore.schoolms.gui;

import com.toedter.calendar.JDateChooser;
import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.model.UserSession;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * My Leave Panel (Teacher Self-Service)
 * Displays the logged-in teacher's own leave records,
 * shows leave balance, and allows submitting new leave requests.
 * Leave status stays "Pending" until Admin approves/rejects from TeacherManagement.
 */
public class MyLeavePanel extends JPanel {

    private DefaultTableModel tableModel;
    private JPanel balancePanel;

    public MyLeavePanel() {
        initComponents();
        loadLeaveData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("My Leave Requests");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(44, 62, 80));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setBackground(Color.WHITE);

        JButton btnApply = new JButton("Apply for Leave");
        btnApply.setBackground(new Color(37, 99, 235));
        btnApply.setForeground(Color.WHITE);
        btnApply.setFocusPainted(false);
        btnApply.addActionListener(e -> applyForLeave());
        btnPanel.add(btnApply);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadLeaveData());
        btnPanel.add(btnRefresh);

        headerPanel.add(btnPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Center: balance cards + table
        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setBackground(Color.WHITE);

        // Leave balance cards
        balancePanel = new JPanel(new GridLayout(1, 5, 10, 0));
        balancePanel.setBackground(Color.WHITE);
        balancePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        centerPanel.add(balancePanel, BorderLayout.NORTH);

        // Leave records table
        tableModel = new DefaultTableModel(
            new Object[]{"Leave ID", "Leave Type", "Start Date", "End Date", "Days", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable leaveTable = new JTable(tableModel);
        leaveTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        leaveTable.setRowHeight(28);
        leaveTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        leaveTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(leaveTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createBalanceCard(String typeName, int used, int max) {
        JPanel card = new JPanel(new BorderLayout(3, 3));
        card.setBackground(new Color(248, 250, 252));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel lblType = new JLabel(typeName, SwingConstants.CENTER);
        lblType.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblType.setForeground(new Color(100, 116, 139));
        card.add(lblType, BorderLayout.NORTH);

        int remaining = max - used;
        JLabel lblRemaining = new JLabel(remaining + "/" + max, SwingConstants.CENTER);
        lblRemaining.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblRemaining.setForeground(remaining > 0 ? new Color(22, 163, 74) : new Color(220, 38, 38));
        card.add(lblRemaining, BorderLayout.CENTER);

        JLabel lblUsed = new JLabel("Used: " + used, SwingConstants.CENTER);
        lblUsed.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblUsed.setForeground(new Color(148, 163, 184));
        card.add(lblUsed, BorderLayout.SOUTH);

        return card;
    }

    private void loadLeaveData() {
        UserSession session = UserSession.get();
        if (session == null || session.getTeacherNIC() == null) return;

        String nic = session.getTeacherNIC();

        try {
            // Load leave records
            tableModel.setRowCount(0);
            ResultSet rs = MySQL.executeSearch(
                "SELECT l.leave_id, lt.leave_type_name, l.start_date, l.end_date, "
                + "DATEDIFF(l.end_date, l.start_date) + 1 as days, l.status "
                + "FROM teacher_leave l "
                + "JOIN leave_type lt ON l.leave_type_id = lt.leave_type_id "
                + "WHERE l.teacher_NIC = '" + nic + "' "
                + "ORDER BY l.leave_id DESC");

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("leave_id"),
                    rs.getString("leave_type_name"),
                    rs.getString("start_date"),
                    rs.getString("end_date"),
                    rs.getInt("days"),
                    rs.getString("status")
                });
            }
            rs.close();

            // Load leave balances
            balancePanel.removeAll();
            ResultSet ltRs = MySQL.executeSearch("SELECT leave_type_id, leave_type_name, max_days FROM leave_type");
            while (ltRs.next()) {
                int typeId = ltRs.getInt("leave_type_id");
                String typeName = ltRs.getString("leave_type_name");
                int maxDays = ltRs.getInt("max_days");

                // Count approved leave days used this year
                ResultSet usedRs = MySQL.executeSearch(
                    "SELECT COALESCE(SUM(DATEDIFF(end_date, start_date) + 1), 0) as used "
                    + "FROM teacher_leave "
                    + "WHERE teacher_NIC = '" + nic + "' "
                    + "AND leave_type_id = " + typeId + " "
                    + "AND status = 'Approved' "
                    + "AND YEAR(start_date) = YEAR(CURDATE())");
                int usedDays = 0;
                if (usedRs.next()) {
                    usedDays = usedRs.getInt("used");
                }
                usedRs.close();

                balancePanel.add(createBalanceCard(typeName, usedDays, maxDays));
            }
            ltRs.close();

            balancePanel.revalidate();
            balancePanel.repaint();

        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading leave data: " + e.getMessage());
        }
    }

    private void applyForLeave() {
        UserSession session = UserSession.get();
        if (session == null || session.getTeacherNIC() == null) return;

        // Build form dialog
        JDateChooser startDateChooser = new JDateChooser();
        JDateChooser endDateChooser = new JDateChooser();
        startDateChooser.setDateFormatString("yyyy-MM-dd");
        endDateChooser.setDateFormatString("yyyy-MM-dd");

        JComboBox<String> cmbLeaveType = new JComboBox<>();
        try {
            ResultSet rs = MySQL.executeSearch("SELECT leave_type_id, leave_type_name FROM leave_type");
            while (rs.next()) {
                cmbLeaveType.addItem(rs.getString("leave_type_id") + " - " + rs.getString("leave_type_name"));
            }
            rs.close();
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading leave types: " + e.getMessage());
            return;
        }

        JTextArea txtReason = new JTextArea(3, 20);
        txtReason.setLineWrap(true);
        txtReason.setWrapStyleWord(true);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Leave Type:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(cmbLeaveType, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Start Date:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(startDateChooser, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("End Date:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(endDateChooser, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(new JLabel("Reason:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(new JScrollPane(txtReason), gbc);

        int result = JOptionPane.showConfirmDialog(this, formPanel,
            "Apply for Leave", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String typeStr = (String) cmbLeaveType.getSelectedItem();
                if (typeStr == null) {
                    JOptionIcon.showWarningDialog(this, "Please select a leave type.");
                    return;
                }
                String typeId = typeStr.split(" - ")[0];

                Date startDate = startDateChooser.getDate();
                Date endDate = endDateChooser.getDate();

                if (startDate == null || endDate == null) {
                    JOptionIcon.showWarningDialog(this, "Please select both start and end dates.");
                    return;
                }
                if (startDate.after(endDate)) {
                    JOptionIcon.showWarningDialog(this, "Start date must be before or equal to end date.");
                    return;
                }

                // Check for overlapping leave requests
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String startDateStr = sdf.format(startDate);
                String endDateStr = sdf.format(endDate);
                
                ResultSet checkRs = MySQL.executeSearch(
                    "SELECT COUNT(*) as count FROM teacher_leave "
                    + "WHERE teacher_NIC = '" + session.getTeacherNIC() + "' "
                    + "AND status IN ('Pending', 'Approved') "
                    + "AND (start_date <= '" + endDateStr + "' AND end_date >= '" + startDateStr + "')");
                
                if (checkRs.next() && checkRs.getInt("count") > 0) {
                    JOptionIcon.showWarningDialog(this, "You already have a leave request for this date range.\n"
                        + "Please choose different dates or wait for the existing request to be processed.");
                    checkRs.close();
                    return;
                }
                checkRs.close();

                String reason = txtReason.getText().trim().replace("'", "''");

                MySQL.executeIUD(
                    "INSERT INTO teacher_leave (teacher_NIC, leave_type_id, start_date, end_date, reason, status) "
                    + "VALUES ('" + session.getTeacherNIC() + "', " + typeId + ", "
                    + "'" + sdf.format(startDate) + "', '" + sdf.format(endDate) + "', "
                    + "'" + reason + "', 'Pending')");

                loadLeaveData();
                JOptionIcon.showSuccessDialog(this, "Leave request submitted successfully!\nStatus: Pending (awaiting Admin approval)");

            } catch (Exception e) {
                JOptionIcon.showErrorDialog(this, "Error submitting leave request: " + e.getMessage());
            }
        }
    }
}
