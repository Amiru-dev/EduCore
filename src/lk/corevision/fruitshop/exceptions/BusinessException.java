package lk.corevision.fruitshop.exceptions;

/**
 * Custom exception for business logic errors.
 * This exception should be thrown when business rules are violated
 * or expected business operations cannot be completed.
 */
public class BusinessException extends Exception {
    
    private String businessOperation;
    private BusinessErrorType errorType;
    private Object[] errorDetails;
    
    /**
     * Enum for different types of business errors
     */
    public enum BusinessErrorType {
        INSUFFICIENT_STOCK,
        PRODUCT_NOT_FOUND,
        CUSTOMER_NOT_FOUND,
        EMPLOYEE_NOT_FOUND,
        SUPPLIER_NOT_FOUND,
        ORDER_NOT_FOUND,
        DUPLICATE_RECORD,
        INVALID_STATUS_TRANSITION,
        PAYMENT_FAILED,
        REFUND_FAILED,
        PERMISSION_DENIED,
        OPERATION_NOT_ALLOWED,
        CONSTRAINT_VIOLATION,
        UNKNOWN
    }
    
    /**
     * Constructs a new BusinessException with the specified detail message.
     * @param message the detail message
     */
    public BusinessException(String message) {
        super(message);
        this.errorType = BusinessErrorType.UNKNOWN;
    }
    
    /**
     * Constructs a new BusinessException with the specified detail message and cause.
     * @param message the detail message
     * @param cause the cause
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.errorType = BusinessErrorType.UNKNOWN;
    }
    
    /**
     * Constructs a new BusinessException with the specified detail message and error type.
     * @param message the detail message
     * @param errorType the type of business error
     */
    public BusinessException(String message, BusinessErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }
    
    /**
     * Constructs a new BusinessException with the specified detail message, operation, and error type.
     * @param message the detail message
     * @param businessOperation the business operation that failed
     * @param errorType the type of business error
     */
    public BusinessException(String message, String businessOperation, BusinessErrorType errorType) {
        super(message);
        this.businessOperation = businessOperation;
        this.errorType = errorType;
    }
    
    /**
     * Constructs a new BusinessException with the specified detail message, operation, error type, and details.
     * @param message the detail message
     * @param businessOperation the business operation that failed
     * @param errorType the type of business error
     * @param errorDetails additional details about the error
     */
    public BusinessException(String message, String businessOperation, BusinessErrorType errorType, Object[] errorDetails) {
        super(message);
        this.businessOperation = businessOperation;
        this.errorType = errorType;
        this.errorDetails = errorDetails;
    }
    
    /**
     * Gets the business operation that failed.
     * @return the business operation
     */
    public String getBusinessOperation() {
        return businessOperation;
    }
    
    /**
     * Sets the business operation.
     * @param businessOperation the business operation to set
     */
    public void setBusinessOperation(String businessOperation) {
        this.businessOperation = businessOperation;
    }
    
    /**
     * Gets the error type.
     * @return the error type
     */
    public BusinessErrorType getErrorType() {
        return errorType;
    }
    
    /**
     * Sets the error type.
     * @param errorType the error type to set
     */
    public void setErrorType(BusinessErrorType errorType) {
        this.errorType = errorType;
    }
    
    /**
     * Gets the error details.
     * @return the error details
     */
    public Object[] getErrorDetails() {
        return errorDetails;
    }
    
    /**
     * Sets the error details.
     * @param errorDetails the error details to set
     */
    public void setErrorDetails(Object[] errorDetails) {
        this.errorDetails = errorDetails;
    }
}