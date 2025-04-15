package com.davi_crawler.config.jasypt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JasyptApplicationTests {

    private final Logger logger = LogManager.getLogger(JasyptApplicationTests.class);

    @Test
    public void contextLoads() {
    }

    @Test
    public void jasypt() {
        String url = "jdbc:postgresql://X.X.X.X:PORT/davi";
        String username = "";
        String password = "";

        logger.error("TEST");
        logger.warn(jasyptEncoding(url));
        logger.info(jasyptEncoding(username));
        logger.debug(jasyptEncoding(password));
    }

    public String jasyptEncoding(String value) {

        String key = "#2023AB28443KE545222097ANNM4345";
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
        pbeEnc.setPassword(key);
        return pbeEnc.encrypt(value);
    }
}
