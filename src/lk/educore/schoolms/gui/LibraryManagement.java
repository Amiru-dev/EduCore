package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.BarcodeGenerator;
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
 * Library Management Panel
 * Full implementation: Book CRUD, Issue/Return tracking, Inventory overview, Barcode generation
 */
public class LibraryManagement extends JPanel {

    // Books Tab
    private JTable bookTable;
    private DefaultTableModel bookTableModel;
    private JTextField txtSearch;
    private JButton btnAddBook, btnEditBook, btnDeleteBook, btnBarcode;

    // Issue/Return Tab
    private JTable issueTable;
    private DefaultTableModel issueTableModel;
    private JTextField txtIssueSearch;
    private JButton btnIssue, btnReturn, btnUpdateOverdue;

    // Inventory Tab
    private JTable inventoryTable;
    private DefaultTableModel inventoryTableModel;
    private JLabel lblTotalBooks, lblAvailableBooks, lblIssuedBooks, lblOverdueBooks;

    private JTabbedPane tabbedPane;

    public LibraryManagement() {
        initComponents();
        loadBookData();
        loadIssueData();
        loadInventoryData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Tabbed pane
        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Books", createBooksPanel());
        tabbedPane.addTab("Issue / Return", createIssuePanel());
        tabbedPane.addTab("Inventory", createInventoryPanel());

        // Refresh data on tab switch
        tabbedPane.addChangeListener(e -> {
            int idx = tabbedPane.getSelectedIndex();
            if (idx == 0) loadBookData();
            else if (idx == 1) loadIssueData();
            else if (idx == 2) loadInventoryData();
        });

        add(tabbedPane, BorderLayout.CENTER);
    }

    // =====================================================================
    // HEADER
    // =====================================================================

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        // Title - Change to "Library" for teachers
        String titleText = "Library Management";
        if (UserSession.get() != null && UserSession.get().isTeacher()) {
            titleText = "Library";
        }
        JLabel titleLabel = new JLabel(titleText);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.WEST);

        return panel;
    }

    // =====================================================================
    // BOOKS TAB
    // =====================================================================

    private JPanel createBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Top bar: search + buttons
        JPanel topBar = new JPanel(new BorderLayout(5, 5));
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        // Search
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        txtSearch = new JTextField(20);
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchBooks();
            }
        });
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> searchBooks());
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        topBar.add(searchPanel, BorderLayout.WEST);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        btnAddBook = new JButton("Add Book");
        btnAddBook.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAddBook.addActionListener(e -> addBook());
        applyPrimaryButtonEffect(btnAddBook);
        btnEditBook = new JButton("Edit Book");
        btnEditBook.setEnabled(false);
        btnEditBook.addActionListener(e -> editBook());
        applySecondaryButtonEffect(btnEditBook);
        btnDeleteBook = new JButton("Delete Book");
        btnDeleteBook.setEnabled(false);
        btnDeleteBook.addActionListener(e -> deleteBook());
        applyDestructiveButtonEffect(btnDeleteBook);
        btnBarcode = new JButton("Generate Barcode");
        btnBarcode.setEnabled(false);
        btnBarcode.addActionListener(e -> generateBarcode());
        applySecondaryButtonEffect(btnBarcode);
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadBookData());
        applySecondaryButtonEffect(btnRefresh);
        
        buttonPanel.add(btnAddBook);
        buttonPanel.add(btnEditBook);
        buttonPanel.add(btnDeleteBook);
        buttonPanel.add(btnBarcode);
        buttonPanel.add(btnRefresh);

        // Hide Add, Edit, Delete, and Barcode buttons for teachers
        if (UserSession.get() != null && UserSession.get().isTeacher()) {
            btnAddBook.setVisible(false);
            btnEditBook.setVisible(false);
            btnDeleteBook.setVisible(false);
            btnBarcode.setVisible(false);
        }
        if (UserSession.get() != null && UserSession.get().isTeacher()) {
            btnAddBook.setVisible(false);
            btnEditBook.setVisible(false);
            btnDeleteBook.setVisible(false);
            btnBarcode.setVisible(false);
        }

        topBar.add(buttonPanel, BorderLayout.EAST);

        panel.add(topBar, BorderLayout.NORTH);

        // Table
        bookTableModel = new DefaultTableModel(
            new Object[]{"Book ID", "ISBN", "Title", "Author", "Category", "Publisher", "Year", "Available", "Total", "Location"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookTable = new JTable(bookTableModel);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.setRowHeight(30);
        bookTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        bookTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Add selection listener to enable/disable buttons
        bookTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelection = bookTable.getSelectedRow() != -1;
                btnEditBook.setEnabled(hasSelection);
                btnDeleteBook.setEnabled(hasSelection);
                btnBarcode.setEnabled(hasSelection);
            }
        });
        
        // Hide Book ID column
        bookTable.getColumnModel().getColumn(0).setMinWidth(0);
        bookTable.getColumnModel().getColumn(0).setMaxWidth(0);
        bookTable.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(bookTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadBookData() {
        try {
            bookTableModel.setRowCount(0);

            ResultSet rs = MySQL.executeSearch(
                "SELECT b.book_id, b.isbn, b.title, b.author, c.category_name, b.publisher, b.publication_year, " +
                "b.available_qty, b.quantity, b.location " +
                "FROM book b " +
                "JOIN book_category c ON b.category_id = c.category_id " +
                "ORDER BY b.title"
            );

            while (rs.next()) {
                bookTableModel.addRow(new Object[]{
                    rs.getInt("book_id"),
                    rs.getString("isbn"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("category_name"),
                    rs.getString("publisher"),
                    rs.getInt("publication_year"),
                    rs.getInt("available_qty"),
                    rs.getInt("quantity"),
                    rs.getString("location")
                });
            }
            rs.close();

        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading book data: " + e.getMessage());
        }
    }

    private void searchBooks() {
        String term = txtSearch.getText().trim();
        try {
            bookTableModel.setRowCount(0);

            String query = "SELECT b.book_id, b.isbn, b.title, b.author, c.category_name, b.publisher, b.publication_year, " +
                           "b.available_qty, b.quantity, b.location " +
                           "FROM book b " +
                           "JOIN book_category c ON b.category_id = c.category_id ";

            if (!term.isEmpty()) {
                String safe = term.replace("'", "''");
                query += "WHERE b.title LIKE '%" + safe + "%' " +
                         "OR b.author LIKE '%" + safe + "%' " +
                         "OR b.isbn LIKE '%" + safe + "%' " +
                         "OR c.category_name LIKE '%" + safe + "%' ";
            }
            query += "ORDER BY b.title";

            ResultSet rs = MySQL.executeSearch(query);
            while (rs.next()) {
                bookTableModel.addRow(new Object[]{
                    rs.getInt("book_id"),
                    rs.getString("isbn"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("category_name"),
                    rs.getString("publisher"),
                    rs.getInt("publication_year"),
                    rs.getInt("available_qty"),
                    rs.getInt("quantity"),
                    rs.getString("location")
                });
            }
            rs.close();

        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error searching books: " + e.getMessage());
        }
    }

    private void addBook() {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentFrame, "Add Book", true);
        setDialogIcon(dialog);

        JTextField txtTitle = new JTextField(22);
        JTextField txtAuthor = new JTextField(22);
        JTextField txtISBN = new JTextField(22);
        JComboBox<String> cmbCategory = new JComboBox<>();
        JTextField txtPublisher = new JTextField(22);
        JTextField txtYear = new JTextField(22);
        JSpinner spnQty = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));
        JTextField txtPrice = new JTextField(22);
        JTextField txtLocation = new JTextField(22);

        loadCategoriesToCombo(cmbCategory);

        JPanel formPanel = buildFormPanel(new Object[][]{
            {"Title: *", txtTitle},
            {"Author: *", txtAuthor},
            {"ISBN:", txtISBN},
            {"Category: *", cmbCategory},
            {"Publisher:", txtPublisher},
            {"Publication Year:", txtYear},
            {"Quantity: *", spnQty},
            {"Price (LKR):", txtPrice},
            {"Shelf Location:", txtLocation}
        });

        boolean[] confirmed = {false};
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        btnSave.addActionListener(ev -> {
            if (txtTitle.getText().trim().isEmpty()) {
                JOptionIcon.showWarningDialog(dialog, "Title is required."); return;
            }
            if (txtAuthor.getText().trim().isEmpty()) {
                JOptionIcon.showWarningDialog(dialog, "Author is required."); return;
            }
            if (cmbCategory.getSelectedItem() == null) {
                JOptionIcon.showWarningDialog(dialog, "Category is required."); return;
            }
            confirmed[0] = true;
            dialog.dispose();
        });
        btnCancel.addActionListener(ev -> dialog.dispose());

        showDialog(dialog, formPanel, btnSave, btnCancel, parentFrame);

        if (!confirmed[0]) return;

        try {
            String title = txtTitle.getText().trim().replace("'", "''");
            String author = txtAuthor.getText().trim().replace("'", "''");
            String isbn = txtISBN.getText().trim().replace("'", "''");
            String publisher = txtPublisher.getText().trim().replace("'", "''");
            String location = txtLocation.getText().trim().replace("'", "''");
            int qty = (Integer) spnQty.getValue();
            int catId = getCategoryId((String) cmbCategory.getSelectedItem());

            String yearStr = txtYear.getText().trim();
            int year = yearStr.isEmpty() ? 0 : Integer.parseInt(yearStr);
            String priceStr = txtPrice.getText().trim();
            double price = priceStr.isEmpty() ? 0 : Double.parseDouble(priceStr);

            String sql = "INSERT INTO book (title, author, isbn, category_id, publisher, publication_year, quantity, available_qty, price, location) VALUES ('"
                + title + "', '" + author + "', "
                + (isbn.isEmpty() ? "NULL" : "'" + isbn + "'") + ", "
                + catId + ", "
                + (publisher.isEmpty() ? "NULL" : "'" + publisher + "'") + ", "
                + (year == 0 ? "NULL" : year) + ", "
                + qty + ", " + qty + ", "
                + (price == 0 ? "NULL" : price) + ", "
                + (location.isEmpty() ? "NULL" : "'" + location + "'") + ")";

            if (MySQL.executeIUD(sql) > 0) {
                loadBookData();
                loadInventoryData();
                JOptionIcon.showSuccessDialog(this, "Book added successfully!");
            } else {
                JOptionIcon.showErrorDialog(this, "Failed to add book.");
            }
        } catch (NumberFormatException nfe) {
            JOptionIcon.showErrorDialog(this, "Invalid year or price format.");
        } catch (Exception ex) {
            JOptionIcon.showErrorDialog(this, "Error adding book: " + ex.getMessage());
        }
    }

    private void editBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a book to edit.");
            return;
        }

        int bookId = (int) bookTableModel.getValueAt(selectedRow, 0);
        String currentISBN = str(bookTableModel.getValueAt(selectedRow, 1));
        String currentTitle = str(bookTableModel.getValueAt(selectedRow, 2));
        String currentAuthor = str(bookTableModel.getValueAt(selectedRow, 3));
        String currentCategory = str(bookTableModel.getValueAt(selectedRow, 4));
        String currentPublisher = str(bookTableModel.getValueAt(selectedRow, 5));
        int currentYear = intVal(bookTableModel.getValueAt(selectedRow, 6));
        int currentAvailable = intVal(bookTableModel.getValueAt(selectedRow, 7));
        int currentTotal = intVal(bookTableModel.getValueAt(selectedRow, 8));
        String currentLocation = str(bookTableModel.getValueAt(selectedRow, 9));

        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentFrame, "Edit Book", true);
        setDialogIcon(dialog);

        JTextField txtTitle = new JTextField(currentTitle, 22);
        JTextField txtAuthor = new JTextField(currentAuthor, 22);
        JTextField txtISBN = new JTextField(currentISBN, 22);
        JComboBox<String> cmbCategory = new JComboBox<>();
        JTextField txtPublisher = new JTextField(currentPublisher, 22);
        JTextField txtYear = new JTextField(currentYear > 0 ? String.valueOf(currentYear) : "", 22);
        JSpinner spnTotal = new JSpinner(new SpinnerNumberModel(currentTotal, 1, 9999, 1));
        JSpinner spnAvailable = new JSpinner(new SpinnerNumberModel(currentAvailable, 0, currentTotal, 1));
        JTextField txtLocation = new JTextField(currentLocation, 22);

        loadCategoriesToCombo(cmbCategory);
        cmbCategory.setSelectedItem(currentCategory);

        JPanel formPanel = buildFormPanel(new Object[][]{
            {"Title: *", txtTitle},
            {"Author: *", txtAuthor},
            {"ISBN:", txtISBN},
            {"Category: *", cmbCategory},
            {"Publisher:", txtPublisher},
            {"Publication Year:", txtYear},
            {"Total Quantity:", spnTotal},
            {"Available Qty:", spnAvailable},
            {"Shelf Location:", txtLocation}
        });

        boolean[] confirmed = {false};
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        btnSave.addActionListener(ev -> {
            if (txtTitle.getText().trim().isEmpty()) {
                JOptionIcon.showWarningDialog(dialog, "Title is required."); return;
            }
            if (txtAuthor.getText().trim().isEmpty()) {
                JOptionIcon.showWarningDialog(dialog, "Author is required."); return;
            }
            int av = (Integer) spnAvailable.getValue();
            int tot = (Integer) spnTotal.getValue();
            if (av > tot) {
                JOptionIcon.showWarningDialog(dialog, "Available quantity cannot exceed total quantity."); return;
            }
            confirmed[0] = true;
            dialog.dispose();
        });
        btnCancel.addActionListener(ev -> dialog.dispose());

        showDialog(dialog, formPanel, btnSave, btnCancel, parentFrame);

        if (!confirmed[0]) return;

        try {
            String title = txtTitle.getText().trim().replace("'", "''");
            String author = txtAuthor.getText().trim().replace("'", "''");
            String isbn = txtISBN.getText().trim().replace("'", "''");
            String publisher = txtPublisher.getText().trim().replace("'", "''");
            String location = txtLocation.getText().trim().replace("'", "''");
            int total = (Integer) spnTotal.getValue();
            int available = (Integer) spnAvailable.getValue();
            int catId = getCategoryId((String) cmbCategory.getSelectedItem());

            String yearStr = txtYear.getText().trim();
            int year = yearStr.isEmpty() ? 0 : Integer.parseInt(yearStr);

            String sql = "UPDATE book SET title='" + title + "', author='" + author + "', "
                + "isbn=" + (isbn.isEmpty() ? "NULL" : "'" + isbn + "'") + ", "
                + "category_id=" + catId + ", "
                + "publisher=" + (publisher.isEmpty() ? "NULL" : "'" + publisher + "'") + ", "
                + "publication_year=" + (year == 0 ? "NULL" : year) + ", "
                + "quantity=" + total + ", available_qty=" + available + ", "
                + "location=" + (location.isEmpty() ? "NULL" : "'" + location + "'") + " "
                + "WHERE book_id=" + bookId;

            if (MySQL.executeIUD(sql) > 0) {
                loadBookData();
                loadInventoryData();
                JOptionIcon.showSuccessDialog(this, "Book updated successfully!");
            } else {
                JOptionIcon.showErrorDialog(this, "Failed to update book.");
            }
        } catch (NumberFormatException nfe) {
            JOptionIcon.showErrorDialog(this, "Invalid year format.");
        } catch (Exception ex) {
            JOptionIcon.showErrorDialog(this, "Error updating book: " + ex.getMessage());
        }
    }

    private void deleteBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a book to delete.");
            return;
        }

        int bookId = (int) bookTableModel.getValueAt(selectedRow, 0);
        String title = str(bookTableModel.getValueAt(selectedRow, 2));

        int confirm = JOptionIcon.showConfirmDialog(this,
            "Are you sure you want to delete \"" + title + "\"?\nThis will also remove all associated issue records.");
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            // Delete associated issues first (though FK cascade handles it)
            MySQL.executeIUD("DELETE FROM book_issue WHERE book_id=" + bookId);
            int rows = MySQL.executeIUD("DELETE FROM book WHERE book_id=" + bookId);
            if (rows > 0) {
                loadBookData();
                loadIssueData();
                loadInventoryData();
                JOptionIcon.showSuccessDialog(this, "Book deleted successfully!");
            } else {
                JOptionIcon.showErrorDialog(this, "Failed to delete book.");
            }
        } catch (Exception ex) {
            JOptionIcon.showErrorDialog(this, "Error deleting book: " + ex.getMessage());
        }
    }

    private void generateBarcode() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a book to generate a barcode.");
            return;
        }

        String isbn = str(bookTableModel.getValueAt(selectedRow, 1));
        String title = str(bookTableModel.getValueAt(selectedRow, 2));
        int bookId = (int) bookTableModel.getValueAt(selectedRow, 0);

        // Use ISBN if available, otherwise use book_id
        String barcodeData = (isbn != null && !isbn.isEmpty()) ? isbn : "BK" + String.format("%05d", bookId);

        try {
            String path = BarcodeGenerator.generateBookBarcode(barcodeData);
            if (path != null) {
                JOptionIcon.showSuccessDialog(this, "Barcode generated for \"" + title + "\"\nSaved to: " + path);
            } else {
                JOptionIcon.showErrorDialog(this, "Failed to generate barcode.");
            }
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error generating barcode: " + e.getMessage());
        }
    }

    // =====================================================================
    // ISSUE / RETURN TAB
    // =====================================================================

    private JPanel createIssuePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout(5, 5));
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        txtIssueSearch = new JTextField(20);
        txtIssueSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchIssues();
            }
        });
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> searchIssues());

        JComboBox<String> cmbFilter = new JComboBox<>(new String[]{"All", "Issued", "Returned", "Overdue"});
        cmbFilter.addActionListener(e -> filterIssues((String) cmbFilter.getSelectedItem()));

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(txtIssueSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(new JLabel("  Status:"));
        searchPanel.add(cmbFilter);
        topBar.add(searchPanel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        btnIssue = new JButton("Issue Book");
        btnIssue.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnIssue.addActionListener(e -> issueBook());
        applyPrimaryButtonEffect(btnIssue, new Color(22, 163, 74), new Color(18, 140, 60));
        btnReturn = new JButton("Return Book");
        btnReturn.setEnabled(false);
        btnReturn.addActionListener(e -> returnBook());
        applySecondaryButtonEffect(btnReturn);
        btnUpdateOverdue = new JButton("Mark Overdue");
        btnUpdateOverdue.setEnabled(false);
        btnUpdateOverdue.addActionListener(e -> updateOverdueStatus());
        applySecondaryButtonEffect(btnUpdateOverdue);
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadIssueData());
        applySecondaryButtonEffect(btnRefresh);
        
        buttonPanel.add(btnIssue);
        buttonPanel.add(btnReturn);
        buttonPanel.add(btnUpdateOverdue);
        buttonPanel.add(btnRefresh);

        // Hide Issue, Return, and Mark Overdue buttons for teachers
        if (UserSession.get() != null && UserSession.get().isTeacher()) {
            btnIssue.setVisible(false);
            btnReturn.setVisible(false);
            btnUpdateOverdue.setVisible(false);
        }
        if (UserSession.get() != null && UserSession.get().isTeacher()) {
            btnIssue.setVisible(false);
            btnReturn.setVisible(false);
            btnUpdateOverdue.setVisible(false);
        }

        topBar.add(buttonPanel, BorderLayout.EAST);

        panel.add(topBar, BorderLayout.NORTH);

        // Table
        issueTableModel = new DefaultTableModel(
            new Object[]{"Issue ID", "Book Title", "Student ID", "Student Name", "Issue Date", "Due Date", "Return Date", "Fine (LKR)", "Status", "Remarks"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        issueTable = new JTable(issueTableModel);
        issueTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        issueTable.setRowHeight(30);
        issueTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        issueTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Add selection listener to enable/disable buttons
        issueTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean hasSelection = issueTable.getSelectedRow() != -1;
                btnReturn.setEnabled(hasSelection);
                btnUpdateOverdue.setEnabled(hasSelection);
            }
        });
        
        // Hide Issue ID
        issueTable.getColumnModel().getColumn(0).setMinWidth(0);
        issueTable.getColumnModel().getColumn(0).setMaxWidth(0);
        issueTable.getColumnModel().getColumn(0).setWidth(0);

        // Color overdue rows
        issueTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = str(issueTableModel.getValueAt(row, 8));
                if (!isSelected) {
                    if ("Overdue".equals(status)) {
                        c.setBackground(new Color(255, 230, 230));
                    } else if ("Returned".equals(status)) {
                        c.setBackground(new Color(230, 255, 230));
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(issueTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadIssueData() {
        loadIssueData(null);
    }

    private void loadIssueData(String statusFilter) {
        try {
            issueTableModel.setRowCount(0);

            String query = "SELECT bi.issue_id, b.title, bi.student_id, " +
                "CONCAT(s.first_name, ' ', s.last_name) AS student_name, " +
                "bi.issue_date, bi.due_date, bi.return_date, bi.fine, bi.status, bi.remarks " +
                "FROM book_issue bi " +
                "JOIN book b ON bi.book_id = b.book_id " +
                "JOIN student s ON bi.student_id = s.student_id ";

            if (statusFilter != null && !statusFilter.equals("All")) {
                query += "WHERE bi.status = '" + statusFilter + "' ";
            }

            query += "ORDER BY bi.issue_date DESC";

            ResultSet rs = MySQL.executeSearch(query);
            while (rs.next()) {
                String returnDate = rs.getString("return_date");
                issueTableModel.addRow(new Object[]{
                    rs.getInt("issue_id"),
                    rs.getString("title"),
                    rs.getString("student_id"),
                    rs.getString("student_name"),
                    rs.getString("issue_date"),
                    rs.getString("due_date"),
                    returnDate != null ? returnDate : "-",
                    rs.getDouble("fine"),
                    rs.getString("status"),
                    rs.getString("remarks")
                });
            }
            rs.close();

        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading issue data: " + e.getMessage());
        }
    }

    private void searchIssues() {
        String term = txtIssueSearch.getText().trim();
        try {
            issueTableModel.setRowCount(0);

            String query = "SELECT bi.issue_id, b.title, bi.student_id, " +
                "CONCAT(s.first_name, ' ', s.last_name) AS student_name, " +
                "bi.issue_date, bi.due_date, bi.return_date, bi.fine, bi.status, bi.remarks " +
                "FROM book_issue bi " +
                "JOIN book b ON bi.book_id = b.book_id " +
                "JOIN student s ON bi.student_id = s.student_id ";

            if (!term.isEmpty()) {
                String safe = term.replace("'", "''");
                query += "WHERE b.title LIKE '%" + safe + "%' " +
                         "OR bi.student_id LIKE '%" + safe + "%' " +
                         "OR s.first_name LIKE '%" + safe + "%' " +
                         "OR s.last_name LIKE '%" + safe + "%' ";
            }
            query += "ORDER BY bi.issue_date DESC";

            ResultSet rs = MySQL.executeSearch(query);
            while (rs.next()) {
                String returnDate = rs.getString("return_date");
                issueTableModel.addRow(new Object[]{
                    rs.getInt("issue_id"),
                    rs.getString("title"),
                    rs.getString("student_id"),
                    rs.getString("student_name"),
                    rs.getString("issue_date"),
                    rs.getString("due_date"),
                    returnDate != null ? returnDate : "-",
                    rs.getDouble("fine"),
                    rs.getString("status"),
                    rs.getString("remarks")
                });
            }
            rs.close();

        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error searching issues: " + e.getMessage());
        }
    }

    private void filterIssues(String status) {
        loadIssueData(status);
    }

    private void issueBook() {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentFrame, "Issue Book to Student", true);
        setDialogIcon(dialog);

        JComboBox<String> cmbBook = new JComboBox<>();
        JTextField txtStudentId = new JTextField(22);
        com.toedter.calendar.JDateChooser issueDateChooser = new com.toedter.calendar.JDateChooser();
        issueDateChooser.setDateFormatString("yyyy-MM-dd");
        issueDateChooser.setDate(new Date());
        com.toedter.calendar.JDateChooser dueDateChooser = new com.toedter.calendar.JDateChooser();
        dueDateChooser.setDateFormatString("yyyy-MM-dd");
        // Default due date = 14 days from now
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.DAY_OF_MONTH, 14);
        dueDateChooser.setDate(cal.getTime());
        JTextArea txtRemarks = new JTextArea(2, 20);
        txtRemarks.setLineWrap(true);
        JScrollPane remarksScroll = new JScrollPane(txtRemarks);

        // Load available books
        try {
            ResultSet rs = MySQL.executeSearch(
                "SELECT book_id, title, available_qty FROM book WHERE available_qty > 0 ORDER BY title"
            );
            while (rs.next()) {
                cmbBook.addItem(rs.getInt("book_id") + " | " + rs.getString("title") + " (Avail: " + rs.getInt("available_qty") + ")");
            }
            rs.close();
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading books: " + e.getMessage());
            return;
        }

        if (cmbBook.getItemCount() == 0) {
            JOptionIcon.showWarningDialog(this, "No books are currently available for issue.");
            return;
        }

        JPanel formPanel = buildFormPanel(new Object[][]{
            {"Book: *", cmbBook},
            {"Student ID: *", txtStudentId},
            {"Issue Date: *", issueDateChooser},
            {"Due Date: *", dueDateChooser},
            {"Remarks:", remarksScroll}
        });

        boolean[] confirmed = {false};
        JButton btnSave = new JButton("Issue");
        JButton btnCancel = new JButton("Cancel");

        btnSave.addActionListener(ev -> {
            if (cmbBook.getSelectedItem() == null) {
                JOptionIcon.showWarningDialog(dialog, "Please select a book."); return;
            }
            if (txtStudentId.getText().trim().isEmpty()) {
                JOptionIcon.showWarningDialog(dialog, "Student ID is required."); return;
            }
            if (issueDateChooser.getDate() == null) {
                JOptionIcon.showWarningDialog(dialog, "Issue date is required."); return;
            }
            if (dueDateChooser.getDate() == null) {
                JOptionIcon.showWarningDialog(dialog, "Due date is required."); return;
            }
            if (dueDateChooser.getDate().before(issueDateChooser.getDate())) {
                JOptionIcon.showWarningDialog(dialog, "Due date cannot be before issue date."); return;
            }
            confirmed[0] = true;
            dialog.dispose();
        });
        btnCancel.addActionListener(ev -> dialog.dispose());

        showDialog(dialog, formPanel, btnSave, btnCancel, parentFrame);

        if (!confirmed[0]) return;

        try {
            String selectedBook = (String) cmbBook.getSelectedItem();
            int bookId = Integer.parseInt(selectedBook.split("\\|")[0].trim());
            String studentId = txtStudentId.getText().trim().replace("'", "''");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String issueDate = sdf.format(issueDateChooser.getDate());
            String dueDate = sdf.format(dueDateChooser.getDate());
            String remarks = txtRemarks.getText().trim().replace("'", "''");

            // Validate student exists
            ResultSet rsStudent = MySQL.executeSearch("SELECT student_id FROM student WHERE student_id='" + studentId + "'");
            if (!rsStudent.next()) {
                JOptionIcon.showErrorDialog(this, "Student ID '" + studentId + "' not found in the system.");
                rsStudent.close();
                return;
            }
            rsStudent.close();

            // Check if student already has this book issued
            ResultSet rsDup = MySQL.executeSearch(
                "SELECT issue_id FROM book_issue WHERE book_id=" + bookId + " AND student_id='" + studentId + "' AND status='Issued'"
            );
            if (rsDup.next()) {
                JOptionIcon.showWarningDialog(this, "This student already has this book issued and not yet returned.");
                rsDup.close();
                return;
            }
            rsDup.close();

            // Insert issue record
            String remarkVal = remarks.isEmpty() ? "NULL" : "'" + remarks + "'";
            String sql = "INSERT INTO book_issue (book_id, student_id, issue_date, due_date, status, remarks) VALUES ("
                + bookId + ", '" + studentId + "', '" + issueDate + "', '" + dueDate + "', 'Issued', " + remarkVal + ")";

            if (MySQL.executeIUD(sql) > 0) {
                // Decrease available quantity
                MySQL.executeIUD("UPDATE book SET available_qty = available_qty - 1 WHERE book_id=" + bookId);
                loadIssueData();
                loadBookData();
                loadInventoryData();
                JOptionIcon.showSuccessDialog(this, "Book issued successfully!");
            } else {
                JOptionIcon.showErrorDialog(this, "Failed to issue book.");
            }

        } catch (Exception ex) {
            JOptionIcon.showErrorDialog(this, "Error issuing book: " + ex.getMessage());
        }
    }

    private void returnBook() {
        int selectedRow = issueTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select an issue record to return.");
            return;
        }

        String status = str(issueTableModel.getValueAt(selectedRow, 8));
        if ("Returned".equals(status)) {
            JOptionIcon.showWarningDialog(this, "This book has already been returned.");
            return;
        }

        int issueId = (int) issueTableModel.getValueAt(selectedRow, 0);
        String bookTitle = str(issueTableModel.getValueAt(selectedRow, 1));
        String studentName = str(issueTableModel.getValueAt(selectedRow, 3));
        String dueDateStr = str(issueTableModel.getValueAt(selectedRow, 5));

        // Calculate fine if overdue
        double fine = 0;
        String todayStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dueDate = sdf.parse(dueDateStr);
            Date today = new Date();
            fine = lk.educore.schoolms.service.LibraryService.calculateFine(dueDate, today);
        } catch (Exception e) {
            // Ignore date parse error
        }

        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentFrame, "Return Book", true);
        setDialogIcon(dialog);

        JLabel lblInfo = new JLabel("<html><b>Book:</b> " + bookTitle + "<br><b>Student:</b> " + studentName + "<br><b>Due Date:</b> " + dueDateStr + "</html>");
        com.toedter.calendar.JDateChooser returnDateChooser = new com.toedter.calendar.JDateChooser();
        returnDateChooser.setDateFormatString("yyyy-MM-dd");
        returnDateChooser.setDate(new Date());
        JTextField txtFine = new JTextField(String.format("%.2f", fine), 22);
        JTextArea txtRemarks = new JTextArea(2, 20);
        txtRemarks.setLineWrap(true);
        JScrollPane remarksScroll = new JScrollPane(txtRemarks);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(lblInfo, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Return Date: *"), gbc);
        gbc.gridx = 1; formPanel.add(returnDateChooser, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Fine (LKR):"), gbc);
        gbc.gridx = 1; formPanel.add(txtFine, gbc);

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Remarks:"), gbc);
        gbc.gridx = 1; formPanel.add(remarksScroll, gbc);

        if (fine > 0) {
            JLabel fineNote = new JLabel("<html><font color='red'>⚠ Book is overdue. Fine auto-calculated at LKR 10/day.</font></html>");
            gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
            formPanel.add(fineNote, gbc);
            gbc.gridwidth = 1;
        }

        boolean[] confirmed = {false};
        JButton btnSave = new JButton("Confirm Return");
        JButton btnCancel = new JButton("Cancel");

        btnSave.addActionListener(ev -> {
            if (returnDateChooser.getDate() == null) {
                JOptionIcon.showWarningDialog(dialog, "Return date is required."); return;
            }
            confirmed[0] = true;
            dialog.dispose();
        });
        btnCancel.addActionListener(ev -> dialog.dispose());

        showDialog(dialog, formPanel, btnSave, btnCancel, parentFrame);

        if (!confirmed[0]) return;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String returnDate = sdf.format(returnDateChooser.getDate());
            double actualFine = Double.parseDouble(txtFine.getText().trim().isEmpty() ? "0" : txtFine.getText().trim());
            String remarks = txtRemarks.getText().trim().replace("'", "''");
            String remarksVal = remarks.isEmpty() ? "NULL" : "'" + remarks + "'";

            // Get book_id from issue record
            ResultSet rs = MySQL.executeSearch("SELECT book_id FROM book_issue WHERE issue_id=" + issueId);
            if (!rs.next()) { rs.close(); JOptionIcon.showErrorDialog(this, "Issue record not found."); return; }
            int bookId = rs.getInt("book_id");
            rs.close();

            // Update issue record
            String sql = "UPDATE book_issue SET return_date='" + returnDate + "', fine=" + actualFine
                + ", status='Returned', remarks=" + remarksVal + " WHERE issue_id=" + issueId;

            if (MySQL.executeIUD(sql) > 0) {
                // Increase available quantity
                MySQL.executeIUD("UPDATE book SET available_qty = available_qty + 1 WHERE book_id=" + bookId);
                loadIssueData();
                loadBookData();
                loadInventoryData();
                String fineMsg = actualFine > 0 ? "\nFine charged: LKR " + String.format("%.2f", actualFine) : "";
                JOptionIcon.showSuccessDialog(this, "Book returned successfully!" + fineMsg);
            } else {
                JOptionIcon.showErrorDialog(this, "Failed to process return.");
            }

        } catch (NumberFormatException nfe) {
            JOptionIcon.showErrorDialog(this, "Invalid fine amount.");
        } catch (Exception ex) {
            JOptionIcon.showErrorDialog(this, "Error returning book: " + ex.getMessage());
        }
    }

    private void updateOverdueStatus() {
        try {
            String todayStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            int rows = MySQL.executeIUD(
                "UPDATE book_issue SET status='Overdue' " +
                "WHERE status='Issued' AND due_date < '" + todayStr + "'"
            );
            loadIssueData();
            JOptionIcon.showSuccessDialog(this, rows + " record(s) marked as Overdue.");
        } catch (Exception ex) {
            JOptionIcon.showErrorDialog(this, "Error updating overdue status: " + ex.getMessage());
        }
    }

    // =====================================================================
    // INVENTORY TAB
    // =====================================================================

    private JPanel createInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Stats cards at top
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        lblTotalBooks = createStatCard("Total Books", "0", new Color(52, 152, 219));
        lblAvailableBooks = createStatCard("Available", "0", new Color(39, 174, 96));
        lblIssuedBooks = createStatCard("Issued", "0", new Color(230, 126, 34));
        lblOverdueBooks = createStatCard("Overdue", "0", new Color(231, 76, 60));

        statsPanel.add(lblTotalBooks.getParent());
        statsPanel.add(lblAvailableBooks.getParent());
        statsPanel.add(lblIssuedBooks.getParent());
        statsPanel.add(lblOverdueBooks.getParent());
        panel.add(statsPanel, BorderLayout.NORTH);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadInventoryData());
        JButton btnViewOverdue = new JButton("View Overdue Records");
        btnViewOverdue.addActionListener(e -> {
            tabbedPane.setSelectedIndex(1);
            filterIssues("Overdue");
        });
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnViewOverdue);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Inventory table: per-category summary
        inventoryTableModel = new DefaultTableModel(
            new Object[]{"Category", "Total Books", "Total Copies", "Available Copies", "Issued Copies", "Utilization %"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        inventoryTable = new JTable(inventoryTableModel);
        inventoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        inventoryTable.setRowHeight(25);
        inventoryTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JLabel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTitle.setForeground(Color.WHITE);
        card.add(lblTitle, BorderLayout.NORTH);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValue.setForeground(Color.WHITE);
        lblValue.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(lblValue, BorderLayout.CENTER);

        return lblValue;
    }

    private void loadInventoryData() {
        try {
            // Update stat cards
            ResultSet rs1 = MySQL.executeSearch("SELECT COUNT(*) AS total, SUM(quantity) AS total_copies, SUM(available_qty) AS total_avail FROM book");
            if (rs1.next()) {
                int total = rs1.getInt("total");
                int copies = rs1.getInt("total_copies");
                int avail = rs1.getInt("total_avail");
                int issued = copies - avail;
                lblTotalBooks.setText(String.valueOf(total));
                lblAvailableBooks.setText(String.valueOf(avail));
                lblIssuedBooks.setText(String.valueOf(issued));
            }
            rs1.close();

            ResultSet rs2 = MySQL.executeSearch(
                "SELECT COUNT(*) AS cnt FROM book_issue WHERE status='Overdue'"
            );
            if (rs2.next()) {
                lblOverdueBooks.setText(String.valueOf(rs2.getInt("cnt")));
            }
            rs2.close();

            // Inventory table: per-category
            inventoryTableModel.setRowCount(0);
            ResultSet rs3 = MySQL.executeSearch(
                "SELECT bc.category_name, COUNT(b.book_id) AS total_books, " +
                "SUM(b.quantity) AS total_copies, SUM(b.available_qty) AS available_copies, " +
                "(SUM(b.quantity) - SUM(b.available_qty)) AS issued_copies " +
                "FROM book_category bc " +
                "LEFT JOIN book b ON bc.category_id = b.category_id " +
                "GROUP BY bc.category_id, bc.category_name " +
                "ORDER BY bc.category_name"
            );
            while (rs3.next()) {
                int totalCopies = rs3.getInt("total_copies");
                int issuedCopies = rs3.getInt("issued_copies");
                double util = totalCopies > 0 ? (issuedCopies * 100.0 / totalCopies) : 0;
                inventoryTableModel.addRow(new Object[]{
                    rs3.getString("category_name"),
                    rs3.getInt("total_books"),
                    totalCopies,
                    rs3.getInt("available_copies"),
                    issuedCopies,
                    String.format("%.1f%%", util)
                });
            }
            rs3.close();

        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading inventory data: " + e.getMessage());
        }
    }

    // =====================================================================
    // UTILITY / HELPER METHODS
    // =====================================================================

    private void loadCategoriesToCombo(JComboBox<String> combo) {
        try {
            ResultSet rs = MySQL.executeSearch("SELECT category_name FROM book_category ORDER BY category_name");
            while (rs.next()) {
                combo.addItem(rs.getString("category_name"));
            }
            rs.close();
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading categories: " + e.getMessage());
        }
    }

    private int getCategoryId(String categoryName) throws Exception {
        ResultSet rs = MySQL.executeSearch(
            "SELECT category_id FROM book_category WHERE category_name='" + categoryName.replace("'", "''") + "'"
        );
        if (rs.next()) {
            int id = rs.getInt("category_id");
            rs.close();
            return id;
        }
        rs.close();
        throw new Exception("Category not found: " + categoryName);
    }

    private JPanel buildFormPanel(Object[][] rows) {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 6, 5, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        for (int i = 0; i < rows.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            formPanel.add(new JLabel((String) rows[i][0]), gbc);
            gbc.gridx = 1;
            formPanel.add((Component) rows[i][1], gbc);
        }

        JLabel note = new JLabel("* Required fields");
        note.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        note.setForeground(Color.GRAY);
        gbc.gridx = 0; gbc.gridy = rows.length; gbc.gridwidth = 2;
        formPanel.add(note, gbc);

        return formPanel;
    }

    private void showDialog(JDialog dialog, JPanel formPanel, JButton btnSave, JButton btnCancel, Frame parent) {
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);

        dialog.setLayout(new BorderLayout());
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setMinimumSize(new Dimension(420, dialog.getHeight()));
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    private void setDialogIcon(JDialog dialog) {
        try {
            InputStream imgStream = getClass().getResourceAsStream("/lk/educore/schoolms/resources/logo.png");
            if (imgStream != null) {
                dialog.setIconImage(new javax.swing.ImageIcon(ImageIO.read(imgStream)).getImage());
            }
        } catch (IOException ex) {
            SignIn.getLoggerObject().log(Level.WARNING, "Failed to load dialog icon: " + ex.getMessage(), ex);
        }
    }

    private String str(Object val) {
        return val != null ? val.toString() : "";
    }

    private int intVal(Object val) {
        if (val == null) return 0;
        try { return Integer.parseInt(val.toString()); } catch (NumberFormatException e) { return 0; }
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

    private void applyPrimaryButtonEffect(JButton button, Color normalColor, Color hoverColor) {
        button.setBackground(normalColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(hoverColor);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(normalColor);
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