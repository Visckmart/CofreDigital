package Utilities;
import java.sql.Statement;
import java.util.Date;

import Authentication.PasswordHandler;
import Database.DatabaseHandler;

public class LogHandler {
    
    public static void log(int codigo, String emailAddress) {
        String login_name = emailAddress == null ? "NULL" : "'"+emailAddress+"'";
        try {
            System.out.println(new Date().getTime());
            DatabaseHandler dbHandler = DatabaseHandler.getInstance();
            Statement statement = dbHandler.connection.createStatement();
            statement.setQueryTimeout(30);
            String query = String.format(
                "INSERT into REGISTROS (hash, usuario, codigo) values('%s', %s, %d);",
                PasswordHandler.generateSalt(), login_name, codigo
            );
            System.out.println(query);
            statement.executeUpdate(query);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    static void log(int codigo) {
        log(codigo, null);
    }
}
