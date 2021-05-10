package Database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Utilities.LogHandler;
import Utilities.UserState;

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

    public Connection connection;
    public DatabaseHandler() throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:Database/test.db");
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
                    LogHandler.log(2004, email);
                    return UserState.BLOCKED;
                }
            }
            LogHandler.log(2003, email);
            return UserState.VALID;
        } else {
            LogHandler.log(2005, email);
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

    public List<String[]> getAllRegisters() throws Exception {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from REGISTROS INNER JOIN Mensagens ON Mensagens.codigo = Registros.codigo");
        ArrayList<String[]> registros = new ArrayList<String[]>();
        while(rs.next()) {
            String usu = rs.getString("usuario");
            if (usu == null) {
                usu = "";
            }
            String arq = rs.getString("usuario");
            if (arq == null) {
                arq = "";
            }
            String msg = rs.getString("mensagem");
            msg = msg.replace("<login_name>", usu);
            msg = msg.replace("<arq_name>", arq);
            String[] x = {rs.getString("timestamp"), msg};
            registros.add(x);

            // System.out.println(rs.getString("timestamp"));
            // System.out.println(rs.getString("codigo"));
            // System.out.println(rs.getString("usuario"));
            // System.out.println(rs.getString("arquivo"));
        }
        return registros;
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
                    LogHandler.log(2004, email);
                    return UserState.BLOCKED;
                }
            }
            LogHandler.log(2003, email);
            return UserState.VALID;
        } else {
            LogHandler.log(2005, email);
            return UserState.INVALID;
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
