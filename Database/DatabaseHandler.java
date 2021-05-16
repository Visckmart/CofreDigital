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
    private DatabaseHandler() throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:Database/database.db");
    }

    public void registerUser(String email, byte[] certificate, String encryptedPassword, String salt, int gid) throws  Exception {
        PreparedStatement statement = connection.prepareStatement("insert into USUARIOS(email, senha, salt, certificado, gid) values(?, ?, ?, ?, ?);");
        if(verifyUserEmail(email) != UserLoginState.INVALID) {
            throw new Exception("Usuário já existe!");
        }
        try {
            statement.setString(1, email);
            statement.setString(2, encryptedPassword);
            statement.setString(3, salt);
            statement.setBytes(4, certificate);
            statement.setInt(5, gid);
            statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            return;
        }
    }

    public UserLoginState verifyUserEmail(String email) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * from USUARIOS WHERE email =?");
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            UserState.emailAddress = email;

            if (rs.next()) {
                String dateString = rs.getString("timeout");
                rs.close();
                if (dateString != null) {
                    LocalDateTime timestamp = LocalDateTime.parse(dateString, TimestampFormatter);
                    if (timestamp.compareTo(LocalDateTime.now(ZoneId.of("UTC"))) > 0) {
                        LogHandler.logWithUser(2004);
                        UserState.emailAddress = null;
                        return UserLoginState.BLOCKED;
                    }
                }
                LogHandler.logWithUser(2003);
                return UserLoginState.VALID;
            } else {
                LogHandler.logWithUser(2005);
                UserState.emailAddress = null;
                return UserLoginState.INVALID;
            }
        } catch(Exception ignored) {
            return null;
        }
    }

    public void registerAccess(String email) {
        try {
            PreparedStatement statement = connection.prepareStatement("select * from USUARIOS where email=?");
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            int accesses = rs.getInt("accesses");
            statement = connection.prepareStatement("UPDATE USUARIOS SET accesses=?, timeout=null where email=?");
            statement.setInt(1, accesses + 1);
            statement.setString(2, email);
            statement.executeUpdate();
            statement.close();
        } catch (Exception ignored) {

        }
    }

    public void registerQuery(String email) {
        try {
            PreparedStatement statement = connection.prepareStatement("select * from USUARIOS where email=?");
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            int accesses = rs.getInt("queries");
            statement = connection.prepareStatement("UPDATE USUARIOS SET queries=?, timeout=null where email=?");
            statement.setInt(1, accesses + 1);
            statement.setString(2, email);
            statement.executeUpdate();
            statement.close();
        } catch (Exception ignored) {

        }
    }

    public void registerAttempts(String email, boolean success) {
        try {
            PreparedStatement statement = connection.prepareStatement("select * from USUARIOS where email=?");
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int attempts = rs.getInt("attempts") + 1;
                String dateString = rs.getString("timeout");
                rs.close();
                if (attempts >= 3 && !success) {
                    if (dateString != null) {
                        LocalDateTime timestamp = LocalDateTime.parse(dateString, TimestampFormatter);
                        if (timestamp.compareTo(LocalDateTime.now(ZoneId.of("UTC"))) < 0) {
                            statement = connection.prepareStatement("UPDATE USUARIOS SET attempts=1, timeout=null where email=?");
                        } else {
                            statement =
                                connection.prepareStatement(
                                    "UPDATE USUARIOS SET timeout = datetime('now','+2 minutes'), attempts=3 where email=?");
                        }
                        statement.setString(1, email);
                    } else {
                        statement =
                            connection.prepareStatement(
                                "UPDATE USUARIOS SET timeout = datetime('now','+2 minutes'), attempts=3 where email=?");
                        statement.setString(1, email);
                    }
                    statement.executeUpdate();
                } else if (attempts < 3) {
                    if (success) {
                        statement = connection.prepareStatement("UPDATE USUARIOS SET attempts = 0, timeout = null where email=?");
                        statement.setString(1, email);
                    } else {
                        statement = connection.prepareStatement("UPDATE USUARIOS SET attempts = ?, timeout = null where email=?");
                        statement.setInt(1, attempts);
                        statement.setString(2, email);
                    }
                    statement.executeUpdate();
                }
            } else {
                statement.close();
                rs.close();
                throw new Exception("Email não encontrado");
            }
            statement.close();
            rs.close();
        } catch(Exception ignored) {
        }
    }

    public String[] getPasswordAndSalt(String email) {
        try {
            PreparedStatement statement = connection.prepareStatement("select * from usuarios where email=?");
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                String password = rs.getString("senha");
                String salt = rs.getString("salt");
                rs.close();
                String[] value = { password, salt };
                return value;
            } else {
                return null;
            }
        } catch (Exception ignored) {
            return null;
        }
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
                String usuario = "<u><b>" + rs.getString("usuario") + "</b></u>";
                if (usuario != null) {
                    mensagem = mensagem.replace("<login_name>", usuario);
                }
                String arquivo = "<u><b>" + rs.getString("usuario") + "</b></u>";
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
            
            String[] registro = { adjustedDate.format(TimestampFormatter), "<html>" + mensagem + "</html>" };
            registros.add(registro);
        }
        rs.close();
        return registros;
    }

    public byte[] getEncodedCertificate(String emailAddress) {
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(
                "SELECT certificado from USUARIOS where email = '" + emailAddress + "'"
            );
            if (rs.next() == false) {
                return null;
            }
            String certificado = rs.getString("certificado");
            return certificado.getBytes();
        } catch (Exception ignored) {
            return null;
        }
    }

    public void updateUserCertificate(String emailAddress, byte[] certificate) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE USUARIOS SET certificado=? where email=?");
            statement.setBytes(1, certificate);
            statement.setString(2, emailAddress);
            statement.executeUpdate();
            statement.close();
        } catch (Exception ignored) {

        }
    }

    public void updateUserPassword(String emailAddress, String senha, String salt) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE USUARIOS SET senha=?, salt=? where email=?");
            statement.setString(1, senha);
            statement.setString(2, salt);
            statement.setString(3, emailAddress);
            statement.executeUpdate();
            statement.close();
        } catch (Exception ignored) {

        }
    }

    public void updateUserState(String emailAddress) {
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(
                "SELECT * from USUARIOS where email = '" + emailAddress + "'"
            );
            UserState.emailAddress = rs.getString("email");
            UserState.attempts = rs.getInt("attempts");
            UserState.group = UserGroup.values()[rs.getInt("gid")];
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
}
