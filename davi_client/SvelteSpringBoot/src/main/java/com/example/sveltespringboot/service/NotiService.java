package com.example.sveltespringboot.service;

import com.example.sveltespringboot.entity.NotiEntity;
import com.example.sveltespringboot.repository.NotiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotiService {
    @Autowired
    private NotiRepository notiRepository;

    public void write(NotiEntity notiEntity) {
        notiRepository.save(notiEntity);
    }

    public List<NotiEntity> getListNoti(){
        return notiRepository.findAll();
    }
}
