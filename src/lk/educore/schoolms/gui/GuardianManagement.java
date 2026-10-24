package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;
import lk.educore.schoolms.validate.DataValidator;
import lk.educore.schoolms.exceptions.ValidationException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.logging.Level;

/**
 * Guardian Management Panel
 * Parent/guardian CRUD, address management
 */
public class GuardianManagement extends JPanel {

    private JTable guardianTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnAddGuardian, btnEditGuardian, btnDeleteGuardian, btnRefresh;

    public GuardianManagement() {
        initComponents();
        loadGuardianData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        
        JLabel titleLabel = new JLabel("Guardian Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Search and button panel
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        
        txtSearch = new JTextField(20);
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchGuardians();
            }
        });
        
        btnAddGuardian = new JButton("Add Guardian");
        btnAddGuardian.addActionListener(e -> addGuardian());

        btnEditGuardian = new JButton("Edit Guardian");
        btnEditGuardian.addActionListener(e -> editGuardian());

        btnDeleteGuardian = new JButton("Delete Guardian");
        btnDeleteGuardian.addActionListener(e -> deleteGuardian());

        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadGuardianData());

        rightPanel.add(new JLabel("Search:"));
        rightPanel.add(txtSearch);
        rightPanel.add(btnAddGuardian);
        rightPanel.add(btnEditGuardian);
        rightPanel.add(btnDeleteGuardian);
        rightPanel.add(btnRefresh);
        
        headerPanel.add(rightPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(
            new Object[]{"Guardian ID", "Name", "NIC", "Relationship", "Phone", "Email"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        guardianTable = new JTable(tableModel);
        guardianTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        guardianTable.setRowHeight(25);
        guardianTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(guardianTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadGuardianData() {
        try {
            tableModel.setRowCount(0);
            
            ResultSet rs = MySQL.executeSearch("SELECT guardian_id, first_name, last_name, nic, relationship, phone, email " +
                          "FROM guardian ORDER BY first_name, last_name");
            
            while (rs.next()) {
                int guardianId = rs.getInt("guardian_id");
                String name = rs.getString("first_name") + " " + rs.getString("last_name");
                String nic = rs.getString("nic");
                String relationship = rs.getString("relationship");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                
                tableModel.addRow(new Object[]{guardianId, name, nic, relationship, phone, email});
            }
            
            rs.close();
            
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading guardian data: " + e.getMessage());
        }
    }

    private void searchGuardians() {
        String searchTerm = txtSearch.getText().trim();
        try {
            tableModel.setRowCount(0);

            String query = "SELECT guardian_id, first_name, last_name, nic, relationship, phone, email " +
                          "FROM guardian ";

            if (!searchTerm.isEmpty()) {
                query += "WHERE first_name LIKE '%" + searchTerm.replace("'", "''") + "%' " +
                        "OR last_name LIKE '%" + searchTerm.replace("'", "''") + "%' " +
                        "OR nic LIKE '%" + searchTerm.replace("'", "''") + "%' " +
                        "OR phone LIKE '%" + searchTerm.replace("'", "''") + "%' " +
                        "OR email LIKE '%" + searchTerm.replace("'", "''") + "%' ";
            }

            query += "ORDER BY first_name, last_name";

            ResultSet rs = MySQL.executeSearch(query);

            while (rs.next()) {
                int guardianId = rs.getInt("guardian_id");
                String name = rs.getString("first_name") + " " + rs.getString("last_name");
                String nic = rs.getString("nic");
                String relationship = rs.getString("relationship");
                String phone = rs.getString("phone");
                String email = rs.getString("email");

                tableModel.addRow(new Object[]{guardianId, name, nic, relationship, phone, email});
            }

            rs.close();

        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error searching guardians: " + e.getMessage());
        }
    }

    private void addGuardian() {
        // Build a proper JDialog
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentFrame, "Add Guardian", true);

        // Set EduCore logo icon
        try {
            InputStream imgStream = getClass().getResourceAsStream("/lk/educore/schoolms/resources/logo.png");
            if (imgStream != null) {
                ImageIcon originalIcon = new ImageIcon(ImageIO.read(imgStream));
                dialog.setIconImage(originalIcon.getImage());
            } else {
                SignIn.getLoggerObject().log(Level.WARNING, "EduCore logo not found at /lk/educore/schoolms/resources/logo.png");
            }
        } catch (IOException ex) {
            SignIn.getLoggerObject().log(Level.WARNING, "Failed to load logo: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            SignIn.getLoggerObject().log(Level.WARNING, "Unexpected error loading logo: " + ex.getMessage(), ex);
        }

        // Form fields
        JTextField txtFirstName = new JTextField(20);
        JTextField txtLastName = new JTextField(20);
        JTextField txtNIC = new JTextField(20);
        JComboBox<String> cmbRelationship = new JComboBox<>(new String[]{"Father", "Mother", "Guardian"});
        JTextField txtPhone = new JTextField(20);
        JTextField txtEmail = new JTextField(20);
        JTextField txtOccupation = new JTextField(20);
        JTextArea txtAddress = new JTextArea(3, 20);
        txtAddress.setLineWrap(true);
        txtAddress.setWrapStyleWord(true);
        JScrollPane addressScroll = new JScrollPane(txtAddress);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("First Name: *"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtFirstName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Last Name: *"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtLastName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("NIC:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtNIC, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Relationship: *"), gbc);
        gbc.gridx = 1;
        formPanel.add(cmbRelationship, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Phone: *"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtPhone, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Occupation:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtOccupation, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(new JLabel("Address: *"), gbc);
        gbc.gridx = 1;
        formPanel.add(addressScroll, gbc);

        JLabel noteLabel = new JLabel("* Required fields");
        noteLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        noteLabel.setForeground(Color.GRAY);
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        formPanel.add(noteLabel, gbc);

        // Buttons
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        btnSave.addActionListener(ev -> {
            String firstName = txtFirstName.getText().trim();
            String lastName = txtLastName.getText().trim();
            String nic = txtNIC.getText().trim();
            String relationship = (String) cmbRelationship.getSelectedItem();
            String phone = txtPhone.getText().trim();
            String email = txtEmail.getText().trim();
            String occupation = txtOccupation.getText().trim();
            String address = txtAddress.getText().trim();

            // Validation
            try {
                DataValidator.isRequired(firstName, "First Name");
                DataValidator.isRequired(lastName, "Last Name");
                DataValidator.isRequired(phone, "Phone");
                DataValidator.isRequired(address, "Address");

                if (!nic.isEmpty()) {
                    DataValidator.isValidNIC(nic);
                }

                if (!email.isEmpty()) {
                    DataValidator.isValidEmail(email);
                }

                // Check if guardian with same NIC already exists
                if (!nic.isEmpty()) {
                    ResultSet checkNic = MySQL.executeSearch("SELECT guardian_id, first_name, last_name FROM guardian WHERE nic = '" + nic.replace("'", "''") + "'");
                    if (checkNic.next()) {
                        String existingName = checkNic.getString("first_name") + " " + checkNic.getString("last_name");
                        checkNic.close();
                        JOptionIcon.showWarningDialog(dialog, "A guardian with NIC '" + nic + "' already exists:\n" + existingName + "\n\nPlease edit the existing guardian instead of adding a duplicate.");
                        return;
                    }
                    checkNic.close();
                }

                String nicVal = nic.isEmpty() ? "NULL" : "'" + nic.replace("'", "''") + "'";
                String emailVal = email.isEmpty() ? "NULL" : "'" + email.replace("'", "''") + "'";
                String occupationVal = occupation.isEmpty() ? "NULL" : "'" + occupation.replace("'", "''") + "'";

                String sql = "INSERT INTO guardian (first_name, last_name, nic, relationship, phone, email, address, occupation) VALUES ('"
                        + firstName.replace("'", "''") + "', '" + lastName.replace("'", "''") + "', " + nicVal + ", '"
                        + relationship + "', '" + phone + "', " + emailVal + ", '" + address.replace("'", "''") + "', " + occupationVal + ")";

                int rows = MySQL.executeIUD(sql);

                if (rows > 0) {
                    loadGuardianData();
                    JOptionIcon.showSuccessDialog(dialog, "Guardian added successfully!");
                    dialog.dispose();
                } else {
                    JOptionIcon.showErrorDialog(dialog, "Failed to add guardian.");
                }

            } catch (ValidationException e) {
                JOptionIcon.showWarningDialog(dialog, e.getUserFriendlyMessage());
            } catch (Exception ex) {
                JOptionIcon.showErrorDialog(dialog, "Error adding guardian: " + ex.getMessage());
            }
        });
        btnCancel.addActionListener(ev -> dialog.dispose());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);

        dialog.setLayout(new BorderLayout());
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setMinimumSize(new Dimension(450, dialog.getHeight()));
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }

    private void editGuardian() {
        int selectedRow = guardianTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a guardian to edit.");
            return;
        }

        int guardianId = (int) tableModel.getValueAt(selectedRow, 0);

        // Use existing GuardianDialog for editing
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        GuardianDialog dialog = new GuardianDialog(parentFrame, guardianId);
        dialog.setVisible(true);

        // Refresh data after dialog closes
        loadGuardianData();
    }

    private void deleteGuardian() {
        int selectedRow = guardianTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a guardian to delete.");
            return;
        }

        int guardianId = (int) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);

        // Check if guardian has associated students
        try {
            ResultSet rs = MySQL.executeSearch("SELECT COUNT(*) as count FROM student WHERE guardian_id = " + guardianId);
            if (rs.next() && rs.getInt("count") > 0) {
                JOptionIcon.showWarningDialog(this, "Cannot delete this guardian. There are students associated with this guardian. Please reassign or delete the students first.");
                rs.close();
                return;
            }
            rs.close();
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error checking guardian associations: " + e.getMessage());
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete the guardian \"" + name + "\"?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM guardian WHERE guardian_id = " + guardianId;
                int rows = MySQL.executeIUD(sql);

                if (rows > 0) {
                    loadGuardianData();
                    JOptionIcon.showSuccessDialog(this, "Guardian deleted successfully!");
                } else {
                    JOptionIcon.showErrorDialog(this, "Failed to delete guardian.");
                }
            } catch (Exception ex) {
                JOptionIcon.showErrorDialog(this, "Error deleting guardian: " + ex.getMessage());
            }
        }
    }
}