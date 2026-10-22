package lk.educore.schoolms.service;

import lk.educore.schoolms.exceptions.ValidationException;

import java.util.*;

/**
 * Academic & Examination Business Service
 * Encapsulates grading algorithms, examination statistics,
 * pass/fail rules, and academic performance evaluation for EduCore.
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class AcademicService {

    /**
     * Standard grade scale thresholds (percentage)
     */
    public static final double GRADE_A_MIN = 75.0;
    public static final double GRADE_B_MIN = 65.0;
    public static final double GRADE_C_MIN = 50.0;
    public static final double GRADE_S_MIN = 35.0;

    /**
     * Calculate grade letter based on marks obtained and maximum marks.
     * Scale:
     * - A: >= 75%
     * - B: 65% - 74.99%
     * - C: 50% - 64.99%
     * - S: 35% - 49.99%
     * - W: < 35%
     *
     * @param marks marks obtained
     * @param maxMarks maximum possible marks
     * @return grade letter ("A", "B", "C", "S", "W")
     * @throws ValidationException if marks are negative or maxMarks is <= 0 or marks > maxMarks
     */
    public static String calculateGrade(double marks, double maxMarks) throws ValidationException {
        if (maxMarks <= 0) {
            throw new ValidationException("Maximum marks must be greater than zero", "maxMarks", String.valueOf(maxMarks));
        }
        if (marks < 0) {
            throw new ValidationException("Marks obtained cannot be negative", "marks", String.valueOf(marks));
        }
        if (marks > maxMarks) {
            throw new ValidationException("Marks obtained cannot exceed maximum marks (" + maxMarks + ")", "marks", String.valueOf(marks));
        }

        double percentage = calculatePercentage(marks, maxMarks);
        if (percentage >= GRADE_A_MIN) return "A";
        if (percentage >= GRADE_B_MIN) return "B";
        if (percentage >= GRADE_C_MIN) return "C";
        if (percentage >= GRADE_S_MIN) return "S";
        return "W";
    }

    /**
     * Calculate percentage from marks and maxMarks.
     */
    public static double calculatePercentage(double marks, double maxMarks) throws ValidationException {
        if (maxMarks <= 0) {
            throw new ValidationException("Maximum marks must be greater than zero", "maxMarks", String.valueOf(maxMarks));
        }
        if (marks < 0) {
            throw new ValidationException("Marks cannot be negative", "marks", String.valueOf(marks));
        }
        return (marks / maxMarks) * 100.0;
    }

    /**
     * Check if a grade is a passing grade.
     * A, B, C, S are passing grades; W is a failing grade.
     */
    public static boolean isPassingGrade(String gradeLetter) {
        if (gradeLetter == null) return false;
        String trimmed = gradeLetter.trim().toUpperCase();
        return "A".equals(trimmed) || "B".equals(trimmed) || "C".equals(trimmed) || "S".equals(trimmed);
    }

    /**
     * Calculate pass rate percentage for a class/cohort.
     *
     * @param grades list of grade letters
     * @return pass rate percentage (0.0 - 100.0)
     */
    public static double calculatePassRate(List<String> grades) {
        if (grades == null || grades.isEmpty()) return 0.0;
        int passed = 0;
        int total = 0;
        for (String g : grades) {
            if (g != null && !g.trim().isEmpty()) {
                total++;
                if (isPassingGrade(g)) {
                    passed++;
                }
            }
        }
        if (total == 0) return 0.0;
        double rate = ((double) passed / total) * 100.0;
        return Math.round(rate * 100.0) / 100.0;
    }

    /**
     * Calculate class average mark.
     */
    public static double calculateClassAverage(double[] marks) {
        if (marks == null || marks.length == 0) return 0.0;
        double sum = 0.0;
        for (double m : marks) {
            sum += m;
        }
        double avg = sum / marks.length;
        return Math.round(avg * 100.0) / 100.0;
    }

    /**
     * Find highest mark in class.
     */
    public static double calculateHighestMark(double[] marks) {
        if (marks == null || marks.length == 0) return 0.0;
        double max = marks[0];
        for (double m : marks) {
            if (m > max) max = m;
        }
        return max;
    }

    /**
     * Find lowest mark in class.
     */
    public static double calculateLowestMark(double[] marks) {
        if (marks == null || marks.length == 0) return 0.0;
        double min = marks[0];
        for (double m : marks) {
            if (m < min) min = m;
        }
        return min;
    }

    /**
     * Check if average percentage qualifies student for Academic Honors / Distinction (>= 75.0%).
     */
    public static boolean isHonorsEligible(double averagePercentage) {
        return averagePercentage >= GRADE_A_MIN;
    }

    /**
     * Count grade distribution across a class.
     */
    public static Map<String, Integer> calculateGradeDistribution(List<String> grades) {
        Map<String, Integer> dist = new LinkedHashMap<>();
        dist.put("A", 0);
        dist.put("B", 0);
        dist.put("C", 0);
        dist.put("S", 0);
        dist.put("W", 0);

        if (grades == null) return dist;
        for (String g : grades) {
            if (g != null) {
                String key = g.trim().toUpperCase();
                if (dist.containsKey(key)) {
                    dist.put(key, dist.get(key) + 1);
                }
            }
        }
        return dist;
    }
}
