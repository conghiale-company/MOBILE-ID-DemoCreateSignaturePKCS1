package org.example;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

    public class AESWithRSASignatureExample {
    public static void run(String dataToBeSign) throws Exception {
        // Tạo cặp khóa RSA
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        // Tạo khóa AES
        KeyGenerator aesKeyGen = KeyGenerator.getInstance("AES");
        aesKeyGen.init(128);
        SecretKey aesKey = aesKeyGen.generateKey();

        // Mã hóa khóa riêng RSA bằng AES
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encryptedPrivateKeyBytes = aesCipher.doFinal(privateKey.getEncoded());
        String encryptedPrivateKeyBase64 = Base64.getEncoder().encodeToString(encryptedPrivateKeyBytes);
        System.out.println("Encrypted RSA Private Key: " + encryptedPrivateKeyBase64);

        // Giải mã khóa riêng RSA bằng AES
        aesCipher.init(Cipher.DECRYPT_MODE, aesKey);
        byte[] decryptedPrivateKeyBytes = aesCipher.doFinal(encryptedPrivateKeyBytes);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decryptedPrivateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey decryptedPrivateKey = keyFactory.generatePrivate(keySpec);

        // Dữ liệu cần ký
//        String data = "This is some data to sign";
        byte[] dataBytes = dataToBeSign.getBytes("UTF8");

        // Tạo chữ ký bằng khóa riêng RSA
        Signature rsa = Signature.getInstance("SHA256withRSA");
        rsa.initSign(decryptedPrivateKey);
        rsa.update(dataBytes);
        byte[] signatureBytes = rsa.sign();
        String signatureBase64 = Base64.getEncoder().encodeToString(signatureBytes);
        System.out.println("RSA Signature: " + signatureBase64);

        // Xác minh chữ ký bằng khóa công khai RSA
        rsa.initVerify(publicKey);
        rsa.update(dataBytes);
        boolean isVerified = rsa.verify(signatureBytes);
        System.out.println("Signature Verified: " + isVerified);
    }
}

