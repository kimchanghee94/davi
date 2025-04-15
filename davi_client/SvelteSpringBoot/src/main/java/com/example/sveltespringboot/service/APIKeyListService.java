package com.example.sveltespringboot.service;

import com.example.sveltespringboot.entity.APIColumnListEntity;
import com.example.sveltespringboot.repository.APIKeyListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class APIKeyListService {
    @Autowired
    private APIKeyListRepository apiKeyListRepository;

    public void insert(APIColumnListEntity apiColumnListEntity){
        apiKeyListRepository.save(apiColumnListEntity);
    }

    public List<APIColumnListEntity> selectList(APIColumnListEntity apiColumnListEntity){
        List<Long> apiId = Arrays.asList(apiColumnListEntity.getApiEntity().getApiId());
        return apiKeyListRepository.findAllById(apiId);
    }
}
