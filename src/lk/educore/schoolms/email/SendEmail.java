package lk.educore.schoolms.email;

import lk.educore.schoolms.exceptions.EmailException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * Simple Email Sender Sends text emails using JavaMail API
 */
public class SendEmail {

    // Email configuration 
    private static final String EMAIL_HOST = "smtp.gmail.com";
    private static final String EMAIL_PORT = "465";
    private static final String EMAIL_USERNAME = "asuspro162@gmail.com";
    private static final String EMAIL_PASSWORD = "ieovbnmqbrdtjjrp";
    private static final String FROM_EMAIL = "asuspro162@gmail.com";

    /**
     * Send simple text email
     *
     * @param to Recipient email address
     * @param subject Email subject
     * @param message Email body content
     * @throws EmailException if email sending fails
     */
    public static void sendEmail(String to, String subject, String message) throws EmailException {
        try {
            // Validate recipient
            if (to == null || to.trim().isEmpty()) {
                throw new EmailException("Recipient email is required", EmailException.ErrorType.INVALID_RECIPIENT, to);
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
            mimeMessage.setText(message);

            // Send email
            Transport.send(mimeMessage);

        } catch (AuthenticationFailedException e) {
            throw new EmailException("Email authentication failed", EmailException.ErrorType.AUTHENTICATION_FAILED, to, e);
        } catch (MessagingException e) {
            if (e.getMessage() != null && e.getMessage().contains("Invalid Addresses")) {
                throw new EmailException("Invalid recipient email address", EmailException.ErrorType.INVALID_RECIPIENT, to, e);
            }
            throw new EmailException("Failed to send email", EmailException.ErrorType.SENDING_FAILED, to, e);
        } catch (Exception e) {
            throw new EmailException("Unexpected error sending email", EmailException.ErrorType.UNKNOWN, to, e);
        }
    }

    /**
     * Send HTML email
     *
     * @param to Recipient email address
     * @param subject Email subject
     * @param htmlContent HTML email body content
     * @throws EmailException if email sending fails
     */
    public static void sendHTMLEmail(String to, String subject, String htmlContent) throws EmailException {
        try {
            // Validate recipient
            if (to == null || to.trim().isEmpty()) {
                throw new EmailException("Recipient email is required", EmailException.ErrorType.INVALID_RECIPIENT, to);
            }

            // Set up email properties
            Properties props = new Properties();
            props.put("mail.smtp.host", EMAIL_HOST);
            props.put("mail.smtp.port", EMAIL_PORT);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust", EMAIL_HOST);

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
            mimeMessage.setContent(htmlContent, "text/html");

            // Send email
            Transport.send(mimeMessage);

        } catch (AuthenticationFailedException e) {
            throw new EmailException("Email authentication failed", EmailException.ErrorType.AUTHENTICATION_FAILED, to, e);
        } catch (MessagingException e) {
            if (e.getMessage() != null && e.getMessage().contains("Invalid Addresses")) {
                throw new EmailException("Invalid recipient email address", EmailException.ErrorType.INVALID_RECIPIENT, to, e);
            }
            throw new EmailException("Failed to send email", EmailException.ErrorType.SENDING_FAILED, to, e);
        } catch (Exception e) {
            throw new EmailException("Unexpected error sending email", EmailException.ErrorType.UNKNOWN, to, e);
        }
    }

    /**
     * Send email to multiple recipients
     *
     * @param recipients Array of recipient email addresses
     * @param subject Email subject
     * @param message Email body content
     * @throws EmailException if email sending fails
     */
    public static void sendBulkEmail(String[] recipients, String subject, String message) throws EmailException {
        try {
            // Validate recipients
            if (recipients == null || recipients.length == 0) {
                throw new EmailException("Recipients list is empty", EmailException.ErrorType.INVALID_RECIPIENT);
            }

            // Set up email properties
            Properties props = new Properties();
            props.put("mail.smtp.host", EMAIL_HOST);
            props.put("mail.smtp.port", EMAIL_PORT);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust", EMAIL_HOST);

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

            // Add all recipients
            InternetAddress[] recipientAddresses = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++) {
                recipientAddresses[i] = new InternetAddress(recipients[i]);
            }
            mimeMessage.setRecipients(Message.RecipientType.TO, recipientAddresses);

            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);

            // Send email
            Transport.send(mimeMessage);

        } catch (AuthenticationFailedException e) {
            throw new EmailException("Email authentication failed", EmailException.ErrorType.AUTHENTICATION_FAILED, null, e);
        } catch (MessagingException e) {
            throw new EmailException("Failed to send bulk email", EmailException.ErrorType.SENDING_FAILED, null, e);
        } catch (Exception e) {
            throw new EmailException("Unexpected error sending bulk email", EmailException.ErrorType.UNKNOWN, null, e);
        }
    }

    /**
     * Update email credentials
     *
     * @param username Email username
     * @param password Email password (app password for Gmail)
     * @param fromEmail From email address
     */
    public static void updateCredentials(String username, String password, String fromEmail) {
        // This method would typically update stored credentials
        // For now, it's a placeholder for dynamic credential updates
    }

    /**
     * Test email connection
     *
     * @return true if connection successful, false otherwise
     */
    public static boolean testConnection() {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", EMAIL_HOST);
            props.put("mail.smtp.port", EMAIL_PORT);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust", EMAIL_HOST);

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
                }
            });

            Transport transport = session.getTransport("smtp");
            transport.connect();
            transport.close();
            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
