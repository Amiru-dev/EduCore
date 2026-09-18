package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Guardian Management Panel
 * Parent/guardian CRUD, address management
 */
public class GuardianManagement extends JPanel {

    private JTable guardianTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnAddGuardian, btnRefresh;

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
        
        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadGuardianData());
        
        rightPanel.add(new JLabel("Search:"));
        rightPanel.add(txtSearch);
        rightPanel.add(btnAddGuardian);
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
        loadGuardianData();
    }

    private void addGuardian() {
        JOptionIcon.showInfoDialog(this, "Add Guardian - Coming Soon");
    }
}