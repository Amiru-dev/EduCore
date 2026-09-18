package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Transport Management Panel
 * Main interface for vehicle management, route management, and student-route assignments
 */
public class TransportManagement extends JPanel {

    private JTable transportTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<String> cmbSearchBy;
    private JButton btnAddVehicle, btnRefresh;
    private JTabbedPane tabbedPane;

    public TransportManagement() {
        initComponents();
        loadTransportData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Vehicles Tab
        JPanel vehiclesPanel = createVehiclesPanel();
        tabbedPane.addTab("Vehicles", vehiclesPanel);
        
        // Routes Tab
        JPanel routesPanel = createRoutesPanel();
        tabbedPane.addTab("Routes", routesPanel);
        
        // Assignments Tab
        JPanel assignmentsPanel = createAssignmentsPanel();
        tabbedPane.addTab("Student Assignments", assignmentsPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Title
        JLabel titleLabel = new JLabel("Transport Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.WEST);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        cmbSearchBy = new JComboBox<>(new String[]{"Plate No", "Vehicle Type", "Driver Name", "Status"});
        txtSearch = new JTextField(20);
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchTransport();
            }
        });
        
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> searchTransport());
        
        searchPanel.add(new JLabel("Search by:"));
        searchPanel.add(cmbSearchBy);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        
        panel.add(searchPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createVehiclesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        
        btnAddVehicle = new JButton("Add Vehicle");
        btnAddVehicle.addActionListener(e -> addVehicle());
        
        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadTransportData());

        buttonPanel.add(btnAddVehicle);
        buttonPanel.add(btnRefresh);

        panel.add(buttonPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(
            new Object[]{"Plate No", "Type", "Capacity", "Driver", "Driver Phone", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        transportTable = new JTable(tableModel);
        transportTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        transportTable.setRowHeight(25);
        transportTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(transportTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createRoutesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(new JLabel("Route Management - Coming Soon", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAssignmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(new JLabel("Student Assignments - Coming Soon", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }

    private void loadTransportData() {
        try {
            tableModel.setRowCount(0);
            
            ResultSet rs = MySQL.executeSearch("SELECT v.plate_no, vt.type_name, v.capacity, v.driver_name, v.driver_phone, vs.status_name " +
                          "FROM vehicle v " +
                          "JOIN vehicle_type vt ON v.vehicle_type_id = vt.vehicle_type_id " +
                          "JOIN vehicle_status vs ON v.vehicle_status_id = vs.vehicle_status_id " +
                          "ORDER BY v.plate_no");
            
            while (rs.next()) {
                String plateNo = rs.getString("plate_no");
                String type = rs.getString("type_name");
                int capacity = rs.getInt("capacity");
                String driver = rs.getString("driver_name");
                String driverPhone = rs.getString("driver_phone");
                String status = rs.getString("status_name");
                
                tableModel.addRow(new Object[]{plateNo, type, capacity, driver, driverPhone, status});
            }
            
            rs.close();
            
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading transport data: " + e.getMessage());
        }
    }

    private void searchTransport() {
        // Simplified search implementation
        loadTransportData();
    }

    private void addVehicle() {
        JOptionIcon.showInfoDialog(this, "Add Vehicle - Coming Soon");
    }
}