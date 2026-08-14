package lk.educore.schoolms.email;

import lk.educore.schoolms.exceptions.EmailException;
import lk.educore.schoolms.model.QRCodeGenerator;
import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

/**
 * QR Code Email Sender
 * Sends emails with QR code attachments using JavaMail API
 */
public class SendQREmail {

    // Email configuration - UPDATE WITH YOUR GMAIL APP PASSWORD
    private static final String EMAIL_HOST = "smtp.gmail.com";
    private static final String EMAIL_PORT = "587";
    private static final String EMAIL_USERNAME = "asuspro5001@gmail.com"; // UPDATE WITH YOUR EMAIL
    private static final String EMAIL_PASSWORD = "your-app-password"; // UPDATE WITH YOUR GMAIL APP PASSWORD
    private static final String FROM_EMAIL = "asuspro5001@gmail.com"; // UPDATE WITH YOUR EMAIL

    /**
     * Send student ID card with QR code via email
     * @param to Recipient email address
     * @param studentId Student ID
     * @param studentName Student full name
     * @param grade Student grade
     * @throws EmailException if email sending fails
     */
    public static void sendStudentIDCard(String to, String studentId, String studentName, String grade) throws EmailException {
        try {
            // Validate recipient
            if (to == null || to.trim().isEmpty()) {
                throw new EmailException("Recipient email is required", EmailException.ErrorType.INVALID_RECIPIENT, to);
            }

            // Generate QR code
            String qrPath = QRCodeGenerator.generateStudentQR(studentId, studentName);
            if (qrPath == null) {
                throw new EmailException("Failed to generate QR code", EmailException.ErrorType.ATTACHMENT_ERROR, to);
            }

            // Create email content
            String subject = "Your Student ID Card - " + studentName;
            String message = "Dear " + studentName + ",\n\n" +
                           "Please find attached your student ID card with QR code.\n\n" +
                           "Student ID: " + studentId + "\n" +
                           "Grade: " + grade + "\n\n" +
                           "Please keep this ID card safe and present it when required.\n\n" +
                           "Best regards,\n" +
                           "Central College Anuradhapura\n" +
                           "EduCore School Management System";

            // Send email with QR attachment
            SendFileEmail.sendEmailWithAttachment(to, subject, message, qrPath);

        } catch (EmailException e) {
            throw e;
        } catch (Exception e) {
            throw new EmailException("Failed to send student ID card email", EmailException.ErrorType.UNKNOWN, to, e);
        }
    }

    /**
     * Send teacher ID card with QR code via email
     * @param to Recipient email address
     * @param teacherNIC Teacher NIC
     * @param teacherName Teacher full name
     * @param teacherType Teacher type
     * @throws EmailException if email sending fails
     */
    public static void sendTeacherIDCard(String to, String teacherNIC, String teacherName, String teacherType) throws EmailException {
        try {
            // Validate recipient
            if (to == null || to.trim().isEmpty()) {
                throw new EmailException("Recipient email is required", EmailException.ErrorType.INVALID_RECIPIENT, to);
            }

            // Generate QR code
            String qrPath = QRCodeGenerator.generateTeacherQR(teacherNIC, teacherName);
            if (qrPath == null) {
                throw new EmailException("Failed to generate QR code", EmailException.ErrorType.ATTACHMENT_ERROR, to);
            }

            // Create email content
            String subject = "Your Teacher ID Card - " + teacherName;
            String message = "Dear " + teacherName + ",\n\n" +
                           "Please find attached your teacher ID card with QR code.\n\n" +
                           "Teacher NIC: " + teacherNIC + "\n" +
                           "Type: " + teacherType + "\n\n" +
                           "Please keep this ID card safe and present it when required.\n\n" +
                           "Best regards,\n" +
                           "Central College Anuradhapura\n" +
                           "EduCore School Management System";

            // Send email with QR attachment
            SendFileEmail.sendEmailWithAttachment(to, subject, message, qrPath);

        } catch (EmailException e) {
            throw e;
        } catch (Exception e) {
            throw new EmailException("Failed to send teacher ID card email", EmailException.ErrorType.UNKNOWN, to, e);
        }
    }

    /**
     * Send custom QR code via email
     * @param to Recipient email address
     * @param subject Email subject
     * @param message Email body content
     * @param qrContent Content to encode in QR
     * @param fileName QR code file name
     * @throws EmailException if email sending fails
     */
    public static void sendCustomQR(String to, String subject, String message, String qrContent, String fileName) throws EmailException {
        try {
            // Validate recipient
            if (to == null || to.trim().isEmpty()) {
                throw new EmailException("Recipient email is required", EmailException.ErrorType.INVALID_RECIPIENT, to);
            }

            // Generate QR code
            String qrPath = QRCodeGenerator.generateCustomQR(qrContent, fileName);
            if (qrPath == null) {
                throw new EmailException("Failed to generate QR code", EmailException.ErrorType.ATTACHMENT_ERROR, to);
            }

            // Send email with QR attachment
            SendFileEmail.sendEmailWithAttachment(to, subject, message, qrPath);

        } catch (EmailException e) {
            throw e;
        } catch (Exception e) {
            throw new EmailException("Failed to send custom QR email", EmailException.ErrorType.UNKNOWN, to, e);
        }
    }

    /**
     * Send HTML email with QR code attachment
     * @param to Recipient email address
     * @param subject Email subject
     * @param htmlContent HTML email body content
     * @param studentId Student ID
     * @param studentName Student full name
     * @throws EmailException if email sending fails
     */
    public static void sendHTMLStudentIDCard(String to, String subject, String htmlContent, String studentId, String studentName) throws EmailException {
        try {
            // Validate recipient
            if (to == null || to.trim().isEmpty()) {
                throw new EmailException("Recipient email is required", EmailException.ErrorType.INVALID_RECIPIENT, to);
            }

            // Generate QR code
            String qrPath = QRCodeGenerator.generateStudentQR(studentId, studentName);
            if (qrPath == null) {
                throw new EmailException("Failed to generate QR code", EmailException.ErrorType.ATTACHMENT_ERROR, to);
            }

            // Send HTML email with QR attachment
            SendFileEmail.sendHTMLEmailWithAttachment(to, subject, htmlContent, qrPath);

        } catch (EmailException e) {
            throw e;
        } catch (Exception e) {
            throw new EmailException("Failed to send HTML student ID card email", EmailException.ErrorType.UNKNOWN, to, e);
        }
    }

    /**
     * Send student registration email with QR code
     * @param to Recipient email address
     * @param studentId Student ID
     * @param studentName Student full name
     * @param grade Student grade
     * @param username Login username
     * @param temporaryPassword Temporary password
     * @throws EmailException if email sending fails
     */
    public static void sendRegistrationEmailWithQR(String to, String studentId, String studentName, String grade, 
                                                    String username, String temporaryPassword) throws EmailException {
        try {
            // Validate recipient
            if (to == null || to.trim().isEmpty()) {
                throw new EmailException("Recipient email is required", EmailException.ErrorType.INVALID_RECIPIENT, to);
            }

            // Generate QR code
            String qrPath = QRCodeGenerator.generateStudentQR(studentId, studentName);
            if (qrPath == null) {
                throw new EmailException("Failed to generate QR code", EmailException.ErrorType.ATTACHMENT_ERROR, to);
            }

            // Create HTML email content
            String htmlContent = "<html><head><style>" +
                               "body { font-family: Arial, sans-serif; }" +
                               ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                               ".header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }" +
                               ".content { padding: 20px; background-color: #f9f9f9; }" +
                               ".footer { padding: 20px; text-align: center; color: #666; }" +
                               "</style></head><body>" +
                               "<div class='container'>" +
                               "<div class='header'>" +
                               "<h1>Welcome to Central College Anuradhapura</h1>" +
                               "</div>" +
                               "<div class='content'>" +
                               "<h2>Student Registration Confirmation</h2>" +
                               "<p>Dear " + studentName + ",</p>" +
                               "<p>We are pleased to confirm your registration at Central College Anuradhapura.</p>" +
                               "<h3>Your Student Details:</h3>" +
                               "<ul>" +
                               "<li><strong>Student ID:</strong> " + studentId + "</li>" +
                               "<li><strong>Grade:</strong> " + grade + "</li>" +
                               "<li><strong>Username:</strong> " + username + "</li>" +
                               "<li><strong>Temporary Password:</strong> " + temporaryPassword + "</li>" +
                               "</ul>" +
                               "<p>Please find your student ID card with QR code attached to this email.</p>" +
                               "<p><strong>Important:</strong> Please change your password after your first login.</p>" +
                               "</div>" +
                               "<div class='footer'>" +
                               "<p>EduCore School Management System</p>" +
                               "<p>Contact: +94 78 476 6700 | Email: sheshandanuksha19@gmail.com</p>" +
                               "</div>" +
                               "</div></body></html>";

            String subject = "Student Registration Confirmation - " + studentName;

            // Send HTML email with QR attachment
            SendFileEmail.sendHTMLEmailWithAttachment(to, subject, htmlContent, qrPath);

        } catch (EmailException e) {
            throw e;
        } catch (Exception e) {
            throw new EmailException("Failed to send registration email with QR", EmailException.ErrorType.UNKNOWN, to, e);
        }
    }

    /**
     * Send multiple QR codes in a single email
     * @param to Recipient email address
     * @param subject Email subject
     * @param message Email body content
     * @param qrData Array of QR data objects containing content and filename
     * @throws EmailException if email sending fails
     */
    public static void sendMultipleQRs(String to, String subject, String message, QRData[] qrData) throws EmailException {
        try {
            // Validate recipient
            if (to == null || to.trim().isEmpty()) {
                throw new EmailException("Recipient email is required", EmailException.ErrorType.INVALID_RECIPIENT, to);
            }

            // Generate all QR codes
            String[] filePaths = new String[qrData.length];
            for (int i = 0; i < qrData.length; i++) {
                String qrPath = QRCodeGenerator.generateCustomQR(qrData[i].content, qrData[i].fileName);
                if (qrPath == null) {
                    throw new EmailException("Failed to generate QR code: " + qrData[i].fileName, 
                                           EmailException.ErrorType.ATTACHMENT_ERROR, to);
                }
                filePaths[i] = qrPath;
            }

            // Send email with multiple QR attachments
            SendFileEmail.sendEmailWithMultipleAttachments(to, subject, message, filePaths);

        } catch (EmailException e) {
            throw e;
        } catch (Exception e) {
            throw new EmailException("Failed to send multiple QR codes email", EmailException.ErrorType.UNKNOWN, to, e);
        }
    }

    /**
     * Inner class to hold QR data
     */
    public static class QRData {
        public String content;
        public String fileName;

        public QRData(String content, String fileName) {
            this.content = content;
            this.fileName = fileName;
        }
    }
}