package lk.corevision.fruitshop.exceptions;

/**
 * Custom exception for email-related errors.
 * This exception should be thrown when email operations fail
 * due to authentication issues, network problems, or invalid recipient addresses.
 */
public class EmailException extends Exception {
    
    private String recipientAddress;
    private EmailErrorType errorType;
    
    /**
     * Enum for different types of email errors
     */
    public enum EmailErrorType {
        AUTHENTICATION_FAILED,
        CONNECTION_FAILED,
        INVALID_RECIPIENT,
        INVALID_SENDER,
        ATTACHMENT_FAILED,
        TIMEOUT,
        UNKNOWN
    }
    
    /**
     * Constructs a new EmailException with the specified detail message.
     * @param message the detail message
     */
    public EmailException(String message) {
        super(message);
        this.errorType = EmailErrorType.UNKNOWN;
    }
    
    /**
     * Constructs a new EmailException with the specified detail message and cause.
     * @param message the detail message
     * @param cause the cause
     */
    public EmailException(String message, Throwable cause) {
        super(message, cause);
        this.errorType = EmailErrorType.UNKNOWN;
    }
    
    /**
     * Constructs a new EmailException with the specified detail message and error type.
     * @param message the detail message
     * @param errorType the type of email error
     */
    public EmailException(String message, EmailErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }
    
    /**
     * Constructs a new EmailException with the specified detail message, cause, and error type.
     * @param message the detail message
     * @param cause the cause
     * @param errorType the type of email error
     */
    public EmailException(String message, Throwable cause, EmailErrorType errorType) {
        super(message, cause);
        this.errorType = errorType;
    }
    
    /**
     * Constructs a new EmailException with the specified detail message, recipient address, and error type.
     * @param message the detail message
     * @param recipientAddress the email address that caused the error
     * @param errorType the type of email error
     */
    public EmailException(String message, String recipientAddress, EmailErrorType errorType) {
        super(message);
        this.recipientAddress = recipientAddress;
        this.errorType = errorType;
    }
    
    /**
     * Gets the recipient address that caused the error.
     * @return the recipient address
     */
    public String getRecipientAddress() {
        return recipientAddress;
    }
    
    /**
     * Sets the recipient address.
     * @param recipientAddress the recipient address to set
     */
    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }
    
    /**
     * Gets the error type.
     * @return the error type
     */
    public EmailErrorType getErrorType() {
        return errorType;
    }
    
    /**
     * Sets the error type.
     * @param errorType the error type to set
     */
    public void setErrorType(EmailErrorType errorType) {
        this.errorType = errorType;
    }
}