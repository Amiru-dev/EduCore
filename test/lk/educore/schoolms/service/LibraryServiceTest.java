package lk.educore.schoolms.service;

import lk.educore.schoolms.exceptions.BusinessException;
import lk.educore.schoolms.exceptions.ValidationException;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Unit tests for LibraryService business logic.
 * Tests due date generation, overdue day tracking, fines, limits, and statuses.
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class LibraryServiceTest {

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
    public void testCalculateDueDate() throws ValidationException {
        Date issueDate = createDate(2026, Calendar.JANUARY, 1);
        Date dueDate = LibraryService.calculateDueDate(issueDate, 14);

        Calendar cal = Calendar.getInstance();
        cal.setTime(dueDate);
        assertEquals(2026, cal.get(Calendar.YEAR));
        assertEquals(Calendar.JANUARY, cal.get(Calendar.MONTH));
        assertEquals(15, cal.get(Calendar.DAY_OF_MONTH));
    }

    @Test(expected = ValidationException.class)
    public void testCalculateDueDate_NullIssueDate() throws ValidationException {
        LibraryService.calculateDueDate(null, 14);
    }

    @Test(expected = ValidationException.class)
    public void testCalculateDueDate_ZeroLoanDays() throws ValidationException {
        LibraryService.calculateDueDate(new Date(), 0);
    }

    @Test
    public void testCalculateOverdueDays() throws ValidationException {
        Date dueDate = createDate(2026, Calendar.MARCH, 15);

        // Returned before due date
        Date earlyReturn = createDate(2026, Calendar.MARCH, 10);
        assertEquals(0L, LibraryService.calculateOverdueDays(dueDate, earlyReturn));

        // Returned on exact due date
        Date onTimeReturn = createDate(2026, Calendar.MARCH, 15);
        assertEquals(0L, LibraryService.calculateOverdueDays(dueDate, onTimeReturn));

        // Returned 5 days late
        Date lateReturn5Days = createDate(2026, Calendar.MARCH, 20);
        assertEquals(5L, LibraryService.calculateOverdueDays(dueDate, lateReturn5Days));

        // Returned 30 days late
        Date lateReturn30Days = createDate(2026, Calendar.APRIL, 14);
        assertEquals(30L, LibraryService.calculateOverdueDays(dueDate, lateReturn30Days));
    }

    @Test
    public void testCalculateFine_DefaultRate() throws ValidationException {
        Date dueDate = createDate(2026, Calendar.MAY, 1);

        // On time -> 0 fine
        Date onTime = createDate(2026, Calendar.MAY, 1);
        assertEquals(0.0, LibraryService.calculateFine(dueDate, onTime), 0.001);

        // 3 days overdue @ 10 LKR/day = 30 LKR
        Date threeDaysLate = createDate(2026, Calendar.MAY, 4);
        assertEquals(30.0, LibraryService.calculateFine(dueDate, threeDaysLate), 0.001);

        // 10 days overdue @ 10 LKR/day = 100 LKR
        Date tenDaysLate = createDate(2026, Calendar.MAY, 11);
        assertEquals(100.0, LibraryService.calculateFine(dueDate, tenDaysLate), 0.001);
    }

    @Test
    public void testCalculateFine_CustomRate() throws ValidationException {
        Date dueDate = createDate(2026, Calendar.JUNE, 1);
        Date fiveDaysLate = createDate(2026, Calendar.JUNE, 6);

        // Custom rate 25 LKR / day * 5 days = 125 LKR
        assertEquals(125.0, LibraryService.calculateFine(dueDate, fiveDaysLate, 25.0), 0.001);
    }

    @Test(expected = ValidationException.class)
    public void testCalculateFine_NegativeRate() throws ValidationException {
        Date dueDate = createDate(2026, Calendar.JUNE, 1);
        Date late = createDate(2026, Calendar.JUNE, 5);
        LibraryService.calculateFine(dueDate, late, -5.0);
    }

    @Test
    public void testIsOverdue() {
        Date dueDate = createDate(2026, Calendar.JULY, 15);
        Date beforeDue = createDate(2026, Calendar.JULY, 10);
        Date afterDue = createDate(2026, Calendar.JULY, 20);

        assertFalse(LibraryService.isOverdue(dueDate, beforeDue));
        assertTrue(LibraryService.isOverdue(dueDate, afterDue));
        assertFalse(LibraryService.isOverdue(null, afterDue));
    }

    @Test
    public void testCanStudentBorrow() throws BusinessException {
        // Can borrow when under limit
        assertTrue(LibraryService.canStudentBorrow(0, 3));
        assertTrue(LibraryService.canStudentBorrow(1, 3));
        assertTrue(LibraryService.canStudentBorrow(2, 3));
    }

    @Test(expected = BusinessException.class)
    public void testCanStudentBorrow_ExceedsLimit() throws BusinessException {
        LibraryService.canStudentBorrow(3, 3);
    }

    @Test
    public void testDetermineIssueStatus() {
        Date dueDate = createDate(2026, Calendar.OCTOBER, 10);
        Date beforeDue = createDate(2026, Calendar.OCTOBER, 5);
        Date afterDue = createDate(2026, Calendar.OCTOBER, 15);

        // Already returned on time
        assertEquals("Returned", LibraryService.determineIssueStatus(dueDate, beforeDue, afterDue));

        // Already returned late
        assertEquals("Returned Late", LibraryService.determineIssueStatus(dueDate, afterDue, afterDue));

        // Not yet returned, but not overdue
        assertEquals("Issued", LibraryService.determineIssueStatus(dueDate, null, beforeDue));

        // Not yet returned, and overdue
        assertEquals("Overdue", LibraryService.determineIssueStatus(dueDate, null, afterDue));
    }
}
