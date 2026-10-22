package lk.educore.schoolms.model;

import lk.educore.schoolms.exceptions.BusinessException;
import lk.educore.schoolms.exceptions.ValidationException;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Unit tests for ExamResult and FeeRecord domain models.
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class ExamResultAndFeeRecordTest {

    private Date createDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Test
    public void testExamResult_GradingAndPassStatus() throws ValidationException {
        ExamResult resDistinction = new ExamResult(1, 10, "STU-0001", "Mathematics", 85.0, 100.0);
        assertEquals("A", resDistinction.getGradeLetter());
        assertEquals(85.0, resDistinction.calculatePercentage(), 0.001);
        assertEquals(4.0, resDistinction.getGradePoint(), 0.001);
        assertTrue(resDistinction.isPassed());

        ExamResult resCredit = new ExamResult(2, 10, "STU-0002", "Mathematics", 55.0, 100.0);
        assertEquals("C", resCredit.getGradeLetter());
        assertEquals(2.0, resCredit.getGradePoint(), 0.001);
        assertTrue(resCredit.isPassed());

        ExamResult resFail = new ExamResult(3, 10, "STU-0003", "Mathematics", 25.0, 100.0);
        assertEquals("W", resFail.getGradeLetter());
        assertEquals(0.0, resFail.getGradePoint(), 0.001);
        assertFalse(resFail.isPassed());
    }

    @Test
    public void testFeeRecord_PaymentLifecycle() throws Exception {
        Date dueDate = createDate(2026, Calendar.SEPTEMBER, 30);
        FeeRecord fee = new FeeRecord(501, "STU-0001", "Term Fee", 15000.0, dueDate);

        assertEquals("Pending", fee.getStatus());
        assertEquals(15000.0, fee.getBalance(), 0.01);
        assertFalse(fee.isFullyPaid());

        // First installment: 5000 on Sept 15
        Date firstPayDate = createDate(2026, Calendar.SEPTEMBER, 15);
        fee.recordPayment(5000.0, firstPayDate);

        assertEquals("Partial", fee.getStatus());
        assertEquals(10000.0, fee.getBalance(), 0.01);
        assertFalse(fee.isFullyPaid());

        // Second installment: 10000 on Sept 25
        Date secondPayDate = createDate(2026, Calendar.SEPTEMBER, 25);
        fee.recordPayment(10000.0, secondPayDate);

        assertEquals("Paid", fee.getStatus());
        assertEquals(0.0, fee.getBalance(), 0.01);
        assertTrue(fee.isFullyPaid());
    }

    @Test(expected = BusinessException.class)
    public void testFeeRecord_OverpaymentForbidden() throws Exception {
        Date dueDate = createDate(2026, Calendar.SEPTEMBER, 30);
        FeeRecord fee = new FeeRecord(502, "STU-0001", "Facility Fee", 5000.0, dueDate);
        fee.recordPayment(6000.0, new Date()); // Exceeds balance
    }

    @Test(expected = BusinessException.class)
    public void testFeeRecord_PaymentOnCancelledFeeForbidden() throws Exception {
        Date dueDate = createDate(2026, Calendar.SEPTEMBER, 30);
        FeeRecord fee = new FeeRecord(503, "STU-0001", "Facility Fee", 5000.0, dueDate);
        fee.setStatus("Cancelled");
        fee.recordPayment(2000.0, new Date());
    }
}
