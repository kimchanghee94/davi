package com.davi_crawler.common.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
public class SequenceService {
    private final EntityManager entityManager;

    //현재 시퀀스 번호 가져오기
    public Long getCurrentValueOfSequence(String sequenceName) throws Exception{
        Session session = entityManager.unwrap(Session.class);
        session.doWork(connection -> {
            connection.prepareStatement("SELECT nextval('" + sequenceName + "')").execute();
        });

        String query = "SELECT currval('" + sequenceName + "')";
        Object result = entityManager.createNativeQuery(query).getSingleResult();

        Long curVal = ((Number) result).longValue() - 1;
        session.doWork(connection -> {
            connection.prepareStatement("SELECT setval('" + sequenceName + "', " + curVal + ")").execute();
        });

        return curVal;
    }

    //시퀀스 0번으로 초기화
    public void initSeqNumZero(String sequenceName) throws Exception{
        Session session = entityManager.unwrap(Session.class);

        //0으로 초기화 할 수 있도록 범위 설정
        session.doWork(connection -> {
            connection.prepareStatement("ALTER SEQUENCE " + sequenceName + " MINVALUE 0 START WITH 1").execute();
        });

        session.doWork(connection -> {
            connection.prepareStatement("SELECT setval('" + sequenceName + "', " + 0 + ")").execute();
        });
    }
}