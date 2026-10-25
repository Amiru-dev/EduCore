package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.model.UserSession;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

/**
 * Event Management Panel
 * School events CRUD, notice board, and status tracking
 */
public class EventManagement extends JPanel {

    private JTable eventTable;
    private DefaultTableModel tableModel;
    private JButton btnAddEvent, btnEditEvent, btnDeleteEvent, btnRefresh;
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
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 10));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        txtSearch = new JTextField(20);
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchEvents();
            }
        });
        
        btnAddEvent = new JButton("Add Event");
        btnAddEvent.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAddEvent.addActionListener(e -> addEvent());
        applyPrimaryButtonEffect(btnAddEvent);

        btnEditEvent = new JButton("Edit Event");
        btnEditEvent.setEnabled(false);
        btnEditEvent.addActionListener(e -> editEvent());
        applySecondaryButtonEffect(btnEditEvent);

        btnDeleteEvent = new JButton("Delete Event");
        btnDeleteEvent.setEnabled(false);
        btnDeleteEvent.addActionListener(e -> deleteEvent());
        applyDestructiveButtonEffect(btnDeleteEvent);

        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadEventData());
        applySecondaryButtonEffect(btnRefresh);

        rightPanel.add(new JLabel("Search:"));
        rightPanel.add(txtSearch);
        rightPanel.add(btnAddEvent);
        rightPanel.add(btnEditEvent);
        rightPanel.add(btnDeleteEvent);
        rightPanel.add(btnRefresh);

        if (UserSession.get() != null && UserSession.get().isTeacher()) {
            btnAddEvent.setVisible(false);
            btnEditEvent.setVisible(false);
            btnDeleteEvent.setVisible(false);
        }
        
        headerPanel.add(rightPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(
            new Object[]{"Event ID", "Event Name", "Description", "Location", "Start Date", "End Date", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        eventTable = new JTable(tableModel);
        eventTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eventTable.setRowHeight(30);
        eventTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        eventTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Add selection listener to enable/disable buttons
        eventTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelection = eventTable.getSelectedRow() != -1;
                btnEditEvent.setEnabled(hasSelection);
                btnDeleteEvent.setEnabled(hasSelection);
            }
        });

        JScrollPane scrollPane = new JScrollPane(eventTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadEventData() {
        try {
            tableModel.setRowCount(0);

            ResultSet rs = MySQL.executeSearch("SELECT e.event_id, e.event_name, e.description, e.location, e.start_date, e.end_date, es.status_name " +
                          "FROM event e " +
                          "JOIN event_status es ON e.event_status_id = es.event_status_id " +
                          "ORDER BY e.start_date DESC");

            while (rs.next()) {
                int eventId = rs.getInt("event_id");
                String eventName = rs.getString("event_name");
                String description = rs.getString("description");
                String location = rs.getString("location");
                String startDate = rs.getString("start_date");
                String endDate = rs.getString("end_date");
                String status = rs.getString("status_name");

                tableModel.addRow(new Object[]{eventId, eventName, description, location, startDate, endDate, status});
            }

            rs.close();

        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading event data: " + e.getMessage());
        }
    }

    private void searchEvents() {
        String searchTerm = txtSearch.getText().trim();
        try {
            tableModel.setRowCount(0);

            String query = "SELECT e.event_id, e.event_name, e.description, e.location, e.start_date, e.end_date, es.status_name " +
                          "FROM event e " +
                          "JOIN event_status es ON e.event_status_id = es.event_status_id ";

            if (!searchTerm.isEmpty()) {
                query += "WHERE e.event_name LIKE '%" + searchTerm.replace("'", "''") + "%' " +
                        "OR e.description LIKE '%" + searchTerm.replace("'", "''") + "%' " +
                        "OR e.location LIKE '%" + searchTerm.replace("'", "''") + "%' ";
            }

            query += "ORDER BY e.start_date DESC";

            ResultSet rs = MySQL.executeSearch(query);

            while (rs.next()) {
                int eventId = rs.getInt("event_id");
                String eventName = rs.getString("event_name");
                String description = rs.getString("description");
                String location = rs.getString("location");
                String startDate = rs.getString("start_date");
                String endDate = rs.getString("end_date");
                String status = rs.getString("status_name");

                tableModel.addRow(new Object[]{eventId, eventName, description, location, startDate, endDate, status});
            }

            rs.close();

        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error searching events: " + e.getMessage());
        }
    }

    private void addEvent() {
        if (UserSession.get() != null && UserSession.get().isTeacher()) {
            JOptionIcon.showWarningDialog(this, "Access denied: Teachers have view-only access to Events.");
            return;
        }

        // Build a proper JDialog
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentFrame, "Add Event", true);

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
        JTextField txtEventName = new JTextField(20);
        JTextArea txtDescription = new JTextArea(3, 20);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(txtDescription);
        JTextField txtLocation = new JTextField(20);
        JComboBox<String> cmbStatus = new JComboBox<>(new String[]{"Active", "Completed", "Cancelled", "Postponed"});
        com.toedter.calendar.JDateChooser startDateChooser = new com.toedter.calendar.JDateChooser();
        startDateChooser.setDateFormatString("yyyy-MM-dd HH:mm:ss");
        startDateChooser.setDate(new Date());
        com.toedter.calendar.JDateChooser endDateChooser = new com.toedter.calendar.JDateChooser();
        endDateChooser.setDateFormatString("yyyy-MM-dd HH:mm:ss");

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Event Name: *"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtEventName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(descScroll, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtLocation, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Start Date: *"), gbc);
        gbc.gridx = 1;
        formPanel.add(startDateChooser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("End Date:"), gbc);
        gbc.gridx = 1;
        formPanel.add(endDateChooser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Status: *"), gbc);
        gbc.gridx = 1;
        formPanel.add(cmbStatus, gbc);

        JLabel noteLabel = new JLabel("* Required fields");
        noteLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        noteLabel.setForeground(Color.GRAY);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        formPanel.add(noteLabel, gbc);

        // Buttons
        boolean[] confirmed = {false};
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        btnSave.addActionListener(ev -> {
            String eventName = txtEventName.getText().trim();
            Date startDate = startDateChooser.getDate();
            Date endDate = endDateChooser.getDate();

            if (eventName.isEmpty()) {
                JOptionIcon.showWarningDialog(dialog, "Event name is required.");
                return;
            }
            if (startDate == null) {
                JOptionIcon.showWarningDialog(dialog, "Start date is required.");
                return;
            }
            if (endDate != null && endDate.before(startDate)) {
                JOptionIcon.showWarningDialog(dialog, "End date cannot come before start date.");
                return;
            }

            confirmed[0] = true;
            dialog.dispose();
        });
        btnCancel.addActionListener(ev -> dialog.dispose());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);

        dialog.setLayout(new BorderLayout());
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setMinimumSize(new Dimension(400, dialog.getHeight()));
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);

        // Process after dialog closes
        if (!confirmed[0]) {
            return;
        }

        String eventName = txtEventName.getText().trim();
        String description = txtDescription.getText().trim();
        String location = txtLocation.getText().trim();
        String status = (String) cmbStatus.getSelectedItem();
        Date startDate = startDateChooser.getDate();
        Date endDate = endDateChooser.getDate();

        // Validation
        if (eventName.isEmpty()) {
            JOptionIcon.showWarningDialog(this, "Event name is required.");
            return;
        }
        if (startDate == null) {
            JOptionIcon.showWarningDialog(this, "Start date is required.");
            return;
        }
        if (endDate != null && endDate.before(startDate)) {
            JOptionIcon.showWarningDialog(this, "End date cannot come before start date.");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startDateStr = sdf.format(startDate);
        String endDateStr = endDate != null ? "'" + sdf.format(endDate) + "'" : "NULL";

        try {
            ResultSet rs = MySQL.executeSearch(
                    "SELECT event_status_id FROM event_status WHERE status_name = '" + status + "'"
            );
            if (!rs.next()) {
                JOptionIcon.showErrorDialog(this, "Event status not found.");
                rs.close();
                return;
            }
            int statusId = rs.getInt("event_status_id");
            rs.close();

            String descVal = description.isEmpty() ? "NULL" : "'" + description.replace("'", "''") + "'";
            String locVal = location.isEmpty() ? "NULL" : "'" + location.replace("'", "''") + "'";

            String sql = "INSERT INTO event (event_name, description, location, start_date, end_date, event_status_id, created_by) VALUES ('"
                    + eventName.replace("'", "''") + "', " + descVal + ", " + locVal + ", '" + startDateStr + "', "
                    + endDateStr + ", " + statusId + ", 'admin')";

            int rows = MySQL.executeIUD(sql);

            if (rows > 0) {
                loadEventData();
                JOptionIcon.showSuccessDialog(this, "Event added successfully!");
            } else {
                JOptionIcon.showErrorDialog(this, "Failed to add event.");
            }

        } catch (Exception ex) {
            JOptionIcon.showErrorDialog(this, "Error adding event: " + ex.getMessage());
        }
    }

    private void editEvent() {
        if (UserSession.get() != null && UserSession.get().isTeacher()) {
            JOptionIcon.showWarningDialog(this, "Access denied: Teachers have view-only access to Events.");
            return;
        }

        int selectedRow = eventTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select an event to edit.");
            return;
        }

        int eventId = (int) tableModel.getValueAt(selectedRow, 0);
        String eventName = (String) tableModel.getValueAt(selectedRow, 1);
        String description = (String) tableModel.getValueAt(selectedRow, 2);
        String location = (String) tableModel.getValueAt(selectedRow, 3);
        String startDate = (String) tableModel.getValueAt(selectedRow, 4);
        String endDate = (String) tableModel.getValueAt(selectedRow, 5);
        String status = (String) tableModel.getValueAt(selectedRow, 6);

        // Build a proper JDialog
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentFrame, "Edit Event", true);

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
        JTextField txtEventName = new JTextField(eventName, 20);
        JTextArea txtDescription = new JTextArea(description, 3, 20);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(txtDescription);
        JTextField txtLocation = new JTextField(location, 20);
        JComboBox<String> cmbStatus = new JComboBox<>(new String[]{"Active", "Completed", "Cancelled", "Postponed"});
        cmbStatus.setSelectedItem(status);
        com.toedter.calendar.JDateChooser startDateChooser = new com.toedter.calendar.JDateChooser();
        startDateChooser.setDateFormatString("yyyy-MM-dd HH:mm:ss");
        try {
            startDateChooser.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDate));
        } catch (Exception e) {
            startDateChooser.setDate(new Date());
        }
        com.toedter.calendar.JDateChooser endDateChooser = new com.toedter.calendar.JDateChooser();
        endDateChooser.setDateFormatString("yyyy-MM-dd HH:mm:ss");
        if (endDate != null && !endDate.equals("null")) {
            try {
                endDateChooser.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate));
            } catch (Exception e) {
                // Ignore parse error
            }
        }

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Event Name: *"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtEventName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(descScroll, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtLocation, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Start Date: *"), gbc);
        gbc.gridx = 1;
        formPanel.add(startDateChooser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("End Date:"), gbc);
        gbc.gridx = 1;
        formPanel.add(endDateChooser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Status: *"), gbc);
        gbc.gridx = 1;
        formPanel.add(cmbStatus, gbc);

        JLabel noteLabel = new JLabel("* Required fields");
        noteLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        noteLabel.setForeground(Color.GRAY);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        formPanel.add(noteLabel, gbc);

        // Buttons
        boolean[] confirmed = {false};
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        btnSave.addActionListener(ev -> {
            String newEventName = txtEventName.getText().trim();
            Date newStartDate = startDateChooser.getDate();
            Date newEndDate = endDateChooser.getDate();

            if (newEventName.isEmpty()) {
                JOptionIcon.showWarningDialog(dialog, "Event name is required.");
                return;
            }
            if (newStartDate == null) {
                JOptionIcon.showWarningDialog(dialog, "Start date is required.");
                return;
            }
            if (newEndDate != null && newEndDate.before(newStartDate)) {
                JOptionIcon.showWarningDialog(dialog, "End date cannot come before start date.");
                return;
            }

            confirmed[0] = true;
            dialog.dispose();
        });
        btnCancel.addActionListener(ev -> dialog.dispose());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);

        dialog.setLayout(new BorderLayout());
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setMinimumSize(new Dimension(400, dialog.getHeight()));
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);

        // Process after dialog closes
        if (!confirmed[0]) {
            return;
        }

        String newEventName = txtEventName.getText().trim();
        String newDescription = txtDescription.getText().trim();
        String newLocation = txtLocation.getText().trim();
        String newStatus = (String) cmbStatus.getSelectedItem();
        Date newStartDate = startDateChooser.getDate();
        Date newEndDate = endDateChooser.getDate();

        // Validation
        if (newEventName.isEmpty()) {
            JOptionIcon.showWarningDialog(this, "Event name is required.");
            return;
        }
        if (newStartDate == null) {
            JOptionIcon.showWarningDialog(this, "Start date is required.");
            return;
        }
        if (newEndDate != null && newEndDate.before(newStartDate)) {
            JOptionIcon.showWarningDialog(this, "End date cannot come before start date.");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startDateStr = sdf.format(newStartDate);
        String endDateStr = newEndDate != null ? "'" + sdf.format(newEndDate) + "'" : "NULL";

        try {
            ResultSet rs = MySQL.executeSearch(
                    "SELECT event_status_id FROM event_status WHERE status_name = '" + newStatus + "'"
            );
            if (!rs.next()) {
                JOptionIcon.showErrorDialog(this, "Event status not found.");
                rs.close();
                return;
            }
            int statusId = rs.getInt("event_status_id");
            rs.close();

            String descVal = newDescription.isEmpty() ? "NULL" : "'" + newDescription.replace("'", "''") + "'";
            String locVal = newLocation.isEmpty() ? "NULL" : "'" + newLocation.replace("'", "''") + "'";

            String sql = "UPDATE event SET event_name = '" + newEventName.replace("'", "''") + "', "
                    + "description = " + descVal + ", "
                    + "location = " + locVal + ", "
                    + "start_date = '" + startDateStr + "', "
                    + "end_date = " + endDateStr + ", "
                    + "event_status_id = " + statusId + " "
                    + "WHERE event_id = " + eventId;

            int rows = MySQL.executeIUD(sql);

            if (rows > 0) {
                loadEventData();
                JOptionIcon.showSuccessDialog(this, "Event updated successfully!");
            } else {
                JOptionIcon.showErrorDialog(this, "Failed to update event.");
            }

        } catch (Exception ex) {
            JOptionIcon.showErrorDialog(this, "Error updating event: " + ex.getMessage());
        }
    }

    private void deleteEvent() {
        if (UserSession.get() != null && UserSession.get().isTeacher()) {
            JOptionIcon.showWarningDialog(this, "Access denied: Teachers have view-only access to Events.");
            return;
        }

        int selectedRow = eventTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select an event to delete.");
            return;
        }

        int eventId = (int) tableModel.getValueAt(selectedRow, 0);
        String eventName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete the event \"" + eventName + "\"?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM event WHERE event_id = " + eventId;
                int rows = MySQL.executeIUD(sql);

                if (rows > 0) {
                    loadEventData();
                    JOptionIcon.showSuccessDialog(this, "Event deleted successfully!");
                } else {
                    JOptionIcon.showErrorDialog(this, "Failed to delete event.");
                }
            } catch (Exception ex) {
                JOptionIcon.showErrorDialog(this, "Error deleting event: " + ex.getMessage());
            }
        }
    }

    private void applyPrimaryButtonEffect(JButton button) {
        button.setBackground(new Color(37, 99, 235));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(new Color(29, 78, 216));
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(new Color(37, 99, 235));
                }
            }
        });
    }

    private void applySecondaryButtonEffect(JButton button) {
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(71, 85, 105));
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225), 1));
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(new Color(248, 250, 252));
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(148, 163, 184), 1),
                        BorderFactory.createEmptyBorder(8, 16, 8, 16)
                    ));
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(Color.WHITE);
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                        BorderFactory.createEmptyBorder(8, 16, 8, 16)
                    ));
                }
            }
        });
    }

    private void applyDestructiveButtonEffect(JButton button) {
        button.setBackground(new Color(220, 38, 38));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(new Color(185, 28, 28));
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(new Color(220, 38, 38));
                }
            }
        });
    }
}