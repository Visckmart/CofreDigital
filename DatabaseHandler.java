import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseHandler {
    public static void main(String[] args) throws Exception {
        Connection c = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:test.db");
        System.out.println("Opened database successfully");
    }
}
