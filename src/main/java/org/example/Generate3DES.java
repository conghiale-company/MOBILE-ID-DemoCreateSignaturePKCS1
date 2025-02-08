package org.example;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

public class Generate3DES {
    public static void run() throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // Thêm BouncyCastle Provider
        Security.addProvider(new BouncyCastleProvider());

        // Tạo khóa 3DES
        KeyGenerator keyGen = KeyGenerator.getInstance("DESede", "BC");
        keyGen.init(168); // Khóa 168 bit cho 3DES
        SecretKey key = keyGen.generateKey();

        // In ra khóa 3DES
        byte[] keyBytes = key.getEncoded();
        System.out.println("3DES Key: " + bytesToHex(keyBytes));

        // Tạo KCV (Key Check Value)
        Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding", "BC");
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "DESede");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        byte[] zeroBytes = new byte[8]; // Chuỗi byte toàn số 0
        byte[] encrypted = cipher.doFinal(zeroBytes);

        // KCV là 3 byte đầu tiên của mã hóa
        byte[] kcv = new byte[3];
        System.arraycopy(encrypted, 0, kcv, 0, 3);
        System.out.println("KCV: " + bytesToHex(kcv));
    }

    // Hàm chuyển byte array thành hex string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
