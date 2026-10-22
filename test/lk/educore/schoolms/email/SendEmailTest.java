package lk.educore.schoolms.email;

import lk.educore.schoolms.exceptions.EmailException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for SendEmail service parameter validation and error handling.
 * Avoids live network socket connections during unit testing.
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class SendEmailTest {

    @Test
    public void testSendEmail_NullRecipientThrowsException() {
        try {
            SendEmail.sendEmail(null, "Subject", "Message");
            fail("Expected EmailException for null recipient");
        } catch (EmailException e) {
            assertEquals(EmailException.ErrorType.INVALID_RECIPIENT, e.getErrorType());
            assertTrue(e.getUserFriendlyMessage().contains("Invalid email recipient"));
        }
    }

    @Test
    public void testSendEmail_EmptyRecipientThrowsException() {
        try {
            SendEmail.sendEmail("   ", "Subject", "Message");
            fail("Expected EmailException for blank recipient");
        } catch (EmailException e) {
            assertEquals(EmailException.ErrorType.INVALID_RECIPIENT, e.getErrorType());
        }
    }

    @Test
    public void testSendHTMLEmail_NullRecipientThrowsException() {
        try {
            SendEmail.sendHTMLEmail(null, "Subject", "<h1>Content</h1>");
            fail("Expected EmailException for null recipient");
        } catch (EmailException e) {
            assertEquals(EmailException.ErrorType.INVALID_RECIPIENT, e.getErrorType());
        }
    }

    @Test
    public void testSendHTMLEmail_EmptyRecipientThrowsException() {
        try {
            SendEmail.sendHTMLEmail("", "Subject", "<h1>Content</h1>");
            fail("Expected EmailException for empty recipient");
        } catch (EmailException e) {
            assertEquals(EmailException.ErrorType.INVALID_RECIPIENT, e.getErrorType());
        }
    }
}
