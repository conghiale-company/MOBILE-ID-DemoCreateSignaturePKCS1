package org.example;

import java.security.*;
import java.util.Base64;

public class RSASignatureExample {
    public static void run(String dataToBeSign) throws Exception {
        // Tạo cặp khóa RSA
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        // Dữ liệu cần ký
//        String data = "This is some data to sign";
        byte[] dataBytes = dataToBeSign.getBytes("UTF8");

        // Tạo chữ ký bằng khóa RSA
        Signature rsa = Signature.getInstance("SHA256withRSA");
        rsa.initSign(privateKey);
        rsa.update(dataBytes);
        byte[] signatureBytes = rsa.sign();

        // In chữ ký dưới dạng Base64
        System.out.println("RSA Signature: " + Base64.getEncoder().encodeToString(signatureBytes));
        System.out.println("Private Key: " + Base64.getEncoder().encodeToString(privateKey.getEncoded()));
        System.out.println("Public Key: " + Base64.getEncoder().encodeToString(publicKey.getEncoded()));

        // Xác minh chữ ký
        rsa.initVerify(publicKey);
        rsa.update(dataBytes);
        boolean isVerified = rsa.verify(signatureBytes);
        System.out.println("Signature Verified: " + isVerified);
    }
}

