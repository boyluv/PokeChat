package com.example.tuanle.chatapplication.Algorithm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public final class CrRSA {

    // Transformation
    private static final String cryptMode = "RSA";
    private static final String transMode = "RSA/NONE/OAEPWithSHA256AndMGF1Padding";

    // Initialization Vector
    private static final byte[] initVector =
            {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    // LOG.D Option
    private static final String logTAG = "AES";


    private static KeyPairGenerator kpg;
    private static KeyPair kp;
    public static PublicKey publicKey;
    public static PrivateKey privateKey;

    /*
    --------------------------------------------------
    Generate Key Pair
    Get Public & Private Key
    --------------------------------------------------
    */
    public static void generateKey()
            throws NoSuchAlgorithmException {
        kpg = KeyPairGenerator.getInstance(cryptMode);
        kpg.initialize(1024);
        kp = kpg.genKeyPair();
        publicKey = kp.getPublic();
        privateKey = kp.getPrivate();

    }

    public static String getPublicKey(PublicKey publicKey) {
        return ByteArrayToHex(publicKey.getEncoded());
    }

    public static String getPrivateKey(PrivateKey privateKey) {
        return ByteArrayToHex(privateKey.getEncoded());
    }

    /*
    --------------------------------------------------
    Hex & Byte Array Process
    HexToByteArray
    ByteArrayToHex
    --------------------------------------------------
    */
    private static String ByteArrayToHex(byte[] bytesArray){
        StringBuffer strBuffer = new StringBuffer(bytesArray.length * 2);
        int i = 0;
        while (i < bytesArray.length){
            int v = bytesArray[i] & 0xff;
            if (v < 16) strBuffer.append('0');
            strBuffer.append(Integer.toHexString(v));
            i++;
        }
        String getHexString = strBuffer.toString().toUpperCase();
        return getHexString;
    }

    private static byte[] HexToByteArray(String str) {
        byte[] bytesArray = new byte[str.length() / 2];
        int i = 0;
        while (i < bytesArray.length) {
            int index = i * 2;
            int v = Integer.parseInt(str.substring(index, index + 2), 16);
            bytesArray[i] = (byte)v;
            i++;
        }
        return bytesArray;
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
        return Base64.encodeToString(cipher, Base64.DEFAULT);
    }

    private static byte[] decodedProcess(String encodedCipher) {
        //NO_WRAP means "\n at the end"
        return Base64.decode(encodedCipher, Base64.DEFAULT);
    }

    /*
    --------------------------------------------------
    Encoding & Decoding Process
    Using Base64
    Base64.NO_WRAP for "\n" at the end
    --------------------------------------------------
    */
    private static PublicKey StringToPublicKey(String publicKey)
        throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicKeyBytes = HexToByteArray(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(cryptMode);
        PublicKey Key = keyFactory.generatePublic(keySpec);
        return Key;
    }

    private static PrivateKey StringToPrivateKey(String privateKey)
        throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] privateKeyBytes = HexToByteArray(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(cryptMode);
        PrivateKey Key = keyFactory.generatePrivate(keySpec);
        return Key;
    }

    /*
    --------------------------------------------------
    Encrypting & Decrypting Process
    Using "RSA/NONE/OAEPWithSHA256AndMGF1Padding" transformation
    --------------------------------------------------
    */
    public static byte[] encryptedProcess(final String plain, String publicKey)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {
        PublicKey Key = StringToPublicKey(publicKey);
        Cipher cipher = Cipher.getInstance(cryptMode);
        cipher.init(Cipher.ENCRYPT_MODE, Key);
        byte[] encryptedBytes = cipher.doFinal(plain.getBytes());
        return encryptedBytes;
    }

    public static byte[] decryptedProcess(final byte[] encryptedBytes, String privateKey)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        PrivateKey Key = StringToPrivateKey(privateKey);
        Cipher cipher = Cipher.getInstance(cryptMode);
        cipher.init(Cipher.DECRYPT_MODE, Key);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return decryptedBytes;
    }

    public static final String encryptRSA(String plainText, String publicKey)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] cipherBytes = encryptedProcess(plainText, publicKey);
        String cipherText = ByteArrayToHex(cipherBytes);
        return cipherText;
    }

    public static final String decryptRSA(String cipherText, String privateKey)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] cipherBytes = HexToByteArray(cipherText);
        String plainText = new String(decryptedProcess(cipherBytes, privateKey));
        return plainText;
    }

    private CrRSA() {

    }
}
