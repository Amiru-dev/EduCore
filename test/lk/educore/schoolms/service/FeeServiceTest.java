package lk.educore.schoolms.service;

import lk.educore.schoolms.exceptions.BusinessException;
import lk.educore.schoolms.exceptions.ValidationException;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Unit tests for FeeService business logic.
 * Tests fee collection rate, balances, payment status, financial surplus/deficit, and teacher salaries.
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class FeeServiceTest {

    private Date createDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Test
    public void testCalculateCollectionRate() throws ValidationException {
        // Collected 8000 out of 10000 total (8000 collected + 1500 pending + 500 overdue) = 80.0%
        assertEquals(80.0, FeeService.calculateCollectionRate(8000, 1500, 500), 0.01);

        // 100% collected
        assertEquals(100.0, FeeService.calculateCollectionRate(50000, 0, 0), 0.01);

        // 0% collected
        assertEquals(0.0, FeeService.calculateCollectionRate(0, 10000, 5000), 0.01);

        // Zero total fees
        assertEquals(0.0, FeeService.calculateCollectionRate(0, 0, 0), 0.01);
    }

    @Test(expected = ValidationException.class)
    public void testCalculateCollectionRate_NegativeAmount() throws ValidationException {
        FeeService.calculateCollectionRate(-100, 500, 200);
    }

    @Test
    public void testCalculateOutstandingBalance() throws ValidationException {
        assertEquals(5000.0, FeeService.calculateOutstandingBalance(15000.0, 10000.0), 0.01);
        assertEquals(0.0, FeeService.calculateOutstandingBalance(10000.0, 10000.0), 0.01);
        assertEquals(0.0, FeeService.calculateOutstandingBalance(10000.0, 12000.0), 0.01); // Overpaid
    }

    @Test(expected = ValidationException.class)
    public void testCalculateOutstandingBalance_NegativeFee() throws ValidationException {
        FeeService.calculateOutstandingBalance(-500.0, 100.0);
    }

    @Test
    public void testDetermineFeeStatus() {
        Date dueDate = createDate(2026, Calendar.SEPTEMBER, 30);
        Date beforeDue = createDate(2026, Calendar.SEPTEMBER, 15);
        Date afterDue = createDate(2026, Calendar.OCTOBER, 5);

        // Fully paid
        assertEquals("Paid", FeeService.determineFeeStatus(10000.0, 10000.0, dueDate, beforeDue));
        assertEquals("Paid", FeeService.determineFeeStatus(10000.0, 15000.0, dueDate, afterDue));

        // Overdue (unpaid or partially paid after due date)
        assertEquals("Overdue", FeeService.determineFeeStatus(10000.0, 5000.0, dueDate, afterDue));
        assertEquals("Overdue", FeeService.determineFeeStatus(10000.0, 0.0, dueDate, afterDue));

        // Partial payment before due date
        assertEquals("Partial", FeeService.determineFeeStatus(10000.0, 4000.0, dueDate, beforeDue));

        // Pending (zero paid before due date)
        assertEquals("Pending", FeeService.determineFeeStatus(10000.0, 0.0, dueDate, beforeDue));
    }

    @Test
    public void testCalculateNetFinance() throws ValidationException {
        // Surplus
        assertEquals(25000.0, FeeService.calculateNetFinance(100000.0, 75000.0), 0.01);

        // Deficit
        assertEquals(-15000.0, FeeService.calculateNetFinance(50000.0, 65000.0), 0.01);

        // Break-even
        assertEquals(0.0, FeeService.calculateNetFinance(50000.0, 50000.0), 0.01);
    }

    @Test(expected = ValidationException.class)
    public void testCalculateNetFinance_NegativeIncome() throws ValidationException {
        FeeService.calculateNetFinance(-1000.0, 5000.0);
    }

    @Test
    public void testCalculateGrossSalary() throws ValidationException {
        // Basic: 60000, Allowance: 15000, Bonus: 5000 -> Gross: 80000
        assertEquals(80000.0, FeeService.calculateGrossSalary(60000.0, 15000.0, 5000.0), 0.01);
        assertEquals(50000.0, FeeService.calculateGrossSalary(50000.0, 0.0, 0.0), 0.01);
    }

    @Test(expected = ValidationException.class)
    public void testCalculateGrossSalary_NegativeComponent() throws ValidationException {
        FeeService.calculateGrossSalary(50000.0, -500.0, 0.0);
    }

    @Test
    public void testCalculateNetSalary() throws ValidationException, BusinessException {
        // Basic: 50000, Allowance: 10000, Bonus: 5000 (Gross = 65000)
        // EPF @ 8% of basic = 4000
        // Other deductions = 1000
        // Net = 65000 - 5000 = 60000
        assertEquals(60000.0, FeeService.calculateNetSalary(50000.0, 10000.0, 5000.0, 1000.0, 0.08), 0.01);
    }

    @Test(expected = BusinessException.class)
    public void testCalculateNetSalary_DeductionsExceedGross() throws ValidationException, BusinessException {
        FeeService.calculateNetSalary(10000.0, 0.0, 0.0, 20000.0, 0.08);
    }

    @Test
    public void testCalculateDailyRate() throws ValidationException {
        // Basic: 60000, Working days: 20 -> Daily rate = 3000
        assertEquals(3000.0, FeeService.calculateDailyRate(60000.0, 20), 0.01);
    }

    @Test(expected = ValidationException.class)
    public void testCalculateDailyRate_ZeroWorkingDays() throws ValidationException {
        FeeService.calculateDailyRate(60000.0, 0);
    }
}
