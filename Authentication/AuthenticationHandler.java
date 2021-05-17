package Authentication;
import Database.DatabaseHandler;
import Utilities.LogHandler;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.security.cert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.cert.Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.DateTimeException;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
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

    public boolean verifyUserPrivateKey(byte[] privateKeyContent, String secretKey, String emailAddress) {
        PrivateKey privateKey;
        try {
            privateKey = this.privateKeyFromFile(privateKeyContent, secretKey.getBytes(StandardCharsets.UTF_8));
        }
        catch (Exception e) {
            LogHandler.logWithUser(4005);
            return false;
        }

        byte[] userCertificateContent = DatabaseHandler.getInstance().getEncodedCertificate(emailAddress);
        try {
            Certificate userCertificate = this.certificateFromFile(userCertificateContent);
            if (this.verifyPrivateKey(privateKey, userCertificate)) {
                DatabaseHandler.getInstance().registerAttempts(emailAddress, true);
                UserState.privateKey = privateKey;
                UserState.username = AuthenticationHandler.getUsernameFromCertificate(userCertificate);
                return true;
            } else {
                throw new Exception("Chave inválida");
            }
        } catch (Exception exc) {
            LogHandler.logWithUser(4006);
            return false;
        }
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

    public static void checkCertificate(Certificate cert, Optional<String> email)
        throws CertificateNotYetValidException, CertificateExpiredException, CertificateException {
        X509Certificate certNovo = (X509Certificate) cert;
        certNovo.checkValidity();

        if(email.isPresent()) {
            String subject = certNovo.getSubjectDN().getName();
            int emailIndex = subject.indexOf("EMAILADDRESS=")+13;
            String emailName = subject.substring(emailIndex, subject.indexOf(',', emailIndex));

            if(!email.get().equals(emailName)) {
                throw new CertificateException();
            }
        }
    }

    static String getUsernameFromCertificate(Certificate cert) {
        String fullName = ((X509Certificate)cert).getSubjectDN().getName();
        int usernameStart = fullName.indexOf("CN=") + 3;
        int usernameEnd = fullName.indexOf(",", usernameStart);

        return (String)fullName.subSequence(usernameStart, usernameEnd);
    }

    public static String[] getCertificateInfo(Certificate certificate) {
        X509Certificate cert = (X509Certificate)certificate;
        int version = cert.getVersion();
        BigInteger serialNumber = cert.getSerialNumber();
        Date notBefore = cert.getNotBefore();
        Date notAfter = cert.getNotAfter();
        String algorithm = cert.getSigAlgName();
        String nameIssuer = cert.getIssuerDN().getName();
        int issuerNameIndex = nameIssuer.indexOf("CN=")+3;
        String issuerName = nameIssuer.substring(issuerNameIndex, nameIssuer.indexOf(',', issuerNameIndex));

        String nameSubject = cert.getSubjectDN().getName();
        int nameIndex = nameSubject.indexOf("CN=")+3;
        String subjectName = nameSubject.substring(nameIndex, nameSubject.indexOf(',', nameIndex));
        int emailIndex = nameSubject.indexOf("EMAILADDRESS=")+13;
        String emailName = nameSubject.substring(emailIndex, nameSubject.indexOf(',', emailIndex));
        String[] r = {Integer.toString(version), serialNumber.toString(), notBefore.toString(), notAfter.toString(), algorithm, issuerName, subjectName, emailName};
        return r;
    }
    public static String getEmailFromCertificate(Certificate cert) {
        X509Certificate certNovo = (X509Certificate) cert;
        String fullName = ((X509Certificate)cert).getSubjectDN().getName();
        int emailIndex = fullName.indexOf("EMAILADDRESS=") + 13;
        int emailEnd = fullName.indexOf(",", emailIndex);

        return fullName.substring(emailIndex, emailEnd);
    }
}
