package lk.educore.schoolms.exceptions;

import java.sql.SQLException;

/**
 * Database Exception
 * Custom exception for database-related errors with SQL state and error code information
 */
public class DatabaseException extends Exception {

    private String sqlState;
    private int errorCode;
    private String sqlMessage;

    /**
     * Constructor with message
     */
    public DatabaseException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with SQLException details
     */
    public DatabaseException(String message, SQLException sqlException) {
        super(message, sqlException);
        this.sqlState = sqlException.getSQLState();
        this.errorCode = sqlException.getErrorCode();
        this.sqlMessage = sqlException.getMessage();
    }

    /**
     * Constructor with full details
     */
    public DatabaseException(String message, String sqlState, int errorCode, String sqlMessage) {
        super(message);
        this.sqlState = sqlState;
        this.errorCode = errorCode;
        this.sqlMessage = sqlMessage;
    }

    /**
     * Get SQL state
     */
    public String getSqlState() {
        return sqlState;
    }

    /**
     * Get error code
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Get SQL message
     */
    public String getSqlMessage() {
        return sqlMessage;
    }

    /**
     * Check if it's a connection error
     */
    public boolean isConnectionError() {
        return sqlState != null && sqlState.startsWith("08");
    }

    /**
     * Check if it's a constraint violation
     */
    public boolean isConstraintViolation() {
        return sqlState != null && sqlState.startsWith("23");
    }

    /**
     * Check if it's a duplicate key error
     */
    public boolean isDuplicateKey() {
        return errorCode == 1062 || (sqlState != null && sqlState.equals("23000"));
    }

    /**
     * Get user-friendly error message
     */
    public String getUserFriendlyMessage() {
        if (isConnectionError()) {
            return "Unable to connect to the database. Please check your connection settings.";
        } else if (isDuplicateKey()) {
            return "This record already exists in the database.";
        } else if (isConstraintViolation()) {
            return "This operation violates a database constraint.";
        } else {
            return "A database error occurred: " + getMessage();
        }
    }
}