package com.example.sveltespringboot.service;

import com.example.sveltespringboot.dto.APIParamDto;

import java.util.Map;

public interface APITestService {
    Map<String, Object> getSelectApi(APIParamDto apiDto);
}
