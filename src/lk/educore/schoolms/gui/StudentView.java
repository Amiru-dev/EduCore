package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.logging.Level;
import javax.imageio.ImageIO;

/**
 * Student View Dialog Comprehensive student details view with action buttons
 */
public class StudentView extends JDialog {

    // Color palette
    private static final Color PRIMARY = new Color(37, 99, 235);
    private static final Color PRIMARY_DARK = new Color(29, 78, 216);
    private static final Color DANGER = new Color(220, 38, 38);
    private static final Color SUCCESS = new Color(22, 163, 74);
    private static final Color WARNING = new Color(202, 138, 4);
    private static final Color NEUTRAL = new Color(100, 116, 139);
    private static final Color BG_MAIN = new Color(248, 250, 252);
    private static final Color BG_CARD = Color.WHITE;
    private static final Color HEADER_BG = new Color(30, 41, 59);
    private static final Color TEXT_LABEL = new Color(71, 85, 105);
    private static final Color TEXT_VALUE = new Color(15, 23, 42);
    private static final Color BORDER_COLOR = new Color(226, 232, 240);
    private static final Color TABLE_HEADER = new Color(241, 245, 249);
    private static final Color ROW_ALT = new Color(248, 250, 252);

    private String studentId;
    private JButton btnEdit, btnDelete, btnEmail, btnQRCode, btnAttendance, btnFees, btnResults;

    public StudentView(String studentId) {
        this.studentId = studentId;
        initComponents();

        try {
            InputStream imgStream = getClass().getResourceAsStream("/lk/educore/schoolms/resources/logo.png");
            if (imgStream == null) {
                SignIn.getLoggerObject().log(Level.WARNING, "EduCore logo image resource not found at /lk/educore/schoolms/resources/logo.png");
                return;
            }
            ImageIcon originalIcon = new ImageIcon(ImageIO.read(imgStream));
            this.setIconImage(originalIcon.getImage());

        } catch (IOException e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Failed to load educore logo image: " + e.getMessage(), e);
        } catch (Exception e) {
            SignIn.getLoggerObject().log(Level.WARNING, "Unexpected error loading educore logo image: " + e.getMessage(), e);
        }
    }

    private void initComponents() {
        setSize(850, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setBackground(BG_MAIN);
        setTitle("Student View");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        getRootPane().setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createContentPanel(), BorderLayout.CENTER);
        add(createActionPanel(), BorderLayout.SOUTH);
    }

    // ─────────────────────────────── HEADER ───────────────────────────────
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, HEADER_BG, getWidth(), 0, new Color(15, 23, 42));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(0, 72));
        header.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 24));

        // Left: icon + title
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);

        JLabel iconLabel = new JLabel("\uD83C\uDF93");   // 🎓
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        iconLabel.setForeground(Color.WHITE);

        JPanel titles = new JPanel(new GridLayout(2, 1, 0, 2));
        titles.setOpaque(false);
        JLabel titleLabel = new JLabel("Student Profile");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        JLabel subLabel = new JLabel("View and manage student details");
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subLabel.setForeground(new Color(148, 163, 184));
        titles.add(titleLabel);
        titles.add(subLabel);

        left.add(iconLabel);
        left.add(titles);

        // Right: student ID badge
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        JLabel idBadge = new JLabel(studentId);
        idBadge.setFont(new Font("Segoe UI", Font.BOLD, 13));
        idBadge.setForeground(new Color(147, 197, 253));
        idBadge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(59, 130, 246, 120), 1, true),
                BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        right.add(idBadge);

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    // ─────────────────────────────── CONTENT ──────────────────────────────
    private JPanel createContentPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG_MAIN);
        wrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabs.setBackground(BG_MAIN);
        tabs.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));

        tabs.addTab("Basic Info", new StudentInformation(studentId));
        tabs.addTab("Address", new StudentAddressView(studentId));
        tabs.addTab("Academic", createAcademicPanel());
        tabs.addTab("Financial", createFinancialPanel());

        wrapper.add(tabs, BorderLayout.CENTER);
        return wrapper;
    }

    // ─────────────────────── ACADEMIC PANEL ───────────────────────────────
    private JPanel createAcademicPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BG_MAIN);
        outer.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = sectionTitle("Academic Information");
        outer.add(title, BorderLayout.NORTH);

        JPanel card = card();
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 16, 10, 16);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        try {
            ResultSet rs = MySQL.executeSearch(
                    "SELECT g.grade_name, s.registration_date, s.is_active "
                    + "FROM student s JOIN grade g ON s.grade_id = g.grade_id "
                    + "WHERE s.student_id = '" + studentId + "'");

            if (rs.next()) {
                addCardRow(card, gbc, 0, "Grade", rs.getString("grade_name"));
                addCardRow(card, gbc, 1, "Registration Date", rs.getString("registration_date"));
                addCardRow(card, gbc, 2, "Status", rs.getBoolean("is_active") ? "Active" : "Inactive");
            }
            rs.close();
        } catch (Exception e) {
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            card.add(new JLabel("Error loading academic information: " + e.getMessage()), gbc);
        }

        // push rows to the top
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        card.add(Box.createVerticalGlue(), gbc);

        outer.add(card, BorderLayout.CENTER);
        return outer;
    }

    // ─────────────────────── FINANCIAL PANEL ──────────────────────────────
    private JPanel createFinancialPanel() {
        JPanel outer = new JPanel(new BorderLayout(0, 12));
        outer.setBackground(BG_MAIN);
        outer.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        outer.add(sectionTitle("Fee History"), BorderLayout.NORTH);

        // Table
        String[] cols = {"Fee Type", "Amount", "Due Date", "Paid Date", "Status", "Receipt No"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(32);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(TEXT_VALUE);
        table.setBackground(BG_CARD);

        // Header styling
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(TABLE_HEADER);
        table.getTableHeader().setForeground(TEXT_LABEL);
        table.getTableHeader().setPreferredSize(new Dimension(0, 36));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_COLOR));

        // Alternating rows + status coloring
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean focus, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                lbl.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                if (!sel) {
                    lbl.setBackground(row % 2 == 0 ? BG_CARD : ROW_ALT);
                    if (col == 4) { // Status column
                        String status = val != null ? val.toString() : "";
                        switch (status) {
                            case "Paid":
                                lbl.setForeground(SUCCESS);
                                break;
                            case "Overdue":
                                lbl.setForeground(DANGER);
                                break;
                            case "Pending":
                                lbl.setForeground(WARNING);
                                break;
                            case "Cancelled":
                                lbl.setForeground(NEUTRAL);
                                break;
                            default:
                                lbl.setForeground(TEXT_VALUE);
                        }
                    } else {
                        lbl.setForeground(TEXT_VALUE);
                    }
                }
                return lbl;
            }
        });

        // Load data
        try {
            ResultSet rs = MySQL.executeSearch(
                    "SELECT ft.fee_type_name, f.amount, f.due_date, f.paid_date, f.status, f.receipt_no "
                    + "FROM fee f INNER JOIN fee_type ft ON f.fee_type_id = ft.fee_type_id "
                    + "WHERE f.student_id = '" + studentId + "' ORDER BY f.due_date DESC");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("fee_type_name"),
                    String.format("Rs. %.2f", rs.getDouble("amount")),
                    rs.getString("due_date") != null ? rs.getString("due_date") : "—",
                    rs.getString("paid_date") != null ? rs.getString("paid_date") : "—",
                    rs.getString("status"),
                    rs.getString("receipt_no") != null ? rs.getString("receipt_no") : "—"
                });
            }
            rs.close();
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading fee history: " + e.getMessage());
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        scroll.getViewport().setBackground(BG_CARD);
        outer.add(scroll, BorderLayout.CENTER);

        // Summary cards row
        JPanel summaryRow = new JPanel(new GridLayout(1, 3, 10, 0));
        summaryRow.setOpaque(false);
        try {
            ResultSet rt = MySQL.executeSearch("SELECT COALESCE(SUM(amount),0) t FROM fee WHERE student_id='" + studentId + "'");
            ResultSet rp = MySQL.executeSearch("SELECT COALESCE(SUM(amount),0) p FROM fee WHERE student_id='" + studentId + "' AND status='Paid'");
            ResultSet rn = MySQL.executeSearch("SELECT COALESCE(SUM(amount),0) n FROM fee WHERE student_id='" + studentId + "' AND status IN ('Pending','Overdue')");
            double total = 0, paid = 0, pending = 0;
            if (rt.next()) {
                total = rt.getDouble("t");
            }
            if (rp.next()) {
                paid = rp.getDouble("p");
            }
            if (rn.next()) {
                pending = rn.getDouble("n");
            }
            rt.close();
            rp.close();
            rn.close();

            summaryRow.add(summaryCard("Total Fees", String.format("Rs. %.2f", total), NEUTRAL));
            summaryRow.add(summaryCard("Paid", String.format("Rs. %.2f", paid), SUCCESS));
            summaryRow.add(summaryCard("Pending", String.format("Rs. %.2f", pending), pending > 0 ? DANGER : NEUTRAL));
        } catch (Exception e) {
            summaryRow.add(new JLabel("Unable to load summary"));
        }
        outer.add(summaryRow, BorderLayout.SOUTH);

        return outer;
    }

    // ─────────────────────── ACTION PANEL ─────────────────────────────────
    private JPanel createActionPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG_CARD);
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);
        btnEdit = styledButton("Edit", PRIMARY, Color.WHITE);
        btnDelete = styledButton("Delete", DANGER, Color.WHITE);
        left.add(btnEdit);
        left.add(btnDelete);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        btnEmail = styledButton("Email", new Color(126, 34, 206), Color.WHITE);
        btnQRCode = styledButton("QR Code", new Color(15, 118, 110), Color.WHITE);
        btnAttendance = styledButton("Attendance", WARNING, Color.WHITE);
        btnFees = styledButton("Fees", SUCCESS, Color.WHITE);
        btnResults = styledButton("Results", new Color(234, 88, 12), Color.WHITE);
        right.add(btnEmail);
        right.add(btnQRCode);
        right.add(btnAttendance);
        right.add(btnFees);
        right.add(btnResults);

        wrapper.add(left, BorderLayout.WEST);
        wrapper.add(right, BorderLayout.EAST);

        btnEdit.addActionListener(e -> editStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnEmail.addActionListener(e -> sendEmail());
        btnQRCode.addActionListener(e -> generateQRCode());
        btnAttendance.addActionListener(e -> viewAttendance());
        btnFees.addActionListener(e -> viewFees());
        btnResults.addActionListener(e -> viewResults());

        return wrapper;
    }

    // ─────────────────────── HELPER BUILDERS ──────────────────────────────
    private JLabel sectionTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lbl.setForeground(TEXT_VALUE);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        return lbl;
    }

    private JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(8, 0, 8, 0)
        ));
        return p;
    }

    private void addCardRow(JPanel card, GridBagConstraints gbc, int row, String label, String value) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.25;
        JLabel lblLabel = new JLabel(label + ":");
        lblLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblLabel.setForeground(TEXT_LABEL);
        card.add(lblLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.75;
        JLabel lblValue = new JLabel(value != null ? value : "—");
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblValue.setForeground(TEXT_VALUE);
        card.add(lblValue, gbc);
    }

    private JPanel summaryCard(String label, String value, Color color) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setBackground(BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(TEXT_LABEL);
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 16));
        val.setForeground(color);
        p.add(lbl, BorderLayout.NORTH);
        p.add(val, BorderLayout.CENTER);
        return p;
    }

    private JButton styledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bg.darker() : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(7, 14, 7, 14));
        return btn;
    }

    // ─────────────────────── ACTIONS ──────────────────────────────────────
    private void editStudent() {
        Window parent = SwingUtilities.getWindowAncestor(this);
        new StudentEditDialog((Frame) parent, studentId).setVisible(true);
    }

    private void deleteStudent() {
        int confirm = JOptionIcon.showConfirmDialog(this, "Are you sure you want to delete this student?");
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                MySQL.executeIUD("DELETE FROM student WHERE student_id = '" + studentId + "'");
                JOptionIcon.showSuccessDialog(this, "Student deleted successfully.");
                dispose();
            } catch (Exception e) {
                JOptionIcon.showErrorDialog(this, "Error deleting student: " + e.getMessage());
            }
        }
    }

    private void sendEmail() {
        try {
            ResultSet rs = MySQL.executeSearch("SELECT email, first_name, last_name FROM student WHERE student_id = '" + studentId + "'");
            if (rs.next()) {
                String email = rs.getString("email");
                String name = rs.getString("first_name") + " " + rs.getString("last_name");
                new StudentMailDialog((Frame) SwingUtilities.getWindowAncestor(this), email, name).setVisible(true);
            }
            rs.close();
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error loading student email: " + e.getMessage());
        }
    }

    private void generateQRCode() {
        if (studentId == null || studentId.trim().isEmpty()) {
            JOptionIcon.showErrorDialog(this, "Student ID is missing. Cannot generate QR code.");
            return;
        }

        try {
            ResultSet rs = MySQL.executeSearch("SELECT first_name, last_name FROM student WHERE student_id = '" + studentId + "'");
            if (rs.next()) {
                String name = rs.getString("first_name") + " " + rs.getString("last_name");
                lk.educore.schoolms.model.QRCodeGenerator.generateStudentQR(studentId, name);
                JOptionIcon.showSuccessDialog(this, "QR code generated successfully for " + name);
            }
            rs.close();
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error generating QR code: " + e.getMessage());
        }
    }

    private void viewAttendance() {
        Window parent = SwingUtilities.getWindowAncestor(this);
        new StudentAttendanceDialog((Frame) parent, studentId).setVisible(true);
    }

    private void viewFees() {
        // Switch to the Financial tab (index 3)
        Component c = getContentPane().getComponent(1);
        if (c instanceof JPanel) {
            Component inner = ((JPanel) c).getComponent(0);
            if (inner instanceof JTabbedPane) {
                ((JTabbedPane) inner).setSelectedIndex(3);
            }
        }
    }

    private void viewResults() {
        JOptionIcon.showInfoDialog(this, "View Results - Coming Soon");
    }
}
