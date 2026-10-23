package lk.educore.schoolms.model;

/**
 * UserSession Singleton
 * Holds the currently logged-in user's identity so that any panel
 * can determine who is logged in and what their role is.
 *
 * For Teacher users, the NIC is populated so that data queries
 * can be scoped to "my classes / my subjects" via the
 * teacher_class and teacher_subject tables.
 *
 * @author EduCore Software Team
 */
public class UserSession {

    private static UserSession instance;

    private int userId;
    private String username;
    private String role;           // "Admin" | "Teacher" | "Student"
    private String teacherNIC;     // null for non-teacher users
    private int teacherId;         // -1 for non-teacher users

    // Private constructor — use init() to create
    private UserSession() {
    }

    /**
     * Initialise (or re-initialise) the session after successful login.
     *
     * @param userId   the user.user_id from the database
     * @param username the login username
     * @param role     "Admin", "Teacher", or "Student"
     * @param teacherNIC teacher.NIC if the user is a Teacher, otherwise null
     * @param teacherId  teacher.teacher_id if the user is a Teacher, otherwise -1
     */
    public static void init(int userId, String username, String role,
                            String teacherNIC, int teacherId) {
        instance = new UserSession();
        instance.userId = userId;
        instance.username = username;
        instance.role = role;
        instance.teacherNIC = teacherNIC;
        instance.teacherId = teacherId;
    }

    /**
     * Get the current session. Returns null if no user is logged in.
     */
    public static UserSession get() {
        return instance;
    }

    /**
     * Clear the session on logout.
     */
    public static void clear() {
        instance = null;
    }

    /**
     * Check whether the current user is an Admin.
     */
    public boolean isAdmin() {
        return "Admin".equals(role);
    }

    /**
     * Check whether the current user is a Teacher.
     */
    public boolean isTeacher() {
        return "Teacher".equals(role);
    }

    // ── Getters ──────────────────────────────────────────────

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public String getTeacherNIC() {
        return teacherNIC;
    }

    public int getTeacherId() {
        return teacherId;
    }
}
