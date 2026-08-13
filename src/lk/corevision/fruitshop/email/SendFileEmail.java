package lk.corevision.fruitshop.email;

import java.io.ByteArrayOutputStream;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import lk.corevision.fruitshop.exceptions.EmailException;

/**
 * Email service for sending emails with PDF attachments generated from
 * JasperReports. Provides functionality to send emails with report attachments
 * using SMTP with authentication.
 *
 * @author CoreVision Software Team
 * @version 1.0.0
 */
public class SendFileEmail {

    private static String from;
    private static String password;

    /**
     * Sends an email with a PDF attachment generated from JasperPrint.
     *
     * @param to the recipient email address
     * @param subject the email subject
     * @param messageText the email content (HTML format supported)
     * @param jasperPrint the JasperPrint object to generate PDF from
     * @param FileName the name for the PDF attachment (without extension)
     * @return true if email was sent successfully
     * @throws EmailException if email sending fails due to authentication,
     * network issues, or invalid recipient
     */
    public static boolean sendEmail(String to, String subject, String messageText, JasperPrint jasperPrint, String FileName) throws EmailException {
        // Validate input parameters
        if (to == null || to.trim().isEmpty()) {
            throw new EmailException("Recipient email address cannot be null or empty",
                    to, EmailException.EmailErrorType.INVALID_RECIPIENT);
        }

        if (jasperPrint == null) {
            throw new EmailException("JasperPrint object cannot be null",
                    EmailException.EmailErrorType.ATTACHMENT_FAILED);
        }

        if (FileName == null) {
            FileName = "Fruit_Shop_System_Generated_Report";
        }

        from = "asuspro5001@gmail.com";
        password = "xdhmaqxxfflvhxpg";

        try {
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
            properties.put("mail.smtp.connectiontimeout", "60000");
            properties.put("mail.smtp.timeout", "60000");
            properties.put("mail.smtp.writetimeout", "60000");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            });

            // Generate PDF to ByteArrayOutputStream
            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, pdfOutputStream);

            // Create email message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);

            // Add email content
            Multipart multipart = new MimeMultipart();

            // Text body part
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setContent(messageText, "text/html; charset=utf-8");
            multipart.addBodyPart(textBodyPart);

            // Attachment part
            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource dataSource = new ByteArrayDataSource(pdfOutputStream.toByteArray(), "application/pdf");
            attachmentPart.setDataHandler(new DataHandler(dataSource));
            attachmentPart.setFileName(FileName + ".pdf");
            multipart.addBodyPart(attachmentPart);

            // Combine and send
            message.setContent(multipart);
            Transport.send(message);

            return true;
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
            throw new EmailException("Failed to send email with attachment: " + e.getMessage(),
                    e, EmailException.EmailErrorType.ATTACHMENT_FAILED);
        }
    }
}
