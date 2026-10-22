package lk.educore.schoolms.service;

import lk.educore.schoolms.exceptions.BusinessException;
import lk.educore.schoolms.exceptions.ValidationException;

import java.util.Calendar;
import java.util.Date;

/**
 * Library Business Service Encapsulates library loan duration, due date
 * calculation, overdue day tracking, fine calculations, borrowing limits, and
 * issue status determination.
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class LibraryService {

    /**
     * Standard loan period in days
     */
    public static final int DEFAULT_LOAN_PERIOD_DAYS = 14;

    /**
     * Standard fine rate per day (LKR)
     */
    public static final double DEFAULT_DAILY_FINE_RATE = 10.0;

    /**
     * Maximum books a student may borrow concurrently
     */
    public static final int MAX_STUDENT_BORROW_LIMIT = 3;

    /**
     * Calculate loan due date given issue date and loan period in days.
     *
     * @param issueDate date the book is issued
     * @param loanDays number of days allowed for borrowing
     * @return due date
     * @throws ValidationException if issueDate is null or loanDays <= 0
     */
    public static Date calculateDueDate(Date issueDate, int loanDays) throws ValidationException {
        if (issueDate == null) {
            throw new ValidationException("Issue date is required", "issueDate", null);
        }
        if (loanDays <= 0) {
            throw new ValidationException("Loan duration must be positive", "loanDays", String.valueOf(loanDays));
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(issueDate);
        cal.add(Calendar.DAY_OF_YEAR, loanDays);
        return cal.getTime();
    }

    /**
     * Calculate overdue days between due date and return/current date. If
     * returned on or before due date, overdue days is 0.
     *
     * @param dueDate scheduled return date
     * @param returnDate actual return date or current check date
     * @return number of days overdue (0 if on-time or early)
     * @throws ValidationException if either date is null
     */
    public static long calculateOverdueDays(Date dueDate, Date returnDate) throws ValidationException {
        if (dueDate == null) {
            throw new ValidationException("Due date is required", "dueDate", null);
        }
        if (returnDate == null) {
            throw new ValidationException("Return date is required", "returnDate", null);
        }

        if (!returnDate.after(dueDate)) {
            return 0L;
        }

        long diffMs = returnDate.getTime() - dueDate.getTime();
        return diffMs / (1000L * 60 * 60 * 24);
    }

    /**
     * Calculate fine amount for an overdue book using default rate (LKR
     * 10.00/day).
     *
     * @param dueDate scheduled return date
     * @param returnDate actual return date
     * @return fine amount (0.0 if not overdue)
     */
    public static double calculateFine(Date dueDate, Date returnDate) throws ValidationException {
        return calculateFine(dueDate, returnDate, DEFAULT_DAILY_FINE_RATE);
    }

    /**
     * Calculate fine amount for an overdue book using a specified daily fine
     * rate.
     *
     * @param dueDate scheduled return date
     * @param returnDate actual return date
     * @param dailyRate fine rate per day in LKR
     * @return fine amount (0.0 if not overdue)
     * @throws ValidationException if dates or dailyRate are invalid
     */
    public static double calculateFine(Date dueDate, Date returnDate, double dailyRate) throws ValidationException {
        if (dailyRate < 0) {
            throw new ValidationException("Daily fine rate cannot be negative", "dailyRate", String.valueOf(dailyRate));
        }
        long overdueDays = calculateOverdueDays(dueDate, returnDate);
        return overdueDays * dailyRate;
    }

    /**
     * Check if a book issue is currently overdue.
     */
    public static boolean isOverdue(Date dueDate, Date currentDate) {
        if (dueDate == null || currentDate == null) {
            return false;
        }
        return currentDate.after(dueDate);
    }

    /**
     * Validate whether a student is eligible to borrow a new book.
     *
     * @param currentBorrowedCount number of books student currently has
     * borrowed
     * @param maxLimit maximum allowed borrow limit
     * @return true if allowed to borrow
     * @throws BusinessException if student has reached or exceeded borrow limit
     */
    public static boolean canStudentBorrow(int currentBorrowedCount, int maxLimit) throws BusinessException {
        if (currentBorrowedCount >= maxLimit) {
            throw new BusinessException("Student has reached maximum allowed borrow limit of " + maxLimit + " books",
                    BusinessException.ErrorType.BUSINESS_RULE_VIOLATION, "BookIssue");
        }
        return true;
    }

    /**
     * Determine book issue status.
     *
     * @param dueDate due date
     * @param returnDate actual return date (null if not yet returned)
     * @param currentDate current system date
     * @return Status string: "Returned", "Returned Late", "Issued", "Overdue"
     */
    public static String determineIssueStatus(Date dueDate, Date returnDate, Date currentDate) {
        if (returnDate != null) {
            if (dueDate != null && returnDate.after(dueDate)) {
                return "Returned Late";
            }
            return "Returned";
        }
        if (dueDate != null && currentDate != null && currentDate.after(dueDate)) {
            return "Overdue";
        }
        return "Issued";
    }
}
