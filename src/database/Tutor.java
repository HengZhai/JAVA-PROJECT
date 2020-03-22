package database;

/**
 * This class is mainly used to store and get the username of tutor.
 */
public class Tutor {
    private static String username;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Tutor.username = username;
    }
}
