import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;

public class FileHandler {
    private final Cipher rsaCipher;
    private final Cipher desCipher;
    private final Signature signature;
    public FileHandler() throws Exception {
        rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        signature = Signature.getInstance("SHA1withRSA");
    }

    public SecretKey decryptEnvelope(PrivateKey privateKey, byte[] envelope) throws Exception {
        rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedEnvelope = rsaCipher.doFinal(envelope);

        SecureRandom rng = SecureRandom.getInstance("SHA1PRNG");
        rng.setSeed(decryptedEnvelope);
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        keyGenerator.init(rng);
        return keyGenerator.generateKey();
    }


    public byte[] decryptFile(SecretKey secretKey, byte[] encryptedFile) throws Exception {
        desCipher.init(Cipher.DECRYPT_MODE, secretKey);
        return desCipher.doFinal(encryptedFile);
    }


    public boolean verifyFileAuthenticity(byte[] digitalSignature, byte[] fileContent, Certificate certificate) throws Exception {
        signature.initVerify(certificate.getPublicKey());
        signature.update(fileContent);
        return signature.verify(digitalSignature);
    }
//
//    public static void main(String[] args) throws Exception {
//        byte[] fileContent = Files.readAllBytes(Paths.get("./Pacote-T4/Keys/user01-pkcs8-des.key"));
//        AuthenticationHandler handler = new AuthenticationHandler();
//        PrivateKey privateKey = handler.privateKeyFromFile(fileContent, "user01".getBytes());
//
//        byte[] certificateContent = Files.readAllBytes(Paths.get("./Pacote-T4/Keys/user01-x509.crt"));
//        Certificate certificate = handler.certificateFromFile(certificateContent);
//
//
//        FileHandler fileHandler = new FileHandler();
//        byte[] envelope = Files.readAllBytes(Paths.get("./Pacote-T4/Files/index.env"));
//        SecretKey key = fileHandler.decryptEnvelope(privateKey, envelope);
//
//        byte[] encrypted = Files.readAllBytes(Paths.get("./Pacote-T4/Files/index.enc"));
//        byte[] file = fileHandler.decryptFile(key, encrypted);
//
//        byte[] signature = Files.readAllBytes(Paths.get("./Pacote-T4/Files/index.asd"));
//        System.out.println(fileHandler.verifyFileAuthenticity(signature, file, certificate));
//    }
}
