package lk.educore.schoolms.exceptions;

/**
 * Email Exception
 * Custom exception for email-related errors with error type information
 */
public class EmailException extends Exception {

    public enum ErrorType {
        AUTHENTICATION_FAILED,
        CONNECTION_FAILED,
        INVALID_RECIPIENT,
        ATTACHMENT_ERROR,
        SENDING_FAILED,
        TIMEOUT,
        UNKNOWN
    }

    private ErrorType errorType;
    private String recipient;

    /**
     * Constructor with message
     */
    public EmailException(String message) {
        super(message);
        this.errorType = ErrorType.UNKNOWN;
    }

    /**
     * Constructor with message and cause
     */
    public EmailException(String message, Throwable cause) {
        super(message, cause);
        this.errorType = ErrorType.UNKNOWN;
    }

    /**
     * Constructor with error type
     */
    public EmailException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    /**
     * Constructor with error type and recipient
     */
    public EmailException(String message, ErrorType errorType, String recipient) {
        super(message);
        this.errorType = errorType;
        this.recipient = recipient;
    }

    /**
     * Constructor with full details
     */
    public EmailException(String message, ErrorType errorType, String recipient, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
        this.recipient = recipient;
    }

    /**
     * Get error type
     */
    public ErrorType getErrorType() {
        return errorType;
    }

    /**
     * Get recipient
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * Get user-friendly error message
     */
    public String getUserFriendlyMessage() {
        switch (errorType) {
            case AUTHENTICATION_FAILED:
                return "Email authentication failed. Please check your email credentials.";
            case CONNECTION_FAILED:
                return "Failed to connect to email server. Please check your internet connection.";
            case INVALID_RECIPIENT:
                return "Invalid email recipient: " + recipient;
            case ATTACHMENT_ERROR:
                return "Failed to attach file to email.";
            case SENDING_FAILED:
                return "Failed to send email. Please try again.";
            case TIMEOUT:
                return "Email sending timed out. Please try again.";
            default:
                return "An email error occurred: " + getMessage();
        }
    }
}