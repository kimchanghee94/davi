package com.davi_crawler.service;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Log4j2
@SpringBootTest
public class Inner {

    @Test
    @Transactional
    public void method2(){
        log.info("==== innerMethod start ====");
        log.info("==== innerMethod transaction Active : {}",
                TransactionSynchronizationManager.isActualTransactionActive());
        log.info("==== innerMethod end ====");
    }
}
