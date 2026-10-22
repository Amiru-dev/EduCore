package lk.educore.schoolms.model;

import lk.educore.schoolms.exceptions.BusinessException;
import lk.educore.schoolms.exceptions.ValidationException;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Unit tests for Book and BookIssue domain models.
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class BookAndIssueTest {

    private Date createDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Test
    public void testBookInventoryOperations() throws Exception {
        Book book = new Book(1, "Clean Code", "Robert C. Martin", "978-0132350884", "Software", 3);

        assertEquals(1, book.getBookId());
        assertEquals("Clean Code", book.getTitle());
        assertTrue(book.isAvailable());
        assertEquals(3, book.getAvailableCopies());

        // Borrow copy 1
        book.borrowCopy();
        assertEquals(2, book.getAvailableCopies());

        // Borrow copy 2 & 3
        book.borrowCopy();
        book.borrowCopy();
        assertEquals(0, book.getAvailableCopies());
        assertFalse(book.isAvailable());

        // Return copy
        book.returnCopy();
        assertEquals(1, book.getAvailableCopies());
        assertTrue(book.isAvailable());
    }

    @Test(expected = BusinessException.class)
    public void testBookBorrow_ExhaustedCopies() throws Exception {
        Book book = new Book(1, "Clean Code", "Robert C. Martin", "978-0132350884", "Software", 1);
        book.borrowCopy(); // 0 available
        book.borrowCopy(); // throws
    }

    @Test(expected = BusinessException.class)
    public void testBookReturn_ExceedsTotalCopies() throws Exception {
        Book book = new Book(1, "Clean Code", "Robert C. Martin", "978-0132350884", "Software", 2);
        book.returnCopy(); // already 2/2 -> throws
    }

    @Test
    public void testBookIssueLifecycle() throws ValidationException {
        Date issueDate = createDate(2026, Calendar.AUGUST, 1);
        BookIssue issue = new BookIssue(101, 1, "STU-0001", issueDate, 14);

        assertEquals(101, issue.getIssueId());
        assertEquals("STU-0001", issue.getStudentId());
        assertEquals("Issued", issue.getStatus());

        // Check before due date (Aug 15 is due date)
        Date checkAug10 = createDate(2026, Calendar.AUGUST, 10);
        assertFalse(issue.isCurrentlyOverdue(checkAug10));

        // Return 5 days late (Aug 20) @ 10 LKR/day = 50 LKR fine
        Date returnDate = createDate(2026, Calendar.AUGUST, 20);
        issue.markReturned(returnDate, 10.0);

        assertEquals("Returned Late", issue.getStatus());
        assertEquals(50.0, issue.getFineAmount(), 0.01);
    }
}
