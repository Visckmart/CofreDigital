import java.util.ArrayList;
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

    static List<List<String>> generatePasswordCombinations(List<List<String>> gruposDigitados) {
        System.out.println(gruposDigitados.size());
        if (gruposDigitados.size() == 1) {
            return gruposDigitados;
        } else if (gruposDigitados.size() == 2) {
            List<List<String>> r = new ArrayList<List<String>>();
            ArrayList<String> temp = new ArrayList<String>();
            List<List<String>> slice = gruposDigitados.subList(1, gruposDigitados.size());
            List<String> combs = generatePasswordCombinations(slice).get(0);
            for (String fonema: gruposDigitados.get(0)) {
                ArrayList<String> newList = new ArrayList<String>();
                for (String string : combs) {
                    temp.add(fonema + string);
                    // newList.add(string);
                }
                // temp.add(newList);
            }
            System.out.println(temp);
            r.add(temp);
            return r;
        } else {
            List<List<String>> temp = new ArrayList<List<String>>();
            List<List<String>> slice = gruposDigitados.subList(1, gruposDigitados.size());
            temp.add(gruposDigitados.get(0));
            for (List<String> list : generatePasswordCombinations(slice)) {
                temp.add(list);
            }
            return generatePasswordCombinations(temp);
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

        System.out.println(generatePasswordCombinations(temp));
        // System.out.println("a");
    }
    static boolean checkPhoneticPassword(List<List<String>> gruposFoneticosDigitados, String emailAddress) {
        return true;
    }
}
