import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserLoginHandler {
    
    static boolean checkEmailAddress(String emailAddress) {
        Pattern p = Pattern.compile("[a-zA-Z0-9_]+@[a-zA-Z0-9_]+.[a-zA-Z0-9_]{2,64}");//. represents single character  
        Matcher m = p.matcher(emailAddress);  
        boolean b = m.matches();
        return b;
    }

    static List<List<String>> generatePasswordCombinations(List<List<String>> gruposDigitados) {
        int qtdGrupos = gruposDigitados.size();
        if (qtdGrupos == 1) {
            return gruposDigitados;
        }
        if (qtdGrupos == 2) {
            ArrayList<String> grupo = new ArrayList<String>();
            
            List<List<String>> slice = gruposDigitados.subList(1, qtdGrupos);
            List<String> combs = generatePasswordCombinations(slice).get(0);
            
            for (String fonema: gruposDigitados.get(0)) {
                for (String string : combs) {
                    grupo.add(fonema + string);
                }
            }

            List<List<String>> combinations = new ArrayList<List<String>>();
            combinations.add(grupo);
            return combinations;

        } else {
            List<List<String>> novosGrupos = new ArrayList<List<String>>();
            novosGrupos.add(gruposDigitados.get(0));

            List<List<String>> slice = gruposDigitados.subList(1, qtdGrupos);
            for (List<String> list : generatePasswordCombinations(slice)) {
                novosGrupos.add(list);
            }
            
            return generatePasswordCombinations(novosGrupos);
        }
    }

    public static void main(String[] args) throws Exception {
        List<List<String>> temp = new ArrayList<List<String>>();
        ArrayList<String> a = new ArrayList<String>();
        a.add("A");
        a.add("B");
        a.add("C");
        temp.add(a);
        ArrayList<String> b = new ArrayList<String>();
        b.add("D");
        b.add("E");
        b.add("F");
        temp.add(b);
        ArrayList<String> c = new ArrayList<String>();
        c.add("G");
        c.add("H");
        c.add("I");
        temp.add(c);
        ArrayList<String> d = new ArrayList<String>();
        d.add("J");
        d.add("K");
        d.add("L");
        temp.add(d);

        System.out.println(generatePasswordCombinations(temp));
        // System.out.println("a");
    }

    static boolean checkPhoneticPassword(List<List<String>> gruposFoneticosDigitados, String emailAddress) throws Exception {
        String[] pwdAndSalt = DatabaseHandler.getInstance().getPasswordAndSalt(emailAddress);
        System.out.println(pwdAndSalt[0] + " | " + pwdAndSalt[1]);
        for (String password : generatePasswordCombinations(gruposFoneticosDigitados).get(0)) {
            // System.out.println(password);
            Optional<String> encodedPassword = PasswordHandler.encodePassword(password, pwdAndSalt[1]);
            if (encodedPassword.get().equals(pwdAndSalt[0])) {
                System.out.println("Certa");
                return true;
            }
        }
        return false;
    }
}
