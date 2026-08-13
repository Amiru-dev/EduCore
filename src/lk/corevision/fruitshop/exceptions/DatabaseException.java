package lk.corevision.fruitshop.exceptions;

/**
 * Custom exception for database-related errors. This exception should be thrown
 * when database operations fail due to connection issues, query errors, or data
 * integrity problems.
 */
public class DatabaseException extends Exception {

    private String errorCode;
    private String sqlState;

    /**
     * Constructs a new DatabaseException with the specified detail message.
     *
     * @param message the detail message
     */
    public DatabaseException(String message) {
        super(message);
    }

    /**
     * Constructs a new DatabaseException with the specified detail message and
     * cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new DatabaseException with the specified detail message,
     * error code, and SQL state.
     *
     * @param message the detail message
     * @param errorCode the database error code
     * @param sqlState the SQL state
     */
    public DatabaseException(String message, String errorCode, String sqlState) {
        super(message);
        this.errorCode = errorCode;
        this.sqlState = sqlState;
    }

    /**
     * Constructs a new DatabaseException with the specified detail message,
     * cause, error code, and SQL state.
     *
     * @param message the detail message
     * @param cause the cause
     * @param errorCode the database error code
     * @param sqlState the SQL state
     */
    public DatabaseException(String message, Throwable cause, String errorCode, String sqlState) {
        super(message, cause);
        this.errorCode = errorCode;
        this.sqlState = sqlState;
    }

    /**
     * Gets the database error code.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Gets the SQL state.
     *
     * @return the SQL state
     */
    public String getSqlState() {
        return sqlState;
    }

    /**
     * Sets the error code.
     *
     * @param errorCode the error code to set
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Sets the SQL state.
     *
     * @param sqlState the SQL state to set
     */
    public void setSqlState(String sqlState) {
        this.sqlState = sqlState;
    }
}
