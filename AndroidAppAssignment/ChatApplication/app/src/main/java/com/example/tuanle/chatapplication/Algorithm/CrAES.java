package com.example.tuanle.chatapplication.Algorithm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class CrAES {

    // Transformation
    private static final String transMode = "AES/CBC/PKCS5Padding";

    // Hash Function
    private static final String hashFunc = "SHA-256";

    // Initialization Vector
    private static final byte[] initVector =
            {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    // LOG.D Option
    private static final String logTAG = "AES";

    // One-way Hash Function
    private static byte[] messageDigest(final String password)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest md = MessageDigest.getInstance(hashFunc);

        byte[] pwdBytes = password.getBytes("UTF-8");
        md.update(pwdBytes, 0, pwdBytes.length);

        return md.digest();
    }

    // Generate Secret Key
    private static SecretKeySpec generateKey(byte[] digest, final String algorithm) {
        return new SecretKeySpec(digest, algorithm);
    }

    private static String encodedProcess(byte[] cipher) {
        //NO_WRAP means "\n at the end"
        return Base64.encodeToString(cipher, Base64.NO_WRAP);
    }

    private static byte[] encryptedProcess(final SecretKeySpec key, final byte[] iv, final byte[] message)
            throws GeneralSecurityException {
        final Cipher cipher = Cipher.getInstance(transMode);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        byte[] cipherText = cipher.doFinal(message);

        return cipherText;
    }

    // AES Encrypt
    public static String encryptAES(final String password, String message)
            throws GeneralSecurityException {
        try {
            // Secret Key
            final SecretKeySpec secretKey = generateKey(messageDigest(password), password);

            // Cipher Text
            byte[] cipherText = encryptedProcess(secretKey, initVector, message.getBytes("UTF-8"));

            // Cipher Text Encoded
            String cipherEncoded = encodedProcess(cipherText);
            return cipherEncoded;
        }
        catch (UnsupportedEncodingException e) {
            if (false)
                Log.e(logTAG, "UnsupportedEncodingException ", e);
            throw new GeneralSecurityException(e);
        }
    }

    private static byte[] decodedProcess(String encodedCipher) {
        //NO_WRAP means "\n at the end"
        return Base64.decode(encodedCipher, Base64.NO_WRAP);
    }

    public static byte[] decryptedProcess(final SecretKeySpec key, final byte[] iv, final byte[] decodedCipherText)
            throws GeneralSecurityException {
        final Cipher cipher = Cipher.getInstance(transMode);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        byte[] decryptedText = cipher.doFinal(decodedCipherText);

        return decryptedText;
    }

    public static String decryptAES(final String password, String base64EncodedCipherText)
            throws GeneralSecurityException {

        try {
            final SecretKeySpec secretKey = generateKey(messageDigest(password), password);

            byte[] cipherDecoded = decodedProcess(base64EncodedCipherText);

            byte[] decrytedText = decryptedProcess(secretKey, initVector, cipherDecoded);

            String plainText = new String(decrytedText, "UTF-8");

            return plainText;
        } catch (UnsupportedEncodingException e) {
            if (false)
                Log.e(logTAG, "UnsupportedEncodingException ", e);
            throw new GeneralSecurityException(e);
        }
    }

    private CrAES() {
      
    }
}
