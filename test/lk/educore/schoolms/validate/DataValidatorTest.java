package lk.educore.schoolms.validate;

import lk.educore.schoolms.exceptions.ValidationException;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Exhaustive unit tests for DataValidator class.
 * Covers all 15 validation methods, regex rules, boundaries, and exception handling.
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class DataValidatorTest {

    @Test
    public void testIsValidEmail_Valid() throws ValidationException {
        assertTrue(DataValidator.isValidEmail("amirupekw@gmail.com"));
        assertTrue(DataValidator.isValidEmail("user.name+tag@example.co.uk"));
        assertTrue(DataValidator.isValidEmail("admin@educore.org"));
    }

    @Test
    public void testIsValidEmail_Invalid() {
        String[] invalidEmails = {
                null, "", "   ", "invalid-email", "user@", "@domain.com",
                "user@domain", "user@.com", "user space@domain.com"
        };
        for (String email : invalidEmails) {
            try {
                DataValidator.isValidEmail(email);
                fail("Expected ValidationException for email: " + email);
            } catch (ValidationException e) {
                assertTrue(e.isEmailError());
            }
        }
    }

    @Test
    public void testIsValidNIC_Valid() throws ValidationException {
        // Old format: 9 digits + V/v/X/x
        assertTrue(DataValidator.isValidNIC("200403110V"));
        assertTrue(DataValidator.isValidNIC("200403110v"));
        assertTrue(DataValidator.isValidNIC("200403110X"));
        assertTrue(DataValidator.isValidNIC("200403110x"));

        // New format: 12 digits
        assertTrue(DataValidator.isValidNIC("200403110370"));
        assertTrue(DataValidator.isValidNIC("198512345678"));
    }

    @Test
    public void testIsValidNIC_Invalid() {
        String[] invalidNICs = {
                null, "", "   ", "12345", "123456789", "12345678901",
                "1234567890123", "200403110A", "20040311037V"
        };
        for (String nic : invalidNICs) {
            try {
                DataValidator.isValidNIC(nic);
                fail("Expected ValidationException for NIC: " + nic);
            } catch (ValidationException e) {
                assertTrue(e.isNICError());
            }
        }
    }

    @Test
    public void testIsValidPhone_Valid() throws ValidationException {
        // 10 digits starting with 0
        assertTrue(DataValidator.isValidPhone("0715916293"));
        assertTrue(DataValidator.isValidPhone("0112345678"));
        assertTrue(DataValidator.isValidPhone("0770000000"));
    }

    @Test
    public void testIsValidPhone_Invalid() {
        String[] invalidPhones = {
                null, "", "   ", "12345", "071591629", "07159162933",
                "7159162930", "071591629a", "+94715916293"
        };
        for (String phone : invalidPhones) {
            try {
                DataValidator.isValidPhone(phone);
                fail("Expected ValidationException for phone: " + phone);
            } catch (ValidationException e) {
                assertTrue(e.isPhoneError());
            }
        }
    }

    @Test
    public void testIsValidPassword_Valid() throws ValidationException {
        assertTrue(DataValidator.isValidPassword("Psd@1234"));
        assertTrue(DataValidator.isValidPassword("123456")); // exactly 6 chars
        assertTrue(DataValidator.isValidPassword("VeryLongSecurePassword123!"));
    }

    @Test
    public void testIsValidPassword_Invalid() {
        String[] invalidPasswords = {null, "", "   ", "12345", "a"};
        for (String pwd : invalidPasswords) {
            try {
                DataValidator.isValidPassword(pwd);
                fail("Expected ValidationException for password: " + pwd);
            } catch (ValidationException e) {
                assertNotNull(e.getMessage());
            }
        }
    }

    @Test
    public void testIsValidStudentID_Valid() throws ValidationException {
        assertTrue(DataValidator.isValidStudentID("STU-0001"));
        assertTrue(DataValidator.isValidStudentID("STU-1234"));
        assertTrue(DataValidator.isValidStudentID("STU-9999"));
    }

    @Test
    public void testIsValidStudentID_Invalid() {
        String[] invalidStudentIDs = {
                null, "", "   ", "STU-01", "STU-12345", "STD-1234",
                "stu-1234", "1234", "STU-ABCD"
        };
        for (String id : invalidStudentIDs) {
            try {
                DataValidator.isValidStudentID(id);
                fail("Expected ValidationException for student ID: " + id);
            } catch (ValidationException e) {
                assertNotNull(e.getMessage());
            }
        }
    }

    @Test
    public void testIsLettersOnly_Valid() throws ValidationException {
        assertTrue(DataValidator.isLettersOnly("Amiru"));
        assertTrue(DataValidator.isLettersOnly("Amiru Upek"));
        assertTrue(DataValidator.isLettersOnly("John Doe"));
    }

    @Test
    public void testIsLettersOnly_Invalid() {
        String[] invalidNames = {null, "", "   ", "Amiru123", "User!", "12345"};
        for (String name : invalidNames) {
            try {
                DataValidator.isLettersOnly(name);
                fail("Expected ValidationException for letters only: " + name);
            } catch (ValidationException e) {
                assertNotNull(e.getMessage());
            }
        }
    }

    @Test
    public void testIsNumbersOnly_Valid() throws ValidationException {
        assertTrue(DataValidator.isNumbersOnly("0"));
        assertTrue(DataValidator.isNumbersOnly("12345"));
        assertTrue(DataValidator.isNumbersOnly("999999999"));
    }

    @Test
    public void testIsNumbersOnly_Invalid() {
        String[] invalidNumbers = {null, "", "   ", "123a", "-123", "12.34", "abc"};
        for (String num : invalidNumbers) {
            try {
                DataValidator.isNumbersOnly(num);
                fail("Expected ValidationException for numbers only: " + num);
            } catch (ValidationException e) {
                assertNotNull(e.getMessage());
            }
        }
    }

    @Test
    public void testIsDecimal_Valid() throws ValidationException {
        assertTrue(DataValidator.isDecimal("100"));
        assertTrue(DataValidator.isDecimal("100.5"));
        assertTrue(DataValidator.isDecimal("100.50"));
        assertTrue(DataValidator.isDecimal("0.75"));
    }

    @Test
    public void testIsDecimal_Invalid() {
        String[] invalidDecimals = {null, "", "   ", "100.5.5", "abc", "-100"};
        for (String dec : invalidDecimals) {
            try {
                DataValidator.isDecimal(dec);
                fail("Expected ValidationException for decimal: " + dec);
            } catch (ValidationException e) {
                assertNotNull(e.getMessage());
            }
        }
    }

    @Test
    public void testIsRequired() throws ValidationException {
        assertTrue(DataValidator.isRequired("Some text"));
        assertTrue(DataValidator.isRequired("Some text", "Field Label"));

        try {
            DataValidator.isRequired(null);
            fail("Expected ValidationException for null");
        } catch (ValidationException e) {
            assertTrue(e.isRequiredFieldError());
        }

        try {
            DataValidator.isRequired("   ", "CustomField");
            fail("Expected ValidationException for blank string");
        } catch (ValidationException e) {
            assertTrue(e.getUserFriendlyMessage().contains("CustomField"));
        }
    }

    @Test
    public void testIsValidLength_Valid() throws ValidationException {
        assertTrue(DataValidator.isValidLength("123", 2, 5));
        assertTrue(DataValidator.isValidLength("12", 2, 5));
        assertTrue(DataValidator.isValidLength("12345", 2, 5));
    }

    @Test
    public void testIsValidLength_Invalid() {
        // Below min length
        try {
            DataValidator.isValidLength("1", 2, 5);
            fail("Expected ValidationException for string below min length");
        } catch (ValidationException e) {
            assertNotNull(e.getMessage());
        }

        // Above max length
        try {
            DataValidator.isValidLength("123456", 2, 5);
            fail("Expected ValidationException for string above max length");
        } catch (ValidationException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testIsPositiveNumber_Valid() throws ValidationException {
        assertTrue(DataValidator.isPositiveNumber("500"));
        assertTrue(DataValidator.isPositiveNumber("0.01"));
        assertTrue(DataValidator.isPositiveNumber("1500.50", "Amount"));
    }

    @Test
    public void testIsPositiveNumber_Invalid() {
        String[] invalid = {null, "", "0", "-5", "-0.01", "abc"};
        for (String num : invalid) {
            try {
                DataValidator.isPositiveNumber(num);
                fail("Expected ValidationException for positive number: " + num);
            } catch (ValidationException e) {
                assertNotNull(e.getMessage());
            }
        }
    }

    @Test
    public void testIsValidDOB_Valid() throws ValidationException {
        assertTrue(DataValidator.isValidDOB("2004-01-15"));
        assertTrue(DataValidator.isValidDOB("2015-12-31"));
    }

    @Test
    public void testIsValidDOB_Invalid() {
        // Future date
        try {
            DataValidator.isValidDOB("2099-01-01");
            fail("Expected ValidationException for future DOB");
        } catch (ValidationException e) {
            assertTrue(e.getMessage().contains("future"));
        }

        // > 120 years ago
        try {
            DataValidator.isValidDOB("1800-01-01");
            fail("Expected ValidationException for DOB > 120 years ago");
        } catch (ValidationException e) {
            assertNotNull(e.getMessage());
        }

        // Malformed format
        String[] malformed = {null, "", "15-01-2004", "2004/01/15", "invalid-date"};
        for (String dob : malformed) {
            try {
                DataValidator.isValidDOB(dob);
                fail("Expected ValidationException for malformed DOB: " + dob);
            } catch (ValidationException e) {
                assertNotNull(e.getMessage());
            }
        }
    }

    @Test
    public void testIsValidAmount_Valid() throws ValidationException {
        assertTrue(DataValidator.isValidAmount("100"));
        assertTrue(DataValidator.isValidAmount("100.5"));
        assertTrue(DataValidator.isValidAmount("100.55"));
    }

    @Test
    public void testIsValidAmount_Invalid() {
        // More than 2 decimal places
        try {
            DataValidator.isValidAmount("100.555");
            fail("Expected ValidationException for amount with 3 decimal places");
        } catch (ValidationException e) {
            assertTrue(e.getMessage().contains("maximum 2 decimal places"));
        }

        // Zero or negative
        try {
            DataValidator.isValidAmount("0");
            fail("Expected ValidationException for 0 amount");
        } catch (ValidationException e) {
            assertNotNull(e.getMessage());
        }

        try {
            DataValidator.isValidAmount("-50.00");
            fail("Expected ValidationException for negative amount");
        } catch (ValidationException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testIsValidDateRange() throws ValidationException {
        Calendar cal = Calendar.getInstance();
        Date start = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, 5);
        Date end = cal.getTime();

        assertTrue(DataValidator.isValidDateRange(start, end));
        assertTrue(DataValidator.isValidDateRange(start, start)); // Same date is valid
        assertTrue(DataValidator.isValidDateRange(null, end));

        // End before start throws exception
        try {
            DataValidator.isValidDateRange(end, start);
            fail("Expected ValidationException when end date is before start date");
        } catch (ValidationException e) {
            assertTrue(e.getMessage().contains("End date cannot come before start date"));
        }
    }

    @Test
    public void testValidateRegex() {
        assertTrue(DataValidator.validate("12345", "^[0-9]+$"));
        assertFalse(DataValidator.validate("abc123", "^[0-9]+$"));
        assertFalse(DataValidator.validate(null, "^[0-9]+$"));
        assertFalse(DataValidator.validate("", "^[0-9]+$"));
    }
}
