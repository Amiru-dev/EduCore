package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.UserSession;

import javax.swing.*;
import java.awt.*;

/**
 * Help Dialog
 * User guide and help documentation - role-specific content
 */
public class HelpDialog extends JDialog {

    private String role;

    public HelpDialog(Frame parent) {
        this(parent, UserSession.get() != null ? UserSession.get().getRole() : "Admin");
    }

    public HelpDialog(Frame parent, String role) {
        super(parent, "Help - EduCore", true);
        this.role = role;
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
        titlePanel.add(titleLabel);
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
        
        String content;
        if ("Admin".equals(role)) {
            content = "Getting Started with EduCore - Admin\n\n" +
                "1. Dashboard Overview\n" +
                "   - View system statistics and quick actions\n" +
                "   - Navigate to different modules using the sidebar\n\n" +
                "2. Student Management\n" +
                "   - Register new students with auto-generated credentials\n" +
                "   - Manage student profiles and guardian information\n" +
                "   - Enable/disable student accounts\n" +
                "   - Generate QR codes for student IDs\n\n" +
                "3. Teacher Management\n" +
                "   - Add teachers with auto-generated passwords\n" +
                "   - Manage teacher profiles and status\n" +
                "   - Right-click rows to enable/disable teachers\n" +
                "   - Manage teacher assignments (classes & subjects)\n\n" +
                "4. Fee Management\n" +
                "   - Configure fee structures\n" +
                "   - Track payments and generate receipts\n\n" +
                "5. Library Management\n" +
                "   - Manage book inventory\n" +
                "   - Issue and return books\n\n" +
                "For admin-specific support, contact support@educore.com";
        } else if ("Teacher".equals(role)) {
            content = "Getting Started with EduCore - Teacher\n\n" +
                "1. Dashboard Overview\n" +
                "   - View your assigned classes and subjects\n" +
                "   - Navigate to teacher-specific modules\n\n" +
                "2. My Classes\n" +
                "   - View students in your assigned classes\n" +
                "   - Mark student attendance\n" +
                "   - View student information (read-only)\n\n" +
                "3. Attendance\n" +
                "   - Mark your own attendance\n" +
                "   - View attendance history\n\n" +
                "4. Library\n" +
                "   - Search and view book inventory\n" +
                "   - View issue records (read-only)\n\n" +
                "5. My Profile\n" +
                "   - View your personal information\n" +
                "   - Change your password\n\n" +
                "For teacher-specific support, contact support@educore.com";
        } else { // Student
            content = "Getting Started with EduCore - Student\n\n" +
                "1. Dashboard Overview\n" +
                "   - View your academic information\n" +
                "   - Navigate to student-specific modules\n\n" +
                "2. My Profile\n" +
                "   - View your personal information\n" +
                "   - Update your address\n" +
                "   - Change your password\n\n" +
                "3. Exams & Results\n" +
                "   - View your exam results\n" +
                "   - Check your grades and remarks\n\n" +
                "4. Attendance\n" +
                "   - View your attendance record\n\n" +
                "5. Fees\n" +
                "   - View your fee history\n" +
                "   - Check payment status\n\n" +
                "6. Library\n" +
                "   - Search and view available books\n\n" +
                "For student-specific support, contact support@educore.com";
        }
        
        JTextArea textArea = new JTextArea(content);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFeaturesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        String content;
        if ("Admin".equals(role)) {
            content = "EduCore Features - Admin\n\n" +
                "Student Management:\n" +
                "• Student registration with auto-generated credentials\n" +
                "• Guardian information management and reuse\n" +
                "• Student profile and address management\n" +
                "• QR code generation for student IDs\n" +
                "• Enable/disable student accounts\n\n" +
                "Teacher Management:\n" +
                "• Teacher profiles with auto-generated passwords\n" +
                "• Right-click to enable/disable teachers\n" +
                "• Attendance tracking\n" +
                "• Leave management\n" +
                "• Salary management\n" +
                "• Class and subject assignments\n\n" +
                "Fee Management:\n" +
                "• Fee structure configuration\n" +
                "• Payment tracking and receipt generation\n" +
                "• Fee analytics and reports\n\n" +
                "Library Management:\n" +
                "• Book inventory management\n" +
                "• Issue and return tracking\n" +
                "• Barcode generation\n\n" +
                "Exam Management:\n" +
                "• Exam scheduling\n" +
                "• Marks entry and grade calculation\n" +
                "• Result sheet generation";
        } else if ("Teacher".equals(role)) {
            content = "EduCore Features - Teacher\n\n" +
                "My Classes:\n" +
                "• View assigned classes and students\n" +
                "• Mark student attendance\n" +
                "• View student profiles (read-only)\n\n" +
                "Attendance:\n" +
                "• Mark your own attendance\n" +
                "• View attendance history\n\n" +
                "Library:\n" +
                "• Search and view book inventory\n" +
                "• View issue records (read-only)\n\n" +
                "My Profile:\n" +
                "• View personal and employment details\n" +
                "• View assigned classes and subjects\n" +
                "• Change your password\n\n" +
                "Note: As a teacher, you have read-only access to student,\n" +
                "library, and fee management modules.";
        } else { // Student
            content = "EduCore Features - Student\n\n" +
                "My Profile:\n" +
                "• View personal information\n" +
                "• Update residential address\n" +
                "• Change your password\n\n" +
                "Exams & Results:\n" +
                "• View exam results\n" +
                "• Check grades and remarks\n" +
                "• View academic performance\n\n" +
                "Attendance:\n" +
                "• View attendance record\n" +
                "• Check attendance percentage\n\n" +
                "Fees:\n" +
                "• View fee history\n" +
                "• Check payment status\n" +
                "• View fee receipts\n\n" +
                "Library:\n" +
                "• Search and view available books\n" +
                "• View book details\n\n" +
                "Events:\n" +
                "• View upcoming school events";
        }
        
        JTextArea textArea = new JTextArea(content);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFAQPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        String content;
        if ("Admin".equals(role)) {
            content = "Frequently Asked Questions - Admin\n\n" +
                "Q: How do I add a new student?\n" +
                "A: Go to Student Management > Add Student. Fill in the details\n" +
                "   and the system will auto-generate credentials.\n\n" +
                "Q: How do I reuse an existing guardian?\n" +
                "A: When registering a student, enter the guardian's NIC and\n" +
                "   click Search. Select the existing guardian to reuse.\n\n" +
                "Q: How do I enable a disabled teacher?\n" +
                "A: Right-click on the teacher row and select Enable Teacher.\n\n" +
                "Q: Can I edit student passwords?\n" +
                "A: No, students must change their own password via My Profile.\n\n" +
                "Q: How do I assign classes to teachers?\n" +
                "A: Go to Teacher Management > Assignments tab to assign\n" +
                "   classes and subjects.\n\n" +
                "Q: How do I backup the database?\n" +
                "A: Use MySQL backup tools to backup the school_management_db.";
        } else if ("Teacher".equals(role)) {
            content = "Frequently Asked Questions - Teacher\n\n" +
                "Q: How do I change my password?\n" +
                "A: Go to My Profile > Change Password. Enter your current\n" +
                "   password and set a new one.\n\n" +
                "Q: Can I edit student information?\n" +
                "A: No, teachers have read-only access to student profiles.\n\n" +
                "Q: How do I mark student attendance?\n" +
                "A: Go to My Classes > Attendance tab and mark attendance\n" +
                "   for your assigned classes.\n\n" +
                "Q: Can I issue books from the library?\n" +
                "A: No, teachers have read-only access to library management.\n\n" +
                "Q: How do I view my assigned classes?\n" +
                "A: Go to My Profile to view your assigned classes and subjects.\n\n" +
                "Q: Can I generate reports?\n" +
                "A: Report generation is an admin-only feature.";
        } else { // Student
            content = "Frequently Asked Questions - Student\n\n" +
                "Q: How do I change my password?\n" +
                "A: Go to My Profile > Change Password. Enter your current\n" +
                "   password and set a new one.\n\n" +
                "Q: How do I update my address?\n" +
                "A: Go to My Profile > Update Address and enter your new address.\n\n" +
                "Q: Where can I view my exam results?\n" +
                "A: Go to Exams & Results tab to view your grades and remarks.\n\n" +
                "Q: How do I check my fee status?\n" +
                "A: Go to Fees tab to view your fee history and payment status.\n\n" +
                "Q: Can I borrow books from the library?\n" +
                "A: Contact the library staff to borrow books. You can view\n" +
                "   available books in the Library tab.\n\n" +
                "Q: What if I forget my password?\n" +
                "A: Contact the school administrator to reset your password.";
        }
        
        JTextArea textArea = new JTextArea(content);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        return panel;
    }
}