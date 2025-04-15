package com.davi_crawler.common.util;

import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Log4j2
@SpringBootTest
public class RsaUtilTests {

    @Test
    public void rsaEncDecTest() throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException, InvalidKeySpecException {
        // RSA 키쌍을 생성
        KeyPair keyPair = RsaUtil.genRSAKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        log.fatal("publicKey" + publicKey.toString());

        String plainText = "RSA Encryption test";

        // Base64 인코딩된 암호화 문자열
        String encrypted = RsaUtil.encryptRSA(plainText, publicKey);

        // 복호화
        String decrypted = RsaUtil.decryptRSA(encrypted, privateKey);

        log.error(plainText + ", " + decrypted + ", " + encrypted);

        Assert.assertEquals(plainText, decrypted);

        // 공개키를 Base64 인코딩한 문자일을 만듦
        byte[] bytePublicKey = publicKey.getEncoded();
        String base64PublicKey = Base64.getEncoder().encodeToString(bytePublicKey);

        // 개인키를 Base64 인코딩한 문자열을 만듦
        byte[] bytePrivateKey = privateKey.getEncoded();
        String base64PrivateKey = Base64.getEncoder().encodeToString(bytePrivateKey);

        // base64 암호화한 String 에서 Public Key 를 다시생성한후 암호화 테스트를 진행
        PublicKey rePublicKey = RsaUtil.getPublicKeyFromBase64Encrypted(base64PublicKey);
        String encryptedRe = RsaUtil.encryptRSA(plainText, rePublicKey);
        String decryptedRe = RsaUtil.decryptRSA(encryptedRe, privateKey);

        Assert.assertEquals(plainText, decryptedRe);

        // base64 암호화한 String 에서 Private Key 를 다시생성한후 복호화 테스트를 진행
        PrivateKey privateKeyRe = RsaUtil.getPrivateKeyFromBase64Encrypted(base64PrivateKey);
        String decryptedReRe = RsaUtil.decryptRSA(encryptedRe, privateKeyRe);

        Assert.assertEquals(decrypted, decryptedReRe);
    }
}
