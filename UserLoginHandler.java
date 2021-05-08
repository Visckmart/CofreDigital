import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserLoginHandler {
    
    static boolean checkEmailAddress(String emailAddress) {
        Pattern p = Pattern.compile("[a-zA-Z0-9_]+@[a-zA-Z0-9_]+.[a-zA-Z0-9_]{2,64}");//. represents single character  
        Matcher m = p.matcher(emailAddress);  
        boolean b = m.matches();
        return b;
    }

    static boolean checkPhoneticPassword(List<List<String>> gruposFoneticosDigitados, String emailAddress) {
        return true;
    }
}
