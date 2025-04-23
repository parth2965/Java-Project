package Working;

public class Session {
    private static String userId; // StudentID or FacultyID
    private static String userRole; // "Student" or "Faculty"
    private static String userPassword; // Password entered at login

    public static void setUser(String id, String role, String password) {
        userId = id;
        userRole = role;
        userPassword = password;
    }

    public static String getUserId() {
        return userId;
    }

    public static String getUserRole() {
        return userRole;
    }

    public static String getUserPassword() {
        return userPassword;
    }

    public static boolean isStudent() {
        return "Student".equalsIgnoreCase(userRole);
    }

    public static boolean isFaculty() {
        return "Faculty".equalsIgnoreCase(userRole);
    }

    public static void clearSession() {
        userId = null;
        userRole = null;
        userPassword = null;
    }
}