package lk.educore.schoolms.exceptions;

/**
 * Business Exception Custom exception for business logic errors
 */
public class BusinessException extends Exception {

    public enum ErrorType {
        DUPLICATE_RECORD,
        RECORD_NOT_FOUND,
        INVALID_OPERATION,
        INSUFFICIENT_PERMISSIONS,
        OPERATION_NOT_ALLOWED,
        CONFLICT,
        BUSINESS_RULE_VIOLATION,
        UNKNOWN
    }

    private ErrorType errorType;
    private String entity;
    private String entityId;

    /**
     * Constructor with message
     */
    public BusinessException(String message) {
        super(message);
        this.errorType = ErrorType.UNKNOWN;
    }

    /**
     * Constructor with message and cause
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.errorType = ErrorType.UNKNOWN;
    }

    /**
     * Constructor with error type
     */
    public BusinessException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    /**
     * Constructor with error type and entity
     */
    public BusinessException(String message, ErrorType errorType, String entity) {
        super(message);
        this.errorType = errorType;
        this.entity = entity;
    }

    /**
     * Constructor with full details
     */
    public BusinessException(String message, ErrorType errorType, String entity, String entityId) {
        super(message);
        this.errorType = errorType;
        this.entity = entity;
        this.entityId = entityId;
    }

    /**
     * Get error type
     */
    public ErrorType getErrorType() {
        return errorType;
    }

    /**
     * Get entity
     */
    public String getEntity() {
        return entity;
    }

    /**
     * Get entity ID
     */
    public String getEntityId() {
        return entityId;
    }

    /**
     * Get user-friendly error message
     */
    public String getUserFriendlyMessage() {
        switch (errorType) {
            case DUPLICATE_RECORD:
                String duplicateMsg = "This " + (entity != null ? entity : "record") + " already exists.";
                if (entityId != null) {
                    duplicateMsg += " ID: " + entityId;
                }
                return duplicateMsg;
            case RECORD_NOT_FOUND:
                String notFoundMsg = (entity != null ? entity : "Record") + " not found.";
                if (entityId != null) {
                    notFoundMsg += " ID: " + entityId;
                }
                return notFoundMsg;
            case INVALID_OPERATION:
                return "Invalid operation: " + getMessage();
            case INSUFFICIENT_PERMISSIONS:
                return "You don't have sufficient permissions to perform this operation.";
            case OPERATION_NOT_ALLOWED:
                return "This operation is not allowed: " + getMessage();
            case CONFLICT:
                return "There is a conflict with existing data: " + getMessage();
            case BUSINESS_RULE_VIOLATION:
                return "Business rule violation: " + getMessage();
            default:
                return "A business error occurred: " + getMessage();
        }
    }

    /**
     * Check if it's a duplicate record error
     */
    public boolean isDuplicateRecord() {
        return errorType == ErrorType.DUPLICATE_RECORD;
    }

    /**
     * Check if it's a record not found error
     */
    public boolean isRecordNotFound() {
        return errorType == ErrorType.RECORD_NOT_FOUND;
    }

    /**
     * Check if it's a permission error
     */
    public boolean isPermissionError() {
        return errorType == ErrorType.INSUFFICIENT_PERMISSIONS;
    }
}
