package lk.corevision.fruitshop.validate;

import java.util.regex.Pattern;
import lk.corevision.fruitshop.exceptions.ValidationException;

/**
 * Data validation utility class for validating various input formats. Provides
 * static methods for validating email addresses, phone numbers, passwords, NIC
 * numbers, and other data formats.
 *
 * @author CoreVision Software Team
 * @version 1.0.0
 */
public class DataValidator {

    private static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$";
    private static final String MOBILE_REGEX = "^(?:0|94|\\+94|0094)?(?:(11|21|23|24|25|26|27|31|32|33|34|35|36|37|38|41|45|47|51|52|54|55|57|63|65|66|67|81|91)(0|2|3|4|5|7|9)|7(0|1|2|4|5|6|7|8)\\d)\\d{6}$";
    private static final String LANDLINE_REGEX = "^(?:0|94|\\+94|0094)?(?:(11|21|23|24|25|26|27|31|32|33|34|35|36|37|38|41|45|47|51|52|54|55|57|63|65|66|67|81|91)(0|2|3|4|5|7|9)|7(0|1|2|4|5|6|7|8)\\d)\\d{6}$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,12}$";
    private static final String LETTER_REGEX = "^[a-zA-Z]+$";
    private static final String PRICE_REGEX = "^(?!0(\\.0+)?$)\\d+(\\.\\d+)?$";
    private static final String NIC_REGEX = "^(([5,6,7,8,9]{1})([0-9]{1})([0,1,2,3,5,6,7,8]{1})([0-9]{6})([v|V|x|X]))|(([1,2]{1})([0,9]{1})([0-9]{2})([0,1,2,3,5,6,7,8]{1})([0-9]{7}))";
    private static final String NUMBER_REGEX = "^\\d+";

    /**
     * Validates an email address format.
     *
     * @param email the email address to validate
     * @return true if the email is invalid, false if valid
     */
    public static boolean isInvalidEmail(String email) {
        return !Pattern.matches(EMAIL_REGEX, email);
    }

    /**
     * Validates a mobile phone number format (Sri Lankan format).
     *
     * @param mobile the mobile number to validate
     * @return true if the mobile number is invalid, false if valid
     */
    public static boolean isInvalidMobile(String mobile) {
        return !Pattern.matches(MOBILE_REGEX, mobile);
    }

    /**
     * Validates a landline phone number format (Sri Lankan format).
     *
     * @param landline the landline number to validate
     * @return true if the landline number is invalid, false if valid
     */
    public static boolean isInvalidLandline(String landline) {
        return !Pattern.matches(LANDLINE_REGEX, landline);
    }

    /**
     * Validates a password format. Password must contain: at least one
     * lowercase letter, one uppercase letter, one digit, one special character,
     * and be 8-12 characters long.
     *
     * @param password the password to validate
     * @return true if the password is invalid, false if valid
     */
    public static boolean isInvalidPassword(String password) {
        return !Pattern.matches(PASSWORD_REGEX, password);
    }

    /**
     * Validates a Sri Lankan National Identity Card (NIC) number format.
     * Supports both old (10 digits with letter) and new (12 digits) formats.
     *
     * @param nic the NIC number to validate
     * @return true if the NIC is invalid, false if valid
     */
    public static boolean isInvalidNIC(String nic) {
        return !Pattern.matches(NIC_REGEX, nic);

    }

    /**
     * Validates that a string contains only letters (A-Z, a-z).
     *
     * @param letter the string to validate
     * @return true if the string contains non-letter characters, false if valid
     */
    public static boolean isInvalidLetter(String letter) {
        return !Pattern.matches(LETTER_REGEX, letter);
    }

    /**
     * Validates a price format (positive number, cannot be zero).
     *
     * @param price the price to validate
     * @return true if the price is invalid, false if valid
     */
    public static boolean isInvalidPrice(String price) {
        return !Pattern.matches(PRICE_REGEX, price);

    }

    /**
     * Validates that a string contains only digits.
     *
     * @param number the string to validate
     * @return true if the string contains non-digit characters, false if valid
     */
    public static boolean isInvalidNumber(String number) {
        return !Pattern.matches(NUMBER_REGEX, number);

    }

    /**
     * Validates an email address and throws ValidationException if invalid.
     *
     * @param email the email address to validate
     * @throws ValidationException if the email format is invalid
     */
    public static void validateEmailOrThrow(String email) throws ValidationException {
        if (isInvalidEmail(email)) {
            throw new ValidationException("Invalid email format: " + email, "email", email,
                    ValidationException.ValidationErrorType.INVALID_EMAIL);
        }
    }

    /**
     * Validates a mobile number and throws ValidationException if invalid.
     *
     * @param mobile the mobile number to validate
     * @throws ValidationException if the mobile number format is invalid
     */
    public static void validateMobileOrThrow(String mobile) throws ValidationException {
        if (isInvalidMobile(mobile)) {
            throw new ValidationException("Invalid mobile number format: " + mobile, "mobile", mobile,
                    ValidationException.ValidationErrorType.INVALID_MOBILE);
        }
    }

    /**
     * Validates a landline number and throws ValidationException if invalid.
     *
     * @param landline the landline number to validate
     * @throws ValidationException if the landline number format is invalid
     */
    public static void validateLandlineOrThrow(String landline) throws ValidationException {
        if (isInvalidLandline(landline)) {
            throw new ValidationException("Invalid landline number format: " + landline, "landline", landline,
                    ValidationException.ValidationErrorType.INVALID_LANDLINE);
        }
    }

    /**
     * Validates a password and throws ValidationException if invalid.
     *
     * @param password the password to validate
     * @throws ValidationException if the password format is invalid
     */
    public static void validatePasswordOrThrow(String password) throws ValidationException {
        if (isInvalidPassword(password)) {
            throw new ValidationException("Invalid password format. Must contain uppercase, lowercase, digit, and special character, and be 8-12 characters long.",
                    "password", password, ValidationException.ValidationErrorType.INVALID_PASSWORD);
        }
    }

    /**
     * Validates a NIC number and throws ValidationException if invalid.
     *
     * @param nic the NIC number to validate
     * @throws ValidationException if the NIC format is invalid
     */
    public static void validateNICOrThrow(String nic) throws ValidationException {
        if (isInvalidNIC(nic)) {
            throw new ValidationException("Invalid NIC format: " + nic, "nic", nic,
                    ValidationException.ValidationErrorType.INVALID_NIC);
        }
    }

    /**
     * Validates that a string contains only letters and throws
     * ValidationException if invalid.
     *
     * @param letter the string to validate
     * @throws ValidationException if the string contains non-letter characters
     */
    public static void validateLetterOrThrow(String letter) throws ValidationException {
        if (isInvalidLetter(letter)) {
            throw new ValidationException("Invalid format. Only letters are allowed: " + letter, "letter", letter,
                    ValidationException.ValidationErrorType.INVALID_LETTERS);
        }
    }

    /**
     * Validates a price and throws ValidationException if invalid.
     *
     * @param price the price to validate
     * @throws ValidationException if the price format is invalid
     */
    public static void validatePriceOrThrow(String price) throws ValidationException {
        if (isInvalidPrice(price)) {
            throw new ValidationException("Invalid price format. Must be a positive number: " + price, "price", price,
                    ValidationException.ValidationErrorType.INVALID_PRICE);
        }
    }

    /**
     * Validates that a string contains only digits and throws
     * ValidationException if invalid.
     *
     * @param number the string to validate
     * @throws ValidationException if the string contains non-digit characters
     */
    public static void validateNumberOrThrow(String number) throws ValidationException {
        if (isInvalidNumber(number)) {
            throw new ValidationException("Invalid format. Only numbers are allowed: " + number, "number", number,
                    ValidationException.ValidationErrorType.INVALID_NUMBER);
        }
    }

}
