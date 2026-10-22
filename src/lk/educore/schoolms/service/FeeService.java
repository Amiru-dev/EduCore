package lk.educore.schoolms.service;

import lk.educore.schoolms.exceptions.BusinessException;
import lk.educore.schoolms.exceptions.ValidationException;

import java.util.Date;

/**
 * Fee & Financial Business Service Encapsulates fee status evaluation,
 * collection rate analytics, student balance calculations, income/expense
 * balancing, and teacher salary computation.
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class FeeService {

    /**
     * Standard EPF employee deduction rate (8%)
     */
    public static final double DEFAULT_EPF_RATE = 0.08;

    /**
     * Standard ETF employer contribution rate (3%)
     */
    public static final double DEFAULT_ETF_RATE = 0.03;

    /**
     * Calculate fee collection rate percentage. Rate = (collected / (collected
     * + pending + overdue)) * 100.0
     *
     * @param totalCollected total amount paid
     * @param totalPending total pending amount
     * @param totalOverdue total overdue amount
     * @return collection rate percentage (0.0 - 100.0)
     * @throws ValidationException if any input amount is negative
     */
    public static double calculateCollectionRate(double totalCollected, double totalPending, double totalOverdue)
            throws ValidationException {
        if (totalCollected < 0 || totalPending < 0 || totalOverdue < 0) {
            throw new ValidationException("Financial amounts cannot be negative", "amount", "negative values");
        }
        double totalBilled = totalCollected + totalPending + totalOverdue;
        if (totalBilled == 0.0) {
            return 0.0;
        }
        double rate = (totalCollected / totalBilled) * 100.0;
        return Math.round(rate * 100.0) / 100.0;
    }

    /**
     * Calculate outstanding balance for a fee record.
     *
     * @param feeAmount total fee billed
     * @param amountPaid total amount paid
     * @return remaining balance
     * @throws ValidationException if amounts are invalid or amountPaid is
     * negative
     */
    public static double calculateOutstandingBalance(double feeAmount, double amountPaid) throws ValidationException {
        if (feeAmount < 0) {
            throw new ValidationException("Fee amount cannot be negative", "feeAmount", String.valueOf(feeAmount));
        }
        if (amountPaid < 0) {
            throw new ValidationException("Amount paid cannot be negative", "amountPaid", String.valueOf(amountPaid));
        }
        double balance = feeAmount - amountPaid;
        return balance < 0.0 ? 0.0 : Math.round(balance * 100.0) / 100.0;
    }

    /**
     * Determine fee payment status.
     *
     * @param totalAmount total required fee
     * @param amountPaid amount paid so far
     * @param dueDate fee payment deadline
     * @param checkDate current date or date of evaluation
     * @return "Paid", "Partial", "Pending", or "Overdue"
     */
    public static String determineFeeStatus(double totalAmount, double amountPaid, Date dueDate, Date checkDate) {
        if (amountPaid >= totalAmount && totalAmount > 0) {
            return "Paid";
        }
        if (dueDate != null && checkDate != null && checkDate.after(dueDate)) {
            return "Overdue";
        }
        if (amountPaid > 0 && amountPaid < totalAmount) {
            return "Partial";
        }
        return "Pending";
    }

    /**
     * Calculate net financial result (Income - Expense).
     *
     * @param totalIncome total received income
     * @param totalExpense total incurred expenses
     * @return net amount (positive for surplus, negative for deficit)
     * @throws ValidationException if income or expense is negative
     */
    public static double calculateNetFinance(double totalIncome, double totalExpense) throws ValidationException {
        if (totalIncome < 0) {
            throw new ValidationException("Total income cannot be negative", "totalIncome", String.valueOf(totalIncome));
        }
        if (totalExpense < 0) {
            throw new ValidationException("Total expense cannot be negative", "totalExpense", String.valueOf(totalExpense));
        }
        return Math.round((totalIncome - totalExpense) * 100.0) / 100.0;
    }

    /**
     * Calculate gross teacher salary. Gross = basicSalary + allowance + bonus
     *
     * @param basicSalary base salary
     * @param allowance additional allowances
     * @param bonus bonus amount
     * @return gross salary
     * @throws ValidationException if any component is negative
     */
    public static double calculateGrossSalary(double basicSalary, double allowance, double bonus)
            throws ValidationException {
        if (basicSalary < 0) {
            throw new ValidationException("Basic salary cannot be negative", "basicSalary", String.valueOf(basicSalary));
        }
        if (allowance < 0) {
            throw new ValidationException("Allowance cannot be negative", "allowance", String.valueOf(allowance));
        }
        if (bonus < 0) {
            throw new ValidationException("Bonus cannot be negative", "bonus", String.valueOf(bonus));
        }
        return Math.round((basicSalary + allowance + bonus) * 100.0) / 100.0;
    }

    /**
     * Calculate net teacher salary after standard EPF deduction and other
     * deductions.
     *
     * @param basicSalary base salary
     * @param allowance allowances
     * @param bonus bonuses
     * @param otherDeductions additional deductions (unpaid leaves, etc.)
     * @param epfRate EPF percentage (e.g. 0.08 for 8%)
     * @return net salary
     */
    public static double calculateNetSalary(double basicSalary, double allowance, double bonus,
            double otherDeductions, double epfRate)
            throws ValidationException, BusinessException {
        if (epfRate < 0 || epfRate > 1.0) {
            throw new ValidationException("EPF rate must be between 0.0 and 1.0", "epfRate", String.valueOf(epfRate));
        }
        if (otherDeductions < 0) {
            throw new ValidationException("Deductions cannot be negative", "otherDeductions", String.valueOf(otherDeductions));
        }
        double gross = calculateGrossSalary(basicSalary, allowance, bonus);
        double epfDeduction = basicSalary * epfRate;
        double totalDeductions = epfDeduction + otherDeductions;
        if (totalDeductions > gross) {
            throw new BusinessException("Total deductions exceed gross salary", BusinessException.ErrorType.BUSINESS_RULE_VIOLATION);
        }
        double net = gross - totalDeductions;
        return Math.round(net * 100.0) / 100.0;
    }

    /**
     * Calculate daily wage / salary rate based on basic salary and working
     * days.
     */
    public static double calculateDailyRate(double basicSalary, int workingDays) throws ValidationException {
        if (basicSalary < 0) {
            throw new ValidationException("Basic salary cannot be negative", "basicSalary", String.valueOf(basicSalary));
        }
        if (workingDays <= 0) {
            throw new ValidationException("Working days must be greater than zero", "workingDays", String.valueOf(workingDays));
        }
        double daily = basicSalary / workingDays;
        return Math.round(daily * 100.0) / 100.0;
    }
}
