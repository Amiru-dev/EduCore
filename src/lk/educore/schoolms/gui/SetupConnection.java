package lk.educore.schoolms.gui;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;
import lk.educore.schoolms.validate.DataValidator;
import lk.educore.schoolms.exceptions.ValidationException;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import javax.imageio.ImageIO;

/**
 * Database Connection Setup Screen Allows users to configure database
 * connection settings
 */
public class SetupConnection extends JFrame {

    private JTextField txtHost;
    private JTextField txtPort;
    private JTextField txtDBName;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtDumpPath;
    private JTextField txtBackupPath;

    public SetupConnection() {
        initComponents();
        loadExistingConfig();

        try {
            InputStream imgStream = getClass().getResourceAsStream("/lk/educore/schoolms/resources/logo.png");
            if (imgStream == null) {
                SignIn.getLoggerObject().log(Level.WARNING, "School logo image resource not found at /lk/educore/schoolms/resources/logo.png");
                return;
            }
            ImageIcon originalIcon = new ImageIcon(ImageIO.read(imgStream));
            this.setIconImage(originalIcon.getImage());

        } catch (IOException e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Failed to load school logo image: " + e.getMessage(), e);
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Unexpected error loading school logo image: " + e.getMessage(), e);
        }
    }

    private void initComponents() {
        setTitle("EduCore School Management System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 450);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Database Connection Setup");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Database Host
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Database Host:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtHost = new JTextField("localhost", 20);
        formPanel.add(txtHost, gbc);

        // Port
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Port:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtPort = new JTextField("3306", 20);
        formPanel.add(txtPort, gbc);

        // Database Name
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Database Name:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtDBName = new JTextField("school_management_db", 20);
        formPanel.add(txtDBName, gbc);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtUsername = new JTextField("root", 20);
        formPanel.add(txtUsername, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtPassword = new JPasswordField(20);
        formPanel.add(txtPassword, gbc);

        // Dump Path
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("mysqldump.exe Path:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JPanel dumpPanel = new JPanel(new BorderLayout(5, 0));
        txtDumpPath = new JTextField("C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump.exe", 15);
        JButton btnBrowseDump = new JButton("Browse");
        btnBrowseDump.addActionListener(e -> browseDumpPath());
        dumpPanel.add(txtDumpPath, BorderLayout.CENTER);
        dumpPanel.add(btnBrowseDump, BorderLayout.EAST);
        formPanel.add(dumpPanel, gbc);

        // Backup Path
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Backup Path:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JPanel backupPanel = new JPanel(new BorderLayout(5, 0));
        txtBackupPath = new JTextField(15);
        JButton btnBrowseBackup = new JButton("Browse");
        btnBrowseBackup.addActionListener(e -> browseBackupPath());
        backupPanel.add(txtBackupPath, BorderLayout.CENTER);
        backupPanel.add(btnBrowseBackup, BorderLayout.EAST);
        formPanel.add(backupPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnTest = new JButton("Test Connection");
        btnTest.addActionListener(e -> testConnection());

        JButton btnSave = new JButton("Save & Continue");
        btnSave.addActionListener(e -> saveConfiguration());

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> cancelSetup());

        buttonPanel.add(btnTest);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadExistingConfig() {
        if (MySQL.configExists()) {
            MySQL.initConfig();
            txtHost.setText(MySQL.getDbHost());
            txtPort.setText(MySQL.getDbPort());
            txtDBName.setText(MySQL.getDbName());
            txtUsername.setText(MySQL.getDbUser());
            String dumpPath = MySQL.getDumpPath();
            if (dumpPath != null && !dumpPath.isEmpty()) {
                txtDumpPath.setText(dumpPath);
            }
            String backupPath = MySQL.getBackupPath();
            if (backupPath != null && !backupPath.isEmpty()) {
                txtBackupPath.setText(backupPath);
            }
        }
    }

    private void browseDumpPath() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Executable files (*.exe)", "exe"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            txtDumpPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void browseBackupPath() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            txtBackupPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void testConnection() {
        try {
            // Validate inputs (dump path not required for connection test)
            DataValidator.isRequired(txtHost.getText().trim(), "Database Host");
            DataValidator.isRequired(txtPort.getText().trim(), "Port");
            DataValidator.isRequired(txtDBName.getText().trim(), "Database Name");
            DataValidator.isRequired(txtUsername.getText().trim(), "Username");
            DataValidator.isRequired(new String(txtPassword.getPassword()).trim(), "Password");

            String host = txtHost.getText().trim();
            String port = txtPort.getText().trim();
            String dbName = txtDBName.getText().trim();
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();

            // Test connection
            if (MySQL.testConnection(host, port, dbName, username, password)) {
                JOptionIcon.showSuccessDialog(this, "Database connection successful!");
            } else {
                JOptionIcon.showErrorDialog(this, "Failed to connect to database. Please check your settings.");
            }
        } catch (ValidationException e) {
            JOptionIcon.showWarningDialog(this, e.getUserFriendlyMessage());
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error testing connection: " + e.getMessage());
        }
    }

    private void saveConfiguration() {
        try {
            // Validate inputs
            DataValidator.isRequired(txtHost.getText().trim(), "Database Host");
            DataValidator.isRequired(txtPort.getText().trim(), "Port");
            DataValidator.isRequired(txtDBName.getText().trim(), "Database Name");
            DataValidator.isRequired(txtUsername.getText().trim(), "Username");
            DataValidator.isRequired(new String(txtPassword.getPassword()).trim(), "Password");
            DataValidator.isRequired(txtBackupPath.getText().trim(), "Backup Path");

            String host = txtHost.getText().trim();
            String port = txtPort.getText().trim();
            String dbName = txtDBName.getText().trim();
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();
            String dumpPath = txtDumpPath.getText().trim();
            String backupPath = txtBackupPath.getText().trim();

            // Validate paths
            // Only validate dump path if it's provided and not the default
            if (!dumpPath.isEmpty() && !dumpPath.equals("C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump.exe")) {
                File dumpFile = new File(dumpPath);
                if (!dumpFile.exists() || !dumpFile.isFile()) {
                    JOptionIcon.showErrorDialog(this, "Dump path does not exist or is not a file.");
                    return;
                }
            }

            File backupDir = new File(backupPath);
            if (!backupDir.exists() || !backupDir.isDirectory()) {
                JOptionIcon.showErrorDialog(this, "Backup path does not exist or is not a directory.");
                return;
            }

            // Test connection before saving
            if (!MySQL.testConnection(host, port, dbName, username, password)) {
                JOptionIcon.showErrorDialog(this, "Failed to connect to database. Please check your settings.");
                return;
            }

            // Save configuration
            MySQL.saveConfig(host, port, dbName, username, password, dumpPath, backupPath);

            JOptionIcon.showSuccessDialog(this, "Configuration saved successfully!");

            // Open login screen
            dispose();
            new SignIn().setVisible(true);

        } catch (ValidationException e) {
            JOptionIcon.showWarningDialog(this, e.getUserFriendlyMessage());
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error saving configuration: " + e.getMessage());
        }
    }

    private void cancelSetup() {
        if (MySQL.configExists()) {
            // Configuration exists, go to login
            dispose();
            new SignIn().setVisible(true);
        } else {
            // No configuration, exit application
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        FlatMacLightLaf.registerCustomDefaultsSource("lk/educore/schoolms/theme");
        FlatMacLightLaf.setup();
        SwingUtilities.invokeLater(() -> {
            new SetupConnection().setVisible(true);
        });
    }
}
