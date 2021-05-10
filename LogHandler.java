import java.sql.Statement;

public class LogHandler {
    
    static void log(int codigo, String emailAddress) {
        String login_name = emailAddress == null ? "NULL" : "'"+emailAddress+"'";
        try {
            DatabaseHandler dbHandler = DatabaseHandler.getInstance();
            Statement statement = dbHandler.connection.createStatement();
            statement.setQueryTimeout(30);
            String query = String.format(
                "INSERT into REGISTROS (usuario, codigo) values(%s, '%d');",
                login_name, codigo
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
