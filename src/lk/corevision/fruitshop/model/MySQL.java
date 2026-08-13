package lk.corevision.fruitshop.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.Key;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;
import lk.corevision.fruitshop.exceptions.DatabaseException;
import lk.corevision.fruitshop.gui.SetupConnection;

/**
 * Database connection and utility class for MySQL operations. Provides methods
 * for connection management, query execution, and result processing.
 *
 * @author CoreVision Software Team
 * @version 1.0.0
 */
public class MySQL implements Serializable {

    public String ip;
    public String port;
    public String pw;
    public String un;
    public String dbname;

    public String dump;
    public String path;

    private static final String ALGORITHM = "AES";
    private static final byte[] KEY = "1234567890123456".getBytes();
    private static Connection connection;

    /**
     * Creates a database connection using encrypted configuration file. If
     * configuration file doesn't exist, prompts user for database credentials.
     *
     * @throws DatabaseException if connection fails due to authentication,
     * network issues, or missing configuration
     */
    public static void createConnection() throws DatabaseException {
        if (connection == null) {
            try {
                File file = new File("dbinfor.ser");
                if (file.exists() && file.isFile()) {
                    Key secretKey = new SecretKeySpec(KEY, ALGORITHM);
                    Cipher cipher = Cipher.getInstance(ALGORITHM);
                    cipher.init(Cipher.DECRYPT_MODE, secretKey);

                    FileInputStream inputStream = new FileInputStream("dbinfor.ser");
                    CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);
                    ObjectInputStream objectInputStream = new ObjectInputStream(cipherInputStream);
                    MySQL db = (MySQL) objectInputStream.readObject();
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    connection = DriverManager.getConnection("jdbc:mysql://" + db.ip + ":" + db.port + "/" + db.dbname, db.un, db.pw);
                } else {
                    new SetupConnection().setVisible(true);
                }
            } catch (SQLException e) {
                throw new DatabaseException("Database connection failed: " + e.getMessage(), e,
                        String.valueOf(e.getErrorCode()), e.getSQLState());
            } catch (Exception e) {
                throw new DatabaseException("Failed to create database connection: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Gets the current database connection, creating one if it doesn't exist.
     *
     * @return the database connection
     * @throws DatabaseException if connection cannot be established
     */
    public static Connection getConnection() throws DatabaseException {
        createConnection();
        return connection;
    }

    /**
     * Executes a SELECT query and returns the result set.
     *
     * @param query the SQL SELECT query to execute
     * @return the result set containing query results
     * @throws DatabaseException if query execution fails
     */
    public static ResultSet executeSearch(String query) throws DatabaseException {
        try {
            createConnection();
            return connection.createStatement().executeQuery(query);
        } catch (SQLException e) {
            throw new DatabaseException("Query execution failed: " + e.getMessage(), e,
                    String.valueOf(e.getErrorCode()), e.getSQLState());
        }
    }

    /**
     * Executes an INSERT, UPDATE, or DELETE query and returns the number of
     * affected rows.
     *
     * @param query the SQL IUD (Insert/Update/Delete) query to execute
     * @return the number of rows affected by the query
     * @throws DatabaseException if query execution fails
     */
    public static Integer executeIUD(String query) throws DatabaseException {
        try {
            createConnection();
            return connection.createStatement().executeUpdate(query);
        } catch (SQLException e) {
            throw new DatabaseException("IUD operation failed: " + e.getMessage(), e,
                    String.valueOf(e.getErrorCode()), e.getSQLState());
        }
    }

    /**
     * Closes the current database connection.
     *
     * @throws DatabaseException if connection closing fails
     */
    public static void closeConnection() throws DatabaseException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to close database connection: " + e.getMessage(), e,
                    String.valueOf(e.getErrorCode()), e.getSQLState());
        }
    }

}
