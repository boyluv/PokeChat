package com.example.tuanle.chatapplication.Algorithm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public final class CrDES {

    // Transformation
    private final static String transMode = "DES/CBC/PKCS5Padding";

    // Hash Function
    private static final String hashFunc = "SHA1PRNG";

    // Initialization Vector
    private static final byte[] initVector = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    // LOG.D Option
    private static final String logTAG = "DES";

    private static Key getRawKey(String key) throws Exception {
        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        return keyFactory.generateSecret(dks);
    }

    public static String encryptDES(String key, String data) {
        byte[] a = encryptProcess(key, data.getBytes());
        final String b = encodedProcess(a);
        return b;
    }

    public static String decryptDES(String key, String data)
            throws GeneralSecurityException {
        try {
            byte[] decryptedText = decryptProcess(key, decodedProcess(data));
            String plainText = new String(decryptedText, "UTF-8");
            return plainText;
        }
        catch (UnsupportedEncodingException e) {
            if (false)
                Log.e(logTAG, "UnsupportedEncodingException ", e);
            throw new GeneralSecurityException(e);
        }
    }

    /*
    --------------------------------------------------
    Encoding & Decoding Process
    Using Base64
    Base64.NO_WRAP for "\n" at the end
    --------------------------------------------------
    */
    private static String encodedProcess(byte[] cipher) {
        //NO_WRAP means "\n at the end"
        return Base64.encodeToString(cipher, Base64.NO_WRAP);
    }

    private static byte[] decodedProcess(String encodedCipher) {
        //NO_WRAP means "\n at the end"
        return Base64.decode(encodedCipher, Base64.NO_WRAP);
    }

    /*
    --------------------------------------------------
    Encrypting & Decrypting Process
    Using "DES/CBC/PKCS5Padding" transformation
    --------------------------------------------------
    */
    private static byte[] encryptProcess(String key, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(transMode);
            IvParameterSpec iv = new IvParameterSpec(initVector);
            cipher.init(Cipher.ENCRYPT_MODE, getRawKey(key), iv);
            byte[] bytes = cipher.doFinal(data);
            return bytes;
        } catch (Exception e) {
            return null;
        }
    }

    private static byte[] decryptProcess(String key, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(transMode);
            IvParameterSpec iv = new IvParameterSpec(initVector);
            cipher.init(Cipher.DECRYPT_MODE, getRawKey(key), iv);
            byte[] decrytedText = cipher.doFinal(data);
            return decrytedText;
        } catch (Exception e) {
            return null;
        }
    }

    private CrDES() {

    }
}
