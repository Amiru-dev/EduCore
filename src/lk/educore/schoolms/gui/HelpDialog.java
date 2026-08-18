package lk.educore.schoolms.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Help Dialog
 * User guide and help documentation
 */
public class HelpDialog extends JDialog {

    public HelpDialog(Frame parent) {
        super(parent, "Help - EduCore", true);
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(600, 500);
        setLayout(new BorderLayout(10, 10));

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("EduCore User Guide");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titlePanel.add(titlePanel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Help content
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Getting Started tab
        JPanel gettingStartedPanel = createGettingStartedPanel();
        tabbedPane.addTab("Getting Started", gettingStartedPanel);
        
        // Features tab
        JPanel featuresPanel = createFeaturesPanel();
        tabbedPane.addTab("Features", featuresPanel);
        
        // FAQ tab
        JPanel faqPanel = createFAQPanel();
        tabbedPane.addTab("FAQ", faqPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Close button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());
        buttonPanel.add(btnClose);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createGettingStartedPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        JTextArea textArea = new JTextArea(
            "Getting Started with EduCore\n\n" +
            "1. Database Setup\n" +
            "   - On first launch, configure your MySQL database connection\n" +
            "   - Enter host, port, database name, username, and password\n" +
            "   - Test the connection before saving\n\n" +
            "2. User Login\n" +
            "   - Login with Admin credentials (default: admin/admin)\n" +
            "   - Select your role: Admin, Teacher, or Student\n\n" +
            "3. Dashboard\n" +
            "   - View overview statistics\n" +
            "   - Navigate to different modules using the sidebar\n\n" +
            "4. Student Management\n" +
            "   - Register new students\n" +
            "   - View and edit student details\n" +
            "   - Generate QR codes for students\n\n" +
            "5. Teacher Management\n" +
            "   - Add teachers to the system\n" +
            "   - Manage teacher attendance\n" +
            "   - Configure salary options\n\n" +
            "6. Fee Management\n" +
            "   - Create fee records\n" +
            "   - Mark fees as paid\n" +
            "   - Generate receipts\n\n" +
            "For more information, contact support at support@educore.com"
        );
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFeaturesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        JTextArea textArea = new JTextArea(
            "EduCore Features\n\n" +
            "Student Management:\n" +
            "• Student registration with auto-generated IDs\n" +
            "• Guardian information management\n" +
            "• Student profile and address management\n" +
            "• QR code generation for student IDs\n\n" +
            "Teacher Management:\n" +
            "• Teacher profiles and salary management\n" +
            "• Attendance tracking with QR scanning\n" +
            "• Leave management\n\n" +
            "Fee Management:\n" +
            "• Fee structure configuration\n" +
            "• Payment tracking and receipt generation\n" +
            "• Fee analytics and reports\n\n" +
            "Exam Management:\n" +
            "• Exam scheduling\n" +
            "• Marks entry and grade calculation\n" +
            "• Result sheet generation\n\n" +
            "Transport Management:\n" +
            "• Vehicle and route management\n" +
            "• Student-transport assignment\n\n" +
            "Library Management:\n" +
            "• Book inventory management\n" +
            "• Issue and return tracking\n" +
            "• Barcode generation\n\n" +
            "Additional Features:\n" +
            "• Email integration for notifications\n" +
            "• Report generation with JasperReports\n" +
            "• Database backup and restore\n" +
            "• Role-based access control"
        );
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFAQPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        JTextArea textArea = new JTextArea(
            "Frequently Asked Questions\n\n" +
            "Q: How do I reset my password?\n" +
            "A: Contact the system administrator to reset your password.\n\n" +
            "Q: Can I generate reports for a specific date range?\n" +
            "A: Yes, most reports support date range filtering.\n\n" +
            "Q: How do I backup the database?\n" +
            "A: Go to Tools > Backup & Restore to create a backup.\n\n" +
            "Q: Can I import student data from Excel?\n" +
            "A: This feature is coming in a future update.\n\n" +
            "Q: How do I configure email settings?\n" +
            "A: Email settings are configured in the application.properties file.\n\n" +
            "Q: What database is required?\n" +
            "A: EduCore requires MySQL 5.7 or higher.\n\n" +
            "Q: Can multiple users access the system simultaneously?\n" +
            "A: Yes, EduCore supports concurrent user access.\n\n" +
            "Q: How do I customize the school logo?\n" +
            "A: Replace the logo file in the resources folder.\n\n" +
            "Q: Is the data encrypted?\n" +
            "A: Database credentials are encrypted using AES-128 encryption.\n\n" +
            "Q: Where can I get technical support?\n" +
            "A: Contact support@educore.com for technical assistance."
        );
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        return panel;
    }
}