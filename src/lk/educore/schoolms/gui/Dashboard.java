package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.SendLogo;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import java.awt.*;

/**
 * Main Dashboard Frame
 * Main application window with sidebar navigation for EduCore School Management System
 */
public class Dashboard extends JFrame {

    private String username;
    private String role;
    private JPanel contentPanel;
    private JButton[] navButtons;
    private JLabel lblUsername;
    private JLabel lblRole;

    public Dashboard(String username, String role) {
        this.username = username;
        this.role = role;
        initComponents();
    }

    private void initComponents() {
        setTitle("Dashboard - EduCore School Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Apply FlatLaf theme
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Sidebar panel
        JPanel sidebarPanel = createSidebar();
        mainPanel.add(sidebarPanel, BorderLayout.WEST);

        // Content panel
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(new DashboardPanel(), BorderLayout.CENTER);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Header panel
        JPanel headerPanel = createHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        add(mainPanel);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBackground(new Color(44, 62, 80));

        // Logo area
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(new Color(44, 62, 80));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JLabel logoLabel = new JLabel("EduCore");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logoLabel.setForeground(Color.WHITE);
        logoPanel.add(logoLabel);
        
        sidebar.add(logoPanel, BorderLayout.NORTH);

        // Navigation buttons
        JPanel navPanel = new JPanel(new GridLayout(10, 1, 0, 2));
        navPanel.setBackground(new Color(44, 62, 80));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] navItems = {
            "Dashboard",
            "Students",
            "Teachers",
            "Fees",
            "Exams",
            "Transport",
            "Academic",
            "Finance",
            "Events",
            "Guardians",
            "Library"
        };

        navButtons = new JButton[navItems.length];
        for (int i = 0; i < navItems.length; i++) {
            navButtons[i] = createNavButton(navItems[i]);
            final int index = i;
            navButtons[i].addActionListener(e -> handleNavigation(index));
            navPanel.add(navButtons[i]);
        }

        sidebar.add(navPanel, BorderLayout.CENTER);

        // Logout button
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoutPanel.setBackground(new Color(44, 62, 80));
        logoutPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        
        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(231, 76, 60));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(e -> handleLogout());
        logoutPanel.add(btnLogout);
        
        sidebar.add(logoutPanel, BorderLayout.SOUTH);

        return sidebar;
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(52, 73, 94));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setPreferredSize(new Dimension(180, 35));
        return button;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(236, 240, 241));
        header.setPreferredSize(new Dimension(0, 60));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Title
        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.add(titleLabel, BorderLayout.WEST);

        // User info panel
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(new Color(236, 240, 241));

        // Profile icon
        JLabel profileIcon = new JLabel();
        profileIcon.setIcon(SendLogo.getProfileIcon());
        userPanel.add(profileIcon);

        // Username
        lblUsername = new JLabel(username);
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userPanel.add(lblUsername);

        // Role
        lblRole = new JLabel("(" + role + ")");
        lblRole.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblRole.setForeground(Color.GRAY);
        userPanel.add(lblRole);

        header.add(userPanel, BorderLayout.EAST);

        return header;
    }

    private void handleNavigation(int index) {
        // Reset all button colors
        for (JButton button : navButtons) {
            button.setBackground(new Color(52, 73, 94));
        }

        // Highlight selected button
        navButtons[index].setBackground(new Color(39, 174, 96));

        // Update content panel based on selection
        contentPanel.removeAll();

        switch (index) {
            case 0: // Dashboard
                contentPanel.add(new DashboardPanel(), BorderLayout.CENTER);
                break;
            case 1: // Students
                contentPanel.add(new StudentManagement(), BorderLayout.CENTER);
                break;
            case 2: // Teachers
                contentPanel.add(new TeacherManagement(), BorderLayout.CENTER);
                break;
            case 3: // Fees
                contentPanel.add(new FeeManagement(), BorderLayout.CENTER);
                break;
            case 4: // Exams
                contentPanel.add(new ExamManagement(), BorderLayout.CENTER);
                break;
            case 5: // Transport
                contentPanel.add(new TransportManagement(), BorderLayout.CENTER);
                break;
            case 6: // Academic
                contentPanel.add(new AcademicPerformance(), BorderLayout.CENTER);
                break;
            case 7: // Finance
                contentPanel.add(new FinanceManagement(), BorderLayout.CENTER);
                break;
            case 8: // Events
                contentPanel.add(new EventManagement(), BorderLayout.CENTER);
                break;
            case 9: // Guardians
                contentPanel.add(new GuardianManagement(), BorderLayout.CENTER);
                break;
            case 10: // Library
                contentPanel.add(new LibraryManagement(), BorderLayout.CENTER);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Dashboard("admin", "Admin").setVisible(true);
        });
    }
}