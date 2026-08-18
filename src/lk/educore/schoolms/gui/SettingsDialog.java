package lk.educore.schoolms.gui;

import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import java.awt.*;

/**
 * Settings Dialog
 * Application settings and configuration
 */
public class SettingsDialog extends JDialog {

    private JCheckBox chkEmailNotifications;
    private JCheckBox chkAutoBackup;
    private JComboBox<String> cmbTheme;
    private JComboBox<String> cmbLanguage;
    private JTextField txtBackupPath;
    private JButton btnSave, btnCancel, btnReset;

    public SettingsDialog(Frame parent) {
        super(parent, "Settings", true);
        initComponents();
        loadSettings();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(450, 400);
        setLayout(new BorderLayout(10, 10));

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Email Notifications
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Email Notifications:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        chkEmailNotifications = new JCheckBox("Enable email notifications");
        formPanel.add(chkEmailNotifications, gbc);

        // Auto Backup
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Auto Backup:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        chkAutoBackup = new JCheckBox("Enable automatic backup");
        formPanel.add(chkAutoBackup, gbc);

        // Theme
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Theme:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cmbTheme = new JComboBox<>(new String[]{"Light", "Dark", "System Default"});
        formPanel.add(cmbTheme, gbc);

        // Language
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Language:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cmbLanguage = new JComboBox<>(new String[]{"English", "Sinhala", "Tamil"});
        formPanel.add(cmbLanguage, gbc);

        // Backup Path
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Backup Path:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JPanel backupPathPanel = new JPanel(new BorderLayout(5, 0));
        txtBackupPath = new JTextField(20);
        JButton btnBrowse = new JButton("Browse");
        btnBrowse.addActionListener(e -> browseBackupPath());
        backupPathPanel.add(txtBackupPath, BorderLayout.CENTER);
        backupPathPanel.add(btnBrowse, BorderLayout.EAST);
        formPanel.add(backupPathPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnSave = new JButton("Save Settings");
        btnSave.addActionListener(e -> saveSettings());
        
        btnReset = new JButton("Reset to Default");
        btnReset.addActionListener(e -> resetSettings());
        
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnReset);
        buttonPanel.add(btnCancel);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadSettings() {
        // Load settings from properties file or database
        // For now, set defaults
        chkEmailNotifications.setSelected(true);
        chkAutoBackup.setSelected(false);
        cmbTheme.setSelectedItem("Light");
        cmbLanguage.setSelectedItem("English");
        txtBackupPath.setText("C:/backup/");
    }

    private void browseBackupPath() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Backup Directory");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            txtBackupPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void saveSettings() {
        // Save settings to properties file or database
        JOptionIcon.showSuccessDialog(this, "Settings saved successfully!");
        dispose();
    }

    private void resetSettings() {
        chkEmailNotifications.setSelected(true);
        chkAutoBackup.setSelected(false);
        cmbTheme.setSelectedItem("Light");
        cmbLanguage.setSelectedItem("English");
        txtBackupPath.setText("C:/backup/");
        JOptionIcon.showInfoDialog(this, "Settings reset to default values.");
    }
}