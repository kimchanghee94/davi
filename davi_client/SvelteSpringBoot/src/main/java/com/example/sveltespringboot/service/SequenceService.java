package com.example.sveltespringboot.service;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;

@Service
public class SequenceService {

    @Autowired
    private EntityManager entityManager;

    public Long getCurrentValueOfSequence(String sequenceName) {
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
}