package lk.educore.schoolms.exceptions;

import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Unit tests for custom exception classes:
 * BusinessException, ValidationException, DatabaseException, and EmailException.
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class ExceptionsTest {

    @Test
    public void testBusinessException_AllErrorTypes() {
        // DUPLICATE_RECORD
        BusinessException be1 = new BusinessException("Student already exists",
                BusinessException.ErrorType.DUPLICATE_RECORD, "Student", "STU-0001");
        assertTrue(be1.isDuplicateRecord());
        assertFalse(be1.isRecordNotFound());
        assertFalse(be1.isPermissionError());
        assertEquals("Student", be1.getEntity());
        assertEquals("STU-0001", be1.getEntityId());
        assertTrue(be1.getUserFriendlyMessage().contains("already exists"));

        // RECORD_NOT_FOUND
        BusinessException be2 = new BusinessException("Not found",
                BusinessException.ErrorType.RECORD_NOT_FOUND, "Book", "B-101");
        assertTrue(be2.isRecordNotFound());
        assertTrue(be2.getUserFriendlyMessage().contains("not found"));

        // INSUFFICIENT_PERMISSIONS
        BusinessException be3 = new BusinessException("Access denied",
                BusinessException.ErrorType.INSUFFICIENT_PERMISSIONS);
        assertTrue(be3.isPermissionError());
        assertTrue(be3.getUserFriendlyMessage().contains("permissions"));

        // INVALID_OPERATION
        BusinessException be4 = new BusinessException("Bad op", BusinessException.ErrorType.INVALID_OPERATION);
        assertTrue(be4.getUserFriendlyMessage().contains("Invalid operation"));

        // OPERATION_NOT_ALLOWED
        BusinessException be5 = new BusinessException("Disallowed", BusinessException.ErrorType.OPERATION_NOT_ALLOWED);
        assertTrue(be5.getUserFriendlyMessage().contains("not allowed"));

        // CONFLICT
        BusinessException be6 = new BusinessException("Time clash", BusinessException.ErrorType.CONFLICT);
        assertTrue(be6.getUserFriendlyMessage().contains("conflict"));

        // BUSINESS_RULE_VIOLATION
        BusinessException be7 = new BusinessException("Limit hit", BusinessException.ErrorType.BUSINESS_RULE_VIOLATION);
        assertTrue(be7.getUserFriendlyMessage().contains("Business rule violation"));

        // UNKNOWN
        BusinessException be8 = new BusinessException("Unknown", BusinessException.ErrorType.UNKNOWN);
        assertTrue(be8.getUserFriendlyMessage().contains("business error occurred"));
    }

    @Test
    public void testValidationException_FieldsAndPredicates() {
        ValidationException veEmail = new ValidationException("Invalid format", "email", "bad-email", "^pattern$");
        assertEquals("email", veEmail.getFieldName());
        assertEquals("bad-email", veEmail.getFieldValue());
        assertEquals("^pattern$", veEmail.getValidationRule());
        assertTrue(veEmail.isEmailError());
        assertFalse(veEmail.isPhoneError());
        assertFalse(veEmail.isNICError());
        assertTrue(veEmail.getUserFriendlyMessage().contains("Invalid email"));

        ValidationException vePhone = new ValidationException("Phone number error", "phone", "1234");
        assertTrue(vePhone.isPhoneError());

        ValidationException veNIC = new ValidationException("NIC error", "nic", "123");
        assertTrue(veNIC.isNICError());

        ValidationException veReq = new ValidationException("Field is required", "studentId", "");
        assertTrue(veReq.isRequiredFieldError());
    }

    @Test
    public void testDatabaseException_SqlStateAndErrors() {
        // Connection error (08xxx)
        SQLException sqlConn = new SQLException("Connection lost", "08S01", 1152);
        DatabaseException deConn = new DatabaseException("Database offline", sqlConn);
        assertEquals("08S01", deConn.getSqlState());
        assertEquals(1152, deConn.getErrorCode());
        assertTrue(deConn.isConnectionError());
        assertFalse(deConn.isConstraintViolation());
        assertTrue(deConn.getUserFriendlyMessage().contains("Unable to connect"));

        // Duplicate key (1062)
        SQLException sqlDup = new SQLException("Duplicate entry", "23000", 1062);
        DatabaseException deDup = new DatabaseException("Key collision", sqlDup);
        assertTrue(deDup.isDuplicateKey());
        assertTrue(deDup.isConstraintViolation());
        assertTrue(deDup.getUserFriendlyMessage().contains("already exists"));

        // General constraint violation (23xxx)
        DatabaseException deConst = new DatabaseException("FK fail", "23503", 1452, "Cannot add child");
        assertTrue(deConst.isConstraintViolation());
        assertFalse(deConst.isConnectionError());
        assertTrue(deConst.getUserFriendlyMessage().contains("violates a database constraint"));

        // General error
        DatabaseException deGen = new DatabaseException("Syntax error", "42000", 1064, "Check syntax");
        assertTrue(deGen.getUserFriendlyMessage().contains("A database error occurred"));
    }

    @Test
    public void testEmailException_AllErrorTypes() {
        EmailException ee1 = new EmailException("Auth failed", EmailException.ErrorType.AUTHENTICATION_FAILED);
        assertEquals(EmailException.ErrorType.AUTHENTICATION_FAILED, ee1.getErrorType());
        assertTrue(ee1.getUserFriendlyMessage().contains("authentication failed"));

        EmailException ee2 = new EmailException("Bad recipient", EmailException.ErrorType.INVALID_RECIPIENT, "bad@recipient");
        assertEquals("bad@recipient", ee2.getRecipient());
        assertTrue(ee2.getUserFriendlyMessage().contains("Invalid email recipient"));

        EmailException ee3 = new EmailException("Timeout", EmailException.ErrorType.TIMEOUT);
        assertTrue(ee3.getUserFriendlyMessage().contains("timed out"));

        EmailException ee4 = new EmailException("Conn failed", EmailException.ErrorType.CONNECTION_FAILED);
        assertTrue(ee4.getUserFriendlyMessage().contains("Failed to connect"));

        EmailException ee5 = new EmailException("Attach fail", EmailException.ErrorType.ATTACHMENT_ERROR);
        assertTrue(ee5.getUserFriendlyMessage().contains("attach file"));

        EmailException ee6 = new EmailException("Send fail", EmailException.ErrorType.SENDING_FAILED);
        assertTrue(ee6.getUserFriendlyMessage().contains("Failed to send"));
    }
}
