package com.example.sveltespringboot.service.impl;

import com.example.sveltespringboot.dto.APIParamDto;
import com.example.sveltespringboot.service.APITestService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("APITestService")
public class APITestTestServiceImpl implements APITestService {

    @Override
    public Map<String, Object> getSelectApi(APIParamDto apiParamDto) {
        Map<String,Object> result = new HashMap<>();

        if(apiParamDto.getSelectIdx() == 0){
            StringBuffer sb = new StringBuffer();
            String url = new String("https://api.vworld.kr/req/data");
            String apiKey = new String("?key=2BBB1BBD-C698-39F3-AD60-5A029B206D64");
            String domain = new String("&domain=http://localhost:5170");
            String dataForm = new String("&service=data&version=2.0&request=getfeature&format=json&size=1000&page=1&geometry=false&attribute=true&crs=EPSG:3857&geomfilter=BOX(13663271.680031825,3894007.9689600193,14817776.555251127,4688953.0631258525)&data=LT_C_ADSIDO_INFO");

            result.put("name", "국토교통부_교통링크");
            result.put("url", url);
            result.put("data", sb.append(apiKey).append(domain).append(dataForm));

            sb = new StringBuffer();
            result.put("resultUrl", sb.append(url).append(apiKey).append(domain).append(dataForm));
        }else if(apiParamDto.getSelectIdx() == 1){
        }else if(apiParamDto.getSelectIdx() == 2){
        }else if(apiParamDto.getSelectIdx() == 3){
        }else{
            System.out.println("Test FAILED");
            result.put("success", false);
            result.put("statusCode", "00");
            result.put("message", "Test failed");
            return result;
        }

        result.put("success", true);
        result.put("statusCode", "00");
        result.put("message", "Test perfect");

        return result;
    }
}
