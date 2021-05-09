import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

public class DatabaseHandler {
    private static DatabaseHandler instance;
    public static DatabaseHandler getInstance() {
        if (instance == null) {
            try {
                instance = new DatabaseHandler();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    Connection connection;
    public DatabaseHandler() throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:test.db");
    }

    public void registerUser(String email, byte[] certificate, String encryptedPassword, String salt, int gid) throws  Exception {
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.
        String query = String.format("insert into USUARIOS values('%s', '%s', '%s', '%s', NULL, NULL, '%s');", email, encryptedPassword, salt, new String(certificate), gid);
        statement.executeUpdate(query);
    }

    public UserState verifyUserEmail(String email) throws Exception {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(
            "SELECT * from USUARIOS WHERE email = '" + email + "';"
        );
        if (rs.next()) {
            Timestamp timestamp = rs.getTimestamp("timeout");
            if (timestamp != null) {
                Date date = new Date(timestamp.getTime());
                if (date.compareTo(new Date()) > 0) {
                    return UserState.BLOCKED;
                }
            }
            return UserState.VALID;
        } else {
            return UserState.INVALID;
        }
    }

    public String[] getPasswordAndSalt(String email) throws Exception {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from usuarios where email='"+email+"'");
        if(rs.next()) {
            String password = rs.getString("senha");
            String salt = rs.getString("salt");
            String[] value = { password, salt };
            return value;
        }
        throw new Exception("Email não encontrado");
    }

    public void registerAttempts(String email, boolean success) throws Exception {
        Statement statement = connection.createStatement();
        if(success) {
            statement.executeUpdate("UPTATE USUARIOS SET attepmts = 0 where email="+email);
        } else {
            ResultSet rs = statement.executeQuery("select * from person where email="+email);
            if(rs.next()) {
                int attempts = rs.getInt("attempts") + 1;
                if(attempts >= 3) {
                    statement.executeUpdate("UPDATE USUARIOS SET timeout = date('now',+2 minutes') where email="+email);
                }
                else {
                    statement.executeUpdate("UPDATE USUARIOS SET attempts = "+attempts+" where email="+email);
                }
            }
            throw new Exception("Email não encontrado");
        }
    }

    public static void main(String[] args) throws Exception {
        DatabaseHandler handler = new DatabaseHandler();
        handler.registerUser("th@2132.com", "oi".getBytes(), "123", "232", 0);
        // Statement statement = handler.connection.createStatement();
        // ResultSet rs = statement.executeQuery("select * from mensagens");
        // while(rs.next()) {
        //     System.out.println(rs.getInt("codigo"));
        //     System.out.println(rs.getString("mensagem"));
        //     // System.out.printf("%s %s %s %d %d\n", rs.getString("email"), rs.getString("senha"), rs.getString("salt"), rs.getInt("attempts"), rs.getInt("gid"));
        // }
    }
}
