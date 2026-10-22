package lk.educore.schoolms.email;

import lk.educore.schoolms.exceptions.EmailException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for email services: SendEmail, SendFileEmail, and SendQREmail.
 * Tests parameter validation, attachment presence verification, and exception models.
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class EmailServiceTest {

    @Test
    public void testSendEmail_ParameterValidation() {
        try {
            SendEmail.sendEmail(null, "Test Subject", "Test Content");
            fail("Expected EmailException for null email");
        } catch (EmailException e) {
            assertEquals(EmailException.ErrorType.INVALID_RECIPIENT, e.getErrorType());
        }

        try {
            SendEmail.sendEmail("", "Test Subject", "Test Content");
            fail("Expected EmailException for empty email");
        } catch (EmailException e) {
            assertEquals(EmailException.ErrorType.INVALID_RECIPIENT, e.getErrorType());
        }
    }

    @Test
    public void testSendFileEmail_ParameterValidation() {
        // Null recipient
        try {
            SendFileEmail.sendEmailWithAttachment(null, "Subject", "Content", "path/to/file.pdf");
            fail("Expected EmailException for null recipient");
        } catch (EmailException e) {
            assertEquals(EmailException.ErrorType.INVALID_RECIPIENT, e.getErrorType());
        }

        // Empty recipient
        try {
            SendFileEmail.sendEmailWithAttachment("   ", "Subject", "Content", "path/to/file.pdf");
            fail("Expected EmailException for blank recipient");
        } catch (EmailException e) {
            assertEquals(EmailException.ErrorType.INVALID_RECIPIENT, e.getErrorType());
        }

        // Missing attachment file
        try {
            SendFileEmail.sendEmailWithAttachment("test@example.com", "Subject", "Content", "non_existent_file_99999.xyz");
            fail("Expected EmailException for non-existent attachment");
        } catch (EmailException e) {
            assertEquals(EmailException.ErrorType.ATTACHMENT_ERROR, e.getErrorType());
            assertTrue(e.getUserFriendlyMessage().contains("attach file"));
        }
    }

    @Test
    public void testSendQREmail_ParameterValidation() {
        // Null recipient
        try {
            SendQREmail.sendStudentIDCard(null, "STU-0001", "Test Student", "Grade 10");
            fail("Expected EmailException for null recipient");
        } catch (EmailException e) {
            assertEquals(EmailException.ErrorType.INVALID_RECIPIENT, e.getErrorType());
        }

        // Empty recipient
        try {
            SendQREmail.sendStudentIDCard("", "STU-0001", "Test Student", "Grade 10");
            fail("Expected EmailException for empty recipient");
        } catch (EmailException e) {
            assertEquals(EmailException.ErrorType.INVALID_RECIPIENT, e.getErrorType());
        }

        // Teacher ID card - null recipient
        try {
            SendQREmail.sendTeacherIDCard(null, "198012345678", "Test Teacher", "Permanent");
            fail("Expected EmailException for null teacher recipient");
        } catch (EmailException e) {
            assertEquals(EmailException.ErrorType.INVALID_RECIPIENT, e.getErrorType());
        }
    }
}
