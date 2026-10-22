package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;
import lk.educore.schoolms.charts.PieChart;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

/**
 * Finance Management Panel Income/expense tracking, salary processing, fee
 * collection analytics, and pie chart
 */
public class FinanceManagement extends JPanel {

    private JTable financeTable;
    private DefaultTableModel tableModel;
    private JButton btnAddTransaction, btnRefresh, btnViewSummary;
    private JPanel chartPanel;

    public FinanceManagement() {
        initComponents();
        loadFinanceData();
        loadFinanceChart();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Finance Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));

        btnAddTransaction = new JButton("Add Transaction");
        btnAddTransaction.addActionListener(e -> addTransaction());

        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> {
            loadFinanceData();
            loadFinanceChart();
        });

        btnViewSummary = new JButton("View Summary");
        btnViewSummary.addActionListener(e -> viewSummary());

        buttonPanel.add(btnAddTransaction);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnViewSummary);

        headerPanel.add(buttonPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Split pane for table and chart
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));

        tableModel = new DefaultTableModel(
                new Object[]{"Transaction ID", "Description", "Type", "Category", "Amount", "Reference No.", "Date"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        financeTable = new JTable(tableModel);
        financeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        financeTable.setRowHeight(25);
        financeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(financeTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Chart panel
        chartPanel = new JPanel(new BorderLayout(10, 10));
        chartPanel.setBorder(BorderFactory.createTitledBorder("Financial Overview"));

        splitPane.setTopComponent(tablePanel);
        splitPane.setBottomComponent(chartPanel);
        splitPane.setResizeWeight(0.65);

        add(splitPane, BorderLayout.CENTER);
    }

    private void loadFinanceData() {
        try {
            tableModel.setRowCount(0);

            ResultSet rs = MySQL.executeSearch("SELECT f.finance_id, f.description, tt.type_name, f.category, f.amount, f.reference_no, f.transaction_date "
                    + "FROM finance f "
                    + "JOIN transaction_type tt ON f.transaction_type_id = tt.transaction_type_id "
                    + "ORDER BY f.transaction_date DESC");

            DecimalFormat df = new DecimalFormat("'Rs.' #,##0.00");

            while (rs.next()) {
                int financeId = rs.getInt("finance_id");
                String description = rs.getString("description");
                String type = rs.getString("type_name");
                String category = rs.getString("category");
                double amount = rs.getDouble("amount");
                String refNo = rs.getString("reference_no");
                String date = rs.getString("transaction_date");

                tableModel.addRow(new Object[]{financeId, description, type, category, df.format(amount), refNo, date});
            }

            rs.close();

        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading finance data: " + e.getMessage());
        }
    }

    private void loadFinanceChart() {
        chartPanel.removeAll();
        try {
            double income = 0.0;
            double expense = 0.0;

            ResultSet rs = MySQL.executeSearch(
                    "SELECT tt.type_name, SUM(f.amount) as total "
                    + "FROM finance f "
                    + "JOIN transaction_type tt ON f.transaction_type_id = tt.transaction_type_id "
                    + "GROUP BY tt.type_name"
            );

            while (rs.next()) {
                String type = rs.getString("type_name");
                double total = rs.getDouble("total");
                if ("Income".equalsIgnoreCase(type)) {
                    income = total;
                } else if ("Expense".equalsIgnoreCase(type)) {
                    expense = total;
                }
            }
            rs.close();

            if (income == 0 && expense == 0) {
                JLabel noDataLabel = new JLabel("No financial data available to display chart.", SwingConstants.CENTER);
                noDataLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                noDataLabel.setForeground(Color.GRAY);
                chartPanel.add(noDataLabel, BorderLayout.CENTER);
            } else {
                JPanel chart = PieChart.createFinancePieChart(income, expense);
                chartPanel.add(chart, BorderLayout.CENTER);

                DecimalFormat df = new DecimalFormat("'Rs.' #,##0.00");
                JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
                JLabel incomeLabel = new JLabel("Total Income: " + df.format(income));
                incomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
                incomeLabel.setForeground(new Color(76, 175, 80));
                JLabel expenseLabel = new JLabel("Total Expense: " + df.format(expense));
                expenseLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
                expenseLabel.setForeground(new Color(244, 67, 54));
                double net = lk.educore.schoolms.service.FeeService.calculateNetFinance(income, expense);
                JLabel netLabel = new JLabel("Net: " + df.format(net));
                netLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
                netLabel.setForeground(net >= 0 ? new Color(33, 150, 243) : new Color(244, 67, 54));
                legendPanel.add(incomeLabel);
                legendPanel.add(expenseLabel);
                legendPanel.add(netLabel);
                chartPanel.add(legendPanel, BorderLayout.SOUTH);
            }

        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Chart unavailable: " + e.getMessage(), SwingConstants.CENTER);
            errorLabel.setForeground(Color.RED);
            chartPanel.add(errorLabel, BorderLayout.CENTER);
        }

        chartPanel.revalidate();
        chartPanel.repaint();
    }

    private void addTransaction() {
        // Build a proper JDialog 
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentFrame, "Add Finance Transaction", true);

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
        JTextField txtDescription = new JTextField(20);
        JComboBox<String> cmbType = new JComboBox<>(new String[]{"Income", "Expense"});
        JTextField txtCategory = new JTextField(20);
        JTextField txtAmount = new JTextField(20);
        JTextField txtRefNo = new JTextField(20);
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setDate(new Date());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Description: *"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtDescription, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Transaction Type: *"), gbc);
        gbc.gridx = 1;
        formPanel.add(cmbType, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtCategory, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Amount (Rs.): *"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtAmount, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Reference No.:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtRefNo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Transaction Date: *"), gbc);
        gbc.gridx = 1;
        formPanel.add(dateChooser, gbc);

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
        dialog.setMinimumSize(new Dimension(380, dialog.getHeight()));
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);

        // Process after dialog closes
        if (!confirmed[0]) {
            return;
        }

        String description = txtDescription.getText().trim();
        String type = (String) cmbType.getSelectedItem();
        String category = txtCategory.getText().trim();
        String amountStr = txtAmount.getText().trim();
        String refNo = txtRefNo.getText().trim();
        Date date = dateChooser.getDate();

        // Validation
        if (description.isEmpty()) {
            JOptionIcon.showWarningDialog(this, "Description is required.");
            return;
        }
        if (amountStr.isEmpty()) {
            JOptionIcon.showWarningDialog(this, "Amount is required.");
            return;
        }
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionIcon.showWarningDialog(this, "Please enter a valid positive amount.");
            return;
        }
        if (date == null) {
            JOptionIcon.showWarningDialog(this, "Transaction date is required.");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(date);

        try {
            ResultSet rs = MySQL.executeSearch(
                    "SELECT transaction_type_id FROM transaction_type WHERE type_name = '" + type + "'"
            );
            if (!rs.next()) {
                JOptionIcon.showErrorDialog(this, "Transaction type not found.");
                rs.close();
                return;
            }
            int typeId = rs.getInt("transaction_type_id");
            rs.close();

            String categoryVal = category.isEmpty() ? "NULL" : "'" + category.replace("'", "''") + "'";
            String refNoVal = refNo.isEmpty() ? "NULL" : "'" + refNo.replace("'", "''") + "'";

            String sql = "INSERT INTO finance (description, amount, transaction_type_id, transaction_date, category, reference_no) VALUES ('"
                    + description.replace("'", "''") + "', " + amount + ", " + typeId + ", '" + dateStr + "', "
                    + categoryVal + ", " + refNoVal + ")";

            int rows = MySQL.executeIUD(sql);

            if (rows > 0) {
                loadFinanceData();
                loadFinanceChart();
                JOptionIcon.showSuccessDialog(this, "Transaction added successfully!");
            } else {
                JOptionIcon.showErrorDialog(this, "Failed to add transaction.");
            }

        } catch (Exception ex) {
            JOptionIcon.showErrorDialog(this, "Error adding transaction: " + ex.getMessage());
        }
    }

    private void viewSummary() {
        try {
            // Fetch summary stats
            double totalIncome = 0.0;
            double totalExpense = 0.0;

            ResultSet rs = MySQL.executeSearch(
                    "SELECT tt.type_name, SUM(f.amount) as total, COUNT(*) as count "
                    + "FROM finance f "
                    + "JOIN transaction_type tt ON f.transaction_type_id = tt.transaction_type_id "
                    + "GROUP BY tt.type_name"
            );

            int incomeCount = 0, expenseCount = 0;
            while (rs.next()) {
                String type = rs.getString("type_name");
                double total = rs.getDouble("total");
                int count = rs.getInt("count");
                if ("Income".equalsIgnoreCase(type)) {
                    totalIncome = total;
                    incomeCount = count;
                } else if ("Expense".equalsIgnoreCase(type)) {
                    totalExpense = total;
                    expenseCount = count;
                }
            }
            rs.close();

            // Fetch category breakdown
            ResultSet catRs = MySQL.executeSearch(
                    "SELECT f.category, tt.type_name, SUM(f.amount) as total, COUNT(*) as count "
                    + "FROM finance f "
                    + "JOIN transaction_type tt ON f.transaction_type_id = tt.transaction_type_id "
                    + "WHERE f.category IS NOT NULL "
                    + "GROUP BY f.category, tt.type_name "
                    + "ORDER BY total DESC"
            );

            // Build category breakdown table
            DefaultTableModel catModel = new DefaultTableModel(
                    new Object[]{"Category", "Type", "Transactions", "Total Amount"}, 0
            ) {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };

            DecimalFormat df = new DecimalFormat("'Rs.' #,##0.00");
            while (catRs.next()) {
                String cat = catRs.getString("category");
                String type = catRs.getString("type_name");
                int count = catRs.getInt("count");
                double total = catRs.getDouble("total");
                catModel.addRow(new Object[]{cat, type, count, df.format(total)});
            }
            catRs.close();

            double net = totalIncome - totalExpense;

            // Build a proper JDialog with icon
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
            JDialog dialog = new JDialog(parentFrame, "Financial Summary", true);

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

            // Build summary panel
            JPanel summaryPanel = new JPanel(new BorderLayout(10, 10));
            summaryPanel.setPreferredSize(new Dimension(550, 420));

            // Stats cards
            JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
            statsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));

            statsPanel.add(createSummaryCard("Total Income", df.format(totalIncome),
                    incomeCount + " transactions", new Color(76, 175, 80)));
            statsPanel.add(createSummaryCard("Total Expense", df.format(totalExpense),
                    expenseCount + " transactions", new Color(244, 67, 54)));
            statsPanel.add(createSummaryCard("Net Balance", df.format(net),
                    net >= 0 ? "Surplus" : "Deficit",
                    net >= 0 ? new Color(33, 150, 243) : new Color(244, 67, 54)));

            summaryPanel.add(statsPanel, BorderLayout.NORTH);

            // Category breakdown
            JTable catTable = new JTable(catModel);
            catTable.setRowHeight(24);
            catTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
            JScrollPane catScroll = new JScrollPane(catTable);
            catScroll.setBorder(BorderFactory.createTitledBorder("Category Breakdown"));

            summaryPanel.add(catScroll, BorderLayout.CENTER);

            // Close button
            JButton btnClose = new JButton("Close");
            btnClose.addActionListener(e -> dialog.dispose());

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
            btnPanel.add(btnClose);

            dialog.setLayout(new BorderLayout());
            dialog.add(summaryPanel, BorderLayout.CENTER);
            dialog.add(btnPanel, BorderLayout.SOUTH);
            dialog.pack();
            dialog.setMinimumSize(new Dimension(570, dialog.getHeight()));
            dialog.setLocationRelativeTo(parentFrame);
            dialog.setVisible(true);

        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading financial summary: " + e.getMessage());
        }
    }

    private JPanel createSummaryCard(String title, String value, String subtitle, Color color) {
        JPanel card = new JPanel(new BorderLayout(3, 3));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));

        JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
        titleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        titleLbl.setForeground(new Color(80, 80, 80));

        JLabel valueLbl = new JLabel(value, SwingConstants.CENTER);
        valueLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valueLbl.setForeground(color);

        JLabel subtitleLbl = new JLabel(subtitle, SwingConstants.CENTER);
        subtitleLbl.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        subtitleLbl.setForeground(Color.GRAY);

        card.add(titleLbl, BorderLayout.NORTH);
        card.add(valueLbl, BorderLayout.CENTER);
        card.add(subtitleLbl, BorderLayout.SOUTH);

        return card;
    }
}
