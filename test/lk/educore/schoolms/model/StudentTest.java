package lk.educore.schoolms.model;

import lk.educore.schoolms.exceptions.ValidationException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for Student domain model.
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class StudentTest {

    @Test
    public void testStudentPropertiesAndFullName() {
        Student student = new Student("STU-0001", "Amiru", "Upek", "amiru@example.com",
                "0715916293", "200403110370", "2004-01-15", "Grade 12", "Colombo");

        assertEquals("STU-0001", student.getStudentId());
        assertEquals("Amiru", student.getFirstName());
        assertEquals("Upek", student.getLastName());
        assertEquals("Amiru Upek", student.getFullName());
        assertEquals("Grade 12", student.getGrade());
        assertEquals("Active", student.getStatus());
    }

    @Test
    public void testCalculateAge() {
        Student student = new Student();
        student.setDateOfBirth("2010-05-20");
        assertTrue(student.calculateAge() > 0);

        student.setDateOfBirth(null);
        assertEquals(0, student.calculateAge());
    }

    @Test
    public void testValidation_Success() throws ValidationException {
        Student student = new Student("STU-0001", "Amiru", "Upek", "amiru@example.com",
                "0715916293", "200403110370", "2004-01-15", "Grade 12", "Colombo");

        assertTrue(student.validate());
    }

    @Test(expected = ValidationException.class)
    public void testValidation_InvalidStudentID() throws ValidationException {
        Student student = new Student("INVALID-ID", "Amiru", "Upek", "amiru@example.com",
                "0715916293", "200403110370", "2004-01-15", "Grade 12", "Colombo");

        student.validate();
    }

    @Test(expected = ValidationException.class)
    public void testValidation_InvalidEmail() throws ValidationException {
        Student student = new Student("STU-0001", "Amiru", "Upek", "not-an-email",
                "0715916293", "200403110370", "2004-01-15", "Grade 12", "Colombo");

        student.validate();
    }

    @Test
    public void testEqualsAndHashCode() {
        Student s1 = new Student("STU-0001", "Amiru", "Upek", "a@b.com", "0715916293", null, null, "Grade 10", null);
        Student s2 = new Student("STU-0001", "Amiru", "Upek", "a@b.com", "0715916293", null, null, "Grade 10", null);
        Student s3 = new Student("STU-0002", "Sunil", "Perera", "s@b.com", "0715916294", null, null, "Grade 10", null);

        assertEquals(s1, s2);
        assertFalse(s1.equals(s3));
        assertEquals(s1.hashCode(), s2.hashCode());
    }
}
