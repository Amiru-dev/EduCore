package lk.educore.schoolms.model;

import lk.educore.schoolms.exceptions.BusinessException;
import lk.educore.schoolms.exceptions.ValidationException;
import lk.educore.schoolms.service.FeeService;

import java.util.Date;
import java.util.Objects;

/**
 * Fee Record Domain Model
 * Represents a student fee transaction, tracking payments, balances, and due dates in EduCore.
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class FeeRecord {

    private int feeId;
    private String studentId;
    private String feeType;
    private double amount;
    private double amountPaid;
    private Date dueDate;
    private Date paymentDate;
    private String status; // "Paid", "Pending", "Overdue", "Partial", "Cancelled"

    public FeeRecord() {
        this.status = "Pending";
    }

    public FeeRecord(int feeId, String studentId, String feeType, double amount, Date dueDate)
            throws ValidationException {
        if (amount <= 0) {
            throw new ValidationException("Fee amount must be greater than zero", "amount", String.valueOf(amount));
        }
        this.feeId = feeId;
        this.studentId = studentId;
        this.feeType = feeType;
        this.amount = amount;
        this.amountPaid = 0.0;
        this.dueDate = dueDate;
        this.status = "Pending";
    }

    public double getBalance() throws ValidationException {
        return FeeService.calculateOutstandingBalance(amount, amountPaid);
    }

    public boolean isFullyPaid() {
        return amountPaid >= amount && amount > 0;
    }

    public void recordPayment(double payment, Date payDate) throws ValidationException, BusinessException {
        if (payment <= 0) {
            throw new ValidationException("Payment amount must be positive", "payment", String.valueOf(payment));
        }
        if ("Cancelled".equalsIgnoreCase(status)) {
            throw new BusinessException("Cannot record payment on a cancelled fee",
                    BusinessException.ErrorType.OPERATION_NOT_ALLOWED, "FeeRecord", String.valueOf(feeId));
        }
        if (amountPaid + payment > amount) {
            throw new BusinessException("Payment amount (" + payment + ") exceeds remaining balance (" + (amount - amountPaid) + ")",
                    BusinessException.ErrorType.BUSINESS_RULE_VIOLATION, "FeeRecord", String.valueOf(feeId));
        }

        this.amountPaid += payment;
        this.paymentDate = payDate;
        this.status = FeeService.determineFeeStatus(amount, amountPaid, dueDate, payDate);
    }

    public void evaluateStatus(Date checkDate) {
        if (!"Cancelled".equalsIgnoreCase(status)) {
            this.status = FeeService.determineFeeStatus(amount, amountPaid, dueDate, checkDate);
        }
    }

    // Getters and Setters
    public int getFeeId() { return feeId; }
    public void setFeeId(int feeId) { this.feeId = feeId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getFeeType() { return feeType; }
    public void setFeeType(String feeType) { this.feeType = feeType; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public Date getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeeRecord feeRecord = (FeeRecord) o;
        return feeId == feeRecord.feeId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(feeId);
    }

    @Override
    public String toString() {
        return "FeeRecord{" +
                "feeId=" + feeId +
                ", studentId='" + studentId + '\'' +
                ", feeType='" + feeType + '\'' +
                ", amount=" + amount +
                ", paid=" + amountPaid +
                ", status='" + status + '\'' +
                '}';
    }
}
