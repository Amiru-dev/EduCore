package lk.educore.schoolms.model;

import lk.educore.schoolms.exceptions.BusinessException;
import lk.educore.schoolms.exceptions.ValidationException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for Teacher domain model.
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class TeacherTest {

    @Test
    public void testTeacherProperties() {
        Teacher teacher = new Teacher("198512345678", "Nimal", "Silva", "nimal@example.com",
                "0771234567", "Permanent", 60000.0, 15000.0, 5000.0);

        assertEquals("198512345678", teacher.getNic());
        assertEquals("Nimal Silva", teacher.getFullName());
        assertEquals("Permanent", teacher.getTeacherType());
        assertEquals(60000.0, teacher.getBasicSalary(), 0.001);
        assertEquals(15000.0, teacher.getAllowance(), 0.001);
        assertEquals(5000.0, teacher.getBonus(), 0.001);
        assertEquals("Active", teacher.getStatus());
    }

    @Test
    public void testCalculateGrossAndNetSalary() throws ValidationException, BusinessException {
        Teacher teacher = new Teacher("198512345678", "Nimal", "Silva", "nimal@example.com",
                "0771234567", "Permanent", 50000.0, 10000.0, 5000.0);

        // Gross = 50000 + 10000 + 5000 = 65000
        assertEquals(65000.0, teacher.calculateGrossSalary(), 0.01);

        // EPF @ 8% of basic (50000 * 0.08 = 4000)
        // Net = 65000 - 4000 = 61000
        assertEquals(61000.0, teacher.calculateNetSalary(0.08), 0.01);
    }

    @Test
    public void testValidation_Success() throws ValidationException {
        Teacher teacher = new Teacher("198512345678", "Nimal", "Silva", "nimal@example.com",
                "0771234567", "Permanent", 50000.0, 10000.0, 5000.0);

        assertTrue(teacher.validate());
    }

    @Test(expected = ValidationException.class)
    public void testValidation_InvalidNIC() throws ValidationException {
        Teacher teacher = new Teacher("123", "Nimal", "Silva", "nimal@example.com",
                "0771234567", "Permanent", 50000.0, 10000.0, 5000.0);

        teacher.validate();
    }

    @Test(expected = ValidationException.class)
    public void testValidation_NegativeBasicSalary() throws ValidationException {
        Teacher teacher = new Teacher("198512345678", "Nimal", "Silva", "nimal@example.com",
                "0771234567", "Permanent", -5000.0, 10000.0, 5000.0);

        teacher.validate();
    }

    @Test
    public void testEqualsAndHashCode() {
        Teacher t1 = new Teacher("198512345678", "A", "B", null, null, null, 0, 0, 0);
        Teacher t2 = new Teacher("198512345678", "C", "D", null, null, null, 0, 0, 0);
        Teacher t3 = new Teacher("199012345678", "A", "B", null, null, null, 0, 0, 0);

        assertEquals(t1, t2);
        assertFalse(t1.equals(t3));
        assertEquals(t1.hashCode(), t2.hashCode());
    }
}
