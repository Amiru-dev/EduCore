package lk.corevision.fruitshop.exceptions;

/**
 * Custom exception for validation-related errors.
 * This exception should be thrown when input validation fails
 * due to invalid format, missing required fields, or business rule violations.
 */
public class ValidationException extends Exception {
    
    private String fieldName;
    private String fieldValue;
    private ValidationErrorType errorType;
    
    /**
     * Enum for different types of validation errors
     */
    public enum ValidationErrorType {
        INVALID_EMAIL,
        INVALID_PHONE,
        INVALID_MOBILE,
        INVALID_LANDLINE,
        INVALID_PASSWORD,
        INVALID_NIC,
        INVALID_LETTERS,
        INVALID_PRICE,
        INVALID_NUMBER,
        INVALID_DATE,
        REQUIRED_FIELD_MISSING,
        VALUE_OUT_OF_RANGE,
        FORMAT_MISMATCH,
        UNKNOWN
    }
    
    /**
     * Constructs a new ValidationException with the specified detail message.
     * @param message the detail message
     */
    public ValidationException(String message) {
        super(message);
        this.errorType = ValidationErrorType.UNKNOWN;
    }
    
    /**
     * Constructs a new ValidationException with the specified detail message and error type.
     * @param message the detail message
     * @param errorType the type of validation error
     */
    public ValidationException(String message, ValidationErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }
    
    /**
     * Constructs a new ValidationException with the specified detail message, field name, and error type.
     * @param message the detail message
     * @param fieldName the name of the field that failed validation
     * @param errorType the type of validation error
     */
    public ValidationException(String message, String fieldName, ValidationErrorType errorType) {
        super(message);
        this.fieldName = fieldName;
        this.errorType = errorType;
    }
    
    /**
     * Constructs a new ValidationException with the specified detail message, field name, field value, and error type.
     * @param message the detail message
     * @param fieldName the name of the field that failed validation
     * @param fieldValue the value that failed validation
     * @param errorType the type of validation error
     */
    public ValidationException(String message, String fieldName, String fieldValue, ValidationErrorType errorType) {
        super(message);
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.errorType = errorType;
    }
    
    /**
     * Gets the field name that caused the validation error.
     * @return the field name
     */
    public String getFieldName() {
        return fieldName;
    }
    
    /**
     * Sets the field name.
     * @param fieldName the field name to set
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    
    /**
     * Gets the field value that failed validation.
     * @return the field value
     */
    public String getFieldValue() {
        return fieldValue;
    }
    
    /**
     * Sets the field value.
     * @param fieldValue the field value to set
     */
    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
    
    /**
     * Gets the error type.
     * @return the error type
     */
    public ValidationErrorType getErrorType() {
        return errorType;
    }
    
    /**
     * Sets the error type.
     * @param errorType the error type to set
     */
    public void setErrorType(ValidationErrorType errorType) {
        this.errorType = errorType;
    }
}