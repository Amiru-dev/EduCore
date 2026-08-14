package lk.educore.schoolms.exceptions;

/**
 * Validation Exception
 * Custom exception for validation errors with field information
 */
public class ValidationException extends Exception {

    private String fieldName;
    private String fieldValue;
    private String validationRule;

    /**
     * Constructor with message
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with field information
     */
    public ValidationException(String message, String fieldName, String fieldValue) {
        super(message);
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    /**
     * Constructor with full details
     */
    public ValidationException(String message, String fieldName, String fieldValue, String validationRule) {
        super(message);
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.validationRule = validationRule;
    }

    /**
     * Get field name
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Get field value
     */
    public String getFieldValue() {
        return fieldValue;
    }

    /**
     * Get validation rule
     */
    public String getValidationRule() {
        return validationRule;
    }

    /**
     * Get user-friendly error message
     */
    public String getUserFriendlyMessage() {
        if (fieldName != null) {
            return "Invalid " + fieldName + ": " + getMessage();
        }
        return getMessage();
    }

    /**
     * Check if it's an email validation error
     */
    public boolean isEmailError() {
        return "email".equalsIgnoreCase(fieldName) || 
               getMessage().toLowerCase().contains("email");
    }

    /**
     * Check if it's a phone validation error
     */
    public boolean isPhoneError() {
        return "phone".equalsIgnoreCase(fieldName) || 
               getMessage().toLowerCase().contains("phone");
    }

    /**
     * Check if it's a NIC validation error
     */
    public boolean isNICError() {
        return "nic".equalsIgnoreCase(fieldName) || 
               getMessage().toLowerCase().contains("nic");
    }

    /**
     * Check if it's a required field error
     */
    public boolean isRequiredFieldError() {
        return getMessage().toLowerCase().contains("required") || 
               getMessage().toLowerCase().contains("empty");
    }
}