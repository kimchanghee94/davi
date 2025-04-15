package com.example.sveltespringboot.controller;

import com.example.sveltespringboot.dto.APIDto;
import com.example.sveltespringboot.dto.APIParamDto;
import com.example.sveltespringboot.dto.APIServiceDto;
import com.example.sveltespringboot.service.APIService;
import com.example.sveltespringboot.service.APITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user-api")
public class APIController {

    @Autowired
    private APITestService apiTestService;
    @Autowired
    private APIService apiService;

    //http://localhost:5052/api/v1/user-api/select-api
    @RequestMapping(value="/select-api", method = RequestMethod.POST)
    public Map<String, Object> getSelectApi(@RequestBody APIParamDto apiParamDto) {
        return apiTestService.getSelectApi(apiParamDto);
    }

    //http://localhost:5052/api/v1/user-api/get-api-list
    @RequestMapping(value="/get-api-list", method = RequestMethod.POST)
    public Map<String, Object> getApiList(@RequestBody Map<String, Object> paramVo){
        System.out.println("Test for get api list" + paramVo);
        return apiService.getApiList((Integer)paramVo.get("listCnt"));
    }

    //http://localhost:5052/api/v1/user-api/get-api-service-list
    @RequestMapping(value="/get-api-service-list", method = RequestMethod.POST)
    public Map<String, Object> getApiServiceList(@RequestBody APIDto apiDto){
        System.out.println("Test for apiDto : \n" + apiDto);
        return apiService.getApiServiceList(apiDto);
    }

    //http://localhost:5052/api/v1/user-api/get-api-reqRes-list
    @RequestMapping(value="/get-api-reqRes-list", method = RequestMethod.POST)
    public Map<String, Object> getApiReqResList(@RequestBody APIServiceDto apiServiceDto){
        System.out.println("Test for ApiServiceDto : \n" + apiServiceDto);
        return apiService.getApiReqResList(apiServiceDto);
    }
}