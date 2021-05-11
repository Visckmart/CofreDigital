package Authentication;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import Database.DatabaseHandler;

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
    
    public static Optional<String> encodePassword(String password, String salt) {
        assert password != null && salt != null : "Argumentos insucifientes para criptografar senha.";
        assert password.length() > 0 && salt.length() > 0 : "Argumentos inválidos para criptografar senha.";
        
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update((password + salt).getBytes());
            byte[] digest = messageDigest.digest();
            String hexDigest = byteArrayToHexString(digest);
            return Optional.of(hexDigest);

        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace();
            return Optional.empty();
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

    public static boolean checkPhoneticPassword(List<List<String>> gruposFoneticosDigitados, String emailAddress) throws Exception {
        String[] pwdAndSalt = DatabaseHandler.getInstance().getPasswordAndSalt(emailAddress);
        String passwordHash = pwdAndSalt[0];
        String salt = pwdAndSalt[1];
        System.out.println(passwordHash + " | " + salt);

        List<String> allCombinations = generatePasswordCombinations(gruposFoneticosDigitados).get(0);
        for (String phoneticCombination : allCombinations) {
            Optional<String> encodedPassword = PasswordHandler.encodePassword(phoneticCombination, salt);
            if (encodedPassword.get().equals(pwdAndSalt[0])) { return true; }
        }
        return false;
    }
}