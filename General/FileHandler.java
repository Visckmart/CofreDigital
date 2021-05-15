package General;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import Authentication.AuthenticationHandler;
import Authentication.UserState;
import Database.DatabaseHandler;
import Utilities.LogHandler;

import java.io.IOException;
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


    public byte[] decryptAndVerifyFile(String directory, String fileName) throws IOException, InvalidKeyException, SignatureException,  Exception {
        Path directoryPath = FileSystems.getDefault().getPath(directory);
        FileHandler fileHandler;

        fileHandler = new FileHandler();

        byte[] envelopeContent, encryptedContent, signatureContent;
        try {
            Path envelopePath = directoryPath.resolve(fileName + ".env");
            envelopeContent = Files.readAllBytes(envelopePath);
            Path encryptedFilePath = directoryPath.resolve(fileName + ".enc");
            encryptedContent = Files.readAllBytes(encryptedFilePath);
            Path signatureFilePath = directoryPath.resolve(fileName + ".asd");
            signatureContent = Files.readAllBytes(signatureFilePath);
        } catch (Exception e) {
            if(fileName.equals("index")) {
                LogHandler.logWithUser(8004);
            }
            throw new IOException("Arquivo não encontrado");
        }


        byte[] fileContent;
        try {
            SecretKey simmetricKey = fileHandler.decryptEnvelope(UserState.privateKey, envelopeContent);

            fileContent = fileHandler.decryptFile(simmetricKey, encryptedContent);
        } catch (Exception e) {
            if (fileName.equals("index")) {
                LogHandler.logWithUser(8007);
            } else {
                LogHandler.logWithUserAndFile(8015, fileName);
            }
            throw new InvalidKeyException();
        }
        if (fileName.equals("index")) {
            LogHandler.logWithUser(8005);
        } else {
            LogHandler.logWithUserAndFile(8013, fileName);
        }

        try {
            byte[] userCertificateContent = DatabaseHandler.getInstance().getEncodedCertificate(UserState.emailAddress);
            Certificate userCertificate = new AuthenticationHandler().certificateFromFile(userCertificateContent);
            boolean isAuthentic = fileHandler.verifyFileAuthenticity(signatureContent, fileContent, userCertificate);
            if (!isAuthentic) {
                throw new Exception("Erro na verificação de Autenticidade");
            }

            if (fileName.equals("index")) {
                LogHandler.logWithUser(8006);
            } else {
                LogHandler.logWithUserAndFile(8014, fileName);
            }
            return fileContent;
        } catch (Exception e) {
            if(fileName.equals("index")) {
                LogHandler.logWithUser(8008);
            } else {
                LogHandler.logWithUserAndFile(8016, fileName);
            }
            throw new SignatureException();
        }
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
