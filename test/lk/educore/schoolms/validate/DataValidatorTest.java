package lk.educore.schoolms.validate;

import lk.educore.schoolms.exceptions.ValidationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for DataValidator class. Tests cover all validation methods
 * for email, phone, password, NIC, letters, numbers, and amount validation.
 *
 * @author EduCore Software Team
 * @version 1.0.0
 * @see DataValidator
 */
public class DataValidatorTest {

    public DataValidatorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of isValidEmail method, of class DataValidator.
     */
    @Test
    public void testIsValidEmail() {
        System.out.println("isValidEmail");
        String email = "amirupekw@gmail.com";
        // Valid email should return true
        try {
            boolean result = DataValidator.isValidEmail(email);
            assertTrue(result);
        } catch (ValidationException e) {
            fail("Valid email should not throw exception: " + e.getMessage());
        }

        // Test invalid email
        String invalidEmail = "invalid-email";
        try {
            boolean result = DataValidator.isValidEmail(invalidEmail);
            fail("Invalid email should throw ValidationException");
        } catch (ValidationException e) {
            // Expected exception
            assertNotNull(e.getMessage());
        }
    }

    /**
     * Test of isValidPhone method, of class DataValidator.
     */
    @Test
    public void testIsValidPhone() {
        System.out.println("isValidPhone");
        String phone = "0715916293";
        // Valid phone should return true
        try {
            boolean result = DataValidator.isValidPhone(phone);
            assertTrue(result);
        } catch (ValidationException e) {
            fail("Valid phone should not throw exception: " + e.getMessage());
        }

        // Test invalid phone
        String invalidPhone = "12345";
        try {
            boolean result = DataValidator.isValidPhone(invalidPhone);
            fail("Invalid phone should throw ValidationException");
        } catch (ValidationException e) {
            // Expected exception
            assertNotNull(e.getMessage());
        }
    }

    /**
     * Test of isValidPassword method, of class DataValidator.
     */
    @Test
    public void testIsValidPassword() {
        System.out.println("isValidPassword");
        String password = "Psd@1234";
        // Valid password should return true
        try {
            boolean result = DataValidator.isValidPassword(password);
            assertTrue(result);
        } catch (ValidationException e) {
            fail("Valid password should not throw exception: " + e.getMessage());
        }

        // Test invalid password (too short)
        String invalidPassword = "12345";
        try {
            boolean result = DataValidator.isValidPassword(invalidPassword);
            fail("Invalid password should throw ValidationException");
        } catch (ValidationException e) {
            // Expected exception
            assertNotNull(e.getMessage());
        }
    }

    /**
     * Test of isValidNIC method, of class DataValidator.
     */
    @Test
    public void testIsValidNIC() {
        System.out.println("isValidNIC");
        String nic = "200403110370";
        // Valid NIC should return true
        try {
            boolean result = DataValidator.isValidNIC(nic);
            assertTrue(result);
        } catch (ValidationException e) {
            fail("Valid NIC should not throw exception: " + e.getMessage());
        }

        // Test invalid NIC
        String invalidNIC = "12345";
        try {
            boolean result = DataValidator.isValidNIC(invalidNIC);
            fail("Invalid NIC should throw ValidationException");
        } catch (ValidationException e) {
            // Expected exception
            assertNotNull(e.getMessage());
        }
    }

    /**
     * Test of isLettersOnly method, of class DataValidator.
     */
    @Test
    public void testIsLettersOnly() {
        System.out.println("isLettersOnly");
        String letters = "dsf";
        // Valid letters should return true
        try {
            boolean result = DataValidator.isLettersOnly(letters);
            assertTrue(result);
        } catch (ValidationException e) {
            fail("Valid letters should not throw exception: " + e.getMessage());
        }

        // Test invalid letters (contains numbers)
        String invalidLetters = "abc123";
        try {
            boolean result = DataValidator.isLettersOnly(invalidLetters);
            fail("Invalid letters should throw ValidationException");
        } catch (ValidationException e) {
            // Expected exception
            assertNotNull(e.getMessage());
        }
    }

    /**
     * Test of isNumbersOnly method, of class DataValidator.
     */
    @Test
    public void testIsNumbersOnly() {
        System.out.println("isNumbersOnly");
        String numbers = "12345";
        // Valid numbers should return true
        try {
            boolean result = DataValidator.isNumbersOnly(numbers);
            assertTrue(result);
        } catch (ValidationException e) {
            fail("Valid numbers should not throw exception: " + e.getMessage());
        }

        // Test invalid numbers (contains letters)
        String invalidNumbers = "abc123";
        try {
            boolean result = DataValidator.isNumbersOnly(invalidNumbers);
            fail("Invalid numbers should throw ValidationException");
        } catch (ValidationException e) {
            // Expected exception
            assertNotNull(e.getMessage());
        }
    }

    /**
     * Test of isPositiveNumber method, of class DataValidator.
     */
    @Test
    public void testIsPositiveNumber() {
        System.out.println("isPositiveNumber");
        String number = "500";
        // Valid positive number should return true
        try {
            boolean result = DataValidator.isPositiveNumber(number);
            assertTrue(result);
        } catch (ValidationException e) {
            fail("Valid positive number should not throw exception: " + e.getMessage());
        }

        // Test invalid number (zero)
        String invalidNumber = "0";
        try {
            boolean result = DataValidator.isPositiveNumber(invalidNumber);
            fail("Zero should throw ValidationException");
        } catch (ValidationException e) {
            // Expected exception
            assertNotNull(e.getMessage());
        }
    }

    /**
     * Test of isRequired method, of class DataValidator.
     */
    @Test
    public void testIsRequired() {
        System.out.println("isRequired");
        String text = "Some text";
        // Non-empty text should return true
        try {
            boolean result = DataValidator.isRequired(text);
            assertTrue(result);
        } catch (ValidationException e) {
            fail("Non-empty text should not throw exception: " + e.getMessage());
        }

        // Test empty text
        String emptyText = "";
        try {
            boolean result = DataValidator.isRequired(emptyText);
            fail("Empty text should throw ValidationException");
        } catch (ValidationException e) {
            // Expected exception
            assertNotNull(e.getMessage());
        }
    }

}
