package lk.educore.schoolms.gui;

import lk.educore.schoolms.model.MySQL;
import lk.educore.schoolms.validate.JOptionIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * About Dialog
 * Application information and credits
 */
public class AboutDialog extends JDialog {

    public AboutDialog(Frame parent) {
        super(parent, "About EduCore", true);
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(400, 300);
        setLayout(new BorderLayout(10, 10));

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Logo and title
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        
        JLabel logoLabel = new JLabel("🏫");
        logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        headerPanel.add(logoLabel, BorderLayout.WEST);
        
        JPanel titlePanel = new JPanel(new GridLayout(3, 1));
        JLabel titleLabel = new JLabel("EduCore");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        JLabel subtitleLabel = new JLabel("School Management System");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JLabel versionLabel = new JLabel("Version 1.0.0");
        versionLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        versionLabel.setForeground(Color.GRAY);
        
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        titlePanel.add(versionLabel);
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Description
        JTextArea descriptionArea = new JTextArea(
            "EduCore School Management System is a comprehensive solution " +
            "for managing all aspects of school administration including " +
            "student records, teacher management, fee collection, exams, " +
            "transport, library, and more.\n\n" +
            "Features:\n" +
            "• Student and Teacher Management\n" +
            "• Fee Collection and Receipt Generation\n" +
            "• Exam and Results Management\n" +
            "• Transport Management\n" +
            "• Library Management\n" +
            "• QR Code and Barcode Generation\n" +
            "• Email Integration\n" +
            "• Report Generation"
        );
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        mainPanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);

        // Copyright
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel copyrightLabel = new JLabel("© 2025 EduCore Software. All rights reserved.");
        copyrightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        copyrightLabel.setForeground(Color.GRAY);
        footerPanel.add(copyrightLabel);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        // Close button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());
        buttonPanel.add(btnClose);
        mainPanel.add(buttonPanel, BorderLayout.PAGE_END);

        add(mainPanel);
    }
}