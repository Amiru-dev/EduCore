package lk.educore.schoolms.util;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import lk.educore.schoolms.model.MySQL;

/**
 * Utility class for BCrypt password hashing, verification, and migration.
 *
 * @author EduCore Software Team
 */
public class PasswordUtil {

    private static final Logger LOGGER = Logger.getLogger(PasswordUtil.class.getName());
    private static final int BCRYPT_LOG_ROUNDS = 12;

    /**
     * Hash a plain text password using BCrypt with standard cost factor.
     *
     * @param plainPassword plain text password
     * @return BCrypt hashed string
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_LOG_ROUNDS));
    }

    /**
     * Check whether candidate plaintext password matches a stored password.
     * Handles BCrypt hashes as well as legacy plaintext passwords.
     *
     * @param candidatePassword candidate plain text password
     * @param storedPassword    hash or plaintext stored in database
     * @return true if password matches
     */
    public static boolean checkPassword(String candidatePassword, String storedPassword) {
        if (candidatePassword == null || storedPassword == null) {
            return false;
        }

        if (isHashed(storedPassword)) {
            try {
                return BCrypt.checkpw(candidatePassword, storedPassword);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error validating BCrypt hash", e);
                return false;
            }
        }

        // Fallback for legacy unhashed passwords
        return candidatePassword.equals(storedPassword);
    }

    /**
     * Determine if a stored password string is already a BCrypt hash.
     *
     * @param password string to check
     * @return true if format matches standard BCrypt hash ($2a$, $2b$, $2y$)
     */
    public static boolean isHashed(String password) {
        if (password == null) {
            return false;
        }
        return (password.startsWith("$2a$") || password.startsWith("$2b$")
                || password.startsWith("$2y$") || password.startsWith("$2x$"))
                && password.length() >= 59;
    }

    /**
     * Check whether a stored password needs to be re-hashed (e.g. legacy plain text).
     *
     * @param storedPassword password from database
     * @return true if password is not a BCrypt hash
     */
    public static boolean needsRehash(String storedPassword) {
        return !isHashed(storedPassword);
    }

    /**
     * Scan the database (user and teacher tables) and hash any plain-text passwords.
     * This ensures all existing records in the database are converted to secure BCrypt hashes.
     */
    public static void migrateDatabasePasswords() {
        try {
            Connection conn = MySQL.getConnection();
            if (conn == null || conn.isClosed()) {
                return;
            }

            // 1. Migrate user table
            try (PreparedStatement selectUsers = conn.prepareStatement("SELECT user_id, password FROM user");
                 ResultSet rs = selectUsers.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt("user_id");
                    String currentPw = rs.getString("password");
                    if (currentPw != null && !isHashed(currentPw)) {
                        String hashed = hashPassword(currentPw);
                        try (PreparedStatement update = conn.prepareStatement(
                                "UPDATE user SET password = ? WHERE user_id = ?")) {
                            update.setString(1, hashed);
                            update.setInt(2, userId);
                            update.executeUpdate();
                        }
                        LOGGER.log(Level.INFO, "Migrated user ID {0} password to BCrypt", userId);
                    }
                }
            }

            // 2. Migrate teacher table
            try (PreparedStatement selectTeachers = conn.prepareStatement("SELECT teacher_id, password FROM teacher");
                 ResultSet rs = selectTeachers.executeQuery()) {
                while (rs.next()) {
                    int teacherId = rs.getInt("teacher_id");
                    String currentPw = rs.getString("password");
                    if (currentPw != null && !isHashed(currentPw)) {
                        String hashed = hashPassword(currentPw);
                        try (PreparedStatement update = conn.prepareStatement(
                                "UPDATE teacher SET password = ? WHERE teacher_id = ?")) {
                            update.setString(1, hashed);
                            update.setInt(2, teacherId);
                            update.executeUpdate();
                        }
                        LOGGER.log(Level.INFO, "Migrated teacher ID {0} password to BCrypt", teacherId);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to run password migration: " + e.getMessage(), e);
        }
    }
}
