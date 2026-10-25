package lk.educore.schoolms.validate;

import lk.educore.schoolms.exceptions.ValidationException;
import java.util.regex.*;

/**
 * Data Validator
 * Provides validation methods for common data types used in the school management system
 */
public class DataValidator {

    // Email regex pattern
    private static final String EMAIL_PATTERN = 
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    
    // Sri Lankan NIC pattern (old: 9 digits + V/X, new: 12 digits)
    private static final String NIC_OLD_PATTERN = "^[0-9]{9}[vVxX]$";
    private static final String NIC_NEW_PATTERN = "^[0-9]{12}$";
    
    // Phone number pattern (Sri Lanka: 10 digits starting with 0)
    private static final String PHONE_PATTERN = "^0[0-9]{9}$";
    
    // Student ID pattern (STU-XXXX)
    private static final String STUDENT_ID_PATTERN = "^STU-[0-9]{4}$";
    
    // Letters only pattern
    private static final String LETTERS_ONLY_PATTERN = "^[a-zA-Z\\s]+$";
    
    // Numbers only pattern
    private static final String NUMBERS_ONLY_PATTERN = "^[0-9]+$";
    
    // Decimal number pattern
    private static final String DECIMAL_PATTERN = "^[0-9]+\\.?[0-9]*$";

    /**
     * Validate email address
     */
    public static boolean isValidEmail(String email) throws ValidationException {
        return isValidEmail(email, "Email");
    }

    /**
     * Validate email address with field label
     */
    public static boolean isValidEmail(String email, String fieldLabel) throws ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException(fieldLabel + " is required", fieldLabel, email);
        }

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            throw new ValidationException("Invalid " + fieldLabel + " format", fieldLabel, email, EMAIL_PATTERN);
        }

        return true;
    }

    /**
     * Validate Sri Lankan NIC
     */
    public static boolean isValidNIC(String nic) throws ValidationException {
        return isValidNIC(nic, "NIC");
    }

    /**
     * Validate Sri Lankan NIC with field label
     */
    public static boolean isValidNIC(String nic, String fieldLabel) throws ValidationException {
        if (nic == null || nic.trim().isEmpty()) {
            throw new ValidationException(fieldLabel + " is required", fieldLabel, nic);
        }

        Pattern oldPattern = Pattern.compile(NIC_OLD_PATTERN);
        Pattern newPattern = Pattern.compile(NIC_NEW_PATTERN);

        if (!oldPattern.matcher(nic).matches() && !newPattern.matcher(nic).matches()) {
            throw new ValidationException("Invalid " + fieldLabel + " format (use 9 digits + V/X or 12 digits)", fieldLabel, nic);
        }

        return true;
    }

    /**
     * Validate phone number
     */
    public static boolean isValidPhone(String phone) throws ValidationException {
        return isValidPhone(phone, "Phone number");
    }

    /**
     * Validate phone number with field label
     */
    public static boolean isValidPhone(String phone, String fieldLabel) throws ValidationException {
        if (phone == null || phone.trim().isEmpty()) {
            throw new ValidationException(fieldLabel + " is required", fieldLabel, phone);
        }

        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phone);

        if (!matcher.matches()) {
            throw new ValidationException("Invalid " + fieldLabel + " format (use 10 digits starting with 0)", fieldLabel, phone, PHONE_PATTERN);
        }

        return true;
    }

    /**
     * Validate password
     */
    public static boolean isValidPassword(String password) throws ValidationException {
        if (password == null || password.trim().isEmpty()) {
            throw new ValidationException("Password is required", "password", password);
        }
        
        if (password.length() < 6) {
            throw new ValidationException("Password must be at least 6 characters", "password", password);
        }
        
        return true;
    }

    /**
     * Validate student ID
     */
    public static boolean isValidStudentID(String studentId) throws ValidationException {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new ValidationException("Student ID is required", "studentId", studentId);
        }
        
        Pattern pattern = Pattern.compile(STUDENT_ID_PATTERN);
        Matcher matcher = pattern.matcher(studentId);
        
        if (!matcher.matches()) {
            throw new ValidationException("Invalid student ID format (use STU-XXXX)", "studentId", studentId, STUDENT_ID_PATTERN);
        }
        
        return true;
    }

    /**
     * Validate letters only (for names)
     */
    public static boolean isLettersOnly(String text) throws ValidationException {
        return isLettersOnly(text, "This field");
    }

    /**
     * Validate letters only (for names) with custom field name
     */
    public static boolean isLettersOnly(String text, String fieldName) throws ValidationException {
        if (text == null || text.trim().isEmpty()) {
            throw new ValidationException(fieldName + " is required", "name", text);
        }
        
        Pattern pattern = Pattern.compile(LETTERS_ONLY_PATTERN);
        Matcher matcher = pattern.matcher(text);
        
        if (!matcher.matches()) {
            throw new ValidationException(fieldName + " should contain only letters", "name", text, LETTERS_ONLY_PATTERN);
        }
        
        return true;
    }

    /**
     * Validate numbers only
     */
    public static boolean isNumbersOnly(String text) throws ValidationException {
        if (text == null || text.trim().isEmpty()) {
            throw new ValidationException("This field is required", "number", text);
        }
        
        Pattern pattern = Pattern.compile(NUMBERS_ONLY_PATTERN);
        Matcher matcher = pattern.matcher(text);
        
        if (!matcher.matches()) {
            throw new ValidationException("This field should contain only numbers", "number", text, NUMBERS_ONLY_PATTERN);
        }
        
        return true;
    }

    /**
     * Validate decimal number
     */
    public static boolean isDecimal(String text) throws ValidationException {
        if (text == null || text.trim().isEmpty()) {
            throw new ValidationException("This field is required", "decimal", text);
        }
        
        Pattern pattern = Pattern.compile(DECIMAL_PATTERN);
        Matcher matcher = pattern.matcher(text);
        
        if (!matcher.matches()) {
            throw new ValidationException("Invalid decimal number format", "decimal", text, DECIMAL_PATTERN);
        }
        
        return true;
    }

    /**
     * Validate required field (not empty)
     */
    public static boolean isRequired(String text) throws ValidationException {
        if (text == null || text.trim().isEmpty()) {
            throw new ValidationException("This field is required", "required", text);
        }
        return true;
    }

    /**
     * Validate required field with field label
     */
    public static boolean isRequired(String text, String fieldLabel) throws ValidationException {
        if (text == null || text.trim().isEmpty()) {
            throw new ValidationException(fieldLabel + " is required", fieldLabel, text);
        }
        return true;
    }

    /**
     * Validate string length
     */
    public static boolean isValidLength(String text, int minLength, int maxLength) throws ValidationException {
        if (text == null || text.trim().isEmpty()) {
            throw new ValidationException("This field is required", "length", text);
        }
        
        if (text.length() < minLength) {
            throw new ValidationException("This field must be at least " + minLength + " characters", "length", text);
        }
        
        if (text.length() > maxLength) {
            throw new ValidationException("This field must not exceed " + maxLength + " characters", "length", text);
        }
        
        return true;
    }

    /**
     * Validate positive number
     */
    public static boolean isPositiveNumber(String text) throws ValidationException {
        return isPositiveNumber(text, "This field");
    }

    /**
     * Validate positive number with field label
     */
    public static boolean isPositiveNumber(String text, String fieldLabel) throws ValidationException {
        if (text == null || text.trim().isEmpty()) {
            throw new ValidationException(fieldLabel + " is required", fieldLabel, text);
        }
        
        try {
            double value = Double.parseDouble(text);
            if (value <= 0) {
                throw new ValidationException(fieldLabel + " must be a positive number", fieldLabel, text);
            }
        } catch (NumberFormatException e) {
            throw new ValidationException(fieldLabel + " must be a valid number", fieldLabel, text);
        }
        
        return true;
    }

    /**
     * Validate date of birth (not in future, reasonable age)
     */
    public static boolean isValidDOB(String dob) throws ValidationException {
        if (dob == null || dob.trim().isEmpty()) {
            throw new ValidationException("Date of birth is required", "dob", dob);
        }
        
        try {
            java.time.LocalDate date = java.time.LocalDate.parse(dob);
            java.time.LocalDate today = java.time.LocalDate.now();
            
            if (date.isAfter(today)) {
                throw new ValidationException("Date of birth cannot be in the future", "dob", dob);
            }
            
            if (date.isBefore(today.minusYears(120))) {
                throw new ValidationException("Invalid date of birth", "dob", dob);
            }
            
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new ValidationException("Invalid date format (use YYYY-MM-DD)", "dob", dob);
        }
        
        return true;
    }

    /**
     * Validate amount (positive decimal with 2 decimal places)
     */
    public static boolean isValidAmount(String amount) throws ValidationException {
        if (amount == null || amount.trim().isEmpty()) {
            throw new ValidationException("Amount is required", "amount", amount);
        }
        
        try {
            double value = Double.parseDouble(amount);
            if (value <= 0) {
                throw new ValidationException("Amount must be positive", "amount", amount);
            }
            
            // Check decimal places
            String[] parts = amount.split("\\.");
            if (parts.length == 2 && parts[1].length() > 2) {
                throw new ValidationException("Amount can have maximum 2 decimal places", "amount", amount);
            }
            
        } catch (NumberFormatException e) {
            throw new ValidationException("Invalid amount format", "amount", amount);
        }
        
        return true;
    }

    /**
     * Validate date range (end date cannot come before start date)
     */
    public static boolean isValidDateRange(java.util.Date startDate, java.util.Date endDate) throws ValidationException {
        if (startDate != null && endDate != null && endDate.before(startDate)) {
            throw new ValidationException("End date cannot come before start date", "endDate", endDate.toString());
        }
        return true;
    }

    /**
     * Generic validation without throwing exception
     */
    public static boolean validate(String text, String pattern) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        return m.matches();
    }
}