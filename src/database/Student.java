package database;

/**
 * This class is mainly used to store and get the username of student.
 */
public class Student {
    private static String username;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Student.username = username;
    }
}
