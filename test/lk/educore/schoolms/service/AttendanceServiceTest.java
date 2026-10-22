package lk.educore.schoolms.service;

import lk.educore.schoolms.exceptions.BusinessException;
import lk.educore.schoolms.exceptions.ValidationException;
import org.junit.Test;

import java.time.LocalTime;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Unit tests for AttendanceService business logic.
 * Tests attendance percentage, exam eligibility, QR formatting and parsing, and check-in statuses.
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class AttendanceServiceTest {

    @Test
    public void testCalculateAttendancePercentage() throws ValidationException {
        // 180 out of 200 days = 90.0%
        assertEquals(90.0, AttendanceService.calculateAttendancePercentage(180, 200), 0.01);

        // 150 out of 200 days = 75.0%
        assertEquals(75.0, AttendanceService.calculateAttendancePercentage(150, 200), 0.01);

        // 0 days attended
        assertEquals(0.0, AttendanceService.calculateAttendancePercentage(0, 200), 0.01);

        // 0 total school days
        assertEquals(0.0, AttendanceService.calculateAttendancePercentage(0, 0), 0.01);
    }

    @Test(expected = ValidationException.class)
    public void testCalculateAttendancePercentage_NegativePresent() throws ValidationException {
        AttendanceService.calculateAttendancePercentage(-5, 100);
    }

    @Test(expected = ValidationException.class)
    public void testCalculateAttendancePercentage_PresentExceedsTotal() throws ValidationException {
        AttendanceService.calculateAttendancePercentage(105, 100);
    }

    @Test
    public void testIsEligibleForExams() throws ValidationException {
        // Exactly at threshold (75%)
        assertTrue(AttendanceService.isEligibleForExams(75, 100, 75.0));

        // Above threshold (85%)
        assertTrue(AttendanceService.isEligibleForExams(85, 100, 75.0));

        // Below threshold (74.5%)
        assertFalse(AttendanceService.isEligibleForExams(149, 200, 75.0)); // 74.5%
    }

    @Test
    public void testFormatAndParseStudentQR() throws ValidationException, BusinessException {
        String studentId = "STU-0042";
        String studentName = "Amiru Upek";

        String formatted = AttendanceService.formatStudentQR(studentId, studentName);
        assertEquals("StudentID:STU-0042|Name:Amiru Upek", formatted);

        Map<String, String> parsed = AttendanceService.parseStudentQR(formatted);
        assertEquals("STU-0042", parsed.get("studentId"));
        assertEquals("Amiru Upek", parsed.get("studentName"));
    }

    @Test
    public void testFormatAndParseTeacherQR() throws ValidationException, BusinessException {
        String nic = "198501234567";
        String name = "Kamal Perera";

        String formatted = AttendanceService.formatTeacherQR(nic, name);
        assertEquals("TeacherNIC:198501234567|Name:Kamal Perera", formatted);

        Map<String, String> parsed = AttendanceService.parseTeacherQR(formatted);
        assertEquals("198501234567", parsed.get("teacherNIC"));
        assertEquals("Kamal Perera", parsed.get("teacherName"));
    }

    @Test(expected = BusinessException.class)
    public void testParseStudentQR_Malformed() throws BusinessException {
        AttendanceService.parseStudentQR("RandomCorruptedString");
    }

    @Test(expected = BusinessException.class)
    public void testParseTeacherQR_Malformed() throws BusinessException {
        AttendanceService.parseTeacherQR("RandomCorruptedString");
    }

    @Test
    public void testDetermineAttendanceStatus() {
        // Before or at 7:30 AM -> Present
        assertEquals("Present", AttendanceService.determineAttendanceStatus(LocalTime.of(7, 15)));
        assertEquals("Present", AttendanceService.determineAttendanceStatus(LocalTime.of(7, 30)));

        // Between 7:31 AM and 8:00 AM -> Late
        assertEquals("Late", AttendanceService.determineAttendanceStatus(LocalTime.of(7, 31)));
        assertEquals("Late", AttendanceService.determineAttendanceStatus(LocalTime.of(8, 0)));

        // After 8:00 AM -> Very Late
        assertEquals("Very Late", AttendanceService.determineAttendanceStatus(LocalTime.of(8, 1)));
        assertEquals("Very Late", AttendanceService.determineAttendanceStatus(LocalTime.of(9, 30)));

        // Null -> Absent
        assertEquals("Absent", AttendanceService.determineAttendanceStatus(null));
    }
}
