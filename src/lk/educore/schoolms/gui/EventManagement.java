package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Event Management Panel
 * School events CRUD, notice board, and status tracking
 */
public class EventManagement extends JPanel {

    private JTable eventTable;
    private DefaultTableModel tableModel;
    private JButton btnAddEvent, btnRefresh;
    private JTextField txtSearch;

    public EventManagement() {
        initComponents();
        loadEventData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        
        JLabel titleLabel = new JLabel("Event Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Search and button panel
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        
        txtSearch = new JTextField(20);
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchEvents();
            }
        });
        
        btnAddEvent = new JButton("Add Event");
        btnAddEvent.addActionListener(e -> addEvent());
        
        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadEventData());
        
        rightPanel.add(new JLabel("Search:"));
        rightPanel.add(txtSearch);
        rightPanel.add(btnAddEvent);
        rightPanel.add(btnRefresh);
        
        headerPanel.add(rightPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(
            new Object[]{"Event ID", "Event Name", "Start Date", "End Date", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        eventTable = new JTable(tableModel);
        eventTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eventTable.setRowHeight(25);
        eventTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(eventTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadEventData() {
        try {
            tableModel.setRowCount(0);
            
            ResultSet rs = MySQL.executeSearch("SELECT e.event_id, e.event_name, e.start_date, e.end_date, es.status_name " +
                          "FROM event e " +
                          "JOIN event_status es ON e.event_status_id = es.event_status_id " +
                          "ORDER BY e.start_date DESC");
            
            while (rs.next()) {
                int eventId = rs.getInt("event_id");
                String eventName = rs.getString("event_name");
                String startDate = rs.getString("start_date");
                String endDate = rs.getString("end_date");
                String status = rs.getString("status_name");
                
                tableModel.addRow(new Object[]{eventId, eventName, startDate, endDate, status});
            }
            
            rs.close();
            
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading event data: " + e.getMessage());
        }
    }

    private void searchEvents() {
        loadEventData();
    }

    private void addEvent() {
        JOptionIcon.showInfoDialog(this, "Add Event - Coming Soon");
    }
}