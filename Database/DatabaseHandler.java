package Database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private DateTimeFormatter TimestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public DatabaseHandler() throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:Database/test.db");
    }

    public void registerUser(String email, byte[] certificate, String encryptedPassword, String salt, int gid) throws  Exception {
        Statement statement = connection.createStatement();
        if(verifyUserEmail(email) != UserState.INVALID) {
            throw new Exception("Usuário já existe!");
        }
        String query = String.format("insert into USUARIOS values('%s', '%s', '%s', '%s', NULL, NULL, '%s');", email, encryptedPassword, salt, new String(certificate), gid);
        statement.executeUpdate(query);
        statement.close();
    }

    public UserState verifyUserEmail(String email) throws Exception {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(
            "SELECT * from USUARIOS WHERE email = '" + email + "';"
        );
        if (rs.next()) {
            String dateString = rs.getString("timeout");
            rs.close();
            if (dateString != null) {
                LocalDateTime timestamp = LocalDateTime.parse(dateString, TimestampFormatter);
                if (timestamp.compareTo(LocalDateTime.now()) > 0) {
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


    public void registerAttempts(String email, boolean success) throws Exception {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from USUARIOS where email='"+email+"'");
        if(rs.next()) {
            int attempts = rs.getInt("attempts") + 1;
            rs.close();
            if(attempts >= 3 && !success) {
                statement.executeUpdate("UPDATE USUARIOS SET timeout = datetime('now','+2 minutes') where email='"+email+"'");
            }
            else if(attempts < 3) {
                if(success) {
                    statement.executeUpdate("UPDATE USUARIOS SET attempts = 0, timeout = null where email='"+email+"'");
                } else {
                    statement.executeUpdate("UPDATE USUARIOS SET attempts = "+attempts+", timeout = null where email='"+email+"'");
                }
            }
        }
        else {
            rs.close();
            throw new Exception("Email não encontrado");
        }
    }

    public String[] getPasswordAndSalt(String email) throws Exception {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from usuarios where email='"+email+"'");
        if(rs.next()) {
            String password = rs.getString("senha");
            String salt = rs.getString("salt");
            rs.close();
            String[] value = { password, salt };
            return value;
        }
        throw new Exception("Email não encontrado");
    }

    public List<String[]> getAllRegisters() throws Exception {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(
            "SELECT * from REGISTROS " + 
            "INNER JOIN Mensagens on Mensagens.codigo = Registros.codigo"
        );
        ArrayList<String[]> registros = new ArrayList<String[]>();
        while (rs.next()) {
            String mensagem = rs.getString("mensagem");
            if (mensagem != null) {
                String usuario = rs.getString("usuario");
                if (usuario != null) {
                    mensagem = mensagem.replace("<login_name>", usuario);
                }
                String arquivo = rs.getString("usuario");
                if (arquivo != null) {
                    mensagem = mensagem.replace("<arq_name>", arquivo);
                }
            } else {
                mensagem = "ERRO";
            }
            LocalDateTime date = LocalDateTime.parse(
                rs.getString("timestamp"),
                TimestampFormatter
            );
            ZonedDateTime originalDate = ZonedDateTime.of(date, ZoneId.of("UTC"));
            ZonedDateTime adjustedDate = originalDate.withZoneSameInstant(ZoneId.of("UTC-3"));
            
            String[] registro = { adjustedDate.format(TimestampFormatter), mensagem };
            registros.add(registro);

            // System.out.println(rs.getString("timestamp"));
            // System.out.println(rs.getString("codigo"));
            // System.out.println(rs.getString("usuario"));
            // System.out.println(rs.getString("arquivo"));
        }
        rs.close();
        return registros;
    }

    public String getEncodedCertificate(String emailAddress) throws Exception {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(
            "SELECT certificado from USUARIOS where email = '" + emailAddress + "'"
        );
        
        return rs.getString("certificado");
    }
    // public static void main(String[] args) throws Exception {
    //     DatabaseHandler handler = new DatabaseHandler();
    //     handler.registerUser("th@2132.com", "oi".getBytes(), "123", "232", 0);
        // Statement statement = handler.connection.createStatement();
        // ResultSet rs = statement.executeQuery("select * from mensagens");
        // while(rs.next()) {
        //     System.out.println(rs.getInt("codigo"));
        //     System.out.println(rs.getString("mensagem"));
        //     // System.out.printf("%s %s %s %d %d\n", rs.getString("email"), rs.getString("senha"), rs.getString("salt"), rs.getInt("attempts"), rs.getInt("gid"));
        // }
    // }
}
