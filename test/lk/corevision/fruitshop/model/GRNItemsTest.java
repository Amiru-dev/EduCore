package lk.corevision.fruitshop.model;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for GRNItems model class. Tests cover all getter and setter
 * methods for GRN item properties.
 *
 * @author CoreVision Software Team
 * @version 1.0.0
 * @see GRNItems
 */
public class GRNItemsTest {

    public GRNItemsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getGrnItemId and setGrnItemId methods
     */
    @Test
    public void testGrnItemId() {
        GRNItems item = new GRNItems();
        String testId = "GRN-001";
        item.setGrnItemId(testId);
        assertEquals(testId, item.getGrnItemId());
    }

    /**
     * Test of getGrnId and setGrnId methods
     */
    @Test
    public void testGrnId() {
        GRNItems item = new GRNItems();
        String testGrnId = "GRN-REF-001";
        item.setGrnId(testGrnId);
        assertEquals(testGrnId, item.getGrnId());
    }

    /**
     * Test of getQty and setQty methods
     */
    @Test
    public void testQty() {
        GRNItems item = new GRNItems();
        String testQty = "100";
        item.setQty(testQty);
        assertEquals(testQty, item.getQty());
    }

    /**
     * Test of getUnitPrice and setUnitPrice methods
     */
    @Test
    public void testUnitPrice() {
        GRNItems item = new GRNItems();
        String testPrice = "50.00";
        item.setUnitPrice(testPrice);
        assertEquals(testPrice, item.getUnitPrice());
    }

    /**
     * Test of getRawmaterialName and setRawmaterialName methods
     */
    @Test
    public void testRawmaterialName() {
        GRNItems item = new GRNItems();
        String testName = "Apple";
        item.setRawmaterialName(testName);
        assertEquals(testName, item.getRawmaterialName());
    }
}
