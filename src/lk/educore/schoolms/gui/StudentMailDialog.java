package lk.educore.schoolms.gui;

import lk.educore.schoolms.email.SendEmail;
import lk.educore.schoolms.email.SendFileEmail;
import lk.educore.schoolms.validate.JOptionIcon;
import lk.educore.schoolms.validate.DataValidator;
import lk.educore.schoolms.exceptions.ValidationException;

import javax.swing.*;
import java.awt.*;

/**
 * Student Mail Dialog
 * Dialog for sending emails to students
 */
public class StudentMailDialog extends JDialog {

    private String studentEmail;
    private String studentName;
    private JTextField txtSubject;
    private JTextArea txtMessage;
    private JTextField txtAttachment;
    private JCheckBox chkAttachFile;

    public StudentMailDialog(Frame parent, String studentEmail, String studentName) {
        super(parent, "Send Email to Student", true);
        this.studentEmail = studentEmail;
        this.studentName = studentName;
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(500, 400);
        setLayout(new BorderLayout(10, 10));

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Recipient info
        JPanel recipientPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        recipientPanel.add(new JLabel("To: " + studentName + " (" + studentEmail + ")"));
        mainPanel.add(recipientPanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Subject
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Subject:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtSubject = new JTextField(30);
        formPanel.add(txtSubject, gbc);

        // Message
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Message:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtMessage = new JTextArea(10, 30);
        txtMessage.setLineWrap(true);
        txtMessage.setWrapStyleWord(true);
        JScrollPane messageScroll = new JScrollPane(txtMessage);
        formPanel.add(messageScroll, gbc);

        // Attachment
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Attachment:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JPanel attachmentPanel = new JPanel(new BorderLayout(5, 0));
        
        chkAttachFile = new JCheckBox("Attach file");
        chkAttachFile.addActionListener(e -> toggleAttachmentField());
        
        txtAttachment = new JTextField(20);
        txtAttachment.setEnabled(false);
        
        JButton btnBrowse = new JButton("Browse");
        btnBrowse.setEnabled(false);
        btnBrowse.addActionListener(e -> browseAttachment());
        
        attachmentPanel.add(chkAttachFile, BorderLayout.WEST);
        attachmentPanel.add(txtAttachment, BorderLayout.CENTER);
        attachmentPanel.add(btnBrowse, BorderLayout.EAST);
        
        formPanel.add(attachmentPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnSend = new JButton("Send Email");
        btnSend.addActionListener(e -> sendEmail());
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());

        buttonPanel.add(btnSend);
        buttonPanel.add(btnCancel);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void toggleAttachmentField() {
        boolean enabled = chkAttachFile.isSelected();
        txtAttachment.setEnabled(enabled);
    }

    private void browseAttachment() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            txtAttachment.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void sendEmail() {
        try {
            String subject = txtSubject.getText().trim();
            String message = txtMessage.getText().trim();
            
            DataValidator.isRequired(subject, "Subject");
            DataValidator.isRequired(message, "Message");
            
            if (chkAttachFile.isSelected()) {
                String attachmentPath = txtAttachment.getText().trim();
                DataValidator.isRequired(attachmentPath, "Attachment");
                SendFileEmail.sendEmailWithAttachment(studentEmail, subject, message, attachmentPath);
            } else {
                SendEmail.sendEmail(studentEmail, subject, message);
            }
            
            JOptionIcon.showSuccessDialog(this, "Email sent successfully!");
            dispose();
            
        } catch (ValidationException e) {
            JOptionIcon.showWarningDialog(this, e.getUserFriendlyMessage());
        } catch (Exception e) {
            JOptionIcon.showErrorDialog(this, "Error sending email: " + e.getMessage());
        }
    }
}