package com.aspacelifetech.interviewservice.handlers;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.concurrent.CompletableFuture;

/**
 * @Author: Idris Ishaq
 * @Date: 28 Feb, 2024
 */

public class EncryptionDecryptionHandler {

    private static final String INSTANCE = "AES/ECB/PKCS5Padding";
    private static final byte[] AES_BYTES = "X0f172ns9ha0Xs4gX0f172ns9ha0Xs4g".getBytes();
    private static final String ALGORITHM = "AES";


    public static CompletableFuture<String> encrypt(String... values) {
        return encrypt(String.join("$.", values));
    }


    public static CompletableFuture<String> encrypt(String plainText) {
        CompletableFuture<String> future = new CompletableFuture<>();
        try {
            Cipher cipher = Cipher.getInstance(INSTANCE);
            StringBuilder sb = new StringBuilder();
            SecretKeySpec secretKey = new SecretKeySpec(AES_BYTES, ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] bytes = cipher.doFinal(plainText.getBytes());
            for (byte b : bytes) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            future.complete(sb.toString());
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    public static CompletableFuture<String> decrypt(String value) {
        CompletableFuture<String> future = new CompletableFuture<>();
        try {

            int length = (int) Math.ceil((double) value.length() / 2);
            byte[] result = new byte[length];
            for (int i = 0; i < length; i++) {
                int startIndex = i * 2;
                int endIndex = Math.min(startIndex + 2, value.length());
                String hex = value.substring(startIndex, endIndex);
                int intValue = Integer.parseInt(hex, 16);
                result[i] = (byte) intValue;
            }
            Cipher cipher = Cipher.getInstance(INSTANCE);
            SecretKeySpec secretKey = new SecretKeySpec(AES_BYTES, ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(result);
            future.complete(new String(decryptedBytes));
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        return future;
    }

}
