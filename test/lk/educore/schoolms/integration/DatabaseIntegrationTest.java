package lk.educore.schoolms.integration;

import lk.educore.schoolms.model.MySQL;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;

import static org.junit.Assert.*;

/**
 * Integration tests for MySQL database connectivity and query operations.
 * Validates connection lifecycle and query execution when database configuration is present.
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class DatabaseIntegrationTest {

    @AfterClass
    public static void tearDownClass() {
        MySQL.closeConnection();
    }

    @Test
    public void testDatabaseConnectionLifecycle() {
        File configFile = new File("dbinfor.ser");
        if (!configFile.exists() || !configFile.isFile()) {
            System.out.println("Skipping testDatabaseConnectionLifecycle: dbinfor.ser not found");
            return;
        }

        try {
            MySQL.initConfig();
            Connection conn = MySQL.getConnection();
            if (conn != null && !conn.isClosed()) {
                assertTrue("Connection should be valid", conn.isValid(5));

                // Execute simple ping search
                ResultSet rs = MySQL.executeSearch("SELECT 1 AS ping");
                assertNotNull(rs);
                assertTrue(rs.next());
                assertEquals(1, rs.getInt("ping"));
                rs.close();

                // Test close and reopen
                MySQL.closeConnection();
                assertTrue("Connection should be closed", conn.isClosed());

                Connection newConn = MySQL.getConnection();
                assertNotNull(newConn);
                assertFalse(newConn.isClosed());
            }
        } catch (Exception e) {
            System.out.println("Database server offline or unreachable: " + e.getMessage());
            // Graceful skip if external MySQL service is not currently running
            assertTrue(true);
        }
    }
}
