package lk.educore.schoolms.model;

import java.io.*;
import java.sql.*;
import java.util.Properties;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.util.Base64;

/**
 * MySQL Database Connection Manager
 * Handles encrypted database configuration and connection management
 * for the EduCore School Management System
 */
public class MySQL {

    private static final String CONFIG_FILE = "dbinfor.ser";
    private static final String ENCRYPTION_KEY = "EduCoreSchool2025"; // 16 bytes for AES-128
    private static Connection connection;
    private static String dbHost;
    private static String dbPort;
    private static String dbName;
    private static String dbUser;
    private static String dbPassword;
    private static String dumpPath;
    private static String backupPath;

    /**
     * Initialize database configuration from encrypted file
     */
    public static void initConfig() {
        try {
            File configFile = new File(CONFIG_FILE);
            if (configFile.exists()) {
                FileInputStream fis = new FileInputStream(configFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                Properties props = (Properties) ois.readObject();
                ois.close();

                String encryptedData = props.getProperty("dbconfig");
                if (encryptedData != null) {
                    String decryptedData = decrypt(encryptedData);
                    String[] parts = decryptedData.split("\\|");
                    if (parts.length >= 7) {
                        dbHost = parts[0];
                        dbPort = parts[1];
                        dbName = parts[2];
                        dbUser = parts[3];
                        dbPassword = parts[4];
                        dumpPath = parts[5];
                        backupPath = parts[6];
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Save database configuration to encrypted file
     */
    public static void saveConfig(String host, String port, String name, String user, String password, String dump, String backup) {
        try {
            dbHost = host;
            dbPort = port;
            dbName = name;
            dbUser = user;
            dbPassword = password;
            dumpPath = dump;
            backupPath = backup;

            String data = host + "|" + port + "|" + name + "|" + user + "|" + password + "|" + dump + "|" + backup;
            String encryptedData = encrypt(data);

            Properties props = new Properties();
            props.setProperty("dbconfig", encryptedData);

            FileOutputStream fos = new FileOutputStream(CONFIG_FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(props);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Encrypt string using AES
     */
    private static String encrypt(String data) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Decrypt string using AES
     */
    private static String decrypt(String encryptedData) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

    /**
     * Get database connection
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                initConfig();
                String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
                connection = DriverManager.getConnection(url, dbUser, dbPassword);
            } catch (SQLException e) {
                throw new SQLException("Failed to connect to database: " + e.getMessage(), e);
            }
        }
        return connection;
    }

    /**
     * Close database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test database connection
     */
    public static boolean testConnection(String host, String port, String name, String user, String password) {
        try {
            String url = "jdbc:mysql://" + host + ":" + port + "/" + name + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            Connection testConn = DriverManager.getConnection(url, user, password);
            testConn.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Get configuration values
     */
    public static String getDbHost() { return dbHost; }
    public static String getDbPort() { return dbPort; }
    public static String getDbName() { return dbName; }
    public static String getDbUser() { return dbUser; }
    public static String getDumpPath() { return dumpPath; }
    public static String getBackupPath() { return backupPath; }

    /**
     * Check if configuration exists
     */
    public static boolean configExists() {
        return new File(CONFIG_FILE).exists();
    }

    /**
     * Delete configuration file
     */
    public static void deleteConfig() {
        File configFile = new File(CONFIG_FILE);
        if (configFile.exists()) {
            configFile.delete();
        }
    }
}