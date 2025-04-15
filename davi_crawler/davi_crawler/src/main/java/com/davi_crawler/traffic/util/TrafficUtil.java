package com.davi_crawler.traffic.util;

import com.davi_crawler.common.util.ConstantUtil;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class TrafficUtil {
    /**
     * 교통 Traffic API 형태에서 공통 파라미터 반환
     * @return 공통 파라미터 반환
     */
    public static Map<String, String> getTrafficParams(){
        Map<String, String> params = new HashMap<>();
        params.put("serviceKey", ConstantUtil.PDP_SERVICE_KEY);
        params.put("numOfRows", "1000");
        params.put("pageNo", "1");
        params.put("type", "json");

        return params;
    }
}
