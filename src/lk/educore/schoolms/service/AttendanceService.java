package lk.educore.schoolms.service;

import lk.educore.schoolms.exceptions.BusinessException;
import lk.educore.schoolms.exceptions.ValidationException;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Attendance Business Service Encapsulates attendance percentage calculations,
 * exam eligibility rules, QR code payload serialization/parsing, and check-in
 * status determination.
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class AttendanceService {

    /**
     * Standard minimum attendance required to sit for examinations (75%)
     */
    public static final double MIN_ATTENDANCE_EXAM_THRESHOLD = 75.0;

    /**
     * School starting time (e.g., 07:30 AM)
     */
    public static final LocalTime SCHOOL_START_TIME = LocalTime.of(7, 30);

    /**
     * Late arrival cutoff time (e.g., 08:00 AM)
     */
    public static final LocalTime LATE_CUTOFF_TIME = LocalTime.of(8, 0);

    /**
     * Calculate student/teacher attendance percentage.
     *
     * @param presentDays number of days attended
     * @param totalDays total school working days
     * @return attendance percentage (0.0 - 100.0)
     * @throws ValidationException if days are negative or presentDays >
     * totalDays
     */
    public static double calculateAttendancePercentage(int presentDays, int totalDays) throws ValidationException {
        if (presentDays < 0) {
            throw new ValidationException("Present days cannot be negative", "presentDays", String.valueOf(presentDays));
        }
        if (totalDays < 0) {
            throw new ValidationException("Total days cannot be negative", "totalDays", String.valueOf(totalDays));
        }
        if (presentDays > totalDays) {
            throw new ValidationException("Present days cannot exceed total days", "presentDays", String.valueOf(presentDays));
        }
        if (totalDays == 0) {
            return 0.0;
        }
        double percentage = ((double) presentDays / totalDays) * 100.0;
        return Math.round(percentage * 100.0) / 100.0;
    }

    /**
     * Check whether student meets the minimum attendance threshold for exam
     * eligibility.
     *
     * @param presentDays days attended
     * @param totalDays total working days
     * @param requiredThreshold minimum required percentage (e.g. 75.0)
     * @return true if eligible
     */
    public static boolean isEligibleForExams(int presentDays, int totalDays, double requiredThreshold)
            throws ValidationException {
        if (requiredThreshold < 0 || requiredThreshold > 100.0) {
            throw new ValidationException("Threshold must be between 0.0 and 100.0", "requiredThreshold", String.valueOf(requiredThreshold));
        }
        double attendance = calculateAttendancePercentage(presentDays, totalDays);
        return attendance >= requiredThreshold;
    }

    /**
     * Format payload for student QR code.
     */
    public static String formatStudentQR(String studentId, String studentName) throws ValidationException {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new ValidationException("Student ID is required", "studentId", null);
        }
        if (studentName == null || studentName.trim().isEmpty()) {
            throw new ValidationException("Student name is required", "studentName", null);
        }
        return "StudentID:" + studentId.trim() + "|Name:" + studentName.trim();
    }

    /**
     * Format payload for teacher QR code.
     */
    public static String formatTeacherQR(String teacherNIC, String teacherName) throws ValidationException {
        if (teacherNIC == null || teacherNIC.trim().isEmpty()) {
            throw new ValidationException("Teacher NIC is required", "teacherNIC", null);
        }
        if (teacherName == null || teacherName.trim().isEmpty()) {
            throw new ValidationException("Teacher name is required", "teacherName", null);
        }
        return "TeacherNIC:" + teacherNIC.trim() + "|Name:" + teacherName.trim();
    }

    /**
     * Parse student QR code payload. Expected format:
     * "StudentID:<id>|Name:<name>"
     *
     * @param qrContent raw scanned QR content
     * @return Map with "studentId" and "studentName"
     * @throws BusinessException if format is invalid
     */
    public static Map<String, String> parseStudentQR(String qrContent) throws BusinessException {
        if (qrContent == null || !qrContent.contains("StudentID:") || !qrContent.contains("|Name:")) {
            throw new BusinessException("Invalid student QR code payload", BusinessException.ErrorType.BUSINESS_RULE_VIOLATION);
        }

        try {
            String[] parts = qrContent.split("\\|Name:");
            if (parts.length != 2) {
                throw new BusinessException("Malformed student QR code structure", BusinessException.ErrorType.BUSINESS_RULE_VIOLATION);
            }
            String idPart = parts[0].replace("StudentID:", "").trim();
            String namePart = parts[1].trim();

            if (idPart.isEmpty() || namePart.isEmpty()) {
                throw new BusinessException("Student QR contains empty ID or name", BusinessException.ErrorType.BUSINESS_RULE_VIOLATION);
            }

            Map<String, String> result = new HashMap<>();
            result.put("studentId", idPart);
            result.put("studentName", namePart);
            return result;
        } catch (Exception e) {
            throw new BusinessException("Failed to parse student QR: " + e.getMessage(), BusinessException.ErrorType.INVALID_OPERATION, "QR");
        }
    }

    /**
     * Parse teacher QR code payload. Expected format:
     * "TeacherNIC:<nic>|Name:<name>"
     *
     * @param qrContent raw scanned QR content
     * @return Map with "teacherNIC" and "teacherName"
     * @throws BusinessException if format is invalid
     */
    public static Map<String, String> parseTeacherQR(String qrContent) throws BusinessException {
        if (qrContent == null || !qrContent.contains("TeacherNIC:") || !qrContent.contains("|Name:")) {
            throw new BusinessException("Invalid teacher QR code payload", BusinessException.ErrorType.BUSINESS_RULE_VIOLATION);
        }

        try {
            String[] parts = qrContent.split("\\|Name:");
            if (parts.length != 2) {
                throw new BusinessException("Malformed teacher QR code structure", BusinessException.ErrorType.BUSINESS_RULE_VIOLATION);
            }
            String nicPart = parts[0].replace("TeacherNIC:", "").trim();
            String namePart = parts[1].trim();

            if (nicPart.isEmpty() || namePart.isEmpty()) {
                throw new BusinessException("Teacher QR contains empty NIC or name", BusinessException.ErrorType.BUSINESS_RULE_VIOLATION);
            }

            Map<String, String> result = new HashMap<>();
            result.put("teacherNIC", nicPart);
            result.put("teacherName", namePart);
            return result;
        } catch (Exception e) {
            throw new BusinessException("Failed to parse teacher QR: " + e.getMessage(), BusinessException.ErrorType.INVALID_OPERATION, "QR");
        }
    }

    /**
     * Determine check-in status based on scan time.
     *
     * @param checkInTime time student or teacher checked in
     * @return "Present" if on or before school start time, "Late" if between
     * start and late cutoff, "Very Late" afterwards.
     */
    public static String determineAttendanceStatus(LocalTime checkInTime) {
        if (checkInTime == null) {
            return "Absent";
        }
        if (!checkInTime.isAfter(SCHOOL_START_TIME)) {
            return "Present";
        }
        if (!checkInTime.isAfter(LATE_CUTOFF_TIME)) {
            return "Late";
        }
        return "Very Late";
    }
}
