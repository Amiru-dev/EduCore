package lk.educore.schoolms.gui;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import javaswingdev.chart.ModelPieChart;
import javaswingdev.chart.PieChart;
import lk.educore.schoolms.component.RoundPanel;
import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.model.SendLogo;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Student Dashboard
 * Full-featured, modern student portal with sidebar navigation, gradient stat cards,
 * interactive pie charts, and polished management panels matching Admin/Teacher UI.
 */
public class StudentDashboard extends JFrame {

    private String username;
    private String studentId;
    private String studentName = "";
    private String studentGrade = "";
    private int gradeId = -1;

    // Header & Content switching
    private JLabel lblHeaderTitle;
    private JPanel contentPanel;
    private JToggleButton[] navButtons;
    private ButtonGroup navButtonGroup;
    private int currentNavIndex = 0;

    // Dashboard stat labels
    private JLabel lblAttendanceRate, lblPendingFees, lblUpcomingExams, lblBooksBorrowed;
    private JPanel chartHolder1, chartHolder2;
    private JPanel noticesListPanel;

    // Profile labels
    private JLabel lblStudentIdVal, lblNameVal, lblGradeVal, lblEmailVal, lblPhoneVal, lblDobVal, lblGenderVal, lblAddressVal;
    private JLabel lblRegDateVal, lblStatusVal, lblGuardianVal, lblGuardianRelVal, lblGuardianPhoneVal, lblGuardianNicVal, lblGuardianOccVal;
    private JLabel lblProfileNameBadge, lblProfileIdBadge, lblProfileGradeBadge, lblProfileStatusBadge;

    // Results & Exams
    private JTable resultsTable, upcomingExamsTable;
    private DefaultTableModel resultsTableModel, upcomingExamsTableModel;
    private List<Integer> resultIdList = new ArrayList<>();
    private JTextField txtResultSearch;
    private JComboBox<String> cmbResultTerm;
    private JLabel lblResultAvg, lblResultTotal, lblResultHighest;

    // Attendance
    private JTable attendanceTable;
    private DefaultTableModel attendanceTableModel;
    private JComboBox<String> cmbAttendanceFilter;
    private JLabel lblAttTotal, lblAttPresent, lblAttAbsent, lblAttLate;

    // Fees
    private JTable feeTable;
    private DefaultTableModel feeTableModel;
    private List<Integer> feeIdList = new ArrayList<>();
    private JComboBox<String> cmbFeeFilter;
    private JLabel lblFeeTotal, lblFeePaid, lblFeePending;

    // Library
    private JTable myBooksTable, catalogTable;
    private DefaultTableModel myBooksTableModel, catalogTableModel;
    private JTextField txtCatalogSearch;
    private JComboBox<String> cmbCatalogCategory;
    private JComboBox<String> cmbMyBooksFilter;
    private JLabel lblLibIssued, lblLibOverdue, lblLibReturned, lblLibFines;

    // Events
    private JTable eventsTable;
    private DefaultTableModel eventsTableModel;
    private JTextField txtEventSearch;
    private JComboBox<String> cmbEventStatus;

    public StudentDashboard(String username) {
        this.username = username;
        resolveStudentId();
        initComponents();
        loadDashboardData();
    }

    /**
     * Resolve the student_id and basic metadata from username or student ID.
     */
    private void resolveStudentId() {
        try {
            String clean = username != null ? username.replace("'", "''") : "";
            ResultSet rs = MySQL.executeSearch(
                "SELECT s.student_id, CONCAT(s.first_name, ' ', s.last_name) AS name, " +
                "CONCAT(g.grade_name, ' - ', g.section) AS grade_name, s.grade_id " +
                "FROM student s " +
                "JOIN grade g ON s.grade_id = g.grade_id " +
                "LEFT JOIN user u ON s.user_id = u.user_id " +
                "WHERE u.username = '" + clean + "' OR s.student_id = '" + clean + "'"
            );
            if (rs.next()) {
                studentId = rs.getString("student_id");
                studentName = rs.getString("name");
                studentGrade = rs.getString("grade_name");
                gradeId = rs.getInt("grade_id");
            }
            rs.close();
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Failed to resolve student ID", e);
        }
    }

    // =====================================================================
    // INIT COMPONENTS
    // =====================================================================

    private void initComponents() {
        setTitle("EduCore School Management System - Student Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Window icon
        try {
            InputStream imgStream = getClass().getResourceAsStream("/lk/educore/schoolms/resources/logo.png");
            if (imgStream != null) {
                setIconImage(new ImageIcon(ImageIO.read(imgStream)).getImage());
                imgStream.close();
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Failed to load logo", e);
        }

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Sidebar
        mainPanel.add(createSidebar(), BorderLayout.WEST);

        // Top Header
        mainPanel.add(createHeader(), BorderLayout.NORTH);

        // Content Panel (Card / Replacement layout)
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(buildDashboardContent(), BorderLayout.CENTER);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
        setJMenuBar(createMenuBar());
    }

    // =====================================================================
    // SIDEBAR — EXACT MATCH TO ADMIN / TEACHER
    // =====================================================================

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBackground(new Color(44, 62, 80));

        // Logo Header
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(new Color(44, 62, 80));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel logoLabel = new JLabel("EduCore");
        try {
            InputStream is = getClass().getResourceAsStream("/lk/educore/schoolms/resources/logo.png");
            if (is != null) {
                Image img = ImageIO.read(is).getScaledInstance(35, 35, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(img));
                logoLabel.setIconTextGap(10);
                is.close();
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Failed to load sidebar logo", e);
        }
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logoLabel.setForeground(Color.WHITE);
        logoPanel.add(logoLabel);
        sidebar.add(logoPanel, BorderLayout.NORTH);

        // 7 Nav Items tailored for Students
        String[] navItems = {
            "Dashboard", "My Profile", "Exams & Results", "Attendance", "Fees", "Library", "Events"
        };
        String[] iconBases = {
            "dashboard", "students", "exams", "academic", "fees", "library", "events"
        };

        JPanel navPanel = new JPanel(new GridLayout(navItems.length, 1, 0, 2));
        navPanel.setBackground(new Color(44, 62, 80));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        navButtons = new JToggleButton[navItems.length];
        navButtonGroup = new ButtonGroup();

        for (int i = 0; i < navItems.length; i++) {
            navButtons[i] = createNavButton(navItems[i], iconBases[i]);
            final int idx = i;
            navButtons[i].addActionListener(e -> handleNavigation(idx));
            navButtonGroup.add(navButtons[i]);
            navPanel.add(navButtons[i]);
        }

        // Set Dashboard selected initially
        if (navButtons.length > 0) {
            navButtons[0].setSelected(true);
        }

        sidebar.add(navPanel, BorderLayout.CENTER);

        // Bottom Logout Button
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoutPanel.setBackground(new Color(44, 62, 80));
        logoutPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(231, 76, 60));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLogout.setFocusPainted(false);
        btnLogout.setPreferredSize(new Dimension(160, 36));
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> handleLogout());
        logoutPanel.add(btnLogout);

        sidebar.add(logoutPanel, BorderLayout.SOUTH);

        return sidebar;
    }

    private JToggleButton createNavButton(String text, String iconBase) {
        JToggleButton button = new JToggleButton(text);
        button.setBackground(new Color(44, 62, 80));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setPreferredSize(new Dimension(180, 36));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                if (!button.isSelected()) {
                    button.setBackground(new Color(52, 73, 94));
                    button.setForeground(new Color(165, 216, 221));
                }
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                if (!button.isSelected()) {
                    button.setBackground(new Color(44, 62, 80));
                    button.setForeground(Color.WHITE);
                }
            }
        });

        button.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                button.setBackground(new Color(39, 174, 96));
                button.setForeground(Color.WHITE);
            } else {
                button.setBackground(new Color(44, 62, 80));
                button.setForeground(Color.WHITE);
            }
        });

        try {
            button.setIcon(new ImageIcon(getClass().getResource("/lk/educore/schoolms/resources/" + iconBase + "-light.png")));
            button.setRolloverIcon(new ImageIcon(getClass().getResource("/lk/educore/schoolms/resources/" + iconBase + "-colored.png")));
            button.setSelectedIcon(new ImageIcon(getClass().getResource("/lk/educore/schoolms/resources/" + iconBase + "-colored.png")));
            button.setIconTextGap(10);
        } catch (Exception e) {
            // Icon optional fallback
        }

        return button;
    }

    // =====================================================================
    // HEADER — EXACT MATCH TO ADMIN / TEACHER
    // =====================================================================

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(236, 240, 241));
        header.setPreferredSize(new Dimension(0, 60));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Section Title
        lblHeaderTitle = new JLabel("Dashboard");
        lblHeaderTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblHeaderTitle.setForeground(new Color(44, 62, 80));
        header.add(lblHeaderTitle, BorderLayout.WEST);

        // User info panel
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        userPanel.setBackground(new Color(236, 240, 241));

        JLabel profileIcon = new JLabel();
        profileIcon.setIcon(SendLogo.getProfileIcon());
        userPanel.add(profileIcon);

        JLabel lblUsername = new JLabel(username != null ? username : "Student");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        userPanel.add(lblUsername);

        JLabel lblRole = new JLabel("(Student)");
        lblRole.setFont(new Font("Segoe UI", Font.ITALIC, 18));
        lblRole.setForeground(Color.GRAY);
        userPanel.add(lblRole);

        header.add(userPanel, BorderLayout.EAST);
        return header;
    }

    // =====================================================================
    // MENU BAR
    // =====================================================================

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JMenuItem logOutMenu = new JMenuItem("Log Out");
        logOutMenu.addActionListener(e -> handleLogout());
        fileMenu.add(logOutMenu);

        JMenuItem exitMenu = new JMenuItem("Exit");
        exitMenu.addActionListener(e -> System.exit(0));
        fileMenu.add(exitMenu);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JMenuItem aboutMenu = new JMenuItem("About EduCore");
        aboutMenu.addActionListener(e -> {
            try {
                new AboutDialog(this).setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "EduCore School Management System v2.0", "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        helpMenu.add(aboutMenu);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        return menuBar;
    }

    // =====================================================================
    // NAVIGATION HANDLER
    // =====================================================================

    private void handleNavigation(int index) {
        currentNavIndex = index;
        contentPanel.removeAll();

        switch (index) {
            case 0:
                lblHeaderTitle.setText("Dashboard");
                contentPanel.add(buildDashboardContent(), BorderLayout.CENTER);
                loadDashboardData();
                break;
            case 1:
                lblHeaderTitle.setText("My Profile");
                contentPanel.add(buildProfileContent(), BorderLayout.CENTER);
                loadProfileData();
                break;
            case 2:
                lblHeaderTitle.setText("Exams & Results");
                contentPanel.add(buildResultsContent(), BorderLayout.CENTER);
                loadResults();
                loadUpcomingExams();
                break;
            case 3:
                lblHeaderTitle.setText("Attendance");
                contentPanel.add(buildAttendanceContent(), BorderLayout.CENTER);
                loadAttendanceHistory();
                break;
            case 4:
                lblHeaderTitle.setText("Fees & Payments");
                contentPanel.add(buildFeesContent(), BorderLayout.CENTER);
                loadFeeData();
                break;
            case 5:
                lblHeaderTitle.setText("Library Portal");
                contentPanel.add(buildLibraryContent(), BorderLayout.CENTER);
                loadLibraryData();
                loadCatalogData();
                break;
            case 6:
                lblHeaderTitle.setText("Events & Notices");
                contentPanel.add(buildEventsContent(), BorderLayout.CENTER);
                loadEventsData();
                break;
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void handleLogout() {
        int confirm = JOptionIcon.showConfirmDialog(this, "Are you sure you want to logout?");
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new SignIn().setVisible(true);
        }
    }

    // =====================================================================
    // TAB 0: DASHBOARD CONTENT — GRADIENT CARDS + PIE CHARTS + NOTICES
    // =====================================================================

    private JPanel buildDashboardContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Header Strip
        JPanel header = createHeaderStrip("Dashboard Overview",
            (studentName != null && !studentName.isEmpty())
                ? "Welcome back, " + studentName + (studentGrade.isEmpty() ? "" : "  |  " + studentGrade)
                : "Welcome to EduCore Student Portal"
        );
        panel.add(header, BorderLayout.NORTH);

        // Scrollable content body
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // 1) Stat Cards (1x4)
        content.add(buildStatCards());
        content.add(Box.createRigidArea(new Dimension(0, 20)));

        // 2) Charts Row
        content.add(buildChartsRow());
        content.add(Box.createRigidArea(new Dimension(0, 20)));

        // 3) Notices & Quick Events
        content.add(buildNoticesSection());

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel buildStatCards() {
        JPanel cards = new JPanel(new GridLayout(1, 4, 15, 0));
        cards.setOpaque(false);
        cards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 155));
        cards.setPreferredSize(new Dimension(0, 155));

        // Card 1: Attendance Rate
        lblAttendanceRate = new JLabel("0%");
        cards.add(createStatCard(lblAttendanceRate, "Attendance",
                Color.decode("#183d8a"), Color.decode("#719bf0"),
                "/lk/educore/schoolms/resources/other/attendance.png"));

        // Card 2: Pending Fees
        lblPendingFees = new JLabel("Rs.0");
        cards.add(createStatCard(lblPendingFees, "Pending Fees",
                Color.decode("#931835"), Color.decode("#F18352"),
                "/lk/educore/schoolms/resources/other/fee.png"));

        // Card 3: Upcoming Exams
        lblUpcomingExams = new JLabel("0");
        cards.add(createStatCard(lblUpcomingExams, "Upcoming Exams",
                Color.decode("#C55404"), Color.decode("#F8D32A"),
                "/lk/educore/schoolms/resources/other/exam.png"));

        // Card 4: Books Borrowed
        lblBooksBorrowed = new JLabel("0");
        cards.add(createStatCard(lblBooksBorrowed, "Books Borrowed",
                Color.decode("#0D5917"), Color.decode("#51F815"),
                "/lk/educore/schoolms/resources/other/student.png"));

        return cards;
    }

    private RoundPanel createStatCard(JLabel valueLabel, String subtitle,
                                      Color startColor, Color endColor, String iconPath) {
        RoundPanel card = new RoundPanel();
        card.setGradientColors(startColor, endColor);
        card.setLayout(new BorderLayout(10, 0));
        card.setBorder(BorderFactory.createEmptyBorder(22, 24, 22, 20));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel(subtitle);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        sub.setForeground(new Color(255, 255, 255, 225));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(valueLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        textPanel.add(sub);
        card.add(textPanel, BorderLayout.CENTER);

        // Icon
        try {
            InputStream stream = getClass().getResourceAsStream(iconPath);
            if (stream != null) {
                ImageIcon raw = new ImageIcon(ImageIO.read(stream));
                Image scaled = raw.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                JLabel icon = new JLabel(new ImageIcon(scaled));
                icon.setVerticalAlignment(SwingConstants.CENTER);
                card.add(icon, BorderLayout.EAST);
                stream.close();
            }
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Stat card icon load failed: " + iconPath, e);
        }

        return card;
    }

    private JPanel buildChartsRow() {
        JPanel row = new JPanel(new GridLayout(1, 2, 20, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 320));
        row.setPreferredSize(new Dimension(0, 320));

        chartHolder1 = new JPanel(new BorderLayout());
        chartHolder1.setOpaque(false);

        chartHolder2 = new JPanel(new BorderLayout());
        chartHolder2.setOpaque(false);

        row.add(wrapChartCard("My Attendance (Last 30 Days)", chartHolder1));
        row.add(wrapChartCard("Fee Payment Status", chartHolder2));

        return row;
    }

    private JPanel wrapChartCard(String title, JPanel holder) {
        RoundPanel card = new RoundPanel(16);
        card.setBackgroundColor(Color.WHITE);
        card.setBorderColor(new Color(230, 233, 238));
        card.setBorderEnabled(true);
        card.setLayout(new BorderLayout(0, 10));
        card.setBorder(BorderFactory.createEmptyBorder(15, 18, 15, 18));

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setForeground(new Color(44, 62, 80));

        card.add(lbl, BorderLayout.NORTH);
        card.add(holder, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildNoticesSection() {
        RoundPanel card = new RoundPanel(16);
        card.setBackgroundColor(Color.WHITE);
        card.setBorderColor(new Color(230, 233, 238));
        card.setBorderEnabled(true);
        card.setLayout(new BorderLayout(0, 10));
        card.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel lbl = new JLabel("Upcoming Events & Announcements");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setForeground(new Color(44, 62, 80));
        top.add(lbl, BorderLayout.WEST);

        JButton btnViewAll = new JButton("View All Events →");
        btnViewAll.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnViewAll.setForeground(new Color(37, 99, 235));
        btnViewAll.setBorderPainted(false);
        btnViewAll.setContentAreaFilled(false);
        btnViewAll.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnViewAll.addActionListener(e -> {
            if (navButtons.length > 6) {
                navButtons[6].setSelected(true);
                handleNavigation(6);
            }
        });
        top.add(btnViewAll, BorderLayout.EAST);

        card.add(top, BorderLayout.NORTH);

        noticesListPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        noticesListPanel.setOpaque(false);
        card.add(noticesListPanel, BorderLayout.CENTER);

        return card;
    }

    // =====================================================================
    // TAB 1: MY PROFILE — COMPLETE MODERN REDESIGN
    // =====================================================================

    private JPanel buildProfileContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        panel.add(createHeaderStrip("My Profile", "Student Identity, Academic and Guardian Information"), BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(15, 25, 20, 25));

        // 1) Identity Banner Card
        RoundPanel banner = new RoundPanel(16);
        banner.setBackgroundColor(new Color(248, 250, 252));
        banner.setBorderColor(new Color(226, 232, 240));
        banner.setBorderEnabled(true);
        banner.setLayout(new BorderLayout(20, 0));
        banner.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        // Left Avatar + Name
        JPanel leftBanner = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftBanner.setOpaque(false);

        JLabel avatar = new JLabel();
        avatar.setIcon(SendLogo.getProfileIcon());
        leftBanner.add(avatar);

        JPanel titles = new JPanel();
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        titles.setOpaque(false);

        lblProfileNameBadge = new JLabel(studentName.isEmpty() ? "Student Name" : studentName);
        lblProfileNameBadge.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblProfileNameBadge.setForeground(new Color(15, 23, 42));

        JPanel badgeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
        badgeRow.setOpaque(false);

        lblProfileIdBadge = createPill(studentId != null ? studentId : "ID: --", new Color(224, 242, 254), new Color(3, 105, 161));
        lblProfileGradeBadge = createPill(studentGrade.isEmpty() ? "Grade: --" : studentGrade, new Color(243, 232, 255), new Color(126, 34, 206));
        lblProfileStatusBadge = createPill("Active Student", new Color(220, 252, 231), new Color(21, 128, 61));

        badgeRow.add(lblProfileIdBadge);
        badgeRow.add(lblProfileGradeBadge);
        badgeRow.add(lblProfileStatusBadge);

        titles.add(lblProfileNameBadge);
        titles.add(Box.createRigidArea(new Dimension(0, 4)));
        titles.add(badgeRow);
        leftBanner.add(titles);

        banner.add(leftBanner, BorderLayout.CENTER);

        // Right button
        JPanel rightBanner = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightBanner.setOpaque(false);

        JButton btnEditAddress = new JButton("Update Address");
        btnEditAddress.setBackground(new Color(37, 99, 235));
        btnEditAddress.setForeground(Color.WHITE);
        btnEditAddress.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEditAddress.setFocusPainted(false);
        btnEditAddress.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditAddress.addActionListener(e -> openAddressEditDialog());
        rightBanner.add(btnEditAddress);

        JButton btnRefreshProfile = new JButton("Refresh");
        btnRefreshProfile.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnRefreshProfile.addActionListener(e -> loadProfileData());
        rightBanner.add(btnRefreshProfile);

        banner.add(rightBanner, BorderLayout.EAST);
        content.add(banner);
        content.add(Box.createRigidArea(new Dimension(0, 20)));

        // 2) Two-column detail cards
        JPanel cardsRow = new JPanel(new GridLayout(1, 2, 20, 0));
        cardsRow.setOpaque(false);
        cardsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 440));

        // --- Card 1: Personal & Academic ---
        RoundPanel card1 = createDetailCard("Personal & Academic Information");
        JPanel grid1 = new JPanel(new GridBagLayout());
        grid1.setOpaque(false);

        lblStudentIdVal = new JLabel("—");
        lblNameVal = new JLabel("—");
        lblGradeVal = new JLabel("—");
        lblDobVal = new JLabel("—");
        lblGenderVal = new JLabel("—");
        lblRegDateVal = new JLabel("—");
        lblStatusVal = new JLabel("—");

        addDetailRow(grid1, 0, "Student ID:", lblStudentIdVal);
        addDetailRow(grid1, 1, "Full Name:", lblNameVal);
        addDetailRow(grid1, 2, "Grade & Section:", lblGradeVal);
        addDetailRow(grid1, 3, "Date of Birth:", lblDobVal);
        addDetailRow(grid1, 4, "Gender:", lblGenderVal);
        addDetailRow(grid1, 5, "Registration Date:", lblRegDateVal);
        addDetailRow(grid1, 6, "Account Status:", lblStatusVal);

        card1.add(grid1, BorderLayout.CENTER);
        cardsRow.add(card1);

        // --- Card 2: Contact & Guardian ---
        RoundPanel card2 = createDetailCard("Contact & Guardian Information");
        JPanel grid2 = new JPanel(new GridBagLayout());
        grid2.setOpaque(false);

        lblEmailVal = new JLabel("—");
        lblPhoneVal = new JLabel("—");
        lblAddressVal = new JLabel("—");
        lblGuardianVal = new JLabel("—");
        lblGuardianRelVal = new JLabel("—");
        lblGuardianPhoneVal = new JLabel("—");
        lblGuardianNicVal = new JLabel("—");
        lblGuardianOccVal = new JLabel("—");

        addDetailRow(grid2, 0, "Student Email:", lblEmailVal);
        addDetailRow(grid2, 1, "Phone Number:", lblPhoneVal);
        addDetailRow(grid2, 2, "Address:", lblAddressVal);
        addDetailRow(grid2, 3, "Guardian Name:", lblGuardianVal);
        addDetailRow(grid2, 4, "Relationship:", lblGuardianRelVal);
        addDetailRow(grid2, 5, "Guardian Phone:", lblGuardianPhoneVal);
        addDetailRow(grid2, 6, "Guardian NIC:", lblGuardianNicVal);
        addDetailRow(grid2, 7, "Occupation:", lblGuardianOccVal);

        card2.add(grid2, BorderLayout.CENTER);
        cardsRow.add(card2);

        content.add(cardsRow);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private RoundPanel createDetailCard(String title) {
        RoundPanel card = new RoundPanel(16);
        card.setBackgroundColor(Color.WHITE);
        card.setBorderColor(new Color(226, 232, 240));
        card.setBorderEnabled(true);
        card.setLayout(new BorderLayout(0, 15));
        card.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLbl.setForeground(new Color(30, 41, 59));
        titleLbl.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(241, 245, 249)));
        card.add(titleLbl, BorderLayout.NORTH);

        return card;
    }

    private void addDetailRow(JPanel panel, int row, String label, JLabel valueLabel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 4, 6, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(100, 116, 139));
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        valueLabel.setForeground(new Color(15, 23, 42));
        panel.add(valueLabel, gbc);
    }

    private void openAddressEditDialog() {
        if (studentId == null) {
            JOptionIcon.showWarningDialog(this, "Student ID not found!");
            return;
        }

        JDialog dialog = new JDialog(this, "Update Residential Address", true);
        dialog.setSize(480, 320);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel info = new JLabel("Enter updated residential address for " + studentName + ":");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        p.add(info, BorderLayout.NORTH);

        JTextArea txt = new JTextArea(lblAddressVal.getText().replace("—", "").trim());
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txt.setLineWrap(true);
        txt.setWrapStyleWord(true);
        p.add(new JScrollPane(txt), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton btnSave = new JButton("Save Changes");
        btnSave.setBackground(new Color(39, 174, 96));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSave.addActionListener(ev -> {
            String newAddr = txt.getText().trim();
            if (newAddr.isEmpty()) {
                JOptionIcon.showWarningDialog(dialog, "Address cannot be empty!");
                return;
            }
            try {
                MySQL.executeIUD("UPDATE student SET address='" + newAddr.replace("'", "''") + "' WHERE student_id='" + studentId + "'");
                JOptionIcon.showSuccessDialog(dialog, "Address updated successfully!");
                lblAddressVal.setText(newAddr);
                dialog.dispose();
            } catch (Exception ex) {
                JOptionIcon.showErrorDialog(dialog, "Failed to update address: " + ex.getMessage());
            }
        });

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(ev -> dialog.dispose());

        buttons.add(btnCancel);
        buttons.add(btnSave);
        p.add(buttons, BorderLayout.SOUTH);

        dialog.add(p);
        dialog.setVisible(true);
    }

    // =====================================================================
    // TAB 2: EXAMS & RESULTS — RESULTS + TIMETABLE
    // =====================================================================

    private JPanel buildResultsContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        panel.add(createHeaderStrip("Exams & Results", "Academic Results, Subject Grades, and Upcoming Exam Schedules"), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Subtab 1: Academic Results
        tabs.addTab("Academic Results", buildResultsSubTab());

        // Subtab 2: Upcoming Exam Timetable
        tabs.addTab("Upcoming Exam Timetable", buildUpcomingExamsSubTab());

        panel.add(tabs, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildResultsSubTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Top Summary Bar
        JPanel summaryBar = new JPanel(new GridLayout(1, 3, 15, 0));
        summaryBar.setOpaque(false);
        summaryBar.setPreferredSize(new Dimension(0, 75));

        lblResultAvg = new JLabel("0.0%");
        summaryBar.add(createMiniCard(lblResultAvg, "Average Marks", new Color(37, 99, 235)));

        lblResultTotal = new JLabel("0");
        summaryBar.add(createMiniCard(lblResultTotal, "Exams Taken", new Color(13, 148, 136)));

        lblResultHighest = new JLabel("—");
        summaryBar.add(createMiniCard(lblResultHighest, "Highest Score", new Color(217, 119, 6)));

        // Filter & Toolbar
        JPanel toolbar = new JPanel(new BorderLayout(10, 5));
        toolbar.setOpaque(false);

        JPanel leftFilters = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        leftFilters.setOpaque(false);

        leftFilters.add(new JLabel("Search Subject:"));
        txtResultSearch = new JTextField(14);
        txtResultSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterResultsTable();
            }
        });
        leftFilters.add(txtResultSearch);

        leftFilters.add(new JLabel("Term:"));
        cmbResultTerm = new JComboBox<>(new String[]{"All Terms", "1st Term", "2nd Term", "3rd Term"});
        cmbResultTerm.addActionListener(e -> filterResultsTable());
        leftFilters.add(cmbResultTerm);

        toolbar.add(leftFilters, BorderLayout.WEST);

        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 5));
        rightActions.setOpaque(false);

        JButton btnViewDetails = new JButton("View Result Details");
        btnViewDetails.setBackground(new Color(37, 99, 235));
        btnViewDetails.setForeground(Color.WHITE);
        btnViewDetails.setFocusPainted(false);
        btnViewDetails.addActionListener(e -> openResultDetailsDialog());
        rightActions.add(btnViewDetails);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadResults());
        rightActions.add(btnRefresh);

        toolbar.add(rightActions, BorderLayout.EAST);

        JPanel topContainer = new JPanel(new BorderLayout(0, 12));
        topContainer.setOpaque(false);
        topContainer.add(summaryBar, BorderLayout.NORTH);
        topContainer.add(toolbar, BorderLayout.SOUTH);
        panel.add(topContainer, BorderLayout.NORTH);

        // Table
        resultsTableModel = new DefaultTableModel(
            new Object[]{"Exam Name", "Subject", "Term / Type", "Marks", "Max Marks", "Grade", "Date", "Remarks"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        resultsTable = createStyledTable(resultsTableModel);

        // Grade Badge Renderer
        resultsTable.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                l.setHorizontalAlignment(SwingConstants.CENTER);
                l.setFont(new Font("Segoe UI", Font.BOLD, 13));
                if (!isSelected) {
                    String g = value != null ? value.toString() : "";
                    if (g.startsWith("A")) {
                        l.setForeground(new Color(22, 101, 52));
                    } else if (g.startsWith("B")) {
                        l.setForeground(new Color(30, 64, 175));
                    } else if (g.startsWith("C") || g.startsWith("S")) {
                        l.setForeground(new Color(180, 83, 9));
                    } else {
                        l.setForeground(new Color(185, 28, 28));
                    }
                }
                return l;
            }
        });

        panel.add(new JScrollPane(resultsTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildUpcomingExamsSubTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JLabel info = new JLabel("Upcoming Examination Timetable for " + (studentGrade.isEmpty() ? "your class" : studentGrade));
        info.setFont(new Font("Segoe UI", Font.BOLD, 14));
        info.setForeground(new Color(44, 62, 80));
        topBar.add(info, BorderLayout.WEST);

        JButton btnRefresh = new JButton("Refresh Timetable");
        btnRefresh.addActionListener(e -> loadUpcomingExams());
        topBar.add(btnRefresh, BorderLayout.EAST);

        panel.add(topBar, BorderLayout.NORTH);

        upcomingExamsTableModel = new DefaultTableModel(
            new Object[]{"Exam Name", "Subject", "Exam Type", "Exam Date", "Duration (Mins)", "Max Marks"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        upcomingExamsTable = createStyledTable(upcomingExamsTableModel);
        panel.add(new JScrollPane(upcomingExamsTable), BorderLayout.CENTER);

        return panel;
    }

    private void openResultDetailsDialog() {
        int sel = resultsTable.getSelectedRow();
        if (sel == -1) {
            JOptionIcon.showWarningDialog(this, "Please select an exam result row to view details!");
            return;
        }

        try {
            int modelRow = resultsTable.convertRowIndexToModel(sel);
            if (modelRow >= 0 && modelRow < resultIdList.size()) {
                int resultId = resultIdList.get(modelRow);
                new ResultDialog(this, resultId).setVisible(true);
            }
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Could not open result details: " + e.getMessage());
        }
    }

    // =====================================================================
    // TAB 3: ATTENDANCE CONTENT — STATS + HISTORY
    // =====================================================================

    private JPanel buildAttendanceContent() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);

        panel.add(createHeaderStrip("My Attendance", "Personal Attendance Records, Timings, and Presence History"), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 12));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // 4 Summary Metric Cards
        JPanel statRow = new JPanel(new GridLayout(1, 4, 15, 0));
        statRow.setOpaque(false);
        statRow.setPreferredSize(new Dimension(0, 75));

        lblAttTotal = new JLabel("0");
        statRow.add(createMiniCard(lblAttTotal, "Total Days Recorded", new Color(71, 85, 105)));

        lblAttPresent = new JLabel("0");
        statRow.add(createMiniCard(lblAttPresent, "Present Days", new Color(39, 174, 96)));

        lblAttAbsent = new JLabel("0");
        statRow.add(createMiniCard(lblAttAbsent, "Absent Days", new Color(231, 76, 60)));

        lblAttLate = new JLabel("0");
        statRow.add(createMiniCard(lblAttLate, "Late Days", new Color(243, 156, 18)));

        // Filter toolbar
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setOpaque(false);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        left.setOpaque(false);
        left.add(new JLabel("Filter by Status:"));
        cmbAttendanceFilter = new JComboBox<>(new String[]{"All", "Present", "Absent", "Late"});
        cmbAttendanceFilter.addActionListener(e -> filterAttendanceTable());
        left.add(cmbAttendanceFilter);
        toolbar.add(left, BorderLayout.WEST);

        JButton btnRefresh = new JButton("Refresh Attendance");
        btnRefresh.addActionListener(e -> loadAttendanceHistory());
        toolbar.add(btnRefresh, BorderLayout.EAST);

        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.setOpaque(false);
        top.add(statRow, BorderLayout.NORTH);
        top.add(toolbar, BorderLayout.SOUTH);

        content.add(top, BorderLayout.NORTH);

        // Table
        attendanceTableModel = new DefaultTableModel(
            new Object[]{"Date", "Status", "Check-In Time", "Check-Out Time", "Remarks"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        attendanceTable = createStyledTable(attendanceTableModel);

        // Color rows by attendance status
        attendanceTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    try {
                        String st = attendanceTable.getValueAt(row, 1).toString();
                        if ("Present".equalsIgnoreCase(st)) {
                            c.setBackground(new Color(240, 253, 244));
                        } else if ("Absent".equalsIgnoreCase(st)) {
                            c.setBackground(new Color(254, 242, 242));
                        } else if ("Late".equalsIgnoreCase(st)) {
                            c.setBackground(new Color(255, 251, 235));
                        } else {
                            c.setBackground(Color.WHITE);
                        }
                    } catch (Exception ex) {
                        c.setBackground(Color.WHITE);
                    }
                }
                return c;
            }
        });

        content.add(new JScrollPane(attendanceTable), BorderLayout.CENTER);
        panel.add(content, BorderLayout.CENTER);

        return panel;
    }

    // =====================================================================
    // TAB 4: FEES & PAYMENTS — CARDS + TABLE + VIEW RECEIPT
    // =====================================================================

    private JPanel buildFeesContent() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);

        panel.add(createHeaderStrip("Fees & Payments", "School Fees, Payment Dues, and Official Receipts"), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 12));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // 3 Summary Cards
        JPanel statRow = new JPanel(new GridLayout(1, 3, 15, 0));
        statRow.setOpaque(false);
        statRow.setPreferredSize(new Dimension(0, 80));

        lblFeeTotal = new JLabel("Rs.0");
        statRow.add(createMiniCard(lblFeeTotal, "Total Fees", new Color(37, 99, 235)));

        lblFeePaid = new JLabel("Rs.0");
        statRow.add(createMiniCard(lblFeePaid, "Total Paid", new Color(22, 163, 74)));

        lblFeePending = new JLabel("Rs.0");
        statRow.add(createMiniCard(lblFeePending, "Pending / Overdue", new Color(220, 38, 38)));

        // Toolbar
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setOpaque(false);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        left.setOpaque(false);
        left.add(new JLabel("Filter by Status:"));
        cmbFeeFilter = new JComboBox<>(new String[]{"All", "Paid", "Pending", "Overdue"});
        cmbFeeFilter.addActionListener(e -> filterFeeTable());
        left.add(cmbFeeFilter);
        toolbar.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        right.setOpaque(false);

        JButton btnViewReceipt = new JButton("View / Print Receipt");
        btnViewReceipt.setBackground(new Color(22, 163, 74));
        btnViewReceipt.setForeground(Color.WHITE);
        btnViewReceipt.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnViewReceipt.setFocusPainted(false);
        btnViewReceipt.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnViewReceipt.addActionListener(e -> openFeeReceiptDialog());
        right.add(btnViewReceipt);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadFeeData());
        right.add(btnRefresh);

        toolbar.add(right, BorderLayout.EAST);

        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.setOpaque(false);
        top.add(statRow, BorderLayout.NORTH);
        top.add(toolbar, BorderLayout.SOUTH);
        content.add(top, BorderLayout.NORTH);

        // Table
        feeTableModel = new DefaultTableModel(
            new Object[]{"Fee Type", "Amount (LKR)", "Due Date", "Paid Date", "Status", "Receipt No"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        feeTable = createStyledTable(feeTableModel);

        // Color rows by fee status
        feeTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    try {
                        String st = feeTable.getValueAt(row, 4).toString();
                        if ("Paid".equalsIgnoreCase(st)) {
                            c.setBackground(new Color(240, 253, 244));
                        } else if ("Overdue".equalsIgnoreCase(st)) {
                            c.setBackground(new Color(254, 242, 242));
                        } else if ("Pending".equalsIgnoreCase(st)) {
                            c.setBackground(new Color(255, 251, 235));
                        } else {
                            c.setBackground(Color.WHITE);
                        }
                    } catch (Exception ex) {
                        c.setBackground(Color.WHITE);
                    }
                }
                return c;
            }
        });

        content.add(new JScrollPane(feeTable), BorderLayout.CENTER);
        panel.add(content, BorderLayout.CENTER);

        return panel;
    }

    private void openFeeReceiptDialog() {
        int sel = feeTable.getSelectedRow();
        if (sel == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a fee record to view its receipt!");
            return;
        }

        try {
            int modelRow = feeTable.convertRowIndexToModel(sel);
            String status = feeTable.getValueAt(sel, 4).toString();
            String receiptNo = feeTable.getValueAt(sel, 5) != null ? feeTable.getValueAt(sel, 5).toString() : "";

            if (!"Paid".equalsIgnoreCase(status) && (receiptNo.isEmpty() || receiptNo.equals("—"))) {
                JOptionIcon.showWarningDialog(this, "Receipt is only available for Paid fees. This fee is currently: " + status);
                return;
            }

            if (modelRow >= 0 && modelRow < feeIdList.size()) {
                int feeId = feeIdList.get(modelRow);
                new FeeReceiptDialog(this, feeId).setVisible(true);
            }
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Could not open receipt dialog: " + e.getMessage());
        }
    }

    // =====================================================================
    // TAB 5: LIBRARY PORTAL — BORROWINGS + SEARCH CATALOG
    // =====================================================================

    private JPanel buildLibraryContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        panel.add(createHeaderStrip("Library Portal", "Borrowed Books, Due Dates, and School Library Catalog"), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Subtab 1: Borrowed Books
        tabs.addTab("My Borrowed Books", buildMyBooksSubTab());

        // Subtab 2: Catalog Search
        tabs.addTab("Search School Library Catalog", buildCatalogSubTab());

        panel.add(tabs, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildMyBooksSubTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Summary Bar
        JPanel summaryBar = new JPanel(new GridLayout(1, 4, 15, 0));
        summaryBar.setOpaque(false);
        summaryBar.setPreferredSize(new Dimension(0, 75));

        lblLibIssued = new JLabel("0");
        summaryBar.add(createMiniCard(lblLibIssued, "Currently Issued", new Color(37, 99, 235)));

        lblLibOverdue = new JLabel("0");
        summaryBar.add(createMiniCard(lblLibOverdue, "Overdue Books", new Color(220, 38, 38)));

        lblLibReturned = new JLabel("0");
        summaryBar.add(createMiniCard(lblLibReturned, "Returned Books", new Color(22, 163, 74)));

        lblLibFines = new JLabel("Rs.0");
        summaryBar.add(createMiniCard(lblLibFines, "Total Fines", new Color(217, 119, 6)));

        // Toolbar
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setOpaque(false);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        left.setOpaque(false);
        left.add(new JLabel("Status:"));
        cmbMyBooksFilter = new JComboBox<>(new String[]{"All", "Issued", "Returned", "Overdue"});
        cmbMyBooksFilter.addActionListener(e -> filterMyBooksTable());
        left.add(cmbMyBooksFilter);
        toolbar.add(left, BorderLayout.WEST);

        JButton btnRefresh = new JButton("Refresh My Books");
        btnRefresh.addActionListener(e -> loadLibraryData());
        toolbar.add(btnRefresh, BorderLayout.EAST);

        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.setOpaque(false);
        top.add(summaryBar, BorderLayout.NORTH);
        top.add(toolbar, BorderLayout.SOUTH);
        panel.add(top, BorderLayout.NORTH);

        // Table
        myBooksTableModel = new DefaultTableModel(
            new Object[]{"Book Title", "Author", "Category", "Issue Date", "Due Date", "Return Date", "Fine (LKR)", "Status"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        myBooksTable = createStyledTable(myBooksTableModel);

        myBooksTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    try {
                        String st = myBooksTable.getValueAt(row, 7).toString();
                        if ("Returned".equalsIgnoreCase(st)) {
                            c.setBackground(new Color(240, 253, 244));
                        } else if ("Overdue".equalsIgnoreCase(st)) {
                            c.setBackground(new Color(254, 242, 242));
                        } else {
                            c.setBackground(new Color(239, 246, 255));
                        }
                    } catch (Exception ex) {
                        c.setBackground(Color.WHITE);
                    }
                }
                return c;
            }
        });

        panel.add(new JScrollPane(myBooksTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildCatalogSubTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Filter / Search Toolbar
        JPanel toolbar = new JPanel(new BorderLayout(10, 5));
        toolbar.setOpaque(false);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        left.setOpaque(false);

        left.add(new JLabel("Search Book / Author:"));
        txtCatalogSearch = new JTextField(16);
        txtCatalogSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                loadCatalogData();
            }
        });
        left.add(txtCatalogSearch);

        left.add(new JLabel("Category:"));
        cmbCatalogCategory = new JComboBox<>();
        cmbCatalogCategory.addItem("All Categories");
        loadCategoryList();
        cmbCatalogCategory.addActionListener(e -> loadCatalogData());
        left.add(cmbCatalogCategory);

        toolbar.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 5));
        right.setOpaque(false);

        JButton btnSearch = new JButton("Search");
        btnSearch.setBackground(new Color(37, 99, 235));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.addActionListener(e -> loadCatalogData());
        right.add(btnSearch);

        JButton btnReset = new JButton("Reset");
        btnReset.addActionListener(e -> {
            txtCatalogSearch.setText("");
            cmbCatalogCategory.setSelectedIndex(0);
            loadCatalogData();
        });
        right.add(btnReset);

        toolbar.add(right, BorderLayout.EAST);
        panel.add(toolbar, BorderLayout.NORTH);

        // Catalog Table
        catalogTableModel = new DefaultTableModel(
            new Object[]{"ISBN", "Title", "Author", "Category", "Available", "Total Copies", "Status"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        catalogTable = createStyledTable(catalogTableModel);

        // Availability renderer
        catalogTable.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                l.setHorizontalAlignment(SwingConstants.CENTER);
                l.setFont(new Font("Segoe UI", Font.BOLD, 12));
                if (!isSelected) {
                    String st = value != null ? value.toString() : "";
                    if ("Available".equalsIgnoreCase(st)) {
                        l.setForeground(new Color(22, 163, 74));
                    } else {
                        l.setForeground(new Color(220, 38, 38));
                    }
                }
                return l;
            }
        });

        panel.add(new JScrollPane(catalogTable), BorderLayout.CENTER);
        return panel;
    }

    private void loadCategoryList() {
        try {
            ResultSet rs = MySQL.executeSearch("SELECT category_name FROM book_category ORDER BY category_name");
            while (rs.next()) {
                cmbCatalogCategory.addItem(rs.getString("category_name"));
            }
            rs.close();
        } catch (Exception e) {
            // Ignore if categories fail
        }
    }

    // =====================================================================
    // TAB 6: EVENTS & NOTICES
    // =====================================================================

    private JPanel buildEventsContent() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);

        panel.add(createHeaderStrip("School Events & Notices", "Official Notices, School Events, Holidays, and Assemblies"), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 12));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Toolbar
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setOpaque(false);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        left.setOpaque(false);

        left.add(new JLabel("Search Event:"));
        txtEventSearch = new JTextField(16);
        txtEventSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterEventsTable();
            }
        });
        left.add(txtEventSearch);

        left.add(new JLabel("Status:"));
        cmbEventStatus = new JComboBox<>(new String[]{"All", "Active", "Completed", "Postponed", "Cancelled"});
        cmbEventStatus.addActionListener(e -> filterEventsTable());
        left.add(cmbEventStatus);

        toolbar.add(left, BorderLayout.WEST);

        JButton btnRefresh = new JButton("Refresh Events");
        btnRefresh.addActionListener(e -> loadEventsData());
        toolbar.add(btnRefresh, BorderLayout.EAST);

        content.add(toolbar, BorderLayout.NORTH);

        // Table
        eventsTableModel = new DefaultTableModel(
            new Object[]{"Event Name", "Description", "Location", "Start Date & Time", "End Date & Time", "Status"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        eventsTable = createStyledTable(eventsTableModel);

        // Status renderer
        eventsTable.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                l.setHorizontalAlignment(SwingConstants.CENTER);
                l.setFont(new Font("Segoe UI", Font.BOLD, 12));
                if (!isSelected) {
                    String st = value != null ? value.toString() : "";
                    if ("Active".equalsIgnoreCase(st)) {
                        l.setForeground(new Color(22, 163, 74));
                    } else if ("Completed".equalsIgnoreCase(st)) {
                        l.setForeground(new Color(100, 116, 139));
                    } else if ("Postponed".equalsIgnoreCase(st)) {
                        l.setForeground(new Color(217, 119, 6));
                    } else {
                        l.setForeground(new Color(220, 38, 38));
                    }
                }
                return l;
            }
        });

        content.add(new JScrollPane(eventsTable), BorderLayout.CENTER);
        panel.add(content, BorderLayout.CENTER);

        return panel;
    }

    // =====================================================================
    // DATA LOADING METHODS
    // =====================================================================

    private void loadDashboardData() {
        if (studentId == null) return;

        try {
            // 1. Attendance Rate (30 days)
            ResultSet rs1 = MySQL.executeSearch(
                "SELECT ROUND(" +
                "(SELECT COUNT(*) FROM student_attendance WHERE student_id='" + studentId + "' AND status='Present' AND date_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)) * 100.0 / " +
                "NULLIF((SELECT COUNT(*) FROM student_attendance WHERE student_id='" + studentId + "' AND date_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)), 0), 1)"
            );
            if (rs1.next()) {
                String rate = rs1.getString(1);
                lblAttendanceRate.setText(rate != null ? rate + "%" : "N/A");
            }
            rs1.close();

            // 2. Pending Fees
            ResultSet rs2 = MySQL.executeSearch(
                "SELECT COALESCE(SUM(amount), 0) FROM fee WHERE student_id='" + studentId + "' AND status != 'Paid' AND status != 'Cancelled'"
            );
            if (rs2.next()) {
                double pend = rs2.getDouble(1);
                lblPendingFees.setText("Rs." + formatNumber(pend));
            }
            rs2.close();

            // 3. Upcoming Exams
            ResultSet rs3 = MySQL.executeSearch(
                "SELECT COUNT(*) FROM exam e " +
                "JOIN student s ON s.grade_id = e.grade_id " +
                "WHERE s.student_id='" + studentId + "' AND e.exam_date >= CURDATE()"
            );
            if (rs3.next()) {
                lblUpcomingExams.setText(String.valueOf(rs3.getInt(1)));
            }
            rs3.close();

            // 4. Books Borrowed
            ResultSet rs4 = MySQL.executeSearch(
                "SELECT COUNT(*) FROM book_issue WHERE student_id='" + studentId + "' AND status='Issued'"
            );
            if (rs4.next()) {
                lblBooksBorrowed.setText(String.valueOf(rs4.getInt(1)));
            }
            rs4.close();

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Dashboard stats load failed", e);
        }

        loadAttendanceChart();
        loadFeeStatusChart();
        loadNoticesPreview();
    }

    private void loadAttendanceChart() {
        if (chartHolder1 == null) return;
        chartHolder1.removeAll();

        PieChart chart = new PieChart();
        chart.setChartType(PieChart.PeiChartType.DONUT_CHART);

        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.Y_AXIS));
        legendPanel.setOpaque(false);
        legendPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

        boolean hasData = false;
        try {
            ResultSet rs = MySQL.executeSearch(
                "SELECT status, COUNT(*) AS count FROM student_attendance " +
                "WHERE student_id='" + studentId + "' AND date_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) " +
                "GROUP BY status"
            );

            while (rs.next()) {
                String status = rs.getString("status");
                int count = rs.getInt("count");
                if (count > 0) {
                    Color color;
                    switch (status) {
                        case "Present": color = new Color(76, 175, 80); break;
                        case "Absent":  color = new Color(244, 67, 54); break;
                        case "Late":    color = new Color(255, 152, 0); break;
                        default:        color = new Color(158, 158, 158); break;
                    }
                    chart.addData(new ModelPieChart(status, count, color));
                    legendPanel.add(createLegendItem(status, count, color));
                    hasData = true;
                }
            }
            rs.close();
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Attendance chart failed", e);
        }

        if (!hasData) {
            chart.addData(new ModelPieChart("Present", 1, new Color(76, 175, 80)));
            legendPanel.add(createLegendItem("No Recent Records", 0, new Color(158, 158, 158)));
        }

        chartHolder1.add(chart, BorderLayout.CENTER);
        chartHolder1.add(legendPanel, BorderLayout.EAST);
        chartHolder1.revalidate();
        chartHolder1.repaint();
    }

    private void loadFeeStatusChart() {
        if (chartHolder2 == null) return;
        chartHolder2.removeAll();

        PieChart chart = new PieChart();
        chart.setChartType(PieChart.PeiChartType.DONUT_CHART);

        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.Y_AXIS));
        legendPanel.setOpaque(false);
        legendPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

        boolean hasData = false;
        try {
            ResultSet rs = MySQL.executeSearch(
                "SELECT status, COUNT(*) AS count FROM fee WHERE student_id='" + studentId + "' GROUP BY status"
            );

            while (rs.next()) {
                String status = rs.getString("status");
                int count = rs.getInt("count");
                if (count > 0) {
                    Color color;
                    switch (status) {
                        case "Paid":      color = new Color(76, 175, 80);  break;
                        case "Pending":   color = new Color(33, 150, 243); break;
                        case "Overdue":   color = new Color(244, 67, 54);  break;
                        default:          color = new Color(158, 158, 158); break;
                    }
                    chart.addData(new ModelPieChart(status, count, color));
                    legendPanel.add(createLegendItem(status, count, color));
                    hasData = true;
                }
            }
            rs.close();
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Fee chart failed", e);
        }

        if (!hasData) {
            chart.addData(new ModelPieChart("All Clear", 1, new Color(76, 175, 80)));
            legendPanel.add(createLegendItem("No Fees Due", 0, new Color(76, 175, 80)));
        }

        chartHolder2.add(chart, BorderLayout.CENTER);
        chartHolder2.add(legendPanel, BorderLayout.EAST);
        chartHolder2.revalidate();
        chartHolder2.repaint();
    }

    private void loadNoticesPreview() {
        if (noticesListPanel == null) return;
        noticesListPanel.removeAll();

        try {
            ResultSet rs = MySQL.executeSearch(
                "SELECT event_name, location, start_date, description " +
                "FROM event " +
                "WHERE start_date >= CURDATE() " +
                "ORDER BY start_date ASC LIMIT 2"
            );

            int count = 0;
            while (rs.next()) {
                count++;
                String title = rs.getString("event_name");
                String date = rs.getString("start_date");
                String loc = rs.getString("location");
                String desc = rs.getString("description");

                JPanel box = new JPanel(new BorderLayout(5, 5));
                box.setBackground(new Color(248, 250, 252));
                box.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
                    BorderFactory.createEmptyBorder(10, 14, 10, 14)
                ));

                JLabel lblTitle = new JLabel(title);
                lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
                lblTitle.setForeground(new Color(30, 41, 59));

                JLabel lblSub = new JLabel("📅 " + date + "  |  📍 " + (loc != null ? loc : "School Premises"));
                lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                lblSub.setForeground(new Color(100, 116, 139));

                JLabel lblDesc = new JLabel(desc != null && desc.length() > 60 ? desc.substring(0, 60) + "..." : (desc != null ? desc : ""));
                lblDesc.setFont(new Font("Segoe UI", Font.ITALIC, 11));
                lblDesc.setForeground(new Color(71, 85, 105));

                box.add(lblTitle, BorderLayout.NORTH);
                box.add(lblSub, BorderLayout.CENTER);
                box.add(lblDesc, BorderLayout.SOUTH);

                noticesListPanel.add(box);
            }
            rs.close();

            if (count == 0) {
                JLabel empty = new JLabel("No upcoming notices scheduled at this moment.", SwingConstants.CENTER);
                empty.setFont(new Font("Segoe UI", Font.ITALIC, 12));
                empty.setForeground(Color.GRAY);
                noticesListPanel.add(empty);
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Notices preview failed", e);
        }

        noticesListPanel.revalidate();
        noticesListPanel.repaint();
    }

    private void loadProfileData() {
        if (studentId == null) return;
        try {
            ResultSet rs = MySQL.executeSearch(
                "SELECT s.student_id, s.first_name, s.last_name, s.dob, s.gender, s.email, s.phone, s.address, " +
                "s.registration_date, s.is_active, CONCAT(g.grade_name, ' - ', g.section) AS grade_name, " +
                "gu.first_name AS g_first, gu.last_name AS g_last, gu.relationship, gu.phone AS g_phone, " +
                "gu.nic AS g_nic, gu.occupation " +
                "FROM student s " +
                "JOIN grade g ON s.grade_id = g.grade_id " +
                "LEFT JOIN guardian gu ON s.guardian_id = gu.guardian_id " +
                "WHERE s.student_id = '" + studentId + "'"
            );

            if (rs.next()) {
                String fn = rs.getString("first_name") + " " + rs.getString("last_name");
                studentName = fn;
                studentGrade = rs.getString("grade_name");

                lblProfileNameBadge.setText(fn);
                lblProfileIdBadge.setText("ID: " + rs.getString("student_id"));
                lblProfileGradeBadge.setText(studentGrade);

                lblStudentIdVal.setText(rs.getString("student_id"));
                lblNameVal.setText(fn);
                lblGradeVal.setText(studentGrade);
                lblDobVal.setText(rs.getString("dob"));
                lblGenderVal.setText(rs.getString("gender"));
                lblRegDateVal.setText(rs.getString("registration_date"));
                lblStatusVal.setText(rs.getInt("is_active") == 1 ? "Active Student" : "Inactive");

                lblEmailVal.setText(rs.getString("email") != null ? rs.getString("email") : "—");
                lblPhoneVal.setText(rs.getString("phone") != null ? rs.getString("phone") : "—");
                lblAddressVal.setText(rs.getString("address") != null ? rs.getString("address") : "—");

                String gName = (rs.getString("g_first") != null ? rs.getString("g_first") + " " + rs.getString("g_last") : "—");
                lblGuardianVal.setText(gName);
                lblGuardianRelVal.setText(rs.getString("relationship") != null ? rs.getString("relationship") : "—");
                lblGuardianPhoneVal.setText(rs.getString("g_phone") != null ? rs.getString("g_phone") : "—");
                lblGuardianNicVal.setText(rs.getString("g_nic") != null ? rs.getString("g_nic") : "—");
                lblGuardianOccVal.setText(rs.getString("occupation") != null ? rs.getString("occupation") : "—");
            }
            rs.close();
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Profile load failed", e);
        }
    }

    private void loadResults() {
        if (studentId == null || resultsTableModel == null) return;
        try {
            resultsTableModel.setRowCount(0);
            resultIdList.clear();

            ResultSet rs = MySQL.executeSearch(
                "SELECT r.result_id, e.exam_name, sub.subject_name, et.exam_type_name, " +
                "r.marks_obtained, e.max_marks, r.grade_letter, e.exam_date, r.remarks " +
                "FROM result r " +
                "JOIN exam e ON r.exam_id = e.exam_id " +
                "JOIN subject sub ON e.subject_id = sub.subject_id " +
                "JOIN exam_type et ON e.exam_type_id = et.exam_type_id " +
                "WHERE r.student_id = '" + studentId + "' " +
                "ORDER BY e.exam_date DESC"
            );

            double sum = 0;
            int count = 0;
            double highest = 0;

            while (rs.next()) {
                resultIdList.add(rs.getInt("result_id"));
                double marks = rs.getDouble("marks_obtained");
                sum += marks;
                count++;
                if (marks > highest) highest = marks;

                resultsTableModel.addRow(new Object[]{
                    rs.getString("exam_name"),
                    rs.getString("subject_name"),
                    rs.getString("exam_type_name"),
                    marks,
                    rs.getDouble("max_marks"),
                    rs.getString("grade_letter"),
                    rs.getString("exam_date"),
                    rs.getString("remarks") != null ? rs.getString("remarks") : ""
                });
            }
            rs.close();

            if (lblResultAvg != null) {
                lblResultAvg.setText(count > 0 ? String.format("%.1f%%", sum / count) : "0.0%");
                lblResultTotal.setText(String.valueOf(count));
                lblResultHighest.setText(count > 0 ? String.format("%.1f", highest) : "—");
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Results load failed", e);
        }
    }

    private void filterResultsTable() {
        if (studentId == null || resultsTableModel == null) return;
        String query = txtResultSearch != null ? txtResultSearch.getText().trim().toLowerCase() : "";
        String term = cmbResultTerm != null ? cmbResultTerm.getSelectedItem().toString() : "All Terms";

        try {
            resultsTableModel.setRowCount(0);
            resultIdList.clear();

            StringBuilder sql = new StringBuilder(
                "SELECT r.result_id, e.exam_name, sub.subject_name, et.exam_type_name, " +
                "r.marks_obtained, e.max_marks, r.grade_letter, e.exam_date, r.remarks " +
                "FROM result r " +
                "JOIN exam e ON r.exam_id = e.exam_id " +
                "JOIN subject sub ON e.subject_id = sub.subject_id " +
                "JOIN exam_type et ON e.exam_type_id = et.exam_type_id " +
                "WHERE r.student_id = '" + studentId + "' "
            );

            if (!"All Terms".equals(term)) {
                sql.append("AND et.exam_type_name = '").append(term).append("' ");
            }
            if (!query.isEmpty()) {
                sql.append("AND (LOWER(sub.subject_name) LIKE '%").append(query.replace("'", "''")).append("%' ")
                   .append("OR LOWER(e.exam_name) LIKE '%").append(query.replace("'", "''")).append("%') ");
            }

            sql.append("ORDER BY e.exam_date DESC");

            ResultSet rs = MySQL.executeSearch(sql.toString());
            while (rs.next()) {
                resultIdList.add(rs.getInt("result_id"));
                resultsTableModel.addRow(new Object[]{
                    rs.getString("exam_name"),
                    rs.getString("subject_name"),
                    rs.getString("exam_type_name"),
                    rs.getDouble("marks_obtained"),
                    rs.getDouble("max_marks"),
                    rs.getString("grade_letter"),
                    rs.getString("exam_date"),
                    rs.getString("remarks") != null ? rs.getString("remarks") : ""
                });
            }
            rs.close();

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Filter results failed", e);
        }
    }

    private void loadUpcomingExams() {
        if (studentId == null || upcomingExamsTableModel == null) return;
        try {
            upcomingExamsTableModel.setRowCount(0);
            ResultSet rs = MySQL.executeSearch(
                "SELECT e.exam_name, sub.subject_name, et.exam_type_name, e.exam_date, e.duration_minutes, e.max_marks " +
                "FROM exam e " +
                "JOIN subject sub ON e.subject_id = sub.subject_id " +
                "JOIN exam_type et ON e.exam_type_id = et.exam_type_id " +
                "JOIN student s ON s.grade_id = e.grade_id " +
                "WHERE s.student_id = '" + studentId + "' AND e.exam_date >= CURDATE() " +
                "ORDER BY e.exam_date ASC"
            );

            while (rs.next()) {
                upcomingExamsTableModel.addRow(new Object[]{
                    rs.getString("exam_name"),
                    rs.getString("subject_name"),
                    rs.getString("exam_type_name"),
                    rs.getString("exam_date"),
                    rs.getInt("duration_minutes") + " mins",
                    rs.getDouble("max_marks")
                });
            }
            rs.close();

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Upcoming exams load failed", e);
        }
    }

    private void loadAttendanceHistory() {
        if (studentId == null || attendanceTableModel == null) return;
        try {
            attendanceTableModel.setRowCount(0);
            ResultSet rs = MySQL.executeSearch(
                "SELECT attendance_date, status, check_in_time, check_out_time, remarks " +
                "FROM student_attendance " +
                "WHERE student_id = '" + studentId + "' " +
                "ORDER BY attendance_date DESC"
            );

            int total = 0, present = 0, absent = 0, late = 0;

            while (rs.next()) {
                total++;
                String st = rs.getString("status");
                if ("Present".equalsIgnoreCase(st)) present++;
                else if ("Absent".equalsIgnoreCase(st)) absent++;
                else if ("Late".equalsIgnoreCase(st)) late++;

                attendanceTableModel.addRow(new Object[]{
                    rs.getString("attendance_date"),
                    st,
                    rs.getString("check_in_time") != null ? rs.getString("check_in_time") : "—",
                    rs.getString("check_out_time") != null ? rs.getString("check_out_time") : "—",
                    rs.getString("remarks") != null ? rs.getString("remarks") : ""
                });
            }
            rs.close();

            if (lblAttTotal != null) {
                lblAttTotal.setText(String.valueOf(total));
                lblAttPresent.setText(String.valueOf(present));
                lblAttAbsent.setText(String.valueOf(absent));
                lblAttLate.setText(String.valueOf(late));
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Attendance load failed", e);
        }
    }

    private void filterAttendanceTable() {
        if (studentId == null || attendanceTableModel == null) return;
        String filter = cmbAttendanceFilter != null ? cmbAttendanceFilter.getSelectedItem().toString() : "All";
        try {
            attendanceTableModel.setRowCount(0);
            String sql = "SELECT attendance_date, status, check_in_time, check_out_time, remarks " +
                         "FROM student_attendance WHERE student_id = '" + studentId + "' ";
            if (!"All".equals(filter)) {
                sql += "AND status = '" + filter + "' ";
            }
            sql += "ORDER BY attendance_date DESC";

            ResultSet rs = MySQL.executeSearch(sql);
            while (rs.next()) {
                attendanceTableModel.addRow(new Object[]{
                    rs.getString("attendance_date"),
                    rs.getString("status"),
                    rs.getString("check_in_time") != null ? rs.getString("check_in_time") : "—",
                    rs.getString("check_out_time") != null ? rs.getString("check_out_time") : "—",
                    rs.getString("remarks") != null ? rs.getString("remarks") : ""
                });
            }
            rs.close();
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Filter attendance failed", e);
        }
    }

    private void loadFeeData() {
        if (studentId == null || feeTableModel == null) return;
        try {
            feeTableModel.setRowCount(0);
            feeIdList.clear();

            ResultSet rs = MySQL.executeSearch(
                "SELECT f.fee_id, ft.fee_type_name, f.amount, f.due_date, f.paid_date, f.status, f.receipt_no " +
                "FROM fee f " +
                "JOIN fee_type ft ON f.fee_type_id = ft.fee_type_id " +
                "WHERE f.student_id = '" + studentId + "' " +
                "ORDER BY f.due_date DESC"
            );

            double total = 0, paid = 0, pending = 0;

            while (rs.next()) {
                feeIdList.add(rs.getInt("fee_id"));
                double amt = rs.getDouble("amount");
                String status = rs.getString("status");

                total += amt;
                if ("Paid".equalsIgnoreCase(status)) paid += amt;
                else pending += amt;

                feeTableModel.addRow(new Object[]{
                    rs.getString("fee_type_name"),
                    String.format("%,.2f", amt),
                    rs.getString("due_date"),
                    rs.getString("paid_date") != null ? rs.getString("paid_date") : "—",
                    status,
                    rs.getString("receipt_no") != null ? rs.getString("receipt_no") : "—"
                });
            }
            rs.close();

            if (lblFeeTotal != null) {
                lblFeeTotal.setText("Rs." + formatNumber(total));
                lblFeePaid.setText("Rs." + formatNumber(paid));
                lblFeePending.setText("Rs." + formatNumber(pending));
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Fee load failed", e);
        }
    }

    private void filterFeeTable() {
        if (studentId == null || feeTableModel == null) return;
        String filter = cmbFeeFilter != null ? cmbFeeFilter.getSelectedItem().toString() : "All";
        try {
            feeTableModel.setRowCount(0);
            feeIdList.clear();

            String sql = "SELECT f.fee_id, ft.fee_type_name, f.amount, f.due_date, f.paid_date, f.status, f.receipt_no " +
                         "FROM fee f " +
                         "JOIN fee_type ft ON f.fee_type_id = ft.fee_type_id " +
                         "WHERE f.student_id = '" + studentId + "' ";
            if (!"All".equals(filter)) {
                sql += "AND f.status = '" + filter + "' ";
            }
            sql += "ORDER BY f.due_date DESC";

            ResultSet rs = MySQL.executeSearch(sql);
            while (rs.next()) {
                feeIdList.add(rs.getInt("fee_id"));
                feeTableModel.addRow(new Object[]{
                    rs.getString("fee_type_name"),
                    String.format("%,.2f", rs.getDouble("amount")),
                    rs.getString("due_date"),
                    rs.getString("paid_date") != null ? rs.getString("paid_date") : "—",
                    rs.getString("status"),
                    rs.getString("receipt_no") != null ? rs.getString("receipt_no") : "—"
                });
            }
            rs.close();
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Filter fee failed", e);
        }
    }

    private void loadLibraryData() {
        if (studentId == null || myBooksTableModel == null) return;
        try {
            myBooksTableModel.setRowCount(0);

            ResultSet rs = MySQL.executeSearch(
                "SELECT b.title, b.author, c.category_name, bi.issue_date, bi.due_date, bi.return_date, bi.fine, bi.status " +
                "FROM book_issue bi " +
                "JOIN book b ON bi.book_id = b.book_id " +
                "LEFT JOIN book_category c ON b.category_id = c.category_id " +
                "WHERE bi.student_id = '" + studentId + "' " +
                "ORDER BY bi.issue_date DESC"
            );

            int issued = 0, overdue = 0, returned = 0;
            double fines = 0;

            while (rs.next()) {
                String st = rs.getString("status");
                double fine = rs.getDouble("fine");
                fines += fine;

                if ("Issued".equalsIgnoreCase(st)) issued++;
                else if ("Overdue".equalsIgnoreCase(st)) overdue++;
                else if ("Returned".equalsIgnoreCase(st)) returned++;

                myBooksTableModel.addRow(new Object[]{
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("category_name") != null ? rs.getString("category_name") : "General",
                    rs.getString("issue_date"),
                    rs.getString("due_date"),
                    rs.getString("return_date") != null ? rs.getString("return_date") : "—",
                    String.format("%,.2f", fine),
                    st
                });
            }
            rs.close();

            if (lblLibIssued != null) {
                lblLibIssued.setText(String.valueOf(issued));
                lblLibOverdue.setText(String.valueOf(overdue));
                lblLibReturned.setText(String.valueOf(returned));
                lblLibFines.setText("Rs." + formatNumber(fines));
            }

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Library load failed", e);
        }
    }

    private void filterMyBooksTable() {
        if (studentId == null || myBooksTableModel == null) return;
        String filter = cmbMyBooksFilter != null ? cmbMyBooksFilter.getSelectedItem().toString() : "All";
        try {
            myBooksTableModel.setRowCount(0);
            String sql = "SELECT b.title, b.author, c.category_name, bi.issue_date, bi.due_date, bi.return_date, bi.fine, bi.status " +
                         "FROM book_issue bi " +
                         "JOIN book b ON bi.book_id = b.book_id " +
                         "LEFT JOIN book_category c ON b.category_id = c.category_id " +
                         "WHERE bi.student_id = '" + studentId + "' ";
            if (!"All".equals(filter)) {
                sql += "AND bi.status = '" + filter + "' ";
            }
            sql += "ORDER BY bi.issue_date DESC";

            ResultSet rs = MySQL.executeSearch(sql);
            while (rs.next()) {
                myBooksTableModel.addRow(new Object[]{
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("category_name") != null ? rs.getString("category_name") : "General",
                    rs.getString("issue_date"),
                    rs.getString("due_date"),
                    rs.getString("return_date") != null ? rs.getString("return_date") : "—",
                    String.format("%,.2f", rs.getDouble("fine")),
                    rs.getString("status")
                });
            }
            rs.close();
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Filter my books failed", e);
        }
    }

    private void loadCatalogData() {
        if (catalogTableModel == null) return;
        String search = txtCatalogSearch != null ? txtCatalogSearch.getText().trim().toLowerCase() : "";
        String cat = cmbCatalogCategory != null && cmbCatalogCategory.getSelectedItem() != null
                ? cmbCatalogCategory.getSelectedItem().toString() : "All Categories";

        try {
            catalogTableModel.setRowCount(0);

            StringBuilder sql = new StringBuilder(
                "SELECT b.isbn, b.title, b.author, c.category_name, b.available_qty, b.quantity " +
                "FROM book b " +
                "LEFT JOIN book_category c ON b.category_id = c.category_id WHERE 1=1 "
            );

            if (!"All Categories".equals(cat)) {
                sql.append("AND c.category_name = '").append(cat.replace("'", "''")).append("' ");
            }
            if (!search.isEmpty()) {
                sql.append("AND (LOWER(b.title) LIKE '%").append(search.replace("'", "''")).append("%' ")
                   .append("OR LOWER(b.author) LIKE '%").append(search.replace("'", "''")).append("%' ")
                   .append("OR LOWER(b.isbn) LIKE '%").append(search.replace("'", "''")).append("%') ");
            }

            sql.append("ORDER BY b.title ASC");

            ResultSet rs = MySQL.executeSearch(sql.toString());
            while (rs.next()) {
                int avail = rs.getInt("available_qty");
                int total = rs.getInt("quantity");
                catalogTableModel.addRow(new Object[]{
                    rs.getString("isbn"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("category_name") != null ? rs.getString("category_name") : "General",
                    avail,
                    total,
                    avail > 0 ? "Available" : "Checked Out"
                });
            }
            rs.close();

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Catalog load failed", e);
        }
    }

    private void loadEventsData() {
        if (eventsTableModel == null) return;
        try {
            eventsTableModel.setRowCount(0);
            ResultSet rs = MySQL.executeSearch(
                "SELECT e.event_name, e.description, e.location, e.start_date, e.end_date, es.status_name " +
                "FROM event e " +
                "JOIN event_status es ON e.event_status_id = es.event_status_id " +
                "ORDER BY e.start_date DESC"
            );

            while (rs.next()) {
                eventsTableModel.addRow(new Object[]{
                    rs.getString("event_name"),
                    rs.getString("description") != null ? rs.getString("description") : "",
                    rs.getString("location") != null ? rs.getString("location") : "School",
                    rs.getString("start_date"),
                    rs.getString("end_date") != null ? rs.getString("end_date") : "—",
                    rs.getString("status_name")
                });
            }
            rs.close();

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Events load failed", e);
        }
    }

    private void filterEventsTable() {
        if (eventsTableModel == null) return;
        String query = txtEventSearch != null ? txtEventSearch.getText().trim().toLowerCase() : "";
        String status = cmbEventStatus != null ? cmbEventStatus.getSelectedItem().toString() : "All";

        try {
            eventsTableModel.setRowCount(0);
            StringBuilder sql = new StringBuilder(
                "SELECT e.event_name, e.description, e.location, e.start_date, e.end_date, es.status_name " +
                "FROM event e " +
                "JOIN event_status es ON e.event_status_id = es.event_status_id WHERE 1=1 "
            );

            if (!"All".equals(status)) {
                sql.append("AND es.status_name = '").append(status).append("' ");
            }
            if (!query.isEmpty()) {
                sql.append("AND (LOWER(e.event_name) LIKE '%").append(query.replace("'", "''")).append("%' ")
                   .append("OR LOWER(e.location) LIKE '%").append(query.replace("'", "''")).append("%') ");
            }

            sql.append("ORDER BY e.start_date DESC");

            ResultSet rs = MySQL.executeSearch(sql.toString());
            while (rs.next()) {
                eventsTableModel.addRow(new Object[]{
                    rs.getString("event_name"),
                    rs.getString("description") != null ? rs.getString("description") : "",
                    rs.getString("location") != null ? rs.getString("location") : "School",
                    rs.getString("start_date"),
                    rs.getString("end_date") != null ? rs.getString("end_date") : "—",
                    rs.getString("status_name")
                });
            }
            rs.close();

        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Filter events failed", e);
        }
    }

    // =====================================================================
    // UI REUSABLE HELPERS
    // =====================================================================

    private JPanel createHeaderStrip(String title, String subtitle) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(243, 242, 242));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(225, 225, 225)),
                BorderFactory.createEmptyBorder(14, 25, 14, 25)));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        titleLbl.setForeground(new Color(50, 50, 50));
        header.add(titleLbl, BorderLayout.WEST);

        if (subtitle != null && !subtitle.isEmpty()) {
            JLabel subLbl = new JLabel(subtitle);
            subLbl.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            subLbl.setForeground(new Color(110, 110, 110));
            header.add(subLbl, BorderLayout.EAST);
        }

        return header;
    }

    private RoundPanel createMiniCard(JLabel valueLabel, String subtitle, Color color) {
        RoundPanel card = new RoundPanel(14);
        card.setGradientColors(color, color.brighter());
        card.setLayout(new BorderLayout(5, 0));
        card.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel(subtitle);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(new Color(255, 255, 255, 220));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(valueLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        textPanel.add(sub);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    private JLabel createPill(String text, Color bg, Color fg) {
        JLabel pill = new JLabel(text);
        pill.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pill.setForeground(fg);
        pill.setOpaque(true);
        pill.setBackground(bg);
        pill.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(fg.darker(), 1, true),
            BorderFactory.createEmptyBorder(3, 10, 3, 10)
        ));
        return pill;
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(241, 245, 249));
        table.getTableHeader().setForeground(new Color(51, 65, 85));
        table.setGridColor(new Color(235, 238, 242));
        table.setShowVerticalLines(false);
        return table;
    }

    private JPanel createLegendItem(String label, int value, Color color) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel colorBox = new JPanel();
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(14, 14));

        JLabel lbl = new JLabel(label + " (" + value + ")");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(80, 80, 80));

        row.add(colorBox);
        row.add(lbl);
        return row;
    }

    private static String formatNumber(double number) {
        if (number < 1000) {
            return String.format("%.0f", number);
        } else if (number < 1_000_000) {
            return String.format("%.1fk", number / 1000);
        } else {
            return String.format("%.1fM", number / 1_000_000);
        }
    }

    public static void main(String[] args) {
        try {
            FlatMacLightLaf.registerCustomDefaultsSource("lk/educore/schoolms/theme");
            FlatMacLightLaf.setup();
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new StudentDashboard("STU001").setVisible(true));
    }
}