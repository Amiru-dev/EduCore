package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;
import lk.educore.schoolms.validate.DataValidator;
import lk.educore.schoolms.exceptions.ValidationException;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Attendance Frame
 * QR code scanner for teacher attendance using OpenCV camera
 */
public class AttendanceFrame extends JFrame {

    private JLabel lblCameraFeed;
    private JLabel lblScannedNIC;
    private JLabel lblTeacherName;
    private JLabel lblCheckInTime;
    private JButton btnStartCamera, btnStopCamera, btnMarkAttendance, btnClose;
    private boolean cameraRunning = false;
    private Timer cameraTimer;

    public AttendanceFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("QR Attendance Scanner");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Camera feed panel
        JPanel cameraPanel = new JPanel(new BorderLayout());
        cameraPanel.setBorder(BorderFactory.createTitledBorder("Camera Feed"));
        
        lblCameraFeed = new JLabel("Camera not started", SwingConstants.CENTER);
        lblCameraFeed.setPreferredSize(new Dimension(400, 300));
        lblCameraFeed.setBackground(Color.BLACK);
        lblCameraFeed.setOpaque(true);
        lblCameraFeed.setForeground(Color.WHITE);
        lblCameraFeed.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        cameraPanel.add(lblCameraFeed, BorderLayout.CENTER);
        mainPanel.add(cameraPanel, BorderLayout.CENTER);

        // Control panel
        JPanel controlPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        
        btnStartCamera = new JButton("Start Camera");
        btnStartCamera.addActionListener(e -> startCamera());
        
        btnStopCamera = new JButton("Stop Camera");
        btnStopCamera.setEnabled(false);
        btnStopCamera.addActionListener(e -> stopCamera());
        
        btnMarkAttendance = new JButton("Mark Attendance");
        btnMarkAttendance.setEnabled(false);
        btnMarkAttendance.addActionListener(e -> markAttendance());
        
        btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());

        controlPanel.add(btnStartCamera);
        controlPanel.add(btnStopCamera);
        controlPanel.add(btnMarkAttendance);
        controlPanel.add(btnClose);

        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        // Scanned info panel
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Scanned Information"));
        
        infoPanel.add(new JLabel("Teacher NIC:"));
        lblScannedNIC = new JLabel();
        infoPanel.add(lblScannedNIC);
        
        infoPanel.add(new JLabel("Teacher Name:"));
        lblTeacherName = new JLabel();
        infoPanel.add(lblTeacherName);
        
        infoPanel.add(new JLabel("Check-in Time:"));
        lblCheckInTime = new JLabel();
        infoPanel.add(lblCheckInTime);
        
        infoPanel.add(new JLabel("Status:"));
        JLabel lblStatus = new JLabel("Waiting for scan...");
        infoPanel.add(lblStatus);

        mainPanel.add(infoPanel, BorderLayout.NORTH);

        add(mainPanel);
    }

    private void startCamera() {
        // This is a placeholder for OpenCV camera integration
        // In a real implementation, you would use OpenCV to capture video and detect QR codes
        
        cameraRunning = true;
        btnStartCamera.setEnabled(false);
        btnStopCamera.setEnabled(true);
        btnMarkAttendance.setEnabled(true);
        
        lblCameraFeed.setText("Camera Active - Simulated");
        lblCameraFeed.setBackground(new Color(0, 100, 0));
        
        // Simulate camera feed with timer
        cameraTimer = new Timer(100, e -> {
            // Simulate camera updates
            if (cameraRunning) {
                // In real implementation, this would process camera frames
                // and detect QR codes
            }
        });
        cameraTimer.start();
        
        JOptionIcon.showInfoDialog(this, "Camera started. Place QR code in front of camera.\n" +
            "Note: This is a simulation. OpenCV integration required for actual QR scanning.");
    }

    private void stopCamera() {
        cameraRunning = false;
        btnStartCamera.setEnabled(true);
        btnStopCamera.setEnabled(false);
        btnMarkAttendance.setEnabled(false);
        
        if (cameraTimer != null) {
            cameraTimer.stop();
        }
        
        lblCameraFeed.setText("Camera stopped");
        lblCameraFeed.setBackground(Color.BLACK);
    }

    private void markAttendance() {
        // For simulation, manually enter NIC
        String nic = JOptionPane.showInputDialog(this, "Enter Teacher NIC for attendance:");
        
        if (nic != null && !nic.trim().isEmpty()) {
            try {
                DataValidator.isValidNIC(nic.trim());
                
                // Load teacher information
                ResultSet rs = MySQL.executeSearch("SELECT first_name, last_name FROM teacher WHERE NIC = '" + nic.trim() + "' AND status_id = 1");
                
                if (rs.next()) {
                    String name = rs.getString("first_name") + " " + rs.getString("last_name");
                    
                    lblScannedNIC.setText(nic.trim());
                    lblTeacherName.setText(name);
                    
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    lblCheckInTime.setText(now.format(formatter));
                    
                    // Record attendance
                    recordAttendance(nic.trim());
                    
                    JOptionIcon.showSuccessDialog(this, "Attendance marked for " + name);
                    
                } else {
                    JOptionIcon.showErrorDialog(this, "Teacher not found or inactive: " + nic);
                }
                
                rs.close();
                
            } catch (ValidationException e) {
                JOptionIcon.showErrorDialog(this, e.getUserFriendlyMessage());
            } catch (Exception e) {
                JOptionIcon.showErrorDialog(this, "Error marking attendance: " + e.getMessage());
            }
        }
    }

    private void recordAttendance(String nic) {
        try {
            // Check if attendance already marked today
            ResultSet checkRs = MySQL.executeSearch("SELECT COUNT(*) FROM teacher_attendance WHERE teacher_NIC = '" + nic + "' AND DATE(date_time) = CURDATE()");
            
            if (checkRs.next() && checkRs.getInt(1) > 0) {
                JOptionIcon.showWarningDialog(this, "Attendance already marked for today.");
                checkRs.close();
                return;
            }
            
            checkRs.close();
            
            // Insert attendance record
            MySQL.executeIUD("INSERT INTO teacher_attendance (teacher_NIC, date_time, check_in) VALUES ('" + nic + "', NOW(), CURTIME())");
            
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error recording attendance: " + e.getMessage());
        }
    }

    @Override
    public void dispose() {
        if (cameraTimer != null) {
            cameraTimer.stop();
        }
        super.dispose();
    }
}