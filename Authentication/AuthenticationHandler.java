package Authentication;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.io.ByteArrayInputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Random;

public class AuthenticationHandler {
    private final Signature signature;
    private final SecureRandom rng;
    private final Cipher cipher;
    private final KeyGenerator keyGenerator;
    private final KeyFactory keyFactory;
    private final CertificateFactory certificateFactory;
    private final Base64.Decoder decoder;
    public AuthenticationHandler() throws Exception {
        signature = Signature.getInstance("SHA1withRSA");
        rng = SecureRandom.getInstance("SHA1PRNG");
        cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        keyGenerator = KeyGenerator.getInstance("DES");
        keyFactory = KeyFactory.getInstance("RSA");
        decoder = Base64.getDecoder();
        certificateFactory = CertificateFactory.getInstance("X.509");
    }

    public boolean verifyPrivateKey(PrivateKey privateKey, Certificate certificate) throws Exception {
        byte[] array = new byte[2048];
        new Random().nextBytes(array);

        signature.initSign(privateKey);
        signature.update(array);
        byte[] signedArray = signature.sign();

        signature.initVerify(certificate.getPublicKey());
        signature.update(array);
        return signature.verify(signedArray);
    }

    public PrivateKey privateKeyFromFile(byte[] file, byte[] secretKey) throws Exception {
        rng.setSeed(secretKey);
        keyGenerator.init(56, rng);
        Key decryptKey = keyGenerator.generateKey();
        cipher.init(Cipher.DECRYPT_MODE, decryptKey);

        String fileContent = new String(cipher.doFinal(file));
        String publicKeyPEM = fileContent
            .replace("\n", "")
            .replace("\r", "")
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "");

        byte[] decoded = decoder.decode(publicKeyPEM.getBytes());
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decoded));
    }

    public Certificate certificateFromFile(byte[] file) throws Exception {
        String fileString = new String(file);
        int beginIndex = fileString.indexOf("-----BEGIN CERTIFICATE-----") + "-----BEGIN CERTIFICATE-----".length();
        int endIndex = fileString.indexOf("-----END CERTIFICATE-----");
        String encodedCertificate = fileString.substring(beginIndex, endIndex)
            .replace("\n", "")
            .replace("\r", "");
        byte[] decodedCertificate = decoder.decode(encodedCertificate);

        return certificateFactory.generateCertificate(new ByteArrayInputStream(decodedCertificate));
    }

//    public static void main(String[] args) throws Exception {
//        byte[] fileContent = Files.readAllBytes(Paths.get("./Pacote-T4/Keys/user01-pkcs8-des.key"));
//        AuthenticationHandler handler = new AuthenticationHandler();
//        PrivateKey privateKey = handler.privateKeyFromFile(fileContent, "user01".getBytes());
//
//        byte[] certificateContent = Files.readAllBytes(Paths.get("./Pacote-T4/Keys/user01-x509.crt"));
//        Certificate certificate = handler.certificateFromFile(certificateContent);
//        System.out.println(handler.verifyPrivateKey(privateKey, certificate));
//    }
}
