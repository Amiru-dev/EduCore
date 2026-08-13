package lk.corevision.fruitshop.email;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lk.corevision.fruitshop.exceptions.EmailException;

/**
 * Email service for sending simple text emails. Provides functionality to send
 * emails using SMTP with authentication.
 *
 * @author CoreVision Software Team
 * @version 1.0.0
 */
public class SendEmail {

    private static String from;
    private static String password;

    /**
     * Sends a simple text email to the specified recipient.
     *
     * @param to the recipient email address
     * @param subject the email subject
     * @param messageText the email content
     * @throws EmailException if email sending fails due to authentication,
     * network issues, or invalid recipient
     */
    public static void sendEmail(String to, String subject, String messageText) throws EmailException {
        // Validate input parameters
        if (to == null || to.trim().isEmpty()) {
            throw new EmailException("Recipient email address cannot be null or empty",
                    to, EmailException.EmailErrorType.INVALID_RECIPIENT);
        }

        if (subject == null) {
            subject = "";
        }

        if (messageText == null) {
            messageText = "";
        }

        from = "asuspro5001@gmail.com";
        password = "xdhmaqxxfflvhxpg";

        try {
            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.connectiontimeout", "10000");
            properties.put("mail.smtp.timeout", "10000");
            properties.put("mail.smtp.writetimeout", "10000");

            // Create a session with authentication
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            });

            // Create the message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(messageText);

            // Send the email
            Transport.send(message);
        } catch (javax.mail.AuthenticationFailedException e) {
            throw new EmailException("Email authentication failed. Check sender credentials.",
                    e, EmailException.EmailErrorType.AUTHENTICATION_FAILED);
        } catch (javax.mail.SendFailedException e) {
            throw new EmailException("Failed to send email to recipient: " + to,
                    to, EmailException.EmailErrorType.INVALID_RECIPIENT);
        } catch (javax.mail.MessagingException e) {
            if (e.getCause() instanceof java.net.ConnectException
                    || e.getCause() instanceof java.net.SocketTimeoutException) {
                throw new EmailException("Network connection failed while sending email",
                        e, EmailException.EmailErrorType.CONNECTION_FAILED);
            }
            throw new EmailException("Failed to send email: " + e.getMessage(),
                    e, EmailException.EmailErrorType.UNKNOWN);
        } catch (Exception e) {
            throw new EmailException("Unexpected error while sending email: " + e.getMessage(),
                    e, EmailException.EmailErrorType.UNKNOWN);
        }
    }
}
