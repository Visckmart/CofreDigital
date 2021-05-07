import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.Random;

class PasswordHandler {
    static String generateSalt() {
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
    
    static Optional<String> encodePassword(String password, String salt) {
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
}