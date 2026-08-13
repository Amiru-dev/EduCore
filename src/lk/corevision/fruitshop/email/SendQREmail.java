package lk.corevision.fruitshop.email;

import java.io.File;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import lk.corevision.fruitshop.exceptions.EmailException;

/**
 * Email service for sending emails with QR code attachments. Provides
 * functionality to send emails with QR code image attachments using SMTP with
 * authentication.
 *
 * @author CoreVision Software Team
 * @version 1.0.0
 */
public class SendQREmail {

    /**
     * Sends an email with a QR code attachment. 
     *
     * @param to the recipient email address
     * @param subject the email subject
     * @param messageText the email content
     * @param nic the NIC number used to locate the QR code file
     * @throws EmailException if email sending fails due to authentication,
     * network issues, invalid recipient, or missing QR file
     */
    public static void sendEmail(String to, String subject, String messageText, String nic) throws EmailException {
        // Validate input parameters
        if (to == null || to.trim().isEmpty()) {
            throw new EmailException("Recipient email address cannot be null or empty",
                    to, EmailException.EmailErrorType.INVALID_RECIPIENT);
        }

        if (nic == null || nic.trim().isEmpty()) {
            throw new EmailException("NIC cannot be null or empty for QR code file lookup",
                    EmailException.EmailErrorType.ATTACHMENT_FAILED);
        }

        String from = "asuspro5001@gmail.com";
        String password = "xdhmaqxxfflvhxpg";

        String host = "smtp.gmail.com";

        try {
            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.connectiontimeout", "10000");
            properties.put("mail.smtp.timeout", "10000");
            properties.put("mail.smtp.writetimeout", "10000");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            });

            // Construct the file path for the attachment
            String homePath = System.getProperty("user.home");
            String folderPath = homePath
                    + File.separator + "Documents"
                    + File.separator + "FruitShopApp"
                    + File.separator + "QR";

            String fileName = nic;
            String filePath = folderPath + File.separator + fileName + ".png";

            File attachmentFile = new File(filePath);

            if (!attachmentFile.exists()) {
                throw new EmailException("QR code file not found at: " + filePath,
                        EmailException.EmailErrorType.ATTACHMENT_FAILED);
            }

            // Create email message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);

            // Add email content
            Multipart multipart = new MimeMultipart();

            // Text body part
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText(messageText);
            multipart.addBodyPart(textBodyPart);

            // Attachment part
            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource dataSource = new FileDataSource(filePath);
            attachmentPart.setDataHandler(new DataHandler(dataSource));
            attachmentPart.setFileName(attachmentFile.getName());
            multipart.addBodyPart(attachmentPart);

            // Combine and send
            message.setContent(multipart);
            Transport.send(message);
        } catch (EmailException e) {
            throw e; // Re-throw EmailException as-is
        } catch (javax.mail.AuthenticationFailedException e) {
            throw new EmailException("Email authentication failed. Check sender credentials.",
                    e, EmailException.EmailErrorType.AUTHENTICATION_FAILED);
        } catch (javax.mail.SendFailedException e) {
            throw new EmailException("Failed to send email to recipient: " + to,
                    to, EmailException.EmailErrorType.INVALID_RECIPIENT);
        } catch (Exception e) {
            if (e.getCause() instanceof java.net.ConnectException
                    || e.getCause() instanceof java.net.SocketTimeoutException) {
                throw new EmailException("Network connection failed while sending email",
                        e, EmailException.EmailErrorType.CONNECTION_FAILED);
            }
            throw new EmailException("Failed to send email with QR code attachment: " + e.getMessage(),
                    e, EmailException.EmailErrorType.ATTACHMENT_FAILED);
        }
    }
}
