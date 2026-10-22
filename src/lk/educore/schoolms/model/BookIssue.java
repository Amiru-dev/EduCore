package lk.educore.schoolms.model;

import lk.educore.schoolms.exceptions.ValidationException;
import lk.educore.schoolms.service.LibraryService;

import java.util.Date;
import java.util.Objects;

/**
 * Book Issue Domain Model
 * Represents a library book borrowing record, tracking due dates, returns, and fines.
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class BookIssue {

    private int issueId;
    private int bookId;
    private String studentId;
    private Date issueDate;
    private Date dueDate;
    private Date returnDate;
    private double fineAmount;
    private String status; // "Issued", "Returned", "Returned Late", "Overdue"

    public BookIssue() {
        this.status = "Issued";
    }

    public BookIssue(int issueId, int bookId, String studentId, Date issueDate, int loanDays)
            throws ValidationException {
        this.issueId = issueId;
        this.bookId = bookId;
        this.studentId = studentId;
        this.issueDate = issueDate;
        this.dueDate = LibraryService.calculateDueDate(issueDate, loanDays);
        this.status = "Issued";
    }

    public long getOverdueDays(Date checkDate) throws ValidationException {
        return LibraryService.calculateOverdueDays(dueDate, checkDate != null ? checkDate : new Date());
    }

    public double calculateFine(Date returnDate, double dailyRate) throws ValidationException {
        return LibraryService.calculateFine(dueDate, returnDate, dailyRate);
    }

    public void markReturned(Date returnDate, double dailyRate) throws ValidationException {
        this.returnDate = returnDate;
        this.fineAmount = calculateFine(returnDate, dailyRate);
        this.status = LibraryService.determineIssueStatus(dueDate, returnDate, new Date());
    }

    public boolean isCurrentlyOverdue(Date checkDate) {
        return LibraryService.isOverdue(dueDate, checkDate);
    }

    // Getters and Setters
    public int getIssueId() { return issueId; }
    public void setIssueId(int issueId) { this.issueId = issueId; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public Date getIssueDate() { return issueDate; }
    public void setIssueDate(Date issueDate) { this.issueDate = issueDate; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }

    public double getFineAmount() { return fineAmount; }
    public void setFineAmount(double fineAmount) { this.fineAmount = fineAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookIssue bookIssue = (BookIssue) o;
        return issueId == bookIssue.issueId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(issueId);
    }

    @Override
    public String toString() {
        return "BookIssue{" +
                "issueId=" + issueId +
                ", bookId=" + bookId +
                ", studentId='" + studentId + '\'' +
                ", status='" + status + '\'' +
                ", fineAmount=" + fineAmount +
                '}';
    }
}
