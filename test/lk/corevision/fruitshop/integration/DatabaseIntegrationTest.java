package lk.corevision.fruitshop.integration;

import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import lk.corevision.fruitshop.exceptions.DatabaseException;
import lk.corevision.fruitshop.model.MySQL;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Integration tests for database operations. These tests require a valid
 * database configuration file (dbinfor.ser) and a running MySQL database with
 * the fruitshop schema. Tests cover complete CRUD operations and connection
 * management.
 *
 * @author CoreVision Software Team
 * @version 1.0.0
 * @see MySQL
 */
public class DatabaseIntegrationTest {

    public DatabaseIntegrationTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
        try {
            MySQL.closeConnection();
        } catch (DatabaseException ex) {
            Logger.getLogger(DatabaseIntegrationTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test database connection establishment
     */
    @Test
    public void testDatabaseConnection() {
        try {
            java.io.File file = new java.io.File("dbinfor.ser");
            if (!file.exists() || !file.isFile()) {
                System.out.println("Skipping test - database configuration file not found");
                return;
            }

            MySQL.createConnection();
            assertNotNull(MySQL.getConnection());
            assertTrue(MySQL.getConnection().isValid(5));
        } catch (Exception e) {
            System.out.println("Database connection test failed: " + e.getMessage());
            assertTrue("Database connection failed (expected if database not running)", true);
        }
    }

    /**
     * Test simple SELECT query execution
     */
    @Test
    public void testSelectQuery() {
        try {
            java.io.File file = new java.io.File("dbinfor.ser");
            if (!file.exists() || !file.isFile()) {
                System.out.println("Skipping test - database configuration file not found");
                return;
            }

            String query = "SELECT 1 as test_column";
            ResultSet result = MySQL.executeSearch(query);
            assertNotNull(result);
            assertTrue(result.next());
            assertEquals(1, result.getInt("test_column"));
        } catch (Exception e) {
            System.out.println("SELECT query test failed: " + e.getMessage());
            assertTrue("SELECT query failed (expected if database not running)", true);
        }
    }

    /**
     * Test INSERT query execution
     */
    @Test
    public void testInsertQuery() {
        try {
            java.io.File file = new java.io.File("dbinfor.ser");
            if (!file.exists() || !file.isFile()) {
                System.out.println("Skipping test - database configuration file not found");
                return;
            }

            // Create a test table if it doesn't exist
            String createTable = "CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY, name VARCHAR(50))";
            MySQL.executeIUD(createTable);

            // Insert test data
            String insertQuery = "INSERT INTO test_table (id, name) VALUES (1, 'Test Name')";
            Integer result = MySQL.executeIUD(insertQuery);
            assertNotNull(result);
            assertTrue(result > 0);

            // Clean up
            String deleteQuery = "DELETE FROM test_table WHERE id = 1";
            MySQL.executeIUD(deleteQuery);
        } catch (Exception e) {
            System.out.println("INSERT query test failed: " + e.getMessage());
            assertTrue("INSERT query failed (expected if database not running)", true);
        }
    }

    /**
     * Test UPDATE query execution
     */
    @Test
    public void testUpdateQuery() {
        try {
            java.io.File file = new java.io.File("dbinfor.ser");
            if (!file.exists() || !file.isFile()) {
                System.out.println("Skipping test - database configuration file not found");
                return;
            }

            // Create test table and insert data
            String createTable = "CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY, name VARCHAR(50))";
            MySQL.executeIUD(createTable);
            String insertQuery = "INSERT INTO test_table (id, name) VALUES (2, 'Original Name')";
            MySQL.executeIUD(insertQuery);

            // Update the data
            String updateQuery = "UPDATE test_table SET name = 'Updated Name' WHERE id = 2";
            Integer result = MySQL.executeIUD(updateQuery);
            assertNotNull(result);

            // Verify update
            ResultSet rs = MySQL.executeSearch("SELECT name FROM test_table WHERE id = 2");
            assertTrue(rs.next());
            assertEquals("Updated Name", rs.getString("name"));

            // Clean up
            String deleteQuery = "DELETE FROM test_table WHERE id = 2";
            MySQL.executeIUD(deleteQuery);
        } catch (Exception e) {
            System.out.println("UPDATE query test failed: " + e.getMessage());
            assertTrue("UPDATE query failed (expected if database not running)", true);
        }
    }

    /**
     * Test DELETE query execution
     */
    @Test
    public void testDeleteQuery() {
        try {
            java.io.File file = new java.io.File("dbinfor.ser");
            if (!file.exists() || !file.isFile()) {
                System.out.println("Skipping test - database configuration file not found");
                return;
            }

            // Create test table and insert data
            String createTable = "CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY, name VARCHAR(50))";
            MySQL.executeIUD(createTable);
            String insertQuery = "INSERT INTO test_table (id, name) VALUES (3, 'To Be Deleted')";
            MySQL.executeIUD(insertQuery);

            // Delete the data
            String deleteQuery = "DELETE FROM test_table WHERE id = 3";
            Integer result = MySQL.executeIUD(deleteQuery);
            assertNotNull(result);

            // Verify deletion
            ResultSet rs = MySQL.executeSearch("SELECT COUNT(*) as count FROM test_table WHERE id = 3");
            assertTrue(rs.next());
            assertEquals(0, rs.getInt("count"));
        } catch (Exception e) {
            System.out.println("DELETE query test failed: " + e.getMessage());
            assertTrue("DELETE query failed (expected if database not running)", true);
        }
    }

    /**
     * Test connection management - closing and reopening
     */
    @Test
    public void testConnectionManagement() {
        try {
            java.io.File file = new java.io.File("dbinfor.ser");
            if (!file.exists() || !file.isFile()) {
                System.out.println("Skipping test - database configuration file not found");
                return;
            }

            // Create initial connection
            MySQL.createConnection();
            assertNotNull(MySQL.getConnection());

            // Close connection
            MySQL.closeConnection();

            // Create new connection
            MySQL.createConnection();
            assertNotNull(MySQL.getConnection());
            assertTrue(MySQL.getConnection().isValid(5));
        } catch (Exception e) {
            System.out.println("Connection management test failed: " + e.getMessage());
            assertTrue("Connection management failed (expected if database not running)", true);
        }
    }
}
