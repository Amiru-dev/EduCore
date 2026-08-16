package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;
import lk.educore.schoolms.validate.DataValidator;
import lk.educore.schoolms.exceptions.ValidationException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Sign In Screen Login screen with role-based authentication for EduCore School
 * Management System
 */
public class SignIn extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRole;
    private JButton btnSignIn;
    private JButton btnSetup;
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel formPanel;
    private JPanel buttonPanel;

    public SignIn() {
        initComponents();
        checkDatabaseConfig();
    }

    private void initComponents() {
        setTitle("Sign In - EduCore School Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 350);
        setLocationRelativeTo(null);
        setResizable(false);

        // Apply FlatLaf theme
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Main panel
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Logo and title panel
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel logoLabel = new JLabel("🏫 EduCore", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(logoLabel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form panel
        formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtUsername = new JTextField(20);
        formPanel.add(txtUsername, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtPassword = new JPasswordField(20);
        formPanel.add(txtPassword, gbc);

        // Role
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Role:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cmbRole = new JComboBox<>(new String[]{"Admin", "Teacher", "Student"});
        formPanel.add(cmbRole, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnSignIn = new JButton("Sign In");
        btnSignIn.setPreferredSize(new Dimension(100, 30));
        btnSignIn.addActionListener(e -> handleSignIn());

        btnSetup = new JButton("Setup Database");
        btnSetup.setPreferredSize(new Dimension(120, 30));
        btnSetup.addActionListener(e -> openSetupDialog());

        buttonPanel.add(btnSignIn);
        buttonPanel.add(btnSetup);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void checkDatabaseConfig() {
        try {
            MySQL.getConnection();
        } catch (Exception e) {
            int result = JOptionPane.showConfirmDialog(this,
                    "Database not configured. Would you like to set it up now?",
                    "Database Configuration Required",
                    JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                openSetupDialog();
            }
        }
    }

    private void openSetupDialog() {
        new SetupConnection(this).setVisible(true);
    }

    private void handleSignIn() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String role = (String) cmbRole.getSelectedItem();

        try {
            DataValidator.isRequired(username);
            DataValidator.isRequired(password);

            Connection conn = MySQL.getConnection();
            String query = "SELECT u.user_id, u.password, r.role_name FROM user u "
                    + "JOIN role r ON u.role_id = r.role_id "
                    + "WHERE u.username = ? AND r.role_name = ?";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, role);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");

                if (password.equals(storedPassword)) {
                    JOptionIcon.showSuccessDialog(this, "Login successful!");

                    // Route to appropriate dashboard based on role
                    dispose();
                    if ("Student".equals(role)) {
                        new StudentDashboard(username).setVisible(true);
                    } else {
                        // Admin and Teacher go to main Dashboard
                        new Dashboard(username, role).setVisible(true);
                    }
                } else {
                    JOptionIcon.showErrorDialog(this, "Invalid password!");
                }
            } else {
                JOptionIcon.showErrorDialog(this, "User not found!");
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (ValidationException e) {
            JOptionIcon.showErrorDialog(this, e.getUserFriendlyMessage());
        } catch (SQLException e) {
            JOptionIcon.showErrorDialog(this, "Database error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SignIn().setVisible(true);
        });
    }
}
