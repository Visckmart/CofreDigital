package Utilities;
import java.sql.Statement;

import Authentication.UserState;
import Database.DatabaseHandler;

public class LogHandler {
    
    public static void logWithUserAndFile(int codigo, String fileName) {
        String login_name = UserState.emailAddress == null ? "NULL" : "'"+UserState.emailAddress+"'";
        String file_name = fileName == null ? "NULL" : "'"+fileName+"'";
        
        try {
            // System.out.println(new Date().getTime());
            DatabaseHandler dbHandler = DatabaseHandler.getInstance();
            Statement statement = dbHandler.connection.createStatement();
            statement.setQueryTimeout(30);
            String query = String.format(
                "INSERT into REGISTROS (codigo, usuario, arquivo) values(%d, %s, %s);",
                codigo, login_name, file_name
            );
//            System.out.println(query);
            statement.executeUpdate(query);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public static void logWithUser(int codigo) {
        logWithUserAndFile(codigo, null);
    }

    public static void log(int codigo) {
        logWithUserAndFile(codigo, null);
    }
}
