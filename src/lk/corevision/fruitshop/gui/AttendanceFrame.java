package lk.corevision.fruitshop.gui;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;
import java.util.logging.Level;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import lk.corevision.fruitshop.exceptions.DatabaseException;
import lk.corevision.fruitshop.model.MySQL;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class AttendanceFrame extends JFrame {

    static {
        String dllPath = System.getProperty("user.dir")
                + File.separator
                + "opencv"
                + File.separator
                + "opencv_java490.dll";
        System.load(dllPath);
    }

    // --- Constants ---
    private static final int MINIMUM_CHECKOUT_MINUTES = 2;
    private static final long DUPLICATE_SCAN_COOLDOWN_MS = 5000;

    // --- Colors ---
    private static final Color BG_DARK = new Color(26, 26, 46);
    private static final Color BG_LIGHTER = new Color(22, 33, 62);
    private static final Color ACCENT = new Color(254, 122, 21);
    private static final Color TEXT_PRIMARY = Color.WHITE;
    private static final Color TEXT_SECONDARY = new Color(200, 200, 200);

    // Status colors
    private static final Color STATUS_WAITING = new Color(108, 117, 125);
    private static final Color STATUS_SCANNING = new Color(13, 110, 253);
    private static final Color STATUS_SUCCESS = new Color(25, 135, 84);
    private static final Color STATUS_WARNING = new Color(253, 126, 20);
    private static final Color STATUS_ERROR = new Color(220, 53, 69);
    private static final Color STATUS_COMPLETED = new Color(13, 202, 240);

    // --- State Fields ---
    private final Employee parentPanel;
    private VideoCapture camera;
    private Timer cameraTimer;
    private Timer clockTimer;
    private boolean processing = false;
    private String lastScannedNIC = "";
    private long lastScanTimeMillis = 0;

    // Drag support
    private int pX, pY;

    // --- UI Components ---
    private JLabel clockLabel;
    private JLabel cameraStatusLabel;
    private CameraPanel cameraPanel;
    private JLabel statusLabel;
    private JPanel statusPanel;

    // Employee details UI (CardLayout)
    private CardLayout rightCardLayout;
    private JPanel rightCardContainer;

    // Waiting Card fields
    private JLabel empNameLabel;
    private JLabel empNicLabel;
    private JLabel empDateLabel;
    private JLabel empTimeLabel;
    private JLabel empStatusBadge;

    // Success Card fields
    private JLabel successNameLabel;
    private JLabel successDeptLabel;
    private JLabel successTimeLabel;
    private JLabel successActionBadge;

    public AttendanceFrame(Employee employee) {
        this.parentPanel = employee;
        initComponents();
        startCamera();
        startLiveClock();
        setVisible(true);
    }

    private void initComponents() {
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main container with gradient-like background
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(BG_DARK);
        mainContainer.setBorder(BorderFactory.createLineBorder(ACCENT, 1));

        // Window drag listener
        mainContainer.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                pX = me.getX();
                pY = me.getY();
            }
        });
        mainContainer.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent me) {
                setLocation(getLocation().x + me.getX() - pX, getLocation().y + me.getY() - pY);
            }
        });

        mainContainer.add(createHeaderPanel(), BorderLayout.NORTH);
        mainContainer.add(createCenterPanel(), BorderLayout.CENTER);
        mainContainer.add(createStatusBar(), BorderLayout.SOUTH);

        setContentPane(mainContainer);

        // Escape key binding to close
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "closeFrame");
        getRootPane().getActionMap().put("closeFrame", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                stopCamera();
            }
        });
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_DARK);
        header.setBorder(new EmptyBorder(15, 25, 15, 25));

        // Camera Status (Left)
        cameraStatusLabel = new JLabel("● Camera Connecting...");
        cameraStatusLabel.setFont(new Font("Inter", Font.BOLD, 14));
        cameraStatusLabel.setForeground(STATUS_WARNING);
        header.add(cameraStatusLabel, BorderLayout.WEST);

        // Title (Center)
        JLabel titleLabel = new JLabel("QR Attendance System");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(titleLabel, BorderLayout.CENTER);

        // Clock & Close (Right)
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        rightPanel.setOpaque(false);

        clockLabel = new JLabel("Loading Time...");
        clockLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        clockLabel.setForeground(TEXT_SECONDARY);

        JButton closeButton = new JButton("×");
        closeButton.setFont(new Font("Inter", Font.BOLD, 24));
        closeButton.setForeground(TEXT_SECONDARY);
        closeButton.setContentAreaFilled(false);
        closeButton.setBorderPainted(false);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> stopCamera());
        closeButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                closeButton.setForeground(STATUS_ERROR);
            }

            public void mouseExited(MouseEvent e) {
                closeButton.setForeground(TEXT_SECONDARY);
            }
        });

        rightPanel.add(clockLabel);
        rightPanel.add(closeButton);
        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(20, 0));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Left: Camera
        cameraPanel = new CameraPanel();
        cameraPanel.setPreferredSize(new Dimension(640, 480));

        JPanel cameraContainer = new JPanel(new BorderLayout());
        cameraContainer.setOpaque(false);
        cameraContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BG_LIGHTER, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        cameraContainer.add(cameraPanel, BorderLayout.CENTER);

        // Right: Employee Details (CardLayout)
        rightCardLayout = new CardLayout();
        rightCardContainer = new JPanel(rightCardLayout);
        rightCardContainer.setOpaque(false);

        rightCardContainer.add(createWaitingCard(), "WAITING");
        rightCardContainer.add(createSuccessCard(), "SUCCESS");

        centerPanel.add(cameraContainer, BorderLayout.CENTER);
        centerPanel.add(rightCardContainer, BorderLayout.EAST);

        return centerPanel;
    }

    private JPanel createWaitingCard() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_LIGHTER);
        panel.setPreferredSize(new Dimension(400, 0));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Avatar
        AvatarPanel avatar = new AvatarPanel();
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Labels
        empNameLabel = createDetailLabel("Name: --", 20, Font.BOLD);
        empNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        empNicLabel = createDetailLabel("NIC: --", 16, Font.PLAIN);
        empNicLabel.setForeground(TEXT_SECONDARY);
        empNicLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        empDateLabel = createDetailLabel("Date: --", 16, Font.PLAIN);
        empDateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        empTimeLabel = createDetailLabel("Time: --", 16, Font.PLAIN);
        empTimeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Status Badge
        empStatusBadge = new JLabel("WAITING", SwingConstants.CENTER);
        empStatusBadge.setFont(new Font("Inter", Font.BOLD, 14));
        empStatusBadge.setForeground(Color.WHITE);
        empStatusBadge.setOpaque(true);
        empStatusBadge.setBackground(STATUS_WAITING);
        empStatusBadge.setPreferredSize(new Dimension(200, 40));
        empStatusBadge.setMaximumSize(new Dimension(250, 40));
        empStatusBadge.setAlignmentX(Component.CENTER_ALIGNMENT);
        empStatusBadge.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        panel.add(Box.createVerticalStrut(20));
        panel.add(avatar);
        panel.add(Box.createVerticalStrut(30));
        panel.add(empNameLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(empNicLabel);
        panel.add(Box.createVerticalStrut(30));
        panel.add(empDateLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(empTimeLabel);
        panel.add(Box.createVerticalGlue());
        panel.add(empStatusBadge);
        panel.add(Box.createVerticalStrut(20));

        return panel;
    }

    private JPanel createSuccessCard() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_LIGHTER);
        panel.setPreferredSize(new Dimension(400, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(STATUS_SUCCESS, 2),
                new EmptyBorder(30, 30, 30, 30)
        ));

        // Big Checkmark
        JLabel checkmark = new JLabel("✔", SwingConstants.CENTER);
        checkmark.setFont(new Font("Inter", Font.BOLD, 80));
        checkmark.setForeground(STATUS_SUCCESS);
        checkmark.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Attendance Recorded", SwingConstants.CENTER);
        title.setFont(new Font("Inter", Font.BOLD, 22));
        title.setForeground(STATUS_SUCCESS);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        successNameLabel = new JLabel("John Silva", SwingConstants.CENTER);
        successNameLabel.setFont(new Font("Inter", Font.BOLD, 26));
        successNameLabel.setForeground(TEXT_PRIMARY);
        successNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        successDeptLabel = new JLabel("Harvest Department", SwingConstants.CENTER);
        successDeptLabel.setFont(new Font("Inter", Font.PLAIN, 18));
        successDeptLabel.setForeground(TEXT_SECONDARY);
        successDeptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        successTimeLabel = new JLabel("08:21:14", SwingConstants.CENTER);
        successTimeLabel.setFont(new Font("Inter", Font.BOLD, 36));
        successTimeLabel.setForeground(TEXT_PRIMARY);
        successTimeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        successActionBadge = new JLabel("Check In", SwingConstants.CENTER);
        successActionBadge.setFont(new Font("Inter", Font.BOLD, 18));
        successActionBadge.setForeground(Color.WHITE);
        successActionBadge.setOpaque(true);
        successActionBadge.setBackground(STATUS_SUCCESS);
        successActionBadge.setPreferredSize(new Dimension(200, 50));
        successActionBadge.setMaximumSize(new Dimension(250, 50));
        successActionBadge.setAlignmentX(Component.CENTER_ALIGNMENT);
        successActionBadge.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        panel.add(Box.createVerticalStrut(20));
        panel.add(checkmark);
        panel.add(title);
        panel.add(Box.createVerticalStrut(40));
        panel.add(successNameLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(successDeptLabel);
        panel.add(Box.createVerticalStrut(40));
        panel.add(successTimeLabel);
        panel.add(Box.createVerticalGlue());
        panel.add(successActionBadge);
        panel.add(Box.createVerticalStrut(20));

        return panel;
    }

    private JLabel createDetailLabel(String text, int size, int style) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Inter", style, size));
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    private JPanel createStatusBar() {
        statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(STATUS_WAITING);
        statusPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        statusLabel = new JLabel("⏳ Waiting for QR Code...");
        statusLabel.setFont(new Font("Inter", Font.BOLD, 16));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        statusPanel.add(statusLabel, BorderLayout.CENTER);
        return statusPanel;
    }

    private void startLiveClock() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy  •  hh:mm:ss a");
        clockTimer = new Timer(1000, e -> {
            clockLabel.setText(sdf.format(new Date()));
        });
        clockTimer.start();
    }

    private void startCamera() {
        camera = new VideoCapture(0);

        if (!camera.isOpened()) {
            cameraStatusLabel.setText("● Camera Disconnected");
            cameraStatusLabel.setForeground(STATUS_ERROR);
            setStatus(STATUS_ERROR, "🔴 Camera unavailable. Please check connection.");
            return;
        }

        cameraStatusLabel.setText("● Camera Connected");
        cameraStatusLabel.setForeground(STATUS_SUCCESS);

        final Mat frame = new Mat(); // Reuse single buffer to prevent GC churn
        cameraTimer = new Timer(30, e -> {
            if (processing) {
                return;
            }

            if (camera.read(frame)) {
                BufferedImage image = OpenCVUtils.matToBufferedImage(frame);
                cameraPanel.setImage(image);

                String qrText = scanQRCode(image);
                if (qrText != null && !qrText.trim().isEmpty()) {
                    handleScanDetection(qrText.trim());
                }
            }
        });
        cameraTimer.start();
    }

    private void stopCamera() {
        if (cameraTimer != null) {
            cameraTimer.stop();
        }
        if (clockTimer != null) {
            clockTimer.stop();
        }
        if (cameraPanel != null) {
            cameraPanel.stopPulse();
        }
        if (camera != null && camera.isOpened()) {
            camera.release();
        }
        dispose();
    }

    private String scanQRCode(BufferedImage image) {
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = new QRCodeReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException | ChecksumException | FormatException ex) {
            return null;
        }
    }

    private void handleScanDetection(String nic) {
        long now = System.currentTimeMillis();
        if (nic.equals(lastScannedNIC) && (now - lastScanTimeMillis) < DUPLICATE_SCAN_COOLDOWN_MS) {
            return; // Ignore duplicate scan
        }

        processing = true;
        cameraTimer.stop(); // Freeze camera
        lastScannedNIC = nic;
        lastScanTimeMillis = now;

        setStatus(STATUS_SCANNING, "🔍 Scanning...");
        cameraPanel.setScanState(CameraPanel.ScanState.SCANNING);

        SwingUtilities.invokeLater(() -> {
            empNameLabel.setText("Verifying employee...");
            updateBadge(STATUS_SCANNING, "VERIFYING");
            rightCardLayout.show(rightCardContainer, "WAITING"); // Ensure waiting card is shown
        });

        // 500ms artificial delay for scanning laser animation
        Timer delayTimer = new Timer(500, e -> {
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    handleAttendance(nic);
                    return null;
                }
            };
            worker.execute();
        });
        delayTimer.setRepeats(false);
        delayTimer.start();
    }

    private void handleAttendance(String nic) {
        try {
            // 1. Verify Employee
            String[] employeeData = findEmployeeByNIC(nic);
            if (employeeData == null) {
                playErrorBeep();
                setStatus(STATUS_ERROR, "🔴 Invalid QR — Employee Not Found");
                updateBadge(STATUS_ERROR, "INVALID");
                cameraPanel.setScanState(CameraPanel.ScanState.ERROR);
                scheduleReset();
                return;
            }

            String fullName = employeeData[0] + " " + employeeData[1];
            String department = employeeData[2] != null ? employeeData[2] : "Employee";

            displayEmployeeDetails(fullName, nic);

            // 2. Check/Record Attendance
            Date now = new Date();
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(now);
            String currentTime = new SimpleDateFormat("HH:mm:ss").format(now);

            SwingUtilities.invokeLater(() -> {
                empDateLabel.setText("Date: " + currentDate);
                empTimeLabel.setText("Time: " + currentTime);
            });

            ResultSet rs = findTodayAttendance(nic, currentDate);

            if (rs.next()) {
                // Already checked in today
                if (rs.getTime("checkout_time") != null) {
                    playErrorBeep(); // Already completed, not an error but a warning sound
                    setStatus(STATUS_COMPLETED, "🔵 Attendance Already Completed for Today");
                    updateBadge(STATUS_COMPLETED, "COMPLETED");
                    cameraPanel.setScanState(CameraPanel.ScanState.ERROR); // Flash red for warning
                    scheduleReset();
                    return;
                }

                // Check 2 minute rule
                java.sql.Time sqlCheckin = rs.getTime("checkin_time");
                LocalTime checkinTime = sqlCheckin.toLocalTime();
                LocalTime currentTimeObj = LocalTime.now();

                long minutes = Duration.between(checkinTime, currentTimeObj).toMinutes();

                if (minutes < MINIMUM_CHECKOUT_MINUTES) {
                    playErrorBeep();
                    setStatus(STATUS_WARNING, "🟠 Checkout available after " + MINIMUM_CHECKOUT_MINUTES + " minutes");
                    updateBadge(STATUS_WARNING, "WAITING");
                    cameraPanel.setScanState(CameraPanel.ScanState.ERROR); // Flash red for warning
                    scheduleReset();
                    return;
                }

                // Update Checkout
                updateCheckOut(rs.getInt("employee_attendance_id"), currentTime);
                playSuccessBeep();
                setStatus(STATUS_SUCCESS, "🟢 Check-Out Successful — " + currentTime);

                showSuccessCard(fullName, department, currentTime, "Check Out");

            } else {
                // Insert Check-in
                insertCheckIn(nic, currentDate, currentTime);
                playSuccessBeep();
                setStatus(STATUS_SUCCESS, "🟢 Check-In Successful — " + currentTime);

                showSuccessCard(fullName, department, currentTime, "Check In");
            }

            // Refresh parent table safely
            SwingUtilities.invokeLater(() -> {
                if (parentPanel != null) {
                    parentPanel.loadEmployeesAttendance();
                    parentPanel.resetAttendance();
                }
            });

            scheduleReset();

        } catch (DatabaseException ex) {
            playErrorBeep();
            SignIn.getLoggerObject().log(Level.SEVERE, "Database error during attendance", ex);
            setStatus(STATUS_ERROR, "🔴 Database unavailable — please check connection");
            cameraPanel.setScanState(CameraPanel.ScanState.ERROR);
            scheduleReset();
        } catch (Exception ex) {
            playErrorBeep();
            SignIn.getLoggerObject().log(Level.SEVERE, "Unexpected attendance error", ex);
            setStatus(STATUS_ERROR, "🔴 Unexpected error occurred");
            cameraPanel.setScanState(CameraPanel.ScanState.ERROR);
            scheduleReset();
        }
    }

    private void showSuccessCard(String name, String dept, String time, String action) {
        SwingUtilities.invokeLater(() -> {
            successNameLabel.setText(name);
            successDeptLabel.setText(dept);
            successTimeLabel.setText(time);
            successActionBadge.setText(action);

            cameraPanel.setScanState(CameraPanel.ScanState.SUCCESS);
            rightCardLayout.show(rightCardContainer, "SUCCESS");
        });
    }

    private void scheduleReset() {
        Timer restartTimer = new Timer(3000, ev -> {
            resetEmployeeDetails();
            setStatus(STATUS_WAITING, "⏳ Waiting for QR Code...");
            cameraPanel.setScanState(CameraPanel.ScanState.WAITING);
            rightCardLayout.show(rightCardContainer, "WAITING");
            processing = false;
            cameraTimer.start();
        });
        restartTimer.setRepeats(false);
        restartTimer.start();
    }

    private String[] findEmployeeByNIC(String nic) throws DatabaseException {
        try {
            ResultSet rs = MySQL.executeSearch(
                    "SELECT e.first_name, e.last_name, et.employee_type "
                    + "FROM employee e "
                    + "INNER JOIN employee_type et ON e.employee_type_employee_type_id = et.employee_type_id "
                    + "WHERE e.NIC='" + nic + "'"
            );
            if (rs.next()) {
                return new String[]{
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("employee_type")
                };
            }
            return null;
        } catch (Exception e) {
            throw new DatabaseException("Failed to find employee", e);
        }
    }

    private ResultSet findTodayAttendance(String nic, String currentDate) throws DatabaseException {
        try {
            return MySQL.executeSearch(
                    "SELECT * FROM employee_attendance "
                    + "WHERE employee_NIC='" + nic + "' "
                    + "AND date_time='" + currentDate + "' "
                    + "ORDER BY employee_attendance_id DESC LIMIT 1"
            );
        } catch (Exception e) {
            throw new DatabaseException("Failed to search attendance", e);
        }
    }

    private void insertCheckIn(String nic, String date, String time) throws DatabaseException {
        try {
            MySQL.executeIUD(
                    "INSERT INTO employee_attendance("
                    + "date_time, checkin_time, "
                    + "employee_attendance_status_employee_attendance_status_id, "
                    + "employee_attendance_type_employee_attendance_type_id, "
                    + "employee_NIC) "
                    + "VALUES('" + date + "', '" + time + "', '1', '1', '" + nic + "')"
            );
        } catch (Exception e) {
            throw new DatabaseException("Failed to insert check-in", e);
        }
    }

    private void updateCheckOut(int attendanceId, String time) throws DatabaseException {
        try {
            MySQL.executeIUD(
                    "UPDATE employee_attendance SET checkout_time='" + time + "' "
                    + "WHERE employee_attendance_id=" + attendanceId
            );
        } catch (Exception e) {
            throw new DatabaseException("Failed to update check-out", e);
        }
    }

    private void setStatus(Color color, String text) {
        SwingUtilities.invokeLater(() -> {
            statusPanel.setBackground(color);
            statusLabel.setText(text);
        });
    }

    private void updateBadge(Color color, String text) {
        SwingUtilities.invokeLater(() -> {
            empStatusBadge.setBackground(color);
            empStatusBadge.setText(text);
        });
    }

    private void displayEmployeeDetails(String name, String nic) {
        SwingUtilities.invokeLater(() -> {
            empNameLabel.setText("Name: " + name);
            empNicLabel.setText("NIC: " + nic);
        });
    }

    private void resetEmployeeDetails() {
        SwingUtilities.invokeLater(() -> {
            empNameLabel.setText("Name: --");
            empNicLabel.setText("NIC: --");
            empDateLabel.setText("Date: --");
            empTimeLabel.setText("Time: --");
            updateBadge(STATUS_WAITING, "WAITING");
        });
    }

    // --- Sound Effects ---
    private void playSuccessBeep() {
        playTone(880, 150); // Higher pitch, short
    }

    private void playErrorBeep() {
        playTone(440, 300); // Lower pitch, longer
    }

    private void playTone(int hz, int msecs) {
        new Thread(() -> {
            try {
                float sampleRate = 8000f;
                byte[] buf = new byte[1];
                AudioFormat af = new AudioFormat(sampleRate, 8, 1, true, false);
                SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
                sdl.open(af);
                sdl.start();
                for (int i = 0; i < msecs * 8; i++) {
                    double angle = i / (sampleRate / hz) * 2.0 * Math.PI;
                    buf[0] = (byte) (Math.sin(angle) * 127.0);
                    sdl.write(buf, 0, 1);
                }
                sdl.drain();
                sdl.stop();
                sdl.close();
            } catch (Exception e) {
                // Ignore sound errors
            }
        }).start();
    }

    // --- Inner Classes ---
    private class CameraPanel extends JPanel {

        public enum ScanState {
            WAITING, SCANNING, SUCCESS, ERROR
        }

        private BufferedImage image;
        private float pulseAlpha = 1.0f;
        private boolean fadingOut = true;
        private Timer pulseTimer;

        private ScanState scanState = ScanState.WAITING;
        private int scanLineY = 0;
        private boolean scanLineDown = true;

        public CameraPanel() {
            pulseTimer = new Timer(50, e -> {
                if (scanState == ScanState.WAITING) {
                    if (fadingOut) {
                        pulseAlpha -= 0.05f;
                        if (pulseAlpha <= 0.3f) {
                            fadingOut = false;
                        }
                    } else {
                        pulseAlpha += 0.05f;
                        if (pulseAlpha >= 1.0f) {
                            fadingOut = true;
                        }
                    }
                } else if (scanState == ScanState.SCANNING) {
                    if (scanLineDown) {
                        scanLineY += 15;
                        if (scanLineY >= 250) {
                            scanLineDown = false;
                        }
                    } else {
                        scanLineY -= 15;
                        if (scanLineY <= 0) {
                            scanLineDown = true;
                        }
                    }
                }
                repaint();
            });
            pulseTimer.start();
        }

        public void setImage(BufferedImage img) {
            this.image = img;
            repaint();
        }

        public void setScanState(ScanState state) {
            this.scanState = state;
            if (state == ScanState.SCANNING) {
                scanLineY = 0;
                scanLineDown = true;
            }
            repaint();
        }

        public void stopPulse() {
            if (pulseTimer != null) {
                pulseTimer.stop();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw camera feed scaled (Cover Mode)
                double scaleX = (double) getWidth() / image.getWidth();
                double scaleY = (double) getHeight() / image.getHeight();
                double scale = Math.max(scaleX, scaleY);
                int drawW = (int) (image.getWidth() * scale);
                int drawH = (int) (image.getHeight() * scale);
                int drawX = (getWidth() - drawW) / 2;
                int drawY = (getHeight() - drawH) / 2;
                g2.drawImage(image, drawX, drawY, drawW, drawH, null);

                // Draw Overlay
                g2.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black

                // Guide box dimensions
                int boxSize = 250;
                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                int bx = cx - boxSize / 2;
                int by = cy - boxSize / 2;

                // Top, Bottom, Left, Right dark areas
                g2.fillRect(0, 0, getWidth(), by);
                g2.fillRect(0, by + boxSize, getWidth(), getHeight() - (by + boxSize));
                g2.fillRect(0, by, bx, boxSize);
                g2.fillRect(bx + boxSize, by, getWidth() - (bx + boxSize), boxSize);

                // Draw guide box border depending on state
                g2.setStroke(new BasicStroke(2f));
                if (scanState == ScanState.SUCCESS) {
                    g2.setColor(STATUS_SUCCESS);
                    g2.setStroke(new BasicStroke(6f));
                    g2.drawRoundRect(bx, by, boxSize, boxSize, 20, 20);
                } else if (scanState == ScanState.ERROR) {
                    g2.setColor(STATUS_ERROR);
                    g2.setStroke(new BasicStroke(6f));
                    g2.drawRoundRect(bx, by, boxSize, boxSize, 20, 20);
                } else {
                    g2.setColor(new Color(255, 255, 255, 100));
                    g2.drawRoundRect(bx, by, boxSize, boxSize, 20, 20);
                }

                // Draw pulsing corners if WAITING
                if (scanState == ScanState.WAITING || scanState == ScanState.SCANNING) {
                    int cornerLen = 30;

                    if (scanState == ScanState.WAITING) {
                        g2.setColor(new Color(ACCENT.getRed(), ACCENT.getGreen(), ACCENT.getBlue(), (int) (255 * pulseAlpha)));
                    } else {
                        g2.setColor(ACCENT); // Solid orange while scanning
                    }
                    g2.setStroke(new BasicStroke(4f));

                    // Top Left
                    g2.drawLine(bx, by + cornerLen, bx, by);
                    g2.drawLine(bx, by, bx + cornerLen, by);

                    // Top Right
                    g2.drawLine(bx + boxSize - cornerLen, by, bx + boxSize, by);
                    g2.drawLine(bx + boxSize, by, bx + boxSize, by + cornerLen);

                    // Bottom Left
                    g2.drawLine(bx, by + boxSize - cornerLen, bx, by + boxSize);
                    g2.drawLine(bx, by + boxSize, bx + cornerLen, by + boxSize);

                    // Bottom Right
                    g2.drawLine(bx + boxSize - cornerLen, by + boxSize, bx + boxSize, by + boxSize);
                    g2.drawLine(bx + boxSize, by + boxSize, bx + boxSize, by + boxSize - cornerLen);
                }

                // Draw laser line if SCANNING
                if (scanState == ScanState.SCANNING) {
                    g2.setColor(new Color(13, 110, 253, 200)); // Blue laser
                    g2.fillRect(bx, by + scanLineY, boxSize, 4);
                    // Light glow
                    g2.setColor(new Color(13, 110, 253, 100));
                    g2.fillRect(bx, by + scanLineY - 2, boxSize, 8);
                }

                // Draw result overlay (Checkmark / X)
                if (scanState == ScanState.SUCCESS) {
                    g2.setColor(STATUS_SUCCESS);
                    g2.setFont(new Font("Inter", Font.BOLD, 100));
                    String mark = "✔";
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(mark, cx - fm.stringWidth(mark) / 2, cy + fm.getAscent() / 3);
                } else if (scanState == ScanState.ERROR) {
                    g2.setColor(STATUS_ERROR);
                    g2.setFont(new Font("Inter", Font.BOLD, 100));
                    String mark = "✖";
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(mark, cx - fm.stringWidth(mark) / 2, cy + fm.getAscent() / 3);
                }

                // Instruction text
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Inter", Font.BOLD, 14));
                String instruction = scanState == ScanState.WAITING ? "Place employee QR inside the box"
                        : scanState == ScanState.SCANNING ? "Scanning..." : "";
                if (!instruction.isEmpty()) {
                    FontMetrics fm = g2.getFontMetrics();
                    int textX = cx - fm.stringWidth(instruction) / 2;
                    int textY = by + boxSize + 30;
                    g2.drawString(instruction, textX, textY);
                }
            }
        }
    }

    private class AvatarPanel extends JPanel {

        public AvatarPanel() {
            setPreferredSize(new Dimension(100, 100));
            setMaximumSize(new Dimension(100, 100));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Background circle
            g2.setColor(new Color(40, 50, 80));
            g2.fillOval(0, 0, w, h);

            // Border
            g2.setColor(ACCENT);
            g2.setStroke(new BasicStroke(3f));
            g2.drawOval(1, 1, w - 2, h - 2);

            // User icon (head)
            g2.setColor(new Color(150, 150, 180));
            int headSize = w / 3;
            g2.fillOval(w / 2 - headSize / 2, h / 4, headSize, headSize);

            // User icon (body)
            int bodyW = (int) (w * 0.6);
            int bodyH = (int) (h * 0.4);
            g2.fillArc(w / 2 - bodyW / 2, h / 2 + 10, bodyW, bodyH * 2, 0, 180);
        }
    }

    // Main method for testing standalone (optional)
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            // new AttendanceFrame(null);
        });
    }
}

class OpenCVUtils {

    public static BufferedImage matToBufferedImage(Mat mat) {
        int width = mat.cols();
        int height = mat.rows();
        int channels = mat.channels();
        byte[] sourcePixels = new byte[width * height * channels];
        mat.get(0, 0, sourcePixels);

        BufferedImage image;
        if (channels == 3) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        } else {
            image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        }
        byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);
        return image;
    }
}
