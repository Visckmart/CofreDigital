package General;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import Authentication.AuthenticationHandler;
import Authentication.UserState;
import Database.DatabaseHandler;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
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


    public byte[] decryptAndVerifyFile(String directory, String fileName) throws Exception {
        Path directoryPath = FileSystems.getDefault().getPath(directory);
        FileHandler fileHandler = new FileHandler();

        Path envelopePath = directoryPath.resolve(fileName + ".env");
        byte[] envelopeContent = Files.readAllBytes(envelopePath);
        SecretKey simmetricKey = fileHandler.decryptEnvelope(UserState.privateKey, envelopeContent);
        
        Path encryptedFilePath = directoryPath.resolve(fileName + ".enc");
        byte[] encryptedContent = Files.readAllBytes(encryptedFilePath);
        byte[] fileContent = fileHandler.decryptFile(simmetricKey, encryptedContent);

        Path signatureFilePath = directoryPath.resolve(fileName + ".asd");
        byte[] signatureContent = Files.readAllBytes(signatureFilePath);
        byte[] userCertificateContent = DatabaseHandler.getInstance().getEncodedCertificate(UserState.emailAddress);
        Certificate userCertificate = new AuthenticationHandler().certificateFromFile(userCertificateContent);
        boolean isAuthentic = fileHandler.verifyFileAuthenticity(signatureContent, fileContent, userCertificate);
        if (isAuthentic == false) {
            System.out.println("Autenticidade inválida.");
            throw new Exception("Erro na verificação de Autenticidade");
        }
        return fileContent;
    }
    //     try {
    //         byte[] enve = Files.readAllBytes(filePath);
    //         SecretKey sk = new FileHandler().decryptEnvelope(UserState.privateKey, enve);
    //         System.out.println(sk);
    //         byte[] arq = Files.readAllBytes(FileSystems.getDefault().getPath(fc.getSelectedFile().getAbsolutePath(), "index.enc"));
    //         byte[] fileContent = new FileHandler().decryptFile(sk, arq);
    //         byte[] asd = Files.readAllBytes(FileSystems.getDefault().getPath(fc.getSelectedFile().getAbsolutePath(), "index.asd"));
    //         boolean b = new FileHandler().verifyFileAuthenticity(asd, fileContent, new AuthenticationHandler().certificateFromFile(DatabaseHandler.getInstance().getEncodedCertificate(UserState.emailAddress)));
    //         System.out.println(b);

    //         System.out.println(StandardCharsets.UTF_8.decode(ByteBuffer.wrap(fileContent)));
    //         List<FileInfo> fileInfos = new IndexHandler().parseIndexContent(fileContent)
    // }
//

}
