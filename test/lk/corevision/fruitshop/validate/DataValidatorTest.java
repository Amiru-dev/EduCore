package lk.corevision.fruitshop.validate;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
     * Test of validateEmail method, of class DataValidator.
     */
    @Test
    public void testValidateEmail() {
        System.out.println("validateEmail");
        String email = "amirupekw@gmail.com";
        // DataValidator.validateEmail returns true if INVALID, false if valid
        boolean expResult = false; // Valid email should return false
        boolean result = DataValidator.isInvalidEmail(email);
        assertEquals(expResult, result);

        // Test invalid email
        String invalidEmail = "invalid-email";
        boolean expInvalidResult = true; // Invalid email should return true
        boolean invalidResult = DataValidator.isInvalidEmail(invalidEmail);
        assertEquals(expInvalidResult, invalidResult);
    }

    /**
     * Test of validateMobile method, of class DataValidator.
     */
    @Test
    public void testValidateMobile() {
        System.out.println("validateMobile");
        String mobile = "0715916293";
        // DataValidator.validateMobile returns true if INVALID, false if valid
        boolean expResult = false; // Valid mobile should return false
        boolean result = DataValidator.isInvalidMobile(mobile);
        assertEquals(expResult, result);

        // Test invalid mobile
        String invalidMobile = "12345";
        boolean expInvalidResult = true; // Invalid mobile should return true
        boolean invalidResult = DataValidator.isInvalidMobile(invalidMobile);
        assertEquals(expInvalidResult, invalidResult);
    }

    /**
     * Test of validateLandline method, of class DataValidator.
     */
    @Test
    public void testValidateLandline() {
        System.out.println("validateLandline");
        String landline = "0312272932";
        // DataValidator.validateLandline returns true if INVALID, false if valid
        boolean expResult = false; // Valid landline should return false
        boolean result = DataValidator.isInvalidLandline(landline);
        assertEquals(expResult, result);

        // Test invalid landline
        String invalidLandline = "12345";
        boolean expInvalidResult = true; // Invalid landline should return true
        boolean invalidResult = DataValidator.isInvalidLandline(invalidLandline);
        assertEquals(expInvalidResult, invalidResult);
    }

    /**
     * Test of validatePassword method, of class DataValidator.
     */
    @Test
    public void testValidatePassword() {
        System.out.println("validatePassword");
        String password = "Psd@1234";
        // DataValidator.validatePassword returns true if INVALID, false if valid
        boolean expResult = false; // Valid password should return false
        boolean result = DataValidator.isInvalidPassword(password);
        assertEquals(expResult, result);

        // Test invalid password (too short, no special char, etc.)
        String invalidPassword = "123456";
        boolean expInvalidResult = true; // Invalid password should return true
        boolean invalidResult = DataValidator.isInvalidPassword(invalidPassword);
        assertEquals(expInvalidResult, invalidResult);
    }

    /**
     * Test of validateNIC method, of class DataValidator.
     */
    @Test
    public void testValidateNIC() {
        System.out.println("validateNIC");
        String nic = "200403110370";
        // DataValidator.validateNIC returns true if INVALID, false if valid
        boolean expResult = false; // Valid NIC should return false
        boolean result = DataValidator.isInvalidNIC(nic);
        assertEquals(expResult, result);

        // Test invalid NIC
        String invalidNIC = "12345";
        boolean expInvalidResult = true; // Invalid NIC should return true
        boolean invalidResult = DataValidator.isInvalidNIC(invalidNIC);
        assertEquals(expInvalidResult, invalidResult);
    }

    /**
     * Test of validateLetter method, of class DataValidator.
     */
    @Test
    public void testValidateLetter() {
        System.out.println("validateLetter");
        String letter = "dsf";
        // DataValidator.validateLetter returns true if INVALID, false if valid
        boolean expResult = false; // Valid letters should return false
        boolean result = DataValidator.isInvalidLetter(letter);
        assertEquals(expResult, result);

        // Test invalid letters (contains numbers)
        String invalidLetter = "abc123";
        boolean expInvalidResult = true; // Invalid letters should return true
        boolean invalidResult = DataValidator.isInvalidLetter(invalidLetter);
        assertEquals(expInvalidResult, invalidResult);
    }

    /**
     * Test of validatePrice method, of class DataValidator.
     */
    @Test
    public void testValidatePrice() {
        System.out.println("validatePrice");
        String price = "500";
        // DataValidator.validatePrice returns true if INVALID, false if valid
        boolean expResult = false; // Valid price should return false
        boolean result = DataValidator.isInvalidPrice(price);
        assertEquals(expResult, result);

        // Test invalid price (zero or negative)
        String invalidPrice = "0";
        boolean expInvalidResult = true; // Invalid price should return true
        boolean invalidResult = DataValidator.isInvalidPrice(invalidPrice);
        assertEquals(expInvalidResult, invalidResult);
    }

}
