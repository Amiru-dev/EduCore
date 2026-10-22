package lk.educore.schoolms.model;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;

import static org.junit.Assert.*;

/**
 * Unit tests for MySQL database configuration and manager.
 * Tests configuration serialization, AES encryption/decryption round-trip,
 * getters, connection verification, and cleanup.
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class MySQLTest {

    private static File originalConfig = new File("dbinfor.ser");
    private static File backupConfig = new File("dbinfor.ser.backup");
    private static boolean hadOriginalConfig = false;

    @BeforeClass
    public static void setUpClass() throws Exception {
        if (originalConfig.exists()) {
            hadOriginalConfig = true;
            Files.copy(originalConfig.toPath(), backupConfig.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        if (hadOriginalConfig && backupConfig.exists()) {
            Files.copy(backupConfig.toPath(), originalConfig.toPath(), StandardCopyOption.REPLACE_EXISTING);
            backupConfig.delete();
            MySQL.initConfig(); // reload original
        } else {
            MySQL.deleteConfig();
            if (backupConfig.exists()) backupConfig.delete();
        }
    }

    @Test
    public void testSaveAndInitConfig_EncryptionRoundTrip() {
        String testHost = "127.0.0.1";
        String testPort = "3306";
        String testName = "school_test_db";
        String testUser = "test_user";
        String testPass = "secret_pass_123";
        String testDump = "/usr/bin/mysqldump";
        String testBackup = "/backups";

        // Save configuration (encrypts internally with AES-128)
        MySQL.saveConfig(testHost, testPort, testName, testUser, testPass, testDump, testBackup);
        assertTrue("Config file must exist after saveConfig", MySQL.configExists());

        // Re-initialize from encrypted file (decrypts internally)
        MySQL.initConfig();

        // Verify decrypted properties match exactly
        assertEquals(testHost, MySQL.getDbHost());
        assertEquals(testPort, MySQL.getDbPort());
        assertEquals(testName, MySQL.getDbName());
        assertEquals(testUser, MySQL.getDbUser());
        assertEquals(testPass, MySQL.getDbPassword());
        assertEquals(testDump, MySQL.getDumpPath());
        assertEquals(testBackup, MySQL.getBackupPath());
    }

    @Test
    public void testDeleteConfig() {
        MySQL.saveConfig("host", "3306", "db", "u", "p", "d", "b");
        assertTrue(MySQL.configExists());

        MySQL.deleteConfig();
        assertFalse(MySQL.configExists());
    }

    @Test
    public void testTestConnection_InvalidHostReturnsFalse() {
        // Unreachable fake database should return false without crashing
        boolean result = MySQL.testConnection("192.0.2.1", "3306", "nonexistent_db", "wrong_user", "wrong_pass");
        assertFalse("Unreachable database connection test should return false", result);
    }

    @Test
    public void testCloseConnection_SafeWhenClosedOrNull() {
        // Calling closeConnection multiple times should be safe and idempotent
        MySQL.closeConnection();
        MySQL.closeConnection();
    }
}
