package Authentication;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import Database.DatabaseHandler;
import Utilities.LogHandler;
import Utilities.UserLoginState;

public class PasswordHandler {

    public static String generateSalt() {
        String salt = "";
        
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            int charCode = rand.nextInt(62);
            if (charCode < 26) {
                salt += Character.toString((char)(charCode + 65));
            } else if (charCode < 52) {
                salt += Character.toString((char)(charCode - 26 + 97));
            } else {
                salt += Character.toString((char)(charCode - 52 + 48));
            }
        }
        
        assert salt.length() == 10 : "Tamanho do salt está incorreto.";
        return salt;
    }
    
    private static String byteArrayToHexString(byte[] bytes) {
        StringBuffer hexString = new StringBuffer();
        for(int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0x0100 + (bytes[i] & 0x00FF)).substring(1);
            hexString.append((hex.length() < 2 ? "0" : "") + hex);
        }
        return hexString.toString();
    }
    
    public static String encodePassword(String password, String salt) {
        assert password != null && salt != null : "Argumentos insucifientes para criptografar senha.";
        assert password.length() > 0 && salt.length() > 0 : "Argumentos inválidos para criptografar senha.";
        
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update((password + salt).getBytes());
            byte[] digest = messageDigest.digest();
            String hexDigest = byteArrayToHexString(digest);
            return hexDigest;

        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private static List<List<String>> generatePasswordCombinations(List<List<String>> gruposDigitados) {
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

    public static boolean checkPhoneticPassword(List<List<String>> gruposFoneticosDigitados, String emailAddress) {
        // System.out.println("IGNORANDO TECLADO FONETICO");
        // if (true) return true;
        String[] pwdAndSalt = DatabaseHandler.getInstance().getPasswordAndSalt(emailAddress);
        String passwordHash = pwdAndSalt[0];
        String salt = pwdAndSalt[1];
        System.out.println(passwordHash + " | " + salt);

        List<String> allCombinations = generatePasswordCombinations(gruposFoneticosDigitados).get(0);
        for (String phoneticCombination : allCombinations) {
            String encodedPassword = PasswordHandler.encodePassword(phoneticCombination, salt);
            if (encodedPassword.equals(pwdAndSalt[0])) {
                DatabaseHandler.getInstance().registerAttempts(emailAddress, true);
                LogHandler.logWithUser(3003);
                return true;
            }
        }
        DatabaseHandler instance = DatabaseHandler.getInstance();
        instance.registerAttempts(emailAddress, false);
        instance.updateUserState(UserState.emailAddress);
        int attempts = UserState.attempts;
        LogHandler.logWithUser(3003 + attempts);
        if(instance.verifyUserEmail(UserState.emailAddress) == UserLoginState.BLOCKED) {
            LogHandler.logWithUser(3007);
        }
        return false;
    }
}
