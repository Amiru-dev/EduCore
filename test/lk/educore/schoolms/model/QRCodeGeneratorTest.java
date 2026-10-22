package lk.educore.schoolms.model;

import org.junit.After;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Unit tests for QRCodeGenerator.
 * Tests Student QR, Teacher QR, custom QR generation, existence check, and deletion.
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class QRCodeGeneratorTest {

    private static final String TEST_STUDENT_ID = "STU-TEST-99";
    private static final String TEST_TEACHER_NIC = "198099887766";
    private static final String TEST_CUSTOM_FILE = "test_custom_qr.png";

    @After
    public void tearDown() {
        QRCodeGenerator.deleteQR(TEST_STUDENT_ID + ".png");
        QRCodeGenerator.deleteQR(TEST_TEACHER_NIC + ".png");
        QRCodeGenerator.deleteQR(TEST_CUSTOM_FILE);
    }

    @Test
    public void testGenerateStudentQR() {
        String path = QRCodeGenerator.generateStudentQR(TEST_STUDENT_ID, "Test Student");
        assertNotNull("Generated path should not be null", path);

        File file = new File(path);
        assertTrue("QR code file should exist on disk", file.exists());
        assertTrue("QR code file should have content", file.length() > 0);

        assertTrue("qrExists should return true", QRCodeGenerator.qrExists(TEST_STUDENT_ID + ".png"));

        QRCodeGenerator.deleteQR(TEST_STUDENT_ID + ".png");
        assertFalse("qrExists should return false after deletion", QRCodeGenerator.qrExists(TEST_STUDENT_ID + ".png"));
    }

    @Test
    public void testGenerateTeacherQR() {
        String path = QRCodeGenerator.generateTeacherQR(TEST_TEACHER_NIC, "Test Teacher");
        assertNotNull(path);

        File file = new File(path);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
    }

    @Test
    public void testGenerateCustomQR() {
        String path = QRCodeGenerator.generateCustomQR("EduCore QR Content", TEST_CUSTOM_FILE);
        assertNotNull(path);

        File file = new File(path);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
    }
}
