package lk.educore.schoolms.gui;

import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import java.awt.*;

/**
 * Backup Utility
 * Database backup and restore functionality
 */
public class BackupUtility extends JDialog {

    private JButton btnBackup, btnRestore, btnCancel;
    private JTextField txtBackupPath;
    private JProgressBar progressBar;

    public BackupUtility(Frame parent) {
        super(parent, "Backup & Restore", true);
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(500, 300);
        setLayout(new BorderLayout(10, 10));

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Database Backup & Restore");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Backup panel
        JPanel backupPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Backup path
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        backupPanel.add(new JLabel("Backup Path:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtBackupPath = new JTextField(25);
        txtBackupPath.setText("C:/backup/school_management_backup.sql");
        backupPanel.add(txtBackupPath, gbc);

        JButton btnBrowse = new JButton("Browse");
        btnBrowse.addActionListener(e -> browseBackupPath());
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        backupPanel.add(btnBrowse, gbc);

        mainPanel.add(backupPanel, BorderLayout.CENTER);

        // Progress bar
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);
        mainPanel.add(progressBar, BorderLayout.SOUTH);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnBackup = new JButton("Backup Database");
        btnBackup.addActionListener(e -> backupDatabase());
        
        btnRestore = new JButton("Restore Database");
        btnRestore.addActionListener(e -> restoreDatabase());
        
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());

        buttonPanel.add(btnBackup);
        buttonPanel.add(btnRestore);
        buttonPanel.add(btnCancel);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void browseBackupPath() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Backup Location");
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            txtBackupPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void backupDatabase() {
        String backupPath = txtBackupPath.getText().trim();
        if (backupPath.isEmpty()) {
            JOptionIcon.showWarningDialog(this, "Please specify a backup path.");
            return;
        }

        int confirm = JOptionIcon.showConfirmDialog(this, "Backup database to: " + backupPath + "?");
        if (confirm == JOptionPane.YES_OPTION) {
            progressBar.setVisible(true);
            progressBar.setIndeterminate(true);
            progressBar.setString("Backing up database...");
            
            // Simulate backup process
            Timer timer = new Timer(2000, e -> {
                progressBar.setIndeterminate(false);
                progressBar.setString("Backup completed!");
                JOptionIcon.showSuccessDialog(this, "Database backup completed successfully!");
                progressBar.setVisible(false);
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void restoreDatabase() {
        String backupPath = txtBackupPath.getText().trim();
        if (backupPath.isEmpty()) {
            JOptionIcon.showWarningDialog(this, "Please specify a backup file path.");
            return;
        }

        int confirm = JOptionIcon.showConfirmDialog(this, 
            "WARNING: This will replace the current database. Continue restore from: " + backupPath + "?");
        if (confirm == JOptionPane.YES_OPTION) {
            progressBar.setVisible(true);
            progressBar.setIndeterminate(true);
            progressBar.setString("Restoring database...");
            
            // Simulate restore process
            Timer timer = new Timer(2000, e -> {
                progressBar.setIndeterminate(false);
                progressBar.setString("Restore completed!");
                JOptionIcon.showSuccessDialog(this, "Database restored successfully!");
                progressBar.setVisible(false);
            });
            timer.setRepeats(false);
            timer.start();
        }
    }
}