package lk.educore.schoolms.model;

import org.junit.After;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Unit tests for BarcodeGenerator.
 * Tests Code128, Code39, EAN13, and custom barcode generation, file existence, and deletion.
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class BarcodeGeneratorTest {

    private static final String TEST_BOOK_ID = "TEST-BOOK-123";
    private static final String TEST_C39_FILE = "test_code39.png";
    private static final String TEST_EAN_FILE = "test_ean13.png";
    private static final String TEST_CUSTOM_FILE = "test_custom.png";

    @After
    public void tearDown() {
        BarcodeGenerator.deleteBarcode(TEST_BOOK_ID + ".png");
        BarcodeGenerator.deleteBarcode(TEST_C39_FILE);
        BarcodeGenerator.deleteBarcode(TEST_EAN_FILE);
        BarcodeGenerator.deleteBarcode(TEST_CUSTOM_FILE);
    }

    @Test
    public void testGenerateBookBarcode() {
        String path = BarcodeGenerator.generateBookBarcode(TEST_BOOK_ID);
        assertNotNull("Generated path should not be null", path);

        File file = new File(path);
        assertTrue("Barcode file should exist on disk", file.exists());
        assertTrue("Barcode file should have positive byte size", file.length() > 0);

        assertTrue("barcodeExists should return true", BarcodeGenerator.barcodeExists(TEST_BOOK_ID + ".png"));

        BarcodeGenerator.deleteBarcode(TEST_BOOK_ID + ".png");
        assertFalse("barcodeExists should return false after deletion", BarcodeGenerator.barcodeExists(TEST_BOOK_ID + ".png"));
    }

    @Test
    public void testGenerateCode39Barcode() {
        String path = BarcodeGenerator.generateCode39Barcode("CODE39TEST", TEST_C39_FILE);
        assertNotNull(path);

        File file = new File(path);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
    }

    @Test
    public void testGenerateEAN13Barcode() {
        // EAN-13 requires 12 or 13 digits
        String path = BarcodeGenerator.generateEAN13Barcode("978013235088", TEST_EAN_FILE);
        assertNotNull(path);

        File file = new File(path);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
    }

    @Test
    public void testGenerateCustomBarcode() {
        String path = BarcodeGenerator.generateCustomBarcode("CUSTOM128", TEST_CUSTOM_FILE, 20.0f, 0.4f);
        assertNotNull(path);

        File file = new File(path);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
    }
}
