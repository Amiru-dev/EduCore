package lk.educore.schoolms.gui;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import lk.educore.schoolms.model.SendLogo;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Main Dashboard Frame Main application window with sidebar navigation for
 * EduCore School Management System
 */
public class Dashboard extends JFrame {

    private String username;
    private String role;
    private JPanel contentPanel;
    private JToggleButton[] navButtons;
    private ButtonGroup navButtonGroup;
    private JLabel lblUsername;
    private JLabel lblRole;

    public Dashboard(String username, String role) {
        this.username = username;
        this.role = role;
        initComponents();

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
        setTitle("EduCore School Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Sidebar panel
        JPanel sidebarPanel = createSidebar();
        mainPanel.add(sidebarPanel, BorderLayout.WEST);

        // Content panel
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(new DashboardPanel(role), BorderLayout.CENTER);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Header panel
        JPanel headerPanel = createHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        add(mainPanel);
        
        setJMenuBar(createMenuBar());
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

        // Navigation buttons
        List<String> navItemsList = new ArrayList<>();
        List<String> iconBasesList = new ArrayList<>();
        List<Integer> indexMapping = new ArrayList<>();

        // 0: Dashboard
        navItemsList.add("Dashboard"); iconBasesList.add("dashboard"); indexMapping.add(0);
        // 1: Students
        navItemsList.add("Students"); iconBasesList.add("students"); indexMapping.add(1);
        
        if ("Admin".equals(role)) {
            // 2: Teachers
            navItemsList.add("Teachers"); iconBasesList.add("teachers"); indexMapping.add(2);
            // 3: Fees
            navItemsList.add("Fees"); iconBasesList.add("fees"); indexMapping.add(3);
        }
        
        // 4: Exams
        navItemsList.add("Exams"); iconBasesList.add("exams"); indexMapping.add(4);

        
        // 6: Academic
        navItemsList.add("Academic"); iconBasesList.add("academic"); indexMapping.add(6);
        
        if ("Admin".equals(role)) {
            // 7: Finance
            navItemsList.add("Finance"); iconBasesList.add("finance"); indexMapping.add(7);
        }
        
        // 8: Events
        navItemsList.add("Events"); iconBasesList.add("events"); indexMapping.add(8);
        // 9: Guardians
        navItemsList.add("Guardians"); iconBasesList.add("guardians"); indexMapping.add(9);
        // 10: Library
        navItemsList.add("Library"); iconBasesList.add("library"); indexMapping.add(10);

        JPanel navPanel = new JPanel(new GridLayout(navItemsList.size(), 1, 0, 2));
        navPanel.setBackground(new Color(44, 62, 80));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        navButtons = new JToggleButton[navItemsList.size()];
        navButtonGroup = new ButtonGroup();

        for (int i = 0; i < navItemsList.size(); i++) {
            navButtons[i] = createNavButton(navItemsList.get(i), iconBasesList.get(i));
            final int originalIndex = indexMapping.get(i);
            navButtons[i].addActionListener(e -> handleNavigation(originalIndex));
            navButtonGroup.add(navButtons[i]);
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

    private JToggleButton createNavButton(String text, String iconBase) {
        JToggleButton button = new JToggleButton(text);
        button.setBackground(new Color(44, 62, 80));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setPreferredSize(new Dimension(180, 35));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!button.isSelected()) {
                    button.setBackground(new Color(52, 73, 94)); // Hover background
                    button.setForeground(new Color(165, 216, 221)); // Hover font color
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!button.isSelected()) {
                    button.setBackground(new Color(44, 62, 80)); // Default background
                    button.setForeground(Color.WHITE); // Default font color
                }
            }
        });

        button.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                button.setBackground(new Color(39, 174, 96)); // Selected background
                button.setForeground(Color.WHITE); 
            } else {
                button.setBackground(new Color(44, 62, 80)); // Default background
                button.setForeground(Color.WHITE);
            }
        });
        
        try {
            button.setIcon(new ImageIcon(getClass().getResource("/lk/educore/schoolms/resources/" + iconBase + "-light.png")));
            button.setRolloverIcon(new ImageIcon(getClass().getResource("/lk/educore/schoolms/resources/" + iconBase + "-colored.png")));
            button.setSelectedIcon(new ImageIcon(getClass().getResource("/lk/educore/schoolms/resources/" + iconBase + "-colored.png")));
        } catch (Exception e) {
            // Ignore if icon not found
        }
        
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
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        userPanel.add(lblUsername);

        // Role
        lblRole = new JLabel("(" + role + ")");
        lblRole.setFont(new Font("Segoe UI", Font.ITALIC, 18));
        lblRole.setForeground(Color.GRAY);
        userPanel.add(lblRole);

        header.add(userPanel, BorderLayout.EAST);

        return header;
    }

    private void handleNavigation(int index) {
        // Update content panel based on selection
        contentPanel.removeAll();

        switch (index) {
            case 0: // Dashboard
                contentPanel.add(new DashboardPanel(role), BorderLayout.CENTER);
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

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File Menu
        JMenu fileMenu = new JMenu("File");
        
        JMenuItem setupDatabaseMenu = new JMenuItem("Setup Database");
        setupDatabaseMenu.addActionListener(e -> {
            if ("Admin".equals(role)) {
                new SetupConnection().setVisible(true);
            } else {
                JOptionIcon.showWarningDialog(this, "Access blocked for role: " + role);
            }
        });
        
        JMenuItem backupMenu = new JMenuItem("Backup");
        backupMenu.addActionListener(e -> {
            if ("Admin".equals(role)) {
                new BackupUtility().setVisible(true);
            } else {
                JOptionIcon.showWarningDialog(this, "Access blocked for role: " + role);
            }
        });
        
        JMenuItem logOutMenu = new JMenuItem("Log Out");
        logOutMenu.addActionListener(e -> handleLogout());
        
        JMenuItem exitMenu = new JMenuItem("Exit");
        exitMenu.addActionListener(e -> {
            System.exit(0);
        });
        
        fileMenu.add(setupDatabaseMenu);
        fileMenu.add(backupMenu);
        fileMenu.add(logOutMenu);
        fileMenu.add(exitMenu);
        
        // Edit Menu
        JMenu editMenu = new JMenu("Edit");
        
        JMenuItem updateLogoMenu = new JMenuItem("Update Logo");
        updateLogoMenu.addActionListener(e -> {
            if ("Admin".equals(role)) {
                updateLogo();
            } else {
                JOptionIcon.showWarningDialog(this, "Access blocked for role: " + role);
            }
        });
        
        editMenu.add(updateLogoMenu);
        
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        
        return menuBar;
    }

    private void updateLogo() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select a PNG Logo");
            fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));

            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                // Validate .png extension
                if (!selectedFile.getName().toLowerCase().endsWith(".png")) {
                    JOptionIcon.showErrorDialog(this, "Only .png files are allowed!");
                    return;
                }

                File targetFile = new File("src/lk/educore/schoolms/resources/logo.png");

                if (!targetFile.getParentFile().exists()) {
                    targetFile.getParentFile().mkdirs();
                }

                // Copy file
                try (InputStream in = new FileInputStream(selectedFile); OutputStream out = new FileOutputStream(targetFile)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                    JOptionIcon.showSuccessDialog(this, "Logo image replaced successfully!");
                } catch (IOException ex) {
                    JOptionIcon.showErrorDialog(this, "Error replacing image: " + ex.getMessage());
                }
            }
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Unexpected error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        FlatMacLightLaf.registerCustomDefaultsSource("lk/educore/schoolms/theme");
        FlatMacLightLaf.setup();
        SwingUtilities.invokeLater(() -> {
            new Dashboard("admin", "Admin").setVisible(true);
        });
    }
}
