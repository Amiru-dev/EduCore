package lk.educore.schoolms.email;

import lk.educore.schoolms.exceptions.EmailException;
import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

/**
 * File Attachment Email Sender Sends emails with file attachments using
 * JavaMail API
 */
public class SendFileEmail {

    // Email configuration 
    private static final String EMAIL_HOST = "smtp.gmail.com";
    private static final String EMAIL_PORT = "465";
    private static final String EMAIL_USERNAME = "asuspro5001@gmail.com";
    private static final String EMAIL_PASSWORD = "xdhmaqxxfflvhxpg";
    private static final String FROM_EMAIL = "asuspro5001@gmail.com";

    /**
     * Send email with single file attachment
     *
     * @param to Recipient email address
     * @param subject Email subject
     * @param message Email body content
     * @param filePath Path to file to attach
     * @throws EmailException if email sending fails
     */
    public static void sendEmailWithAttachment(String to, String subject, String message, String filePath) throws EmailException {
        try {
            // Validate recipient
            if (to == null || to.trim().isEmpty()) {
                throw new EmailException("Recipient email is required", EmailException.ErrorType.INVALID_RECIPIENT, to);
            }

            // Validate file
            File file = new File(filePath);
            if (!file.exists()) {
                throw new EmailException("Attachment file not found: " + filePath, EmailException.ErrorType.ATTACHMENT_ERROR, to);
            }

            // Set up email properties
            Properties props = new Properties();
            props.put("mail.smtp.host", EMAIL_HOST);
            props.put("mail.smtp.port", EMAIL_PORT);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust", EMAIL_HOST);
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.connectiontimeout", "10000");
            props.put("mail.smtp.timeout", "10000");
            props.put("mail.smtp.writetimeout", "10000");

            // Create session with authentication
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
                }
            });

            // Create message
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(FROM_EMAIL));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            mimeMessage.setSubject(subject);

            // Create multipart message
            MimeMultipart multipart = new MimeMultipart();

            // Add text part
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(message);
            multipart.addBodyPart(textPart);

            // Add attachment part
            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource source = new FileDataSource(file);
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName(file.getName());
            multipart.addBodyPart(attachmentPart);

            // Set content
            mimeMessage.setContent(multipart);

            // Send email
            Transport.send(mimeMessage);

        } catch (AuthenticationFailedException e) {
            throw new EmailException("Email authentication failed", EmailException.ErrorType.AUTHENTICATION_FAILED, to, e);
        } catch (MessagingException e) {
            if (e.getMessage() != null && e.getMessage().contains("Invalid Addresses")) {
                throw new EmailException("Invalid recipient email address", EmailException.ErrorType.INVALID_RECIPIENT, to, e);
            }
            throw new EmailException("Failed to send email with attachment", EmailException.ErrorType.ATTACHMENT_ERROR, to, e);
        } catch (Exception e) {
            throw new EmailException("Unexpected error sending email with attachment", EmailException.ErrorType.UNKNOWN, to, e);
        }
    }

    /**
     * Send email with multiple file attachments
     *
     * @param to Recipient email address
     * @param subject Email subject
     * @param message Email body content
     * @param filePaths Array of file paths to attach
     * @throws EmailException if email sending fails
     */
    public static void sendEmailWithMultipleAttachments(String to, String subject, String message, String[] filePaths) throws EmailException {
        try {
            // Validate recipient
            if (to == null || to.trim().isEmpty()) {
                throw new EmailException("Recipient email is required", EmailException.ErrorType.INVALID_RECIPIENT, to);
            }

            // Validate files
            for (String filePath : filePaths) {
                File file = new File(filePath);
                if (!file.exists()) {
                    throw new EmailException("Attachment file not found: " + filePath, EmailException.ErrorType.ATTACHMENT_ERROR, to);
                }
            }

            // Set up email properties
            Properties props = new Properties();
            props.put("mail.smtp.host", EMAIL_HOST);
            props.put("mail.smtp.port", EMAIL_PORT);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust", EMAIL_HOST);
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.connectiontimeout", "10000");
            props.put("mail.smtp.timeout", "10000");
            props.put("mail.smtp.writetimeout", "10000");

            // Create session with authentication
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
                }
            });

            // Create message
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(FROM_EMAIL));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            mimeMessage.setSubject(subject);

            // Create multipart message
            MimeMultipart multipart = new MimeMultipart();

            // Add text part
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(message);
            multipart.addBodyPart(textPart);

            // Add attachment parts
            for (String filePath : filePaths) {
                File file = new File(filePath);
                MimeBodyPart attachmentPart = new MimeBodyPart();
                DataSource source = new FileDataSource(file);
                attachmentPart.setDataHandler(new DataHandler(source));
                attachmentPart.setFileName(file.getName());
                multipart.addBodyPart(attachmentPart);
            }

            // Set content
            mimeMessage.setContent(multipart);

            // Send email
            Transport.send(mimeMessage);

        } catch (AuthenticationFailedException e) {
            throw new EmailException("Email authentication failed", EmailException.ErrorType.AUTHENTICATION_FAILED, to, e);
        } catch (MessagingException e) {
            if (e.getMessage() != null && e.getMessage().contains("Invalid Addresses")) {
                throw new EmailException("Invalid recipient email address", EmailException.ErrorType.INVALID_RECIPIENT, to, e);
            }
            throw new EmailException("Failed to send email with attachments", EmailException.ErrorType.ATTACHMENT_ERROR, to, e);
        } catch (Exception e) {
            throw new EmailException("Unexpected error sending email with attachments", EmailException.ErrorType.UNKNOWN, to, e);
        }
    }

    /**
     * Send HTML email with file attachment
     *
     * @param to Recipient email address
     * @param subject Email subject
     * @param htmlContent HTML email body content
     * @param filePath Path to file to attach
     * @throws EmailException if email sending fails
     */
    public static void sendHTMLEmailWithAttachment(String to, String subject, String htmlContent, String filePath) throws EmailException {
        try {
            // Validate recipient
            if (to == null || to.trim().isEmpty()) {
                throw new EmailException("Recipient email is required", EmailException.ErrorType.INVALID_RECIPIENT, to);
            }

            // Validate file
            File file = new File(filePath);
            if (!file.exists()) {
                throw new EmailException("Attachment file not found: " + filePath, EmailException.ErrorType.ATTACHMENT_ERROR, to);
            }

            // Set up email properties
            Properties props = new Properties();
            props.put("mail.smtp.host", EMAIL_HOST);
            props.put("mail.smtp.port", EMAIL_PORT);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust", EMAIL_HOST);
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.connectiontimeout", "10000");
            props.put("mail.smtp.timeout", "10000");
            props.put("mail.smtp.writetimeout", "10000");

            // Create session with authentication
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
                }
            });

            // Create message
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(FROM_EMAIL));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            mimeMessage.setSubject(subject);

            // Create multipart message
            MimeMultipart multipart = new MimeMultipart();

            // Add HTML part
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlContent, "text/html");
            multipart.addBodyPart(htmlPart);

            // Add attachment part
            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource source = new FileDataSource(file);
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName(file.getName());
            multipart.addBodyPart(attachmentPart);

            // Set content
            mimeMessage.setContent(multipart);

            // Send email
            Transport.send(mimeMessage);

        } catch (AuthenticationFailedException e) {
            throw new EmailException("Email authentication failed", EmailException.ErrorType.AUTHENTICATION_FAILED, to, e);
        } catch (MessagingException e) {
            if (e.getMessage() != null && e.getMessage().contains("Invalid Addresses")) {
                throw new EmailException("Invalid recipient email address", EmailException.ErrorType.INVALID_RECIPIENT, to, e);
            }
            throw new EmailException("Failed to send HTML email with attachment", EmailException.ErrorType.ATTACHMENT_ERROR, to, e);
        } catch (Exception e) {
            throw new EmailException("Unexpected error sending HTML email with attachment", EmailException.ErrorType.UNKNOWN, to, e);
        }
    }

    /**
     * Send email with byte array attachment
     *
     * @param to Recipient email address
     * @param subject Email subject
     * @param message Email body content
     * @param fileBytes File content as byte array
     * @param fileName Name to give the attachment
     * @throws EmailException if email sending fails
     */
    public static void sendEmailWithByteArrayAttachment(String to, String subject, String message, byte[] fileBytes, String fileName) throws EmailException {
        try {
            // Validate recipient
            if (to == null || to.trim().isEmpty()) {
                throw new EmailException("Recipient email is required", EmailException.ErrorType.INVALID_RECIPIENT, to);
            }

            // Validate byte array
            if (fileBytes == null || fileBytes.length == 0) {
                throw new EmailException("Attachment data is empty", EmailException.ErrorType.ATTACHMENT_ERROR, to);
            }

            // Set up email properties
            Properties props = new Properties();
            props.put("mail.smtp.host", EMAIL_HOST);
            props.put("mail.smtp.port", EMAIL_PORT);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust", EMAIL_HOST);
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.connectiontimeout", "10000");
            props.put("mail.smtp.timeout", "10000");
            props.put("mail.smtp.writetimeout", "10000");

            // Create session with authentication
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
                }
            });

            // Create message
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(FROM_EMAIL));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            mimeMessage.setSubject(subject);

            // Create multipart message
            MimeMultipart multipart = new MimeMultipart();

            // Add text part
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(message);
            multipart.addBodyPart(textPart);

            // Add attachment part from byte array
            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource source = new ByteArrayDataSource(fileBytes, "application/octet-stream");
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName(fileName);
            multipart.addBodyPart(attachmentPart);

            // Set content
            mimeMessage.setContent(multipart);

            // Send email
            Transport.send(mimeMessage);

        } catch (AuthenticationFailedException e) {
            throw new EmailException("Email authentication failed", EmailException.ErrorType.AUTHENTICATION_FAILED, to, e);
        } catch (MessagingException e) {
            if (e.getMessage() != null && e.getMessage().contains("Invalid Addresses")) {
                throw new EmailException("Invalid recipient email address", EmailException.ErrorType.INVALID_RECIPIENT, to, e);
            }
            throw new EmailException("Failed to send email with byte array attachment", EmailException.ErrorType.ATTACHMENT_ERROR, to, e);
        } catch (Exception e) {
            throw new EmailException("Unexpected error sending email with byte array attachment", EmailException.ErrorType.UNKNOWN, to, e);
        }
    }

    /**
     * ByteArrayDataSource for handling byte array attachments
     */
    private static class ByteArrayDataSource implements DataSource {

        private final byte[] data;
        private final String type;

        public ByteArrayDataSource(byte[] data, String type) {
            this.data = data;
            this.type = type;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Cannot write to this data source");
        }

        @Override
        public String getContentType() {
            return type;
        }

        @Override
        public String getName() {
            return "ByteArrayDataSource";
        }
    }
}
