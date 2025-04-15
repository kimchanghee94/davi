package com.davi_crawler.service;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Log4j2
public class Outer {

    @Test
    @Transactional
    public void method1() throws Exception{
        Inner in = new Inner();
        log.info("==== outerMethod start ====");
        log.info("==== outerMethod transaction Active : {}",
                TransactionSynchronizationManager.isActualTransactionActive());
        in.method2();
        log.info("==== outerMethod end ====");
    }
}
