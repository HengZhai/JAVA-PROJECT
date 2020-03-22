package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class is mainly used to connect the database and disconnect the database
 */
public class DatabaseConnection {

    public static Connection getConn() {
        String url = "jdbc:postgresql://mod-msc-sw1.cs.bham.ac.uk:5432/munich";
        String user = "munich";
        String password = "siu6w1tacv";
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, password);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void closeConn(Connection conn) {
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
