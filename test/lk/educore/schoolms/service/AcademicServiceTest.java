package lk.educore.schoolms.service;

import lk.educore.schoolms.exceptions.ValidationException;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Unit tests for AcademicService business logic.
 * Tests grading scales, boundaries, pass rates, honors, and class statistics.
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class AcademicServiceTest {

    @Test
    public void testCalculateGrade_DistinctBands() throws ValidationException {
        // Band A (>= 75%)
        assertEquals("A", AcademicService.calculateGrade(75.0, 100.0));
        assertEquals("A", AcademicService.calculateGrade(88.5, 100.0));
        assertEquals("A", AcademicService.calculateGrade(100.0, 100.0));
        assertEquals("A", AcademicService.calculateGrade(37.5, 50.0)); // 75% of 50

        // Band B (65% to 74.99%)
        assertEquals("B", AcademicService.calculateGrade(65.0, 100.0));
        assertEquals("B", AcademicService.calculateGrade(74.9, 100.0));
        assertEquals("B", AcademicService.calculateGrade(33.0, 50.0)); // 66% of 50

        // Band C (50% to 64.99%)
        assertEquals("C", AcademicService.calculateGrade(50.0, 100.0));
        assertEquals("C", AcademicService.calculateGrade(64.9, 100.0));
        assertEquals("C", AcademicService.calculateGrade(25.0, 50.0)); // 50% of 50

        // Band S (35% to 49.99%)
        assertEquals("S", AcademicService.calculateGrade(35.0, 100.0));
        assertEquals("S", AcademicService.calculateGrade(49.9, 100.0));
        assertEquals("S", AcademicService.calculateGrade(17.5, 50.0)); // 35% of 50

        // Band W (< 35%)
        assertEquals("W", AcademicService.calculateGrade(34.9, 100.0));
        assertEquals("W", AcademicService.calculateGrade(0.0, 100.0));
        assertEquals("W", AcademicService.calculateGrade(10.0, 50.0)); // 20% of 50
    }

    @Test(expected = ValidationException.class)
    public void testCalculateGrade_NegativeMarks() throws ValidationException {
        AcademicService.calculateGrade(-1.0, 100.0);
    }

    @Test(expected = ValidationException.class)
    public void testCalculateGrade_MarksExceedMax() throws ValidationException {
        AcademicService.calculateGrade(105.0, 100.0);
    }

    @Test(expected = ValidationException.class)
    public void testCalculateGrade_ZeroMaxMarks() throws ValidationException {
        AcademicService.calculateGrade(50.0, 0.0);
    }

    @Test(expected = ValidationException.class)
    public void testCalculateGrade_NegativeMaxMarks() throws ValidationException {
        AcademicService.calculateGrade(50.0, -100.0);
    }

    @Test
    public void testCalculatePercentage() throws ValidationException {
        assertEquals(75.0, AcademicService.calculatePercentage(75.0, 100.0), 0.001);
        assertEquals(80.0, AcademicService.calculatePercentage(40.0, 50.0), 0.001);
        assertEquals(0.0, AcademicService.calculatePercentage(0.0, 100.0), 0.001);
        assertEquals(100.0, AcademicService.calculatePercentage(100.0, 100.0), 0.001);
    }

    @Test
    public void testIsPassingGrade() {
        assertTrue(AcademicService.isPassingGrade("A"));
        assertTrue(AcademicService.isPassingGrade("B"));
        assertTrue(AcademicService.isPassingGrade("C"));
        assertTrue(AcademicService.isPassingGrade("S"));
        assertFalse(AcademicService.isPassingGrade("W"));
        assertFalse(AcademicService.isPassingGrade(null));
        assertFalse(AcademicService.isPassingGrade(""));
        assertFalse(AcademicService.isPassingGrade("F"));
    }

    @Test
    public void testCalculatePassRate() {
        List<String> allPassed = Arrays.asList("A", "B", "C", "S");
        assertEquals(100.0, AcademicService.calculatePassRate(allPassed), 0.01);

        List<String> halfFailed = Arrays.asList("A", "W");
        assertEquals(50.0, AcademicService.calculatePassRate(halfFailed), 0.01);

        List<String> allFailed = Arrays.asList("W", "W");
        assertEquals(0.0, AcademicService.calculatePassRate(allFailed), 0.01);

        assertEquals(0.0, AcademicService.calculatePassRate(null), 0.01);
    }

    @Test
    public void testClassStatistics() {
        double[] marks = {50.0, 75.0, 85.0, 90.0, 40.0};
        assertEquals(68.0, AcademicService.calculateClassAverage(marks), 0.01);
        assertEquals(90.0, AcademicService.calculateHighestMark(marks), 0.01);
        assertEquals(40.0, AcademicService.calculateLowestMark(marks), 0.01);

        // Edge cases with empty or null
        assertEquals(0.0, AcademicService.calculateClassAverage(new double[]{}), 0.01);
        assertEquals(0.0, AcademicService.calculateClassAverage(null), 0.01);
    }

    @Test
    public void testIsHonorsEligible() {
        assertTrue(AcademicService.isHonorsEligible(75.0));
        assertTrue(AcademicService.isHonorsEligible(85.5));
        assertTrue(AcademicService.isHonorsEligible(100.0));
        assertFalse(AcademicService.isHonorsEligible(74.9));
        assertFalse(AcademicService.isHonorsEligible(50.0));
    }

    @Test
    public void testCalculateGradeDistribution() {
        List<String> grades = Arrays.asList("A", "A", "B", "C", "S", "W", "A");
        Map<String, Integer> dist = AcademicService.calculateGradeDistribution(grades);

        assertEquals(Integer.valueOf(3), dist.get("A"));
        assertEquals(Integer.valueOf(1), dist.get("B"));
        assertEquals(Integer.valueOf(1), dist.get("C"));
        assertEquals(Integer.valueOf(1), dist.get("S"));
        assertEquals(Integer.valueOf(1), dist.get("W"));
    }
}
