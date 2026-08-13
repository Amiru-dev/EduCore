package lk.corevision.fruitshop.model;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for OrderItems model class. Tests cover all getter and setter
 * methods for order item properties.
 *
 * @author CoreVision Software Team
 * @version 1.0.0
 * @see OrderItems
 */
public class OrderItemsTest {

    public OrderItemsTest() {
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
     * Test of getFruitId and setFruitId methods
     */
    @Test
    public void testFruitId() {
        OrderItems item = new OrderItems();
        String testId = "FRUIT-001";
        item.setFruitId(testId);
        assertEquals(testId, item.getFruitId());
    }

    /**
     * Test of getFruitName and setFruitName methods
     */
    @Test
    public void testFruitName() {
        OrderItems item = new OrderItems();
        String testName = "Apple";
        item.setFruitName(testName);
        assertEquals(testName, item.getFruitName());
    }

    /**
     * Test of getPackingId and setPackingId methods
     */
    @Test
    public void testPackingId() {
        OrderItems item = new OrderItems();
        String testPackingId = "PACK-001";
        item.setPackingId(testPackingId);
        assertEquals(testPackingId, item.getPackingId());
    }

    /**
     * Test of getMfd and setMfd methods
     */
    @Test
    public void testMfd() {
        OrderItems item = new OrderItems();
        String testMfd = "2024-01-15";
        item.setMfd(testMfd);
        assertEquals(testMfd, item.getMfd());
    }

    /**
     * Test of getSellingPrice and setSellingPrice methods
     */
    @Test
    public void testSellingPrice() {
        OrderItems item = new OrderItems();
        String testPrice = "100.00";
        item.setSellingPrice(testPrice);
        assertEquals(testPrice, item.getSellingPrice());
    }

    /**
     * Test of getDiscountPrice and setDiscountPrice methods
     */
    @Test
    public void testDiscountPrice() {
        OrderItems item = new OrderItems();
        String testDiscount = "80.00";
        item.setDiscountPrice(testDiscount);
        assertEquals(testDiscount, item.getDiscountPrice());
    }

    /**
     * Test of getQty and setQty methods
     */
    @Test
    public void testQty() {
        OrderItems item = new OrderItems();
        String testQty = "50";
        item.setQty(testQty);
        assertEquals(testQty, item.getQty());
    }
}
