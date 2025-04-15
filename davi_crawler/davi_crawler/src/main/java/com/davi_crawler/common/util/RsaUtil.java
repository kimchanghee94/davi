package com.davi_crawler.common.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaUtil {

    //행정구역 공개키 제작 하드코딩 테스트 메서드
    public static RSAPublicKey getMolitRoadPubKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String modulus = "9e0d940e0661d20690b322b7c205d7b1d29812cb57080b94b4e44d078297ace46371c5db021d2c0399cfdbd23ae83a54ca410c36f468fd39c2d93f51e9a840d0702508f6a2fa5eb622d0dab29475d8f68b1123e50e703abf65742300b2c6250f7bee286c9cf4fb7d757a43bc9ebbbf51de8880ed1b0d00bc1098785d6fcf780d";
        String exponent = "10001";

        // Convert the modulus and exponent to BigInteger objects
        BigInteger modulusBigInt = new BigInteger(modulus, 16);
        BigInteger exponentBigInt = new BigInteger(exponent, 16);

        // Create an RSAPublicKeySpec using the modulus and exponent
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulusBigInt, exponentBigInt);

        // Generate an RSAPublicKey from the public key spec
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);

        return publicKey;
    }

    //1024비트 RSA 키쌍을 생성
    public static KeyPair genRSAKeyPair() throws NoSuchAlgorithmException{
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(1024, new SecureRandom());
        return gen.genKeyPair();
    }

    //Public Key로 RSA 암호화를 수행
    public static String encryptRSA(String plainText, PublicKey publicKey)
    throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException,
            BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] bytePlain = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(bytePlain);
    }

    //Private Key로 RSA 복호화를 수행
    public static String decryptRSA(String encrypted, PrivateKey privateKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance("RSA");
        byte[] byteEncrypted = Base64.getDecoder().decode(encrypted.getBytes());

        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytePlain = cipher.doFinal(byteEncrypted);
        return new String(bytePlain, "utf-8");
    }

    public static PublicKey getPublicKeyFromBase64Encrypted(String base64PublicKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] decodedBase64PubKey = Base64.getDecoder().decode(base64PublicKey);

        return KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(decodedBase64PubKey));
    }

    public static PrivateKey getPrivateKeyFromBase64Encrypted(String base64PrivateKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] decodedBase64PrivateKey = Base64.getDecoder().decode(base64PrivateKey);

        return KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(decodedBase64PrivateKey));
    }
}