package Database;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Authentication.PasswordHandler;
import Authentication.UserGroup;
import Authentication.UserState;
import Utilities.LogHandler;
import Utilities.UserLoginState;

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
    private final DateTimeFormatter TimestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public DatabaseHandler() throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:Database/test.db");
    }

    public void seedUsers() throws Exception {
        // Criando usuário padrão
        String userEmail = "user01@inf1416.puc-rio.br";
        if(verifyUserEmail(userEmail) == UserLoginState.INVALID) {
            byte[] certificate = Files.readAllBytes(Paths.get("./Pacote-T4/Keys/user01-x509.crt"));
            String password = PasswordHandler.encodePassword("BABABABA", "232").get();
            registerUser(userEmail, certificate, password, "232", 0);
        }
    }

    public void registerUser(String email, byte[] certificate, String encryptedPassword, String salt, int gid) throws  Exception {
        PreparedStatement statement = connection.prepareStatement("insert into USUARIOS(email, senha, salt, certificado, gid) values(?, ?, ?, ?, ?);");
        if(verifyUserEmail(email) != UserLoginState.INVALID) {
            throw new Exception("Usuário já existe!");
        }
        statement.setString(1, email);
        statement.setString(2, encryptedPassword);
        statement.setString(3, salt);
        statement.setBytes(4, certificate);
        statement.setInt(5, gid);
        statement.executeUpdate();
        statement.close();
    }

    public UserLoginState verifyUserEmail(String email) throws Exception {
        PreparedStatement statement = connection.prepareStatement("SELECT * from USUARIOS WHERE email =?");
        statement.setString(1, email);
        ResultSet rs = statement.executeQuery();

        if (rs.next()) {
            String dateString = rs.getString("timeout");
            rs.close();
            if (dateString != null) {
                LocalDateTime timestamp = LocalDateTime.parse(dateString, TimestampFormatter);
                if (timestamp.compareTo(LocalDateTime.now(ZoneId.of("UTC"))) > 0) {
                    LogHandler.logWithUser(2004);
                    return UserLoginState.BLOCKED;
                }
            }
            LogHandler.logWithUser(2003);
            return UserLoginState.VALID;
        } else {
            LogHandler.logWithUser(2005);
            return UserLoginState.INVALID;
        }
    }

    public void registerAccess(String email) throws Exception {
        PreparedStatement statement = connection.prepareStatement("select * from USUARIOS where email=?");
        statement.setString(1, email);
        ResultSet rs = statement.executeQuery();
        int accesses = rs.getInt("accesses");
        statement = connection.prepareStatement("UPDATE USUARIOS SET accesses=?, timeout=null where email=?");
        statement.setInt(1, accesses + 1);
        statement.setString(2, email);
        statement.executeUpdate();
        statement.close();
    }

    public void registerAttempts(String email, boolean success) throws Exception {
        PreparedStatement statement = connection.prepareStatement("select * from USUARIOS where email=?");
        statement.setString(1, email);
        ResultSet rs = statement.executeQuery();
        if(rs.next()) {
            int attempts = rs.getInt("attempts") + 1;
            String dateString = rs.getString("timeout");
            rs.close();
            if(attempts >= 3 && !success) {
                if (dateString != null) {
                    LocalDateTime timestamp = LocalDateTime.parse(dateString, TimestampFormatter);
                    if(timestamp.compareTo(LocalDateTime.now(ZoneId.of("UTC"))) < 0) {
                        statement = connection.prepareStatement("UPDATE USUARIOS SET attempts=?, timeout=null where email=?");
                        statement.setInt(1, 1);
                        statement.setString(2, email);
                    }
                    else {
                        statement = connection.prepareStatement("UPDATE USUARIOS SET timeout = datetime('now','+2 minutes'), attempts=3 where email=?");
                        statement.setString(1, email);
                    }
                } else {
                    statement = connection.prepareStatement("UPDATE USUARIOS SET timeout = datetime('now','+2 minutes'), attempts=3 where email=?");
                    statement.setString(1, email);
                }
                statement.executeUpdate();
            }
            else if(attempts < 3) {
                if(success) {
                    statement = connection.prepareStatement("UPDATE USUARIOS SET attempts = 0, timeout = null where email=?");
                    statement.setString(1, email);
                } else {
                    statement = connection.prepareStatement("UPDATE USUARIOS SET attempts = ?, timeout = null where email=?");
                    statement.setInt(1, attempts);
                    statement.setString(2, email);
                }
                statement.executeUpdate();
            }
        }
        else {
            statement.close();
            rs.close();
            throw new Exception("Email não encontrado");
        }
        statement.close();
        rs.close();
    }

    public String[] getPasswordAndSalt(String email) throws Exception {
        PreparedStatement statement = connection.prepareStatement("select * from usuarios where email=?");
        statement.setString(1, email);
        ResultSet rs = statement.executeQuery();
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

    public byte[] getEncodedCertificate(String emailAddress) throws Exception {
        System.out.println(emailAddress);
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(
            "SELECT certificado from USUARIOS where email = '" + emailAddress + "'"
        );
        if (rs.next() == false) {
            return null;
        }
        String certificado = rs.getString("certificado");
        return certificado.getBytes();
    }

    public void updateUserCertificate(String emailAddress, byte[] certificate)  throws Exception {
        PreparedStatement statement = connection.prepareStatement("UPDATE USUARIOS SET certificado=? where email=?");
        statement.setBytes(1, certificate);
        statement.setString(2, emailAddress);
        statement.executeUpdate();
        statement.close();
    }

    public void updateUserPassword(String emailAddress, String senha, String salt)  throws Exception {
        PreparedStatement statement = connection.prepareStatement("UPDATE USUARIOS SET senha=?, salt=? where email=?");
        statement.setString(1, senha);
        statement.setString(2, salt);
        statement.setString(3, emailAddress);
        statement.executeUpdate();
        statement.close();
    }

    public void updateUserState(String emailAddress) {
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(
                "SELECT * from USUARIOS where email = '" + emailAddress + "'"
            );
            UserState.emailAddress = rs.getString("email");
            UserState.attempts = rs.getInt("attempts");
            UserState.group = rs.getInt("gid") == 0 ? UserGroup.USER : UserGroup.ADMIN;
            UserState.accesses = rs.getInt("accesses");
            UserState.queries = rs.getInt("queries");
            rs.close();

            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * from USUARIOS");
            int rowCount=0;
            while(rs.next()) {
                rowCount++;
            }
            UserState.totalUsers = rowCount;
        } catch (Exception exc) {
            exc.printStackTrace();
            return;
        }
    }

    public static void main(String[] args) throws Exception {
         DatabaseHandler handler = new DatabaseHandler();
         handler.seedUsers();
         try {
            handler.registerUser("th@232.com", "oi".getBytes(), "123", "232", 0);
         } catch (Exception e) {
             System.out.println(e.getMessage());
         }
         handler.registerAttempts("th@232.com", true);
         // [123, 232]
         System.out.println(Arrays.toString(handler.getPasswordAndSalt("th@232.com")));
         // VALID (após os 2 minutos de espera)
         System.out.println(handler.verifyUserEmail("th@232.com"));
         handler.registerAttempts("th@232.com", false);
         handler.registerAttempts("th@232.com", false);
         handler.updateUserState("th@232.com");
         System.out.println(UserState.attempts);
         handler.registerAttempts("th@232.com", true);
         System.out.println(UserState.attempts);
         // VALID
         System.out.println(handler.verifyUserEmail("th@232.com"));
         handler.registerAttempts("th@232.com", false);
         handler.registerAttempts("th@232.com", false);
         // VALID
         System.out.println(handler.verifyUserEmail("th@232.com"));
         handler.registerAttempts("th@232.com", false);
         handler.updateUserState("th@232.com");
         System.out.println(UserState.attempts);
         // BLOCKED
         System.out.println(handler.verifyUserEmail("th@232.com"));
         handler.registerAttempts("th@232.com", true);
         // BLOCKED
         System.out.println(handler.verifyUserEmail("th@232.com"));

//         Statement statement = handler.connection.createStatement();
//         ResultSet rs = statement.executeQuery("select * from mensagens");
//         while(rs.next()) {
//             System.out.println(rs.getInt("codigo"));
//             System.out.println(rs.getString("mensagem"));
//             System.out.printf("%s %s %s %d %d\n", rs.getString("email"), rs.getString("senha"), rs.getString("salt"), rs.getInt("attempts"), rs.getInt("gid"));
//         }
//         rs.close();
    }
}
