package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Library Management Panel
 * Book management, issue/return tracking, inventory, barcode generation
 */
public class LibraryManagement extends JPanel {

    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnAddBook, btnRefresh, btnGenerateBarcode;
    private JTabbedPane tabbedPane;

    public LibraryManagement() {
        initComponents();
        loadBookData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Books Tab
        JPanel booksPanel = createBooksPanel();
        tabbedPane.addTab("Books", booksPanel);
        
        // Issue/Return Tab
        JPanel issuePanel = createIssuePanel();
        tabbedPane.addTab("Issue/Return", issuePanel);
        
        // Inventory Tab
        JPanel inventoryPanel = createInventoryPanel();
        tabbedPane.addTab("Inventory", inventoryPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Title
        JLabel titleLabel = new JLabel("Library Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.WEST);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
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
        
        panel.add(searchPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        
        btnAddBook = new JButton("Add Book");
        btnAddBook.addActionListener(e -> addBook());
        
        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadBookData());
        
        btnGenerateBarcode = new JButton("Generate Barcode");
        btnGenerateBarcode.addActionListener(e -> generateBarcode());

        buttonPanel.add(btnAddBook);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnGenerateBarcode);

        panel.add(buttonPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(
            new Object[]{"ISBN", "Title", "Author", "Category", "Available", "Total"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookTable = new JTable(tableModel);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.setRowHeight(25);
        bookTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(bookTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createIssuePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(new JLabel("Issue/Return Management - Coming Soon", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(new JLabel("Inventory Management - Coming Soon", SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }

    private void loadBookData() {
        try {
            tableModel.setRowCount(0);
            
            ResultSet rs = MySQL.executeSearch("SELECT b.isbn, b.title, b.author, c.category_name, b.available_qty, b.quantity " +
                          "FROM book b " +
                          "JOIN book_category c ON b.category_id = c.category_id " +
                          "ORDER BY b.title");
            
            while (rs.next()) {
                String isbn = rs.getString("isbn");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String category = rs.getString("category_name");
                int available = rs.getInt("available_qty");
                int total = rs.getInt("quantity");
                
                tableModel.addRow(new Object[]{isbn, title, author, category, available, total});
            }
            
            rs.close();
            
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading book data: " + e.getMessage());
        }
    }

    private void searchBooks() {
        loadBookData();
    }

    private void addBook() {
        JOptionIcon.showInfoDialog(this, "Add Book - Coming Soon");
    }

    private void generateBarcode() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionIcon.showWarningDialog(this, "Please select a book to generate barcode.");
            return;
        }

        String isbn = tableModel.getValueAt(selectedRow, 0).toString();
        String title = tableModel.getValueAt(selectedRow, 1).toString();

        try {
            lk.educore.schoolms.model.BarcodeGenerator.generateBookBarcode(isbn);
            JOptionIcon.showSuccessDialog(this, "Barcode generated successfully for " + title);
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error generating barcode: " + e.getMessage());
        }
    }
}