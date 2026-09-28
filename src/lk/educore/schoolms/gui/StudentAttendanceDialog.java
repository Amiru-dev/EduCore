package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StudentAttendanceDialog extends JDialog {

    private String studentId;
    private JDateChooser dateChooser;
    private JComboBox<String> cmbStatus;
    private JSpinner spinCheckIn;
    private JSpinner spinCheckOut;
    private JTextArea txtRemarks;
    private JButton btnSave, btnCancel;

    public StudentAttendanceDialog(Frame parent, String studentId) {
        super(parent, "Mark Attendance", true);
        this.studentId = studentId;
        initComponents();
        loadExistingAttendance();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(450, 400);
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.0;
        mainPanel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(new JLabel(studentId), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.0;
        mainPanel.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        dateChooser = new JDateChooser(new Date());
        dateChooser.setDateFormatString("yyyy-MM-dd");
        // Reload if date changes
        dateChooser.getDateEditor().addPropertyChangeListener(e -> {
            if ("date".equals(e.getPropertyName())) {
                loadExistingAttendance();
            }
        });
        mainPanel.add(dateChooser, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0.0;
        mainPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cmbStatus = new JComboBox<>(new String[]{"Present", "Absent", "Late"});
        mainPanel.add(cmbStatus, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.weightx = 0.0;
        mainPanel.add(new JLabel("Check-in Time:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        spinCheckIn = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditorIn = new JSpinner.DateEditor(spinCheckIn, "HH:mm:ss");
        spinCheckIn.setEditor(timeEditorIn);
        mainPanel.add(spinCheckIn, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.weightx = 0.0;
        mainPanel.add(new JLabel("Check-out Time:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        spinCheckOut = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditorOut = new JSpinner.DateEditor(spinCheckOut, "HH:mm:ss");
        spinCheckOut.setEditor(timeEditorOut);
        mainPanel.add(spinCheckOut, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.weightx = 0.0;
        mainPanel.add(new JLabel("Remarks:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        txtRemarks = new JTextArea(3, 20);
        txtRemarks.setLineWrap(true);
        txtRemarks.setWrapStyleWord(true);
        mainPanel.add(new JScrollPane(txtRemarks), gbc);

        add(mainPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnSave = new JButton("Save Attendance");
        btnSave.addActionListener(e -> saveAttendance());
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadExistingAttendance() {
        if (dateChooser.getDate() == null) return;
        String date = new SimpleDateFormat("yyyy-MM-dd").format(dateChooser.getDate());
        try {
            String query = "SELECT * FROM student_attendance WHERE student_id = '" + studentId + "' AND attendance_date = '" + date + "'";
            ResultSet rs = MySQL.executeSearch(query);
            if (rs.next()) {
                cmbStatus.setSelectedItem(rs.getString("status"));
                txtRemarks.setText(rs.getString("remarks"));
                
                String checkIn = rs.getString("check_in_time");
                if (checkIn != null) {
                    Date inTime = new SimpleDateFormat("HH:mm:ss").parse(checkIn);
                    spinCheckIn.setValue(inTime);
                }
                
                String checkOut = rs.getString("check_out_time");
                if (checkOut != null) {
                    Date outTime = new SimpleDateFormat("HH:mm:ss").parse(checkOut);
                    spinCheckOut.setValue(outTime);
                }
            } else {
                cmbStatus.setSelectedIndex(0);
                txtRemarks.setText("");
            }
            rs.close();
        } catch (Exception ex) {
            // Ignore error for new dates
        }
    }

    private void saveAttendance() {
        if (dateChooser.getDate() == null) {
            JOptionIcon.showWarningDialog(this, "Please select a date.");
            return;
        }
        
        String date = new SimpleDateFormat("yyyy-MM-dd").format(dateChooser.getDate());
        String status = (String) cmbStatus.getSelectedItem();
        String checkIn = new SimpleDateFormat("HH:mm:ss").format((Date) spinCheckIn.getValue());
        String checkOut = new SimpleDateFormat("HH:mm:ss").format((Date) spinCheckOut.getValue());
        String remarks = txtRemarks.getText().trim();
        String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        try {
            String checkQuery = "SELECT attendance_id FROM student_attendance WHERE student_id = '" + studentId + "' AND attendance_date = '" + date + "'";
            ResultSet rs = MySQL.executeSearch(checkQuery);
            if (rs.next()) {
                // Update
                String updateQuery = "UPDATE student_attendance SET status = '" + status + "', " +
                        "check_in_time = '" + checkIn + "', " +
                        "check_out_time = '" + checkOut + "', " +
                        "remarks = '" + remarks + "' " +
                        "WHERE student_id = '" + studentId + "' AND attendance_date = '" + date + "'";
                MySQL.executeIUD(updateQuery);
                JOptionIcon.showSuccessDialog(this, "Attendance updated successfully.");
            } else {
                // Insert
                String insertQuery = "INSERT INTO student_attendance (student_id, date_time, attendance_date, status, check_in_time, check_out_time, remarks) " +
                        "VALUES ('" + studentId + "', '" + datetime + "', '" + date + "', '" + status + "', '" + checkIn + "', '" + checkOut + "', '" + remarks + "')";
                MySQL.executeIUD(insertQuery);
                JOptionIcon.showSuccessDialog(this, "Attendance marked successfully.");
            }
            rs.close();
            dispose();
        } catch (Exception ex) {
            JOptionIcon.showErrorDialog(this, "Error saving attendance: " + ex.getMessage());
        }
    }
}
